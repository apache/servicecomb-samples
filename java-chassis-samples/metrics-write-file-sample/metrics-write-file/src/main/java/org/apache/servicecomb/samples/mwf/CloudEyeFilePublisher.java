/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.samples.mwf;

import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.foundation.common.net.NetUtils;
import org.apache.servicecomb.foundation.common.utils.JsonUtils;
import org.apache.servicecomb.foundation.metrics.MetricsBootstrapConfig;
import org.apache.servicecomb.foundation.metrics.MetricsInitializer;
import org.apache.servicecomb.foundation.metrics.PolledEvent;
import org.apache.servicecomb.registry.RegistrationManager;
import org.apache.servicecomb.registry.api.RegistrationInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

/**
 * a sample for cloud eye
 *
 * related product only used logback or log4j2
 * both logback and log4j2 support write to different log file depend on different MDC value
 *
 * reference sample-logback.xml and sample-log4j2.xml
 */
public class CloudEyeFilePublisher implements MetricsInitializer {
  private static final Logger LOGGER = LoggerFactory.getLogger(CloudEyeFilePublisher.class);

  private static final Logger CLOUD_EYE_LOGGER = LoggerFactory.getLogger("cloudEyeLogger");

  private final RegistrationManager registrationManager;

  private String filePrefix;

  private String hostName;

  public CloudEyeFilePublisher(RegistrationManager registrationManager) {
    this.registrationManager = registrationManager;
  }

  @Override
  public void init(MeterRegistry meterRegistry, EventBus eventBus, MetricsBootstrapConfig config) {
    LOGGER.info("CloudEyeFilePublisher init() called");
    eventBus.register(this);

    RegistrationInstance microserviceInstance = registrationManager.getPrimaryRegistration().getMicroserviceInstance();
    filePrefix = microserviceInstance.getApplication() + "." + microserviceInstance.getServiceName();
    LOGGER.info("CloudEyeFilePublisher filePrefix set to: {}", filePrefix);
//    // Use system properties or config to get appId and serviceName
//    String appId = System.getProperty("APPLICATION_ID", "defaultApp");
//    String serviceName = System.getProperty("service.name", "defaultService");
//    filePrefix = appId + "." + serviceName;

    hostName = NetUtils.getHostName();
    if (StringUtils.isEmpty(hostName)) {
      hostName = NetUtils.getHostAddress();
    }

    // Set default log directory if not already configured
    System.setProperty("cloudEye.logDir", 
        System.getProperty("cloudEye.logDir", "logs/metrics-write-file"));
  }

  @Subscribe
  public void onPolledEvent(PolledEvent event) {
    LOGGER.info("CloudEyeFilePublisher onPolledEvent() called, meters count: {}", event.getMeters().size());
    long now = System.currentTimeMillis();
    for (Meter meter : event.getMeters()) {
      meter.measure().forEach(measurement -> {
        logMeasurement(meter, measurement.getValue(), now);
      });
    }
  }

  protected void logMeasurement(Meter meter, double value, long now) {
    String metricKey = generateMetricKey(meter);
    String fileName = filePrefix + "." + metricKey + ".dat";
    
    LOGGER.debug("Writing metric to file: {}, value: {}", fileName, value);
    MDC.put("fileName", fileName);

    CloudEyeMetricModel metricModel = new CloudEyeMetricModel();
    metricModel.setNode(hostName);
    metricModel.setTimestamp(now);
    metricModel.getDynamicValue().put(metricKey, String.valueOf(value));

    CloudEyeModel model = new CloudEyeModel();
    model.setPlugin_id(filePrefix);
    model.setMetric(metricModel);

    try {
      CLOUD_EYE_LOGGER.info(JsonUtils.writeValueAsString(model));
    } catch (JsonProcessingException e) {
      LOGGER.error("Failed to write cloud eye log.", e);
    } finally {
      MDC.remove("fileName");
    }
  }

  protected String generateMetricKey(Meter meter) {
    StringBuilder sb = new StringBuilder();
    sb.append(meter.getId().getName());
    for (Tag tag : meter.getId().getTags()) {
      sb.append('.').append(tag.getValue());
    }
    return sb.toString();
  }
}
