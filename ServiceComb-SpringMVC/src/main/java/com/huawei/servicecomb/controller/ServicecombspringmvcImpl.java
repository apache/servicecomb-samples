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

package com.huawei.servicecomb.controller;


import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.apache.servicecomb.provider.rest.common.RestSchema;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.CseSpringDemoCodegen", date = "2019-08-02T08:06:28.094Z")

@RestSchema(schemaId = "servicecombspringmvc")
@RequestMapping(path = "/rest", produces = MediaType.APPLICATION_JSON)
public class ServicecombspringmvcImpl {

  @Autowired
  private ServicecombspringmvcDelegate userServicecombspringmvcDelegate;


  @RequestMapping(value = "/helloworld",
      produces = {"application/json"},
      method = RequestMethod.GET)
  public String helloworld(@RequestParam(value = "name", required = true) String name) {

    return userServicecombspringmvcDelegate.helloworld(name);
  }
}
