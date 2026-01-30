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
package org.apache.servicecomb.samples.apm;

import java.lang.instrument.Instrumentation;

public class AgentMain {
  public static void premain(String args, Instrumentation inst) {
    // Backward compatibility: Set system properties for older Spring Boot versions
    // - ServiceComb 3.2.4 (Spring Boot 3.3.5): System property loading still works (deprecated but functional)
    // - ServiceComb 3.3.0+ (Spring Boot 3.4.5): System property REMOVED, requires META-INF/spring.factories
    // Spring Boot 3.2 deprecated context.initializer.classes, Spring Boot 3.4 removed it completely
    // Keeping both approaches ensures maximum compatibility across versions
    String apmInitializer = "org.apache.servicecomb.samples.apm.ApmBeanRegistrar";
    String existingInitializers = System.getProperty("context.initializer.classes", "");
    
    if (existingInitializers.isEmpty()) {
      System.setProperty("context.initializer.classes", apmInitializer);
    } else {
      System.setProperty("context.initializer.classes", existingInitializers + "," + apmInitializer);
    }

    // JDK 9+ compatibility: Append agent jar to system classloader search path
    // This ensures agent classes are visible to the application classloader
    try {
      java.util.jar.JarFile jarFile = new java.util.jar.JarFile(
          new java.io.File(AgentMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
      inst.appendToSystemClassLoaderSearch(jarFile);
    } catch (Exception e) {
      System.err.println("[APM-Agent] Warning: Failed to append agent jar to system classloader");
      e.printStackTrace();
    }

    // Web container compatibility: Add class transformer to inject agent jar into web container classloaders
    // This handles cases where the application uses a custom classloader (e.g., Tomcat, Jetty)
    inst.addTransformer(
        new SCBClassFileTransformer(AgentMain.class.getProtectionDomain().getCodeSource().getLocation()));
  }
}
