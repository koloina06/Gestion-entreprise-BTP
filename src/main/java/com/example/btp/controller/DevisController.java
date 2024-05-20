package com.example.btp.controller;

import com.example.btp.model.*;
import com.example.btp.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/devis")
public class DevisController {

    private final MaisonService maisonService;

    private final FinitionService finitionService;

    private final TypeTravauxService typeTravauxService;

    private final TraitementService traitementService;

    private final DevisService devisService;

    private final DetailsDevisService detailsDevisService;
    private final TemplateEngine templateEngine;

    private final PaiementService paiementService;

    public DevisController(MaisonService maisonService, FinitionService finitionService, TypeTravauxService typeTravauxService, TraitementService traitementService, DevisService devisService, DetailsDevisService detailsDevisService, TemplateEngine templateEngine, PaiementService paiementService){
        this.maisonService=maisonService;
        this.finitionService=finitionService;
        this.typeTravauxService= typeTravauxService;
        this.traitementService= traitementService;
        this.devisService=devisService;
        this.detailsDevisService=detailsDevisService;
        this.templateEngine=templateEngine;
        this.paiementService=paiementService;
    }

    @GetMapping("demandeDevis")
    public String demandeDevis(Model model){
        if(model.containsAttribute("succes")){
            String succes= (String) model.getAttribute("succes");
            model.addAttribute("succes",succes);
        }
        if(model.containsAttribute("erreur")){
            String erreur= (String) model.getAttribute("erreur");
            model.addAttribute("erreur",erreur);
        }
        List<Maison> maisons= maisonService.list();
        List<Finition> finitions= finitionService.list();
        model.addAttribute("maisons",maisons);
        model.addAttribute("finitions",finitions);
        return "creationDevis";
    }

    @PostMapping ("createDevis")
    public String createDevis(HttpSession session, @RequestParam int maison, @RequestParam int finition, @RequestParam String dateDebut, @RequestParam String dateDevis, @RequestParam String lieu , @RequestParam String refDevis, RedirectAttributes redirectAttributes){
        /*Calendar calendar = Calendar.getInstance();
        Date datyDevis = calendar.getTime();*/
        Date datyDevis= traitementService.toDate(dateDevis,"yyyy-MM-dd");
        Maison m= maisonService.findById(maison);
        Finition f= finitionService.findById(finition);
        double prixTotalTravaux= typeTravauxService.prixTotalTravaux(maison);
        double prixTotal= typeTravauxService.prixTotalDevis(f,prixTotalTravaux);
        Date daty= traitementService.toDate(dateDebut,"yyyy-MM-dd");
        Client client= (Client) session.getAttribute("client");
        if(daty != null && datyDevis !=null){
            if(daty.after(datyDevis)){
                Devis devis= devisService.save(m,f,daty,client.getTelephone(),prixTotal,datyDevis,lieu,refDevis);
                List<TypeTravaux> travaux= typeTravauxService.travauxByMaison(maison);
                for(int i=0; i<travaux.size(); i++){
                    detailsDevisService.save(devis,travaux.get(i));
                }
                redirectAttributes.addFlashAttribute("succes","devis demandé avec succès");
            }else {
                redirectAttributes.addFlashAttribute("erreur","erreur lors de la demande");
            }
        }else {
            redirectAttributes.addFlashAttribute("erreur","erreur lors de la demande");
        }
        return "redirect:/devis/demandeDevis";
    }

    @GetMapping("listeClient")
    public String listeDevisClient(HttpSession session, Model model){
        Client client= (Client) session.getAttribute("client");
        List<Devis> devis= devisService.listDevisClient(client.getTelephone());
        model.addAttribute("devisList",devis);
        return "listeDevisClient";
    }

    @GetMapping("details")
    public String detailsDevisClient(Model model,@RequestParam(required = false) String idDevis,@RequestParam(required = false) String succes){
        int id=0;
        if(idDevis != null){
            id=Integer.parseInt(idDevis);
        }
        if(model.containsAttribute("idDevis")){
            id= (Integer) model.getAttribute("idDevis");
        }
        Devis devis= devisService.getById(id);
        List<DetailsDevis> detailsDevis= detailsDevisService.getByDevis(devis);
        model.addAttribute("devis",devis);
        model.addAttribute("detailsList",detailsDevis);
        if(succes != null){
            System.out.println(succes);
            model.addAttribute("succes",succes);
        }
        /*if(model.containsAttribute("succes")){
            String succes= (String) model.getAttribute("succes");
            model.addAttribute("succes",succes);
        }
        if(model.containsAttribute("erreur")){
            String erreur= (String) model.getAttribute("erreur");
            model.addAttribute("erreur",erreur);
        }*/
        return "detailsDevisClient";
    }

    @PostMapping ("/exportPDF")
    public void exportPdf(HttpServletResponse response, @RequestParam int idDevis) throws Exception {
        Devis devis= devisService.getById(idDevis);
        List<DetailsDevis> detailsDevis= detailsDevisService.getByDevis(devis);
        List<Paiement> paiements= paiementService.getPaiementsByDevis(devis);
        double montantPaye= paiementService.getMontantPaye(idDevis);
        Context context = new Context();
        context.setVariable("devis", devis);
        context.setVariable("detailsList", detailsDevis);
        context.setVariable("paiements",paiements);
        context.setVariable("montantPaye",montantPaye);
        String htmlContent = templateEngine.process("devisPDF", context);
        traitementService.exportPDF(htmlContent,"devis.pdf",response);
    }

    @GetMapping("listeEnCours")
    public String listeDevisEnCours(Model model,@RequestParam(defaultValue = "0") int page){
        int size=5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Devis> devisList= devisService.listeDevisEnCours(pageable);
        model.addAttribute("devisList",devisList);
        return "devisEnCours";
    }

    @GetMapping("detailsEnCours")
    public String detailsEnCours(Model model, @RequestParam int idDevis){
        Devis devis= devisService.getById(idDevis);
        List<DetailsDevis> detailsDevis= detailsDevisService.getByDevis(devis);
        model.addAttribute("detailsList",detailsDevis);
        model.addAttribute("devis",devis);
        return "detailsDevisEnCours";
    }

    @GetMapping("dashboard")
    public String dashboard(Model model, @RequestParam(required = false) String annee){
        double montantTotal= devisService.montantTotalAllDevis();
        List<MontantMois> montantsMois= devisService.montantsParMoisParAn(annee);
        double paiementsTotal= paiementService.totalPaiementEffectue();
        if(annee != null){
            List<Double> montants= new ArrayList<>();
            for(int i=0; i<montantsMois.size(); i++){
                montants.add(montantsMois.get(i).getMontantTotalDevis());
            }
            model.addAttribute("montants",montants);
            model.addAttribute("annee",annee);
        }
        model.addAttribute("montantTotal",montantTotal);
        model.addAttribute("paiementTotal",paiementsTotal);
        return "dashboard";
    }
}