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

package org.apache.servicecomb.samples.practise.houserush.gateway.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.servicecomb.common.rest.codec.produce.ProduceProcessor;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

public class ProduceJsonProcessor implements ProduceProcessor {

  private ObjectMapper mapper = new ObjectMapper();

  public ProduceJsonProcessor() {
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    mapper.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    mapper.setDateFormat(format);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Override
  public String getName() {
    return MediaType.APPLICATION_JSON;
  }

  @Override
  public void doEncodeResponse(OutputStream output, Object result) throws Exception {
    mapper.writeValue(output, result);
  }

  @Override
  public Object doDecodeResponse(InputStream input, JavaType type) throws Exception {
    return mapper.readValue(input, type);
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
