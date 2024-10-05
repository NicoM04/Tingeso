package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userejecutive")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEjecutiveEntity extends UserEntity {

    private String executiveLevel;

}
