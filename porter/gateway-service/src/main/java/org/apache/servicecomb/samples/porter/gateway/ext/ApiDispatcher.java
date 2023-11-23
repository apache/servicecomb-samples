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

package org.apache.servicecomb.samples.porter.gateway.ext;

import org.apache.servicecomb.common.rest.RestProducerInvocationFlow;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.invocation.InvocationCreator;
import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;
import org.apache.servicecomb.edge.core.EdgeInvocationCreator;
import org.apache.servicecomb.edge.core.Utils;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.HttpServletResponseEx;
import org.apache.servicecomb.foundation.vertx.http.VertxServerRequestToHttpServletRequest;
import org.apache.servicecomb.foundation.vertx.http.VertxServerResponseToHttpServletResponse;
import org.springframework.lang.Nullable;

import io.vertx.core.http.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ApiDispatcher extends AbstractEdgeDispatcher {
  public static final String MICROSERVICE_NAME = "param0";

  @Override
  public int getOrder() {
    return 10002;
  }

  @Override
  public void init(Router router) {
    String regex = "/api/([^\\/]+)/(.*)";
    router.routeWithRegex(regex).handler(createBodyHandler());
    router.routeWithRegex(regex).failureHandler(this::onFailure).handler(this::onRequest);
  }

  protected void onRequest(RoutingContext context) {
    String microserviceName = extractMicroserviceName(context);
    String path = Utils.findActualPath(context.request().path(), 2);

    requestByFilter(context, microserviceName, path);
  }

  @Nullable
  private String extractMicroserviceName(RoutingContext context) {
    return context.pathParam(MICROSERVICE_NAME);
  }

  protected void requestByFilter(RoutingContext context, String microserviceName, String path) {
    HttpServletRequestEx requestEx = new VertxServerRequestToHttpServletRequest(context);
    HttpServletResponseEx responseEx = new VertxServerResponseToHttpServletResponse(context.response());
    InvocationCreator creator = new EdgeInvocationCreator(context, requestEx, responseEx,
        microserviceName, path) {
      @Override
      protected Invocation createInstance() {
        Invocation invocation = super.createInstance();
        // get session id from header and cookie for debug reasons
        String sessionId = context.request().getHeader("session-id");
        if (sessionId != null) {
          invocation.addContext("session-id", sessionId);
        } else {
          Cookie sessionCookie = context.request().getCookie("session-id");
          if (sessionCookie != null) {
            invocation.addContext("session-id", sessionCookie.getValue());
          }
        }
        return invocation;
      }
    };
    new RestProducerInvocationFlow(creator, requestEx, responseEx)
        .run();
  }
}
