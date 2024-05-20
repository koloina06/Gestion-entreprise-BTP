package com.example.btp.service;

import com.example.btp.model.Admin;
import com.example.btp.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    public Admin login(String email, String mdp){
        Admin admin= adminRepository.findAdminByEmailAndMdp(email.trim(),mdp.trim());
        return admin;
    }
}
