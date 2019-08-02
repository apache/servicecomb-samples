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

package org.apache.servicecomb.samples.practise.houserush.gateway.config;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LoginUrlConfig {
  private DynamicStringProperty loginUrls = DynamicPropertyFactory.getInstance()
      .getStringProperty("gateway.loginUrls", "");

  private DynamicStringProperty nologinUrls = DynamicPropertyFactory.getInstance()
      .getStringProperty("gateway.noLoginUrls", "");

  public Set<String> loginUrlsSet = new HashSet<>(Arrays.asList(loginUrls.get().split(",")));

  public Set<String> nologinUrlsSet = new HashSet<>(Arrays.asList(nologinUrls.get().split(",")));

  public LoginUrlConfig() {
    //TODO runtime change set

  }
}
