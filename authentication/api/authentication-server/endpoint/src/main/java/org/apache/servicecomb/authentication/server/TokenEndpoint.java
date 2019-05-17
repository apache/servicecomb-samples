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

import javax.ws.rs.core.MediaType;

import org.apache.servicecomb.authentication.jwt.JWTClaims;
import org.apache.servicecomb.authentication.jwt.JsonParser;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "TokenEndpoint")
@RequestMapping(path = "/v1/oauth/token")
public class TokenEndpoint implements TokenService {
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
  @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED)
  public Token getAccessToken(@RequestBody Map<String, String> parameters) {
    String grantType = parameters.get(TokenConst.PARAM_GRANT_TYPE);

    if (TokenConst.GRANT_TYPE_PASSWORD.equals(grantType)) {

      String username = parameters.get(TokenConst.PARAM_USERNAME);
      String password = parameters.get(TokenConst.PARAM_PASSWORD);

      try {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
          JWTClaims claims = new JWTClaims();
          if (userDetails.getAuthorities() != null) {
            userDetails.getAuthorities().forEach(authority -> claims.addAuthority(authority.getAuthority()));
          }
          String content = JsonParser.unparse(claims);
          Jwt accessToken = JwtHelper.encode(content, signer);

          Token token = new Token();
          token.setScope(claims.getScope());
          token.setExpires_in(10 * 60);
          token.setToken_type("bearer");
          token.setAccess_token(accessToken.getEncoded());
          return token;
        } else {
          return null;
        }
      } catch (UsernameNotFoundException e) {
        throw new InvocationException(403, "", "forbidden");
      }
    } else {
      return null;
    }

  }

}
