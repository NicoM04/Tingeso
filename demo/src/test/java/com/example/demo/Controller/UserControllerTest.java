package com.example.demo.Controller;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity(1L, "John Doe", "12345678-9", "john@example.com", "123456789", "password", false);
    }

    @Test
    public void testListAllUser() {
        when(userService.getUsers()).thenReturn(new ArrayList<>(Arrays.asList(user)));

        ResponseEntity<List<UserEntity>> response = userController.listAllUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(user, response.getBody().get(0));
        verify(userService).getUsers();
    }


    @Test
    public void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<UserEntity> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).getUserById(1L);
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userService.getUserById(1L)).thenReturn(null);

        ResponseEntity<UserEntity> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(1L);
    }

    @Test
    public void testSaveUser() {
        when(userService.saveUser(any(UserEntity.class))).thenReturn(user);

        ResponseEntity<UserEntity> response = userController.saveUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).saveUser(user);
    }

    @Test
    public void testUpdateUser() {
        when(userService.updateUser(any(UserEntity.class))).thenReturn(user);

        ResponseEntity<UserEntity> response = userController.updateUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).updateUser(user);
    }

    @Test
    public void testDeleteUserById() throws Exception {
        // Setup the service mock to return true for the delete operation
        when(userService.deleteUser(1L)).thenReturn(true);

        // Call the controller method
        ResponseEntity<Boolean> response = userController.deleteUserById(1L);

        // Assert the response status and verify the service call
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }


    @Test
    public void testDeleteUserById_Exception() throws Exception {
        doThrow(new Exception("Error deleting user")).when(userService).deleteUser(1L);

        Exception exception = assertThrows(Exception.class, () -> {
            userController.deleteUserById(1L);
        });

        assertEquals("Error deleting user", exception.getMessage());
        verify(userService).deleteUser(1L);
    }

    @Test
    public void testLogin_Success() {
        when(userService.login("john@example.com", "password")).thenReturn(user);

        ResponseEntity<UserEntity> response = userController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).login("john@example.com", "password");
    }

    @Test
    public void testLogin_Failure() {
        when(userService.login("john@example.com", "wrongpassword")).thenReturn(null);

        user.setPassword("wrongpassword");
        ResponseEntity<UserEntity> response = userController.login(user);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).login("john@example.com", "wrongpassword");
    }
}
