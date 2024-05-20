package com.example.btp.controller;

import com.example.btp.model.Finition;
import com.example.btp.service.FinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/finition")
public class FinitionController {

    private final FinitionService finitionService;

    @Autowired
    public FinitionController(FinitionService finitionService){
        this.finitionService=finitionService;
    }

    @GetMapping("liste")
    public String liste(Model model,@RequestParam(defaultValue = "0") int page){
        int size=5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Finition> finitions= finitionService.listpaginee(pageable);
        model.addAttribute("finitions",finitions);
        return "listeFinition";
    }

    @GetMapping("toUpdate")
    public String toUpdate(Model model, @RequestParam(required = false) Integer idFinition){
        if(idFinition != null){
            idFinition=idFinition;
        }
        if(model.containsAttribute("succes")){
            String succes= (String) model.getAttribute("succes");
            model.addAttribute("succes",succes);
            idFinition= (Integer) model.getAttribute("idFinition");
         }
        if(model.containsAttribute("erreur")){
            String erreur= (String) model.getAttribute("erreur");
            model.addAttribute("erreur",erreur);
            idFinition= (Integer) model.getAttribute("idFinition");
        }
        Finition finition=finitionService.findById(idFinition);
        model.addAttribute("finition",finition);
        return "updateFinition";
    }

    @PostMapping("update")
    public String update(Model model, @RequestParam int idFinition, @RequestParam String pourcentage, RedirectAttributes redirectAttributes){
        String erreurDouble= null;
        double pourcentageDouble= -1;
        try {
            pourcentageDouble = Double.parseDouble(pourcentage);
        } catch (NumberFormatException e) {
            erreurDouble= "format invalide";
        }
        if(pourcentageDouble >= 0 && erreurDouble==null){
            finitionService.updatePourcentage(idFinition,pourcentageDouble);
            redirectAttributes.addFlashAttribute("succes", "Modifié avec succès");
        }else {
            redirectAttributes.addFlashAttribute("erreur", "Erreur lors de la modification");
        }
        redirectAttributes.addFlashAttribute("idFinition",idFinition);
        redirectAttributes.addFlashAttribute("succes", "Modifié avec succès");
        return "redirect:/finition/toUpdate";
    }
}
