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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "sales")
@SQLDelete(sql = "update sales set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
@EntityListeners(AuditingEntityListener.class)
public class Sale {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String state = "new";

  @Temporal(TemporalType.TIMESTAMP)
  private Date beginAt;

  @Temporal(TemporalType.TIMESTAMP)
  private Date endAt;

  @OneToMany(mappedBy = "sale")
  private List<HouseOrder> houseOrders = new ArrayList<>();

  private Integer realestateId;

  @Transient
  private String realestateName;

  @Temporal(TemporalType.TIMESTAMP)
  private Date deletedAt;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  public String getState() {
    if ("published".equals(state)) {
      Date now = new Date();
      if (now.getTime() < beginAt.getTime()) {
        return "published";
      } else if (now.getTime() >= beginAt.getTime() && now.getTime() <= endAt.getTime()) {
        return "opening";
      } else {
        return "finished";
      }
    }
    return state;
  }
}