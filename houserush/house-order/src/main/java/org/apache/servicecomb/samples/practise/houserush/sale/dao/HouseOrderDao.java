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

package org.apache.servicecomb.samples.practise.houserush.sale.dao;

import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.HouseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

public interface HouseOrderDao extends JpaRepository<HouseOrder, Integer> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT ho FROM HouseOrder ho WHERE ho.sale.id = ?1 and ho.houseId in (?2)")
  List<HouseOrder> findAllBySaleIdAndHouseIdInForUpdate(int saleId, List<Integer> ids);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT ho FROM HouseOrder ho WHERE ho.id = ?1")
  HouseOrder findOneForUpdate(int id);

  @Modifying
  @Query(value ="UPDATE house_orders SET customer_id = ?1,state=?2,ordered_at=?3 where id = ?4 and customer_id is null ",nativeQuery = true)
  int updateByIdAndCustomerIdIsNull(int customerId, String state, Date orderedAt, int houseOrderId);

  int countByCustomerIdAndSaleId(int customerId, int saleId);
}