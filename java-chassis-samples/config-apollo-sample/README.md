## Precondition
see [Precondition](../../README.md)

## Use Apollo as Configuration Center

To use Apollo as configuration source in ServiceComb Java Chassis services:

* Start Apollo service and create a project to associate with Chassis service, then generate a token

  [How to use Apollo configuration center](https://docs.servicecomb.io/java-chassis/zh_CN/config/general-config/)


* Import `config-apollo` in pom:

  ```xml
  <dependency>
        <groupId>org.apache.servicecomb</groupId>
        <artifactId>config-apollo</artifactId>
   </dependency>
  ```

* Configurations for Apollo itself in `microservice.yaml`, for example:

  ```yaml
  apollo:
    config:
      serverUri: http://127.0.0.1:8070	#Apollo portal server address
      serviceName: apollo-test		#service name use AppId in apollo
      env: DEV				#default value DEV
      clusters: default		#default value default
      namespace: application		#default value application
      token: 				#get token from Apollo web pages
  ```

* Start Chassis service and update configurations in Apollo portal service.

    - [how to start the service center](http://servicecomb.apache.org/docs/products/service-center/install/)

  Compile the source code, and use `mvn exec` to execute the main class `MainServer`.

  ```bash
  mvn clean install
  cd config-apollo-sample/
  mvn exec:java -Dexec.mainClass="MainServer"
  ```

* Verify configurations can be configured dynamically.

  Before publishing a new configuration items from apollo portal, `MainServer` will just print `DynamicProperty: {name=timeout, current value=default}` 

  After `timeout` configuration is published with value `100`, `MainServer` will print `DynamicProperty: {name=timeout, current value=100}` 


## More

[Apollo Doc](https://github.com/ctripcorp/apollo/wiki)

[Use Apollo In ServiceComb]((https://docs.servicecomb.io/java-chassis/zh_CN/config/general-config/))

