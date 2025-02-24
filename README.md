# ServiceComb samples

## Description

This project is samples for java-chassis 3.x.x. You can switch to other branches to see different samples.

## subprojects

### basic

A sample shows a provider, a consumer and a gateway using java chassis.

### basic-tomcat

A sample shows a provider, a consumer and a gateway using java chassis.
While provider and consumer are packaged as WAR and deploy in tomcat.

### bmi

A sample microservices application that demonstrates BMI (Body Mass Index) calculation,
consisting of a calculator service and a web interface service with gateway capabilities.
This example shows how to build and connect microservices in a practical scenario.

### java-chassis-deployment-samples

A collection of deployment samples demonstrating how to deploy java-chassis applications in external web containers,
including Spring Boot and traditional WAR deployments.

### java-chassis-interoprability

A demonstration of interoperability between Spring Cloud and Java Chassis frameworks,
featuring a Spring Cloud Gateway with cross-framework service invocations
using both ServiceComb and Nacos service registries.

### porter

A sample shows an application with basic login, download file, delete file functions.
Read
this [document](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-porter.html)
for details.

### java-chassis-samples

Featured samples for java-chassis modules. Each module contains 10 minutes guides.

### java-chassis-integration-tests

java-chassis integration tests。

> Please refer to the version in `servicecomb-java-chassis` as the primary reference.

### java-chassis-benchmark

The benchmark data compares three RESTful frameworks
(Dubbo 3.2.11, Spring Cloud 2022.0.x, and ServiceComb Java-Chassis 3.0.1) under various test conditions:
different payload sizes (1/100/1000), waiting times (0/10/100ms), with 10 threads and 1000 executions.
Historical data from previous tests (2023-12-5, 2023-11-16)
includes comparisons with different versions and JDK 8 environment,
both with and without access logging enabled.

## Precondition

Before running the samples，install:

- [service center](https://github.com/apache/servicecomb-service-center)
- [kie](https://github.com/apache/servicecomb-kie)

Or download [Lightweight Cloud Service Engine](https://support.huaweicloud.com/devg-cse/cse_04_0046.html) which
integrates both service center and kie for ease of use.

## 使用 ServiceComb 开发微服务的示例项目

这个项目使用 java-chassis 3.x.x 版本。可以切换分支，查看其他版本的例子。

## basic

该项目展示了 Java Chassis 开发的一个 provider， 一个 consumer 和一个网关服务。

## basic-tomcat

该项目展示了 Java Chassis 开发的一个 provider、一个 consumer 和一个网关服务。与 basic 项目不同的是，provider 和 consumer
被打包为 WAR 文件并部署在 tomcat 中。

## bmi

一个演示 BMI（身体质量指数）计算的微服务应用示例，包含一个计算器服务和具有网关功能的 Web 界面服务。该示例展示了如何在实际场景中构建和连接微服务。

## java-chassis-deployment-samples

一组部署示例，演示如何在外部 Web 容器中部署 java-chassis 应用，包括 Spring Boot 和传统的 WAR 部署方式。

## java-chassis-interoprability

展示 Spring Cloud 和 Java Chassis 框架之间的互操作性，特点是使用 Spring Cloud 网关通过 ServiceComb 和 Nacos
服务注册中心实现跨框架服务调用。

## porter

该项目演示了一个包括网关、文件下载、认证鉴权等功能的简单应用系统，同时演示了 ServiceComb
推荐的先写接口声明，再写业务代码的软件工程实践。可以阅读[文章](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-porter.html)
了解项目的细节。
这个例子的运行环境采用 `vert.x` 作为 HTTP 服务器，即 `REST over Vert.x transport`。

## java-chassis-samples

java-chassis 各个模块功能的例子。每个子项目包含了 10 分钟可以学习完成的简单示例。

## java-chassis-integration-tests

java-chassis 的集成测试用例。

## java-chassis-benchmark

性能基准测试数据，比较了三个 RESTful 框架（`Dubbo 3.2.11`、`Spring Cloud 2022.0.x` 和 `ServiceComb Java-Chassis
3.0.1`）在不同测试条件下的表现：包括不同数据负载（1/100/1000）、等待时间（0/10/100ms）、10 个线程和 1000
次执行。包含了之前测试（2023-12-5、2023-11-16）的历史数据，涵盖了不同版本和 JDK 8 环境下启用和禁用访问日志的对比。

## 前提条件

运行这些例子之前，需要先安装：

- [注册中心](https://github.com/apache/servicecomb-service-center)
- [配置中心](https://github.com/apache/servicecomb-kie)

> 华为云提供一个出色的[本地轻量化微服务引擎](https://support.huaweicloud.com/devg-cse/cse_04_0046.html) ,可以直接下载安装使用。
