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

package org.apache.servicecomb.samples.practise.houserush.realestate.filesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;

@Component
public class LocalFileStorage extends FileStorage{

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileStorage.class);

  private static final String localPath = System.getProperty("user.home") + File.separator
      + "houserush" + File.separator
      + "image" + File.separator;

  static {
    File file = new File(localPath);
    if(!file.exists()){
      file.mkdirs();
    }
  }

  @Override
  public int storage(InputStream inputStream) {
    int id = (new Date()).hashCode();
    try {
      File file = new File(localPath + id + ".jpeg");
      if(file.exists()){
        file.delete();
      }
      file.createNewFile();

      int len = 1024;
      byte[] bytes = new byte[len];

      OutputStream os = new FileOutputStream(file);

      while ((len = inputStream.read(bytes)) != -1) {
        os.write(bytes, 0, len);
      }
      os.close();
      inputStream.close();
      return id;
    }catch (IOException e){
      LOGGER.error(e.getMessage(), e);
      return -1;
    }
  }

  @Override
  public void delete(int id) {
    File file = new File(localPath + id + ".jpeg");
    if(file.exists()){
      file.delete();
    }
  }

  @Override
  public InputStream getFile(int id) {
    try {
      File file = new File(localPath + id + ".jpeg");
      if(file.exists()){
        return new FileInputStream(file);
      }
    }catch (IOException e){
      LOGGER.error(e.getMessage(), e);
    }
    return null;
  }
}
