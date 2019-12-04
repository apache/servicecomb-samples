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
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.SaleQualification;
import org.apache.servicecomb.samples.practise.houserush.sale.aggregate.view.SaleSummary;
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

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

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

  @PutMapping("house_orders/{saleId}/{houseOrderId}")
  public HouseOrder placeHouseOrder(@RequestHeader int customerId, @PathVariable int saleId,
      @PathVariable int houseOrderId) {
    return houseOrderService.placeHouseOrder(customerId, houseOrderId, saleId);
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
    return houseOrderService.findBackSale(saleId);
  }

  @Override
  @GetMapping("sales/order/{saleId}")
  public Sale findOrderSale(@PathVariable int saleId) {
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
    return houseOrderService.findMyFavorite(customerId);
  }

  @Override
  @DeleteMapping("favorites/{id}")
  public void removeFavorite(@RequestHeader int customerId, @PathVariable int id) {
    Favorite favorite = houseOrderService.findFavorite(id);
    if (favorite.getCustomerId() != customerId) {
      throw new InvocationException(HttpStatus.SC_BAD_REQUEST, "",
          "cannot remove favorite not belong the current customer.");
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
  public List<SaleSummary> indexSales() {

    List<Sale> saleList = houseOrderService.indexSales();
    List<SaleSummary> saleSummaryList = new ArrayList<>();

    //join the the realestate info.
    saleList.forEach(sale -> {
      Realestate realestate = realestateApi.findRealestate(sale.getRealestateId());
      SaleSummary saleSummary = new SaleSummary();
      saleSummary.setId(sale.getId());
      saleSummary.setRealestateName(realestate.getName());
      saleSummary.setBeginAt(sale.getBeginAt());
      saleSummary.setEndAt(sale.getEndAt());
      saleSummary.setState(sale.getState());
      saleSummary.setRealestateId(sale.getRealestateId());
      saleSummaryList.add(saleSummary);
    });

    return saleSummaryList;
  }

  @Override
  @PutMapping("sale_qualification")
  public void updateSaleQualification(@RequestBody List<SaleQualification> saleQualifications) {
    houseOrderService.updateSaleQualification(saleQualifications);
  }
}