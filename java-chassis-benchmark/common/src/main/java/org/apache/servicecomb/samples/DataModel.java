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

import java.util.HashMap;
import java.util.Map;

public class DataModel {
  private Map<String, ChildDataModel> data;

  public Map<String, ChildDataModel> getData() {
    return data;
  }

  public void setData(Map<String, ChildDataModel> data) {
    this.data = data;
  }

  public static DataModel create(String key, int size) {
    DataModel dataModel = new DataModel();
    Map<String, ChildDataModel> data = new HashMap<String, ChildDataModel>(size);

    for (int i = 0; i < size; i++) {
      ChildDataModel childDataModel = new ChildDataModel();
      childDataModel.setData("Have a nice day");
      childDataModel.setNumDouble(3892382939.3333893D);
      childDataModel.setNumFloat(87939873297.334F);
      childDataModel.setNumInt(89273847);
      childDataModel.setNumLong(983798787837438L);
      data.put(key + i, childDataModel);
    }

    dataModel.setData(data);
    return dataModel;
  }
}
