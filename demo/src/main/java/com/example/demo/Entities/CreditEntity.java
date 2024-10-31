package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private int typeLoan;
    private int amount;
    private int dueDate;  // years
    private double interestRate; //years
    private double monthlyPayment;  // Cuota mensual calculada
    private int state; // "Simulacion", "Solicitado", "Aprobado", "Rechazado", "Activo"
    private int idClient;

}
