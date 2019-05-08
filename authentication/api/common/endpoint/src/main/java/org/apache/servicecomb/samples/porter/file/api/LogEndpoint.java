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

package org.apache.servicecomb.samples.porter.file.api;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.porter.common.api.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.netflix.config.DynamicPropertyFactory;

@RestSchema(schemaId = "log")
@RequestMapping(path = "/v1/log")
public class LogEndpoint implements LogService {
  // protect your file in real applications
  private static final File LOG_DIR =
      new File(DynamicPropertyFactory.getInstance().getStringProperty("servicecomb.samples.logdir", ".").get());

  private static final String FILE_POST_FIX = ".log";

  @Override
  @GetMapping(path = "/getLogFileList")
  public List<String> getLogFileList() {
    File[] files = LOG_DIR.listFiles(new FileFilter() {
      @Override
      public boolean accept(File file) {
        return isLogFile(file);
      }
    });

    List<String> result = new ArrayList<>(files.length);
    for (int i = 0; i < files.length; i++) {
      result.add(files[i].getName());
    }
    return result;
  }

  @Override
  @GetMapping(path = "/getLogFileContent")
  public File getLogFileContent(@RequestParam(name = "fileName") String fileName) {
    File file = new File(LOG_DIR, fileName);
    if (isLogFile(file)) {
      return file;
    }
    return null;
  }

  private boolean isLogFile(File file) {
    return file.isFile() && file.canRead() && file.getName().endsWith(FILE_POST_FIX);
  }
}
