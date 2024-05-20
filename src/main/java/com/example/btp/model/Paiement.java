package com.example.btp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Entity
@Setter
@Table(name = "paiement")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpaiement")
    private int idPaiement;

    @Column(name = "refpaiement")
    private String refPaiement;

    @ManyToOne
    @JoinColumn(name = "iddevis")
    private Devis devis;

    @Column(name = "datepaiement")
    private Date datePaiement;

    @Column(name = "montant")
    private double montant;

    public Paiement(){}

    public Paiement(Devis devis, Date datePaiement, double montant, String refPaiement){
        this.setDevis(devis);
        this.setDatePaiement(datePaiement);
        this.setMontant(montant);
        this.setRefPaiement(refPaiement);
    }
}
