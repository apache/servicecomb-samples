# war-external-tomcat

This project demonstrates **how to integrate ServiceComb microservice framework into an existing traditional Java Web application** and deploy it to an external Tomcat.

## Project Features (Differences from spring-boot-external-tomcat)

This project demonstrates a **legacy system integration scenario**:

- **Traditional Servlet/Filter coexist with ServiceComb** - Traditional components configured via web.xml
- **JSP page support** - Retaining existing JSP pages
- **ServiceComb microservices** - Modern microservices integrated through Spring Boot
- **Unified deployment** - All components packaged into a single WAR and deployed to external Tomcat

While the `spring-boot-external-tomcat` project is a **pure Spring Boot application** without legacy components.

Note:

- Integrating legacy code → Not a ServiceComb feature, but a Servlet standard capability
- This sample → Only proves ServiceComb follows standards and doesn't break legacy systems
- ServiceComb's value → Lies in microservice governance (service discovery, load balancing, fault tolerance, etc.), not "coexisting with legacy code"

This sample demonstrates that introducing ServiceComb is safe and won't break existing systems, eliminating migration concerns.

### Project Structure

```text
├── LegacyServlet.java    # [Legacy] Traditional Servlet (configured via web.xml)
├── LegacyFilter.java     # [Legacy] Traditional Filter (configured via web.xml)
├── HelloEndpoint.java    # [New] ServiceComb microservice interface
├── Application.java      # [New] Spring Boot bootstrap class
├── index.jsp             # [Legacy] Traditional JSP page
└── web.xml               # [Legacy] Traditional Servlet container configuration
```

### Key Configurations

1. **pom.xml**: Add `java-chassis-spring-boot-starter-servlet` dependency
2. **Application.java**: Create a bootstrap class that extends `SpringBootServletInitializer`
3. **web.xml**: ServiceComb is auto-configured by Spring Boot; traditional components are configured normally
4. **microservice.yaml**: ServiceComb configuration file

## Deployment Steps

1. Build the project: `mvn clean package`
2. Deploy the generated WAR package to Tomcat's webapps directory
3. Start Tomcat
4. Access the services, after deployment you can visit:

   - **Traditional JSP**: `http://localhost:8080/warExternalTomcat-0.0.1/`
   - **Traditional Servlet**: `http://localhost:8080/warExternalTomcat-0.0.1/legacy`
   - **ServiceComb API**: `http://localhost:8080/warExternalTomcat-0.0.1/rest/hello?name=world`

## ServiceComb 3.x Adaptation Notes

`ServiceComb 3.x` adopts the Spring Boot approach and no longer uses the traditional `RestServletContextListener`. WAR deployment to external Tomcat is achieved through `java-chassis-spring-boot-starter-servlet` and `SpringBootServletInitializer`.

Only:

- `RestServlet.class` - Servlet implementation
- `RestAsyncListener.class` - Async listener (not `ServletContextListener`)

Therefore, `2.x` version samples cannot be upgraded to `3.x` version.
