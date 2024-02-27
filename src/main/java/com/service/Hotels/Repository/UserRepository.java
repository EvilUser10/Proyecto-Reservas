package com.service.Hotels.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.service.Hotels.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    List<User> findById(@Param("id") String id);
    
}
