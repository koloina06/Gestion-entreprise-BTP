package com.example.btp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevisMontant {
    private int idDevis;

    private double montantPaye;

    public DevisMontant(int idDevis, double montantPaye){
        this.setIdDevis(idDevis);
        this.setMontantPaye(montantPaye);
    }
}
