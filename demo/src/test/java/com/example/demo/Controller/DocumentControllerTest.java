package com.example.demo.Controller;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Services.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DocumentControllerTest {

    @InjectMocks
    private DocumentController documentController;

    @Mock
    private DocumentService documentService;

    private DocumentEntity document;
    private final Long creditId = 1L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        document = new DocumentEntity(1L, "testfile.txt", "C:\\path\\to\\testfile.txt", creditId);
    }

    @Test
    public void testUploadDocuments_Success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("testfile.txt");
        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));

        MultipartFile[] files = {file};

        when(documentService.saveDocument(anyString(), eq(creditId))).thenReturn(document);

        ResponseEntity<String> response = documentController.uploadDocuments(files, creditId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Archivo subido correctamente: C:\\path\\to\\testfile.txt\n", response.getBody());
        verify(documentService).saveDocument("testfile.txt", creditId);
    }

    @Test
    public void testUploadDocuments_NoFiles() {
        MultipartFile[] files = {};

        ResponseEntity<String> response = documentController.uploadDocuments(files, creditId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No se han subido archivos.", response.getBody());
    }

    @Test
    public void testUploadDocuments_EmptyFile() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);
        MultipartFile[] files = {file};

        ResponseEntity<String> response = documentController.uploadDocuments(files, creditId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Uno o más archivos están vacíos.", response.getBody());
    }

    @Test
    public void testDownloadDocument_Success() throws IOException {
        // Configuramos el documento que queremos que el servicio devuelva
        when(documentService.getDocumentById(1L)).thenReturn(document);

        // Creamos un recurso simulado para el documento
        Path path = Paths.get(document.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        // Simulamos que el recurso existe
        when(resource.exists()).thenReturn(true);
        // Simulamos la lectura de un inputStream desde el recurso
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("contenido del archivo".getBytes()));

        // Cuando se llama a documentService.getDocumentById, queremos que se devuelva el recurso
        // No hay un método para obtener el recurso, así que este paso es solo ilustrativo
        when(documentService.getDocumentById(1L)).thenReturn(document);

        // Llamamos al método del controlador que queremos probar
        ResponseEntity<Resource> response = documentController.downloadDocument(1L);

        // Verificamos la respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=\"" + document.getFileName() + "\"",
                response.getHeaders().getFirst("Content-Disposition"));
        verify(documentService).getDocumentById(1L);
    }




    @Test
    public void testDownloadDocument_NotFound() {
        when(documentService.getDocumentById(1L)).thenThrow(new IllegalArgumentException("Documento no encontrado."));

        ResponseEntity<Resource> response = documentController.downloadDocument(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllDocuments() {
        List<DocumentEntity> documents = new ArrayList<>(Arrays.asList(document));
        when(documentService.getAllDocuments()).thenReturn(documents);

        ResponseEntity<List<DocumentEntity>> response = documentController.getAllDocuments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(document, response.getBody().get(0));
        verify(documentService).getAllDocuments();
    }

    @Test
    public void testGetDocumentsByCreditId() {
        List<DocumentEntity> documents = new ArrayList<>(Arrays.asList(document));
        when(documentService.getDocumentsByCreditId(creditId)).thenReturn(documents);

        ResponseEntity<List<DocumentEntity>> response = documentController.getDocumentsByCreditId(creditId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(document, response.getBody().get(0));
        verify(documentService).getDocumentsByCreditId(creditId);
    }

    @Test
    public void testGetDocumentsByCreditId_NoContent() {
        when(documentService.getDocumentsByCreditId(creditId)).thenReturn(new ArrayList<>());

        ResponseEntity<List<DocumentEntity>> response = documentController.getDocumentsByCreditId(creditId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}