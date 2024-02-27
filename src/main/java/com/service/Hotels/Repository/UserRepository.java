package com.service.Hotels.Repository;

import com.service.Hotels.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long>{
 
}
