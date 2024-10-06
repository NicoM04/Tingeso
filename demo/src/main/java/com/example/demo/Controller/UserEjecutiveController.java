package com.example.demo.Controller;

import com.example.demo.Entities.UserEjecutiveEntity;
import com.example.demo.Services.UserClientService;
import com.example.demo.Services.UserEjecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userEjecutive")
@CrossOrigin("*")
public class UserEjecutiveController {
    @Autowired
    UserEjecutiveService ejecutiveService;



    @GetMapping("/")
    public ResponseEntity<List<UserEjecutiveEntity>> listAllUserEjecutive() {
        List<UserEjecutiveEntity> ejecutive = ejecutiveService.getEjecutives();
        return ResponseEntity.ok(ejecutive);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEjecutiveEntity> getUserEById(@PathVariable Long id) {
        UserEjecutiveEntity ejecutive = ejecutiveService.getUserEById(id);
        return ResponseEntity.ok(ejecutive);
    }

    @PostMapping("/")
    public ResponseEntity<UserEjecutiveEntity> saveEmployee(@RequestBody UserEjecutiveEntity ejecutive) {
        UserEjecutiveEntity ejecutiveNew = ejecutiveService.saveEjecutive(ejecutive);
        return ResponseEntity.ok(ejecutiveNew);
    }

    @PutMapping("/")
    public ResponseEntity<UserEjecutiveEntity> updateEmployee(@RequestBody UserEjecutiveEntity ejecutive){
        UserEjecutiveEntity ejecutiveUpdated = ejecutiveService.updateEjecutive(ejecutive);
        return ResponseEntity.ok(ejecutiveUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable Long id) throws Exception {
        var isDeleted = ejecutiveService.deleteEjecutive(id);
        return ResponseEntity.noContent().build();
    }

}
