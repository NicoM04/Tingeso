package com.example.demo.Repository;

import com.example.demo.Entities.DocumentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
        // Limpiamos el repositorio antes de cada prueba para evitar datos residuales
        documentRepository.deleteAll();
    }

    @Test
    void testSaveDocument() {
        // Crear una instancia de DocumentEntity
        DocumentEntity document = new DocumentEntity();
        document.setFileName("testFile.txt");
        document.setFileData("test content".getBytes());
        document.setCreditId(1L);

        // Guardar el documento en el repositorio
        DocumentEntity savedDocument = documentRepository.save(document);

        // Verificar que se ha guardado correctamente
        assertThat(savedDocument.getId()).isNotNull();
        assertThat(savedDocument.getFileName()).isEqualTo("testFile.txt");
        assertThat(savedDocument.getCreditId()).isEqualTo(1L);
    }

    @Test
    void testFindByCreditId_Found() {
        // Crear y guardar documentos con diferentes creditIds
        DocumentEntity document1 = new DocumentEntity();
        document1.setFileName("file1.txt");
        document1.setFileData("content1".getBytes());
        document1.setCreditId(1L);

        DocumentEntity document2 = new DocumentEntity();
        document2.setFileName("file2.txt");
        document2.setFileData("content2".getBytes());
        document2.setCreditId(2L);

        documentRepository.save(document1);
        documentRepository.save(document2);

        // Buscar documentos por creditId = 1L
        List<DocumentEntity> documentsByCreditId = documentRepository.findByCreditId(1L);

        // Verificar que se encontró el documento correcto
        assertThat(documentsByCreditId).hasSize(1);
        assertThat(documentsByCreditId.get(0).getFileName()).isEqualTo("file1.txt");
        assertThat(documentsByCreditId.get(0).getCreditId()).isEqualTo(1L);
    }

    @Test
    void testFindByCreditId_NotFound() {
        // Crear y guardar un documento con un creditId distinto
        DocumentEntity document = new DocumentEntity();
        document.setFileName("file.txt");
        document.setFileData("content".getBytes());
        document.setCreditId(1L);

        documentRepository.save(document);

        // Buscar documentos por un creditId inexistente
        List<DocumentEntity> documentsByCreditId = documentRepository.findByCreditId(2L);

        // Verificar que no se encontraron documentos
        assertThat(documentsByCreditId).isEmpty();
    }

    @Test
    void testDeleteDocument() {
        // Crear y guardar un documento
        DocumentEntity document = new DocumentEntity();
        document.setFileName("fileToDelete.txt");
        document.setFileData("data to delete".getBytes());
        document.setCreditId(1L);

        DocumentEntity savedDocument = documentRepository.save(document);

        // Borrar el documento
        documentRepository.delete(savedDocument);

        // Verificar que el documento ha sido eliminado
        assertThat(documentRepository.findById(savedDocument.getId())).isEmpty();
    }

    @Test
    void testFindAllDocuments() {
        // Crear y guardar varios documentos
        DocumentEntity document1 = new DocumentEntity();
        document1.setFileName("file1.txt");
        document1.setFileData("content1".getBytes());
        document1.setCreditId(1L);

        DocumentEntity document2 = new DocumentEntity();
        document2.setFileName("file2.txt");
        document2.setFileData("content2".getBytes());
        document2.setCreditId(2L);

        documentRepository.save(document1);
        documentRepository.save(document2);

        // Obtener todos los documentos del repositorio
        List<DocumentEntity> allDocuments = documentRepository.findAll();

        // Verificar que se encontraron los documentos guardados
        assertThat(allDocuments).hasSize(2);
        assertThat(allDocuments).extracting(DocumentEntity::getFileName)
                .containsExactlyInAnyOrder("file1.txt", "file2.txt");
    }
}
