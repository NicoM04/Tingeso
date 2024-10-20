package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String fileName;  // Nombre del archivo
    private String filePath;  // Ruta completa del archivo en el sistema de archivos

    private Long creditId;  // Relación con la entidad CreditEntity (si el documento está relacionado con un crédito)






}
