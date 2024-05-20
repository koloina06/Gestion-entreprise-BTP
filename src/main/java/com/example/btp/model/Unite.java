package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "unite")
public class Unite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idunite")
    private int idUnite;

    @Column(name = "nomunite")
    private String nomUnite;

    @Column(name = "etat")
    private int etat;

    public Unite(){}
}
