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

package org.apache.servicecomb.samples.practise.houserush.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.samples.practise.houserush.gateway.config.LoginUrlConfig;
import org.apache.servicecomb.samples.practise.houserush.gateway.rpc.UserApi;
import org.apache.servicecomb.samples.practise.houserush.gateway.rpc.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class AuthorizeFilter extends ZuulFilter {

  private static LoginUrlConfig loginUrlConfig = new LoginUrlConfig();
  private static Logger log = LoggerFactory.getLogger(AuthorizeFilter.class);

  @RpcReference(microserviceName = "login", schemaId = "userApiRest")
  private UserApi userApi;

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  private RequestContext ctx;

  @Override
  public Object run() {
    ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    String method = request.getMethod();
    String requestUri = request.getRequestURI();
    log.info(String.format("%s -> %s", method, requestUri));
    requestUri = requestUri.replaceAll("\\d+", "{id}");

    String key = method + " " + requestUri;
    if (loginUrlConfig.loginUrlsSet.contains(key)) {
      String token = request.getHeader("Authorization");
      if (token != null && StringUtils.isNotBlank(token)) {
        User user = userApi.verifyToken(token);
        if (user != null) {
          ctx.addZuulRequestHeader("customerId", String.valueOf(user.getId()));
          ctx.addZuulResponseHeader("newAuthorization", user.getToken());
          return null;
        }
      }
      sendResponse(HttpStatus.SC_FORBIDDEN, "need login!");
    } else if (loginUrlConfig.nologinUrlsSet.contains(key)) {
      if ("/login/signin".equals(requestUri)) {
        try {
          ObjectMapper mapper = new ObjectMapper();
          User user = mapper.readValue(request.getInputStream(), User.class);
          User resultUser = userApi.signin(user);
          if (resultUser != null && resultUser.getToken() != null) {
            sendResponse(HttpStatus.SC_OK, "{\"token\": \"" + resultUser.getToken() + "\"}");
          } else {
            sendResponse(HttpStatus.SC_UNAUTHORIZED, "cannot sign in!");
          }
        } catch (IOException e) {
          e.printStackTrace();
          sendResponse(HttpStatus.SC_UNAUTHORIZED, e.getMessage());
        }
      }
      return null;
    } else {
      sendResponse(HttpStatus.SC_UNAUTHORIZED, "the request url is not validate");
    }
    return null;

  }

  private void sendResponse(int code, String message) {
    ctx.setSendZuulResponse(false);
    ctx.setResponseStatusCode(code);
    try {
      ctx.getResponse().getWriter().write(message);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }


}
