本项目提供认证鉴权服务的实现，主要提供了基于角色的权限管理，和基于JWT的微服务授权模式。微服务的命名参考了OAuth2协议里面的命名方式。可以参考[OAuth2.0原理和验证流程分析](https://www.jianshu.com/p/d74ce6ca0c33)对于OAuth2认证过程的介绍，本项目的认证过程非常类似OAuth2的密码模式。

项目的目标是提供一个商业可用的鉴权实现，对于项目代码实现的问题可以提交issue，本项目也接纳PR，共同完善。

* AuthenticationServer

认证鉴权服务。提供用户管理、角色管理。并提供登录认证、权限查询等接口。鉴权服务及相关API是核心交付件，也是能够被重用的部分。开发者可以基于这个项目开发认证鉴权服务。

* Gateway
提供请求拦截，校验用户是否已经经过认证。一方面演示网关如何和配套鉴权服务完成开发，本项目也是自动化测试的组成部分。

* Client
Client模拟的是使用使用者。一方面演示客户端如何获取Token，本项目也是自动化测试的组成部分。

* ResourceServer
ResourceServer模拟的是业务服务。一方面演示业务服务如何进行权限配置，本项目也是自动化测试的组成部分。


## 实现说明

* 用户管理
用户管理采用了org.springframework.security.core.userdetails的模型，包括：
  1. UserDetailsService：加载用户信息。
  2. UserDetails：用户信息。
  3. GrantedAuthority：角色信息。
  4. PasswordEncoder：用户密码加密和匹配。