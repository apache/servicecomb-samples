# ServiceComb samples

This project is samples for java-chassis 2.0.x. You can switch to other branches to see different samples.

## basic
A sample shows a provider, a consumer and a gateway using java chassis. 

## porter_lightweight
A sample shows an application with basic login, download file, delete file functions. Read this [document](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-porter.html) for details.

## porter_springboot
A sample shows an application with basic login, download file, delete file functions. Read this [document](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-porter.html) for details.

## java-chassis-samples
Featured samples for java-chassis modules. Each module contains 10 minutes guides.

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

## 前提条件

运行这些例子之前，需要先准备CSE运行环境。

* 通过下载安装 [本地简化版 CSE](https://support.huaweicloud.com/devg-servicestage/ss-devg-34.html) ,搭建本地开发环境。
* 使用[华为云云服务](https://support.huaweicloud.com/devg-servicestage/ss-devg-0002.html) 。

[更多信息](https://support.huaweicloud.com/devg-servicestage/ss-devg-0006.html) 可以参考开发指南。

CSE运行环境准备好以后，编辑每个微服务的`bootstrap.yml`文件，配置正确的CSE服务信息，比如配置中心、注册中心的地址。

例子默认使用微服务引擎1.0版本，配置中心得类型为config-center,如果使用微服务引擎2.0，配置文件要做如下修改:

```
servicecomb:
  kie:
    serveUri:
```