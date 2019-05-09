package org.apache.servicecomb.authentication.resource;

import org.apache.servicecomb.config.inject.InjectProperties;
import org.apache.servicecomb.config.inject.InjectProperty;

@InjectProperties(prefix = "servicecomb.authencation.access")
public class AccessConfiguration {
  @InjectProperty(keys = {
      "needAuth.${schemaId}.${operationId}",
      "needAuth.${schemaId}",
      "needAuth"},
      defaultValue = "true")
  public boolean needAuth;

  @InjectProperty(keys = {
      "roles.${schemaId}.${operationId}",
      "roles.${schemaId}",
      "roles"})
  public String roles;
}
