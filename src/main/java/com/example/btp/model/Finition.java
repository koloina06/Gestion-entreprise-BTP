package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "finition")
public class Finition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfinition")
    private int idFinition;

    @Column(name = "typefinition")
    private String typeFinition;

    @Column(name = "augmentation")
    private double augmentation;

    @Column(name = "etat")
    private int etat;

    @Transient
    private double montant;
}
