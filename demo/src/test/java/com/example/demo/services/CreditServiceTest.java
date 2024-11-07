package com.example.demo.services;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Repository.CreditRepository;
import com.example.demo.Repository.DocumentRepository;
import com.example.demo.Services.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@MockitoSettings (strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;
    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private CreditService creditService;

    private CreditEntity credit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks
        credit = new CreditEntity(1L, 1, 10000, 5, 3.5, 0,800, 1, 123L);
        credit.setMonthlyPayment(5000);
    }
/*
    // Test for getCreditById
    @Test
    void testGetCreditById() {
        when(creditRepository.findById(1L)).thenReturn(credit);


        CreditEntity foundCredit = creditService.getCreditById(1L);

        assertNotNull(foundCredit);
        assertEquals(credit.getId(), foundCredit.getId());
        verify(creditRepository, times(1)).findById(1L);
    }
    */


    // Test for getCreditsByClientId
    @Test
    void testGetCreditsByClientId() {
        List<CreditEntity> credits = new ArrayList<>();
        credits.add(credit);

        when(creditRepository.findByIdClient(123L)).thenReturn(credits);

        List<CreditEntity> foundCredits = creditService.getCreditsByClientId(123L);

        assertNotNull(foundCredits);
        assertEquals(1, foundCredits.size());
        assertEquals(credit.getIdClient(), foundCredits.get(0).getIdClient());
        verify(creditRepository, times(1)).findByIdClient(123L);
    }



    // Test for saveCredit
    @Test
    void testSaveCredit() {
        // Setting up expected monthly payment based on inputs
        double expectedMonthlyPayment = 181.91744970256332;  // Example expected result, may vary

        when(creditRepository.save(any(CreditEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        CreditEntity savedCredit = creditService.saveCredit(credit);

        assertNotNull(savedCredit);
        assertEquals(expectedMonthlyPayment, savedCredit.getMonthlyPayment(), 0.01);  // Check with a delta for precision
        verify(creditRepository, times(1)).save(credit);
    }

    @Test
    void testGetAllCredits() {
        // Arrange
        ArrayList<CreditEntity> credits = new ArrayList<>();
        CreditEntity credit1 = new CreditEntity();
        CreditEntity credit2 = new CreditEntity();
        credits.add(credit1);
        credits.add(credit2);

        when(creditRepository.findAll()).thenReturn(credits);

        // Act
        ArrayList<CreditEntity> result = creditService.getAllCredits();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(creditRepository, times(1)).findAll();
    }

    @Test
    void testGetCreditByState() {
        int state = 1;
        List<CreditEntity> credits = List.of(credit); // Lista de créditos simulada

        when(creditRepository.findByState(state)).thenReturn(credits);

        List<CreditEntity> result = creditService.getCreditByState(state);

        assertNotNull(result); // Verificamos que la lista no sea nula
        assertEquals(1, result.size()); // Verificamos el tamaño de la lista
        assertEquals(state, result.get(0).getState()); // Verificamos el estado del crédito
        verify(creditRepository).findByState(state); // Verificamos que se llamó el método con el estado adecuado
    }


    @Test
    void testGetCreditByTypeLoan() {
        // Arrange
        int type = 2;
        ArrayList<CreditEntity> credits = new ArrayList<>();
        CreditEntity credit1 = new CreditEntity();
        credit1.setTypeLoan(type);
        credits.add(credit1);

        when(creditRepository.findByTypeLoan(type)).thenReturn(credits);

        // Act
        ArrayList<CreditEntity> result = creditService.getCreditByTypeLoan(type);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(type, result.get(0).getTypeLoan());
        verify(creditRepository, times(1)).findByTypeLoan(type);
    }

    @Test
    void testUpdateCredit() {
        // Arrange
        CreditEntity credit = new CreditEntity();
        credit.setId(1L);
        credit.setAmount(5000);

        when(creditRepository.save(credit)).thenReturn(credit);

        // Act
        CreditEntity result = creditService.updateCredit(credit);

        // Assert
        assertNotNull(result);
        assertEquals(credit.getId(), result.getId());
        assertEquals(credit.getAmount(), result.getAmount());
        verify(creditRepository, times(1)).save(credit);
    }

    @Test
    void testDeleteCreditSuccess() throws Exception {
        // Arrange
        Long creditId = 1L;
        doNothing().when(creditRepository).deleteById(creditId);

        // Act
        boolean result = creditService.deleteCredit(creditId);

        // Assert
        assertTrue(result);
        verify(creditRepository, times(1)).deleteById(creditId);
    }

    @Test
    void testDeleteCreditFailure() {
        // Arrange
        Long creditId = 1L;
        doThrow(new RuntimeException("Delete failed")).when(creditRepository).deleteById(creditId);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> creditService.deleteCredit(creditId));
        assertEquals("Delete failed", exception.getMessage());
        verify(creditRepository, times(1)).deleteById(creditId);
    }
    // Test for updateCreditStatus
    @Test
    void testUpdateCreditStatus() {
        Long creditId = 1L;
        int newStatus = 2;

        when(creditRepository.findById(creditId)).thenReturn(Optional.of(credit));
        when(creditRepository.save(credit)).thenReturn(credit);

        CreditEntity updatedCredit = creditService.updateCreditStatus(creditId, newStatus);

        assertNotNull(updatedCredit);
        assertEquals(newStatus, updatedCredit.getState());
        verify(creditRepository).findById(creditId);
        verify(creditRepository).save(credit);
    }

    // Test for calculateMonthlyPayment
    @Test
    void testCalculateMonthlyPayment() {
        double loanAmount = 10000;
        double annualInterestRate = 5;
        int termInYears = 2;

        double monthlyPayment = creditService.calculateMonthlyPayment(loanAmount, annualInterestRate, termInYears);

        assertTrue(monthlyPayment > 0); // Verificamos que el cálculo no da 0
    }

    // Test for simulateCredit
    @Test
    void testSimulateCredit() {
        CreditEntity simulatedCredit = creditService.simulateCredit(credit);

        assertNotNull(simulatedCredit);
        assertTrue(simulatedCredit.getMonthlyPayment() > 0);
    }

    @Test
    void testCheckIncomeToPaymentRatioWithinLimit() {
        double monthlyIncome = 20000;
        assertTrue(creditService.checkIncomeToPaymentRatio(credit, monthlyIncome));
    }

    @Test
    void testCheckIncomeToPaymentRatioExceedsLimit() {
        double monthlyIncome = 10000;  // Ratio superior al 35%
        assertFalse(creditService.checkIncomeToPaymentRatio(credit, monthlyIncome));
    }

    @Test
    void testCheckCreditHistoryGood() {
        assertTrue(creditService.checkCreditHistory(true));
    }

    @Test
    void testCheckCreditHistoryBad() {
        assertFalse(creditService.checkCreditHistory(false));
    }

    @Test
    void testCheckEmploymentStabilityEmployedWithinLimit() {
        assertTrue(creditService.checkEmploymentStability(2, false, 0));
    }

    @Test
    void testCheckEmploymentStabilityEmployedBelowLimit() {
        assertFalse(creditService.checkEmploymentStability(0, false, 0));
    }

    @Test
    void testCheckEmploymentStabilitySelfEmployedWithinLimit() {
        assertTrue(creditService.checkEmploymentStability(0, true, 2));
    }

    @Test
    void testCheckEmploymentStabilitySelfEmployedBelowLimit() {
        assertFalse(creditService.checkEmploymentStability(0, true, 1));
    }

    @Test
    void testCheckDebtToIncomeRatioWithinLimit() {
        double totalDebt = 4000;
        double monthlyIncome = 20000;
        assertTrue(creditService.checkDebtToIncomeRatio(credit, totalDebt, monthlyIncome));
    }

    @Test
    void testCheckDebtToIncomeRatioExceedsLimit() {
        double totalDebt = 15000;
        double monthlyIncome = 20000;
        assertFalse(creditService.checkDebtToIncomeRatio(credit, totalDebt, monthlyIncome));
    }

    @Test
    void testCheckMaximumLoanAmountWithinLimitType1() {
        double propertyValue = 100000; // 80% de 100000 es 80000
        credit.setTypeLoan(1);
        credit.setAmount(80000);
        assertTrue(creditService.checkMaximumLoanAmount(credit, propertyValue));
    }

    @Test
    void testCheckMaximumLoanAmountExceedsLimitType1() {
        double propertyValue = 100000; // 80% de 100000 es 80000
        credit.setTypeLoan(1);
        credit.setAmount(90000);
        assertFalse(creditService.checkMaximumLoanAmount(credit, propertyValue));
    }

    @Test
    void testCheckMaximumLoanAmountWithinLimitType2() {
        double propertyValue = 100000; // 70% de 100000 es 70000
        credit.setTypeLoan(2);
        credit.setAmount(70000);
        assertTrue(creditService.checkMaximumLoanAmount(credit, propertyValue));
    }

    @Test
    void testCheckMaximumLoanAmountExceedsLimitType2() {
        double propertyValue = 100000; // 70% de 100000 es 70000
        credit.setTypeLoan(2);
        credit.setAmount(75000);
        assertFalse(creditService.checkMaximumLoanAmount(credit, propertyValue));
    }

    @Test
    void testCheckMaximumLoanAmountInvalidType() {
        credit.setTypeLoan(5); // Tipo no válido
        double propertyValue = 100000;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                creditService.checkMaximumLoanAmount(credit, propertyValue)
        );
        assertEquals("Tipo de préstamo no válido: 5", exception.getMessage());
    }

    @Test
    void testCheckMaximumLoanAmountNegativeAmount() {
        credit.setTypeLoan(1);
        credit.setAmount(-50000); // Monto negativo
        double propertyValue = 100000;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                creditService.checkMaximumLoanAmount(credit, propertyValue)
        );
        assertEquals("El monto del crédito no puede ser negativo.", exception.getMessage());
    }
    @Test
    void testCheckApplicantAge() {
        // Configuración del CreditEntity
        CreditEntity credit = new CreditEntity();

        // Caso 1: Edad actual = 70, plazo = 4 años (termina a los 74 años)
        credit.setDueDate(4);
        assertTrue(creditService.checkApplicantAge(70, credit));

        // Caso 2: Edad actual = 70, plazo = 5 años (termina a los 75 años)
        credit.setDueDate(5);
        assertTrue(creditService.checkApplicantAge(70, credit));

        // Caso 3: Edad actual = 71, plazo = 4 años (termina a los 75 años)
        credit.setDueDate(4);
        assertFalse(creditService.checkApplicantAge(71, credit));

        // Caso 4: Edad actual = 72, plazo = 3 años (termina a los 75 años)
        credit.setDueDate(3);
        assertFalse(creditService.checkApplicantAge(72, credit));

        // Caso 5: Edad actual = 73, plazo = 1 año (termina a los 74 años)
        credit.setDueDate(1);
        assertTrue(creditService.checkApplicantAge(73, credit));

        // Caso 6: Edad actual = 76, plazo = 1 año (termina a los 77 años)
        credit.setDueDate(1);
        assertFalse(creditService.checkApplicantAge(76, credit));
    }

    @Test
    void testEvaluateSavingsCapacity() {
        // Caso 1: Todos los criterios cumplidos
        assertEquals("Sólida", creditService.evaluateSavingsCapacity(true, true, true, true, true));

        // Caso 2: 4 criterios cumplidos
        assertEquals("Moderada", creditService.evaluateSavingsCapacity(true, true, true, true, false));

        // Caso 3: 3 criterios cumplidos
        assertEquals("Moderada", creditService.evaluateSavingsCapacity(true, true, true, false, false));

        // Caso 4: 2 criterios cumplidos
        assertEquals("Insuficiente", creditService.evaluateSavingsCapacity(true, true, false, false, false));

        // Caso 5: 1 criterio cumplido
        assertEquals("Insuficiente", creditService.evaluateSavingsCapacity(true, false, false, false, false));

        // Caso 6: Ningún criterio cumplido
        assertEquals("Insuficiente", creditService.evaluateSavingsCapacity(false, false, false, false, false));
    }

}
