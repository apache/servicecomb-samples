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

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.rpc.UserApi;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.rpc.po.User;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.service.CustomerManageService;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.aggregate.Customer;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.aggregate.Qualification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestSchema(schemaId = "customerManageApiRest")
@RequestMapping("/")
public class CustomerManageApiRestImpl implements CustomerManageApi {

  @RpcReference(microserviceName = "login", schemaId = "userApiRest")
  private UserApi userApi;

  @Autowired
  private CustomerManageService customerManageService;

  @PostMapping("customers")
  public Customer createCustomer(@RequestBody Customer customer) {
    User user = new User();
    user.setUsername(customer.getRealName());
    user.setPassword("123456");
    userApi.createUser(user);
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
    // refresh customer
    customer = customerManageService.findCustomer(id);
    return customer;
  }

  @GetMapping("customers/{customerId}/sales/{saleId}/qulification_count")
  public int getQualificationsCount(@PathVariable int customerId, @PathVariable int saleId) {
    return customerManageService.getQualificationsCount(customerId, saleId);

  }
}