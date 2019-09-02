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

package org.apache.servicecomb.samples.practise.houserush.gateway.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.definition.MicroserviceMeta;
import org.apache.servicecomb.provider.pojo.Invoker;
import org.apache.servicecomb.samples.practise.houserush.gateway.config.LoginUrlConfig;
import org.apache.servicecomb.samples.practise.houserush.gateway.rpc.UserApi;
import org.apache.servicecomb.samples.practise.houserush.gateway.rpc.po.User;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.InvocationType;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.apache.servicecomb.tracing.Span;

import java.util.concurrent.CompletableFuture;

public class AuthHandler implements Handler {

  private static UserApi userApi;
  private static LoginUrlConfig loginUrlConfig = new LoginUrlConfig();

  static {
    userApi = Invoker.createProxy("login", "userApiRest", UserApi.class);
  }

  @Override
  public void init(MicroserviceMeta microserviceMeta, InvocationType invocationType) {

  }

  @Span
  @Override
  public void handle(Invocation invocation, AsyncResponse asyncResp) throws Exception {
    //Internal call
    if (invocation.getRequestEx() == null) {
      invocation.next(asyncResp);
      return;
    }
    String token = invocation.getContext("Authorization");
    String method = invocation.getOperationMeta().getHttpMethod();
    String microsoerviceName = invocation.getMicroserviceName();
    String path = invocation.getOperationMeta().getOperationPath();
    String requestKey = method + " /" + microsoerviceName + path;
    if (loginUrlConfig.getNeedLoginUrlsSet().contains(requestKey)) {
      if (token == null) {
        asyncResp.consumerFail(new InvocationException(HttpStatus.SC_FORBIDDEN, "forbidden", "need authenticated"));
        return;
      }
//      CompletableFuture<User> res = userApi.verifyToken(token);
//      res.whenComplete((resUser, e) -> {
//        if (res.isCompletedExceptionally() || resUser == null) {
//          asyncResp.consumerFail(res.isCompletedExceptionally() ? e : new InvocationException(HttpStatus.SC_FORBIDDEN, "forbidden", "authenticated failed"));
//          return;
//        }
//        //add to clientRequest in CustomClientFilter
//        invocation.addContext("customerId", "" + resUser.getId());
//        invocation.addContext("newAuthorization", resUser.getToken());
//        try {
//          invocation.next(asyncResp);
//        } catch (Exception ex) {
//          asyncResp.consumerFail(new InvocationException(HttpStatus.SC_FORBIDDEN, "forbidden", "authenticated fail"));
//        }
//      });
      invocation.next(asyncResp);

    } else if (loginUrlConfig.getNoNeedLoginUrlsSet().contains(requestKey)) {
      if ("PUT /login/users/signin".equals(requestKey)) {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(invocation.getRequestEx().getBodyBytes(), User.class);
        CompletableFuture<User> res = userApi.signin(user);
        res.whenComplete((resUser, e) -> {
          if (res.isCompletedExceptionally() || resUser == null) {
            asyncResp.consumerFail(res.isCompletedExceptionally() ? e : new InvocationException(HttpStatus.SC_FORBIDDEN, "forbidden", "login failed"));
            return;
          }
          asyncResp.success(resUser);
        });
      } else {
        invocation.next(asyncResp);
      }

    } else {
      asyncResp.consumerFail(new InvocationException(HttpStatus.SC_FORBIDDEN, "forbidden", "the request url is not validate"));
    }
  }

}

