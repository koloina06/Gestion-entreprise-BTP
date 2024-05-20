package com.example.btp.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaisonTravauxService {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void saveFromCsv(){
        entityManager.createNativeQuery("INSERT INTO maisontravaux(idmaison,idtypetravaux,etat)  " +
                "SELECT m.idmaison,t.idtypetravaux,0 " +
                "FROM maisontravauxtemp mtp " +
                "JOIN maison m on mtp.typemaison=m.typeMaison " +
                "JOIN typetravaux t on mtp.typeTravaux=t.nomTypeTravaux "+
                "AND CAST(mtp.prixunitaire AS double precision) = t.prixunitaire " +
                "AND CAST(mtp.quantite AS double precision) = t.quantite;").executeUpdate();
    }
}