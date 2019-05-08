package org.apache.servicecomb.authentication.jwt;

public class JWTHeader {
  private String typ;

  private String alg;

  public String getTyp() {
    return typ;
  }

  public void setTyp(String typ) {
    this.typ = typ;
  }

  public String getAlg() {
    return alg;
  }

  public void setAlg(String alg) {
    this.alg = alg;
  }


}
