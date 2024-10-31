package com.example.demo.Controller;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private static final String UPLOAD_DIR = "C:\\Users\\Nicolas Morales\\Desktop\\repositorio\\Tingeso\\files\\";

    /**
     * Endpoint para subir un archivo.
     * @param files Archivo subido por el cliente.
     * @param creditId ID del crédito asociado (opcional).
     * @return Mensaje de éxito o error.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocuments(@RequestParam("files") MultipartFile[] files,
                                                  @RequestParam(value = "creditId", required = false) Long creditId) {
        StringBuilder responseMessage = new StringBuilder();
        try {
            // Verificar que al menos un archivo no esté vacío
            if (files.length == 0) {
                return ResponseEntity.badRequest().body("No se han subido archivos.");
            }

            for (MultipartFile file : files) {
                // Verificar que cada archivo no esté vacío
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body("Uno o más archivos están vacíos.");
                }

                // Obtener el nombre del archivo
                String fileName = file.getOriginalFilename();

                // Definir la ruta completa donde se almacenará el archivo
                Path filePath = Paths.get(UPLOAD_DIR + fileName);

                // Guardar el archivo en la carpeta local
                Files.copy(file.getInputStream(), filePath);

                // Guardar la ruta del archivo y la información del crédito en la base de datos
                documentService.saveDocument(fileName, creditId);
                responseMessage.append("Archivo subido correctamente: ").append(filePath.toString()).append("\n");
            }

            return ResponseEntity.ok(responseMessage.toString());

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir los archivos: " + e.getMessage());
        }
    }


    /**
     * Endpoint para descargar un archivo.
     * @param documentId ID del documento a descargar.
     * @return El archivo como recurso.
     */
    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        try {
            // 1. Recuperar el documento de la base de datos
            DocumentEntity document = documentService.getDocumentById(documentId);
            Path filePath = Paths.get(document.getFilePath());

            // 2. Cargar el archivo como recurso
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new IOException("Archivo no encontrado: " + filePath);
            }

            // 3. Devolver el archivo con los encabezados correctos para descargar
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    // Endpoint para obtener todos los documentos
    @GetMapping("/all")
    public ResponseEntity<List<DocumentEntity>> getAllDocuments() {
        List<DocumentEntity> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }
}
