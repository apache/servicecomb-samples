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

package org.apache.servicecomb.samples.practise.houserush.sale.aggregate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "house_orders")
@EntityListeners(AuditingEntityListener.class)
public class HouseOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "sale_id")
  private Sale sale;

  private Integer customerId;

  private String state = "new";

  private Integer houseId;

  @OneToMany(mappedBy = "houseOrder")
  @Fetch(FetchMode.JOIN)
  private List<Favorite> favorites = new ArrayList<>();

  @Temporal(TemporalType.TIMESTAMP)
  private Date orderedAt;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date CreatedAt;

  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date UpdatedAt;

  @Transient
  private String houseName;

  @Transient
  private BigDecimal price;

  @Transient
  private String builDingName;

  @Transient
  private String realestateName;
}