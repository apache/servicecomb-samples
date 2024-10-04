# ServiceComb samples

This project is samples for java-chassis 3.x.x. You can switch to other branches to see different samples.

## basic
A sample shows a provider, a consumer and a gateway using java chassis. 

## porter_lightweight
A sample shows an application with basic login, download file, delete file functions. Read this [document](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-porter.html) for details.

## porter_springboot
A sample shows an application with basic login, download file, delete file functions. Read this [document](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-porter.html) for details.

## java-chassis-samples
Featured samples for java-chassis modules. Each module contains 10 minutes guides.

## java-chassis-integration-tests
java-chassis integration tests。 

## Precondition

Before running the samples，install [service center](https://github.com/apache/servicecomb-service-center) and [kie](https://github.com/apache/servicecomb-kie). Or download [Lightweight Cloud Service Engine](https://support.huaweicloud.com/devg-cse/cse_devg_0036.html) which integrates both service center and kie for ease of use.

## Running Java Samples

1. Clone the repository:
```
git clone https://github.com/apache/servicecomb
```
3. Navigate to the sample directory (e.g., `servicecomb-samples/java/alpha-service`)
4. Build the project: `mvn clean package`
5. Run the application: `java -jar target/alpha-service.jar`
6. Access the service:
```
 `http://localhost:8080/alpha-service
```

# 使用ServiceComb开发微服务的示例项目

这个项目使用java-chassis 2.0.x版本。可以切换分支，查看其他版本的例子。

## basic
该项目展示了Java Chassis开发的一个provider， 一个consumer和一个网关服务。 

## porter_lightweight
该项目演示了一个包括网关、文件下载、认证鉴权等功能的简单应用系统，同时演示了ServiceComb推荐的先写接口声明，再写业务代码的软件工程实践。可以阅读[文章](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-porter.html)了解项目的细节。这个例子的运行环境采用vert.x作为HTTP服务器，即 REST
over Vert.x transport。

## porter_springboot
该项目演示了一个包括网关、文件下载、认证鉴权等功能的简单应用系统，同时演示了ServiceComb推荐的先写接口声明，再写业务代码的软件工程实践。可以阅读[文章](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-porter.html)了解项目的细节。这个例子的运行环境采用Spring Boot及其Embeded Tomcat，即
REST over Servlet。

## java-chassis-samples
java-chassis 各个模块功能的例子。每个子项目包含了10分钟可以学习完成的简单示例。

## java-chassis-integration-tests
java-chassis 的集成测试用例。 

## 前提条件

运行这些例子之前，需要先安装[注册中心](https://github.com/apache/servicecomb-service-center) 和[配置中心](https://github.com/apache/servicecomb-kie) 。华为云提供一个出色的[本地轻量化微服务引擎](https://support.huaweicloud.com/devg-cse/cse_devg_0036.html) ,可以直接下载安装使用。

