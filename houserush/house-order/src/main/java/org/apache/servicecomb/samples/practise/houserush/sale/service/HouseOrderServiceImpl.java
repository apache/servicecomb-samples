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

import org.apache.http.HttpStatus;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.Favorite;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.HouseOrder;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.Sale;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.SaleQualification;
import org.apache.servicecomb.samples.practise.houserush.sale.dao.FavoriteDao;
import org.apache.servicecomb.samples.practise.houserush.sale.dao.HouseOrderDao;
import org.apache.servicecomb.samples.practise.houserush.sale.dao.SaleDao;
import org.apache.servicecomb.samples.practise.houserush.sale.dao.SaleQualificationDao;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.CustomerManageApi;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.RealestateApi;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.House;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.Realestate;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HouseOrderServiceImpl implements HouseOrderService {
  @Autowired
  HouseOrderDao houseOrderDao;

  @Autowired
  SaleDao saleDao;

  @Autowired
  FavoriteDao favoriteDao;

  @Autowired
  SaleQualificationDao saleQualificationDao;

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
      throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "some house is already in this sale.");
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
  public HouseOrder placeHouseOrder(int customerId,int houseOrderId) {
    HouseOrder houseOrder = houseOrderDao.findOne(houseOrderId);
    Sale sale = houseOrder.getSale();
    if (null != sale && "opening".equals(sale.getState())) {
      SaleQualification qualification  = saleQualificationDao.findBySaleIdAndCustomerId(sale.getId(),customerId);
      if(!qualification.hasPlaceQualification()){
        throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "do not have the enough qualification to buy houses in this sale, " +
            "the qualifications count is " + qualification.getQualificationCount() + " , the order count is " + qualification.getOrderCount());
      }
      houseOrder.setCustomerId(customerId);
      houseOrder.setState("confirmed");
      houseOrder.setOrderedAt(new Date());
      int count = houseOrderDao.updateByIdAndCustomerIdIsNull(customerId,houseOrder.getState(),houseOrder.getOrderedAt(),houseOrderId);
      if(count<1){
        throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "this house have been occupied first by other customer, please choose another house or try it later.");
      }
      qualification.addOrderCount();
      saleQualificationDao.save(qualification);
      return houseOrder;
    } else {
      throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "this house which you chose does not belong to the current sale.");
    }
  }

  @Override
  @Transactional
  public HouseOrder cancelHouseOrder(int customerId, int houseOrderId) {
    HouseOrder houseOrder = houseOrderDao.findOneForUpdate(houseOrderId);
    Sale sale = houseOrder.getSale();

    if (null != sale && "opening".equals(sale.getState())) {
      if (customerId == houseOrder.getCustomerId()) {
        houseOrder.setCustomerId(null);
        houseOrder.setState("new");
        houseOrder.setOrderedAt(null);
        houseOrderDao.save(houseOrder);
        return houseOrder;
      } else {
        throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "cannot unoccupied the house which have not been occupied first by current customer first!");
      }
    } else {
      throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "this house which you chose does not belong to the current sale.");
    }
  }

  @Override
  @Transactional
  public Favorite addFavorite(int customerId, int houseOrderId) {
    HouseOrder houseOrder = houseOrderDao.findOne(houseOrderId);
    if (null == houseOrder) {
      throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "this houseOrder you chose does not exist");
    }
    if (favoriteDao.countByCustomerIdAndHouseOrderId(customerId, houseOrderId) == 0) {
      Favorite favorite = new Favorite();
      favorite.setCustomerId(customerId);
      favorite.setHouseOrder(houseOrder);

      favoriteDao.save(favorite);
      return favorite;
    } else {
      throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "this house which you chose is already marked favorite by you.");
    }
  }

  @Override
  public Favorite findFavorite(int id) {
    return favoriteDao.findOne(id);
  }

  @Override
  public void removeFavorite(int id) {
    favoriteDao.delete(id);
  }

  @Override
  public Sale createSale(Sale sale) {
    sale.setRealestateName(realestateApi.findRealestate(sale.getId()).getName());
    return saleDao.save(sale);
  }

  @Override
  public Sale findSale(int saleId) {
    return saleDao.findOne(saleId);
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

  @Override
  public void updateSaleQualification(List<SaleQualification> saleQualifications){
    if(null!=saleQualifications){
      saleQualifications.forEach(saleQualification -> {
        SaleQualification qualification = saleQualificationDao.findBySaleIdAndCustomerId(saleQualification.getSaleId(),saleQualification.getCustomerId());
        if(null == qualification){
          saleQualification.setOrderCount(0);
          saleQualificationDao.save(saleQualification);
        }else{
          qualification.setQualificationCount(saleQualification.getQualificationCount());
          saleQualificationDao.save(qualification);
        }
      });
    }
  }
}
