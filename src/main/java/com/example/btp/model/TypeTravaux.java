package com.example.btp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "typetravaux")
public class TypeTravaux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtypetravaux")
    private int idTypeTravaux;

    @NotBlank(message = "Le code ne peut pas être vide")
    @Column(name = "code")
    private String code;

    @NotBlank(message = "Le type ne peut pas être vide")
    @Column(name = "nomtypetravaux")
    private String nomTypeTravaux;

    @ManyToOne
    @JoinColumn(name = "idunite")
    private Unite unite;

    @Min(value = 1, message = "La quantite ne peut pas etre negative")
    @Column(name = "quantite")
    private double quantite;

    @Min(value = 1, message = "Le prix unitaire ne peut pas etre negatif")
    @Column(name = "prixunitaire")
    private double prixUnitaire;

    @Transient
    private double prixTotal;


}
