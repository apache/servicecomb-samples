# Trust Sample

This sample shows using public key mechanism to authenticate user and black/white user list. The public key mechanism security is based on Service Center, and users should first configure Service Center authentication. This sample does not show how to configure Service Center authentication.

In this sample, both customer and hacker try to access store, while store configure it's black list to deny hacker's access.

## Provider Service

1. Add dependence in `pom.xml` file:

    ```xml
    <dependency>
        <groupId>org.apache.servicecomb</groupId>
        <artifactId>handler-publickey-auth</artifactId>
    </dependency>
    ```

2. Add publicKey accessControl rules in `microservice.yaml`:

    ```yaml
    publicKey:
        accessControl:
        enabled: true
        # excludePathPatterns: '/scb/management/*'
        # includePathPatterns: '/CodeFirstComputeImpl/*'
        black:
            list01:
            category: property ## property, fixed value
            propertyName: serviceName ## property name, e.g. serviceName, appId, environment, alias, version and so on, also support key in properties.
            # property value match expression.
            # if propertyName is serviceName
            # only supports prefix match and postfix match and exactly match.
            # e.g. hacker*, *hacker, hacker
            rule: hacker
        white:
            list02:
            category: property
            propertyName: serviceName
            rule: cust*
   ```

   This configuration allows serviceNames start with `cust` to access, denied serviceNames `hacker` to access.

## Consumer Service

1. Add dependence in `pom.xml` file:

    ```xml
    <dependency>
        <groupId>org.apache.servicecomb</groupId>
        <artifactId>handler-publickey-auth</artifactId>
    </dependency>
    ```

## Precondition

see [Precondition](../../README.md)

## Sample Quick Start

### Start the ServiceComb/Service Center

* [how to start the service center](http://servicecomb.apache.org/docs/products/service-center/install/)
* make sure service center address is configured correctly in `microservice.yaml` file

### Start the `store` service

* Start provider service by maven

  Compile the source code, and use `mvn exec` to execute the main class `TrustMain`.

  ```bash
  mvn clean install
  cd auth-sample/auth-provider/
  mvn exec:java -Dexec.mainClass="org.apache.servicecomb.samples.trust.provider.TrustMain"
  ```

* Start provider service by IDE

  Import the project by InteliJ IDEA or Eclipse, then find `main` function `TrustMain` of provider service and `RUN` it like any other Java program.

### Start the `customer` service

Just like how to start `store` service. But the main class of auth-consumer service is `AuthConsumerMain`.

```bash
cd auth-sample/auth-consumer/
mvn exec:java -Dexec.mainClass="org.apache.servicecomb.samples.trust.consumer.CustomerMain"
```

### Start the `hacker` service

Just like how to start `store` service. But the main class of auth-consumer service is `HackerMain`.

```bash
cd auth-sample/auth-consumer/
mvn exec:java -Dexec.mainClass="org.apache.servicecomb.samples.trust.consumer.HackerMain"
```

### How to verify

On the `store` side, the output should contain the following stuffs if the producer starts up successfully:

1. *`swagger: 2.0 info: version: 1.0.0 ...`* means the producer generated swagger contracts
2. *`rest listen success. address=0.0.0.0:8080`* means the rest endpoint is listening on port 8080

On the `consumer` side, you can see the following outputs if the consumer can invoke the producer:

* *`a=1, b=2, result=3`* means the consumer calls `add` with `a=1, b=2` successfully by rules.

On the `hacker` side, you can see the following outputs if the consumer can invoke the producer:

* *`InvocationException: code=401;msg={message=UNAUTHORIZED}`* means the consumer calls `add` been denied by rules.
