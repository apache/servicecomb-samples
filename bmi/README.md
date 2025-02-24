# bmi

## Description

This guide will introduce microservices through a simple **Body Mass Index (BMI)** application.
BMI is mainly used to measure the degree of body fat.

The application consists of two microservices:

- **BMI Calculator**: Responsible for handling calculation operations.
- **BMI Interface**: Provides user interface and gateway services.

> For detailed information, please refer to the [official documentation](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-bmi.html)

## Precondition

see [Precondition](../README.md)

## Usage

1. Modify Configuration

   Before compiling, you need to modify the static resource path (refer to [issues 53](https://github.com/apache/servicecomb-samples/issues/53)):

   - For Windows users, modify `gateway.webroot` to `/code/servicecomb-samples/bmi/webapp/src/main/resources/static`
     > For absolute path, remove the drive letter. Assuming the code is saved in `D:\code\servicecomb-samples\bmi\webapp\src\main\resources\static`
   - For Linux users, `gateway.webroot` can use relative path directly: `./bmi/webapp/src/main/resources/static`

2. Compile

   ```bash
   mvn clean package
   ```

3. Start calculator
   In `${Project}/calculator/target/`

   ```bash
   java -jar bmi-calculator-3.0.0-SNAPSHOT.jar
   ```

4. Start webapp
   In `${Project}/webapp/target/`

   ```bash
   java -jar webapp-3.0.0-SNAPSHOT.jar
   ```

5. Test
   After starting both microservices, access the interface through: http://localhost:8889

   > You can set the configuration item `bmi.mock.error` in the `application.yml` to simulate error responses.

## 项目说明

本指南将以一个简单的 **体质指数(BMI)** 应用开展微服务之旅。体质指数主要用于衡量人体胖瘦程度。
该应用主要包含两个微服务：

- 体质指数计算器：负责处理运算事务。
- 体质指数界面：提供用户界面及网关服务。

> 具体信息请参[考官方文档](https://servicecomb.apache.org/references/java-chassis/zh_CN/featured-topics/application-bmi.html)

## 前置条件

参见[前置条件](../README.md)

## 使用

1. 修改配置

   编译前需要先修改静态资源路径（参考[issues 53](https://github.com/apache/servicecomb-samples/issues/53)）：
   - 如果你使用 Windows，请修改`gateway.webroot`为`/code/servicecomb-samples/bmi/webapp/src/main/resources/static` > 绝对路径去掉盘符，假设代码保存在`D:\code\servicecomb-samples\bmi\webapp\src\main\resources\static`
   - 如果你使用 Linux，`gateway.webroot`可以直接使用相对位置：`./bmi/webapp/src/main/resources/static`

2. 编译

   ```bash
   mvn clean package
   ```

3. 启动 calculator

   In `${Project}/calculator/target/`

   ```bash
   java -jar bmi-calculator-3.0.0-SNAPSHOT.jar
   ```

4. 启动 webapp

   In `${Project}/webapp/target/`

   ```bash
   java -jar webapp-3.0.0-SNAPSHOT.jar
   ```

5. 测试

   启动 2 个微服务后， 然后通过界面访问：http://localhost:8889

   > 可在 `application.yml` 设置配置项 `bmi.mock.error` 模拟错误返回。
