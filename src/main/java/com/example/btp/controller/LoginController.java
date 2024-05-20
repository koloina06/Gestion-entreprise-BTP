package com.example.btp.controller;

import com.example.btp.model.Admin;
import com.example.btp.model.Client;
import com.example.btp.service.AdminService;
import com.example.btp.service.TraitementService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;

import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping(path = "/")
public class LoginController {

    private final AdminService adminService;

    @Autowired
    public LoginController(AdminService adminService){
        this.adminService=adminService;
    }
    @GetMapping("admin")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model)
    {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @GetMapping("")
    public String loginPageClient(Model model)
    {
        model.addAttribute("client", new Client());
        return "loginClient";
    }

    @PostMapping("loginClient")
    public String checkLoginClient(HttpSession session, @RequestParam String telephone, @Valid Client c, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("client",c);
            model.addAttribute("errors", result.getAllErrors());
            return "loginClient";
        }
        Client client= new Client(telephone);
        session.setAttribute("client", client);
        return "redirect:/devis/demandeDevis";
    }

    @PostMapping ("login")
    public String login(HttpSession session, @RequestParam String email, @RequestParam String mdp, RedirectAttributes redirectAttributes){
        Admin admin= adminService.login(email,mdp);
        if(admin== null){
            redirectAttributes.addAttribute("error", "email ou mot de passe invalide");
            return "redirect:/admin";
        }else{
            session.setAttribute("admin", admin);
            return "redirect:/devis/listeEnCours";
        }
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("errorPage")
    public String errorPage(){
        return "errorPage";
    }


}
