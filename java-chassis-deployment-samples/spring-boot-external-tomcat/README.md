# spring-boot-external-tomcat

## Description

This project shows how to run java-chassis with spring boot in external web container.

Following are some useful articles to run spring boot in external web container.

* [spring boot official site](https://spring.io/blog/2014/03/07/deploying-spring-boot-applications#what-about-the-java-ee-application-server)
* [very good article with examples](https://dzone.com/articles/spring-boot-with-external-tomcat)
* [Q & A in stack overflow](https://stackoverflow.com/questions/34506712/how-to-deploy-spring-boot-web-application-on-tomcat-server)

For java-chassis using spring boot, check following articles.

* [Working with Spring Boot](https://docs.servicecomb.io/java-chassis/zh_CN/using-java-chassis-in-spring-boot/using-java-chassis-in-spring-boot/)

## Precondition

see [Precondition](../../README.md)

## Usage

### Build

```bash
mvn clean package
```

After building, you will find the `springBootExternalTomcat-0.0.1.war` file in the `target` directory.

### Deploy to External Tomcat

1. Download and install Apache Tomcat 9.x or higher
2. Copy the generated WAR file to Tomcat's `webapps` directory:

   ```bash
   cp target/springBootExternalTomcat-0.0.1.war <TOMCAT_HOME>/webapps/
   ```

3. Start Tomcat:

   ```bash
   cd <TOMCAT_HOME>/bin
   ./startup.sh    # On Linux/Mac
   startup.bat     # On Windows
   ```

4. Tomcat will automatically deploy the WAR file

### Test the Service

Once deployed and started, you can test the service using curl:

```bash
curl "http://localhost:8080/springBootExternalTomcat-0.0.1/hello?name=World"
```

Expected response:

```text
World
```
