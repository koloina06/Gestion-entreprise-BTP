package com.example.btp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErreurImport {

    private int numLigne;

    private String colonne;

    private String erreur;

    public ErreurImport(int numLigne,String colonne, String erreur){
        this.setNumLigne(numLigne);
        this.setColonne(colonne);
        this.setErreur(erreur);
    }
}
