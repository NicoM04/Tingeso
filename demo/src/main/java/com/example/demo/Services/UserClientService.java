package com.example.demo.Services;

import com.example.demo.Entities.UserClientEntity;
import com.example.demo.Repository.UserClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class UserClientService {
    @Autowired
    UserClientRepository clientRepository;

    public ArrayList<UserClientEntity> getEmployees(){
        return (ArrayList<UserClientEntity>) clientRepository.findAll();
    }

    public UserClientEntity saveEmployee(UserClientEntity userClient){
        return clientRepository.save(userClient);
    }

    public UserClientEntity updateEmployee(UserClientEntity userClient) {
        return clientRepository.save(userClient);
    }

    public boolean deleteEmployee(Long id) throws Exception {
        try{
            clientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

}
