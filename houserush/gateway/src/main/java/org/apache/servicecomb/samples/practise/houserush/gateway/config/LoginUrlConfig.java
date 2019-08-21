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
  private DynamicStringProperty needLoginUrls = DynamicPropertyFactory.getInstance()
      .getStringProperty("gateway.needLoginUrls", "");

  private DynamicStringProperty noNeedLoginUrls = DynamicPropertyFactory.getInstance()
      .getStringProperty("gateway.noNeedLoginUrls", "");

  private Set<String> needLoginUrlsSet = new HashSet<>(Arrays.asList(needLoginUrls.get().split(",")));

  private Set<String> noNeedLoginUrlsSet = new HashSet<>(Arrays.asList(noNeedLoginUrls.get().split(",")));

  public LoginUrlConfig() {
    // add a callback when this property is changed
    needLoginUrls.addCallback(() -> needLoginUrlsSet = new HashSet<>(Arrays.asList(needLoginUrls.get().split(","))));
    noNeedLoginUrls.addCallback(() -> noNeedLoginUrlsSet = new HashSet<>(Arrays.asList(noNeedLoginUrls.get().split(","))));

  }

  public Set<String> getNeedLoginUrlsSet() {
    return needLoginUrlsSet;
  }

  public Set<String> getNoNeedLoginUrlsSet() {
    return noNeedLoginUrlsSet;
  }
}
