package com.example.btp.repository;

import com.example.btp.model.DetailsDevis;
import com.example.btp.model.Devis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailsDevisRepository extends JpaRepository<DetailsDevis,Integer> {

    public List<DetailsDevis> findDetailsDevisByDevis(Devis devis);
}
