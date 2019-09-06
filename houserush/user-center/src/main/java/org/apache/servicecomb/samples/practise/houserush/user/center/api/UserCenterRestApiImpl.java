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

package org.apache.servicecomb.samples.practise.houserush.user.center.api;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.practise.houserush.user.center.rpc.CustomerManageApi;
import org.apache.servicecomb.samples.practise.houserush.user.center.rpc.HouseOrderApi;
import org.apache.servicecomb.samples.practise.houserush.user.center.rpc.RealestateApi;
import org.apache.servicecomb.samples.practise.houserush.user.center.rpc.po.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestSchema(schemaId = "userCenterApiRest")
@RequestMapping("/")
public class UserCenterRestApiImpl implements UserCenterApi {

  @RpcReference(microserviceName = "house-order", schemaId = "houseOrderApiRest")
  private HouseOrderApi houseOrderApi;

  @RpcReference(microserviceName = "realestate", schemaId = "realestateApiRest")
  private RealestateApi realestateApi;

  @RpcReference(microserviceName = "customer-manage", schemaId = "customerManageApiRest")
  private CustomerManageApi customerManageApi;

  /**
   * 收藏
   * @param customerId
   * @return
   */
  @Override
  @GetMapping("favorites")
  public List<Favorite> findMyFavorite(@RequestHeader int customerId) {
    List<Favorite> favorites =  houseOrderApi.findMyFavorite(customerId);
    favorites.forEach(favorite -> {
      House house = realestateApi.findHouse(favorite.getHouseOrderId());
      favorite.setHouseName(house.getName());//名称
      favorite.setPrice(house.getPrice());//价格
      favorite.setBuilDingName(house.getBuilding().getName());//楼栋名称
      favorite.setRealestateName(house.getBuilding().getRealestate().getName());//楼盘名称
      Sale sale = houseOrderApi.findSaleByRealestateId(house.getBuilding().getRealestate().getId());
      //房屋订单状态
      HouseOrder houseOrder = houseOrderApi.findOne(favorite.getHouseOrderId());
      favorite.setState(houseOrder.getState());
      //楼盘活动开售状态
      //favorite.setState(sale.getState());
      favorite.setHouseOrderId(house.getId());
    });
    return favorites;
  }
  /**
   * 收藏详情
   * @param id
   * @return
   */
  @Override
  @GetMapping("favorites/{id}")
  public HouseDetail findByHouseIdDetail(@PathVariable int id){
    House house = realestateApi.findHouse(id);
    HouseDetail houseDetail = new HouseDetail();
    houseDetail.setHouseName(house.getName());//名称
    houseDetail.setPrice(house.getPrice());//价格
    houseDetail.setBuilDingName(house.getBuilding().getName());//楼栋名称
    houseDetail.setRealestateName(house.getBuilding().getRealestate().getName());//楼盘名称
    houseDetail.setHouseOrderId(id);
    HouseOrder houseOrder = houseOrderApi.findOne(id);
    houseDetail.setState(houseOrder.getState());//订单状态
    return  houseDetail;
  }

  /**
   * 我的购房资格
   * @param customerId
   * @return
   */
  @GetMapping("buyHouseNumber")
  public Customer findMyBuyHouseNumber(@RequestHeader int customerId){
    Customer customer =customerManageApi.findCustomer(customerId);
    List<Qualification> qualifications = customer.getQualifications();
    qualifications.forEach(qualification ->{
      //房屋
      House house = realestateApi.findHouse(qualification.getHouseId());
      qualification.setHouseName(house.getName());//名称
      qualification.setPrice(house.getPrice());//价格
      qualification.setBuilDingName(house.getBuilding().getName());//楼栋名称
      qualification.setRealestateName(house.getBuilding().getRealestate().getName());//楼盘名称
      //根据访问查看订单状态
      HouseOrder houseOrder = houseOrderApi.findOne(qualification.getHouseId());
      qualification.setState(houseOrder.getState());
    });
    return customer;
  }
}
