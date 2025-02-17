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

package org.apache.servicecomb.samples.springmvc.consumer;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
//import org.apache.servicecomb.provider.springmvc.reference.async.CseAsyncRestTemplate;
import org.apache.servicecomb.samples.common.schema.Assertion;
//import org.apache.servicecomb.samples.common.schema.Hello;
import org.apache.servicecomb.samples.common.schema.Hello;
import org.apache.servicecomb.samples.common.schema.models.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.concurrent.CompletableFuture;

@Component("SpringmvcHelloClient")
public class SpringmvcHelloClient {
  private static final Logger LOG = LoggerFactory.getLogger(SpringmvcHelloClient.class);

  private static RestOperations restTemplate = RestTemplateBuilder.create();

  @RpcReference(microserviceName = "springmvc", schemaId = "springmvcHello")
  private static Hello hello;

  @RpcReference(microserviceName = "springmvc", schemaId = "springmvcHello")
  private static HelloAync helloAync;

  public void run() throws Exception {
    Person person = new Person();
    person.setName("ServiceComb/Java Chassis");

    // RestTemplate Consumer or POJO Consumer. You can choose whatever you like
    // RestTemplate Consumer
    String sayHiResult =
        restTemplate.postForObject("cse://springmvc/springmvchello/sayhi?name={name}", null, String.class, "Java Chassis");
    Assertion.assertEquals("Hello Java Chassis", sayHiResult);

    String sayHiDefaultResult =
        restTemplate.postForObject("cse://springmvc/springmvchello/sayhi", null, String.class);
    Assertion.assertEquals("Hello test", sayHiDefaultResult);

    String sayHelloResult = restTemplate.postForObject("cse://springmvc/springmvchello/sayhello", person, String.class);
    Assertion.assertEquals("Hello person ServiceComb/Java Chassis", sayHelloResult);

    System.out.println("RestTemplate Consumer or POJO Consumer.  You can choose whatever you like.");
    System.out.println("RestTemplate consumer sayhi services: " + sayHiResult);
    System.out.println("RestTemplate consumer sayHiDefault services: " + sayHiDefaultResult);
    System.out.println("RestTemplate consumer sayhello services: " + sayHelloResult);

    // POJO Consumer
    String pojoSayHi = hello.sayHi("Java Chassis");
    Assertion.assertEquals("Hello Java Chassis", pojoSayHi);
    String pojoSayHello = hello.sayHello(person);
    Assertion.assertEquals("Hello person ServiceComb/Java Chassis", pojoSayHello);

    System.out.println("POJO consumer sayhi services: " + pojoSayHi);
    System.out.println("POJO consumer sayhello services: " + pojoSayHello);

    //AsyncRestTemplate Consumer
    // NOTICE: since 2.0.0, spring deprecated AsyncRestTemplate, user's can use CompletableFuture of RPC instead
    CompletableFuture<String> asyncSayHi = helloAync.sayHi("Java Chassis");
    String sayHiResp = asyncSayHi.get();
    Assertion.assertEquals("Hello Java Chassis", sayHiResp);
    System.out.println("Async Consumer sayHi services: " + sayHiResp);

    CompletableFuture<String> asyncSayHello = helloAync.sayHello(person);
    asyncSayHello.thenAccept(result -> System.out.println("Async Consumer sayHello services: " + result))
            .exceptionally(ex -> {
              LOG.error("Async Consumer catched exception when sayHello, ", ex);
              return null;
            });
  }
}
