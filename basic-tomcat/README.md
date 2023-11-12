# Description
This project providers sample to show working with Java Chassis Microservices. 

* provider
A Microserivce using Java Chassis with a REST interface. Provider is packaged as WAR and deploy in tomcat. 

* consumer
A Microserivce using Java Chassis with a REST interface. Consumer calls provider with RPC. Consumer is packaged as WAR and deploy in tomcat.

* gateway
A Microserivce using Java Chassis Edge Service to forward requests to consumer. Gateway packaged as executable JAR. 

## Precondition
see [Precondition](../README.md)
# Build and Run

* Build

        mvn clean package

* Run provider

  Deploy basic-provider-2.0-SNAPSHOT.war to tomcat. 

* Run consumer

  Deploy basic-consumer-2.0-SNAPSHOT.war to tomcat. 

* Run gateway

  In ${Project}/gateway/target/

        java -jar basic-gateway-2.0-SNAPSHOT.jar

* Testing

Open in browser： http://localhost:9090/sayHello?name=World



# 项目说明

这个项目提供了 Java Chassis 的简单例子，例子包括：

* provider
使用 Java Chassis 开发一个 REST 接口。 Provider打包为WAR在tomcat部署。 

* consumer
使用 Java Chassis 开发一个 REST 接口， 接口实现通过 RPC 调用 provider 的接口。 Consumer打包为WAR在tomcat部署。

* gateway
使用 Java Chassis Edge Service 开发一个网关， 网关将所有请求转发到 consumer。 Gateway打包为可执行JAR运行。 

## 使用

* 编译

        mvn clean package

* 启动 provider

  将basic-provider-2.0-SNAPSHOT.war部署到Tomcat。

* 启动 consumer

  将basic-consumer-2.0-SNAPSHOT.war部署到Tomcat。

* 启动 gateway

  进入目录 ${Project}/gateway/target/

        java -jar basic-gateway-2.0-SNAPSHOT.jar

* 测试

启动3个微服务后， 然后通过界面访问： http://localhost:9090/sayHello?name=World


