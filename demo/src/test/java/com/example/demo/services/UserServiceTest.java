package com.example.demo.services;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetUsers_thenReturnListOfUsers() {
        // Given
        ArrayList<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity(1L, "Alice", "12.345.678-9", "alice@example.com", "123456789", "password", false));
        when(userRepository.findAll()).thenReturn(users);

        // When
        ArrayList<UserEntity> result = userService.getUsers();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void whenGetUserById_thenReturnUser() {
        // Given
        UserEntity user = new UserEntity(1L, "Bob", "98.765.432-1", "bob@example.com", "987654321", "password", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserEntity result = userService.getUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Bob");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void whenGetUserByRut_thenReturnUser() {
        // Given
        UserEntity user = new UserEntity(1L, "Charlie", "12.345.678-9", "charlie@example.com", "123456789", "password", false);
        when(userRepository.findByRut("12.345.678-9")).thenReturn(user);

        // When
        UserEntity result = userService.getUserByRut("12.345.678-9");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12.345.678-9");
        verify(userRepository, times(1)).findByRut("12.345.678-9");
    }

    @Test
    void whenSaveUser_thenReturnSavedUser() {
        // Given
        UserEntity user = new UserEntity(null, "Diana", "98.765.432-1", "diana@example.com", "987654321", "password", true);
        when(userRepository.save(user)).thenReturn(new UserEntity(1L, "Diana", "98.765.432-1", "diana@example.com", "987654321", "password", true));

        // When
        UserEntity result = userService.saveUser(user);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Diana");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenUpdateUser_thenReturnUpdatedUser() {
        // Given
        UserEntity user = new UserEntity(1L, "Eve", "12.345.678-9", "eve@example.com", "123456789", "password", false);
        when(userRepository.save(user)).thenReturn(user);

        // When
        UserEntity result = userService.updateUser(user);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Eve");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenDeleteUser_thenReturnTrue() throws Exception {
        // Given
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // When
        boolean result = userService.deleteUser(userId);

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void whenLogin_thenReturnUser() {
        // Given
        String mail = "frank@example.com";
        String password = "securepassword";
        UserEntity user = new UserEntity(1L, "Frank", "12.345.678-9", mail, "123456789", password, false);
        when(userRepository.findByMailAndPassword(mail, password)).thenReturn(user);

        // When
        UserEntity result = userService.login(mail, password);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMail()).isEqualTo(mail);
        assertThat(result.getPassword()).isEqualTo(password);
        verify(userRepository, times(1)).findByMailAndPassword(mail, password);
    }

    @Test
    void whenDeleteUserThrowsException_thenReturnException() {
        // Given
        Long userId = 1L;
        doThrow(new RuntimeException("Error deleting user")).when(userRepository).deleteById(userId);

        // When
        Exception exception = null;
        try {
            userService.deleteUser(userId);
        } catch (Exception e) {
            exception = e;
        }

        // Then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Error deleting user");
        verify(userRepository, times(1)).deleteById(userId);
    }

}
