package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

/**
 * Permet la recherche de droits. Est utilisé en particulier pour la vérification de l'existance d'un droit
 * 
 * @author jts
 * 
 */
public class DroitSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String SEARCH_DROIT_RADIES = "droitsRadies";

    /**
     * Recherche sur la date de début de validité du droiit
     */
    private String forDateDebut = null;

    /**
     * Recherche sur la date d'échéance force
     */
    private String forDateEcheanceForcee = null;

    /** Recherche sur l'état du droit */
    private String forEtatDroit = null;

    /**
     * Recherche par l'identifiant du dossier
     */
    private String forIdDossier = null;
    /**
     * Recherche par l'identifiant du droit
     */
    private String forIdDroit = null;

    /**
     * Recherche par l'identifiant de l'enfant
     */
    private String forIdEnfant = null;
    /**
     * Recherche sur le type de droit
     */
    private String forTypeDroit = null;
    /**
     * Recherche sur une liste de dossier
     */
    private Collection<String> inIdDossier = null;

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateEcheanceForcee
     */
    public String getForDateEcheanceForcee() {
        return forDateEcheanceForcee;
    }

    public String getForEtatDroit() {
        return forEtatDroit;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * @return the forTypeDroit
     */
    public String getForTypeDroit() {
        return forTypeDroit;
    }

    /**
     * @return the inIdDossier
     */
    public Collection<String> getInIdDossier() {
        return inIdDossier;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateEcheanceForcee
     *            the forDateEcheanceForcee to set
     */
    public void setForDateEcheanceForcee(String forDateEcheanceForcee) {
        this.forDateEcheanceForcee = forDateEcheanceForcee;
    }

    public void setForEtatDroit(String forEtatDroit) {
        this.forEtatDroit = forEtatDroit;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdEnfant
     *            the forIdEnfant to set
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * @param forTypeDroit
     *            the forTypeDroit to set
     */
    public void setForTypeDroit(String forTypeDroit) {
        this.forTypeDroit = forTypeDroit;
    }

    /**
     * @param inIdDossier
     *            the inIdDossier to set
     */
    public void setInIdDossier(Collection<String> inIdDossier) {
        this.inIdDossier = inIdDossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return DroitModel.class;
    }
}
