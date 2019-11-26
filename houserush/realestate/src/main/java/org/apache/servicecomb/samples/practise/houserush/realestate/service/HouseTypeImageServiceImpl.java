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

package org.apache.servicecomb.samples.practise.houserush.realestate.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.servicecomb.samples.practise.houserush.realestate.aggregate.HouseTypeImage;
import org.apache.servicecomb.samples.practise.houserush.realestate.filesystem.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HouseTypeImageServiceImpl implements HouseTypeImageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(HouseTypeImageServiceImpl.class);

  @Autowired
  FileStorage fileStorage;

  @Override
  public HouseTypeImage createHouseTypeImage(MultipartFile file) {
    HouseTypeImage result = null;
    try {
      int id = fileStorage.storage(file.getInputStream());
      if (id != -1) {
        result = new HouseTypeImage(id);
      }
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return result;
  }

  @Override
  public void removeHouseTypeImage(int id) {
    fileStorage.delete(id);
  }

  @Override
  public byte[] findHouseTypeImage(int id) {
    InputStream is = fileStorage.getFile(id);
    try {
      byte[] bytes = new byte[is.available()];
      is.read(bytes, 0, is.available());
      return bytes;
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return null;
  }
}
