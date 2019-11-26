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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "favorites")
@EntityListeners(AuditingEntityListener.class)
public class Favorite {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne(fetch= FetchType.LAZY)
  @JoinColumn(name = "house_order_id")
  @JsonIgnore
  private HouseOrder houseOrder;

  @Column(name = "house_order_id", insertable = false, updatable = false)
  private int houseOrderId;

  private Integer customerId;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date CreatedAt;

  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date UpdatedAt;
}