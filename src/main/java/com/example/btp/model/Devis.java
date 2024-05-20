package com.example.btp.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Entity
@Setter
@Table(name = "devis")
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddevis")
    private int idDevis;

    @Column(name = "refdevis")
    private String refDevis;

    @Column(name = "datedevis")
    private Date dateDevis;

    @ManyToOne
    @JoinColumn(name = "idmaison")
    private Maison maison;

    @ManyToOne
    @JoinColumn(name = "idfinition")
    private Finition finition;

    @Column(name = "datedebut")
    private Date dateDebut;

    @Nullable
    @Column(name = "datefin")
    private Date dateFin;

    @Column(name = "numeroclient")
    private String numero;

    @Column(name = "augmentation")
    private double augmentation;

    @Column(name = "prixtotaldevis")
    private double prixTotalDevis;

    @Column(name = "lieu")
    private String lieu;

    @Transient
    private double restePaye;

    @Transient
    private double montantPaye;

    @Transient
    private double pourcentage;

    public Devis(){}
    public Devis (Maison maison, Finition finition, Date dateDebut, Date dateFin, String numero, double augmentation, double prixTotalDevis, Date dateDevis, String lieu, String refDevis){
        this.setMaison(maison);
        this.setFinition(finition);
        this.setDateDebut(dateDebut);
        this.setDateFin(dateFin);
        this.setNumero(numero);
        this.setAugmentation(augmentation);
        this.setPrixTotalDevis(prixTotalDevis);
        this.setDateDevis(dateDevis);
        this.setLieu(lieu);
        this.setRefDevis(refDevis);
    }


}
