package globaz.hercule.db.controleEmployeur;

import globaz.hercule.db.CEAbstractViewBean;
import globaz.hercule.service.CEAffiliationService;

/**
 * @author SCO
 * @since 13 oct. 2010
 */
public class CECompteRenduViewBean extends CEAbstractViewBean {

    public String dateDebutControle;
    public String dateEffective;
    public String dateEnvoi;
    public String dateFinControle;
    public String idAffiliation;
    public String idTiers;
    public String numAffilie;
    public String selectedId;
    public String visaReviseur;

    /**
     * Constructeur de CELettreLibreCatalogueViewBean
     */
    public CECompteRenduViewBean() {
        super();
    }

    /**
     * Permet la récupération de l'information d'un affilie (Nom et date d'affiliation)
     * 
     * @param idAffiliation
     * @return
     */
    public String _getInfoTiers(String idAffiliation) {
        return CEAffiliationService.getNomEtDateAffiliation(getSession(), idAffiliation);
    }

    // *******************************************************
    // Getter
    // ***************************************************

    public String getDateDebutControle() {
        return dateDebutControle;
    }

    public String getDateEffective() {
        return dateEffective;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateFinControle() {
        return dateFinControle;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public String getVisaReviseur() {
        return visaReviseur;
    }

    // *******************************************************
    // Setter
    // ***************************************************

    public void setDateDebutControle(String dateDebutControle) {
        this.dateDebutControle = dateDebutControle;
    }

    public void setDateEffective(String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setDateFinControle(String dateFinControle) {
        this.dateFinControle = dateFinControle;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public void setVisaReviseur(String visaReviseur) {
        this.visaReviseur = visaReviseur;
    }
}
