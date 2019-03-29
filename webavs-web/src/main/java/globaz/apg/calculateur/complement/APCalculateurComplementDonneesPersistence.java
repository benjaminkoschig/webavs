package globaz.apg.calculateur.complement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.module.calcul.APRepartitionPaiementData;
import globaz.apg.module.calcul.constantes.ECanton;
import globaz.apg.module.calcul.constantes.EMontantsMax;
import globaz.framework.util.FWCurrency;

public class APCalculateurComplementDonneesPersistence {
    
    private String dateDebut = "";
    private String dateFin = "";
    private String idDroit = "";
    private BigDecimal montantBrut = null;
    private BigDecimal montantJournalier = null;
    private BigDecimal montantNet = null;
    private Integer nombreDeJoursSoldes = 0;
    
    // Lecture
    private List<APRepartitionJointPrestation> prestationJointRepartitions = null;
    private List<APSitProJointEmployeur> situationProfessionnelleEmployeur = null;
    private List<APPrestation> listPrestationStandard;
    private APDroitLAPG droit;

    private Map<String, String> mapTypeAffiliation = new HashMap<>();
    
    private Map<String, BigDecimal[]> taux = new HashMap<String, BigDecimal[]>();
    private Map<String, FWCurrency> mapRMD;
    private Map<EMontantsMax, BigDecimal> montantsMax;
    private Map<String, ECanton> mapCanton;


    // Ecriture
    private List<APRepartitionPaiementData> repartitionsPaiementMap = new ArrayList<>();
    
    public APCalculateurComplementDonneesPersistence(String idDroit) {
        this.idDroit = idDroit;
    }
    
    public APCalculateurComplementDonneesPersistence(String dateDebut, String dateFin, int nombreDeJoursSoldes,
            String idDroit) {
        super();
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreDeJoursSoldes = nombreDeJoursSoldes;
        this.idDroit = idDroit;
    }
    
    public List<APPrestation> getListPrestationStandard() {
        return listPrestationStandard;
    }
    
    public void setListPrestationStandard(List<APPrestation> listPrestationStandard) {
        this.listPrestationStandard = listPrestationStandard;
    }
    
    public String getIdDroit() {
        return idDroit;
    }
    
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public APDroitLAPG getDroit() {
        return droit;
    }

    public void setDroit(APDroitLAPG droit) {
        this.droit = droit;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public BigDecimal getMontantBrut() {
        return montantBrut;
    }

    public BigDecimal getMontantJournalier() {
        return montantJournalier;
    }

    public BigDecimal getMontantNet() {
        return montantNet;
    }

    public Integer getNombreDeJoursSoldes() {
        return nombreDeJoursSoldes;
    }

    public List<APRepartitionPaiementData> getRepartitionsPaiementMap() {
        return repartitionsPaiementMap;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setMontantBrut(BigDecimal montantBrut) {
        this.montantBrut = montantBrut;
    }

    public void setMontantJournalier(BigDecimal montantJournalier) {
        this.montantJournalier = montantJournalier;
    }

    public void setMontantNet(BigDecimal montantNet) {
        this.montantNet = montantNet;
    }

    public void setNombreDeJoursSoldes(Integer nombreDeJoursSoldes) {
        this.nombreDeJoursSoldes = nombreDeJoursSoldes;
    }

    public void setRepartitionsPaiementMap(List<APRepartitionPaiementData> repartitionsPaiementMap) {
        this.repartitionsPaiementMap = repartitionsPaiementMap;
    }

    public List<APRepartitionJointPrestation> getPrestationJointRepartitions() {
        return prestationJointRepartitions;
    }

    public void setPrestationJointRepartitions(List<APRepartitionJointPrestation> prestationJointRepartitions) {
        this.prestationJointRepartitions = prestationJointRepartitions;
    }

    public List<APSitProJointEmployeur> getSituationProfessionnelleEmployeur() {
        return situationProfessionnelleEmployeur;
    }

    public void setSituationProfessionnelleEmployeur(List<APSitProJointEmployeur> situationProfessionnelleEmployeur) {
        this.situationProfessionnelleEmployeur = situationProfessionnelleEmployeur;
    }

    public Map<String, BigDecimal[]> getTaux() {
        return taux;
    }

    public void setTaux(Map<String, BigDecimal[]> taux) {
        this.taux = taux;
    }

    public Map<String, FWCurrency> getMapRMD() {
        return mapRMD;
    }

    public void setMapRMD(Map<String, FWCurrency> mapRMD) {
        this.mapRMD = mapRMD;
    }
    
    public void addRMDParEmployeur(String idSitPro, FWCurrency rmd) {
        mapRMD.put(idSitPro, rmd);
    }

    public FWCurrency getRevenuMoyenDeterminant(String idSitPro) {
        return mapRMD.get(idSitPro);
    }

    public Map<EMontantsMax, BigDecimal> getMontantsMax() {
        return montantsMax;
    }

    public void setMontantsMax(Map<EMontantsMax, BigDecimal> montantsMax) {
        this.montantsMax = montantsMax;
    }

    public Map<String, ECanton> getMapCanton() {
        return mapCanton;
    }

    public void setMapCanton(Map<String, ECanton> mapCanton) {
        this.mapCanton = mapCanton;
    }

    public Map<String, String> getMapTypeAffiliation() {
        return mapTypeAffiliation;
    }

    public void setMapTypeAffiliation(Map<String, String> mapTypeAffiliation) {
        this.mapTypeAffiliation = mapTypeAffiliation;
    }


}
