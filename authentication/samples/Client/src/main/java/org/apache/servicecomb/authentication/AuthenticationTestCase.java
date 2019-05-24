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

import org.apache.servicecomb.authentication.jwt.JWTClaims;
import org.apache.servicecomb.authentication.jwt.JsonParser;
import org.apache.servicecomb.authentication.server.Token;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class AuthenticationTestCase implements TestCase {
  @Override
  public void run() {
    testHanlderAuth();
    testMethodAuth();
  }


  private void testHanlderAuth() {
    // get token
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("username", "admin");
    map.add("password", "changeMyPassword");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    Token token =
        BootEventListener.authenticationServerTokenEndpoint.postForObject("/",
            new HttpEntity<>(map, headers),
            Token.class);
    TestMgr.check("bearer", token.getToken_type());
    TestMgr.check(true, token.getAccess_token().length() > 10);

    // get resources
    headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token.getAccess_token());
    headers.setContentType(MediaType.APPLICATION_JSON);
    String name;
    name = BootEventListener.resouceServerHandlerAuthEndpoint.postForObject("/everyoneSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = BootEventListener.resouceServerHandlerAuthEndpoint.postForObject("/adminSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = BootEventListener.resouceServerHandlerAuthEndpoint.postForObject("/guestOrAdminSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = null;
    try {
      name = BootEventListener.resouceServerHandlerAuthEndpoint.postForObject("/guestSayHello?name=Hi",
          new HttpEntity<>(headers),
          String.class);
    } catch (HttpClientErrorException e) {
      TestMgr.check(403, e.getStatusCode().value());
    }
    TestMgr.check(null, name);
    
    // refresh token
    // get token
    map = new LinkedMultiValueMap<>();
    map.add("grant_type", "refresh_token");
    map.add("refresh_token", token.getRefresh_token());
    map.add("access_token", token.getAccess_token());
    headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    Token tokenNew =
        BootEventListener.authenticationServerTokenEndpoint.postForObject("/",
            new HttpEntity<>(map, headers),
            Token.class);
    TestMgr.check(token.getToken_type(), tokenNew.getToken_type());
   
    JWTClaims claims = JsonParser.parse(JwtHelper.decode(token.getAccess_token()).getClaims(), JWTClaims.class);
    JWTClaims newClaims = JsonParser.parse(JwtHelper.decode(tokenNew.getAccess_token()).getClaims(), JWTClaims.class);
    TestMgr.check(claims.getJti(), newClaims.getJti());
    TestMgr.check(claims.getIat() < newClaims.getIat(), true);

    // get resources
    headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + tokenNew.getAccess_token());
    headers.setContentType(MediaType.APPLICATION_JSON);

    name = BootEventListener.resouceServerHandlerAuthEndpoint.postForObject("/everyoneSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = BootEventListener.resouceServerHandlerAuthEndpoint.postForObject("/adminSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = BootEventListener.resouceServerHandlerAuthEndpoint.postForObject("/guestOrAdminSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = null;
    try {
      name = BootEventListener.resouceServerHandlerAuthEndpoint.postForObject("/guestSayHello?name=Hi",
          new HttpEntity<>(headers),
          String.class);
    } catch (HttpClientErrorException e) {
      TestMgr.check(403, e.getStatusCode().value());
    }
    TestMgr.check(null, name);
  }


  private void testMethodAuth() {
    // get token
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("username", "admin");
    map.add("password", "changeMyPassword");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    Token token =
        BootEventListener.authenticationServerTokenEndpoint.postForObject("/",
            new HttpEntity<>(map, headers),
            Token.class);
    TestMgr.check("bearer", token.getToken_type());
    TestMgr.check(true, token.getAccess_token().length() > 10);
    TestMgr.check(true, token.getRefresh_token().length() > 10);

    // get resources
    headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token.getAccess_token());
    headers.setContentType(MediaType.APPLICATION_JSON);
    String name;
    name = BootEventListener.resouceServerMethodAuthEndpoint.postForObject("/everyoneSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = BootEventListener.resouceServerMethodAuthEndpoint.postForObject("/adminSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = BootEventListener.resouceServerMethodAuthEndpoint.postForObject("/guestOrAdminSayHello?name=Hi",
        new HttpEntity<>(headers),
        String.class);
    TestMgr.check("Hi", name);

    name = null;
    try {
      name = BootEventListener.resouceServerMethodAuthEndpoint.postForObject("/guestSayHello?name=Hi",
          new HttpEntity<>(headers),
          String.class);
    } catch (HttpClientErrorException e) {
      TestMgr.check(403, e.getStatusCode().value());
    }
    TestMgr.check(null, name);
  }
}
