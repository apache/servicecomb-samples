# About Spring Cloud and Java Chassis interoperability

In this demo, we build a gateway using spring-cloud-gateway, a microservice `provider-java-chassis` using Java Chassis, a microservice `provider-spring-cloud` using Spring Cloud. 

Scenario 1:  User -> gateway -> provider-spring-cloud -> provider-java-chassis

Request:

    `http://localhost:9090/spring-cloud/sayHello?name=World`

Result:

    `"Hello from Java Chassis, World"`


Scenario 2:  User -> gateway -> provider-java-chassis -> provider-spring-cloud

Request:

    `http://localhost:9090/java-chassis/sayHello?name=World`

Result:

    `"Hello from Spring Cloud, World"`

## Using Service Center & Kie

Maven profile and Spring Profile using `servicecomb`. 

## Using Nacos

Maven profile and Spring Profile using `nacos`.

>>> Notice: Nacos 2.3.0 and above is required. Because older versions of nacos will not generate instance id for Spring Cloud applications and Java Chassis interoperability needs instance id. 

