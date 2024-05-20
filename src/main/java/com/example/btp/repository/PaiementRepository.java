package com.example.btp.repository;

import com.example.btp.model.Devis;
import com.example.btp.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement,Integer> {

    @Query("select sum(paiement.montant) from Paiement paiement where paiement.devis.idDevis= :idDevis")
    public Double montantPayeDevis(int idDevis);
    public List<Paiement> getPaiementsByDevis(Devis devis);

    @Query("select sum(paiement.montant) from Paiement paiement")
    public Double totalPaiementEffectué();

    public Paiement findPaiementByRefPaiement(String ref);
}
