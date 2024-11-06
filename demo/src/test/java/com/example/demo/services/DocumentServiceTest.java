package com.example.demo.services;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Repository.CreditRepository;
import com.example.demo.Repository.DocumentRepository;
import com.example.demo.Services.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDocument() throws IOException {
        // Mock data for testing
        Long creditId = 1L;
        byte[] fileData = "sample file content".getBytes();
        when(mockFile.getOriginalFilename()).thenReturn("testFile.txt");
        when(mockFile.getBytes()).thenReturn(fileData);

        DocumentEntity savedDocument = new DocumentEntity();
        savedDocument.setFileName("testFile.txt");
        savedDocument.setFileData(fileData);
        savedDocument.setCreditId(creditId);

        // Mock the repository save behavior
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(savedDocument);

        // Call the method under test
        DocumentEntity result = documentService.saveDocument(mockFile, creditId);

        // Assert the result
        assertNotNull(result);
        assertEquals("testFile.txt", result.getFileName());
        assertArrayEquals(fileData, result.getFileData());
        assertEquals(creditId, result.getCreditId());

        // Verify save was called
        verify(documentRepository, times(1)).save(any(DocumentEntity.class));
    }

    @Test
    void testGetDocumentById_Found() {
        // Mock data for testing
        Long documentId = 1L;
        DocumentEntity document = new DocumentEntity();
        document.setId(documentId);
        document.setFileName("testFile.txt");

        // Mock the repository behavior
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        // Call the method under test
        DocumentEntity result = documentService.getDocumentById(documentId);

        // Assert the result
        assertNotNull(result);
        assertEquals("testFile.txt", result.getFileName());
        assertEquals(documentId, result.getId());

        // Verify findById was called
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    void testGetDocumentById_NotFound() {
        Long documentId = 1L;

        // Mock the repository behavior
        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        // Call the method and assert exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            documentService.getDocumentById(documentId);
        });

        assertEquals("Documento no encontrado.", exception.getMessage());

        // Verify findById was called
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    void testGetAllDocuments() {
        // Mock data for testing
        List<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity doc1 = new DocumentEntity();
        doc1.setFileName("file1.txt");
        DocumentEntity doc2 = new DocumentEntity();
        doc2.setFileName("file2.txt");
        documents.add(doc1);
        documents.add(doc2);

        // Mock the repository behavior
        when(documentRepository.findAll()).thenReturn(documents);

        // Call the method under test
        List<DocumentEntity> result = documentService.getAllDocuments();

        // Assert the result
        assertEquals(2, result.size());
        assertEquals("file1.txt", result.get(0).getFileName());
        assertEquals("file2.txt", result.get(1).getFileName());

        // Verify findAll was called
        verify(documentRepository, times(1)).findAll();
    }

    @Test
    void testGetDocumentsByCreditId() {
        // Mock data for testing
        Long creditId = 1L;
        List<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity doc1 = new DocumentEntity();
        doc1.setCreditId(creditId);
        documents.add(doc1);

        // Mock the repository behavior
        when(documentRepository.findByCreditId(creditId)).thenReturn(documents);

        // Call the method under test
        List<DocumentEntity> result = documentService.getDocumentsByCreditId(creditId);

        // Assert the result
        assertEquals(1, result.size());
        assertEquals(creditId, result.get(0).getCreditId());

        // Verify findByCreditId was called
        verify(documentRepository, times(1)).findByCreditId(creditId);
    }

    @Test
    void testGetDocumentData() {
        // Mock data for testing
        Long documentId = 1L;
        byte[] fileData = "sample file content".getBytes();
        DocumentEntity document = new DocumentEntity();
        document.setFileData(fileData);

        // Mock the repository behavior
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        // Call the method under test
        byte[] result = documentService.getDocumentData(documentId);

        // Assert the result
        assertArrayEquals(fileData, result);

        // Verify findById was called
        verify(documentRepository, times(1)).findById(documentId);
    }
}
