package com.example.btp.repository;

import com.example.btp.model.Maison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaisonRepository extends JpaRepository<Maison,Integer> {

    @Query("select maison from Maison maison where maison.etat=0")
    public List<Maison> findAll();

    public Maison findMaisonByIdMaison(int idMaison);

}
