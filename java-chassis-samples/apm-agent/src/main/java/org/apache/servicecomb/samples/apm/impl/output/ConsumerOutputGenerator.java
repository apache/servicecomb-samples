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

import org.apache.servicecomb.core.event.InvocationFinishEvent;
import org.apache.servicecomb.core.invocation.InvocationStageTrace;

public class ConsumerOutputGenerator extends AbstractOutputGenerator {
  @Override
  public void generate(StringBuilder sb, InvocationFinishEvent event) {
    InvocationStageTrace stageTrace = event.getInvocation().getInvocationStageTrace();

    // 1. total: start create invocation -> all filters finished
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_TOTAL, stageTrace.calcTotal());
    // 2. prepare: start create invocation -> finish create invocation
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_PREPARE, stageTrace.calcPrepare());
    // 3. connection: start get connection -> finish get connection
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_CONSUMER_CONNECTION, stageTrace.calcConnection());
    // 4. consumer-encode: start encode request -> finish encode request
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_CONSUMER_ENCODE_REQUEST,
            stageTrace.calcConsumerEncodeRequest());
    // 5. consumer-send: start send request -> finish send request
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_CONSUMER_SEND, stageTrace.calcConsumerSendRequest());
    // 6. wait: finish send request -> start decode response
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_CONSUMER_WAIT, stageTrace.calcWait());
    // 7. consumer-decode: start decode response -> finish decode response
    appendTimeLine(sb, PAD4_TIME_FMT, InvocationStageTrace.STAGE_CONSUMER_DECODE_RESPONSE,
            stageTrace.calcConsumerDecodeResponse());
  }
}
