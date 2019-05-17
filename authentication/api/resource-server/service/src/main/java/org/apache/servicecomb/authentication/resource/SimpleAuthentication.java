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

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SimpleAuthentication implements Authentication {

  /**
   * 
   */
  private static final long serialVersionUID = 6077733273349249822L;

  private boolean authenticated;

  private Collection<? extends GrantedAuthority> authorities;

  public SimpleAuthentication(boolean authenticated, Collection<? extends GrantedAuthority> authorities) {
    this.authenticated = authenticated;
    this.authorities = authorities;
  }


  @Override
  public String getName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public Object getCredentials() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getDetails() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getPrincipal() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isAuthenticated() {
    return this.authenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

}
