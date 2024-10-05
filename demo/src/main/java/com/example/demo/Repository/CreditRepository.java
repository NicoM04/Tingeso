package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entities
        .CreditEntity;
public interface CreditRepository extends JpaRepository<CreditEntity, Long> {
    //agregar un findby por cada wea?, despues viene services

}
