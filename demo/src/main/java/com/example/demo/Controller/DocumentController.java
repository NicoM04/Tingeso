package com.example.demo.Controller;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String UPLOAD_DIR = "C:\\Users\\Nicolas Morales\\Desktop\\repositorio\\Tingeso";

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
                Path filePath = Paths.get(UPLOAD_DIR + "\\" + fileName);





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

            if (document == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Documento no encontrado
            }

            Path filePath = Paths.get(document.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            // 2. Comprobar si el recurso existe
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Archivo no encontrado
            }

            // 3. Determinar el tipo MIME del archivo
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Tipo por defecto si no se puede determinar
            }

            // 4. Obtener el nombre del archivo con la extensión
            String fileName = document.getFileName(); // Asegúrate de que esto tenga la extensión correcta

            // 5. Devolver el archivo con los encabezados correctos para descargar
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType(contentType)) // Usar el tipo MIME correcto
                    .body(resource);

        } catch (IOException e) {
            // Log el error para depuración
            e.printStackTrace(); // Agrega un logger en producción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





    // Endpoint para obtener todos los documentos
    @GetMapping("/all")
    public ResponseEntity<List<DocumentEntity>> getAllDocuments() {
        List<DocumentEntity> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }
    /**
     * Endpoint para obtener documentos por el ID del crédito.
     * @param creditId ID del crédito asociado.
     * @return Lista de documentos asociados al crédito.
     */
    @GetMapping("/byCredit/{creditId}")
    public ResponseEntity<List<DocumentEntity>> getDocumentsByCreditId(@PathVariable Long creditId) {
        List<DocumentEntity> documents = documentService.getDocumentsByCreditId(creditId);

        if (documents.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content si no se encuentran documentos
        }

        return ResponseEntity.ok(documents);
    }
}
