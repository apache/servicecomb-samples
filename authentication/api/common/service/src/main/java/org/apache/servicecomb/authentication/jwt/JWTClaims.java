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

package org.apache.servicecomb.authentication.jwt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JWTClaims extends JWTClaimsCommon {
  protected Set<String> authorities;

  protected Map<String, Object> additionalInformation;

  /**
   * The scope of the access token as described by <a
   * href="http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-3.3">Section 3.3</a>
   */
  protected Set<String> scope;

  public Set<String> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<String> authorities) {
    this.authorities = authorities;
  }

  public Map<String, Object> getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(Map<String, Object> additionalInformation) {
    this.additionalInformation = additionalInformation;
  }

  public Set<String> getScope() {
    return scope;
  }

  public void setScope(Set<String> scope) {
    this.scope = scope;
  }

  public void addAdditionalInformation(String key, Object value) {
    if (this.additionalInformation == null) {
      this.additionalInformation = new HashMap<>();
    }
    this.additionalInformation.put(key, value);
  }

  public void addScope(String operation) {
    if (this.scope == null) {
      this.scope = new HashSet<>();
    }
    this.scope.add(operation);
  }

  public void addAuthority(String authority) {
    if (this.authorities == null) {
      this.authorities = new HashSet<>();
    }
    this.authorities.add(authority);
  }
}
