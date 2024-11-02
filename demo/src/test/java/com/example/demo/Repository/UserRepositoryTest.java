package com.example.demo.Repository;

import com.example.demo.Entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setRut("12345678-9");
        user.setMail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    void testFindByRut() {
        // Configuración del comportamiento simulado
        when(userRepository.findByRut("12345678-9")).thenReturn(user);

        // Llamada al método
        UserEntity foundUser = userRepository.findByRut("12345678-9");

        // Verificación
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByRut("12345678-9");
    }

    @Test
    void testFindByMailAndPassword() {
        // Configuración del comportamiento simulado
        when(userRepository.findByMailAndPassword("test@example.com", "password123")).thenReturn(user);

        // Llamada al método
        UserEntity foundUser = userRepository.findByMailAndPassword("test@example.com", "password123");

        // Verificación
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByMailAndPassword("test@example.com", "password123");
    }

    @Test
    void testFindByRut_NotFound() {
        // Configuración del comportamiento simulado
        when(userRepository.findByRut("invalid-rut")).thenReturn(null);

        // Llamada al método
        UserEntity foundUser = userRepository.findByRut("invalid-rut");

        // Verificación
        assertEquals(null, foundUser);
        verify(userRepository, times(1)).findByRut("invalid-rut");
    }

    @Test
    void testFindByMailAndPassword_InvalidCredentials() {
        // Configuración del comportamiento simulado
        when(userRepository.findByMailAndPassword("wrong@example.com", "wrongpassword")).thenReturn(null);

        // Llamada al método
        UserEntity foundUser = userRepository.findByMailAndPassword("wrong@example.com", "wrongpassword");

        // Verificación
        assertEquals(null, foundUser);
        verify(userRepository, times(1)).findByMailAndPassword("wrong@example.com", "wrongpassword");
    }
}
