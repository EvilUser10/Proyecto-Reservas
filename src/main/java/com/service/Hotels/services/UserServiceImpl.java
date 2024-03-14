package com.service.Hotels.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.interfaces.UserService;
import com.service.Hotels.models.User;
import com.service.Hotels.repositories.UserRepository;

public class UserServiceImpl implements UserService {
  @Autowired
  UserRepository userRepository;

  public User getUserById(Long id) {
    return userRepository.findById(id).get();
  }

  public User addUser(User newUser) {
    try {
      User user = userRepository.save(newUser);
      return user;
    } catch (Exception e) {
      throw new BadRequestException("Error adding new User : " + newUser);
    }
  }

  public void removeUser(Long id) {
    if (id == null) {
      throw new BadRequestException("The ID cannot be null");
    }
    if (userRepository.existsById(id)) {
      try {
        userRepository.deleteById(id);
      } catch (IllegalArgumentException ex) {
        throw new BadRequestException("Error deleting the user with ID " + id);
      }
    }
  }

}
