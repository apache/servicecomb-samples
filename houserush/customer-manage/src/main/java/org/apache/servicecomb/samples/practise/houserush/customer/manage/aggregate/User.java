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

package org.apache.servicecomb.samples.practise.houserush.customer.manage.aggregate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Data
public class User {
  private final static String USER_SECRET = "231sdfqwer21313123cafkhioerutieweirqwuqbjffbqwrwr3";
  private final static String HASH_TYPE = "HmacSHA256";
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String username;

  @Transient
  private String password;

  @JsonIgnore
  private String hashedPassword;

  @Temporal(TemporalType.TIMESTAMP)
  private Date deletedAt;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @Transient
  private String token;

  public String makeHashedPassword(String password) {
    try {
      String data = username + password;
      SecretKey secretKey = new SecretKeySpec(USER_SECRET.getBytes(), HASH_TYPE);
      Mac mac = Mac.getInstance(HASH_TYPE);
      mac.init(secretKey);
      byte[] bytes = mac.doFinal(data.getBytes());
      return new String(Base64.getEncoder().encode(bytes));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String generateToken() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, 30);
    Algorithm algorithm = Algorithm.HMAC256(USER_SECRET);
    token = JWT.create().withSubject(String.valueOf(id)).withExpiresAt(calendar.getTime()).sign(algorithm);
    return token;
  }

  private static Algorithm algorithm = null;
  private static JWTVerifier verifier = null;

  {
    algorithm = Algorithm.HMAC256(USER_SECRET);
    verifier = JWT.require(algorithm)
        .build();
  }

  public static int verifyTokenGetUserId(String token) {
    String sub = verifier.verify(token).getSubject();
    if (StringUtils.isNotBlank(sub)) {
      return Integer.parseInt(sub);
    }
    throw new RuntimeException("verify the token fails");
  }
}
