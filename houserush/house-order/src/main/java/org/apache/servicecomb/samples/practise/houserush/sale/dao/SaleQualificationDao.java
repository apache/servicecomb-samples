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

import javax.persistence.LockModeType;

import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.SaleQualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface SaleQualificationDao extends JpaRepository<SaleQualification, Integer> {

  //avoid user place houseOrders outnumber qualifications
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  SaleQualification findBySaleIdAndCustomerId(int saleId, int customerId);
}
