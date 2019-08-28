### Jmeter 测压

##### 1.机器配置

ubuntu,8核16G 2.6GHZ

##### 2. 测试流程



##### 3.测试结果

- 1500线程数 一个实例

![](C:\Users\linzibin\Desktop\jmeter\result_img\8x2.6_16G_1500.PNG)

- 1500线程数 3个house-order实例

![](C:\Users\linzibin\Desktop\jmeter\result_img\8x2.6_16G_1500_3h.PNG)

- 2000线程数

![](C:\Users\linzibin\Desktop\jmeter\result_img\8x2.6_16G_2000.PNG)

###### 修改了find sale的接口的导致的n+1查询问题后，性能有了明显的提升

- 1500线程数

![](C:\Users\linzibin\Desktop\jmeter\result_img\8x2.6_16G_1500_mysql.PNG)

- 2000线程数

![](C:\Users\linzibin\Desktop\jmeter\result_img\8x2.6_16G_2000_mysql.PNG)

- 5000线程数

![](C:\Users\linzibin\Desktop\jmeter\result_img\8x2.6_16G_5000_mysql.PNG)

- 5000线程数 按微服务分库

![](C:\Users\linzibin\Desktop\jmeter\result_img\8x2.6_16G_5000_mysql_fenku.PNG)

- 5000线程数 两个order_house 和两个realestate 按微服务分库

![](C:\Users\linzibin\Desktop\jmeter\result_img\8x2.6_16G_5000_mysql_h2_r2_分库.PNG)

- 5000线程 5台压测机每台1000,两台服务器，一台跑数据库，另一台跑服务（单一实例），

![](C:\Users\linzibin\Desktop\jmeter\result_img\8.22\8x2.6_16Gx2_5000_.PNG)

- 7500线程 5台压测机每台1500,两台服务器，一台跑数据库，另一台跑服务（），

![](C:\Users\linzibin\Desktop\jmeter\result_img\8.22\8x2.6_16Gx2_7500_.PNG)

- 7500线程 5台压测机每台1500,两台服务器，一台跑数据库，另一台跑服务（house-order两个实例，其他单一实例），

![](C:\Users\linzibin\Desktop\jmeter\result_img\8.22\8x2.6_16Gx2_7500_2.PNG)

- 10000线程 5台压测机每台2000,两台服务器，一台跑数据库，另一台跑服务（单一实例），
  - 跑不动，超时
- 10000线程 5台压测机每台2000,两台服务器，一台跑数据库，另一台跑服务（house-order两个实例，其他单一实例）,或出现部分异常

![](C:\Users\linzibin\Desktop\jmeter\result_img\8.22\8x2.6_16Gx2_10000_.PNG)





- 7500线程 10台压测机，每台750，一台跑数据库，另一台跑服务（单一实例）





