package com.service.Hotels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.Hotels.models.User;



public interface UserRepository extends JpaRepository<User, Long>{
 
}
