package com.service.Hotels.interfaces;

import com.service.Hotels.models.User;


public interface UserService {
  User getUserByID(Long id);
  User addUser(User newUser);
  void removeUser(Long id);
}
