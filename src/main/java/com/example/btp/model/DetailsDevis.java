package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "detailsdevis")
public class DetailsDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetailsdevis")
    private int idDetailsDevis;

    @ManyToOne
    @JoinColumn(name = "iddevis")
    private Devis devis;

    @Column(name = "code")
    private String code;

    @Column(name = "nomtypetravaux")
    private String nomTypeTravaux;

    @ManyToOne
    @JoinColumn(name = "idunite")
    private Unite unite;

    @Column(name = "quantite")
    private double quantite;

    @Column(name = "prixunitaire")
    private double prixUnitaire;

    @Column(name = "prixtotal")
    private double prixTotal;

    public DetailsDevis(){}

    public DetailsDevis(Devis devis, String code, String nomTypeTravaux, Unite unite, double quantite, double prixUnitaire, double prixTotal){
        this.setDevis(devis);
        this.setCode(code);
        this.setNomTypeTravaux(nomTypeTravaux);
        this.setUnite(unite);
        this.setQuantite(quantite);
        this.setPrixUnitaire(prixUnitaire);
        this.setPrixTotal(prixTotal);
    }
}
