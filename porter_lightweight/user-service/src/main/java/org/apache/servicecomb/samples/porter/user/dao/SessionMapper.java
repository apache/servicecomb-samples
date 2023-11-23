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
import org.apache.ibatis.annotations.Update;
import org.apache.servicecomb.samples.porter.user.api.SessionInfo;

@Mapper
public interface SessionMapper {
  @Insert("""
      insert into T_SESSION (SESSION_ID, USER_NAME, ROLE_NAME)
        values(#{sessiondId,jdbcType=VARCHAR}, #{userName,jdbcType=VARCHAR},
               #{roleName,jdbcType=VARCHAR})""")
  void createSession(SessionInfo sessionInfo);

  @Select("""
      select ID, SESSION_ID, USER_NAME, ROLE_NAME, CREATION_TIME, ACTIVE_TIME
        from T_SESSION where SESSION_ID = #{0,jdbcType=VARCHAR}""")
  @Results({
      @Result(property = "id", column = "ID"),
      @Result(property = "sessionId", column = "SESSION_ID"),
      @Result(property = "userName", column = "USER_NAME"),
      @Result(property = "roleName", column = "ROLE_NAME"),
      @Result(property = "creationTime", column = "CREATION_TIME"),
      @Result(property = "activeTime", column = "ACTIVE_TIME")
  })
  SessionInfoModel getSessionInfo(String sessionId);

  @Update("""
      update T_SESSION set CREATION_TIME = now()
        where SESSION_ID = #{sessionId,jdbcType=VARCHAR}
        """)
  void updateSessionInfo(String sessionId);
}
