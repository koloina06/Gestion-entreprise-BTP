package com.example.btp.repository;

import com.example.btp.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

    public Admin findAdminByEmailAndMdp(String email, String mdp);
}
