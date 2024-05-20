package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Setter
@Table(name = "maison")
public class Maison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmaison")
    private int idMaison;

    @Column(name = "typemaison")
    private String typeMaison;

    @Column(name = "descriptions")
    private String description;

    @Column(name = "duree")
    private double duree;

    @Column(name = "etat")
    private int etat;

    @Column(name = "surface")
    private double surface;

    @Transient
    private List<String> desc;

    public Maison(){}

    public Maison(int idMaison, String typeMaison, String description, double duree, int etat){
        this.setIdMaison(idMaison);
        this.setTypeMaison(typeMaison);
        this.setDescription(description);
        this.setDuree(duree);
        this.setEtat(etat);
    }
}
