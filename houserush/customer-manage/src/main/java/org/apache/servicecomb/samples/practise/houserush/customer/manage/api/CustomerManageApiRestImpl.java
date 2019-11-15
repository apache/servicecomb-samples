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

package org.apache.servicecomb.samples.practise.houserush.customer.manage.api;

import java.util.List;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.aggregate.Customer;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.aggregate.Qualification;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.rpc.UserApi;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.rpc.po.User;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.service.CustomerManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Customer-Manage Core Api implement.
 */
@RestSchema(schemaId = "customerManageApiRest")
@RequestMapping("/")
public class CustomerManageApiRestImpl implements CustomerManageApi {

  private static String DEFAULT_PASSWORD = "123456";

  @RpcReference(microserviceName = "login", schemaId = "userApiRest")
  private UserApi userApi;

  @Autowired
  private CustomerManageService customerManageService;

  @PostMapping("customers")
  public Customer createCustomer(@RequestBody Customer customer) {
    User userReq = new User();
    userReq.setUsername(customer.getRealName());
    userReq.setPassword(DEFAULT_PASSWORD);
    User userRes = userApi.createUser(userReq);

    customer.setId(userRes.getId()); // Customer and user have the same id.
    return customerManageService.createCustomer(customer);
  }

  @GetMapping("customers/{id}")
  public Customer findCustomer(@PathVariable int id) {
    return customerManageService.findCustomer(id);
  }

  @PutMapping("customers/{id}")
  public Customer updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
    customer.setId(id);
    return customerManageService.updateCustomer(customer);
  }

  @DeleteMapping("customers/{id}")
  public void removeCustomer(@PathVariable int id) {
    customerManageService.removeCustomer(id);
  }

  @GetMapping("customers")
  public List<Customer> indexCustomers() {
    return customerManageService.indexCustomers();
  }

  @PutMapping(value = "customers/{id}/update_qualifications")
  public Customer updateCustomerQualifications(@PathVariable int id, @RequestBody List<Qualification> qualifications) {
    Customer customer = customerManageService.findCustomer(id);
    customerManageService.updateCustomerQualifications(customer, qualifications);
    return customer;
  }
}