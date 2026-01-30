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
package org.apache.servicecomb.samples.apollo;

import org.apache.servicecomb.config.DynamicProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MainServer {
  @Autowired
  private DynamicProperties dynamicProperties;

  @Autowired
  private Environment environment;

  public String getTimeoutFromDynamic() {
    return dynamicProperties.getStringProperty("timeout", "default");
  }

  public String getTimeoutFromEnv() {
    return environment.getProperty("timeout","default");
  }

  public static void main(String[] args) throws Exception {
    ConfigurableApplicationContext context = null;
    try {
      context = new SpringApplicationBuilder()
              .web(WebApplicationType.NONE)
              .sources(MainServer.class)
              .run(args);

      MainServer mainServer = context.getBean(MainServer.class);
      while (true) {
        System.out.println("Dynamic Properties: " + mainServer.getTimeoutFromDynamic());
        System.out.println("Environment: " + mainServer.getTimeoutFromEnv());
        Thread.sleep(3000);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (context != null) {
        context.close();
      }
    }
  }
}
