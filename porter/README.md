# porter-application

## Description

This project shows how to use Java Chassis to create a simple microservice application.
It shows basic authentication and authorization, uploading files and delete files operations.

## Precondition

see [Precondition](../README.md)

## build and run

1. compile

   ```bash
   mvn clean install
   ```

2. before run

   - create a database using mysql with username and password（e.g. root/root）
   - execute `create_db_user.sql`

3. run `user-service`:

   ```bash
   java $JAVA_OPT -Ddb.url="jdbc:mysql://localhost/porter_user_db?useSSL=false" -Ddb.username=root -Ddb.password=root -jar porter-user-service-3.0-SNAPSHOT.jar
   ```

4. run `file-service`:

   ```bash
   java $JAVA_OPT -jar porter-file-service-3.0-SNAPSHOT.jar
   ```

5. run `gateway-service`:

   The `gateway-service` contains static page files in the `resources/ui` directory.

   - First, you need to copy the page files to the WEB root directory (relative path, current running directory), e.g.:
     `webapp`.
   - Then copy the entire ui directory to `webapp/ui` directory to start.

   Note: Related configuration in `porter_lightweight/gateway-service/src/main/resources/microservice.yaml`
   `gateway.webroot` is your local code location

   Refer to [issues 53](https://github.com/apache/servicecomb-samples/issues/53):

   - For Windows users, modify `gateway.webroot` to
     `/code/servicecomb-samples/porter_lightweight/gateway-service/src/main/resources`
     > For absolute path, remove the drive letter. Assuming the code is saved in
     > `D:\code\servicecomb-samples\porter_lightweight\gateway-service\src\main\resources`
   - For Linux users, `gateway.webroot` can use relative path directly:
     `./porter_lightweight/gateway-service/src/main/resources`

   ```bash
   java $JAVA_OPT -Dgateway.webroot=webapp -jar porter-gateway-service-3.0-SNAPSHOT.jar
   ```

## Try it

1. Using browser: http://localhost:9090/ui/login.html with user `admin` or `guest`, password is `test`.
2. Choose a file to upload. Uploaded file is stored in `file-service` working directory.
   And file Id is random number generated.
3. Delete file. Input the file Id generated in step 2.
   > Only admin account can delete files.

## 说明

这个项目帮助开发者学习如何使用 ServiceComb 开发完整的微服务。
这个项目实现的功能非常简单，用户登录后，上传一个文件和删除一个文件，验证了没有权限的用户无法删除文件。

## 前提条件

查看 [前提条件](../README.md)

## 编译和运行

1. 编译

   ```bash
   mvn clean install
   ```

2. 运行前准备

   - 安装 mysql 数据库，设置用户名密码（假设为 root/root）
   - 执行脚本`create_db_user.sql`

3. 启动`user-service`:

   ```bash
   java $JAVA_OPT porter-user-service-3.0-SNAPSHOT.jar
   ```

4. 启动`file-service`:

   ```bash
   java $JAVA_OPT -jar porter-file-service-3.0-SNAPSHOT.jar
   ```

5. 启动`gateway-serivce`:

   `gateway-service`包含了静态页面文件，在`resources/ui`目录。

   - 首先需要将页面文件拷贝到 WEB 主目录（相对路径，当前运行目录），比如:`webapp`。
   - 然后将 ui 目录整体拷贝到`webapp/ui`目录启动。

   注意：`porter_lightweight/gateway-service/src/main/resources/microservice.yaml`中的相关配置
   `gateway.webroot`是自己本地代码的位置

   参考[issues 53](https://github.com/apache/servicecomb-samples/issues/53)：

   - 如果你使用 Windows，请修改`gateway.webroot`为
     `/code/servicecomb-samples/porter_lightweight/gateway-service/src/main/resources`
     > 绝对路径去掉盘符，假设代码保存在`D:\code\servicecomb-samples\porter_lightweight\gateway-service\src\main\resources`
   - 如果你使用 Linux，`gateway.webroot`可以直接使用相对位置：`./porter_lightweight/gateway-service/src/main/resources`

   ```bash
   java $JAVA_OPT -Dgateway.webroot=webapp -jar porter-gateway-service-3.0-SNAPSHOT.jar
   ```

## 使用

1. 输入: http://localhost:9090/ui/login.html 使用 `admin` 或者 `guest` 登陆，密码为 `test`。
2. 选择一个文件上传，上传成功，上传成功后的文件会保存在`file-service`的当前目录， 文件名称是一个随机的数字，这个数字就是文件 ID。
3. 删除文件：输入上一步的文件 ID，点击删除。 
   > 只有 admin 账户可以删除文件。
