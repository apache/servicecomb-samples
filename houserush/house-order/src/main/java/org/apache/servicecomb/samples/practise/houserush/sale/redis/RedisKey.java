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

package org.apache.servicecomb.samples.practise.houserush.sale.redis;

import org.springframework.stereotype.Component;

@Component
public class RedisKey {
  private final String SALEHASHID = "SALEHASHID_";
  private final String SALEID = "SALEID_";
  private final String HOUSEORDERIDTOSALEID = "HOUSEORDER2SALEID_";
  private final String SALENOHOUSEORDER = "SALENOHOUSEORDER_";

  public String getSaleHashKey(int id) {
    return SALEHASHID + id;
  }
  public String getSaleKey(int id) {
    return SALEID + id;
  }
  public String getHouseOrderIdToSaleId(int id){
    return HOUSEORDERIDTOSALEID+id;
  }
  public String getSaleNoHouseOrder(int saleId){
    return SALENOHOUSEORDER+saleId;
  }
}
