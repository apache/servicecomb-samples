# Body Mass Index(BMI) Calculator Microservice Demo

Check this [document](https://docs.servicecomb.io/java-chassis/zh_CN/featured-topics/application-bmi/) for details explanations about this demo.

## Architecture of BMI Calculator
There are two microservices in this demo.
* Webapp (API Gateway)
* BMI Calculator (computing service)

## Prerequisite
1. [Oracle JDK 1.8+](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
2. [Maven 3.x](https://maven.apache.org/install.html)
3. [Gradle 4.x](https://gradle.org/install/)(Optional)
## Precondition
see [Precondition](../../README.md)

## Quick Start(Linux)
* Run the service center

     - [how to start the service center](http://servicecomb.apache.org/docs/products/service-center/install/)


* Run microservices

    * via maven
       * Run the **BMI calculator service**
       
        ```bash
        cd bmi/calculator; mvn spring-boot:run
        ```
      
       * Run the **webapp service**
       
        ```bash
        cd bmi/webapp; mvn spring-boot:run
        ```
 
    * via gradle
    
       * Run the **BMI calculator service**
       
        ```bash
        cd bmi/calculator; gradle bootRun
        ```
    
       * Run the **webapp service**
       
        ```bash
        cd bmi/webapp; gradle bootRun
        ```

* Visit the services via **<a>http://127.0.0.1:8889</a>**.

