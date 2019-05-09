package org.apache.servicecomb.authentication.resource;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "HandlerAuthEndpoint")
@RequestMapping(path = "/v1/auth/handler")
public class HandlerAuthEndpoint {
  @PostMapping(path = "/adminSayHello")
  public String adminSayHello(String name) {
    return name;
  }

  @PostMapping(path = "/guestSayHello")
  public String guestSayHello(String name) {
    return name;
  }

  @PostMapping(path = "/guestOrAdminSayHello")
  public String guestOrAdminSayHello(String name) {
    return name;
  }

  @PostMapping(path = "/everyoneSayHello")
  public String everyoneSayHello(String name) {
    return name;
  }
}
