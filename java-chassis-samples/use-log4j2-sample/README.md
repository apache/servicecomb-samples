# use-log4j2-sample

This sample demonstrates how to use Log4j2 as the logging framework in ServiceComb Java Chassis microservices.

## Overview

This example shows:

* Using Log4j2 instead of the default logging implementation
* Configuring Log4j2 for console and file output
* Implementing a simple RPC service

## Preconditions

* JDK 17 or higher
* Maven 3.x
* ServiceComb Service Center running

## Start Service Center

See [how to start the service center](http://servicecomb.apache.org/docs/products/service-center/install/)

Make sure the Service Center address is configured correctly in the `microservice.yaml` file:

```yaml
servicecomb:
  registry:
    sc:
      address: http://127.0.0.1:30100
```
## Log4j2 Configuration

The project uses Log4j2 as the logging framework. The configuration file is located at . `src/main/resources/log4j2.xml`

Key configurations:

* Logs output to both console and file
* Log file path: `./logs/use-log4j2-sample/output.log`
* Rolling policy: Max 200MB per file, up to 99 backup files
* Log level: INFO

## How to verify

* Start the Service Center
* Build and run the sample application
* Access the service through the interface: `http://localhost:8080/hello`
* Check the logs in `./logs/use-log4j2-sample/output.log`

## More
* [ServiceComb Java Chassis Documentation](https://docs.servicecomb.io/java-chassis/zh_CN/)
* [Log4j2 Official Documentation](https://logging.apache.org/log4j/2.x/)
