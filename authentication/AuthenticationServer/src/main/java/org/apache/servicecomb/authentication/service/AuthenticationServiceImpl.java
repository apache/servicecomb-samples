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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  @Autowired
  @Qualifier("userDetailsService")
  private UserDetailsService userDetailsService;
  
  @Autowired
  @Qualifier("passwordEncoder")
  private PasswordEncoder passwordEncoder;

  @Override
  public Token login(String userName, String password) {
    UserDetails userDetails;
    try {
      userDetails = userDetailsService.loadUserByUsername(userName);
    } catch (UsernameNotFoundException e) {
      return null;
    }
    if(passwordEncoder.matches(password, userDetails.getPassword())) {
      return null;
    } else {
      return null;
    }
  }

  @Override
  public Token refresh(String refreshToken) {
    return null;
  }

}
