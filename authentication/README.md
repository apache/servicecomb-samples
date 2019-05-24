This project demonstrates authentications and authorizations based on JWT/OAuth2. Projct names follow OAuth2 architecture. 

## Implementations

This project uses spring security API and mainly designed for ServiceComb architecture.

* User Management

  1. UserDetailsService: load users information
  2. UserDetails: User information
  3. GrantedAuthority: authorities
  4. PasswordEncoder: encode or verify user password

## Project

* AuthenticationServer

Authentication server implementation. Provides APIs to login, and query roles, etc. 


* Gateway

Check if users are authenticated and dispatch HTTP request.

* Client

Demonstrates how client uses this project. Integration tests are provided. 


* Api
Reusable part. 

* For testing

Run AuthenticationServer、Gateway、Client、ResourceServer and call

```
http://localhost:9093/v1/test/start
```

see AuthenticationTestCase for details.


本项目提供认证鉴权服务的实现，主要提供了基于角色的权限管理，和基于JWT的微服务授权模式。微服务的命名参考了OAuth2协议里面的命名方式。可以参考[OAuth2.0原理和验证流程分析](https://www.jianshu.com/p/d74ce6ca0c33)对于OAuth2认证过程的介绍，本项目的认证过程非常类似OAuth2的密码模式。

项目的目标是提供一个商业可用的鉴权实现，对于项目代码实现的问题可以提交issue，本项目也接纳PR，共同完善。



## 实现说明

* 用户管理
用户管理采用了org.springframework.security.core.userdetails的模型，包括：
  1. UserDetailsService：加载用户信息。
  2. UserDetails：用户信息。
  3. GrantedAuthority：角色信息。
  4. PasswordEncoder：用户密码加密和匹配。
  
  
## 项目结构介绍

* AuthenticationServer

认证鉴权服务。提供用户管理、角色管理。并提供登录认证、权限查询等接口。鉴权服务及相关API是核心交付件，也是能够被重用的部分。开发者可以基于这个项目开发认证鉴权服务。

* Gateway
提供请求拦截，校验用户是否已经经过认证。一方面演示网关如何和配套鉴权服务完成开发，本项目也是自动化测试的组成部分。

* Client
Client模拟的是使用使用者。一方面演示客户端如何获取Token，本项目也是自动化测试的组成部分。

* ResourceServer
ResourceServer模拟的是业务服务。一方面演示业务服务如何进行权限配置，本项目也是自动化测试的组成部分。

* Api
认证鉴权提取的公共功能，作为复用单元。目前项目处于初始阶段，很多复用代码分散在其他项目中。


* 测试介绍

本项目实现了微服务架构的自动化测试。启动AuthenticationServer、Gateway、Client、ResourceServer后，可以提供

```
http://localhost:9093/v1/test/start
```
触发测试用例的执行。 所有的测试用例放到Client微服务里面， 这个微服务实现了简单的测试框架帮助书写测试用例，对测试结果进行检查等功能。 

测试项目同时展示了这个项目的功能，比如： AuthenticationTestCase 的测试逻辑展示了基本的认证功能，从登陆，到接口的权限检查。 

# TODO LIST
1. provide TLS for authentication server & edge service
2. grant scope for INTERNAL access & EXTERNAL access 
3. access token support: a. use access token to get optional scope or roles token. 这个可以解决角色过多的时候， token过大的一些问题
4. 实现注销逻辑（会话管理）
5. 支持分层的角色机制

       ROLE_LEVEL1
        /       \
   ROLE_LEVEL2  ROLE_LEVEL2

 TOKEN里面只返回ROLE_LEVEL1，设置为ROLE_LEVEL2访问的操作，也可以访问。

6. REFRESH_TOKEN可以用来实现申请不同SCOPE的TOKEN。 
7. 设计目标：无状态。认证服务器和资源服务器均可以多实例部署，每个实例之间不共享状态。在实现很多功能的时候，都遵循这个约束。包括通过refresh token获取新的access token的时候。遵循这个约束，意味着请求需要同时传递refresh token和access token。 
8， 重新设计TOKEN（代码重构、支持会话管理），支持OpenID Connect。

OAUTH的不好的地方：TOKEN在有效期内，容易被利用，无法注销；TOKEN过期后，必须重新认证，和用户是否在一直操作无关，体验不好，虽然可以通过refresh_token获取新的token提升体验，但是refresh_token有效期如果设置的太长，会降低安全性。Token在有效期内，如果修改了权限等信息，无法及时感知，需要重新登录。
OAUTH的好的地方：TOKEN签发、认证都可以由微服务实例独自完成，不需要共用的数据存储，比如数据库、Redis等，效率更高，弹性扩容。


