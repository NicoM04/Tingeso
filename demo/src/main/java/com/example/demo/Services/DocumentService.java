package com.example.demo.Services;
import com.example.demo.Entities.CreditEntity;
import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Repository.CreditRepository;
import com.example.demo.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    private static final String UPLOAD_DIR = "C:\\Users\\nicol\\Desktop\\Tingeso";

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private CreditRepository creditRepository;

    /**
     * Guarda el archivo en el sistema de archivos y la ruta en la base de datos.
     * @param fileName Nombre del archivo subido.
     * @param creditId El ID del crédito asociado al documento.
     * @return La entidad DocumentEntity guardada en la base de datos.
     */
    public DocumentEntity saveDocument(String fileName, Long creditId) throws IOException {
        // Definir la ruta completa del archivo, asegurándote de usar el separador adecuado
        Path filePath = Paths.get(UPLOAD_DIR + "\\" + fileName);
        // Crear la entidad DocumentEntity
        DocumentEntity document = new DocumentEntity();
        document.setFileName(fileName); // Aquí el nombre del archivo se guarda sin cambios
        document.setFilePath(filePath.toString()); // Aquí se guarda la ruta correcta
        document.setCreditId(creditId);

        // Guardar la entidad en la base de datos
        return documentRepository.save(document);
    }


    /**
     * Recupera un documento por su ID.
     * @param documentId ID del documento.
     * @return La entidad DocumentEntity encontrada.
     */
    public DocumentEntity getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado."));
    }

    // Método para obtener todos los documentos
    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAll();
    }

    public List<DocumentEntity> getDocumentsByCreditId(Long creditId) {
        return documentRepository.findByCreditId(creditId);
    }
}
