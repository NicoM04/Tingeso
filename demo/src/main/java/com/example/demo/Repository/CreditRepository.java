package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entities
        .CreditEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CreditRepository extends JpaRepository<CreditEntity, Long> {
    CreditEntity findById(long id);
    List<CreditEntity> findByState(int state);
    List<CreditEntity> findByTypeLoan(int type);
    //agregar un findby por cada wea?, despues viene services

}
