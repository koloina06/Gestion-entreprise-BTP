package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "devistemp")
public class DevisTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "numligne")
    private int numLigne;

    @Column(name = "client")
    private String client;

    @Column(name = "refdevis")
    private String refDevis;

    @Column(name = "typemaison")
    private String typeMaison;

    @Column(name = "finition")
    private String finition;

    @Column(name = "tauxfinition")
    private String tauxFinition;

    @Column(name = "datedevis")
    private String dateDevis;

    @Column(name = "datedebut")
    private String dateDebut;

    @Column(name = "lieu")
    private String lieu;

    public DevisTemp(){}

    public DevisTemp(int numLigne,String client,String refDevis,String typeMaison,String finition,String tauxFinition,String dateDevis, String dateDebut, String lieu){
        this.setNumLigne(numLigne);
        this.setClient(client);
        this.setRefDevis(refDevis);
        this.setTypeMaison(typeMaison);
        this.setFinition(finition);
        this.setTauxFinition(tauxFinition);
        this.setDateDevis(dateDevis);
        this.setDateDebut(dateDebut);
        this.setLieu(lieu);
    }
}
