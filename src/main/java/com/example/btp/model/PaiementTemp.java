package com.example.btp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaiementTemp {

    private String refDevis;

    private String refPaiement;

    private String datePaiement;

    private String montant;

    public PaiementTemp(String refDevis, String refPaiement, String datePaiement, String montant){
        this.setRefDevis(refDevis);
        this.setRefPaiement(refPaiement);
        this.setDatePaiement(datePaiement);
        this.setMontant(montant);
    }
}
