package org.apache.servicecomb.authentication.gateway;

import org.apache.servicecomb.authentication.util.Constants;
import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.swagger.invocation.Response;

public class AuthenticationFilter implements HttpServerFilter {

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx requestEx) {
    String authentication = requestEx.getHeader(Constants.HTTP_HEADER_AUTHORIZATION);
    if (authentication != null) {
      String[] tokens = authentication.split(" ");
      if (tokens.length == 2) {
        if (tokens[0].equals("Bearer")) {
          invocation.addContext(Constants.CONTEXT_HEADER_AUTHORIZATION, tokens[1]);
        }
      }
    }
    return null;
  }

}
