package com.example.btp.service;

import com.example.btp.model.DetailsDevis;
import com.example.btp.model.Devis;
import com.example.btp.model.TypeTravaux;
import com.example.btp.repository.DetailsDevisRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DetailsDevisService {

    @Autowired
    DetailsDevisRepository detailsDevisRepository;

    @Transactional
    public void save(Devis devis, TypeTravaux typeTravaux){
        DetailsDevis detailsDevis= new DetailsDevis(devis,typeTravaux.getCode(), typeTravaux.getNomTypeTravaux(),typeTravaux.getUnite(), typeTravaux.getQuantite(), typeTravaux.getPrixUnitaire(), typeTravaux.getPrixTotal());
        detailsDevisRepository.save(detailsDevis);
    }

    public List<DetailsDevis> getByDevis(Devis devis){
        List<DetailsDevis> detailsDevis= detailsDevisRepository.findDetailsDevisByDevis(devis);
        return detailsDevis;
    }
}
