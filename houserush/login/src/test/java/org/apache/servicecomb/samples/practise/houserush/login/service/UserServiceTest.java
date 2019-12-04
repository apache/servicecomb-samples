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

package org.apache.servicecomb.samples.practise.houserush.login.service;

import org.apache.servicecomb.samples.practise.houserush.login.aggregate.User;
import org.apache.servicecomb.samples.practise.houserush.login.dao.UserDao;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceTest {

  @InjectMocks
  private UserService userService = new UserServiceImpl();

  @Mock
  private UserDao userDao;

  @Test
  public void testCreate() {
    //create a user
    User user = new User();
    user.setUsername("root");
    user.setPassword("123456");
    when(userDao.save(any(User.class))).then(invocation -> {
      User user0 = (User) invocation.getArguments()[0];
      user0.setId(10);
      return user0;
    });
    when(userDao.findByUsername("root")).thenReturn(null).thenReturn(user);

    //first create
    User newUser = new User();
    newUser.setUsername("root");
    newUser.setPassword("123456");
    user = userService.createUser(newUser);
    assertNotNull(user);

    //repeat create
    User newUser2 = new User();
    newUser2.setUsername("root");
    newUser2.setPassword("123456");
    try {
      userService.createUser(newUser2);
      fail("expect to occur an InvocationException but not");
    } catch (InvocationException e) {
      assertEquals("InvocationException: code=400;msg=user already exists", e.getMessage());
    }

  }

  @Test
  public void testUpdatePassword() {
    User user = new User();
    user.setUsername("root");
    user.setHashedPassword(user.makeHashedPassword("123456"));
    user.setId(10);
    when(userDao.findOne(10)).thenReturn(null).thenReturn(user);

    //user not existed
    try {
      userService.updatePassword(10, "123456", "123456789");
      fail("expect to occur an InvocationException but not");
    } catch (InvocationException e) {
      assertEquals("InvocationException: code=400;msg=user not existed", e.getMessage());
    }
    //password is incorrect
    try {
      userService.updatePassword(10, "12345", "123456789");
      fail("expect to occur an InvocationException but not");
    } catch (InvocationException e) {
      assertEquals("InvocationException: code=400;msg=The password is incorrect", e.getMessage());
    }
    boolean success = userService.updatePassword(10, "123456", "123456789");
    assertTrue(success);

  }

  @Test
  public void testSigninAndVerifyToken() {
    //create a user
    User user = new User();
    user.setUsername("root");
    user.setId(10);
    user.setHashedPassword(user.makeHashedPassword("root"));
    when(userDao.findByUsername("root")).thenReturn(user);
    when(userDao.findOne(10)).thenReturn(user);

    //login success
    User user1 = new User();
    user1.setUsername("root");
    user1.setPassword("root");
    user = userService.signin(user1);
    assertNotNull(user);
    assertNotNull(user.getToken());
    //verify success
    String token = user.getToken();
    user = userService.verifyToken(token);
    assertThat(user.getId(), is(10));
    //token expired
    try {
      user = userService.verifyToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3NSIsImV4cCI6MTU2NTE2MzU4MH0.y1vz0mus91c9fje6GHzyzWQxlUA3h8eCg7za_kiATdg");
      fail("expect to occur an InvocationException but not");
    } catch (Exception e) {
      assertEquals("InvocationException: code=400;msg=token has expired", e.getMessage());
    }
    //invalid token expired
    try {
      user = userService.verifyToken("invalid token");
      fail("expect to occur an InvocationException but not");
    } catch (Exception e) {
      assertEquals("InvocationException: code=400;msg=decode token fail", e.getMessage());
    }
    //login fail password incorrect
    User user2 = new User();
    user2.setUsername("root");
    user2.setPassword("root2");
    user = userService.signin(user2);
    assertNull(user);

    //login fail username not existed
    User user3 = new User();
    user3.setUsername("root3");
    user3.setPassword("root");
    user = userService.signin(user2);
    assertNull(user);
  }

}