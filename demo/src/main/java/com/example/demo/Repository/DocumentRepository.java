package com.example.demo.Repository;
import com.example.demo.Entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    // Consulta personalizada para encontrar documentos por el ID del crédito
    List<DocumentEntity> findByCreditId(Long creditId);
    // Puedes agregar métodos personalizados si es necesario
}
