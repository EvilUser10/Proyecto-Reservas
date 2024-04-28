package com.service.Hotels.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.Hotels.repositories.UserRepository;
import com.service.Hotels.services.UserService;

import java.util.List;

import com.service.Hotels.dto.UpdateUserDto;
import com.service.Hotels.dto.UserDto;
import com.service.Hotels.models.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("{userName}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable("userName") String userName) {
        User user = userService.getUserByUserName(userName);
        return ResponseEntity.ok(new UserDto(user));
    }

    @PutMapping("{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") Long id, @RequestBody UpdateUserDto userToUpdate) {
        return ResponseEntity.ok(userService.editUser(id, userToUpdate));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> removeUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return ResponseEntity.ok().body("User with id: " + id + " deleted.");
    }
}
