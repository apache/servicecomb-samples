package org.apache.servicecomb.authentication.user;

import java.util.Collection;

public interface User {
  Collection<Role> getRoles();

  String getPassword();

  String getUsername();
}
