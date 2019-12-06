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
import org.apache.servicecomb.provider.springmvc.reference.async.CseAsyncRestTemplate;
import org.apache.servicecomb.samples.common.schema.Assertion;
import org.apache.servicecomb.samples.common.schema.Hello;
import org.apache.servicecomb.samples.common.schema.models.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

@Component("SpringmvcHelloClient")
public class SpringmvcHelloClient {
  private static final Logger LOG = LoggerFactory.getLogger(SpringmvcHelloClient.class);

  private static RestTemplate restTemplate = RestTemplateBuilder.create();

  @RpcReference(microserviceName = "springmvc", schemaId = "springmvcHello")
  private static Hello hello;

  public void run() throws Exception {
    Person person = new Person();
    person.setName("ServiceComb/Java Chassis");

    // RestTemplate Consumer or POJO Consumer. You can choose whatever you like
    // RestTemplate Consumer
    String sayHiResult =
        restTemplate.postForObject("cse://springmvc/springmvchello/sayhi?name=Java Chassis", null, String.class);
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
    CseAsyncRestTemplate cseAsyncRestTemplate = new CseAsyncRestTemplate();
    ListenableFuture<ResponseEntity<String>> responseEntityListenableFuture = cseAsyncRestTemplate
        .postForEntity("cse://springmvc/springmvchello/sayhi?name=Java Chassis", null, String.class);
    ResponseEntity<String> responseEntity = responseEntityListenableFuture.get();
    Assertion.assertEquals("Hello Java Chassis", responseEntity.getBody());
    System.out.println("AsyncRestTemplate Consumer sayHi services: " + responseEntity.getBody());

    HttpEntity<Person> entity = new HttpEntity<>(person);
    ListenableFuture<ResponseEntity<String>> listenableFuture = cseAsyncRestTemplate
        .exchange("cse://springmvc/springmvchello/sayhello", HttpMethod.POST, entity, String.class);

    listenableFuture.addCallback(
        new ListenableFutureCallback<ResponseEntity<String>>() {
          @Override
          public void onFailure(Throwable ex) {
            LOG.error("AsyncResTemplate Consumer catched exception when sayHello, ", ex);
          }

          @Override
          public void onSuccess(ResponseEntity<String> result) {
            System.out.println("AsyncRestTemplate Consumer sayHello services: " + result.getBody());
          }
        });
  }
}
