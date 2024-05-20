package com.example.btp.service;

import com.example.btp.model.Maison;
import com.example.btp.repository.MaisonRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaisonService {

    @Autowired
    MaisonRepository maisonRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Maison> list(){
        List<Maison> maisons= maisonRepository.findAll();
        for(int i=0; i<maisons.size(); i++){
            List<String> desc= new ArrayList<>();
            if(maisons.get(i).getDescription().contains(",")){
                String[] d= maisons.get(i).getDescription().split(",");
                for(int j=0; j<d.length; j++){
                    desc.add(d[j]);
                }
                maisons.get(i).setDesc(desc);
            }else{
                maisons.get(i).setDesc(desc);
            }
        }
        return maisons;
    }

    public Maison findById(int idMaison){
        Maison maison= maisonRepository.findMaisonByIdMaison(idMaison);
        return maison;
    }

    @Transactional
    public void saveFromCsv(){
        entityManager.createNativeQuery("INSERT INTO maison(typeMaison,descriptions,duree,surface,etat)  " +
                "SELECT typemaison,descriptions, CAST(dureetravaux AS double precision),CAST(surface AS double precision),0 " +
                "FROM maisontravauxtemp " +
                "group by typemaison,descriptions,dureetravaux,surface").executeUpdate();
    }
}
