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

package org.apache.servicecomb.samples.practise.houserush.sale.api;

import org.apache.http.HttpStatus;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.Favorite;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.HouseOrder;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.Sale;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.CustomerManageApi;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.RealestateApi;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.Customer;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.House;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.Qualification;
import org.apache.servicecomb.samples.practise.houserush.sale.rpc.po.Realestate;
import org.apache.servicecomb.samples.practise.houserush.sale.service.HouseOrderService;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestSchema(schemaId = "houseOrderApiRest")
@RequestMapping("/")
public class HouseOrderApiRestImpl implements HouseOrderApi {

  @RpcReference(microserviceName = "realestate", schemaId = "realestateApiRest")
  private RealestateApi realestateApi;

  @Autowired
  HouseOrderService houseOrderService;

  @PostMapping("sales/{saleId}/house_orders")
  public List<HouseOrder> createHouseOrders(@PathVariable int saleId, @RequestBody List<Integer> houseIds) {
    return houseOrderService.createHouseOrders(saleId, houseIds);
  }

  @PutMapping("house_orders/{houseOrderId}")
  public HouseOrder placeHouseOrder(@RequestHeader int customerId, @PathVariable int houseOrderId) {
    return houseOrderService.placeHouseOrder(customerId, houseOrderId);
  }

  @PutMapping("house_orders/{houseOrderId}/cancel")
  public HouseOrder cancelHouseOrder(@RequestHeader int customerId, @PathVariable int houseOrderId) {
    return houseOrderService.cancelHouseOrder(customerId, houseOrderId);
  }

  @Override
  @GetMapping("house_orders/{houseOrderId}")
  public HouseOrder findOne(@PathVariable int houseOrderId) {
    return houseOrderService.findOne(houseOrderId);
  }

  @Override
  @PostMapping("sales")
  public Sale createSale(@RequestBody Sale sale) {
    return houseOrderService.createSale(sale);
  }

  @Override
  @GetMapping("sales/{saleId}")
  public Sale findSale(@PathVariable int saleId) {
    return houseOrderService.findSale(saleId);
  }

  @Override
  @GetMapping("sales/{realestateId}")
  public Sale findSaleByRealestateId(@PathVariable int realestateId) {
    return houseOrderService.findSaleByRealestateId(realestateId);
  }

  @Override
  @PutMapping("sales/{saleId}")
  public Sale updateSale(@PathVariable int saleId, @RequestBody Sale sale) {
    sale.setId(saleId);
    return houseOrderService.updateSale(sale);
  }

  @Override
  @PutMapping("house_orders/{houseOrderId}/add_favorite")
  public Favorite addFavorite(@RequestHeader int customerId, @PathVariable int houseOrderId) {
    return houseOrderService.addFavorite(customerId, houseOrderId);
  }

  @Override
  @GetMapping("favorites")
  public List<Favorite> findMyFavorite(@RequestHeader int customerId) {
    return  houseOrderService.findMyFavorite(customerId);
  }

  @Override
  @DeleteMapping("favorites/{id}")
  public void removeFavorite(@RequestHeader int customerId, @PathVariable int id) {
    Favorite favorite = houseOrderService.findFavorite(id);
    if (favorite.getCustomerId() != customerId) {
      throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "", "cannot remove favorite not belong the current customer.");
    }
    houseOrderService.removeFavorite(id);
  }

  @Override
  @DeleteMapping("sales/{saleId}")
  public void removeSale(@PathVariable int saleId) {
    houseOrderService.removeSale(saleId);
  }

  @Override
  @GetMapping("sales")
  public List<Sale> indexSales() {
    List<Sale>  saleList= houseOrderService.indexSales();
    saleList.forEach(sale -> {
      Realestate realestate = realestateApi.findRealestate(sale.getRealestateId());
      sale.setRealestateName(realestate.getName());
    });
    return saleList;
  }



  @Override
  @GetMapping("sales/indexAllSales")
  public List<Sale> indexAllSales() {
    List<Sale>  saleList= houseOrderService.indexSales();
    return saleList;
  }

  @RpcReference(microserviceName = "customer-manage", schemaId = "customerManageApiRest")
  private CustomerManageApi customerManageApi;

  @GetMapping("sales/list")
  public List<Sale> indexListSales(@RequestHeader int customerId) {
    List<Sale>  saleList = new ArrayList<>();
    //查询我的购房资格列表
    Customer customer = customerManageApi.findCustomer(customerId);
    if(customer == null){
      return saleList;
    }
    //购房销售活动列表
    List<Qualification> qualifications = customer.getQualifications();

    qualifications.forEach(qualification ->{
      //所有资格的活动
      Sale sale = houseOrderService.findSale(qualification.getSaleId());
         //获取楼盘名称
      Realestate realestate = realestateApi.findRealestate(sale.getRealestateId());
      sale.setRealestateName(realestate.getName());
      saleList.add(sale);
    });
    return saleList;
  }



  @GetMapping("sales/details/{saleId}")
  public List<Sale> indexDetailsSales(@RequestHeader int customerId, @PathVariable int saleId) {
    List<Sale>  saleList = new ArrayList<>();
    //查询我的购房资格列表
    Customer customer = customerManageApi.findCustomer(customerId);

    if(customer == null){
      return saleList;
    }
    //购房销售活动列表
    List<Qualification> qualifications = customer.getQualifications();

    qualifications.forEach(qualification ->{
      //所有资格的活动
      Sale sale = houseOrderService.findSale(qualification.getSaleId());
      //查看当前楼盘活动列表
      if(sale.getId().equals(saleId)){
        List<HouseOrder> houseOrders = sale.getHouseOrders();
        houseOrders.forEach(houseOrder ->{
          //房屋
          House house = realestateApi.findHouse(houseOrder.getHouseId());
          houseOrder.setHouseName(house.getName());//名称
          houseOrder.setPrice(house.getPrice());//价格
          houseOrder.setBuilDingName(house.getBuilding().getName());//楼栋名称

        } );
        saleList.add(sale);
      }
    });
    return saleList;
  }

  /**
   * 所有活动订单状态
   * @return
   */
  @GetMapping("sales/indexOrderSales")
  public List<Sale> indexOrderSales() {
    List<Sale>  saleList= houseOrderService.indexSales();
    saleList.forEach(sale -> {
      List<HouseOrder> houseOrders = sale.getHouseOrders();
      houseOrders.forEach(houseOrder ->{
        //房屋
        House house = realestateApi.findHouse(houseOrder.getHouseId());
        houseOrder.setHouseName(house.getName());//名称
        houseOrder.setPrice(house.getPrice());//价格
        houseOrder.setBuilDingName(house.getBuilding().getName());//楼栋名称
      });
    });
    return saleList;
  }














}