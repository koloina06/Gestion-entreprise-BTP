package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "maisontravaux")
public class MaisonTravaux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmaisontravaux")
    private int idMaisonTravaux;

    @ManyToOne
    @JoinColumn(name = "idmaison")
    private Maison maison;

    @ManyToOne
    @JoinColumn(name = "idtypetravaux")
    private TypeTravaux typeTravaux;

    @Column(name = "etat")
    private int etat;
}
