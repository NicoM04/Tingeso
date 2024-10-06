package com.example.demo.Repository;

import com.example.demo.Entities.UserClientEntity;
import com.example.demo.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserClientRepository extends JpaRepository<UserClientEntity, Long> {
    public UserClientEntity findByRut(String rut);
}
