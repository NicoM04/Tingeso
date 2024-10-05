package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//@Table(name = "userclient")


@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserClientEntity extends UserEntity {


    private double monthlyIncome;
}
