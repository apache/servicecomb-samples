package org.apache.servicecomb.authentication.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.Signer;

@Configuration
public class AuthenticationConfiguration {
  @Bean(name = "authSigner")
  public Signer authSigner() {
    return new MacSigner("Please change this key.");
  }
}
