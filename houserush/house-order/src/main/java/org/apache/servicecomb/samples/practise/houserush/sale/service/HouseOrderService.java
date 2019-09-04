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

package org.apache.servicecomb.samples.practise.houserush.sale.service;

import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.Favorite;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.HouseOrder;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.Sale;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.SaleQualification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HouseOrderService {
  List<HouseOrder> createHouseOrders(int saleId, List<Integer> houseIds);

  HouseOrder placeHouseOrder(int customerId, int houseOrderId, int saleId);

  HouseOrder cancelHouseOrder(int customerId, int houseOrderId);

  Favorite addFavorite(int customerId, int houseOrderId);

  Favorite findFavorite(int id);

  void removeFavorite(int id);

  Sale createSale(Sale sale);

  Sale findSale(int saleId);

  Sale updateSale(Sale sale);

  void removeSale(int saleId);

  List<Sale> indexSales();

  void updateSaleQualification(List<SaleQualification> saleQualifications);
}
