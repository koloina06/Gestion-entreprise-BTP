package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "maisontravauxtemp")
public class MaisonTravauxTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "numligne")
    private int numLigne;

    @Column(name = "typemaison")
    private String typeMaison;

    @Column(name = "descriptions")
    private String description;

    @Column(name = "surface")
    private String surface;

    @Column(name = "codetravaux")
    private String codetravaux;

    @Column(name = "typetravaux")
    private String typetravaux;

    @Column(name = "unite")
    private String unite;

    @Column(name = "prixunitaire")
    private String prixUnitaire;

    @Column(name = "quantite")
    private String quantite;

    @Column(name = "dureetravaux")
    private String dureeTravaux;

    public MaisonTravauxTemp(){}

    public MaisonTravauxTemp(int numLigne, String typeMaison, String descriptions, String surface, String codetravaux, String typetravaux, String unite, String prixUnitaire, String quantite, String dureeTravaux){
        this.setNumLigne(numLigne);
        this.setTypeMaison(typeMaison);
        this.setDescription(descriptions);
        this.setSurface(surface);
        this.setCodetravaux(codetravaux);
        this.setTypetravaux(typetravaux);
        this.setUnite(unite);
        this.setPrixUnitaire(prixUnitaire);
        this.setQuantite(quantite);
        this.setDureeTravaux(dureeTravaux);
    }
}
