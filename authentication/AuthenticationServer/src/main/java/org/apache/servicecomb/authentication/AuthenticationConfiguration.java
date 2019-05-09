package org.apache.servicecomb.authentication;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AuthenticationConfiguration {
  @Autowired
  @Qualifier("authPasswordEncoder")
  private PasswordEncoder passwordEncoder;

  @Bean(name = "authPasswordEncoder")
  public PasswordEncoder authPasswordEncoder() {
    return new Pbkdf2PasswordEncoder();
  }

  @Bean(name = "authSigner")
  public Signer authSigner() {
    return new MacSigner("Please change this key.");
  }

  @Bean(name = "authUserDetailsService")
  public UserDetailsService authUserDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    UserDetails uAdmin = new User("admin", passwordEncoder.encode("changeMyPassword"),
        Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
    UserDetails uGuest = new User("guest", passwordEncoder.encode("changeMyPassword"),
        Arrays.asList(new SimpleGrantedAuthority("GUEST")));
    manager.createUser(uAdmin);
    manager.createUser(uGuest);
    return manager;
  }
}
