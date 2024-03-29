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

package org.apache.servicecomb.samples.porter.file.endpoint;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.porter.file.api.InternalAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestSchema(schemaId = "InternalAccessEndpoint")
@RequestMapping(path = "/")
@OpenAPIDefinition(tags = {@Tag(name = "INTERNAL")})
public class InternalAccessEndpoint {
  @Autowired
  private InternalAccessService internalAccessService;

  @GetMapping(path = "localAccess")
  public String localAccess(String name) {
    return internalAccessService.localAccess(name);
  }
}
