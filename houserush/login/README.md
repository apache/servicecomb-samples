## 微服务 login

该微服务作为认证中心，主要负责token的生成与校验。

### 主要功能

- 登录账号的维护

- 生成token

- 校验token

### 设计原理

基于[JWT](https://jwt.io/introduction/)实现易于横向拓展的分布式认证中心。

JWT标准格式

```json
{
  //Header
  "alg": "HS256",
  "typ": "JWT",
  //Payload
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true,
  //Signature
  HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
}
```

[JWT的一种实现](https://github.com/auth0/java-jwt)



### 数据库设计

users表

| id              | int          | 主键id   |
| :-------------- | ------------ | -------- |
| username        | varchar(255) | 用户名   |
| hashed_password | varchar(255) | 密码hash |
| deleted_at      | timestamp    | 删除时间 |
| created_at      | timestamp    | 创建时间 |
| update_at       | timestamp    | 更新时间 |

##### 聚合

user

```java
class User{
    
    //对密码hash加密
    String makeHashedPassword(){};
    
    //使用auth0的java-jwt库来生成jwt
    String generateToken() {};
    
    //解密token获取Id
    int verifyTokenGetUserId(){};
}
```



### 接口设计

```java
  User createUser(User user);

  User findUser(int id);

  void removeUser(int id);

  boolean updatePassword(int id,String oldPassword,String newPassword)

  //登录，签发token
  User signin(User user);

  //校验token
  User verifyToken(String token);
```

