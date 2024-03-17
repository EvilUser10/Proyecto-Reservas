package com.service.Hotels.interfaces;

import com.service.Hotels.models.User;


public interface UserService {
  User getUserById(Long id);
  User getUserByUserName(String username);
  User addUser(User newUser);
  void removeUser(Long id);
}
