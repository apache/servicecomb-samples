# Java CSE Spring MVC microservice

| Language | Framework | Platform
| -------- | -------- |--------|
| Java | CSE Spring MVC | ServiceStage Container, CCE Cluster|

This sample code helps get you started with a simple Java CSE microservice
deployed by ServiceStage Container App to a CCE Cluster.

This sample includes:

* README.md
* LICENSE
* Dockerfile
* pom.xml - this file is the Maven Project Object Model for the microservice
* src/main - this directory contains your Java service source files
* src/test - this directory contains your Java service unit test files

## Getting Started

Clone your code repository and start developing your application on IDE of your choice

1. Install maven.  See https://maven.apache.org/install.html for details.

2. Download local-service-center from http://servicecomb.incubator.apache.org/release/. Read README.md to start local-service-center.

3. Update the service registry address in microservice.yaml according to the README.md in local-service-center.

4. Build the application.

        $ mvn -f pom.xml package

5. Run the application in IDE or execute the jar.

6. Open http://127.0.0.1:8080/rest/helloworld?name=hellworld in a web browser to view your application.

if you want to deploy the sample code on servicestage, View your CI/CD pipeline and service stack on ServiceStage and customize it as per your needs

## License:

See [LICENSE](LICENSE).

## Contributing
