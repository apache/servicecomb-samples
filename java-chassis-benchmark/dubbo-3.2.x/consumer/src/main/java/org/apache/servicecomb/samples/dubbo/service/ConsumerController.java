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

package org.apache.servicecomb.samples.dubbo.service;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.servicecomb.samples.DataModel;
import org.apache.servicecomb.samples.dubbo.api.ConsumerService;
import org.apache.servicecomb.samples.dubbo.api.ProviderService;

@DubboService(protocol = {"rest"})
@Path("/")
public class ConsumerController implements ConsumerService {
  @DubboReference
  private ProviderService providerService;

  @POST
  @Path("/benchmark")
  public DataModel sayHello(@HeaderParam("wait") int wait, DataModel dataModel) {
    return providerService.sayHello(wait, dataModel);
  }
}
