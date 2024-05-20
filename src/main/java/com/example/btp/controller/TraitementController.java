package com.example.btp.controller;

import com.example.btp.model.*;
import com.example.btp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/traitement")
public class TraitementController {

    private final TraitementService traitementService;

    private final UniteService uniteService;

    private final MaisonService maisonService;

    private final TypeTravauxService typeTravauxService;

    private final MaisonTravauxService maisonTravauxService;

    private final FinitionService finitionService;

    private final DevisService devisService;

    private final DetailsDevisService detailsDevisService;

    @Autowired
    public TraitementController(TraitementService traitementService,UniteService uniteService, MaisonService maisonService, TypeTravauxService typeTravauxService, MaisonTravauxService maisonTravauxService, FinitionService finitionService, DevisService devisService, DetailsDevisService detailsDevisService){
        this.maisonTravauxService=maisonTravauxService;
        this.traitementService=traitementService;
        this.uniteService=uniteService;
        this.maisonService=maisonService;
        this.typeTravauxService=typeTravauxService;
        this.finitionService=finitionService;
        this.devisService=devisService;
        this.detailsDevisService=detailsDevisService;
    }

    @GetMapping ("reinitialiser")
    public String reinitialiserBase(HttpSession session){
        traitementService.resetDatabase();
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("toCSVMaisonTravauxDevis")
    public String toImportCsvMT(Model model){
        if(model.containsAttribute("erreursMaisonTravaux")){
            List<ErreurImport> erreurs = (List<ErreurImport>) model.getAttribute("erreursMaisonTravaux");
            model.addAttribute("erreursMaisonTravaux",erreurs);
        }
        if(model.containsAttribute("erreursDevis")){
            List<ErreurImport> erreurs = (List<ErreurImport>) model.getAttribute("erreursDevis");
            model.addAttribute("erreursDevis",erreurs);
        }
        if(model.containsAttribute("succes")){
            String succes= (String) model.getAttribute("succes");
            model.addAttribute("succes",succes);
        }
        return "importMaisonTravauxDevis";
    }


    @PostMapping("importCSVMaisonTravauxDevis")
    public String importCSV(@RequestParam MultipartFile maisonTravaux,@RequestParam MultipartFile devis, RedirectAttributes redirectAttributes){
        traitementService.importCSVMaisonTravaux(maisonTravaux);
        traitementService.importCSVDevis(devis);
        List<MaisonTravauxTemp> maisonTravauxTemps= traitementService.listMaisonTravauxTemp();
        List<DevisTemp> devisTemps= traitementService.listDevisTemp();
        List<ErreurImport> erreursMaisonTravaux= traitementService.checkErreurMaisonTravaux(maisonTravauxTemps);
        List<ErreurImport> erreursDevis= traitementService.checkErreurDevis(devisTemps);
        if(erreursMaisonTravaux.size() > 0){
            traitementService.resetMaisonTravauxTemp();
            redirectAttributes.addFlashAttribute("erreursMaisonTravaux", erreursMaisonTravaux);
        }
        if(erreursDevis.size() >0){
            traitementService.resetDevisTemp();
            redirectAttributes.addFlashAttribute("erreursDevis", erreursDevis);
        }
        if(erreursMaisonTravaux.isEmpty() && erreursDevis.isEmpty()){
            redirectAttributes.addFlashAttribute("succes","données importés avec succès");
            uniteService.saveFromCsv();
            maisonService.saveFromCsv();
            typeTravauxService.saveFromCsv();
            maisonTravauxService.saveFromCsv();
            finitionService.saveFromCsv();
            List<Devis> devisList= devisService.saveFromCsv();
            for(int i=0;i<devisList.size();i++){
                Maison m= maisonService.findById(devisList.get(i).getMaison().getIdMaison());
                Finition f= finitionService.findById(devisList.get(i).getFinition().getIdFinition());
                double prixTotalTravaux= typeTravauxService.prixTotalTravaux(devisList.get(i).getMaison().getIdMaison());
                double prixTotal= typeTravauxService.prixTotalDevis(f,prixTotalTravaux);
                devisService.addPrixAndDateFin(prixTotal,devisList.get(i).getIdDevis(),devisList.get(i).getDateDebut(),m.getDuree());
                List<TypeTravaux> travaux= typeTravauxService.travauxByMaison(devisList.get(i).getMaison().getIdMaison());
                for(int j=0; j<travaux.size(); j++){
                    detailsDevisService.save(devisList.get(i),travaux.get(j));
                }
            }
        }
        return "redirect:/traitement/toCSVMaisonTravauxDevis";
    }

    @GetMapping("toCSVPaiement")
    public String toImportCsvPaiement(Model model){
        if(model.containsAttribute("succes")){
            String succes= (String) model.getAttribute("succes");
            model.addAttribute("succes",succes);
        }
        return "importPaiement";
    }

    @PostMapping("importCSVPaiement")
    public String importCSVPaiement(@RequestParam MultipartFile paiement, RedirectAttributes redirectAttributes){
        List<PaiementTemp> paiementTemps= traitementService.importCSVPaiement(paiement);
        traitementService.savePaiementFromCsv(paiementTemps);
        //redirectAttributes.addFlashAttribute("succes","données importés avec succès");
        return "redirect:/traitement/toCSVPaiement";
    }
}
