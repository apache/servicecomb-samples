package org.apache.servicecomb.authentication.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static <T> T parse(String json, Class<T> clazz) {
    try {
      return MAPPER.readValue(json, clazz);
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot parse json", e);
    }
  }

  public static <T> String unparse(T obj) {
    try {
      return MAPPER.writeValueAsString(obj);
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot unparse json", e);
    }
  }
}
