package com.example.demo.Controller;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Services.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
    }

    @Test
    void testUploadDocuments_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("files", "testFile.txt", MediaType.TEXT_PLAIN_VALUE, "file content".getBytes());

        // Simula el servicio de guardado
        doNothing().when(documentService).saveDocument(any(), anyLong());

        mockMvc.perform(multipart("/api/documents/upload")
                        .file(file)
                        .param("creditId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Archivo subido correctamente: testFile.txt\n"));
    }

    @Test
    void testUploadDocuments_EmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("files", "emptyFile.txt", MediaType.TEXT_PLAIN_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/documents/upload")
                        .file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Uno o más archivos están vacíos."));
    }

    @Test
    void testUploadDocuments_NoFiles() throws Exception {
        mockMvc.perform(multipart("/api/documents/upload"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se han subido archivos."));
    }

    @Test
    void testDownloadDocument_Success() throws Exception {
        // Configura el documento simulado
        Long documentId = 1L;
        byte[] fileData = "file content".getBytes();
        DocumentEntity document = new DocumentEntity();
        document.setFileName("testFile.txt");
        document.setFileData(fileData);

        // Simula la obtención del documento
        when(documentService.getDocumentById(documentId)).thenReturn(document);

        mockMvc.perform(get("/api/documents/download/{documentId}", documentId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"testFile.txt\""))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(fileData));
    }

    @Test
    void testDownloadDocument_NotFound() throws Exception {
        Long documentId = 1L;

        // Simula que el documento no fue encontrado
        when(documentService.getDocumentById(documentId)).thenReturn(null);

        mockMvc.perform(get("/api/documents/download/{documentId}", documentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllDocuments() throws Exception {
        // Configura una lista de documentos simulada
        List<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity doc1 = new DocumentEntity();
        doc1.setFileName("file1.txt");
        DocumentEntity doc2 = new DocumentEntity();
        doc2.setFileName("file2.txt");
        documents.add(doc1);
        documents.add(doc2);

        // Simula la obtención de todos los documentos
        when(documentService.getAllDocuments()).thenReturn(documents);

        mockMvc.perform(get("/api/documents/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fileName").value("file1.txt"))
                .andExpect(jsonPath("$[1].fileName").value("file2.txt"));
    }

    @Test
    void testGetDocumentsByCreditId_WithResults() throws Exception {
        Long creditId = 1L;

        List<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity doc = new DocumentEntity();
        doc.setFileName("file1.txt");
        documents.add(doc);

        // Simula la obtención de documentos por creditId
        when(documentService.getDocumentsByCreditId(creditId)).thenReturn(documents);

        mockMvc.perform(get("/api/documents/byCredit/{creditId}", creditId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fileName").value("file1.txt"));
    }

    @Test
    void testGetDocumentsByCreditId_NoResults() throws Exception {
        Long creditId = 1L;

        // Simula una lista vacía de documentos para el crédito
        when(documentService.getDocumentsByCreditId(creditId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/documents/byCredit/{creditId}", creditId))
                .andExpect(status().isNoContent());
    }
}
