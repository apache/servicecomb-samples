<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--
  ~ Licensed to the Apache Software Foundation (ASF) under one or more
  ~ contributor license agreements.  See the NOTICE file distributed with
  ~ this work for additional information regarding copyright ownership.
  ~ The ASF licenses this file to You under the Apache License, Version 2.0
  ~ (the "License"); you may not use this file except in compliance with
  ~ the License.  You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->
<html>
<head>
    <title>Legacy WAR with ServiceComb</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; }
        h2 { color: #333; }
        a { color: #0066cc; }
    </style>
</head>
<body>
    <h1>传统 WAR 包集成 ServiceComb 示例</h1>
    
    <div class="section">
        <h2>遗留系统组件（Legacy Components）</h2>
        <ul>
            <li><a href="legacy">传统 Servlet</a> - 通过 web.xml 配置的传统 Servlet</li>
            <li>本页面 (index.jsp) - 传统 JSP 页面</li>
        </ul>
    </div>
    
    <div class="section">
        <h2>ServiceComb 微服务（ServiceComb Microservices）</h2>
        <ul>
            <li><a href="rest/hello?name=World">ServiceComb REST API</a> - 通过 @RestSchema 注解的微服务接口</li>
        </ul>
    </div>
    
    <div class="section">
        <h2>特点说明</h2>
        <p>本项目展示了如何在已有的传统 Java Web 应用中集成 ServiceComb 微服务框架：</p>
        <ul>
            <li>传统 Servlet/Filter 与 ServiceComb 共存</li>
            <li>JSP 页面继续正常工作</li>
            <li>ServiceComb 微服务通过 Spring Boot 方式集成</li>
            <li>统一部署到外部 Tomcat</li>
        </ul>
    </div>
    
    <p><small>部署时间: <%= new java.util.Date() %></small></p>
</body>
</html>
