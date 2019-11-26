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

package org.apache.servicecomb.samples.practise.houserush.gateway.filter;

import org.apache.servicecomb.common.rest.RestConst;
import org.apache.servicecomb.common.rest.codec.produce.ProduceProcessor;
import org.apache.servicecomb.common.rest.codec.produce.ProduceProcessorManager;
import org.apache.servicecomb.common.rest.definition.RestOperationMeta;
import org.apache.servicecomb.common.rest.filter.HttpClientFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.definition.OperationMeta;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.HttpServletResponseEx;
import org.apache.servicecomb.foundation.vertx.http.VertxClientRequestToHttpServletRequest;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.context.HttpStatus;
import org.apache.servicecomb.swagger.invocation.exception.CommonExceptionData;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.apache.servicecomb.swagger.invocation.response.ResponseMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.HttpHeaders;
import java.util.Collection;

public class CustomClientFilter implements HttpClientFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomClientFilter.class);

  @Override
  public int getOrder() {
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean enabled() {
    return true;
  }

  @Override
  public void beforeSendRequest(Invocation invocation, HttpServletRequestEx requestEx) {
    String customerId = invocation.getContext("customerId");
    if (customerId != null) {
      requestEx.setHeader("customerId", customerId);
    }

  }

  protected ProduceProcessor findProduceProcessor(RestOperationMeta restOperation,
                                                  HttpServletResponseEx responseEx) {
    String contentType = responseEx.getHeader(HttpHeaders.CONTENT_TYPE);
    if (contentType == null) {
      return null;
    }

    String contentTypeForFind = contentType;
    int idx = contentType.indexOf(';');
    if (idx != -1) {
      contentTypeForFind = contentType.substring(0, idx);
    }
    return restOperation.findProduceProcessor(contentTypeForFind);
  }

  protected Response extractResponse(Invocation invocation, HttpServletResponseEx responseEx) {
    Object result = invocation.getHandlerContext().get(RestConst.READ_STREAM_PART);
    if (result != null) {
      return Response.create(responseEx.getStatusType(), result);
    }

    OperationMeta operationMeta = invocation.getOperationMeta();
    ResponseMeta responseMeta = operationMeta.findResponseMeta(responseEx.getStatus());
    RestOperationMeta swaggerRestOperation = operationMeta.getExtData(RestConst.SWAGGER_REST_OPERATION);
    ProduceProcessor produceProcessor = findProduceProcessor(swaggerRestOperation, responseEx);
    if (produceProcessor == null) {
      // This happens outside the runtime such as Servlet filter response. Here we give a default json parser to it
      // and keep user data not get lose.
      String msg =
          String.format("method %s, path %s, statusCode %d, reasonPhrase %s, response content-type %s is not supported",
              swaggerRestOperation.getHttpMethod(),
              swaggerRestOperation.getAbsolutePath(),
              responseEx.getStatus(),
              responseEx.getStatusType().getReasonPhrase(),
              responseEx.getHeader(HttpHeaders.CONTENT_TYPE));
      LOGGER.warn(msg);
      produceProcessor = ProduceProcessorManager.DEFAULT_PROCESSOR;
    }

    try {
      result = produceProcessor.decodeResponse(responseEx.getBodyBuffer(), responseMeta.getJavaType());
      return Response.create(responseEx.getStatusType(), result);
    } catch (Exception e) {
      LOGGER.error("failed to decode response body, exception is [{}]", e.getMessage());
      String msg =
          String.format("method %s, path %s, statusCode %d, reasonPhrase %s, response content-type %s is not supported",
              swaggerRestOperation.getHttpMethod(),
              swaggerRestOperation.getAbsolutePath(),
              responseEx.getStatus(),
              responseEx.getStatusType().getReasonPhrase(),
              responseEx.getHeader(HttpHeaders.CONTENT_TYPE));
      if (HttpStatus.isSuccess(responseEx.getStatus())) {
        return Response.createConsumerFail(
            new InvocationException(org.apache.http.HttpStatus.SC_BAD_REQUEST, responseEx.getStatusType().getReasonPhrase(),
                new CommonExceptionData(msg), e));
      }
      return Response.createConsumerFail(
          new InvocationException(responseEx.getStatus(), responseEx.getStatusType().getReasonPhrase(),
              new CommonExceptionData(msg), e));
    }
  }

  @Override
  public Response afterReceiveResponse(Invocation invocation, HttpServletResponseEx responseEx) {
    Response response = extractResponse(invocation, responseEx);

    for (String headerName : responseEx.getHeaderNames()) {
      if (headerName.equals(":status")) {
        continue;
      }
      Collection<String> headerValues = responseEx.getHeaders(headerName);
      for (String headerValue : headerValues) {
        response.getHeaders().addHeader(headerName, headerValue);
      }
    }
    //add refresh token
    String newAuthorization = invocation.getContext("newAuthorization");
    if (newAuthorization != null) {
      response.getHeaders().addHeader("newAuthorization", newAuthorization);
    }
    return response;
  }
}
