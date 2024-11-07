package com.example.demo.Controller;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Entities.UserEntity;
import com.example.demo.Services.CreditService;
import com.example.demo.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CreditControllerTest {

    @InjectMocks
    private CreditController creditController;
    private CreditEntity credit;
    private UserEntity user;

    @Mock
    private CreditService creditService;

    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        credit = new CreditEntity(1L, 1, 10000, 5, 5.0, 0.0,800.0, 1, 1L);
        user = new UserEntity();
        user.setId(1L);
        user.setName("Test User");
    }

    @Test
    void getAllCredits_ShouldReturnCredits() {
        ArrayList<CreditEntity> credits = new ArrayList<>(Arrays.asList(credit)); // Cambiar a ArrayList
        when(creditService.getAllCredits()).thenReturn(credits);

        ResponseEntity<List<CreditEntity>> response = creditController.getAllCredits();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(credits, response.getBody());
        verify(creditService, times(1)).getAllCredits();
    }


    @Test
    void getCreditById_ShouldReturnCredit() {
        when(creditService.getCreditById(anyLong())).thenReturn(credit);

        ResponseEntity<CreditEntity> response = creditController.getCreditById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(credit, response.getBody());
        verify(creditService, times(1)).getCreditById(1L);
    }

    @Test
    void getCreditsByClientId_ShouldReturnCredits() {
        List<CreditEntity> credits = new ArrayList<>(Arrays.asList(credit));
        when(creditService.getCreditsByClientId(anyLong())).thenReturn(credits);

        ResponseEntity<List<CreditEntity>> response = creditController.getCreditsByClientId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(credits, response.getBody());
        verify(creditService, times(1)).getCreditsByClientId(1L);
    }
    @Test
    public void getUserByCreditId_ShouldReturnUser_WhenCreditExists() {
        when(creditService.getCreditById(1L)).thenReturn(credit);
        when(userService.getUserById(2L)).thenReturn(user);

        ResponseEntity<UserEntity> response = creditController.getUserByCreditId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    public void getUserByCreditId_ShouldReturnNotFound_WhenCreditDoesNotExist() {
        when(creditService.getCreditById(1L)).thenReturn(null);

        ResponseEntity<UserEntity> response = creditController.getUserByCreditId(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void getUserByCreditId_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(creditService.getCreditById(1L)).thenReturn(credit);
        when(userService.getUserById(2L)).thenReturn(null);

        ResponseEntity<UserEntity> response = creditController.getUserByCreditId(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
    @Test
    void saveCredit_ShouldReturnSavedCredit() {
        when(creditService.saveCredit(any(CreditEntity.class))).thenReturn(credit);

        ResponseEntity<CreditEntity> response = creditController.saveCredit(credit);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(credit, response.getBody());
        verify(creditService, times(1)).saveCredit(credit);
    }

    @Test
    void updateCredit_ShouldReturnUpdatedCredit() {
        when(creditService.updateCredit(any(CreditEntity.class))).thenReturn(credit);

        ResponseEntity<CreditEntity> response = creditController.updateCredit(credit);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(credit, response.getBody());
        verify(creditService, times(1)).updateCredit(credit);
    }

    @Test
    void deleteCreditById_ShouldReturnNoContent() throws Exception {
        when(creditService.deleteCredit(anyLong())).thenReturn(true);

        ResponseEntity<Boolean> response = creditController.deleteCreditById(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(creditService, times(1)).deleteCredit(1L);
    }

    @Test
    void updateCreditStatus_ShouldReturnUpdatedCredit() {
        when(creditService.updateCreditStatus(anyLong(), anyInt())).thenReturn(credit);

        ResponseEntity<CreditEntity> response = creditController.updateCreditStatus(1L, 2);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(credit, response.getBody());
        verify(creditService, times(1)).updateCreditStatus(1L, 2);
    }

    @Test
    void simulateCredit_ShouldReturnSimulatedCredit() {
        when(creditService.simulateCredit(any(CreditEntity.class))).thenReturn(credit);

        ResponseEntity<?> response = creditController.simulateCredit(credit);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(credit, response.getBody());
        verify(creditService, times(1)).simulateCredit(credit);
    }

    @Test
    void checkIncomeToPaymentRatio_ShouldReturnTrue() {
        when(creditService.checkIncomeToPaymentRatio(any(CreditEntity.class), anyDouble())).thenReturn(true);

        ResponseEntity<Boolean> response = creditController.checkIncomeToPaymentRatio(credit, 1000.0);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(creditService, times(1)).checkIncomeToPaymentRatio(credit, 1000.0);
    }

    @Test
    void checkCreditHistory_ShouldReturnTrue() {
        when(creditService.checkCreditHistory(anyBoolean())).thenReturn(true);

        ResponseEntity<Boolean> response = creditController.checkCreditHistory(true);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(creditService, times(1)).checkCreditHistory(true);
    }

    @Test
    void checkEmploymentStability_ShouldReturnTrue() {
        when(creditService.checkEmploymentStability(anyInt(), anyBoolean(), anyInt())).thenReturn(true);

        ResponseEntity<Boolean> response = creditController.checkEmploymentStability(3, false, 3);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(creditService, times(1)).checkEmploymentStability(3, false, 3);
    }

    @Test
    void checkDebtToIncomeRatio_ShouldReturnTrue() {
        when(creditService.checkDebtToIncomeRatio(any(CreditEntity.class), anyDouble(), anyDouble())).thenReturn(true);

        ResponseEntity<Boolean> response = creditController.checkDebtToIncomeRatio(credit, 2000.0, 5000.0);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(creditService, times(1)).checkDebtToIncomeRatio(credit, 2000.0, 5000.0);
    }

    @Test
    void checkMaximumLoanAmount_ShouldReturnTrue() {
        when(creditService.checkMaximumLoanAmount(any(CreditEntity.class), anyDouble())).thenReturn(true);

        ResponseEntity<Boolean> response = creditController.checkMaximumLoanAmount(credit, 50000.0);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(creditService, times(1)).checkMaximumLoanAmount(credit, 50000.0);
    }

    @Test
    void checkApplicantAge_ShouldReturnTrue() {
        when(creditService.checkApplicantAge(anyInt(), any(CreditEntity.class))).thenReturn(true);

        ResponseEntity<Boolean> response = creditController.checkApplicantAge(30, credit);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(creditService, times(1)).checkApplicantAge(30, credit);
    }

    @Test
    void evaluateSavingsCapacity_ShouldReturnInsufficient() {
        when(creditService.evaluateSavingsCapacity(anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn("Insuficiente");

        ResponseEntity<String> response = creditController.evaluateSavingsCapacity(false, false, false, false, false);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Insuficiente", response.getBody());
        verify(creditService, times(1)).evaluateSavingsCapacity(false, false, false, false, false);
    }
}