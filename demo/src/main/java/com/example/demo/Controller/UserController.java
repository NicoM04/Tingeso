package com.example.demo.Controller;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserEntity>> listAllUser() {
        List<UserEntity> user = userService.getUsers();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/")
    public ResponseEntity<UserEntity> saveUser(@RequestBody UserEntity user) {
        UserEntity userNew = userService.saveUser(user);
        return ResponseEntity.ok(userNew);
    }

    @PutMapping("/")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user){
        UserEntity userUpdated = userService.updateUser(user);
        return ResponseEntity.ok(userUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUserById(@PathVariable Long id) throws Exception {
        var isDeleted = userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestBody UserEntity user) {
        UserEntity loggedInUser = userService.login(user.getMail(), user.getPassword());
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser); // Retorna el usuario si se logra iniciar sesión
        } else {
            return ResponseEntity.status(401).body(null); // Devuelve un 401 si las credenciales son incorrectas
        }
    }

}