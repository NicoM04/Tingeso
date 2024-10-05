package com.example.demo.Services;

import com.example.demo.Entities.CreditEntity;
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

    public CreditEntity saveCredit(CreditEntity credit) {
        return creditRepository.save(credit);
    }
    public ArrayList<CreditEntity> getAllCredits() {
        return (ArrayList<CreditEntity>) creditRepository.findAll();
    }

    public void deleteCreditById(Long id) {
        creditRepository.deleteById(id);
    }







}
