# Customized Handler Sample

ServiceComb 3.X support users to define a customized filter and invoke the filter in filter chain.

The new Filter-based processing chain mechanism replaces the legacy Handler-based mechanism to provide enhanced support for asynchronous processing. It unifies mechanisms such as Handler, HttpServerFilter, and HttpClientFilter, representing them all as Filters.

> See:
>
> - [Java Chassis 3版本介绍](https://servicecomb.apache.org/references/java-chassis/zh_CN/introduce3.x.x.html)
> - [处理链介绍](https://servicecomb.apache.org/references/java-chassis/zh_CN/references-handlers/intruduction.html#_2)

1. Customize a filter by implement filter interface, for example:

    ```java
    @Component
    public class MyFilter extends AbstractFilter implements ConsumerFilter {

      private static final Logger LOGGER = LoggerFactory.getLogger(MyFilter.class);

      @Override
      public String getName() {
        return "my-filter";
      }

      @Override
      public int getOrder() {
        return Filter.CONSUMER_LOAD_BALANCE_ORDER + 1;
      }

      @Override
      public CompletableFuture<Response> onFilter(Invocation invocation, FilterNode nextNode) {
        LOGGER.info("It's my filter! \r\n");
        return nextNode.onFilter(invocation);
      }
    }
    ```

2. Configure customized `MyFilter` in `microservice.yaml` file along with other handler together.

    ```yaml
    servicecomb:
      # filters are enabled by default, configuration is optional
      filter:
        # Filter name
        my-filter:
          # Application name + Service name
          chstest.chsClient.order: 10
          chstest.chsClient.enabled: true
    ```

    > Filters are enabled by default, configuration is optional.

## Precondition

see [Precondition](../../README.md)

## Sample Quick Start

### Start the ServiceComb/Service Center

- [how to start the service center](http://servicecomb.apache.org/docs/products/service-center/install/)
- make sure service center address is configured correctly in `microservice.yaml` file

```yaml
servicecomb:
  service:
    registry:
      address: http://127.0.0.1:30100 #service center address
```

### Start the custom-handler-provider service

- Start provider service by maven

  Compile the source code, and use `mvn exec` to execute the main class `CustomHandlerProviderMain`.

  ```bash
  mvn clean install
  cd custom-handler-sample/custom-handler-provider/
  mvn exec:java -Dexec.mainClass="org.apache.servicecomb.samples.customerhandler.provider.CustomHandlerProviderMain"
  ```

- Start provider service by IDE

  Import the project by InteliJ IDEA or Eclipse, then find `main` function `CustomHandlerProviderMain` of provider service and `RUN` it like any other Java program.

### Start the custom-handler-consumer service

Just like how to start custom-handler-provider service. But the main class of custom-handler-consumer service is `CustomHandlerCustomerMain`.

```bash
cd custom-handler-sample/custom-handler-consumer
mvn exec:java -Dexec.mainClass="org.apache.servicecomb.samples.customerhandler.consumer.CustomHandlerCustomerMain"
```

### How to verify

On the producer side, the output should contain the following stuffs if the producer starts up successfully:

1. *`swagger: 2.0 info: version: 1.0.0 ...`* means the producer generated swagger contracts
2. *`rest listen success. address=0.0.0.0:8080`* means the rest endpoint is listening on port 8080

On the consumer side, you can see the following outputs:

1. *`It's my handler!`* means custom handler had take effect
