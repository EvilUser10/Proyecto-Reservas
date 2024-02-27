package com.service.Hotels.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.Hotels.Model.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Long>{
    
}
