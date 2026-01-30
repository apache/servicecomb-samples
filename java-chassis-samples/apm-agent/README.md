# APM-Agent

## Description

This project is an APM (Application Performance Monitoring) sample based on Java Agent technology, demonstrating how to provide non-intrusive performance monitoring capabilities for ServiceComb microservice applications through the Java Instrumentation API.

## APM Introduction

APM (Application Performance Monitoring) is a practice method for monitoring and managing software application performance and availability. APM systems typically provide the following core features:

- **Performance Metrics Collection**: Monitor key metrics such as response time, throughput, and error rate
- **Distributed Tracing**: Track request call chains across multiple services in microservice architecture
- **Business Metrics Monitoring**: Collect and analyze business-related performance data
- **Anomaly Detection and Alerting**: Detect performance issues promptly and notify relevant personnel

Common APM tools include: Zipkin, SkyWalking, Pinpoint, Jaeger, etc.

## ServiceComb APM Support

Apache ServiceComb provides flexible extension mechanisms to support APM integration:

### 1. BootListener Mechanism

ServiceComb provides extension points for application lifecycle through the `BootListener` interface, allowing custom logic to be injected at different stages of service startup:

```java
public interface BootListener {
  void onBootEvent(BootEvent event);
}
```

Supported event types include:

- `BEFORE_FILTER`: Before filter initialization
- `AFTER_FILTER`: After filter initialization
- `BEFORE_PRODUCER_PROVIDER`: Before Producer Provider initialization
- `AFTER_PRODUCER_PROVIDER`: After Producer Provider initialization
- `BEFORE_CONSUMER_PROVIDER`: Before Consumer Provider initialization
- `AFTER_CONSUMER_PROVIDER`: After Consumer Provider initialization
- `BEFORE_TRANSPORT`: Before transport layer initialization
- `AFTER_TRANSPORT`: After transport layer initialization
- `BEFORE_REGISTRY`: Before service registration
- `AFTER_REGISTRY`: After service registration
- `BEFORE_CLOSE`: Before service shutdown
- `AFTER_CLOSE`: After service shutdown

### 2. EventBus Event Mechanism

ServiceComb uses Guava EventBus to publish various events during microservice invocation. APM components can collect performance data by subscribing to these events:

- `InvocationStartEvent`: Invocation start event
- `InvocationStartSendRequestEvent`: Start sending request event
- `InvocationBusinessMethodStartEvent`: Business method execution start event
- `InvocationBusinessMethodFinishEvent`: Business method execution finish event
- `InvocationBusinessFinishEvent`: Business logic execution complete event
- `InvocationEncodeResponseStartEvent`: Start encoding response event
- `InvocationFinishEvent`: Invocation finish event (contains complete performance data)
- `InvocationTimeoutCheckEvent`: Invocation timeout check event
- `ServerAccessLogEvent`: Server access log event

**Base Event Types:**

- `InvocationBaseEvent`: Base class for all invocation events
- `InvocationWithResponseEvent`: Base class for invocation events that contain response information

### 3. Invocation Context

Each invocation has an independent context (`Invocation`) that supports storing local data, facilitating the transfer of APM-related information (such as TraceId, SpanId, etc.) across different stages of the invocation.

## Implementation Overview

### Core Components

1. **AgentMain**: Java Agent entry class
   - Injected before application startup through the `premain` method
   - Registers Spring `ApplicationContextInitializer` to ensure APM listener is registered as a Spring Bean
   - Uses bytecode enhancement technology to support Web container environments

2. **ApmBeanRegistrar**: Spring Bean registrar
   - Implements the `ApplicationContextInitializer` interface
   - Dynamically registers `ApmBootListener` as a Bean during Spring container initialization
   - Ensures ServiceComb can discover the APM listener through dependency injection

3. **ApmBootListener**: APM core listener
   - Implements ServiceComb's `BootListener` interface
   - Subscribes to invocation-related events and collects performance metrics
   - Generates and outputs performance monitoring reports

4. **Output Generators**:
   - `HeaderOutputGenerator`: Generates request header information
   - `ConsumerOutputGenerator`: Generates consumer-side performance data
   - `ProducerOutputGenerator`: Generates provider-side performance data
   - `EdgeOutputGenerator`: Generates edge service performance data

### Technical Features

- **Non-intrusive**: Uses Java Agent technology, applications can integrate APM functionality without modifying any code
- **Transparent Integration**: Automatic registration through Spring extension mechanism, completely transparent to the application
- **Complete Call Chain**: Records timing data for each stage from request start to finish
- **Multi-role Support**: Simultaneously supports monitoring for Consumer, Producer, Edge, and other roles

## Usage

### 1. Add Dependencies

No dependencies need to be added to the microservice project using the APM Agent. The APM Agent is loaded as an independent JAR file through JVM parameters.

### 2. Build APM Agent

Execute in the `apm-agent` directory:

```bash
mvn clean install
```

After building, `apm-agent-3.0-SNAPSHOT.jar` will be generated in the `target` directory.

### 3. Load Agent on Application Startup

When starting a ServiceComb application, add the `-javaagent` JVM parameter:

```bash
java -javaagent:/path/to/apm-agent-3.0-SNAPSHOT.jar -jar your-application.jar
```

Or use with Maven:

```bash
mvn exec:java -Dexec.mainClass="your.main.Class" \
  -Dexec.args="-javaagent:/path/to/apm-agent-3.0-SNAPSHOT.jar"
```

In JetBrains IDEA, you can add VM options in the run configuration:

```bash
-javaagent:/path/to/apm-agent-3.0-SNAPSHOT.jar
```

### 4. View Monitoring Output

The APM Agent will output performance data for each request to the console in the following format:

```text
PROVIDER rest springmvchello.sayHi:
  http method: GET
  url        : /springmvchello/sayhi
  status code: 200
  traceId    : 696fe51656552b35
  total                    : 9.375ms
    prepare                : 2.156ms
    queue                  : 0.542ms
    provider-decode        : 0.321ms
    provider-encode        : 1.876ms
    execute                : 3.245ms
```

## Precondition

See [Precondition](../../README.md)

## Sample Quick Start

This sample uses ServiceComb's basic example project to demonstrate APM Agent functionality.

### Start the Service Center

- [How to start Service Center](http://servicecomb.apache.org/docs/products/service-center/install/)
- Make sure Service Center address is configured correctly in `microservice.yaml`

```yaml
servicecomb:
  service:
    registry:
      address: http://127.0.0.1:30100 #service center address
```

### Run Sample with APM Agent

Using other sample projects (such as `basic-application-sample`) as an example:

1. **Build the project**

   ```bash
   # Execute in the project root directory
   mvn clean install
   ```

2. **Start Provider service (with APM Agent)**

   ```bash
   cd java-chassis-samples/basic-application-sample/basic-provider/target
   java -javaagent:../../../apm-agent/target/apm-agent-3.0-SNAPSHOT.jar -jar basic-provider-3.0-SNAPSHOT.jar
   ```

3. **Start Consumer service (normal startup, without APM Agent)**

   ```bash
   cd java-chassis-samples/basic-application-sample/basic-consumer/target
   java -jar basic-consumer-3.0-SNAPSHOT.jar
   ```

### How to Verify

After both Provider and Consumer services start successfully, you can see the following in the Provider's console:

**Performance monitoring data**: Each service invocation will output detailed performance data, including:

- HTTP method and URL
- Response status code
- TraceId (for distributed tracing)
- Total time and time breakdown by stage

## Output Data Description

### Consumer Side Output

```text
CONSUMER rest provider.hello.sayHi:
  http method: GET
  url        : /hello/sayhi
  status code: 200
  traceId    : 696fe51656552b35
  total                    : 17.857ms
    prepare                : 5.231ms
    consumer-send          : 8.456ms
    consumer-decode        : 2.124ms
    consumer-after-decode  : 1.046ms
```

### Provider Side Output

```text
PROVIDER rest provider.hello.sayHi:
  http method: GET
  url        : /hello/sayhi
  status code: 200
  traceId    : 696fe51656552b35
  total                    : 9.375ms
    prepare                : 2.156ms
    queue                  : 0.542ms
    provider-decode        : 0.321ms
    provider-encode        : 1.876ms
    execute                : 3.245ms
```

## Extension Development

### Custom Output Format

You can customize the output format by modifying or extending implementations of `AbstractOutputGenerator`:

```java
public class CustomOutputGenerator extends AbstractOutputGenerator {
  @Override
  public void generate(StringBuilder sb, InvocationFinishEvent event) {
    // Custom output logic
  }
}
```

### Integration with External APM Systems

You can add code in the `ApmBootListener.onInvocationFinish` method to send performance data to external APM systems (such as Zipkin, SkyWalking, etc.):

```java
@Subscribe
public void onInvocationFinish(InvocationFinishEvent event) {
  // Create Span data
  Span span = createSpan(event);

  // Send to external APM system
  apmClient.sendSpan(span);
}
```

## Version Compatibility

### ServiceComb 3.3.0 Upgrade Notes

If you are upgrading from **ServiceComb 3.2.4 to 3.3.0**, please note the following for APM Agent:

**Root Cause**:

- ServiceComb 3.3.0 uses Spring Boot 3.4.5 (upgraded from Spring Boot 3.3.5 in 3.2.4)
- Spring Boot 3.4 completely removed support for loading `ApplicationContextInitializer` via system property `context.initializer.classes`
- Must use `META-INF/spring.factories` standard SPI mechanism

**Current Solution**:
This project has adopted a **dual-guarantee strategy** that supports both loading methods:

- System property approach (compatible with ServiceComb 3.2.4 and earlier versions)
- spring.factories approach (required for ServiceComb 3.3.0+)

**No additional configuration needed** - the Agent automatically supports both ServiceComb 3.2.4 and 3.3.0.

**Detailed Documentation**:

- [Root Cause Analysis](docs/版本升级根本原因分析.md) - Detailed technical analysis with official documentation evidence (Chinese)
- [Migration Guide](docs/升级到3.3.0问题解决.md) - Quick solution and steps (Chinese)

## Notes

1. **Performance Impact**: The APM Agent will have some impact on application performance. It is recommended to control the sampling rate in production environments
2. **Log Output**: The current implementation outputs directly to the console. For production environments, it is recommended to use asynchronous output to files or message queues
3. **Version Compatibility**: This sample is compatible with ServiceComb 3.2.4 and 3.3.0. Other versions may require adjustments
4. **JDK Version**: Supports JDK 8 and above

## References

- [ServiceComb Java Chassis Official Documentation](http://servicecomb.apache.org/docs/)
- [Java Agent Technical Documentation](https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html)
- [Project Implementation Details](docs/walkthrough.md)
