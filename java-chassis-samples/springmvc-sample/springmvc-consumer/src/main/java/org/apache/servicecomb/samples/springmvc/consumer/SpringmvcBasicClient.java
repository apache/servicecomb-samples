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
import org.apache.servicecomb.samples.common.schema.Assertion;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("SpringmvcBasicClient")
public class SpringmvcBasicClient {
  private RestTemplate restTemplateInvoker = RestTemplateBuilder.create();

  @RpcReference(microserviceName = "springmvc", schemaId = "SpringmvcBasicEndpoint")
  private SpringmvcBasicService rpcInoker;

  public void run() {
    SpringmvcBasicRequestModel requestModel = new SpringmvcBasicRequestModel();
    requestModel.setName("Hello World");
    SpringmvcBasicResponseModel responseModel;

    // Invoke a spring mvc provider using RPC
    responseModel = rpcInoker.sayHello(requestModel);
    Assertion.assertEquals("Hello World", responseModel.getResultMessage());

    // TODO : this test case should work in 2.0, currently not implemented
//    // Invoke a spring mvc provider using RestTemplate
//    responseModel = restTemplateInvoker
//        .postForObject("cse://springmvc/springmvc/basic/postObject", responseModel, SpringmvcBasicResponseModel.class);
//    Assertion.assertEquals("Hello World", responseModel.getResultMessage());
  }
}
