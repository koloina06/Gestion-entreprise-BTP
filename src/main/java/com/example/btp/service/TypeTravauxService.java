package com.example.btp.service;

import com.example.btp.model.Finition;
import com.example.btp.model.TypeTravaux;
import com.example.btp.model.Unite;
import com.example.btp.repository.TypeTravauxRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeTravauxService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    TypeTravauxRepository typeTravauxRepository;

    public List<TypeTravaux> travauxByMaison(int idMaison){
        String sql="select t.idtypetravaux,code,nomtypetravaux,idunite,quantite,prixunitaire from typetravaux t join maisontravaux m on t.idtypetravaux=m.idtypetravaux where idmaison="+idMaison;
        Query nativeQuery = entityManager.createNativeQuery(sql, TypeTravaux.class);
        List<TypeTravaux> travaux = nativeQuery.getResultList();
        for (int i=0; i< travaux.size(); i++){
            travaux.get(i).setPrixTotal(travaux.get(i).getPrixUnitaire()*travaux.get(i).getQuantite());
        }
        return travaux;
    }

    public double prixTotalTravaux(int idMaison){
        String sql="select sum(quantite*prixunitaire) from typetravaux t join maisontravaux m on t.idtypetravaux=m.idtypetravaux where idmaison="+idMaison;
        Query nativeQuery = entityManager.createNativeQuery(sql);
        Object result = nativeQuery.getSingleResult();
        double prixTotal = ((Number) result).doubleValue();
        return prixTotal;
    }

    public double prixTotalDevis(Finition finition, double prixTravaux){
        double augmentation= (prixTravaux* finition.getAugmentation())/100;
        double prixTotal= prixTravaux+augmentation;
        return prixTotal;
    }

    public Page<TypeTravaux> list(Pageable pageable){
        Page<TypeTravaux> typeTravauxList= typeTravauxRepository.findAll(pageable);
        return typeTravauxList;
    }

    public TypeTravaux findById(int idTypeTravaux){
        TypeTravaux typeTravaux= typeTravauxRepository.findTypeTravauxByIdTypeTravaux(idTypeTravaux);
        return typeTravaux;
    }

    public void update(int id,String code, String type, Unite unite, double quantite, double prixUnitaire){
        TypeTravaux typeTravaux= this.findById(id);
        typeTravaux.setCode(code);
        typeTravaux.setNomTypeTravaux(type);
        typeTravaux.setUnite(unite);
        typeTravaux.setQuantite(quantite);
        typeTravaux.setPrixUnitaire(prixUnitaire);
        typeTravauxRepository.save(typeTravaux);
    }

    @Transactional
    public void saveFromCsv(){
        entityManager.createNativeQuery("INSERT INTO typetravaux(code, nomTypeTravaux, idUnite, quantite, prixUnitaire)  " +
                "SELECT codetravaux,typetravaux,u.idunite,CAST(quantite AS double precision),CAST(prixunitaire AS double precision) " +
                "FROM maisontravauxtemp mtp " +
                "JOIN unite u on mtp.unite=u.nomunite " +
                "group by codetravaux,typetravaux,idunite,quantite,prixunitaire;").executeUpdate();
    }
}
