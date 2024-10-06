package com.example.demo.Services;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Entities.UserClientEntity;
import com.example.demo.Repository.CreditRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
@Service
public class CreditService {
    @Autowired
    CreditRepository creditRepository;

    public CreditEntity getCreditById(Long id) {
        return creditRepository.findById(id).get();
    }

    public int getAmountById(Long id) {
        return creditRepository.findById(id).get().getAmount();
    }

    public CreditEntity saveCredit(CreditEntity credit) {
        return creditRepository.save(credit);
    }
    public ArrayList<CreditEntity> getAllCredits() {
        return (ArrayList<CreditEntity>) creditRepository.findAll();
    }

    public ArrayList<CreditEntity> getCreditByState(int state){
        return (ArrayList<CreditEntity>) creditRepository.findByState(state);
    }
    public ArrayList<CreditEntity> getCreditByTypeLoan(int type){
        return (ArrayList<CreditEntity>) creditRepository.findByTypeLoan(type);
    }
    public CreditEntity updateCredit(CreditEntity credit) {
        return creditRepository.save(credit);
    }
    public boolean deleteCredit(Long id) throws Exception {
        try{
            creditRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }







}
