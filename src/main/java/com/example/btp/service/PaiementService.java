package com.example.btp.service;

import com.example.btp.model.Devis;
import com.example.btp.model.Paiement;
import com.example.btp.repository.PaiementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaiementService {

    @Autowired
    PaiementRepository paiementRepository;

    @Transactional
    public void save(Devis devis, Date date, double prix, String refPaiement){
        Paiement paiement= new Paiement(devis,date,prix,refPaiement);
        paiementRepository.save(paiement);
    }

    /*@Transactional
    public int save(Devis devis, Date date, double prix, String refPaiement){
        int val=0;
        if(prix<=devis.getRestePaye() && prix>0){
            Paiement paiement= new Paiement(devis,date,prix,refPaiement);
            paiementRepository.save(paiement);
            val= 10;
        }if(prix <0) {
            val= 0;
        }if(prix >devis.getRestePaye() && devis.getRestePaye() >0){
            prix= prix-devis.getRestePaye();
            Paiement paiement= new Paiement(devis,date,prix,refPaiement);
            paiementRepository.save(paiement);
            val= 20;
        }
        return val;
    }*/


    public double getMontantPaye(int idDevis){
        Double montant= paiementRepository.montantPayeDevis(idDevis);
        if(montant==null){
            montant=0.0;
        }
        return montant;
    }

    public List<Paiement> getPaiementsByDevis(Devis devis){
        List<Paiement> paiements= paiementRepository.getPaiementsByDevis(devis);
        return paiements;
    }

    public double totalPaiementEffectue(){
        Double total= paiementRepository.totalPaiementEffectué();
        if(total==null){
            total=0.0;
        }
        return total;
    };

    public Paiement checkRefExist(String ref){
        return paiementRepository.findPaiementByRefPaiement(ref);
    }

}
