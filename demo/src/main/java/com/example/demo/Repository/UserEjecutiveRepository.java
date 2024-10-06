package com.example.demo.Repository;

import com.example.demo.Entities.UserEjecutiveEntity;
import com.example.demo.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserEjecutiveRepository extends JpaRepository<UserEjecutiveEntity,Long> {
    public UserEjecutiveEntity findByRut(String rut);
}
