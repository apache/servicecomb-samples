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
package org.apache.servicecomb.samples.apm.impl.output;

import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.event.InvocationFinishEvent;
import org.apache.servicecomb.core.invocation.InvocationStageTrace;

public class ProducerOutputGenerator extends AbstractOutputGenerator {
  @Override
  public void generate(StringBuilder sb, InvocationFinishEvent event) {
    Invocation invocation = event.getInvocation();
    InvocationStageTrace stageTrace = invocation.getInvocationStageTrace();

    // 1. total: start create invocation -> all filters finished
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_TOTAL, stageTrace.calcTotal());
    // 2. prepare: start create invocation -> finish create invocation
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_PREPARE, stageTrace.calcPrepare());
    // 4. queue: add in queue -> execute in thread
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_PROVIDER_QUEUE, stageTrace.calcQueue());
    // 5. provider-decode: start decode request -> finish decode request
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_PROVIDER_DECODE_REQUEST,
            stageTrace.calcProviderDecodeRequest());
    // 6. provider-encode: start encode response -> finish encode response
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_PROVIDER_ENCODE_RESPONSE,
            stageTrace.calcProviderEncodeResponse());
    // 7. provider-send: start send response -> finish send response
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_PROVIDER_ENCODE_RESPONSE,
            stageTrace.calcProviderEncodeResponse());
    // 8. execute: start business execute -> finish business execute
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_PROVIDER_BUSINESS, stageTrace.calcBusinessExecute());
  }
}
