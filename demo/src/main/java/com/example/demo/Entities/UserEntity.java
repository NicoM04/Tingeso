package com.example.demo.Entities;

import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@Inheritance(strategy = InheritanceType.JOINED)

@NoArgsConstructor
@AllArgsConstructor
public abstract class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    public Long id;

    public String name;
    public String rut;
    public String mail;
    public String phoneN;



}
