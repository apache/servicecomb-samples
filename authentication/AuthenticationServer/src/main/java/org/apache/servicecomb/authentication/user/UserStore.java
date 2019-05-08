package org.apache.servicecomb.authentication.user;

public interface UserStore {
  User loadUserByUsername(String userName);
}
