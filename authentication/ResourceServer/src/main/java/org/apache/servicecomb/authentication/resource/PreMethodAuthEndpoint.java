package org.apache.servicecomb.authentication.resource;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "PreMethodAuthEndpoint")
@RequestMapping(path = "/v1/auth/method")
public class PreMethodAuthEndpoint {
  @PostMapping(path = "/adminSayHello")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminSayHello(String name) {
    return name;
  }

  @PostMapping(path = "/guestSayHello")
  @PreAuthorize("hasRole('USER')")
  public String guestSayHello(String name) {
    return name;
  }

  @PostMapping(path = "/guestOrAdminSayHello")
  @PreAuthorize("hasRole('USER,ADMIN')")
  public String guestOrAdminSayHello(String name) {
    return name;
  }

  @PostMapping(path = "/everyoneSayHello")
  public String everyoneSayHello(String name) {
    return name;
  }
}
