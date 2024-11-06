package com.example.demo.Services;
import com.example.demo.Entities.CreditEntity;
import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Repository.CreditRepository;
import com.example.demo.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CreditRepository creditRepository;

    /**
     * Guarda el archivo como bytes en la base de datos junto con su información.
     *
     * @param file Archivo subido por el usuario.
     * @param creditId ID del crédito asociado al documento.
     * @return La entidad DocumentEntity guardada en la base de datos.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public DocumentEntity saveDocument(MultipartFile file, Long creditId) throws IOException {
        DocumentEntity document = new DocumentEntity();

        document.setFileName(file.getOriginalFilename()); // Guardar el nombre del archivo
        document.setFileData(file.getBytes()); // Guardar el contenido del archivo en bytes
        document.setCreditId(creditId);

        // Guardar la entidad en la base de datos
        return documentRepository.save(document);
    }

    /**
     * Recupera un documento por su ID.
     *
     * @param documentId ID del documento.
     * @return La entidad DocumentEntity encontrada.
     */
    public DocumentEntity getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado."));
    }

    /**
     * Obtiene todos los documentos.
     *
     * @return Lista de todos los documentos almacenados.
     */
    public List<DocumentEntity> getAllDocuments() {
        return documentRepository.findAll();
    }

    /**
     * Obtiene los documentos asociados a un ID de crédito.
     *
     * @param creditId ID del crédito asociado.
     * @return Lista de documentos asociados al crédito.
     */
    public List<DocumentEntity> getDocumentsByCreditId(Long creditId) {
        return documentRepository.findByCreditId(creditId);
    }

    /**
     * Obtiene los bytes de un archivo almacenado en la base de datos a partir de su ID.
     *
     * @param documentId ID del documento.
     * @return Arreglo de bytes del archivo.
     */
    public byte[] getDocumentData(Long documentId) {
        DocumentEntity document = getDocumentById(documentId);
        return document.getFileData();  // Devolver los bytes del archivo
    }
}
