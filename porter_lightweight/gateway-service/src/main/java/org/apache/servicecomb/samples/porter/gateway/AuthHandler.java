/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.samples.porter.gateway;

import java.util.concurrent.TimeUnit;

import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.provider.springmvc.reference.async.CseAsyncRestTemplate;
import org.apache.servicecomb.samples.porter.user.api.SessionInfo;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


public class AuthHandler implements Handler {
  private CseAsyncRestTemplate restTemplate = new CseAsyncRestTemplate();

  // session expires in 10 minutes, cache for 1 seconds to get rid of concurrent scenarios.
  private Cache<String, String> sessionCache = CacheBuilder.newBuilder()
      .expireAfterAccess(30, TimeUnit.SECONDS)
      .build();

  @Override
  public void handle(Invocation invocation, AsyncResponse asyncResponse) throws Exception {
    if (invocation.getMicroserviceName().equals("user-service")
        && (invocation.getOperationName().equals("login")
            || (invocation.getOperationName().equals("getSession")))) {
      // login：return session id, set cookie by javascript
      invocation.next(asyncResponse);
    } else {
      // check session
      String sessionId = invocation.getContext("session-id");
      if (sessionId == null) {
        throw new InvocationException(403, "", "session is not valid.");
      }

      String sessionInfo = sessionCache.getIfPresent(sessionId);
      if (sessionInfo != null) {
        try {
          // session info stored in InvocationContext. Microservices can get it. 
          invocation.addContext("session-id", sessionId);
          invocation.addContext("session-info", sessionInfo);
          invocation.next(asyncResponse);
        } catch (Exception e) {
          asyncResponse.complete(Response.failResp(new InvocationException(500, "", e.getMessage())));
        }
        return;
      }

      // In edge, handler is executed in reactively. Must have no blocking logic.
      ListenableFuture<ResponseEntity<SessionInfo>> sessionInfoFuture =
          restTemplate.getForEntity("cse://user-service/v1/user/session?sessionId=" + sessionId, SessionInfo.class);
      sessionInfoFuture.addCallback(
          new ListenableFutureCallback<ResponseEntity<SessionInfo>>() {
            @Override
            public void onFailure(Throwable ex) {
              asyncResponse.complete(Response.failResp(new InvocationException(403, "", "session is not valid.")));
            }

            @Override
            public void onSuccess(ResponseEntity<SessionInfo> result) {
              SessionInfo sessionInfo = result.getBody();
              if (sessionInfo == null) {
                asyncResponse.complete(Response.failResp(new InvocationException(403, "", "session is not valid.")));
                return;
              }
              try {
                // session info stored in InvocationContext. Microservices can get it. 
                invocation.addContext("session-id", sessionId);
                String sessionInfoStr = JsonUtils.writeValueAsString(sessionInfo);
                invocation.addContext("session-info", sessionInfoStr);
                invocation.next(asyncResponse);
                sessionCache.put(sessionId, sessionInfoStr);
              } catch (Exception e) {
                asyncResponse.complete(Response.failResp(new InvocationException(500, "", e.getMessage())));
              }
            }
          });
    }
  }
}
