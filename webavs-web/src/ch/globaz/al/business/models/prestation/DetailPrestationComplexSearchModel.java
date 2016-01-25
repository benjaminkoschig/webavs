package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * Mod�le de recherche sur les d�tails de prestations
 * 
 * @author GMO
 * 
 */
public class DetailPrestationComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String SEARCH_RADIATION_DOSSIER = "radiationDossier";

    /**
     * Recherche par �tat
     */
    private String forEtat = null;
    /**
     * Recherche par l'id dossier
     */
    private String forIdDossier = null;
    /**
     * Recherche par l'id droit
     */
    private String forIdDroit = null;

    /**
     * Recherche par l'id enfant
     */
    private String forIdEnfant = null;
    /**
     * REcherche par l'id ent�te
     */
    private String forIdEntete = null;

    /**
     * Recherche par p�riode fin
     */
    private String forPeriodeA = null;

    /**
     * Recherche par p�riode d�but
     */
    private String forPeriodeDe = null;

    /**
     * Recherche par statut
     */
    private String forStatut = null;
    /**
     * type prestation
     */
    private String forTarif = null;

    /**
     * Recherche sur une liste de droits
     */
    private Collection<String> inIdDroit = null;

    /**
     * Exclure la prestation suppl�ment horlofger pour le d�compte
     */
    private String notForTarif = null;

    /**
     * 
     * @return forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * @return forIdEntete
     */
    public String getForIdEntete() {
        return forIdEntete;
    }

    /**
     * 
     * @return forPeriodeA
     */
    public String getForPeriodeA() {
        return forPeriodeA;
    }

    /**
     * 
     * @return forPeriodeDe
     */
    public String getForPeriodeDe() {
        return forPeriodeDe;
    }

    /**
     * 
     * @return forStatut
     */
    public String getForStatut() {
        return forStatut;
    }

    public String getForTarif() {
        return forTarif;
    }

    public String getNotForTarif() {
        return notForTarif;
    }

    /**
     * 
     * @param forEtat
     *            l'�tat � d�finir
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;

    }

    /**
     * @param forIdDossier
     *            id du dossier pour lequel rechercher les prestations
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * 
     * @param forIdDroit
     *            id du droit pour lequel rechercher les prestations
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * 
     * @param forIdEnfant
     *            id de l'enfant pour lequel rechercher les prestations
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * 
     * @param forIdEntete
     *            id de l'en-t�te pour laquelle rechercher les prestations
     */
    public void setForIdEntete(String forIdEntete) {
        this.forIdEntete = forIdEntete;
    }

    /**
     * 
     * @param forPeriodeA
     *            la p�riode de d�but
     */
    public void setForPeriodeA(String forPeriodeA) {
        this.forPeriodeA = forPeriodeA;
    }

    /**
     * 
     * @param forPeriodeDe
     *            la p�riode d�but � d�finir
     */
    public void setForPeriodeDe(String forPeriodeDe) {
        this.forPeriodeDe = forPeriodeDe;
    }

    /**
     * 
     * @param forStatut
     *            le statut � d�finir
     */
    public void setForStatut(String forStatut) {
        this.forStatut = forStatut;
    }

    public void setForTarif(String forTarif) {
        this.forTarif = forTarif;
    }

    public void setNotForTarif(String notForTarif) {
        this.notForTarif = notForTarif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<DetailPrestationComplexModel> whichModelClass() {
        return (DetailPrestationComplexModel.class);
    }

    public void setInIdDroit(Collection<String> inIdDroit) {
        this.inIdDroit = inIdDroit;
    }

    public Collection<String> getInIdDroit() {
        return inIdDroit;
    }

}
