package org.apache.servicecomb.authentication.jwt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JWTClaims extends JWTClaimsCommon {
  protected Set<String> authorities;

  protected Map<String, Object> additionalInformation;

  /**
   * The scope of the access token as described by <a
   * href="http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-3.3">Section 3.3</a>
   */
  protected Set<String> scope;

  public Set<String> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<String> authorities) {
    this.authorities = authorities;
  }

  public Map<String, Object> getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(Map<String, Object> additionalInformation) {
    this.additionalInformation = additionalInformation;
  }

  public Set<String> getScope() {
    return scope;
  }

  public void setScope(Set<String> scope) {
    this.scope = scope;
  }

  public void addAdditionalInformation(String key, Object value) {
    if (this.additionalInformation == null) {
      this.additionalInformation = new HashMap<>();
    }
    this.additionalInformation.put(key, value);
  }

  public void addScope(String operation) {
    if (this.scope == null) {
      this.scope = new HashSet<>();
    }
    this.scope.add(operation);
  }

  public void addAuthority(String authority) {
    if (this.authorities == null) {
      this.authorities = new HashSet<>();
    }
    this.authorities.add(authority);
  }
}
