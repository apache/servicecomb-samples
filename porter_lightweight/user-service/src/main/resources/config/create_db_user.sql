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

CREATE DATABASE IF NOT EXISTS porter_user_db;

USE porter_user_db;

DROP TABLE IF EXISTS T_USER;

CREATE TABLE `T_USER` (
  `ID`  INTEGER(8) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USER_NAME`  VARCHAR(64) NOT NULL COMMENT '用户名称',
  `PASSWORD`  VARCHAR(64) NOT NULL COMMENT '用户密码',
  `ROLE_NAME`  VARCHAR(64) NOT NULL COMMENT '角色名称',
  PRIMARY KEY (`ID`)
);

#### password is encrypted for test
insert into T_USER(USER_NAME, PASSWORD, ROLE_NAME) values("admin", "n4bQgYhMfWWaL+qgxVrQFaO/TxsrC4Is0V1sFbDwCgg=", "admin");
insert into T_USER(USER_NAME, PASSWORD, ROLE_NAME) values("guest", "n4bQgYhMfWWaL+qgxVrQFaO/TxsrC4Is0V1sFbDwCgg=", "guest");

DROP TABLE IF EXISTS T_SESSION;

CREATE TABLE `T_SESSION` (
  `ID`  INTEGER(8) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `SESSION_ID`  VARCHAR(64) NOT NULL COMMENT '临时会话ID',
  `USER_NAME`  VARCHAR(64) NOT NULL COMMENT '用户名称',
  `ROLE_NAME`  VARCHAR(64) NOT NULL COMMENT '角色名称',
  `CREATION_TIME`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ACTIVE_TIME`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近活跃时间',
  PRIMARY KEY (`ID`)
);
