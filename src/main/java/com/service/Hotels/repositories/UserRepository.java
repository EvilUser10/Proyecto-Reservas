package com.service.Hotels.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.Hotels.models.User;



public interface UserRepository extends JpaRepository<User, Long>{
  Optional<User> findByUsername(String username);
}
