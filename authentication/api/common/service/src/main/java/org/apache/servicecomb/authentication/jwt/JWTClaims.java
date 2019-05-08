package org.apache.servicecomb.authentication.jwt;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class JWTClaims extends JWTClaimsCommon {
  protected Set<String> roles = Collections.emptySet();

  protected Map<String, Object> additionalInformation = Collections.emptyMap();

  /**
   * The scope of the access token as described by <a
   * href="http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-3.3">Section 3.3</a>
   */
  protected String scope;

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public Map<String, Object> getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(Map<String, Object> additionalInformation) {
    this.additionalInformation = additionalInformation;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public void addRole(String role) {
    this.roles.add(role);
  }
}
