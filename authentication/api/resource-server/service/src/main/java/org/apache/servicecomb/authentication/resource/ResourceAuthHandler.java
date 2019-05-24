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

package org.apache.servicecomb.authentication.resource;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.authentication.jwt.JWTClaims;
import org.apache.servicecomb.authentication.jwt.JsonParser;
import org.apache.servicecomb.authentication.util.Constants;
import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

public class ResourceAuthHandler implements Handler {

  @Override
  public void handle(Invocation invocation, AsyncResponse asyncResponse) throws Exception {
    AccessConfiguration config = AccessConfigurationManager.getAccessConfiguration(invocation);

    // by pass authentication
    if (!config.needAuth) {
      invocation.next(asyncResponse);
      return;
    }

    String token = invocation.getContext(Constants.CONTEXT_HEADER_AUTHORIZATION);
    if (token == null) {
      asyncResponse.consumerFail(new InvocationException(403, "forbidden", "not authenticated"));
      return;
    }
    // verify tokens
    Jwt jwt = JwtHelper.decode(token);
    JWTClaims claims;
    try {
      jwt.verifySignature(BeanUtils.getBean("authSignerVerifier"));
      claims = JsonParser.parse(jwt.getClaims(), JWTClaims.class);
      // TODO: verify claims.
    } catch (Exception e) {
      asyncResponse.consumerFail(new InvocationException(403, "forbidden", "not authenticated"));
      return;
    }

    // check roles
    if (!StringUtils.isEmpty(config.roles)) {
      String[] roles = config.roles.split(",");
      if (roles.length > 0) {
        boolean valid = false;
        Set<String> authorities = claims.getAuthorities();
        for (String role : roles) {
          if (authorities.contains(role)) {
            valid = true;
            break;
          }
        }
        if (!valid) {
          asyncResponse.consumerFail(new InvocationException(403, "forbidden", "not authenticated"));
          return;
        }
      }
    }

    // pre method authentiation
    Set<GrantedAuthority> grantedAuthorities = new HashSet<>(claims.getAuthorities().size());
    claims.getAuthorities().forEach(v -> grantedAuthorities.add(new SimpleGrantedAuthority(v)));
    SecurityContext sc = new SecurityContextImpl();
    Authentication authentication = new SimpleAuthentication(true, grantedAuthorities);
    sc.setAuthentication(authentication);
    SecurityContextHolder.setContext(sc);

    // next
    invocation.next(asyncResponse);
  }

}
