package com.example.btp.service;

import com.example.btp.model.Unite;
import com.example.btp.repository.UniteRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniteService {

    @Autowired
    UniteRepository uniteRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Unite> list(){
        List<Unite> unites= uniteRepository.findAll();
        return unites;
    };

    public Unite findById(int idUnite){
        Unite unite= uniteRepository.findUniteByIdUnite(idUnite);
        return unite;
    }

    @Transactional
    public void saveFromCsv(){
        entityManager.createNativeQuery("INSERT INTO unite(nomunite,etat) " +
                "SELECT unite,0 " +
                "FROM maisontravauxtemp " +
                "group by unite").executeUpdate();
    }
}
