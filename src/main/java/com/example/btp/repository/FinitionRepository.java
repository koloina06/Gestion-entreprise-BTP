package com.example.btp.repository;

import com.example.btp.model.Admin;
import com.example.btp.model.Finition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinitionRepository extends JpaRepository<Finition,Integer> {

    @Query("select finition from Finition finition where finition.etat=0")
    public Page<Finition> findAll(Pageable pageable);

    @Query("select finition from Finition finition where finition.idFinition= :idFinition and finition.etat=0")
    public Finition getFinitionByIdFinition(int idFinition);
}
