package org.apache.servicecomb.authentication.test;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;


public class PatternTest {
  @Test
  public void testPattern() {
    String regex = "(/v1/log|/inspector|/v1/auth)/(.*)";
    Pattern p = Pattern.compile(regex);
    Assert.assertTrue(p.matcher("/v1/log/login").matches());
    Assert.assertTrue(p.matcher("/inspector/login").matches());
    Assert.assertTrue(p.matcher("/v1/auth/login").matches());
    Assert.assertTrue(!p.matcher("/api/v1/auth/login").matches());
  }
}
