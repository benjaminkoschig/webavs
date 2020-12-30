package globaz.apg.calculateur.maternite.acm2;

import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.framework.util.FWCurrency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Données provenant de la persistence avant conversion vers des données business
 *
 * @author lga
 */
public class ACM2PersistenceInputData {
    private String idDroit = "";
    private int nombreJoursPrestationACM2;
    private Integer nombreInitialDeSituationsProfessionelles = 0;

    /**
     * RMD = Revenu moyen déterminant
     */
    private Map<String, FWCurrency> mapRMD;
    private List<APRepartitionJointPrestation> prestationJointRepartitions = null;
    private List<APSitProJointEmployeur> situationProfessionnelleEmployeur = null;

    /**
     * @param idDroit
     */
    public ACM2PersistenceInputData(String idDroit) {
        super();
        this.idDroit = idDroit;
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

    public void setPrestationJointRepartitions(final List<APRepartitionJointPrestation> prestationJointRepartitions) {
        this.prestationJointRepartitions = prestationJointRepartitions;
    }

    public void setSituationProfessionnelleEmployeur(
            final List<APSitProJointEmployeur> situationProfessionnelleEmployeur) {
        this.situationProfessionnelleEmployeur = situationProfessionnelleEmployeur;
    }

    public int getNombreJoursPrestationACM2() {
        return nombreJoursPrestationACM2;
    }

    public void setNombreJoursPrestationACM2(int nombreJoursPrestationACM2) {
        this.nombreJoursPrestationACM2 = nombreJoursPrestationACM2;
    }

    public void addRMDParEmployeur(String idSitPro, FWCurrency rmd) {
        mapRMD.put(idSitPro, rmd);
    }

    public FWCurrency getRevenuMoyenDeterminant(String idSitPro) {
        return mapRMD.get(idSitPro);
    }

    public Map<String, FWCurrency> getMapRMD() {
        return mapRMD;
    }

    public void setMapRMD(Map<String, FWCurrency> mapRMD) {
        this.mapRMD = mapRMD;
    }

    /**
     * Remplace le revenu par le montant max s'il le dépasse
     *
     * @param montantMaxJournalier
     */
    public void setRevenuMoyenDeterminantParEmployeurAvecMontantMax(FWCurrency montantMaxJournalier) {
        for (Map.Entry<String, FWCurrency> entry : mapRMD.entrySet()) {
            if (entry.getValue().compareTo(montantMaxJournalier) > 0) {
                entry.setValue(montantMaxJournalier);
            }
        }
    }

    public Integer getNombreInitialDeSituationsProfessionelles() {
        return nombreInitialDeSituationsProfessionelles;
    }

    public void setNombreInitialDeSituationsProfessionelles(Integer nombreInitialDeSituationsProfessionelles) {
        this.nombreInitialDeSituationsProfessionelles = nombreInitialDeSituationsProfessionelles;
    }
}
