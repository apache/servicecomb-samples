package org.apache.servicecomb.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfiguration {
  @Bean(name = "authPasswordEncoder")
  private PasswordEncoder authPasswordEncoder() {
    return new PasswordEncoder() {

      @Override
      public String encode(CharSequence rawPassword) {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // TODO Auto-generated method stub
        return false;
      }
      
    };
  }
  
}
