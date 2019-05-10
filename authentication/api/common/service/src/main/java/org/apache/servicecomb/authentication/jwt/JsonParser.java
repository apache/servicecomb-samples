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

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static <T> T parse(String json, Class<T> clazz) {
    try {
      return MAPPER.readValue(json, clazz);
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot parse json", e);
    }
  }

  public static <T> String unparse(T obj) {
    try {
      return MAPPER.writeValueAsString(obj);
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot unparse json", e);
    }
  }
}
