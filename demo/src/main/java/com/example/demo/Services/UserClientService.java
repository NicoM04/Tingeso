package com.example.demo.Services;

import com.example.demo.Entities.UserClientEntity;
import com.example.demo.Repository.UserClientRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service

public class UserClientService {
    @Autowired
    UserClientRepository clientRepository;

    public ArrayList<UserClientEntity> getClients(){
        return (ArrayList<UserClientEntity>) clientRepository.findAll();
    }
    public UserClientEntity getUserCById(Long id){
        return clientRepository.findById(id).get();
    }
    public UserClientEntity getUserCByRut(String rut){
        return clientRepository.findByRut(rut);
    }

    public UserClientEntity saveClient(UserClientEntity userClient){
        return clientRepository.save(userClient);
    }

    public UserClientEntity updateClient(UserClientEntity userClient) {
        return clientRepository.save(userClient);
    }

    public boolean deleteClient(Long id) throws Exception {
        try{
            clientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

}
