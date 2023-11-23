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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.filter.AbstractFilter;
import org.apache.servicecomb.core.filter.EdgeFilter;
import org.apache.servicecomb.core.filter.FilterNode;
import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.samples.porter.user.api.SessionInfo;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class AuthHandler extends AbstractFilter implements EdgeFilter {
  private final UserServiceClient userServiceClient;

  // session expires in 10 minutes, cache for 1 seconds to get rid of concurrent scenarios.
  private Cache<String, String> sessionCache = CacheBuilder.newBuilder()
      .expireAfterAccess(30, TimeUnit.SECONDS)
      .build();

  @Autowired
  public AuthHandler(UserServiceClient userServiceClient) {
    this.userServiceClient = userServiceClient;
  }

  @Override
  public CompletableFuture<Response> onFilter(Invocation invocation, FilterNode nextNode) {
    if (invocation.getMicroserviceName().equals("user-service")
        && (invocation.getOperationName().equals("login")
        || (invocation.getOperationName().equals("getSession")))) {
      // login：return session id, set cookie by javascript
      return nextNode.onFilter(invocation);
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
          return nextNode.onFilter(invocation);
        } catch (Exception e) {
          return CompletableFuture.completedFuture(Response.failResp(new InvocationException(500, "", e.getMessage())));
        }
      }

      // In edge, handler is executed in reactively. Must have no blocking logic.
      CompletableFuture<SessionInfo> result = userServiceClient.getGetSessionOperation().getSession(sessionId);
      return result.whenComplete((info, e) -> {
        if (result.isCompletedExceptionally()) {
          throw new InvocationException(403, "", "session is not valid.");
        } else {
          if (info == null) {
            throw new InvocationException(403, "", "session is not valid.");
          }
          try {
            // session info stored in InvocationContext. Microservices can get it. 
            invocation.addContext("session-id", sessionId);
            String sessionInfoStr = JsonUtils.writeValueAsString(info);
            invocation.addContext("session-info", sessionInfoStr);
            sessionCache.put(sessionId, sessionInfoStr);
          } catch (Exception ee) {
            throw new InvocationException(500, "", ee.getMessage());
          }
        }
      }).thenCompose(info -> nextNode.onFilter(invocation));
    }
  }

  @Override
  public int getOrder() {
    return -1890;
  }

  @Override
  public String getName() {
    return "edge-auth";
  }
}
