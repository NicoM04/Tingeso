package com.example.demo.Repository;

import com.example.demo.Entities.CreditEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditRepositoryTest {

    @Mock
    private CreditRepository creditRepository;

    private CreditEntity credit;

    @BeforeEach
    void setUp() {
        credit = new CreditEntity(1L, 1, 10000, 5, 3.5, 0,800, 1, 123L);
    }

    @Test
    void testFindByIdClient() {
        // Configuración del comportamiento simulado
        when(creditRepository.findByIdClient(123L)).thenReturn(Arrays.asList(credit));

        // Llamada al método
        List<CreditEntity> credits = creditRepository.findByIdClient(123L);

        // Verificación
        assertEquals(1, credits.size());
        assertEquals(credit, credits.get(0));
        verify(creditRepository, times(1)).findByIdClient(123L);
    }

    @Test
    void testFindByIdClient_NoCredits() {
        // Configuración del comportamiento simulado
        when(creditRepository.findByIdClient(456L)).thenReturn(Collections.emptyList());

        // Llamada al método
        List<CreditEntity> credits = creditRepository.findByIdClient(456L);

        // Verificación
        assertEquals(0, credits.size());
        verify(creditRepository, times(1)).findByIdClient(456L);
    }

    @Test
    void testFindByState() {
        // Configuración del comportamiento simulado
        when(creditRepository.findByState(1)).thenReturn(Arrays.asList(credit));

        // Llamada al método
        List<CreditEntity> credits = creditRepository.findByState(1);

        // Verificación
        assertEquals(1, credits.size());
        assertEquals(credit, credits.get(0));
        verify(creditRepository, times(1)).findByState(1);
    }

    @Test
    void testFindByState_NoCredits() {
        // Configuración del comportamiento simulado
        when(creditRepository.findByState(2)).thenReturn(Collections.emptyList());

        // Llamada al método
        List<CreditEntity> credits = creditRepository.findByState(2);

        // Verificación
        assertEquals(0, credits.size());
        verify(creditRepository, times(1)).findByState(2);
    }

    @Test
    void testFindByTypeLoan() {
        // Configuración del comportamiento simulado
        when(creditRepository.findByTypeLoan(1)).thenReturn(Arrays.asList(credit));

        // Llamada al método
        List<CreditEntity> credits = creditRepository.findByTypeLoan(1);

        // Verificación
        assertEquals(1, credits.size());
        assertEquals(credit, credits.get(0));
        verify(creditRepository, times(1)).findByTypeLoan(1);
    }

    @Test
    void testFindByTypeLoan_NoCredits() {
        // Configuración del comportamiento simulado
        when(creditRepository.findByTypeLoan(2)).thenReturn(Collections.emptyList());

        // Llamada al método
        List<CreditEntity> credits = creditRepository.findByTypeLoan(2);

        // Verificación
        assertEquals(0, credits.size());
        verify(creditRepository, times(1)).findByTypeLoan(2);
    }
}
