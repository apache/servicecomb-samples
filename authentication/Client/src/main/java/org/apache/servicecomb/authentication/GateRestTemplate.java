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
package org.apache.servicecomb.authentication;

import java.util.Arrays;

import org.apache.servicecomb.core.definition.MicroserviceVersionMeta;
import org.apache.servicecomb.core.definition.SchemaMeta;
import org.apache.servicecomb.foundation.common.net.URIEndpointObject;
import org.apache.servicecomb.serviceregistry.RegistryUtils;
import org.apache.servicecomb.serviceregistry.api.registry.MicroserviceInstance;
import org.apache.servicecomb.serviceregistry.consumer.MicroserviceVersionRule;
import org.apache.servicecomb.serviceregistry.definition.DefinitionConst;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class GateRestTemplate extends RestTemplate {
  private final String gateName;

  private final String schemaId;

  private final String producerName;

  private String urlPrefix;

  public static GateRestTemplate createEdgeRestTemplate(String gateName, String producerName, String schemaId) {
    return new GateRestTemplate(gateName, producerName, schemaId);
  }


  public GateRestTemplate(String gateName, String producerName, String schemaId) {
    this.gateName = gateName;
    this.producerName = producerName;
    this.schemaId = schemaId;
  }

  public GateRestTemplate init() {
    urlPrefix = getUrlPrefix(gateName, producerName, schemaId);

    setUriTemplateHandler(new ITUriTemplateHandler(urlPrefix));

    setMessageConverters(Arrays.asList(
        new MappingJackson2HttpMessageConverter(),
        new StringHttpMessageConverter(),
        new FormHttpMessageConverter()));

    return this;
  }

  public String getUrlPrefix() {
    return urlPrefix;
  }

  private String getUrlPrefix(String gateName, String producerName, String schemaId) {
    MicroserviceVersionRule microserviceVersionRule = RegistryUtils.getServiceRegistry()
        .getAppManager()
        .getOrCreateMicroserviceVersionRule(RegistryUtils.getAppId(),
            gateName,
            DefinitionConst.VERSION_RULE_ALL);
    MicroserviceInstance microserviceInstance = microserviceVersionRule.getInstances()
        .values()
        .stream()
        .findFirst()
        .get();
    URIEndpointObject edgeAddress = new URIEndpointObject(microserviceInstance.getEndpoints().get(0));

    String urlSchema = "http";
    if (edgeAddress.isSslEnabled()) {
      urlSchema = "https";
    }

    if(producerName == null) {
      return String
          .format("%s://%s:%d",
              urlSchema,
              edgeAddress.getHostOrIp(),
              edgeAddress.getPort());
    }
    
    microserviceVersionRule = RegistryUtils.getServiceRegistry()
        .getAppManager()
        .getOrCreateMicroserviceVersionRule(RegistryUtils.getAppId(),
            producerName,
            DefinitionConst.VERSION_RULE_ALL);
    MicroserviceVersionMeta microserviceVersionMeta = microserviceVersionRule.getLatestMicroserviceVersion();
    SchemaMeta schemaMeta = microserviceVersionMeta.getMicroserviceMeta().ensureFindSchemaMeta(schemaId);
    return String
        .format("%s://%s:%d/api/%s%s",
            urlSchema,
            edgeAddress.getHostOrIp(),
            edgeAddress.getPort(),
            producerName,
            schemaMeta.getSwagger().getBasePath());
  }
}
