package com.service.Hotels.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.service.Hotels.dto.UpdateUserDto;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.ForbiddenException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.User;
import com.service.Hotels.repositories.UserRepository;

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;

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

  public User getUserById(Long userId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return userRepository.findById(userId).get();
    } else {
      throw new ForbiddenException("Access denied");
    }
  }

  public User getUserByUserName(String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      if (username == null) {
        throw new BadRequestException("The username cannot be null");
      }
      return userRepository.findByUsername(username)
          .orElseThrow(() -> new NotFoundException("None found any user with the userName " + username));

    } else {
      throw new ForbiddenException("Access denied");
    }

  }

  public User editUser(Long id, UpdateUserDto userToUpdate) {
    if (id == null) {
      throw new BadRequestException("The Id cannot be null.");
    }

    User userFind = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("User with id: " + id + "not found"));

    userFind.setUsername(userToUpdate.getUsername());
    userFind.setEmail(userToUpdate.getEmail());
    userFind.setPassword(userToUpdate.getPassword());

    try {
      User user = userRepository.save(userFind);
      return user;
    } catch (Exception e) {
      throw (new BadRequestException("The user cannot be modified."));
    }
  }

}
