package com.example.demo.Repository;

import com.example.demo.Entities.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    public UserEntity findByRut(String rut);
    public UserEntity findByMailAndPassword(String mail, String password);

}
