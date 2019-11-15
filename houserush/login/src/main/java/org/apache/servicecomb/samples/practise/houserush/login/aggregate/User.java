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

package org.apache.servicecomb.samples.practise.houserush.login.aggregate;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
@SQLDelete(sql = "update users set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
@EntityListeners(AuditingEntityListener.class)
public class User {
  // This is secret key, you can change it to what you want
  private final static String USER_SECRET = "231sdfqwer21313123cdsafkhioerutieweirqwuqbjffbqwrwr3";
  private final static String HASH_TYPE = "HmacSHA256";
  private final static int TOKEN_EXPIRED_MINUTES = 600;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String username;

  @Transient
  private String password;

  @Transient
  private String oldPassword;

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


  /**
   * Make hashed password, use username as salt, and return the Base64 encoded bytes.
   */
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

  private static Algorithm algorithm = null;
  private static JWTVerifier verifier = null;

  static {
    algorithm = Algorithm.HMAC256(USER_SECRET);
    verifier = JWT.require(algorithm)
        .build();
  }

  /**
   * Generate JWT token for user with TOKEN_EXPIRED_MINUTES minutes.
   *
   * @return the jwt token
   */
  public String generateToken() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, TOKEN_EXPIRED_MINUTES);
    token = JWT.create().withSubject(String.valueOf(id)).withExpiresAt(calendar.getTime()).sign(algorithm);
    return token;
  }

  /**
   * verify the jwt token.
   *
   * @param token
   * @return the user id
   */
  public static int verifyTokenGetUserId(String token) {
    String sub = verifier.verify(token).getSubject();
    if (StringUtils.isNotBlank(sub)) {
      return Integer.parseInt(sub);
    }
    throw new RuntimeException("verify the token fails");
  }
}
