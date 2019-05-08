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

package org.apache.servicecomb.authentication.service;

import org.apache.servicecomb.authentication.api.AuthenticationService;
import org.apache.servicecomb.authentication.api.Token;
import org.apache.servicecomb.authentication.jwt.JWTClaims;
import org.apache.servicecomb.authentication.jwt.JsonParser;
import org.apache.servicecomb.authentication.user.User;
import org.apache.servicecomb.authentication.user.UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  @Autowired
  @Qualifier("authUserStore")
  private UserStore userStore;

  @Autowired
  @Qualifier("authPasswordEncoder")
  private PasswordEncoder passwordEncoder;

  @Autowired
  @Qualifier("authSigner")
  private Signer signer;

  @Override
  public Token login(String userName, String password) {
    User user = userStore.loadUserByUsername(userName);
    if (passwordEncoder.matches(password, user.getPassword())) {
      JWTClaims claims = new JWTClaims();
      if (user.getRoles() != null) {
        user.getRoles().forEach(role -> claims.addRole(role.getRoleName()));
      }
      claims.setScope("read");
      String content = JsonParser.unparse(claims);
      Jwt accessToken = JwtHelper.encode(content, signer);

      Token token = new Token();
      token.setExpires_in(10 * 60);
      token.setToken_type("bearer");
      token.setAccess_token(accessToken.getEncoded());
      return token;
    } else {
      return null;
    }
  }

  @Override
  public Token refresh(String refreshToken) {
    return null;
  }

}
