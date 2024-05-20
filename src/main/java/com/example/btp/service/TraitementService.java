package com.example.btp.service;

import com.example.btp.model.*;
import com.example.btp.repository.DevisTempRepo;
import com.example.btp.repository.MaisonTravauxTempRepo;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.csv.CSVParser;
import java.util.List;

@Service
public class TraitementService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MaisonTravauxTempRepo maisonTravauxTempRepo;

    @Autowired
    DevisTempRepo devisTempRepo;

    @Autowired
    PaiementService paiementService;

    @Autowired
    DevisService devisService;

    public Timestamp toTimestamp(String daty, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        Timestamp d= null;
        try {
            Date date = dateFormat.parse(daty);
            d = new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public String checkTimestamp(String daty,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        Timestamp d= null;
        String error= null;
        try {
            Date date = dateFormat.parse(daty);
            d = new Timestamp(date.getTime());
        } catch (ParseException e) {
            //e.printStackTrace();
            error="format de date invalide";
        }
        return error;
    }


    public Date toDate(String daty,String format){
        SimpleDateFormat dateFormat= new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        Date d= null;
        try {
            d = dateFormat.parse(daty);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public Date toDateCSv(String daty, String inputFormat, String outputFormat) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
        inputDateFormat.setLenient(false);
        Date date = null;
        try {
            date = inputDateFormat.parse(daty);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        String formattedDate = outputDateFormat.format(date);
        try {
            return outputDateFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String checkDate(String daty){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        Date d= null;
        String error= null;
        try {
            d = dateFormat.parse(daty);
        } catch (ParseException e) {
            e.printStackTrace();
            error="format de date invalide";
        }
        return error;
    }


    public void exportPDF(String htmlContent, String filename, HttpServletResponse response) throws Exception{
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename="+filename+"");
        OutputStream outputStream = response.getOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
    }

    public String uploadPhoto(String uploadDirectory, MultipartFile sary){
        String fileName="";
        try {
            File uploadDir = new File(uploadDirectory);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            fileName = sary.getOriginalFilename();
            String filePath = uploadDirectory + File.separator + fileName;
            sary.transferTo(new File(filePath));
        }catch (IOException e){
            e.printStackTrace();
        }
       return fileName;
    }

    @Transactional
    public void resetDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE paiement cascade ");
        jdbcTemplate.execute("TRUNCATE TABLE detailsdevis cascade");
        jdbcTemplate.execute("TRUNCATE TABLE devis cascade");
        jdbcTemplate.execute("TRUNCATE TABLE maisontravaux cascade");
        jdbcTemplate.execute("TRUNCATE TABLE typetravaux cascade");
        jdbcTemplate.execute("TRUNCATE TABLE finition cascade");
        jdbcTemplate.execute("TRUNCATE TABLE maison cascade");
        jdbcTemplate.execute("TRUNCATE TABLE unite cascade");
        jdbcTemplate.execute("ALTER SEQUENCE paiement_idpaiement_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE detailsdevis_iddetailsdevis_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE devis_iddevis_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE maisontravaux_idmaisontravaux_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE typetravaux_idtypetravaux_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE finition_idfinition_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE maison_idmaison_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE unite_idunite_seq RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE maisontravauxtemp ");
        jdbcTemplate.execute("ALTER SEQUENCE maisontravauxtemp_id_seq RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE devistemp ");
        jdbcTemplate.execute("ALTER SEQUENCE devistemp_id_seq RESTART WITH 1");
    }

    public void saveImportMaisonTravaux(int numLigne, String typeMaison, String descriptions, String surface, String codetravaux, String typetravaux, String unite, String prixUnitaire, String quantite, String dureeTravaux){
        MaisonTravauxTemp maisonTravauxTemp= new MaisonTravauxTemp(numLigne,typeMaison,descriptions,surface.replace(",","."),codetravaux,typetravaux,unite,prixUnitaire.replace(",","."),quantite.replace(",","."),dureeTravaux.replace(",","."));
        maisonTravauxTempRepo.save(maisonTravauxTemp);
    }

    public void saveImportDevis(int numLigne,String client,String refDevis,String typeMaison,String finition,String tauxFinition,String dateDevis, String dateDebut, String lieu){
        Date dateDeb= this.toDateCSv(dateDebut,"dd/MM/yyyy","yyyy-MM-dd");
        Date dateDev= this.toDateCSv(dateDevis,"dd/MM/yyyy","yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateDebut= dateFormat.format(dateDeb);
        dateDevis= dateFormat.format(dateDev);
        DevisTemp devisTemp= new DevisTemp(numLigne,client,refDevis,typeMaison,finition,tauxFinition.replace(",",".").replace("%",""),dateDevis,dateDebut,lieu);
        devisTempRepo.save(devisTemp);
    }

    public void importCSVMaisonTravaux(MultipartFile csv) {
        int numLigne=0;
        try (Reader reader = new InputStreamReader(csv.getInputStream());
             CSVParser csvParser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .parse(reader)) {

            for (CSVRecord record : csvParser) {
                String typeMaison = record.get("type_maison").trim();
                String description = record.get("description").trim();
                String surface = record.get("surface").trim();
                String codeTravaux = record.get("code_travaux").trim();
                String typeTravaux = record.get("type_travaux").trim();
                String unite = record.get("unité").trim();
                String prixUnitaire = record.get("prix_unitaire").trim();
                String quantite = record.get("quantite").trim();
                String dureeTravaux = record.get("duree_travaux").trim();
                numLigne++;
                this.saveImportMaisonTravaux(numLigne,typeMaison, description, surface, codeTravaux, typeTravaux, unite, prixUnitaire, quantite, dureeTravaux);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importCSVDevis(MultipartFile csv) {
        int numLigne=0;
        try (Reader reader = new InputStreamReader(csv.getInputStream());
             CSVParser csvParser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .parse(reader)) {

            for (CSVRecord record : csvParser) {
                String client = record.get("client").trim();
                String refDevis = record.get("ref_devis").trim();
                String typeMaison = record.get("type_maison").trim();
                String finition = record.get("finition").trim();
                String tauxFinition = record.get("taux_finition").trim();
                String dateDevis = record.get("date_devis").trim();
                String dateDebut = record.get("date_debut").trim();
                String lieu = record.get("lieu").trim();
                numLigne++;
                this.saveImportDevis(numLigne,client, refDevis, typeMaison, finition, tauxFinition, dateDevis, dateDebut, lieu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PaiementTemp>  importCSVPaiement(MultipartFile csv) {
        List<PaiementTemp> temp= new ArrayList<>();
        int numLigne=0;
        try (Reader reader = new InputStreamReader(csv.getInputStream());
             CSVParser csvParser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .parse(reader)) {

            for (CSVRecord record : csvParser) {
                String refDevis = record.get("ref_devis").trim();
                String refPaiement = record.get("ref_paiement").trim();
                String datePaiement = record.get("date_paiement").trim();
                String montant = record.get("montant").trim();
                numLigne++;;
                PaiementTemp paiementTemp= new PaiementTemp(refDevis,refPaiement,datePaiement,montant);
                temp.add(paiementTemp);
                /*Devis devis= devisService.findByRef(refDevis);
                Date dateP= this.toDateCSv(datePaiement,"dd/MM/yyyy","yyyy-MM-dd");
                int save= paiementService.save(devis,dateP,Double.parseDouble(montant.replace(",",".")),refPaiement);*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public void savePaiementFromCsv(List<PaiementTemp> paiementTemps){
        for(int i=0; i<paiementTemps.size();i++){
            Paiement paiement= paiementService.checkRefExist(paiementTemps.get(i).getRefPaiement());
            if(paiement == null){
                Devis devis= devisService.findByRef(paiementTemps.get(i).getRefDevis());
                Date dateP= this.toDateCSv(paiementTemps.get(i).getDatePaiement(),"dd/MM/yyyy","yyyy-MM-dd");
                paiementService.save(devis,dateP,Double.parseDouble(paiementTemps.get(i).getMontant().replace(",",".")),paiementTemps.get(i).getRefPaiement());
            }
        }
    }

    public List<MaisonTravauxTemp> listMaisonTravauxTemp(){
        List<MaisonTravauxTemp> maisonTravauxTemps= maisonTravauxTempRepo.findAll();
        return maisonTravauxTemps;
    }

    public List<DevisTemp> listDevisTemp(){
        List<DevisTemp> devisTemps= devisTempRepo.findAll();
        return devisTemps;
    }

    /*public List<ErreurImport> checkErreurPaiement(List<MaisonTravauxTemp> temps){

    }*/

    public List<ErreurImport> checkErreurMaisonTravaux(List<MaisonTravauxTemp> temps){
        List<ErreurImport> erreurImports= new ArrayList<>();
        for(int i=0; i<temps.size(); i++){
            if(Double.parseDouble(temps.get(i).getSurface()) <=0 ){
                ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getSurface(),"la surface doit etre > 0");
                erreurImports.add(erreurImport);
            }
            if(Integer.parseInt(temps.get(i).getCodetravaux()) <=0 ){
                ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getCodetravaux(),"le code doit etre > 0");
                erreurImports.add(erreurImport);
            }
            if(Double.parseDouble(temps.get(i).getPrixUnitaire().replace(",",".")) <=0 ){
                ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getPrixUnitaire(),"le prix doit etre > 0");
                erreurImports.add(erreurImport);
            }
            if(Double.parseDouble(temps.get(i).getQuantite().replace(",",".")) <=0){
                ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getQuantite(),"la quantite doit etre > 0");
                erreurImports.add(erreurImport);
            }
            if(Integer.parseInt(temps.get(i).getDureeTravaux()) <=0 ){
                ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getDureeTravaux(),"la duree de travaux doit etre > 0");
                erreurImports.add(erreurImport);
            }
        }
        return erreurImports;
    }

    public List<ErreurImport> checkErreurDevis(List<DevisTemp> temps){
        List<ErreurImport> erreurImports= new ArrayList<>();
        for(int i=0; i<temps.size(); i++){
            if(Double.parseDouble(temps.get(i).getTauxFinition()) <0 ){
                ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getTauxFinition(),"le taux de finition doit etre > 0");
                erreurImports.add(erreurImport);
            }
            //Date dateDev= this.toDateCSv(temps.get(i).getDateDevis(),"dd/MM/yyyy","yyyy-MM-dd");
           // Date dateDeb= this.toDateCSv(temps.get(i).getDateDebut(),"dd/MM/yyyy","yyyy-MM-dd");
            String errorDateDevis = this.checkDate(temps.get(i).getDateDevis());
            String errorDateDebut = this.checkDate(temps.get(i).getDateDebut());
            if(errorDateDevis != null){
                ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getDateDevis(),"format de date invalide");
                erreurImports.add(erreurImport);
            }
            if(errorDateDebut != null){
                ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getDateDebut(),"format de date invalide");
                erreurImports.add(erreurImport);
            }
            if(errorDateDevis==null && errorDateDebut==null){
                Date dateDeb= this.toDate(temps.get(i).getDateDebut(),"yyyy-MM-dd");
                Date dateDev= this.toDate(temps.get(i).getDateDevis(),"yyyy-MM-dd");
                if(dateDeb.before(dateDev)){
                    ErreurImport erreurImport= new ErreurImport(temps.get(i).getNumLigne(),temps.get(i).getDateDebut()+"<"+temps.get(i).getDateDevis(),"date debut inferieur à date de devis");
                    erreurImports.add(erreurImport);
                }
            }
        }
        return erreurImports;
    }

    @Transactional
    public void resetMaisonTravauxTemp() {
        jdbcTemplate.execute("TRUNCATE TABLE maisontravauxtemp ");
        jdbcTemplate.execute("ALTER SEQUENCE maisontravauxtemp_id_seq RESTART WITH 1");
    }

    @Transactional
    public void resetDevisTemp() {
        jdbcTemplate.execute("TRUNCATE TABLE devistemp ");
        jdbcTemplate.execute("ALTER SEQUENCE devistemp_id_seq RESTART WITH 1");
    }

}
