package com.example.demo.Repository;

import com.example.demo.Entities.DocumentEntity;
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
public class DocumentRepositoryTest {

    @Mock
    private DocumentRepository documentRepository;

    private DocumentEntity document;

    @BeforeEach
    void setUp() {
        document = new DocumentEntity();
        document.setId(1L);
        document.setFileName("document.pdf");
        document.setFilePath("/path/to/document.pdf");
        document.setCreditId(1L);
    }

    @Test
    void testFindByCreditId() {
        // Configuración del comportamiento simulado
        when(documentRepository.findByCreditId(1L)).thenReturn(Arrays.asList(document));

        // Llamada al método
        List<DocumentEntity> documents = documentRepository.findByCreditId(1L);

        // Verificación
        assertEquals(1, documents.size());
        assertEquals(document, documents.get(0));
        verify(documentRepository, times(1)).findByCreditId(1L);
    }

    @Test
    void testFindByCreditId_NoDocuments() {
        // Configuración del comportamiento simulado
        when(documentRepository.findByCreditId(2L)).thenReturn(Collections.emptyList());

        // Llamada al método
        List<DocumentEntity> documents = documentRepository.findByCreditId(2L);

        // Verificación
        assertEquals(0, documents.size());
        verify(documentRepository, times(1)).findByCreditId(2L);
    }
}
