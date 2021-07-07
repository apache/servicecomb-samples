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

package org.apache.servicecomb.samples;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableServiceComb
@Component
public class ConsumerApplication {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerApplication.class);

  @RpcReference(microserviceName = "provider", schemaId = "ProviderController")
  private static ProviderService providerService;

  public static void main(String[] args) throws Exception {
    try {
      new SpringApplicationBuilder().web(WebApplicationType.NONE).sources(ConsumerApplication.class).run(args);
    } catch (Exception e) {
      e.printStackTrace();
    }

    new Thread() {
      public void run() {
        AtomicLong index = new AtomicLong(0);

        while (true) {
          try {
            LOGGER.info("call service: name=" + index.get());
            providerService.sayHello("hello" + index.getAndIncrement());
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            LOGGER.error("", e);
          }
        }
      }
    }.start();
  }
}
