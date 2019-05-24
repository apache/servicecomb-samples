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

package org.apache.servicecomb.authentication.server;

import java.util.Map;
import java.util.UUID;

import org.apache.servicecomb.authentication.jwt.JWTClaims;
import org.apache.servicecomb.authentication.jwt.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.stereotype.Component;

import com.netflix.config.DynamicPropertyFactory;

@Component(value = "passwordTokenGranter")
public class PasswordTokenGranter implements TokenGranter {
  @Autowired
  @Qualifier("authUserDetailsService")
  private UserDetailsService userDetailsService;

  @Autowired
  @Qualifier("authPasswordEncoder")
  private PasswordEncoder passwordEncoder;

  @Autowired
  @Qualifier("authSigner")
  private Signer signer;

  @Override
  public Token grant(Map<String, String> parameters) {
    String username = parameters.get(TokenConst.PARAM_USERNAME);
    String password = parameters.get(TokenConst.PARAM_PASSWORD);

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (passwordEncoder.matches(password, userDetails.getPassword())) {
      JWTClaims claims = new JWTClaims();
      if (userDetails.getAuthorities() != null) {
        userDetails.getAuthorities().forEach(authority -> claims.addAuthority(authority.getAuthority()));
      }
      claims.setJti(UUID.randomUUID().toString());
      claims.setIat(System.currentTimeMillis());
      claims.setExp(5 * 60);
      
      String content = JsonParser.unparse(claims);
      Jwt accessToken = JwtHelper.encode(content, signer);

      Token token = new Token();
      token.setScope(claims.getScope());
      token.setExpires_in(10 * 60);
      token.setToken_type("bearer");
      token.setAccess_token(accessToken.getEncoded());
      
      JWTClaims accessTokenClaims = new JWTClaims();
      accessTokenClaims.setJti(UUID.randomUUID().toString());
      accessTokenClaims.setIat(System.currentTimeMillis());
      accessTokenClaims.setExp(60 * 60);
      Jwt refreshToken = JwtHelper.encode(JsonParser.unparse(claims), signer);
      token.setRefresh_token(refreshToken.getEncoded());
      
      return token;
    } else {
      return null;
    }
  }

  @Override
  public String grantType() {
    return TokenConst.GRANT_TYPE_PASSWORD;
  }

  @Override
  public boolean enabled() {
    return DynamicPropertyFactory.getInstance()
        .getBooleanProperty("servicecomb.authentication.granter.password.enabled", true)
        .get();
  }

}
