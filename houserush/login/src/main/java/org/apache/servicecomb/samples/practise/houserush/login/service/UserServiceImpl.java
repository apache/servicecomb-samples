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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserDao userDao;

  public User createUser(User user) {
    if (userDao.findByUsername(user.getUsername()) != null) {
      throw new InvocationException(400, "", "用户名已存在");
    }
    String hashedPassword = user.makeHashedPassword(user.getPassword());
    user.setHashedPassword(hashedPassword);
    return userDao.save(user);
  }


  public User findUser(int id) {
    return userDao.findOne(id);
  }

  public User updateUser(User user) {
    int id = user.getId();
    if (userDao.exists(id)) {
      return userDao.save(user);
    } else {
      throw new DataRetrievalFailureException("cannot update non-existed user");
    }
  }

  public void removeUser(int id) {
    userDao.delete(id);
  }

  public User signin(User user) {
    String username = user.getUsername();
    String password = user.getPassword();
    user = userDao.findByUsername(username);
    if (user != null && password != null) {
      if (user.getHashedPassword().equals(user.makeHashedPassword(password))) {
        user.generateToken();
        return user;
      }
    }
    return null;
  }

  public User verifyToken(String token) {
    int userId = User.verifyTokenGetUserId(token);
    User user = userDao.findOne(userId);
    user.generateToken();
    return user;
  }

  @Override
  public boolean updatePassword(int id, String oldPassword, String newPassword) {
    User user = userDao.findOne(id);
    if (user == null) {
      throw new InvocationException(400, "", "user not existed");
    }
    if (!user.getHashedPassword().equals(user.makeHashedPassword(oldPassword))) {
      throw new InvocationException(400, "", "The password is incorrect");
    }
    user.setHashedPassword(user.makeHashedPassword(newPassword));
    userDao.save(user);
    return true;
  }
}
