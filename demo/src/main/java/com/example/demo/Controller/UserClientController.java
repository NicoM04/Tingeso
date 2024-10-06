package com.example.demo.Controller;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Entities.UserClientEntity;
import com.example.demo.Services.CreditService;
import com.example.demo.Services.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userClient")
@CrossOrigin("*")
public class UserClientController {
    @Autowired
    UserClientService clientService;


    //falta obtener mediante distintos atributos?, registro e inicio de sesion, crear credito segun weas, obtener creditos segun tal wea,
    //despues agregar mas dependiendo de lo q deba hacer cada cosa a detalle en el front
    @GetMapping("/")
    public ResponseEntity<List<UserClientEntity>> listEmployees() {
        List<UserClientEntity> employees = clientService.getClients();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserClientEntity> getEmployeeById(@PathVariable Long id) {
        UserClientEntity client = clientService.getUserCById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/")
    public ResponseEntity<UserClientEntity> saveEmployee(@RequestBody UserClientEntity client) {
        UserClientEntity clientNew = clientService.saveClient(client);
        return ResponseEntity.ok(clientNew);
    }

    @PutMapping("/")
    public ResponseEntity<UserClientEntity> updateEmployee(@RequestBody UserClientEntity client){
        UserClientEntity clientUpdated = clientService.updateClient(client);
        return ResponseEntity.ok(clientUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable Long id) throws Exception {
        var isDeleted = clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
