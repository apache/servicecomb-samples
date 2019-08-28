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

package org.apache.servicecomb.samples.practise.houserush.customer.manage.service;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.aggregate.Customer;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.aggregate.Qualification;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.dao.CustomerDao;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.dao.QualificationDao;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.rpc.HouseOrderApi;
import org.apache.servicecomb.samples.practise.houserush.customer.manage.rpc.po.SaleQualification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerManageServiceImpl implements CustomerManageService {

  @Autowired
  private CustomerDao customerDao;

  @Autowired
  private QualificationDao qualificationDao;

  @RpcReference(microserviceName = "house-order", schemaId = "houseOrderApiRest")
  private HouseOrderApi houseOrderApi;

  @Override
  public Customer createCustomer(Customer customer) {
    return customerDao.save(customer);
  }

  @Override
  public Customer updateCustomer(Customer customer) {
    int id = customer.getId();
    if (customerDao.exists(id)) {
      return customerDao.save(customer);
    } else {
      throw new DataRetrievalFailureException("cannot update the non-existed customer");
    }
  }

  @Override
  public Customer findCustomer(int id) {
    return customerDao.findOne(id);
  }

  @Override
  public void removeCustomer(int id) {
    customerDao.delete(id);
  }

  @Override
  public List<Customer> indexCustomers() {
    return customerDao.findAll();
  }

  @Override
  public boolean updateCustomerQualifications(Customer customer, List<Qualification> qualifications) {
    customer.setQualifications(qualifications);
    qualifications.forEach(qualification -> qualification.setCustomer(customer));
    customerDao.saveAndFlush(customer);
    Map<Integer,Long> map = qualifications.stream().collect(Collectors.groupingBy(Qualification::getSaleId,Collectors.counting()));
    List<SaleQualification> saleQualifications = new ArrayList<>();
    map.forEach((k,v)->saleQualifications.add(new SaleQualification(customer.getId(),k,v.intValue())));
    houseOrderApi.updateSaleQualification(saleQualifications);
    return true;
  }

  @Override
  public int getQualificationsCount(int customerId, int saleId) {
    return qualificationDao.countByCustomerIdAndSaleId(customerId, saleId);
  }
}
