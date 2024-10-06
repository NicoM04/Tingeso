package com.example.demo.Controller;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit")
@CrossOrigin("*")
public class CreditController {
    @Autowired
    CreditService creditService;


    @GetMapping("/")
    public ResponseEntity<List<CreditEntity>> getAllCredits() {
        List<CreditEntity> credit = creditService.getAllCredits();
        return ResponseEntity.ok(credit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditEntity> getCreditById(@PathVariable Long id) {
        CreditEntity credit = creditService.getCreditById(id);
        return ResponseEntity.ok(credit);
    }

    @PostMapping("/")
    public ResponseEntity<CreditEntity> saveCredit(@RequestBody CreditEntity employee) {
        CreditEntity creditNew = creditService.saveCredit(employee);
        return ResponseEntity.ok(creditNew);
    }

    @PutMapping("/")
    public ResponseEntity<CreditEntity> updateCredit(@RequestBody CreditEntity credit){
        CreditEntity creditUpdated = creditService.updateCredit(credit);
        return ResponseEntity.ok(creditUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCreditById(@PathVariable Long id) throws Exception {
        var isDeleted = creditService.deleteCredit(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/amount/{id}")
    public int getAmountById(@PathVariable Long id) {
        return creditService.getAmountById(id);
    }


    @PutMapping("/update-state")
    public ResponseEntity<CreditEntity> updateState(@RequestBody CreditEntity credit){
        CreditEntity creditUpdated = creditService.updateCredit(credit);
        return ResponseEntity.ok(creditUpdated);
    }
    //creo q esta mal esto



}
