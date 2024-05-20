package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idadmin")
    private int idAdmin;

    @Column(name = "nomadmin")
    private String nomAdmin;

    @Column(name = "email")
    private String email;

    @Column(name = "mdp")
    private String mdp;

    public Admin(){}
}
