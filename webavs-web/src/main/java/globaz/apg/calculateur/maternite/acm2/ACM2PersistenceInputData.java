package globaz.apg.calculateur.maternite.acm2;

import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.framework.util.FWCurrency;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Données provenant de la persistence avant conversion vers des données business
 * 
 * @author lga
 * 
 */
public class ACM2PersistenceInputData {
    private String idDroit = "";
    private int nombreJoursPrestationACM2;
    private BigDecimal tauxAVS;
    private BigDecimal tauxAC;
    /**
     * RMD = Revenu moyen déterminant
     */
    private Map<String, FWCurrency> mapRMD;
    private List<APRepartitionJointPrestation> prestationJointRepartitions = null;
    private List<APSitProJointEmployeur> situationProfessionnelleEmployeur = null;

    /**
     * @param idDroit
     * @param nombreJoursPrestationACM2
     */
    public ACM2PersistenceInputData(String idDroit, int nombreJoursPrestationACM2) {
        super();
        this.idDroit = idDroit;
        this.nombreJoursPrestationACM2 = nombreJoursPrestationACM2;
        mapRMD = new HashMap<String, FWCurrency>();
    }

    public String getIdDroit() {
        return idDroit;
    }

    public List<APRepartitionJointPrestation> getPrestationJointRepartitions() {
        return prestationJointRepartitions;
    }

    public List<APSitProJointEmployeur> getSituationProfessionnelleEmployeur() {
        return situationProfessionnelleEmployeur;
    }

    public void setPrestations(final List<APRepartitionJointPrestation> prestationJointRepartitions) {
        this.prestationJointRepartitions = prestationJointRepartitions;
    }

    public void setSituationProfessionnelleEmployeur(
            final List<APSitProJointEmployeur> situationProfessionnelleEmployeur) {
        this.situationProfessionnelleEmployeur = situationProfessionnelleEmployeur;
    }

    public int getNombreJoursPrestationACM2() {
        return nombreJoursPrestationACM2;
    }

    public void setTauxAVS(BigDecimal tauxAVS) {
        this.tauxAVS = tauxAVS;
    }

    public BigDecimal getTauxAVS() {
        return tauxAVS;
    }

    public void setTauxAC(BigDecimal tauxAC) {
        this.tauxAC = tauxAC;
    }

    public BigDecimal getTauxAC() {
        return tauxAC;
    }

    public void addRMDParEmployeur(String idSitPro, FWCurrency rmd) {
        mapRMD.put(idSitPro, rmd);
    }

    public FWCurrency getRevenuMoyenDeterminant(String idSitPro) {
        return mapRMD.get(idSitPro);
    }

}
