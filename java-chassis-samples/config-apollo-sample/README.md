# config-apollo-sample

## Precondition

see [Precondition](../../README.md)

## Use Apollo as Configuration Center

To use Apollo as configuration source in ServiceComb Java Chassis services:

1. Start Apollo service and create a project to associate with Chassis service, then generate a token

   [How to use Apollo configuration center](https://servicecomb.apache.org/references/java-chassis/zh_CN/config/general-config.html)

2. Import `config-apollo` in pom:

   ```xml
   <dependency>
         <groupId>org.apache.servicecomb</groupId>
         <artifactId>config-apollo</artifactId>
    </dependency>
   ```

3. Configurations for Apollo itself in `microservice.yaml`, for example:

   ```yaml
   apollo:
     config:
       serverUri: http://127.0.0.1:8070 #Apollo portal server address
       serviceName: apollo-test #service name use AppId in apollo
       env: DEV #default value DEV
       clusters: default #default value default
       namespace: application #default value application
       token: #get token from Apollo web pages
   ```

4. Start Chassis service and update configurations in Apollo portal service.
   - [how to start the service center](http://servicecomb.apache.org/docs/products/service-center/install/)

   Compile the source code, and use `mvn exec` to execute the main class `MainServer`.

   ```bash
   mvn clean install
   cd config-apollo-sample/
   mvn exec:java -Dexec.mainClass="MainServer"
   ```

5. Verify configurations can be configured dynamically.
   - Before publishing a new configuration items from apollo portal, `MainServer` will just print:

     ```text
     Dynamic Properties: serviceComb
     Environment: serviceComb
     ```

   - After `timeout` configuration is published with value `100`, `MainServer` will print

     ```text
     Config value cache changed: action:SET; item:[timeout]
     Dynamic Properties: 100
     Environment: 100
     ```

## Configuration tips on Apollo server

ServiceComb `config-apollo` interacts with Apollo based on the **Apollo Open Platform Authorization**. You must complete the authorization on the Apollo portal before the service can successfully pull configurations.

1. Create third-party applications:
   - Login to Apollo Portal, go to **`Admin Tools`** -> **`Open Platform Authorization Management`**.
   - Click **`Create third-party app`**.
   - Fill in the `Third party appId` and `Third party application name` etc. If you have already created one, skip this step.
   - Once created, it will generate a **Token**. Copy this token for later use.

2. Authorization :
   - In the **`Authorization`** section of the same page, fill `Token`, `Managed AppId`, `Managed Namespace` etc. and submit.

3. Sync to `microservice.yaml`:
   - Copy the generated **`Token`**, **`AppId`** etc. into your `microservice.yaml`:

> **Note:** These steps are mandatory. Without Open Platform authorization, the client will not be able to fetch any data even if the network connection is correct.

You can try the official Apollo test server if you can't start Apollo service by yourself. Just using `apollo.config.serverUri`: `http://81.68.181.139` (don't add port).
And the management portal also can be access at `http://81.68.181.139/`. login info can be find on: [Apollp project](https://github.com/apolloconfig/apollo).

## More

[Apollp project](https://github.com/apolloconfig/apollo)

[Apollo Doc](https://github.com/ctripcorp/apollo/wiki)

[Use Apollo In ServiceComb](<(https://docs.servicecomb.io/java-chassis/zh_CN/config/general-config/)>)
