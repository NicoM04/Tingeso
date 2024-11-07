package com.example.demo.Controller;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin("*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /**
     * Endpoint para subir archivos y guardarlos como bytes en la base de datos.
     *
     * @param files Archivos subidos por el cliente.
     * @param creditId ID del crédito asociado (opcional).
     * @return Mensaje de éxito o error.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocuments(@RequestParam("files") MultipartFile[] files,
                                                  @RequestParam(value = "creditId", required = false) Long creditId) {
        StringBuilder responseMessage = new StringBuilder();

        try {
            if (files.length == 0) {
                return ResponseEntity.badRequest().body("No se han subido archivos.");
            }

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body("Uno o más archivos están vacíos.");
                }

                // Guardar el archivo como bytes en la base de datos
                documentService.saveDocument(file, creditId);
                responseMessage.append("Archivo subido correctamente: ").append(file.getOriginalFilename()).append("\n");
            }

            return ResponseEntity.ok(responseMessage.toString());

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir los archivos: " + e.getMessage());
        }
    }

    /**
     * Endpoint para descargar un archivo por su ID, retornando los bytes almacenados.
     *
     * @param documentId ID del documento a descargar.
     * @return El archivo como recurso en bytes.
     */
    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        DocumentEntity document = documentService.getDocumentById(documentId);

        if (document == null) {
            return ResponseEntity.notFound().build(); // Documento no encontrado
        }

        // Crear recurso de bytes para descargar
        ByteArrayResource resource = new ByteArrayResource(document.getFileData());

        // Forzar el tipo MIME a PDF y establecer la extensión si no está en el nombre del archivo
        String contentType = "application/pdf";
        String fileName = document.getFileName().toLowerCase().endsWith(".pdf") ?
                document.getFileName() :
                document.getFileName() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(document.getFileData().length)
                .body(resource);
    }


    /**
     * Endpoint para obtener todos los documentos.
     *
     * @return Lista de todos los documentos almacenados.
     */
    @GetMapping("/all")
    public ResponseEntity<List<DocumentEntity>> getAllDocuments() {
        List<DocumentEntity> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    /**
     * Endpoint para obtener documentos por el ID del crédito.
     *
     * @param creditId ID del crédito asociado.
     * @return Lista de documentos asociados al crédito.
     */
    @GetMapping("/byCredit/{creditId}")
    public ResponseEntity<List<DocumentEntity>> getDocumentsByCreditId(@PathVariable("creditId") Long creditId) {
        List<DocumentEntity> documents = documentService.getDocumentsByCreditId(creditId);

        if (documents.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content si no se encuentran documentos
        }

        return ResponseEntity.ok(documents);
    }
}