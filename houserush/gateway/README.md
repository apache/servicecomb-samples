## 微服务 gateway

HouseRush应用网关，作为前端与后端通信的统一入口，为房源管理、订单中心等微服务提供路由和认证鉴权的功能

### 主要功能

- API入口
- 动态路由
- 鉴权

### 设计原理
- 使用[zuul](https://github.com/Netflix/zuul/wiki)来设计实现API网关功能
- [使用zuul做边缘服务](https://docs.servicecomb.io/java-chassis/zh_CN/edge/zuul.html)
![API gateway工作流程](../doc/images/ApiGateway.png)

### 实现

- 路由转发配置

- 鉴权

  - 自定义AuthorizeFilter

    ```java
    @Component
    public class AuthorizeFilter extends ZuulFilter {
        @Override
      public String filterType() {
        return "pre";
      }
    
      @Override
      public int filterOrder() {
        return 0;
      }
      @Override
      public boolean shouldFilter() {
        return true;
      }
      @Override
      public Object run() {
          //根据uri判断是否需要鉴权
          //向认证中心登录获取token
          //提取request的header中的Authorization字段的token
          //向认证中心检验Authorization是否有效
          //...
      }
        
        
    }
    ```