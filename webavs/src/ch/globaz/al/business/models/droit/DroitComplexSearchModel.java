package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * Classe de recherche complexe sur les droits
 * 
 * @author GMO
 * 
 */
public class DroitComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ORDER_CALCUL_DROITS = "calculDroits";
    public static final String ORDER_ECHEANCES_DROIT = "echeancesDroit";
    public static final String SEARCH_DROIT_PLAGE_INCLUSE = "chevauchementDroitsExistants";
    public static final String SEARCH_DROITS_FOR_ID_TIERS = "droitsForIdTiers";
    public static final String SEARCH_ECHEANCES_DROIT = "echeancesDroit";

    /**
     * Recherche sur la date de début de droit
     */
    private String forDebutDroit = null;

    /**
     * Recherche sur la date d'échéance force
     */
    private String forFinDroitForcee = null;

    /**
     * Recherche sur l'identifiant du dossier
     */
    private String forIdDossier = null;

    /**
     * Recherche sur l'identifiant du droit
     */
    private String forIdDroit = null;
    /**
     * Recherche sur l'identifiant de l'enfant
     */
    private String forIdEnfant = null;

    /** Recherche sur l'idTiers */
    private String forIdTiersEnfant = null;

    /**
     * Recherche sur le type de droit
     */
    private String forTypeDroit = null;

    /**
     * Recherche selon une liste de types de droit
     */
    private Collection<String> inTypeDroit = null;

    public String getForDebutDroit() {
        return forDebutDroit;
    }

    /**
     * @return the forFinDroitForcee
     */
    public String getForFinDroitForcee() {
        return forFinDroitForcee;
    }

    /**
     * 
     * @return forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * 
     * @return forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * 
     * @return forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    public String getForIdTiersEnfant() {
        return forIdTiersEnfant;
    }

    /**
     * @return the forTypeDroit
     */
    public String getForTypeDroit() {
        return forTypeDroit;
    }

    public Collection<String> getInTypeDroit() {
        return inTypeDroit;
    }

    public void setForDebutDroit(String forDebutDroit) {
        this.forDebutDroit = forDebutDroit;
    }

    /**
     * @param forFinDroitForcee
     *            the forFinDroitForcee to set
     */
    public void setForFinDroitForcee(String forFinDroitForcee) {
        this.forFinDroitForcee = forFinDroitForcee;
    }

    /**
     * 
     * @param forIdDossier
     *            : the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * 
     * @param forIdDroit
     *            : the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * 
     * @param forIdEnfant
     *            : the forIdEnfant to set
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    public void setForIdTiersEnfant(String forIdTiersEnfant) {
        this.forIdTiersEnfant = forIdTiersEnfant;
    }

    /**
     * @param forTypeDroit
     *            the forTypeDroit to set
     */
    public void setForTypeDroit(String forTypeDroit) {
        this.forTypeDroit = forTypeDroit;
    }

    public void setInTypeDroit(Collection<String> inTypeDroit) {
        this.inTypeDroit = inTypeDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<DroitComplexModel> whichModelClass() {
        return DroitComplexModel.class;
    }
}