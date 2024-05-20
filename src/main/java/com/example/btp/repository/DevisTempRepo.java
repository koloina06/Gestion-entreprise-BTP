package com.example.btp.repository;

import com.example.btp.model.DevisTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevisTempRepo extends JpaRepository<DevisTemp,Integer> {

    public List<DevisTemp> findAll();
}
