package com.example.btp.controller;

import com.example.btp.model.TypeTravaux;
import com.example.btp.model.Unite;
import com.example.btp.service.TypeTravauxService;
import com.example.btp.service.UniteService;
import com.itextpdf.text.pdf.qrcode.Mode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/typeTravaux")
public class TypeTravauxController {

    private final TypeTravauxService typeTravauxService;

    private final UniteService uniteService;

    @Autowired
    public TypeTravauxController(TypeTravauxService typeTravauxService, UniteService uniteService){
        this.typeTravauxService=typeTravauxService;
        this.uniteService=uniteService;
    }

    @GetMapping("liste")
    public String liste(@RequestParam(defaultValue = "0") int page, Model model){
        int size=5;
        Pageable pageable = PageRequest.of(page, size);
        Page<TypeTravaux> typeTravauxPage= typeTravauxService.list(pageable);
        model.addAttribute("travauxList",typeTravauxPage);
        return "listeTypeTravaux";
    }

    @GetMapping("toUpdate")
    public String toUpdate(@RequestParam(required = false) Integer idTypeTravaux, Model model){
        int id=0;
        if(idTypeTravaux != null){
            id= idTypeTravaux;
        }else {
            id= (Integer) model.getAttribute("idTypeTravaux");
        }
        TypeTravaux typeTravaux= typeTravauxService.findById(id);
        List<Unite> unites= uniteService.list();
        for(int i=0; i<unites.size(); i++){
            if(unites.get(i).getIdUnite()==typeTravaux.getUnite().getIdUnite()){
                unites.remove(i);
            }
        }
        if(model.containsAttribute("succes")){
            String succes= (String) model.getAttribute("succes");
            model.addAttribute("succes",succes);
        }
        model.addAttribute("travaux",new TypeTravaux());
        model.addAttribute("unites",unites);
        model.addAttribute("typeTravaux",typeTravaux);
        return "updateTypeTravaux";
    }

    @PostMapping("update")
    public String update(RedirectAttributes redirectAttributes, Model model, @Valid TypeTravaux travaux, BindingResult result, @RequestParam int idTypeTravaux, @RequestParam String code, @RequestParam String nomTypeTravaux, @RequestParam int unite, @RequestParam double quantite, @RequestParam double prixUnitaire){
        if (result.hasErrors()) {
            TypeTravaux typeTravaux= typeTravauxService.findById(idTypeTravaux);
            List<Unite> unites= uniteService.list();
            for(int i=0; i<unites.size(); i++){
                if(unites.get(i).getIdUnite()==typeTravaux.getUnite().getIdUnite()){
                    unites.remove(i);
                }
            }
            System.out.println(result.getAllErrors());
            model.addAttribute("travaux",travaux);
            model.addAttribute("unites",unites);
            model.addAttribute("typeTravaux",typeTravaux);
            model.addAttribute("errors", result);
            model.addAttribute("erreur",result.getAllErrors());
            return "updateTypeTravaux";
        }
        Unite u= uniteService.findById(unite);
        typeTravauxService.update(idTypeTravaux,code,nomTypeTravaux,u,quantite,prixUnitaire);
        redirectAttributes.addFlashAttribute("idTypeTravaux", idTypeTravaux);
        redirectAttributes.addFlashAttribute("succes", "Modifié avec succès");
        return "redirect:/typeTravaux/toUpdate";
    }

   /* @PostMapping("update")
    public String update(RedirectAttributes redirectAttributes, Model model, @Valid TypeTravaux travaux, BindingResult result, @RequestParam int idTypeTravaux, @RequestParam String code, @RequestParam String nomTypeTravaux, @RequestParam int unite, @RequestParam double quantite, @RequestParam double prixUnitaire){
        if (result.hasErrors()) {
            TypeTravaux typeTravaux= typeTravauxService.findById(idTypeTravaux);
            List<Unite> unites= uniteService.list();
            for(int i=0; i<unites.size(); i++){
                if(unites.get(i).getIdUnite()==typeTravaux.getUnite().getIdUnite()){
                    unites.remove(i);
                }
            }
            System.out.println(result.getAllErrors());
            model.addAttribute("travaux",travaux);
            model.addAttribute("unites",unites);
            model.addAttribute("typeTravaux",typeTravaux);
            model.addAttribute("errors", result);
            return "updateTypeTravaux";
        }
        Unite u= uniteService.findById(unite);
        typeTravauxService.update(idTypeTravaux,code,nomTypeTravaux,u,quantite,prixUnitaire);
        redirectAttributes.addFlashAttribute("idTypeTravaux", idTypeTravaux);
        redirectAttributes.addFlashAttribute("succes", "Modifié avec succès");
        return "redirect:/typeTravaux/toUpdate";
    }*/
}
