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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.servicecomb.foundation.common.utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/scb/management"))
public class JavaChassisManagementEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavaChassisManagementEndpoint.class);

  @Autowired
  private Registration registration;

  @GetMapping(path = "/health")
  public boolean health(@RequestParam("instanceId") String instanceId,
      @RequestParam("registryName") String registryName) {
    return "sc-registry".equals(registryName) && registration.getInstanceId().equals(instanceId);
  }

  @PostMapping(path = "/schema/contents")
  public Map<String, String> schemaContents() {
    try {
      List<URI> resourceUris = ResourceUtil.findResourcesBySuffix("export", ".yaml");
      Map<String, String> result = new HashMap<>(resourceUris.size());
      for (URI uri : resourceUris) {
        String path = uri.toURL().getPath();
        String[] segments = path.split("/");
        if (segments.length < 2 || !"export".equals(segments[segments.length - 2])) {
          continue;
        }
        result.put(segments[segments.length - 1].substring(0, segments[segments.length - 1].indexOf(".yaml")),
            IOUtils.toString(uri, StandardCharsets.UTF_8));
      }
      return result;
    } catch (IOException | URISyntaxException e) {
      LOGGER.error("Load schema ids failed from microservices. {}.", e.getMessage());
      return Collections.emptyMap();
    }
  }
}
