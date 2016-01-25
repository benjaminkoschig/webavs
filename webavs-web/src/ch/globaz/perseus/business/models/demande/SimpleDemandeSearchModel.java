package ch.globaz.perseus.business.models.demande;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * Permet de contrôler la superposition des dates au niveau des demandes du dossier On passe la demande en cours pour
 * qu'elle ne soit pas prise en compte dans la recherche
 * 
 * @author DDE
 * 
 */
public class SimpleDemandeSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ORDER_BY_DATE_DEBUT_DATE_VALIDATION = "dateDebutDateValidation";
    public static String WITH_OUT_WHERE = "WithOutWhere";
    public static String WITH_SITUATION_FAMILIALE = "withSituationFamiliale";

    private String csEtatDemande = null;
    private String forDateDebutCheck = null;
    private String forDateFinCheck = null;
    private String forIdDossier = null;
    private String forNotIdDemande = null;
    private String forNotIdDossier = null;

    private List<String> inIdSituationFamiliale = null;

    public String getCsEtatDemande() {
        return csEtatDemande;
    }

    /**
     * @return the forDateDebutCheck
     */
    public String getForDateDebutCheck() {
        return forDateDebutCheck;
    }

    /**
     * @return the forDateFinCheck
     */
    public String getForDateFinCheck() {
        return forDateFinCheck;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forNotIdDemande
     */
    public String getForNotIdDemande() {
        return forNotIdDemande;
    }

    /**
     * @return the forNotIdDossier
     */
    public String getForNotIdDossier() {
        return forNotIdDossier;
    }

    /**
     * @return the inIdSituationFamiliale
     */
    public List<String> getInIdSituationFamiliale() {
        return inIdSituationFamiliale;
    }

    public void setCsEtatDemande(String csEtatDemande) {
        this.csEtatDemande = csEtatDemande;
    }

    /**
     * @param forDateDebutCheck
     *            the forDateDebutCheck to set
     */
    public void setForDateDebutCheck(String forDateDebutCheck) {
        this.forDateDebutCheck = forDateDebutCheck;
    }

    /**
     * @param forDateFinCheck
     *            the forDateFinCheck to set
     */
    public void setForDateFinCheck(String forDateFinCheck) {
        this.forDateFinCheck = forDateFinCheck;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forNotIdDemande
     *            the forNotIdDemande to set
     */
    public void setForNotIdDemande(String forNotIdDemande) {
        this.forNotIdDemande = forNotIdDemande;
    }

    /**
     * @param forNotIdDossier
     *            the forNotIdDossier to set
     */
    public void setForNotIdDossier(String forNotIdDossier) {
        this.forNotIdDossier = forNotIdDossier;
    }

    /**
     * @param inIdSituationFamiliale
     *            the inIdSituationFamiliale to set
     */
    public void setInIdSituationFamiliale(List<String> inIdSituationFamiliale) {
        this.inIdSituationFamiliale = inIdSituationFamiliale;
    }

    @Override
    public Class whichModelClass() {
        return SimpleDemande.class;
    }

}
