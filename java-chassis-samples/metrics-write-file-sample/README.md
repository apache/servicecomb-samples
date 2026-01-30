# Metrics Write File Sample

## Description

This project demonstrates how to export ServiceComb metrics data to separate text files, enabling external monitoring systems to collect runtime performance data by reading these files. It implements a custom metrics publisher that writes metrics in CloudEye-compatible JSON format to dynamically named files.

## What is it for?

- **Non-intrusive Monitoring**: Automatically collects and exports ServiceComb metrics without modifying application code
- **External System Integration**: Provides a file-based interface for third-party monitoring tools (e.g., CloudEye, Prometheus exporters)
- **Flexible Output**: Each metric is written to a separate file with dynamic naming based on metric properties
- **Production Ready**: Uses industry-standard logging frameworks (Log4j2/Logback) for reliable file I/O

## How it Works

### Architecture Overview

```text
ServiceComb Application
  ↓
MeterRegistry (Micrometer)
  ↓ periodic polling
EventBus.post(PolledEvent)
  ↓
CloudEyeFilePublisher (@Subscribe)
  ↓ extract metrics
Log4j2 Routing Appender
  ↓ dynamic file routing via MDC
Individual .dat files
```

### Core Components

1. **CloudEyeFilePublisher**: Implements `MetricsInitializer` interface
   - Subscribes to `PolledEvent` from ServiceComb's EventBus
   - Receives metrics snapshots periodically (controlled by `servicecomb.metrics.window_time`)
   - Converts Micrometer meters to CloudEye JSON format
   - Uses MDC (Mapped Diagnostic Context) for dynamic file routing

2. **Log4j2 Routing Appender**: Dynamically creates log files
   - Routes logs to different files based on `fileName` MDC variable
   - Pattern: `${appId}.${serviceName}.${metricName}.dat`
   - Each metric gets its own file for easy parsing by external systems

3. **Spring Boot AutoConfiguration**: Zero-configuration integration
   - Automatically registers `CloudEyeFilePublisher` as a Spring Bean
   - Supports conditional loading via `servicecomb.metrics.write-file.enabled`
   - Leverages Spring dependency injection for clean architecture

### Output Format

Each `.dat` file contains JSON lines in CloudEye format:

```json
{
  "plugin_id": "myapp.myservice",
  "metric": {
    "node": "hostname",
    "timestamp": 1706025600000,
    "dynamicValue": { "metric.name": 123.45 }
  }
}
```

## Changes from ServiceComb 2.x to 3.x

ServiceComb 3.x migrated from **Netflix Spectator** to **Micrometer** as its metrics API. This module has been adapted accordingly:

### API Changes

| Aspect              | ServiceComb 2.x            | ServiceComb 3.x           | Status      |
| ------------------- | -------------------------- | ------------------------- | ----------- |
| Metrics Library     | Netflix Spectator          | Micrometer                | **Changed** |
| Registry Type       | `GlobalRegistry`           | `MeterRegistry`           | **Changed** |
| Meter API           | `meter.measure()` iterator | `meter.measure()` forEach | **Changed** |
| Extension Interface | `MetricsInitializer`       | `MetricsInitializer`      | **Kept**    |
| Event Subscription  | EventBus `@Subscribe`      | EventBus `@Subscribe`     | **Kept**    |
| Event Type          | `PolledEvent`              | `PolledEvent`             | **Kept**    |

### Architecture Evolution

| Aspect               | ServiceComb 2.x              | ServiceComb 3.x               | Status      |
| -------------------- | ---------------------------- | ----------------------------- | ----------- |
| Loading Mechanism    | Java SPI (`ServiceLoader`)   | Spring Boot AutoConfiguration | **Changed** |
| Registration File    | `META-INF/services/...`      | `META-INF/spring/...imports`  | **Changed** |
| Dependency Injection | Manual `BeanUtils.getBean()` | Constructor injection         | **Changed** |
| Conditional Loading  | Not supported                | `@ConditionalOnProperty`      | **Added**   |
| Spring Integration   | Separate from Spring context | Native Spring Bean            | **Changed** |

### Key Improvements in 3.x

✅ **Better Spring Integration**: `CloudEyeFilePublisher` is now a native Spring Bean with constructor injection  
✅ **Conditional Loading**: Can be disabled via configuration property  
✅ **Cleaner Code**: No need for `BeanUtils.getBean()` hacks  
✅ **Standard Mechanism**: Uses Spring Boot's AutoConfiguration instead of Java SPI

## Usage

### 1. Add Dependency

Add the following dependency to your ServiceComb application's `pom.xml`:

```xml
<dependency>
  <groupId>org.apache.servicecomb.samples</groupId>
  <artifactId>metrics-write-file</artifactId>
  <version>3.0-SNAPSHOT</version>
</dependency>
```

### 2. Configure Application

In your `application.yml`, enable metrics collection:

```yaml
servicecomb:
  service:
    application: myapp
    name: myservice
  metrics:
    window_time: 60000 # metrics collection interval in milliseconds, has default value
```

**Optional**: Disable this feature if needed:

```yaml
servicecomb:
  metrics:
    write-file:
      enabled: false # Default is true
```

### 3. Configure Log4j2

Add the Routing Appender to your application's `log4j2.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="INFO">
  <Appenders>
    <Console name="stdout" target="SYSTEM_OUT">
      <PatternLayout pattern="%d %-5level %c{1} : %msg%n"/>
    </Console>

    <!-- Metrics file routing appender -->
    <Routing name="cloudEyeRouting">
      <Routes pattern="$${ctx:fileName}">
        <Route>
          <File name="cloudEyeAppender-${ctx:fileName}" append="true"
            fileName="${sys:cloudEye.logDir}/${ctx:fileName}">
            <PatternLayout>
              <Pattern>%msg%n</Pattern>
            </PatternLayout>
          </File>
        </Route>
      </Routes>
    </Routing>
  </Appenders>

  <Loggers>
    <Root level="info">
      <AppenderRef ref="stdout"/>
    </Root>

    <!-- CloudEye logger: write metrics to separate files -->
    <Logger name="cloudEyeLogger" level="info" additivity="false">
      <AppenderRef ref="cloudEyeRouting"/>
    </Logger>
  </Loggers>
</Configuration>
```

**Note**: Reference `sample-log4j2.xml` in the `metrics-write-file` module for a complete example.

### 4. Set Log Directory (Optional)

By default, metrics files are written to `logs/` directory. You can customize it:

```bash
java -DcloudEye.logDir=/custom/path/to/metrics -jar your-application.jar
```

Or in `application.yml`:

```yaml
cloudEye:
  logDir: /custom/path/to/metrics
```

## Testing

Since this is a library module, it needs to be tested with an actual ServiceComb application.

### Method 1: Quick Test with Sample Project

Use the `metrics-extend-healthcheck` sample:

```bash
# 1. Build and install the metrics-write-file module
cd java-chassis-samples/metrics-write-file-sample
mvn clean install

# 2. The dependency is already configured in metrics-extend-healthcheck
cd ../metrics-extend-healthcheck

# 3. Make sure log4j2.xml includes the cloudEyeRouting appender (already configured)

# 4. Run the application
mvn spring-boot:run
```

### Method 2: Integration Test

1. Add dependency to your test project
2. Ensure Service Center is running: `http://127.0.0.1:30100`
3. Configure `application.yml` with `servicecomb.metrics.window_time`
4. Start your application
5. Trigger some requests to generate metrics

## How to Verify

### 1. Check Startup Logs

You should see the following log on application startup:

```
INFO  CloudEyeFilePublisher : CloudEyeFilePublisher init() called
INFO  CloudEyeFilePublisher : CloudEyeFilePublisher filePrefix set to: myapp.myservice
```

### 2. Check Periodic Metrics Collection

Every 60 seconds (or your configured `window_time`), you should see:

```
INFO  CloudEyeFilePublisher : CloudEyeFilePublisher onPolledEvent() called, meters count: 45
```

### 3. Verify Metrics Files

Check the `logs/` directory (or your custom `cloudEye.logDir`):

```bash
ls -l logs/

# Expected output (sample):
myapp.myservice.jvm.memory.used.area.heap.id.PS-Eden-Space.dat
myapp.myservice.jvm.threads.live.dat
myapp.myservice.servicecomb.invocation.count.operation.sayHello.dat
```

### 4. Inspect File Content

```bash
cat logs/myapp.myservice.jvm.memory.used.area.heap.dat

# Expected JSON output:
{"plugin_id":"myapp.myservice","metric":{"node":"hostname","timestamp":1706025600000,"dynamicValue":{"jvm.memory.used.area.heap":134217728.0}}}
```

### 5. Trigger Metrics Collection

If no files appear, trigger some service invocations:

```bash
# Example: call a REST endpoint
curl http://localhost:7777/hello

# Or health check endpoint
curl http://localhost:7777/management/health
```

Wait for the next metrics collection cycle (60 seconds by default), and files should appear.

## File Naming Convention

Metrics files follow the pattern: `{appId}.{serviceName}.{metricName}.{tags}.dat`

Examples:

- `myapp.myservice.jvm.memory.used.area.heap.id.PS-Eden-Space.dat`
- `myapp.myservice.servicecomb.invocation.count.operation.sayHello.role.CONSUMER.dat`
- `myapp.myservice.jvm.threads.live.dat`

## Troubleshooting

### No metrics files generated

1. **Check log4j2.xml**: Must include the `cloudEyeRouting` appender

2. **Check feature enabled**: Verify not disabled in config

   ```yaml
   servicecomb:
     metrics:
       write-file:
         enabled: true # Default is true
   ```

3. **Check startup logs**: Should see "CloudEyeFilePublisher init() called"

### Files created but empty

1. **Trigger some traffic**: Make service calls to generate metrics
2. **Wait for collection cycle**: Default is 60 seconds
3. **Check EventBus logs**: Should see "onPolledEvent() called"

### Permission denied errors

1. **Check directory permissions**: Ensure write access to `logs/` directory
2. **Use absolute path**: Set `cloudEye.logDir` to an absolute path
3. **Run with proper user**: Ensure application user has write permissions

## Precondition

See [Precondition](../../README.md)

## References

- [ServiceComb Metrics Documentation](https://servicecomb.apache.org/references/java-chassis/en_US/general-development/metrics/)
- [Micrometer Documentation](https://micrometer.io/docs)
- [Log4j2 Routing Appender](https://logging.apache.org/log4j/2.x/manual/appenders.html#RoutingAppender)
