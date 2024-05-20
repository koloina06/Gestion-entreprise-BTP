package com.example.btp.repository;

import com.example.btp.model.MaisonTravauxTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaisonTravauxTempRepo extends JpaRepository<MaisonTravauxTemp,Integer> {

    public List<MaisonTravauxTemp> findAll();
}
