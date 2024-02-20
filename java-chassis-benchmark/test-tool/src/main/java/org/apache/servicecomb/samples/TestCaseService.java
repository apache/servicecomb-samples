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

package org.apache.servicecomb.samples;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.distribution.CountAtBucket;
import io.micrometer.core.instrument.distribution.HistogramSnapshot;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@Component
public class TestCaseService {
  @RpcReference(schemaId = "ProviderController", microserviceName = "gateway")
  private ProviderService providerService;

  private AtomicLong success = new AtomicLong(0);

  private AtomicLong error = new AtomicLong(0);

  private AtomicLong totalTime = new AtomicLong(0);

  private static MeterRegistry registry = null;

  private static DistributionSummary summary = null;

  public TestCaseService() {
    init();
  }

  protected void init() {
    registerMetrics();
  }

  private void registerMetrics() {
    if (registry != null) {
      registry.close();
    }
    registry = new SimpleMeterRegistry();
    summary = DistributionSummary
        .builder("response.time")
        .description("response.time")
        .tags("response.time", "test") // optional
        .distributionStatisticExpiry(Duration.ofMinutes(10))
        .serviceLevelObjectives(10D, 20D, 50D, 100D, 200D, 500D, 1000D, 1000000D)
        .register(registry);
  }

  public void run(int threadCount, int countPerThread, int wait, int dataSize) throws Exception {
    System.out.println(String.format("Preparing run ==== thread-count:%s times-per-thread:%s wait:%s size:%s====", threadCount,
        countPerThread, wait, dataSize));
    DataModel dataModel = DataModel.create("test-case", dataSize);

    ExecutorService executor = Executors.newFixedThreadPool(threadCount, new ThreadFactory() {
      AtomicLong count = new AtomicLong(0);

      @Override
      public Thread newThread(Runnable r) {
        return new Thread(r, "test-thread" + count.getAndIncrement());
      }
    });
    CountDownLatch initLatch = new CountDownLatch(threadCount);
    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        initLatch.countDown();
      });
    }
    initLatch.await();

    success.set(0);
    error.set(0);
    totalTime.set(0);
    registerMetrics();

    // run
    System.out.println("Starting run =======================================");

    CountDownLatch latch = new CountDownLatch(threadCount * countPerThread);
    long begin = System.currentTimeMillis();
    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        for (int j = 0; j < countPerThread; j++) {
          long b = System.currentTimeMillis();
          try {
            DataModel result = providerService.sayHello(wait, dataModel);
            if (result.getData().size() != dataSize) {
              error.incrementAndGet();
            } else {
              success.incrementAndGet();
            }
          } catch (Exception e) {
            error.incrementAndGet();
          }

          totalTime.addAndGet(System.currentTimeMillis() - b);
          summary.record(System.currentTimeMillis() - b);
          latch.countDown();
        }
      });
    }

    latch.await();

    System.out.println("Total time:" + (System.currentTimeMillis() - begin));
    System.out.println("Success count:" + success.get());
    System.out.println("Error count:" + error.get());
    System.out.println("Success average Latency:" + totalTime.get() / (success.get() + error.get()));

    List<Meter> meters = registry.getMeters();

    for (Meter meter : meters) {
      if ("response.time".equals(meter.getId().getName())) {
        DistributionSummary distributionSummary = (DistributionSummary) meter;
        HistogramSnapshot histogramSnapshot = distributionSummary.takeSnapshot();
        CountAtBucket[] countAtBuckets = histogramSnapshot.histogramCounts();
        System.out.println(histogramSnapshot);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < countAtBuckets.length; i++) {
          if (i == 0) {
            sb.append("|(0, " + Double.valueOf(countAtBuckets[i].bucket()).intValue()
                + ")" + Double.valueOf(countAtBuckets[i].count()).intValue());
            continue;
          }
          sb.append("|(" + Double.valueOf(countAtBuckets[i - 1].bucket()).intValue() + "," +
              Double.valueOf(countAtBuckets[i].bucket()).intValue() +
              ")" + Double.valueOf(countAtBuckets[i].count() - countAtBuckets[i - 1].count()).intValue() + "|");
        }
        System.out.println(sb);
      }
    }

    executor.shutdownNow();
  }
}
