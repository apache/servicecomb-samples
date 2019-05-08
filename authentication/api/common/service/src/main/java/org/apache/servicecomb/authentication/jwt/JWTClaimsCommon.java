package org.apache.servicecomb.authentication.jwt;

public class JWTClaimsCommon {
  // see: https://tools.ietf.org/html/rfc7519
  // (Issuer) Claim
  protected String iss;

  // (Subject) Claim
  protected String sub;

  // (Audience) Claim
  protected String aud;

  // (Expiration Time) Claim
  protected long exp;

  // (Not Before) Claim
  protected long nbf;

  // (Issued At) Claim
  protected long iat;

  // (JWT ID) Claim
  protected String jti;

  public String getIss() {
    return iss;
  }

  public void setIss(String iss) {
    this.iss = iss;
  }

  public String getSub() {
    return sub;
  }

  public void setSub(String sub) {
    this.sub = sub;
  }

  public String getAud() {
    return aud;
  }

  public void setAud(String aud) {
    this.aud = aud;
  }

  public long getExp() {
    return exp;
  }

  public void setExp(long exp) {
    this.exp = exp;
  }

  public long getNbf() {
    return nbf;
  }

  public void setNbf(long nbf) {
    this.nbf = nbf;
  }

  public long getIat() {
    return iat;
  }

  public void setIat(long iat) {
    this.iat = iat;
  }

  public String getJti() {
    return jti;
  }

  public void setJti(String jti) {
    this.jti = jti;
  }


}
