# Description
This project providers sample to show working with Java Chassis Microservices. 

* provider
A Microserivce using Java Chassis with a REST interface.

* consumer
A Microserivce using Java Chassis with a REST interface. Consumer calls provider with RPC.

* gateway
A Microserivce using Java Chassis Edge Service to forward requests to consumer.

# Build and Run

* Build

        mvn clean pacakge

* Run provider

  In ${Project}/provider/target/
  
        java -jar basic-provider-2.0-SNAPSHOT.jar

* Run consumer

  In ${Project}/consumer/target/

        java -jar basic-consumer-2.0-SNAPSHOT.jar

* Run gateway

  In ${Project}/gateway/target/

        java -jar basic-gateway-2.0-SNAPSHOT.jar

* Testing

Open in browser： http://localhost:9090/sayHello?name=World

# 项目说明

这个项目提供了 Java Chassis 的简单例子，例子包括：

* provider
使用 Java Chassis 开发一个 REST 接口。

* consumer
使用 Java Chassis 开发一个 REST 接口， 接口实现通过 RPC 调用 provider 的接口。 

* gateway
使用 Java Chassis Edge Service 开发一个网关， 网关将所有请求转发到 consumer。 

## 使用

* 编译

        mvn clean pacakge

* 启动 provider

  进入目录 ${Project}/provider/target/
  
        java -jar basic-provider-2.0-SNAPSHOT.jar

* 启动 consumer

  进入目录 ${Project}/consumer/target/

        java -jar basic-consumer-2.0-SNAPSHOT.jar

* 启动 gateway

  进入目录 ${Project}/gateway/target/

        java -jar basic-gateway-2.0-SNAPSHOT.jar

* 测试

启动3个微服务后， 然后通过界面访问： http://localhost:9090/sayHello?name=World
