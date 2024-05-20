package com.example.btp.controller;

import com.example.btp.model.Devis;
import com.example.btp.model.Paiement;
import com.example.btp.service.DevisService;
import com.example.btp.service.PaiementService;
import com.example.btp.service.TraitementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Date;

@Controller
@RequestMapping(path = "/paiement")
public class PaiementController {

    private final PaiementService paiementService;

    private final DevisService devisService;

    private final TraitementService traitementService;

    @Autowired
    public PaiementController(PaiementService paiementService, DevisService devisService, TraitementService traitementService){
        this.paiementService=paiementService;
        this.devisService=devisService;
        this.traitementService=traitementService;
    }
    /*@PostMapping("payerDevis")
    public String paiement(@RequestParam int idDevis, @RequestParam String date, @RequestParam double prix,@RequestParam String refPaiement ,RedirectAttributes redirectAttributes){
        Date daty= traitementService.toDate(date,"yyyy-MM-dd");
        Devis devis= devisService.getById(idDevis);
        int result= paiementService.save(devis,daty,prix,refPaiement);
        if(result==10){
            redirectAttributes.addFlashAttribute("succes", "Paiement effectué avec succès");
        }else if(result==0 || prix<0){
            redirectAttributes.addFlashAttribute("erreur", "Erreur lors du paiement");
        }
        redirectAttributes.addFlashAttribute("idDevis",idDevis);
        return "redirect:/devis/details";
    }*/

    @PostMapping("/payerDevis")
    public ResponseEntity<String> payerDevis(@RequestParam int idDevis,
                                             @RequestParam String date,
                                             @RequestParam double prix,
                                             @RequestParam String refPaiement
                                             ) {
        Date daty = traitementService.toDate(date, "yyyy-MM-dd");
        Devis devis = devisService.getById(idDevis);
        if (prix<=devis.getRestePaye() && prix>0) {
            paiementService.save(devis, daty, prix, refPaiement);
            String jsonString = "{\"key\": \"Paiement effectué avec succès\"}";
            return ResponseEntity.ok(jsonString);
        } if (prix <0 ) {
            String jsonString = "{\"key\": \"Erreur lors du paiement\"}";
            return ResponseEntity.ok(jsonString);
        }if(prix >devis.getRestePaye() && devis.getRestePaye() >0){
            double famerimbola= prix-devis.getRestePaye();
            paiementService.save(devis, daty, devis.getRestePaye(), refPaiement);
            NumberFormat formatter = NumberFormat.getInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            String formattedString = formatter.format(famerimbola);
            String jsonString = "{\"key\": \"" + formattedString + "\"}";
            return ResponseEntity.ok(jsonString);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur inattendue");
        }
    }

    @GetMapping("paiementEnCours")
    public String paiementEnCours(Model model,@RequestParam int idDevis){
        Devis devis= devisService.getById(idDevis);
        List<Paiement> paiements= paiementService.getPaiementsByDevis(devis);
        model.addAttribute("devis",devis);
        model.addAttribute("paiements",paiements);
        return "paiementsDevis";
    }
}
