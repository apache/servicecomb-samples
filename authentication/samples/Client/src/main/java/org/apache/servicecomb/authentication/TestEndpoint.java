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

import java.util.List;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "TestEndpoint")
@RequestMapping(path = "/v1/test")
public class TestEndpoint {
  @Autowired
  private List<TestCase> tests;

  @GetMapping(path = "/start")
  public String start() {
    tests.forEach(test -> test.run());

    List<Throwable> errors = TestMgr.errors();
    if (TestMgr.isSuccess()) {
      return TestMgr.successMessage();
    } else {
      TestMgr.summary();

      StringBuilder sb = new StringBuilder();
      sb.append("Failed count : " + errors.size());
      sb.append("\n");
      errors.forEach(t -> sb.append(t.getMessage() + "\n"));
      
      TestMgr.reset();
      return sb.toString();
    }
  }
}
