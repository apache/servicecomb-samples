#### jmeter 分布式部署

选择一台机器当调度机（master），其他机器当压力机（slave）

master控制slave的启动和停止，同时会下发测试脚本给slave

- 注意: 如果用到csv进行参数化，则需要把csv放到每台机器上，并保证路径一致

##### 配置master

​	1.修改jmeter/bin目录下的jmeter.properties,在260行左右的remote_hosts添加slave的ip和端口，端口默认是1099，如果没有修改的话，端口可以省略。ip与ip之间用逗号隔开。

```properties
#例如
remote_hosts=192.168.88.1,192.168.88.2,192.168.88.3:1099
```

可能的错误：

- 如果机器有多网卡，有可能会出现报错或者汇总不到slave的回发的信息。

指定网卡ip

​	windows配置，修改jmeter.bat

```powershell
#新增
setlocal
set rmi_host=-Djava.rmi.server.hostname=192.168.88.72
##然后添加 %rmi_host%到ARGS后面，在190行左右
set ARGS=%JAVA9_OPTS% %DUMP% %HEAP% %VERBOSE_GC% %GC_ALGO% %DDRAW% %SYSTEM_PROPS% %JMETER_LANGUAGE% %RUN_IN_DOCKER% %rmi_host%

```

linux配置，修改jmeter.sh

```bash
#在接近末尾位置添加
rmi_host=-Djava.rmi.server.hostname=192.168.88.188
#然后把 rmi_host 放到"${PRGDIR}/jmeter" 后面
"${PRGDIR}/jmeter" ${rmi_host}  "$@"
```

##### 配置slave

​	修改jmeter-server

```bash
#设置RMI_HOST_DEF，指定本机ip
RMI_HOST_DEF=-Djava.rmi.server.hostname=192.168.88.107
```

然后./jmeter-server启动，就可以在master控制了

- 当线程数过多时，有可能会出现内存溢出错误，可以修改jmeter

  ```bash
  #大概在159行，可设置内存大小
  : "${HEAP:="-Xms1g -Xmx1g -XX:MaxMetaspaceSize=256m"}"
  ```

  





























