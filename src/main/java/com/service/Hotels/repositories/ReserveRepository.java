package com.service.Hotels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.Hotels.models.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Long>{
    
}
