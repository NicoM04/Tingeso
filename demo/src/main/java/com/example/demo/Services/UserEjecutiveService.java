package com.example.demo.Services;

import com.example.demo.Entities.UserClientEntity;
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
    public ArrayList<UserEjecutiveEntity> getEjecutives(){
        return (ArrayList<UserEjecutiveEntity>) ejecutiveRepository.findAll();
    }
    public UserEjecutiveEntity getUserEById(Long id){
        return ejecutiveRepository.findById(id).get();
    }
    public UserEjecutiveEntity getUserEByRut(String rut){
        return ejecutiveRepository.findByRut(rut);
    }
    public UserEjecutiveEntity saveEjecutive(UserEjecutiveEntity userEjecutive){
        return ejecutiveRepository.save(userEjecutive);
    }

    public UserEjecutiveEntity updateEjecutive(UserEjecutiveEntity userEjecutive) {
        return ejecutiveRepository.save(userEjecutive);
    }

    public boolean deleteEjecutive(Long id) throws Exception {
        try{
            ejecutiveRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

}
