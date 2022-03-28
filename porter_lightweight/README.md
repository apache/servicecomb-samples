This project shows how to using java-chassis and service-center to create a simple microservice application. It shows basic authentication and authorization, uploading files and delete files operations.

## Precondition
see [Precondition](../README.md)

## build and run

* compile

```
mvn clean install
```

* run
  * create a database using mysql with user name and password（e.g. root/root）
  * execute create_db_user.sql

* run user-service:

```
java $JAVA_OPT -Ddb.url="jdbc:mysql://localhost/porter_user_db?useSSL=false" -Ddb.username=root -Ddb.password=root -jar porter-user-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```
Note: If startup fails, try removing "$JAVA_OPT" and ">/dev/null 2>&1 &" (same below)

* run file-service:

```
java $JAVA_OPT -jar porter-file-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```

* run gateway-service:

gateway-service contains static web pages in resources/ui. First copy to web root folder，e.g webapp, which is relative to working directory
(Note:"porter_lightweight/gateway-service/src/main/resources/microservice.yaml"The related configuration"gateway:webroot: /code/servicecomb-samples/porter_lightweight/gateway-service/src/main/resources"
Is the location of its own native code). 

```
java $JAVA_OPT -Dgateway.webroot=webapp -jar porter-gateway-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```

## Try it

1. Using browser: http://localhost:9090/ui/login.html with user admin or guest to login, password is test.
2. Choose a file to upload. Uploaded file is stored in file-service working directory. And name is random number generated. 
3. Delete file. Input the random number generated in step 2. 


## 前提条件

查看 [前提条件](../README.md)

这个项目帮助开发者学习如何使用ServiceComb开发完整的微服务。 这个项目实现的功能非常简单，用户登录后，上传一个文件和删除一个文件，验证了没有权限的用户无法删除文件。

# 编译和运行

* 编译

```
mvn clean install
```

* 运行
  * 安装mysql数据库，设置用户名密码（假设为root/root）
  * 执行脚本create_db_user.sql


* 启动user-service:

```
java $JAVA_OPT -Ddb.url="jdbc:mysql://localhost/porter_user_db?useSSL=false" -Ddb.username=root -Ddb.password=root -jar porter-user-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```
注意：如果启动不了，可以尝试去掉“$JAVA_OPT”和“>/dev/null 2>&1 &”（下同）

* 启动file-service:

```
java $JAVA_OPT -jar porter-file-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```

* 启动gateway-serivce:

gateway-service包含了静态页面文件，在resources/ui目录。首先需要将页面文件拷贝到WEB主目录（相对路径，当前运行目录），比如: webapp，然后将ui目录整体拷贝到webapp/ui目录(注意：porter_lightweight/gateway-service/src/main/resources/microservice.yaml中的相关配置
gateway:webroot: /code/servicecomb-samples/porter_lightweight/gateway-service/src/main/resources是自己本地代码的位置)。启动：
```
java $JAVA_OPT -Dgateway.webroot=webapp -jar porter-gateway-service-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
```

# 使用

1. 输入: http://localhost:9090/ui/login.html 使用admin或者guest登陆，密码为test。
2. 选择一个文件上传，上传成功，上传成功后的文件会保存在file-service的当前目录， 文件名称是一个随机的数字，这个数字就是文件ID。
3. 删除文件：输入上一步的文件ID，点击删除。 如果是admin用户，上传成功；如果是guest用户，上传失败。

# 接口使用说明
ServiceComb推荐先定义接口，再定义实现的开发模式。将接口定义为一个独立项目，可以由设计者统一管控，对于接口的修改，需要设计者进行审核。先定义接口还可以让开发者培养良好的开发习惯，避免将对外接口采用内部实现数据结构（比如JsonObject）、运行平台有关的数据结构（比如HttpServletResponse、HttpServletRequest）来定义，使得以后将项目改造为其他技术框架变得复杂。采用这种方式组织的项目，用户很容易在不同的开发框架上进行迁移，比如gRPC、Spring Cloud等。这里的接口定义代码，对于这些运行框架都是通用的，并且具备跨平台特性。

## 对于接口实现者(provider)
  * 依赖api对应的jar包

```
  <dependencies>
    <dependency>
      <groupId>org.apache.servicecomb.samples.porter</groupId>
      <artifactId>user-service-api-endpoint</artifactId>
    </dependency>
  </dependencies>
```

  * 实现Service接口
```
@Service
public class UserServiceImpl implements UserService
```

## 对于接口使用者(consumer)
  * 依赖service对应的jar包（只包含model和接口）

```
  <dependencies>
    <dependency>
      <groupId>org.apache.servicecomb.samples.porter</groupId>
      <artifactId>user-service-api-service</artifactId>
    </dependency>
  </dependencies>
```

  * 采用RPC方式调用
```
  @RpcReference(microserviceName = "user-service", schemaId = "user")
  private static UserService sserService
```

# REST接口常见争议问题和处理

## 查询接口参数很复杂

比如：

```
public List<Goods> queryGoodsByTags(String orgId, List<GoodsTag> tags)
```

当查询参数很复杂的时候，不建议采用query参数或者path参数。主要有如下原因：

* HTTP对于URL的长度有限制
* 复杂参数可能包含特殊字符，需要客户端在拼接URL的时候，进行URL转码，客户端开发者通常会遗漏。
* 对象无法和query参数进行合理的映射。
* query参数或者path参数会被proxy、web server等记录到日志里面，导致日志过大，或者造成敏感信息泄露。

对于复杂的查询操作，建议使用POST方法，相关复杂参数都封装为body。比如：

```
@PostMapping(path = "queryGoodsByTags")
public List<Goods> queryGoodsByTags(@RequestParam(name = "orgId") String orgId, @RequestBody List<GoodsTag> tags)
```

一般的，通过query传递参数的场景，尽可能要保证参数个数少，参数类型为基础类型（字符串、数字等）。参数比较多的场景采用POST来传参。

本项目的处理原则是“尽可能遵循HTTP REST语义，但是不盲目，以系统可靠优先”。

对于DELETE请求，也有类似的情况。在设计GET和DELETE方法时，建议都不使用body参数，尽管HTTP协议并没有强制要求不能使用body，但是由于历史因素，很多WEB服务器支持上会有问题，接口设计应该尽可能避免不必要的麻烦和陷阱。只在POST、PUT、PATCH方法中使用body参数。

## REST接口的Path是否包含操作

比如：下面的接口定义path是否包含deleteGoodsUnitConvertor。 

```
  @DeleteMapping(path = "deleteGoodsUnitConvertor")
  public ResponseBase deleteGoodsUnitConvertor(String goodsUnitConvertorId)
```

由于HTTP的方法POST/PUT/PATCH/GET/DELETE已经包含了增、改、查、删语义，path里面包含delete显得多余。不过由于项目的接口通常比较多，过多的思考接口语义反而增加了理解的难度。所以本项目path全部都包含了方法名字。包含名称有个好处，可以从URL中看出operation id，从而很简单的将URL和契约对应起来，方便查找。此外就是上面提到的原因，并不是所有的删除操作都一定对应于DELETE操作，出于系统可靠性、安全等方面考虑，可能使用POST/PATCH等代表查询或者删除操作。

## 多个对象参数

由于HTTP只能有一个body，所有多个对象参数需要包装为一个参数。 比如：

```
public ResponseBase inboundOrder(InboundOrder inboundOrder, Set<InboundOrderItem> inboundOrderItems)
```

封装为下面的REST接口定义：

```
  @PostMapping(path = "inboundOrder")
  public ResponseBase inboundOrder(@RequestBody InboundOrderRequest inboundOrderRequest) {
    return stockService.inboundOrder(inboundOrderRequest.getInboundOrder(), inboundOrderRequest.getInboundOrderItems());
  }
```

## 敏感信息不能采用query参数

Query参数可能被各种proxy、web server记录，因此对于用户敏感信息，不能使用query参数。 比如：

```
public ResponseBase rechargePrepaidCard(String cardId, double amount)
```

涉及到卡号和金额的数据，需要采用POST提交，参数存储在body。 虽然有些接口仅仅只是查询， 但也可能被设计为POST。 调整后的接口：

```
public ResponseBase rechargePrepaidCard(@RequestBody PrepaidAmountRequest prepaidAmountRequest)
```
