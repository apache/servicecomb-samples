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

package org.apache.servicecomb.samples;

import static java.util.Collections.singletonList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestSchema(schemaId = "UploadSpringMvcConsumer")
@RequestMapping(path = "/")
public class UploadSpringMvcConsumer {
  private static final Logger LOGGER = LoggerFactory.getLogger(UploadSpringMvcConsumer.class);

  private FileSystemResource fileSystemResource1;

  private FileSystemResource fileSystemResource2;


  private static final String message = "cseMessage";

  public UploadSpringMvcConsumer() throws IOException {
    File file1 = File.createTempFile("jaxrstest1", ".txt");
    File file2 = File.createTempFile("测试啊", ".txt");
    File file3 = File.createTempFile("files", ".yaml");
    File file4 = File.createTempFile("files4", ".yaml");
    FileUtils.writeStringToFile(file1, "hello1", StandardCharsets.UTF_8, false);
    FileUtils.writeStringToFile(file2, "中文 2", StandardCharsets.UTF_8, false);
    FileUtils.writeStringToFile(file3, "cse3", StandardCharsets.UTF_8, false);
    FileUtils.writeStringToFile(file4, "cse4", StandardCharsets.UTF_8, false);
    fileSystemResource1 = new FileSystemResource(file1);
    fileSystemResource2 = new FileSystemResource(file2);
  }

  FileSystemResource createFileSystemResource(String prefix, String suffix, String content) {
    try {
      File file = File.createTempFile(prefix, suffix);
      FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8, false);
      return new FileSystemResource(file);
    } catch (IOException e) {
      return null;
    }
  }

  @GetMapping("/uploadMix")
  public String uploadMix() throws Exception {
    LOGGER.info("================================================");
    CountDownLatch latch = new CountDownLatch(2);
    AtomicBoolean failed = new AtomicBoolean(false);

    for (int i = 0; i < 2; i++) {
      final int index = i;
      new Thread() {
        public void run() {
          try {
            Map<String, Object> map = new HashMap<>();
            List<FileSystemResource> fileList = new ArrayList<>();
            fileList.add(createFileSystemResource("测试啊-" + index, ".txt", "中文 2"));
            map.put("file", createFileSystemResource("jaxrstest1-" + index, ".txt", "hello1"));
            map.put("fileList", fileList);
            map.put("str", message);
            map.put("strList", singletonList("2.中文测试"));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            RestTemplate restTemplate = RestTemplateBuilder.create();
            ResponseEntity<Map<String, String>> response =
                restTemplate
                    .exchange("servicecomb://provider/v1/uploadSpringmvcSchema/uploadMultiformMix", HttpMethod.POST,
                        new HttpEntity<>(map, headers), new ParameterizedTypeReference<Map<String, String>>() {
                        });
            Map<String, String> responseBody = response.getBody();
            String result = responseBody.get("file") +
                responseBody.get("fileList") +
                responseBody.get("str") +
                responseBody.get("strList");

            if (!containsAll(result, "hello1", "中文 2", message, "[2.中文测试]")) {
              LOGGER.error("failed result is {}, index is ={}", result, index);
              failed.set(true);
            }
          } catch (Exception e) {
            LOGGER.error("failed result is {}, index is ={}", e.getMessage(), index);
            failed.set(true);
          }
          latch.countDown();
        }
      }.start();
    }

    latch.await();
    return "failed=" + failed.get();
  }

  @GetMapping("/upload")
  public String upload() throws Exception {
    CountDownLatch latch = new CountDownLatch(10);
    AtomicBoolean failed = new AtomicBoolean(false);

    for (int i = 0; i < 10; i++) {
      final int index = i;
      new Thread() {
        public void run() {
          try {
            Map<String, Object> map = new HashMap<>();
            map.put("file1", createFileSystemResource("jaxrstest1-" + index, ".txt", "hello1"));
            map.put("file2", createFileSystemResource("测试啊-" + index, ".txt", "中文 2"));
            map.put("name", message);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            RestTemplate restTemplate = RestTemplateBuilder.create();
            String result = restTemplate.postForObject("servicecomb://provider/v1/uploadSpringmvcSchema/upload",
                new HttpEntity<>(map, headers), String.class);
            if (!containsAll(result, "hello1", "中文 2", message)) {
              LOGGER.error("failed result is {}, index is ={}", result, index);
              failed.set(true);
            }
          } catch (RestClientException e) {
            e.printStackTrace();
            failed.set(true);
          }
          latch.countDown();
        }
      }.start();
    }

    latch.await();
    return "failed=" + failed.get();
  }

  private static boolean containsAll(String str, String... strings) {
    for (String string : strings) {
      if (!str.contains(string)) {
        return false;
      }
    }
    return true;
  }
}
