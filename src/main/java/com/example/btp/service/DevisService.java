package com.example.btp.service;

import com.example.btp.model.*;
import com.example.btp.repository.DevisRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class DevisService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    DevisRepository devisRepository;

    @Autowired
    PaiementService paiementService;

    @Autowired
    FinitionService finitionService;

    @Autowired
    TypeTravauxService typeTravauxService;

    public Date getDateFin(Date dateDebut, double jour){
        LocalDate dateFin= dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dateFin= dateFin.plusDays((long) jour);
        LocalDateTime daty = dateFin.atStartOfDay();
        return Date.from(daty.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Devis save(Maison maison, Finition finition, Date dateDebut, String numero, double prixTotal,Date dateDevis, String lieu, String refDevis){
        Date dateFin= this.getDateFin(dateDebut,maison.getDuree());
        Devis devis= new Devis(maison,finition,dateDebut,dateFin,numero, finition.getAugmentation(), prixTotal, dateDevis,lieu , refDevis);
        devis= devisRepository.save(devis);
        return devis;
    }

    public List<Devis> listDevisClient(String numero){
        List<Devis> devis= devisRepository.findDevisByNumero(numero);
        return devis;
    }

    public Devis getById(int idDevis){
        Devis devis= devisRepository.findDevisByIdDevis(idDevis);;
        double prixTravaux= typeTravauxService.prixTotalTravaux(devis.getMaison().getIdMaison());
        double montantFinition= finitionService.montantFinition(devis.getFinition(),prixTravaux);
        devis.getFinition().setMontant(montantFinition);
        double montantPaye= paiementService.getMontantPaye(idDevis);
        double reste= devis.getPrixTotalDevis()-montantPaye;
        if(reste>=0){
            devis.setRestePaye(reste);
        }else {
            devis.setRestePaye(0);
        }
        return devis;
    }

    public Page<Devis> listeDevisEnCours(Pageable pageable) {
        Page<Devis> devisList = devisRepository.getDevisEnCours(pageable);
        List<DevisMontant> devisMontants = this.montantsPayesAllDevis();
        for (Devis devis : devisList) {
            devis.setMontantPaye(0);
            devis.setPourcentage(0);
            for (DevisMontant devisMontant : devisMontants) {
                if (devis.getIdDevis() == devisMontant.getIdDevis()) {
                    devis.setMontantPaye(devisMontant.getMontantPaye());
                    double pourcentage = (devis.getMontantPaye() * 100) / devis.getPrixTotalDevis();
                    devis.setPourcentage(pourcentage);
                }
            }
        }
        return new PageImpl<>(devisList.getContent(), pageable, devisList.getTotalElements());
    }

    public List<DevisMontant> montantsPayesAllDevis(){
        List<DevisMontant> devisMontants= devisRepository.getMontantPayeParDevis();
        return devisMontants;
    }

    public double montantTotalAllDevis(){
        Double montant= devisRepository.getMontantTotalAllDevis();
        if(montant==null){
            montant=0.0;
        }
        return montant;
    }

    public List<MontantMois> montantsParMoisParAn(String annee) {
        List<MontantMois> montants = devisRepository.getMontantsParMoisParAn(annee);
        List<MontantMois> montantsManquants = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            boolean moisTrouve = false;
            for (MontantMois montant : montants) {
                if (montant.getMois() == i) {
                    moisTrouve = true;
                    break;
                }
            }
            if (!moisTrouve) {
                montantsManquants.add(new MontantMois(0, i));
            }
        }

        montants.addAll(montantsManquants);

        Collections.sort(montants, Comparator.comparingInt(MontantMois::getMois));


        return montants;
    }



    @Transactional
    public List<Devis> saveFromCsv() {
        String insertQuery = "INSERT INTO devis(refdevis, datedevis, idmaison, idfinition, datedebut, numeroclient, augmentation, lieu, prixtotaldevis) " +
                "SELECT DISTINCT mtp.refdevis, TO_DATE(datedevis, 'YYYY-MM-DD') , m.idmaison, f.idfinition, TO_DATE(datedebut, 'YYYY-MM-DD'),  mtp.client, CAST(tauxfinition AS double precision) , mtp.lieu,0 " +
                "FROM devistemp mtp " +
                "JOIN maison m ON mtp.typemaison = m.typemaison " +
                "JOIN finition f ON mtp.finition = f.typefinition";

        entityManager.createNativeQuery(insertQuery).executeUpdate();

        String selectInsertedQuery = "SELECT * FROM devis WHERE refdevis IN (SELECT DISTINCT refdevis FROM devistemp)";
        return entityManager.createNativeQuery(selectInsertedQuery, Devis.class).getResultList();
    }

    public void addPrixAndDateFin(double prixTotal, int idDevis, Date dateDebut, double jour){
        Devis devis= devisRepository.findDevisByIdDevis(idDevis);
        devis.setDateFin(this.getDateFin(dateDebut,jour));
        devis.setPrixTotalDevis(prixTotal);
        devisRepository.save(devis);
    }

    public Devis findByRef(String ref){
        Devis devis= devisRepository.findDevisByRefDevis(ref);
        double montantPaye= paiementService.getMontantPaye(devis.getIdDevis());
        double reste= devis.getPrixTotalDevis()-montantPaye;
        if(reste>=0){
            devis.setRestePaye(reste);
        }else {
            devis.setRestePaye(0);
        }
        return devis;
    }

}
