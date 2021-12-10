## Code First Sample

ServiceComb Java Chassis supports generating provider-service API implicitly. When the service is started, an API is automatically generated and registered to the service center.

When you develop a microservice in transparent RPC mode, the code does not show how you want to define an API, and all generated APIs are POST methods, The input parameters of all the methods will be packaged as a class and transferred as body parameters. Therefore, if you develop providers using implicit APIs, you are advised to choose Spring MVC or JAX-RS mode to obtain complete RESTful statements.

For detail information please refer to [Doc](https://docs.servicecomb.io/java-chassis/zh_CN/build-provider/code-first/)



## Sample Quick Start

* Start the ServiceComb/Service Center

   - [how to start the service center](http://servicecomb.apache.org/docs/products/service-center/install/)
   - make sure service center address is configured correctly in `microservice.yaml` file

```yaml
servicecomb:
 service:
   registry:
     address: http://127.0.0.1:30100		#service center address
```

* Start the codefirst-provider service

   - Start provider service via maven

      Compile the source code, and use `mvn exec` to execute the main class `CodeFirstProviderMain`.

      ```bash
      mvn clean install
      cd codefirst-sample/codefirst-provider/
      mvn exec:java -Dexec.mainClass="org.apache.servicecomb.samples.codefirst.provider.CodeFirstProviderMain"
      ```

   - Start provider service via gradle

      Compile the source code, and use `mvn exec` to execute the main class `CodeFirstProviderMain`.

      ```bash
      cd codefirst-sample/codefirst-provider/
      
      ```

   - Start provider service via IDE

      Import the project by InteliJ IDEA or Eclipse, then find `main` function `CodeFirstProviderMain` of provider service and `RUN` it like any other Java program.

3. Start the codefirst-consumer service

   ```bash
   mvn clean install
   cd codefirst-sample/codefirst-consumer/
   mvn exec:java -Dexec.mainClass="org.apache.servicecomb.samples.codefirst.consumer.CodeFirstConsumerMain"
   ```

   - Start consumer service via maven

      Just like how to start codefirst-provider service. But the main class of codefirst-consumer service is `CodeFirstConsumerMain`.

      ```bash
      mvn exec:java -Dexec.mainClass="org.apache.servicecomb.samples.codefirst.consumer.CodeFirstConsumerMain"
      ```

   - Start consumer service via gradle

      ```bash
      gradle clean run
      ```

4. How to verify
   On the producer side, the output should contain the following stuffs if the producer starts up successfully:
   1. *'swagger: 2.0 info: version: 1.0.0 ...'* means the producer generated swagger contracts
   2. *'rest listen success. address=0.0.0.0:8080'* means the rest endpoint is listening on port 8080
   
   On the consumer side, you can see the following outputs if the consumer can invoke the producer:
   1. *'Pojo Hello Java Chassis'* means the consumer calls sayhi successfully 
   2. *'Jaxrs Hello Java Chassis'* means the consumer calls Jaxrs sayhi successfully
   3. *'Spring mvc Hello Java Chassis'* means the consumer calls SpringMvc sayhi successfully
   4. *'Pojo Hello person ServiceComb/Java Chassis'* means the consumer calls sayhello successfully
   5. *'Jaxrs Hello person ServiceComb/Java Chassis'* means the consumer calls Jaxrs sayhello successfully
   6. *'Spring mvc Hello person ServiceComb/Java Chassis'* means the consumer calls SpringMvc sayhello successfully

## Precondition
see [Precondition](../../README.md)