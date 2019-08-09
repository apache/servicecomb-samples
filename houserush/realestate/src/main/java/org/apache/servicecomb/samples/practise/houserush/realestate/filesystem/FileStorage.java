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

import java.io.InputStream;

public abstract class FileStorage {
  /**
   * storage the file
   * @param inputStream
   * @return an id map to the file,or -1 if storage failed.
   */
  public abstract int storage(InputStream inputStream);

  /**
   * delete file by its id
   * @param id
   */
  public abstract void delete(int id);

  /**
   * get file by its id
   * @param id
   * @return the file's InputStream if it exists, otherwise return {@link null}
   */
  public abstract InputStream getFile(int id);
}
