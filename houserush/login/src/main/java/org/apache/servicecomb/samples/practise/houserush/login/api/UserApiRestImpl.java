/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.samples.practise.houserush.login.api;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.samples.practise.houserush.login.aggregate.User;
import org.apache.servicecomb.samples.practise.houserush.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestSchema(schemaId = "userApiRest")
@RequestMapping("/")
public class UserApiRestImpl implements UserApi {
  @Autowired
  UserService userService;

  @PostMapping("users")
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @GetMapping("users/{id}")
  public User findUser(@PathVariable int id) {
    return userService.findUser(id);
  }

  @DeleteMapping("users/{id}")
  public void removeUser(@PathVariable int id) {
    userService.removeUser(id);
  }

  @PutMapping("users/signin")
  public User signin(@RequestBody User user) {
    return userService.signin(user);
  }

  @PutMapping("users/verify_token")
  public User verifyToken(@RequestParam String token) {
    return userService.verifyToken(token);
  }

  @PutMapping("users/password")
  public boolean updatePassword(@RequestHeader int customerId, @RequestBody User user) {
    return userService.updatePassword(customerId, user.getOldPassword(), user.getPassword());
  }
}
