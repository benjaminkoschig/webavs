package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche de tarifs de prestation
 * 
 * @author jts
 * 
 */
public class PrestationTarifSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur la capacité d'éxercer
     */
    private Boolean forCapableExercer = null;
    /**
     * Recherche sur la catégorie de résident
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_RESIDENT
     */
    private String forCategorieResident = null;
    /**
     * Recherche sur la date de début de validité
     */
    private String forDebutValidite = null;
    /**
     * Recherche sur la date de fin de validité
     */
    private String forFinValidite = null;

    /**
     * Recherche sur l'id de la catégorie de tarif
     * 
     * @see CategorieTarifModel
     */
    private String forIdCategorieTarif = null;
    /**
     * Recherche sur l'id du critère d'âge
     * 
     * @see CritereTarifModel
     */
    private String forIdCritereAge = null;
    /**
     * Recherche sur l'id du critère de nombre
     * 
     * @see CritereTarifModel
     */
    private String forIdCritereNombre = null;
    /**
     * Recherche sur l'id du critère de rang
     * 
     * @see CritereTarifModel
     */
    private String forIdCritereRang = null;
    /**
     * Recherche sur l'id du critère de revenu d'indépendant
     * 
     * @see CritereTarifModel
     */
    private String forIdCritereRevenuIndependant = null;
    /**
     * Recherche sur du critère de revenu de non-actif
     * 
     * @see CritereTarifModel
     */
    private String forIdCritereRevenuNonActif = null;
    /**
     * Recherche sur le mois de séparation
     */
    private String forMoisSeparation = null;
    /**
     * Recherche sur le type de prestation
     * 
     * @see ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE
     */
    private String forTypePrestation = null;

    /**
     * @return the forCapableExercer
     */
    public Boolean getForCapableExercer() {
        return forCapableExercer;
    }

    /**
     * @return the forCategorieResident
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_RESIDENT
     */
    public String getForCategorieResident() {
        return forCategorieResident;
    }

    /**
     * @return the forDebutValidite
     */
    public String getForDebutValidite() {
        return forDebutValidite;
    }

    /**
     * @return the forFinValidite
     */
    public String getForFinValidite() {
        return forFinValidite;
    }

    /**
     * @return the forIdCategorieTarif
     * @see CategorieTarifModel
     */
    public String getForIdCategorieTarif() {
        return forIdCategorieTarif;
    }

    /**
     * @return the forIdCritereAge
     * @see CritereTarifModel
     */
    public String getForIdCritereAge() {
        return forIdCritereAge;
    }

    /**
     * @return the forIdCritereNombre
     * @see CritereTarifModel
     */
    public String getForIdCritereNombre() {
        return forIdCritereNombre;
    }

    /**
     * @return the forIdCritereRang
     * @see CritereTarifModel
     */
    public String getForIdCritereRang() {
        return forIdCritereRang;
    }

    /**
     * @return the forIdCritereRevenuIndependant
     * @see CritereTarifModel
     */
    public String getForIdCritereRevenuIndependant() {
        return forIdCritereRevenuIndependant;
    }

    /**
     * @return the forIdCritereRevenuNonActif
     * @see CritereTarifModel
     */
    public String getForIdCritereRevenuNonActif() {
        return forIdCritereRevenuNonActif;
    }

    /**
     * @return the forMoisSeparation
     */
    public String getForMoisSeparation() {
        return forMoisSeparation;
    }

    /**
     * @return the forTypePrestation
     * @see ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE
     */
    public String getForTypePrestation() {
        return forTypePrestation;
    }

    /**
     * @param forCapableExercer
     *            the forCapableExercer to set
     */
    public void setForCapableExercer(Boolean forCapableExercer) {
        this.forCapableExercer = forCapableExercer;
    }

    /**
     * @param forCategorieResident
     *            the forCategorieResident to set
     */
    public void setForCategorieResident(String forCategorieResident) {
        this.forCategorieResident = forCategorieResident;
    }

    /**
     * @param forDebutValidite
     *            the forDebutValidite to set
     */
    public void setForDebutValidite(String forDebutValidite) {
        this.forDebutValidite = forDebutValidite;
    }

    /**
     * @param forFinValidite
     *            the forFinValidite to set
     */
    public void setForFinValidite(String forFinValidite) {
        this.forFinValidite = forFinValidite;
    }

    /**
     * @param forIdCategorieTarif
     *            the forIdCategorieTarif to set
     */
    public void setForIdCategorieTarif(String forIdCategorieTarif) {
        this.forIdCategorieTarif = forIdCategorieTarif;
    }

    /**
     * @param forIdCritereAge
     *            the forIdCritereAge to set
     */
    public void setForIdCritereAge(String forIdCritereAge) {
        this.forIdCritereAge = forIdCritereAge;
    }

    /**
     * @param forIdCritereNombre
     *            the forIdCritereNombre to set
     */
    public void setForIdCritereNombre(String forIdCritereNombre) {
        this.forIdCritereNombre = forIdCritereNombre;
    }

    /**
     * @param forIdCritereRang
     *            the forIdCritereRang to set
     */
    public void setForIdCritereRang(String forIdCritereRang) {
        this.forIdCritereRang = forIdCritereRang;
    }

    /**
     * @param forIdCritereRevenuIndependant
     *            the forIdCritereRevenuIndependant to set
     */
    public void setForIdCritereRevenuIndependant(String forIdCritereRevenuIndependant) {
        this.forIdCritereRevenuIndependant = forIdCritereRevenuIndependant;
    }

    /**
     * @param forIdCritereRevenuNonActif
     *            the forIdCritereRevenuNonActif to set
     */
    public void setForIdCritereRevenuNonActif(String forIdCritereRevenuNonActif) {
        this.forIdCritereRevenuNonActif = forIdCritereRevenuNonActif;
    }

    /**
     * @param forMoisSeparation
     *            the forMoisSeparation to set
     */
    public void setForMoisSeparation(String forMoisSeparation) {
        this.forMoisSeparation = forMoisSeparation;
    }

    /**
     * @param forTypePrestation
     *            the forTypePrestation to set
     */
    public void setForTypePrestation(String forTypePrestation) {
        this.forTypePrestation = forTypePrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<PrestationTarifModel> whichModelClass() {
        return PrestationTarifModel.class;
    }

}
