package com.example.demo.Services;

import com.example.demo.Entities.UserEjecutiveEntity;
import com.example.demo.Repository.UserEjecutiveRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class UserEjecutiveService {
    @Autowired
    UserEjecutiveRepository ejecutiveRepository;
    public ArrayList<UserEjecutiveEntity> getEmployees(){
        return (ArrayList<UserEjecutiveEntity>) ejecutiveRepository.findAll();
    }

    public UserEjecutiveEntity saveEmployee(UserEjecutiveEntity userEjecutive){
        return ejecutiveRepository.save(userEjecutive);
    }

    public UserEjecutiveEntity updateEmployee(UserEjecutiveEntity userEjecutive) {
        return ejecutiveRepository.save(userEjecutive);
    }

    public boolean deleteEmployee(Long id) throws Exception {
        try{
            ejecutiveRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

}
