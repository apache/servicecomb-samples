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

import java.util.List;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.samples.common.schema.Assertion;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    List<SpringmvcBasicResponseModel> responseModelList;

    // Invoke a spring mvc provider using RPC
    responseModel = rpcInoker.postObject(requestModel);
    Assertion.assertEquals("Hello World", responseModel.getResultMessage());
    responseModelList = rpcInoker.postObjectList(requestModel);
    Assertion.assertEquals("Hello World", responseModelList.get(0).getResultMessage());

    // Invoke a spring mvc provider using RestTemplate
    responseModel = restTemplateInvoker
        .postForObject("cse://springmvc/springmvc/basic/postObject", requestModel, SpringmvcBasicResponseModel.class);
    Assertion.assertEquals("Hello World", responseModel.getResultMessage());
    HttpEntity<SpringmvcBasicRequestModel> requestEntity = new HttpEntity<>(requestModel, null);
    responseModelList = restTemplateInvoker.exchange("cse://springmvc/springmvc/basic/postObjectList", HttpMethod.POST, requestEntity,
        new ParameterizedTypeReference<List<SpringmvcBasicResponseModel>>() {
        }).getBody();
    Assertion.assertEquals("Hello World", responseModelList.get(0).getResultMessage());
  }
}
