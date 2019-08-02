## 微服务 House-Order

### 主要职责
该微服务主要有4个职责
1. 管理员用作开售活动的管理。
2. 普通用户"**抢购**"房产。
3. 管理员获取抢购活动的结果。
4. 普通用户查询自己的抢购结果。

### 主要设计考量
1. 简单，抢购的核心接口只有1，2个，因为这部分后续需要优化性能，所以越简单，后续优化越容易。
2. 第一版利用同步调用的方式，尽量不引入冗余数据，减少数据一致性的考量。

### 后续改进的关注点 TODO list:
1. 需要减少对其他微服务的同步调用，考虑引入异步的事务消息更新机制。提高抢购的性能。
2. 可能会引入缓存系统，初步定为redis。
3. 需要跨微服务事务机制，保证数据库的一致性。

### 数据表设计

主要有2张表:
1. house_orders表，这张表既表示了待抢房源，也表示了抢购订单（抢购结果），差别在与customer_id列是否为null和state列的值。
在开始抢购之前，customer_id为空，state为new, 记录这次开售活动有哪些房源参与销售。开售中和开售后记录抢购结果。
即谁抢购到了该房产。
2. sales表，表示开售活动，主要记录了开售活动的起止时间。sale has_many house_orders。表示这次开售
活动有哪些房产可售。

sales表:

| column name     | type         |  brief     | description       |
| --------------  | ------------  | -------   | --------------    |
| id              | int           | 主键      |  |
| state           | varchar(20)   | 订单状态   | new->published-> ongoing -> finished |
| begin_at        | timestamp     | 开售活动开始时间   |  |
| end_at          | timestamp     | 开售活动结束时间   |  |
| realestate_id   | int           | 楼盘id    | 一次开售活动只能针对同属一个楼盘的部分房源 |
| deleted_at      | timestamp     | 开售活动删除时间 | 一次开售活动只有在未发布前可以进行删除或编辑,软删除方案 |


house_orders表:

| column name     | type         | 简介     | 更多说明        |
| --------------  | ------------  | -------  | -------------- |
| id              | int           | 主键      |  |
| sale_id         | int           | 外键：开售活动id |  |
| customer_id     | int           | 客户id  | 记录了谁抢到了该房源,开售前为空 |
| state           | varchar(20)   | 订单状态 | new 是待抢，confirmed是已抢 |
| ordered_at      | timestamp     | 抢购时间  | state变为confirmed的时间 |

### 接口设计


##### 管理开售活动相关
```java
  @PostMapping("sales")
  Sale createSale(@RequestBody Sale sale)；

  @GetMapping("sales/{saleId}")
  Sale findSale(@PathVariable int saleId)；
  //获取开售活动详情。
  
  @PutMapping("sales/{saleId}")
  Sale Sale updateSale(@PathVariable int saleId, @RequestBody Sale sale)；
  //开售活动发布是通过该接口，一旦发布，不能再修改了,也不能删除。

  @DeleteMapping("sales/{saleId}")
  void removeSale(@PathVariable int saleId)；
  
  @GetMapping("sales")
  List<Sale> indexSales();
  //对客户列出客户有抢购资格的还未开始的开售活动，已开始时间排序；对管理员，列出所有的开售活动。

  @PostMapping("sales/{saleId}/house_orders")
  createHouseOrders(@PathVariable int saleId, @RequestBody List<Integer> houseIds)；
  //为开售活动创建房源，创建时需要检查这些房源是否已被其他开售活动锁定或售出。
  //需要先对这些房源加锁后再添加到当前开售活动中，要注意事务的应用。
  
```

##### 执行抢购相关
```java
  @PutMapping("house_orders/{houseOrderId}")
  HouseOrder placeHouseOrder(@RequestHeader int customerId, @PathVariable int houseOrderId)
  //修改house_orders的customer_id列，抢购前需要检查该房源是否已被占用，
  // select for update加锁，注意事务应用。
  //
```

### 依赖其他微服务。
1. find 和 index Sale的时候，需要调用realestate微服务的相关接口获取
Sale关联楼盘的详细信息。
2. createHouseOrders接口需要依赖realestate微服务查询房源状态和加锁，见接口说明。
3. 依赖CustomerManage微服务，查询当前抢购的客户是否还有抢购的资格。


