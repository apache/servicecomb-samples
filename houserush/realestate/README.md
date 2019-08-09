## 微服务 realestate

该微服务用于楼盘及房源管理

### 快速开始

1、参考[ServiceComb快速入门](http://servicecomb.apache.org/cn/docs/quick-start/)安装开发环境:

- 安装git,详情可参考[git安装教程](https://git-scm.com/book/zh/v2/%E8%B5%B7%E6%AD%A5-%E5%AE%89%E8%A3%85-Git)。
- 安装JDK 1.8,详情可参考[JDK安装教程](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)。
- 安装Maven 3.x, 详情可参考[Maven安装教程](https://maven.apache.org/install.html)。

2、运行 Service Center

- 安装Docker,详情可参考[Docker安装教程](https://www.docker.com/get-started)。
- 命令行或终端输入<code>$ docker pull servicecomb/service-center</code>拉取最新版servicecomb/service-center。
- 命令行或终端输入<code>$ docker run -d -p 30100:30100 servicecomb/service-center:latest</code>在30100端口运行service-center。

3、参考下文的[数据表设计](#dbDisign)创建数据库表。

4、配置realestate的数据库、服务中心地址以及启动端口

- 修改src/main/resources/microservice.yaml文件:
```yaml
#...
servicecomb:
  service:
    registry:
      address: http://127.0.0.1:30100 #service-center地址
  rest:
    address: 0.0.0.0:7771   #微服务启动端口
    #...
spring:
  #数据库url、用户名、密码配置
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/realestate?characterEncoding=utf8&useSSL=false
    username: houserush
    password: password
    #...
```
5、启动微服务
- 该微服务基于spring-boot开发，所以启动微服务只需要在微服务根目录下打开命令行或终端输入<code>$ mvn spring-boot:run</code>,或通过IDE启动spring-boot程序。

### 主要功能

- 楼盘的增删改查

- 建筑楼的增删改查

- 房源的增删改查

- 锁定已售房源

### 设计原理

一个楼盘可能含多栋建筑楼，一栋建筑楼可能含多个房源

### 数据表设计
<span id = "dbDisign" />

- realestates

| 字段            | 类型         | 描述      |
| :-------------- | ------------ | --------|
| id              | int          | 主键id   |
| name            | varchar(255) | 楼盘名称 |
| description     | varchar(2048)| 描述     |
| deleted_at      | timestamp    | 删除时间 |
| created_at      | timestamp    | 创建时间 |
| update_at       | timestamp    | 更新时间 |

- buildings

| 字段                    | 类型         | 描述       |
| :--------------------- | ------------ | --------- |
| id                     | int          | 主键id    |
| realestate_id          | int          | 所在楼盘id |
| name                   | varchar(255) | 建筑楼名称 |
| sequence_in_realestate | tinyint      | 栋        |
| deleted_at             | timestamp    | 删除时间   |
| created_at             | timestamp    | 创建时间   |
| update_at              | timestamp    | 更新时间   |

- houses

| 字段               | 类型         | 描述        |
| :---------------- | ------------ | ---------  |
| id                | int          | 主键id      |
| building_id       | int          | 所在建筑楼id |
| name              | varchar(255) | 房源名称     |
| layer             | int          | 房源所在楼层 |
| state             | varchar(255) | 房源状态     |
| sequence_in_layer | tinyint      | 房间号       |
| price             | decimal      | 价格        |
| deleted_at        | timestamp    | 删除时间     |
| created_at        | timestamp    | 创建时间     |
| update_at         | timestamp    | 更新时间     |

- housetype

| 字段               | 类型         | 描述        |
| :---------------- | ------------ | ---------  |
| id                | int          | 主键id      |
| name              | varchar(255) | 房型名称     |
| image_id          | int          | 房型图片id   |
| deleted_at        | timestamp    | 删除时间     |
| created_at        | timestamp    | 创建时间     |
| update_at         | timestamp    | 更新时间     |

### 接口设计

```java
package org.apache.servicecomb.samples.practise.houserush.realestate.api;

public interface RealestateApi {
    Realestate createRealestate(Realestate realestate);
    
      Realestate findRealestate(int id);
    
      Realestate updateRealestate(int id, Realestate realestate);
    
      void removeRealestate(int id);
    
      List<Realestate> indexRealestates();
    
      Building createBuilding(int realestateId, Building building);
    
      Building findBuilding(int id);
    
      Building updateBuilding(int id, Building building);
    
      void removeBuilding(int id);
    
      List<Building> indexBuildings(int realestateId);
    
      House createHouse(int buidingId, House house);
    
      House findHouse(int id);
    
      House updateHouse(int id, House house);
    
      void removeHouse(int id);
    
      List<House> indexHouses(int buildingId);
    
      List<House> lockHousesForSale(List<Integer> ids);
    
      HouseType createHouseType(HouseType houseType);
    
      void removeHouseType(int id);
    
      HouseType updateHouseType(int id, HouseType houseType);
    
      HouseType findHouseType(int id);
    
      List<HouseType> indexHouseTypes();
    
      HouseTypeImage createHouseTypeImage(MultipartFile file);
    
      void removeHouseTypeImage(int id);
    
      byte[] findHouseTypeImage(int id);
}
```

### Rest API调用示例
点击[houserush-realestate Rest API文档](https://documenter.getpostman.com/view/5023270/SVYkwMZd?version=latest)查看Rest API调用示例。

- 注意:示例中是通过Gateway网关服务调用的Realestate服务,所以示例调用接口较org.apache.servicecomb.samples.practise.houserush.realestate.api.RealestateApiRestImpl中声明的接口多含一个前缀/realestate.
- 鉴权操作、获取用户Token操作请移步[houserush-gateway服务文档](https://github.com/apache/servicecomb-samples/tree/master/houserush/gateway)

### 源码文件解析

```yaml
src/main:
    /java: java源码文件所在目录
        org.apache.servicecomb.samples.practise.houserush.realestate:
            .aggregate: 项目实体类所在包,其中Building、House、Realestate、HouseType类为JPA实体
            .api: Rest接口定义及实现包
                .RealestateApi: 接口定义
                .RealestateApiRestImpl: 接口实现
            .dao: 数据访问对象所在包,其中数据库操作是Spring-Data-JPA实现
            .filesystem: 文件系统相关操作类, 用于存储房型图片
                .FileStorage: 文件存储操作虚基类, LocalFileStorage是文件系统本地存储的实现,自定义你的FileStorage用于其他方式实现吧.
            .service: 各种增删改查具体逻辑实现
            .RealestateConfig: 微服务配置类
            .RealestateApplication: 微服务启动类
    /resources: 项目资源文件所在目录
        /microservice.yaml: 微服务配置文件，其中关键内容在上文"快速开始"中已经提及。
pom.xml: maven配置文件

```