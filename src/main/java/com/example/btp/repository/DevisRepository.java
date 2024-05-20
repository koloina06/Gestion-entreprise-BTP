package com.example.btp.repository;

import com.example.btp.model.Devis;
import com.example.btp.model.DevisMontant;
import com.example.btp.model.MontantMois;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DevisRepository extends JpaRepository<Devis,Integer> {
    public List<Devis> findDevisByNumero(String numero);

    public Devis findDevisByIdDevis(int idDevis);

    @Query("select devis from Devis devis")
    public Page<Devis> getDevisEnCours(Pageable pageable);

    @Query("select new com.example.btp.model.DevisMontant(devis.idDevis,sum(paiement.montant)) from Paiement paiement join Devis devis on paiement.devis.idDevis=devis.idDevis group by devis.idDevis")
    public List<DevisMontant> getMontantPayeParDevis();

    @Query("select sum(devis.prixTotalDevis) from Devis devis")
    public Double getMontantTotalAllDevis();

    @Query("SELECT NEW com.example.btp.model.MontantMois(SUM(devis.prixTotalDevis), EXTRACT(MONTH FROM devis.dateDevis)) " +
            "FROM Devis devis " +
            "WHERE EXTRACT(YEAR FROM devis.dateDevis) = :annee " +
            "GROUP BY EXTRACT(MONTH FROM devis.dateDevis) " +
            "ORDER BY EXTRACT(MONTH FROM devis.dateDevis)")
    public List<MontantMois> getMontantsParMoisParAn(String annee);

    public Devis findDevisByRefDevis(String refDevis);

}