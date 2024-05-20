package com.example.btp.repository;

import com.example.btp.model.Unite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniteRepository extends JpaRepository<Unite,Integer> {

    public List<Unite> findAll();

    public Unite findUniteByIdUnite(int idUnite);
}
