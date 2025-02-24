# About Spring Cloud and Java Chassis interoperability

## Description

In this demo, we build: 

- a gateway `gateway` using spring-cloud-gateway,
- a microservice `provider-java-chassis` using Java Chassis,
- a microservice `provider-spring-cloud` using Spring Cloud.

Both of these three microservice will be registered to `servicecomb` or `nacos`.

Scenario 1: `User -> gateway -> provider-spring-cloud -> provider-java-chassis`

Request:

```text
http://localhost:9090/spring-cloud/sayHello?name=World
```

Result:

```text
"Hello from Java Chassis, World"
```

Scenario 2: `User -> gateway -> provider-java-chassis -> provider-spring-cloud`

Request:

```text
http://localhost:9090/java-chassis/sayHello?name=World
```

Result:

```text
"Hello from Spring Cloud, World"
```

## Using Service Center & Kie

Maven profile and Spring Profile using `servicecomb`.

## Using Nacos

Maven profile and Spring Profile using `nacos`.

> Notice: Nacos 2.3.0 and above is required.
> Because older versions of nacos will not generate instance id for
> Spring Cloud applications and Java Chassis interoperability needs instance id.

## 说明

在本演示中，我们：

- 使用 spring-cloud-gateway 构建网关 `gateway`
- 使用 Java Chassis 构建微服务 `provider-java-chassis`
- 使用 Spring Cloud 构建微服务 `provider-spring-cloud`

以上三个服务注册到 `servicecomb` 或 `nacos`。

场景 1：`用户 -> 网关 -> provider-spring-cloud -> provider-java-chassis`

请求：

```text
http://localhost:9090/spring-cloud/sayHello?name=World
```

结果：

```text
"Hello from Java Chassis, World"
```

场景 2：`用户 -> 网关 -> provider-java-chassis -> provider-spring-cloud`

请求：

```text
http://localhost:9090/java-chassis/sayHello?name=World
```

结果：

```text
"Hello from Spring Cloud, World"
```

## 使用 Service Center 和 Kie

Maven profile 和 Spring Profile 使用 `servicecomb`。

## 使用 Nacos

Maven profile 和 Spring Profile 使用 `nacos`。

> 注意：需要 Nacos 2.3.0 及以上版本。因为旧版本的 nacos 不会为 Spring Cloud 应用程序生成实例 ID，
> 而 Java Chassis 互操作性需要实例 ID。
