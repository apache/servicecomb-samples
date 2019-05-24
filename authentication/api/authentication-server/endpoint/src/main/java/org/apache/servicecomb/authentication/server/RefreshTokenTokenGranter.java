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

import org.apache.servicecomb.authentication.jwt.JWTClaims;
import org.apache.servicecomb.authentication.jwt.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;
import org.springframework.stereotype.Component;

import com.netflix.config.DynamicPropertyFactory;

@Component(value = "fefreshTokenTokenGranter")
public class RefreshTokenTokenGranter implements TokenGranter {
  @Autowired
  @Qualifier("authSignerVerifier")
  private SignerVerifier signerVerifier;

  @Autowired
  @Qualifier("authSigner")
  private Signer signer;

  @Override
  public boolean enabled() {
    return DynamicPropertyFactory.getInstance()
        .getBooleanProperty("servicecomb.authentication.granter.refreshToken.enabled", true)
        .get();
  }

  @Override
  public String grantType() {
    return TokenConst.GRANT_TYPE_REFRESH_TOKEN;
  }

  @Override
  public Token grant(Map<String, String> parameters) {
    String refreshToken = parameters.get(TokenConst.PARAM_REFRESH_TOKEN);
    String accessToken = parameters.get(TokenConst.PARAM_ACCESS_TOKEN);

    // verify refresh tokens
    Jwt jwt = JwtHelper.decode(refreshToken);
    JWTClaims claims;
    try {
      jwt.verifySignature(signerVerifier);
      claims = JsonParser.parse(jwt.getClaims(), JWTClaims.class);
      // TODO: verify claims.
    } catch (Exception e) {
      return null;
    }

    // verify access tokens
    jwt = JwtHelper.decode(accessToken);
    claims = null;
    try {
      jwt.verifySignature(signerVerifier);
      claims = JsonParser.parse(jwt.getClaims(), JWTClaims.class);
      // TODO: verify claims.
    } catch (Exception e) {
      return null;
    }

    claims.setIat(System.currentTimeMillis());
    String content = JsonParser.unparse(claims);
    Jwt newAccessToken = JwtHelper.encode(content, signer);

    Token token = new Token();
    token.setScope(claims.getScope());
    token.setExpires_in(10 * 60);
    token.setToken_type("bearer");
    token.setAccess_token(newAccessToken.getEncoded());
    return token;
  }

}
