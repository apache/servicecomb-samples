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

import java.util.Map;

import org.apache.servicecomb.edge.core.AbstractEdgeDispatcher;
import org.apache.servicecomb.edge.core.EdgeInvocation;

import io.vertx.core.http.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class ApiDispatcher extends AbstractEdgeDispatcher {
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
    Map<String, String> pathParams = context.pathParams();
    String microserviceName = pathParams.get("param0");
    String path = "/" + pathParams.get("param1");

    EdgeInvocation invoker = new EdgeInvocation() {
      // Authentication. Notice: adding context must after setContext or will override by network
      protected void setContext() throws Exception {
        super.setContext();
        // get session id from header and cookie for debug reasons
        String sessionId = context.request().getHeader("session-id");
        if (sessionId != null) {
          this.invocation.addContext("session-id", sessionId);
        } else {
          Cookie sessionCookie = context.cookieMap().get("session-id");
          if (sessionCookie != null) {
            this.invocation.addContext("session-id", sessionCookie.getValue());
          }
        }
      }
    };
    invoker.init(microserviceName, context, path, httpServerFilters);
    invoker.edgeInvoke();
  }
}
