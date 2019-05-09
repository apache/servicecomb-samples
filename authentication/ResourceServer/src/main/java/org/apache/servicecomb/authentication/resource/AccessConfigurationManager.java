package org.apache.servicecomb.authentication.resource;

import java.util.Map;

import org.apache.servicecomb.config.inject.ConfigObjectFactory;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.common.concurrent.ConcurrentHashMapEx;

public class AccessConfigurationManager {
  private static final Map<String, AccessConfiguration> CONFIGURATIONS = new ConcurrentHashMapEx<>();

  private static final ConfigObjectFactory FACTORY = new ConfigObjectFactory();

  public static AccessConfiguration getAccessConfiguration(Invocation invocation) {
    return CONFIGURATIONS.computeIfAbsent(invocation.getOperationMeta().getSchemaQualifiedName(), key -> {
      return FACTORY.create(AccessConfiguration.class, "schemaId", invocation.getSchemaId(), "operationId", invocation.getOperationName());
    });
  }
}
