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

import org.apache.servicecomb.samples.porter.user.api.SessionInfo;

public class SessionInfoModel {
  private int id;

  private String sessiondId;

  private String userName;

  private String roleName;

  private java.sql.Timestamp creationTime;

  private java.sql.Timestamp activeTime;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSessiondId() {
    return sessiondId;
  }

  public void setSessiondId(String sessiondId) {
    this.sessiondId = sessiondId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public java.sql.Timestamp getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(java.sql.Timestamp creationTime) {
    this.creationTime = creationTime;
  }

  public java.sql.Timestamp getActiveTime() {
    return activeTime;
  }

  public void setActiveTime(java.sql.Timestamp activeTime) {
    this.activeTime = activeTime;
  }

  public static SessionInfo toSessionInfo(SessionInfoModel entity) {
    SessionInfo info = new SessionInfo();
    info.setSessiondId(entity.getSessiondId());
    info.setUserName(entity.getUserName());
    info.setRoleName(entity.getRoleName());
    return info;
  }
}
