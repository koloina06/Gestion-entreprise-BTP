package com.example.btp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MontantMois {
    private double montantTotalDevis;

    private int mois;

    public MontantMois(double montantTotalDevis, int mois){
        this.setMontantTotalDevis(montantTotalDevis);
        this.setMois(mois);
    }
}
