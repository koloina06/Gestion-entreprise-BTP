package com.example.btp.service;

import com.example.btp.model.Finition;
import com.example.btp.model.Maison;
import com.example.btp.repository.FinitionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinitionService {

    @Autowired
    FinitionRepository finitionRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Finition> list(){
        List<Finition> finitions= finitionRepository.findAll();
        return finitions;
    }

    public Page<Finition> listpaginee(Pageable pageable){
        Page<Finition> finitions= finitionRepository.findAll(pageable);
        return finitions;
    }

    public Finition findById(int idFinition){
        Finition finition= finitionRepository.getFinitionByIdFinition(idFinition);
        return finition;
    }

    @Transactional
    public void saveFromCsv(){
        entityManager.createNativeQuery("INSERT INTO finition(typeFinition,augmentation,etat)  " +
                "SELECT finition, CAST(tauxfinition AS double precision),0 " +
                "FROM devistemp " +
                "group by finition,tauxfinition;").executeUpdate();
    }

    public void updatePourcentage(int idFinition, double pourcentage){
        Finition finition= this.findById(idFinition);
        finition.setAugmentation(pourcentage);
        finitionRepository.save(finition);
    }

    public double montantFinition(Finition finition, double prixTravaux){
        double augmentation= (prixTravaux* finition.getAugmentation())/100;
        return augmentation;
    }
}
