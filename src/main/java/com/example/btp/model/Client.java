package com.example.btp.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client {

    @Pattern(regexp = "^(032|033|034)\\d{7}$", message = "Numéro de téléphone invalide")
    private String telephone;

    public Client(){}

    public Client(String telephone){
        this.setTelephone(telephone);
    }
}
