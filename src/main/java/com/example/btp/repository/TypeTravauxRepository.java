package com.example.btp.repository;

import com.example.btp.model.TypeTravaux;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeTravauxRepository extends JpaRepository<TypeTravaux,Integer> {

    public Page<TypeTravaux> findAll(Pageable pageable);

    public TypeTravaux findTypeTravauxByIdTypeTravaux(int idTypeTravaux);
}
