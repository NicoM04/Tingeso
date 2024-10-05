package com.example.demo.Repository;

import com.example.demo.Entities.UserClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClientRepository extends JpaRepository<UserClientEntity, Long> {
}
