## 微服务 realestate

该微服务用于楼盘及房源管理

###快速开始

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


### 接口设计

```java
package org.apache.servicecomb.samples.practise.houserush.realestate.api;

public interface RealestateApi {
    /**
    * 新增楼盘
    * @param realestate 楼盘信息
    * @return Realestate 添加成功后的楼盘信息
    */
    Realestate createRealestate(Realestate realestate);
    
    /**
    * 查询楼盘
    * @param id 楼盘id
    * @return Realestate 楼盘信息
    */
    Realestate findRealestate(int id);
    
    /**
    * 修改楼盘信息
    * @param id 楼盘id
    * @param realestate 楼盘信息
    * @return Realestate 修改成功后的楼盘信息
    */
    Realestate updateRealestate(int id, Realestate realestate);
    
    /**
    * 删除楼盘
    * @param id 楼盘id
    */
    void removeRealestate(int id);
    
    /**
    * 查询所有楼盘
    * @return List<Realestate> 所有楼盘列表
    */
    List<Realestate> indexRealestates();
    
    /**
    * 新增建筑楼
    * @param realestateId 楼盘id
    * @param building 建筑楼信息
    * @return Building 添加成功后的建筑楼信息
    */
    Building createBuilding(int realestateId, Building building);
    
    /**
    * 查询建筑楼
    * @param id 建筑楼id
    * @return Building 建筑楼信息
    */
    Building findBuilding(int id);
    
    /**
    * 更改建筑楼信息
    * @param id 建筑楼id
    * @param building 建筑楼信息
    * @return Building 更改成功后的建筑楼信息
    */
    Building updateBuilding(int id, Building building);
    
    /**
    * 删除建筑楼
    * @param id 建筑楼id
    */
    void removeBuilding(int id);
    
    /**
    * 查询某一楼盘下的所有建筑楼
    * @param realestateId 楼盘id
    * @return List<Building> 建筑楼列表
    */
    List<Building> indexBuildings(int realestateId);
    
    /**
    * 新增房源信息
    * @param buidingId 建筑楼id
    * @param house 房源信息
    * @return House 添加成功后的房源信息
    */
    House createHouse(int buidingId, House house);
    
    /**
    * 查询房源信息
    * @param id 房源id
    * @return House 房源信息
    */
    House findHouse(int id);
    
    /**
    * 更改房源信息
    * @param id 房源id
    * @param house 房源信息
    * @return House 更改成功后的房源信息
    */
    House updateHouse(int id, House house);
    
    /**
    * 删除房源信息
    * @param id 房源id
    */
    void removeHouse(int id);
    
    /**
    * 查询某一建筑楼下的所有房源
    * @param buildingId 建筑楼id
    * @return List<House> 所有房源列表
    */
    List<House> indexHouses(int buildingId);
    
    /**
    * 锁定已售房源
    * @param ids 已售房源id列表
    * @return List<House> 锁定的房源列表
    */
    List<House> lockHousesForSale(List<Integer> ids);
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
            .aggregate: 项目实体类所在包,其中Building、House、Realestate类为JPA实体
            .api: Rest接口定义及实现包
                .RealestateApi: 接口定义
                .RealestateApiRestImpl: 接口实现
            .dao: 数据访问对象所在包,其中数据库操作都是Spring-Data-JPA实现
            .service: 各种增删改查具体逻辑实现
            .RealestateConfig: 微服务配置类
            .RealestateApplication: 微服务启动类
    /resources: 项目资源文件所在目录
        /microservice.yaml: 微服务配置文件，其中关键内容在上文"快速开始"中已经提及。
pom.xml: maven配置文件

```