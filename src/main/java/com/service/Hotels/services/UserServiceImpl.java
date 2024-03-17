package com.service.Hotels.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.interfaces.UserService;
import com.service.Hotels.models.User;
import com.service.Hotels.repositories.UserRepository;
@Service
public class UserServiceImpl implements UserService {
  @Autowired
  UserRepository userRepository;

  public User getUserById(Long id) {
    if (id == null) {
      throw new BadRequestException("The Id cannot be null");
    }
    
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("None found any user with the ID " + id));
    return user;
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

  @Override
  public User getUserByUserName(String username) {
    if (username == null) {
      throw new BadRequestException("The username cannot be null");
    }
    User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("None found any user with the userName " + username));
    return user;
  }

}
