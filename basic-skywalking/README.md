

# 项目说明

这个项目提供了 Java Chassis 的简单例子，例子包括：

* provider  
使用 Java Chassis 开发一个 REST 接口。

* consumer  
使用 Java Chassis 开发一个REST接口，接口实现通过 RPC 调用 provider 的接口。

## 使用
1. 首先在网上下载skywalking的软件压缩包，本文实例中使用的是apache-skywalking-apm-es7-8.0.0.tar.gz，下载后直接解压缩即可。
2. 解压完成之后打开config目录下的application.yml,分别作如下修改：

>storage:  
selector: ${SW_STORAGE:mtsql}

>&#35;h2:  
&#35;driver: ${SW_STORAGE_H2_DRIVER:org.h2.jdbcx.JdbcDataSource}  
&#35;url: ${SW_STORAGE_H2_URL:jdbc:h2:mem:skywalking-oap-db;DB_CLOSE_DELAY=-1}  
&#35;user: ${SW_STORAGE_H2_USER:sa}  
&#35;metadataQueryMaxSize: ${SW_STORAGE_H2_QUERY_MAX_SIZE:5000}  
&#35;maxSizeOfArrayColumn: ${SW_STORAGE_MAX_SIZE_OF_ARRAY_COLUMN:20}  
&#35;numOfSearchableValuesPerTag: ${SW_STORAGE_NUM_OF_SEARCHABLE_VALUES_PER_TAG:2}  
mysql:  
properties:  
jdbcUrl: ${SW_JDBC_URL:"jdbc:mysql://localhost:3306/swtest"}  
dataSource.user: ${SW_DATA_SOURCE_USER:root}  
dataSource.password: ${SW_DATA_SOURCE_PASSWORD:root@1234}  
dataSource.cachePrepStmts: ${SW_DATA_SOURCE_CACHE_PREP_STMTS:true}  
dataSource.prepStmtCacheSize: ${SW_DATA_SOURCE_PREP_STMT_CACHE_SQL_SIZE:250}
dataSource.prepStmtCacheSqlLimit: ${SW_DATA_SOURCE_PREP_STMT_CACHE_SQL_LIMIT:2048}
dataSource.useServerPrepStmts: ${SW_DATA_SOURCE_USE_SERVER_PREP_STMTS:true}
metadataQueryMaxSize: ${SW_STORAGE_MYSQL_QUERY_MAX_SIZE:5000}

这里做的修改是将skywalking的存储数据库修改成mysql，原先默认是h2
3. 复制agent文件夹到你要追踪的微服务的部署服务器上，在微服务项目中增加JVM启动参数如下：
>-javaagent:C:\Users\lwx1108349.CHINA\Downloads\apache-skywalking-apm-es7-8.0.0\apache-skywalking-apm-bin-es7\agent\skywalking-agent.jar  
-Dsktingwalking.collector.backend_service=localhost:11800  
-Dskywalking.agent.serivce_name=Your APPlication Name

其中YourPath是agent文件夹所在的路径,Your Application Name是该微服务在skywalkingUI界面上的名字。
4. 然后下载mysql驱动，本文使用的是mysql-connector-java-5.1.46.jar,下载完成之后，将该jar包放到oap-libs下面。
5. 然后用mysql创建一个数据库，库名，用户名和密码一定要和第二步保持一致。
6. 然后就可以启动微服务引擎和相应微服务，等所有启动服务完成之后，就可以启动bin目录下面的startup.bat。


* 测试

所有潜质工资完成之后，然后通过界面访问：http://localhost:8080(skywalking的默认端口是8080，如有更换需求，在webapp文件夹下面的webapp.yml文件下进行更换)。

## 前提条件

运行这些例子之前，需要先准备CSE运行环境。

* 通过下载安装 [本地简化版 CSE](https://support.huaweicloud.com/devg-servicestage/ss-devg-34.html) ,搭建本地开发环境。
* 使用[华为云云服务](https://support.huaweicloud.com/devg-servicestage/ss-devg-0002.html) 。

[更多信息](https://support.huaweicloud.com/devg-servicestage/ss-devg-0006.html) 可以参考开发指南。

CSE运行环境准备好以后，编辑每个微服务的`bootstrap.yml`文件，配置正确的CSE服务信息，比如配置中心、注册中心的地址。

例子默认使用微服务引擎1.0版本，配置中心得类型为config-center,如果使用微服务引擎2.0，配置文件要做如下修改:

```
servicecomb:
  kie:
    serveUri:
```