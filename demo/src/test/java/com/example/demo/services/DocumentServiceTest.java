package com.example.demo.services;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Repository.DocumentRepository;
import com.example.demo.Services.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    private final String uploadDir = "C:\\Users\\Nicolas Morales\\Desktop\\repositorio\\Tingeso";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDocument_ShouldSaveDocument() throws IOException {
        // Given
        String fileName = "testFile.txt";
        Long creditId = 1L;
        Path filePath = Paths.get(uploadDir, fileName);

        DocumentEntity documentToSave = new DocumentEntity(null, fileName, filePath.toString(), creditId);
        DocumentEntity savedDocument = new DocumentEntity(1L, fileName, filePath.toString(), creditId);

        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(savedDocument);

        // When
        DocumentEntity result = documentService.saveDocument(fileName, creditId);

        // Then
        assertThat(result).isEqualTo(savedDocument);
        verify(documentRepository, times(1)).save(any(DocumentEntity.class));
    }



    @Test
    void testGetDocumentById_ShouldReturnDocument() {
        // Given
        Long documentId = 1L;
        DocumentEntity document = new DocumentEntity(documentId, "testFile.txt", "some/path", 1L);

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        // When
        DocumentEntity result = documentService.getDocumentById(documentId);

        // Then
        assertThat(result).isEqualTo(document);
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    void testGetDocumentById_ShouldThrowExceptionWhenNotFound() {
        // Given
        Long documentId = 1L;

        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> documentService.getDocumentById(documentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Documento no encontrado");
    }

    @Test
    void testGetAllDocuments_ShouldReturnAllDocuments() {
        // Given
        DocumentEntity doc1 = new DocumentEntity(1L, "file1.txt", "path/to/file1", 1L);
        DocumentEntity doc2 = new DocumentEntity(2L, "file2.txt", "path/to/file2", 2L);
        List<DocumentEntity> documents = Arrays.asList(doc1, doc2);

        when(documentRepository.findAll()).thenReturn(documents);

        // When
        List<DocumentEntity> result = documentService.getAllDocuments();

        // Then
        assertThat(result).isEqualTo(documents);
        verify(documentRepository, times(1)).findAll();
    }

    @Test
    void testGetDocumentsByCreditId_ShouldReturnDocuments() {
        // Given
        Long creditId = 1L;
        DocumentEntity doc1 = new DocumentEntity(1L, "file1.txt", "path/to/file1", creditId);
        DocumentEntity doc2 = new DocumentEntity(2L, "file2.txt", "path/to/file2", creditId);
        List<DocumentEntity> documents = Arrays.asList(doc1, doc2);

        when(documentRepository.findByCreditId(creditId)).thenReturn(documents);

        // When
        List<DocumentEntity> result = documentService.getDocumentsByCreditId(creditId);

        // Then
        assertThat(result).isEqualTo(documents);
        verify(documentRepository, times(1)).findByCreditId(creditId);
    }
}
