# Java CSE Spring MVC microservice

## Description

| Language | Framework      | Platform                            |
| -------- | -------------- | ----------------------------------- |
| Java     | CSE Spring MVC | ServiceStage Container, CCE Cluster |

This sample code helps get you started with a simple Java CSE microservice
deployed by ServiceStage Container App to a CCE Cluster.

This sample includes:

- `README.md`
- `LICENSE`
- `Dockerfile`
- `pom.xml` - this file is the Maven Project Object Model for the microservice
- `src/main` - this directory contains your Java service source files
- `src/test` - this directory contains your Java service unit test files

## Precondition

see [Precondition](../README.md)

## Getting Started

Clone your code repository and start developing your application on IDE of your choice

1. Install maven. See https://maven.apache.org/install.html for details.

2. Download `local-service-center` from http://servicecomb.incubator.apache.org/release/.
   Read `README.md` to start`local-service-center`.

3. Update the service registry address in `microservice.yaml` according to the `README.md` in `local-service-center`.

4. Build the application.

   ```bash
   mvn -f pom.xml package
   ```

5. Run the application in IDE or execute the jar.

6. Open http://127.0.0.1:8080/rest/helloworld?name=hellworld in a web browser to view your application.

if you want to deploy the sample code on servicestage, View your CI/CD pipeline and service stack on ServiceStage and
customize it as per your needs

## 项目说明

| 语言 | 框架           | 平台                        |
| ---- | -------------- | --------------------------- |
| Java | CSE Spring MVC | ServiceStage 容器, CCE 集群 |

本示例代码帮助您开始使用一个简单的 Java CSE 微服务，该服务通过 ServiceStage 容器应用部署到 CCE 集群。

本示例包含：

- `README.md`
- `LICENSE`
- `Dockerfile`
- `pom.xml` - 这个文件是微服务的 Maven 项目对象模型
- `src/main` - 这个目录包含您的 Java 服务源文件
- `src/test` - 这个目录包含您的 Java 服务单元测试文件

## 前置条件

参见[前置条件](../README.md)

## 入门指南

克隆您的代码仓库，并在您选择的 IDE 上开始开发应用

1. 安装 maven。详情请参见 https://maven.apache.org/install.html。

2. 从 http://servicecomb.incubator.apache.org/release/ 下载 `local-service-center`。
   阅读 `README.md` 来启动 `local-service-center`。

3. 根据 `local-service-center` 中的 `README.md` 更新 `microservice.yaml` 中的服务注册地址。

4. 构建应用。

   ```bash
   mvn -f pom.xml package
   ```

5. 在 IDE 中运行应用或执行 jar 包。

6. 在网络浏览器中打开 http://127.0.0.1:8080/rest/helloworld?name=hellworld 来查看您的应用。

如果您想在 servicestage 上部署示例代码，请查看您在 ServiceStage 上的 CI/CD 流水线和服务堆栈，并根据需要进行自定义。
