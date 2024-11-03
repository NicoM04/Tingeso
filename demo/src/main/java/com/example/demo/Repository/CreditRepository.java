package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entities
        .CreditEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<CreditEntity, Long> {
    CreditEntity findById(long id);
    List<CreditEntity> findByIdClient(Long idClient); // Cambiado a idClient// Método para encontrar créditos por ID de usuario
    List<CreditEntity> findByState(int state);
    List<CreditEntity> findByTypeLoan(int type);
    //agregar un findby por cada wea?, despues viene services

}