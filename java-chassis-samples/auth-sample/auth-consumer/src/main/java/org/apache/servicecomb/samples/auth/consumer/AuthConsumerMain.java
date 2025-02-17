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
package org.apache.servicecomb.samples.auth.consumer;

import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.samples.common.schema.models.Person;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.client.RestOperations;

@SpringBootApplication
public class AuthConsumerMain {

  private static RestOperations restTemplate = RestTemplateBuilder.create();

  public static void main(String[] args) {
    try {
      new SpringApplicationBuilder()
              .web(WebApplicationType.NONE)
              .sources(AuthConsumerMain.class)
              .run(args);
    } catch (Exception e) {
      e.printStackTrace();
    }

    run();
  }

  private static void run() {
    System.out.println("RestTemplate Consumer or POJO Consumer.  You can choose whatever you like.");
    String sayHiResult = restTemplate
            .postForObject(
                    "cse://auth-provider/springmvchello/sayhi?name=Authenticate",
                    null,
                    String.class);

    Person person = new Person();
    person.setName("ServiceComb/Authenticate");
    String sayHelloResult = restTemplate.postForObject(
            "cse://auth-provider/springmvchello/sayhello",
            person,
            String.class);
    System.out.println(sayHiResult);
    System.out.println(sayHelloResult);
  }
}
