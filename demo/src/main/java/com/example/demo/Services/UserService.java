package com.example.demo.Services;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class UserService {
    @Autowired
    UserRepository userRepository;
    public ArrayList<UserEntity> getUsers(){
        return (ArrayList<UserEntity>) userRepository.findAll();
    }
    public UserEntity getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public UserEntity getUserByRut(String rut){
        return userRepository.findByRut(rut);
    }
    public UserEntity saveUser(UserEntity user){
        return userRepository.save(user);
    }

    public UserEntity updateUser(UserEntity user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) throws Exception {
        try{
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    // Método para iniciar sesión
    public UserEntity login(String mail, String password) {
        return userRepository.findByMailAndPassword(mail, password);
    }

}
