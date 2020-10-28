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

package org.apache.servicecomb.samples.bmi;

import org.apache.servicecomb.transport.rest.vertx.VertxHttpDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.DynamicPropertyFactory;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class StaticWebpageDispatcher implements VertxHttpDispatcher {
  private static final Logger LOGGER = LoggerFactory.getLogger(StaticWebpageDispatcher.class);

  private static final String WEB_ROOT = DynamicPropertyFactory.getInstance()
      .getStringProperty("gateway.webroot", "/var/static")
      .get();

  @Override
  public int getOrder() {
    return Integer.MAX_VALUE / 2;
  }

  @Override
  public void init(Router router) {
    String regex = "/(.*)";
    StaticHandler webpageHandler = StaticHandler.create();
    webpageHandler.setWebRoot(WEB_ROOT);
    LOGGER.info("server static web page for WEB_ROOT={}", WEB_ROOT);
    router.routeWithRegex(regex).failureHandler((context) -> {
      LOGGER.error("", context.failure());
    }).handler(webpageHandler);
  }
}
