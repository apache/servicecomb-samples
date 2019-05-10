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

import java.util.Map;

import org.apache.servicecomb.config.inject.ConfigObjectFactory;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.common.concurrent.ConcurrentHashMapEx;

public class AccessConfigurationManager {
  private static final Map<String, AccessConfiguration> CONFIGURATIONS = new ConcurrentHashMapEx<>();

  private static final ConfigObjectFactory FACTORY = new ConfigObjectFactory();

  public static AccessConfiguration getAccessConfiguration(Invocation invocation) {
    return CONFIGURATIONS.computeIfAbsent(invocation.getOperationMeta().getSchemaQualifiedName(), key -> {
      return FACTORY.create(AccessConfiguration.class, "schemaId", invocation.getSchemaId(), "operationId", invocation.getOperationName());
    });
  }
}
