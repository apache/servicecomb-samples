package org.apache.servicecomb.authentication.resource;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.authentication.jwt.JWTClaims;
import org.apache.servicecomb.authentication.jwt.JsonParser;
import org.apache.servicecomb.authentication.util.Constants;
import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

public class ResourceAuthHandler implements Handler {

  @Override
  public void handle(Invocation invocation, AsyncResponse asyncResponse) throws Exception {
    AccessConfiguration config = AccessConfigurationManager.getAccessConfiguration(invocation);

    // by pass authentication
    if (!config.needAuth) {
      invocation.next(asyncResponse);
      return;
    }

    String token = invocation.getContext(Constants.CONTEXT_HEADER_AUTHORIZATION);
    if (token == null) {
      asyncResponse.consumerFail(new InvocationException(403, "forbidden", "not authenticated"));
      return;
    }
    // verify tokens
    Jwt jwt = JwtHelper.decode(token);
    JWTClaims claims;
    try {
      jwt.verifySignature(BeanUtils.getBean("authSigner"));
      claims = JsonParser.parse(jwt.getClaims(), JWTClaims.class);
      // TODO: verify claims.
    } catch (Exception e) {
      asyncResponse.consumerFail(new InvocationException(403, "forbidden", "not authenticated"));
      return;
    }

    // check roles
    if (!StringUtils.isEmpty(config.roles)) {
      String[] roles = config.roles.split(",");
      if (roles.length > 0) {
        boolean valid = false;
        Set<String> authorities = claims.getAuthorities();
        for (String role : roles) {
          if (authorities.contains(role)) {
            valid = true;
            break;
          }
        }
        if (!valid) {
          asyncResponse.consumerFail(new InvocationException(403, "forbidden", "not authenticated"));
          return;
        }
      }
    }

    // pre method authentiation
    invocation.addLocalContext(Constants.CONTEXT_HEADER_CLAIMS, jwt.getClaims());
    invocation.next(asyncResponse);
  }

}
