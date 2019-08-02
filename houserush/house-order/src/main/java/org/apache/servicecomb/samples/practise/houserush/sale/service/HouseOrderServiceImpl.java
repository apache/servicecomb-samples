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

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.HouseOrder;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.Sale;
import org.apache.servicecomb.samples.practise.houserush.sale.dao.HouseOrderDao;
import org.apache.servicecomb.samples.practise.houserush.sale.dao.SaleDao;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.CustomerManageApi;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.RealestateApi;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.Customer;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.House;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.Realestate;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HouseOrderServiceImpl implements HouseOrderService {
  @Autowired
  HouseOrderDao houseOrderDao;

  @Autowired
  SaleDao saleDao;

  @RpcReference(microserviceName = "realestate", schemaId = "realestateApiRest")
  private RealestateApi realestateApi;

  @RpcReference(microserviceName = "customer-manage", schemaId = "customerManageApiRest")
  private CustomerManageApi customerManageApi;

  @Override
  @Transactional
  public List<HouseOrder> createHouseOrders(int saleId, List<Integer> houseIds) {
    Sale sale = saleDao.findOne(saleId);
    if (null == sale) {
      throw new DataRetrievalFailureException("cannot create house for the non-existed sale.");
    }

    List<HouseOrder> houseOrders = houseOrderDao.findAllBySaleIdAndHouseIdInForUpdate(saleId, houseIds);
    if (!houseOrders.isEmpty()) {
      throw new InvocationException(400, "", "some house is already in this sale.");
    }

    List<House> houses = realestateApi.lockHousesForSale(houseIds);
    List<HouseOrder> resHouseOrders = new ArrayList<>();
    houses.forEach(house -> {
      HouseOrder houseOrder = new HouseOrder();
      houseOrder.setHouseId(house.getId());
      houseOrder.setSale(sale);
      houseOrderDao.save(houseOrder);
      resHouseOrders.add(houseOrder);

    });
    return resHouseOrders;
  }

  @Override
  @Transactional
  public HouseOrder placeHouseOrder(int customerId, int houseOrderId) {
    HouseOrder houseOrder = houseOrderDao.findOneForUpdate(houseOrderId);
    Sale sale = houseOrder.getSale();

    if (null != houseOrder) {
      if (null == houseOrder.getCustomerId()) {
        Customer customer = customerManageApi.findCustomer(customerId);
        int qualificationsCount = customerManageApi.getQualificationsCount(customerId, sale.getId());

        int ordersCount = houseOrderDao.countByCustomerIdAndSaleId(customerId, sale.getId());

        if (qualificationsCount <= ordersCount) {
          throw new InvocationException(400, "", "do not have the enough qualification to buy houses in this sale, " +
              "the qualifications count is " + qualificationsCount + " , the order count is " + ordersCount);
        }

        houseOrder.setCustomerId(customerId);
        houseOrder.setState("confirmed");
        houseOrderDao.save(houseOrder);
        return houseOrder;
      } else {
        throw new InvocationException(400, "", "该住宅已被其他人选购，请选择其他房产。");
      }
    } else {
      throw new InvocationException(400, "", "选择的房产不在此次抢购活动中");
    }
  }

  @Override
  public Sale createSale(Sale sale) {
    return saleDao.save(sale);
  }

  @Override
  public Sale findSale(int saleId) {
    Sale sale = saleDao.findOne(saleId);
    Realestate realestate = realestateApi.findRealestate(sale.getRealestateId());
    sale.setRealestateName(realestate.getName());
    return sale;
  }

  @Override
  @Transactional
  public Sale updateSale(Sale sale) {
    int id = sale.getId();
    if (saleDao.exists(id)) {
      return saleDao.save(sale);
    } else {
      throw new DataRetrievalFailureException("cannot update the none-existed sale");
    }
  }

  @Override
  public void removeSale(int saleId) {
    saleDao.delete(saleId);
  }

  @Override
  public List<Sale> indexSales() {
    return saleDao.findAll();
  }
}
