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

package org.apache.servicecomb.authentication.edge;

import org.apache.servicecomb.authentication.api.AuthenticationService;
import org.apache.servicecomb.authentication.api.Token;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestSchema(schemaId = "AuthenticationEndpoint")
@RequestMapping(path = "/v1/auth")
public class AuthenticationEndpoint {
  @RpcReference(microserviceName = "authentication-server", schemaId = "AuthenticationEndpoint")
  private AuthenticationService authenticationService;

  @PostMapping(path = "login")
  public Token login(@RequestParam(name = "userName") String userName,
      @RequestParam(name = "password") String password) {
    return authenticationService.login(userName, password);
  }

  @PostMapping(path = "refresh")
  public Token refresh(@RequestParam(name = "refreshToken") String refreshToken) {
    return authenticationService.refresh(refreshToken);
  }
}
