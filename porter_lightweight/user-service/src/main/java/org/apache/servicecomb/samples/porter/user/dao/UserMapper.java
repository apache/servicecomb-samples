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

package org.apache.servicecomb.samples.porter.user.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
  @Insert("""
      insert into T_USER (ID, USER_NAME, PASSWORD, ROLE_NAME)
        values(#{id,jdbcType=INTEGER}, #{userName,jdbcType=VARCHAR},
               #{password,jdbcType=VARCHAR}, #{roleName,jdbcType=VARCHAR})""")
  void createUser(UserInfo userInfo);

  @Select("""
      select ID, USER_NAME, PASSWORD, ROLE_NAME
        from T_USER where USER_NAME = #{userName,jdbcType=VARCHAR}""")
  @Results({
      @Result(property = "id", column = "ID"),
      @Result(property = "userName", column = "USER_NAME"),
      @Result(property = "password", column = "PASSWORD"),
      @Result(property = "roleName", column = "ROLE_NAME")
  })
  UserInfo getUserInfo(String userName);
}
