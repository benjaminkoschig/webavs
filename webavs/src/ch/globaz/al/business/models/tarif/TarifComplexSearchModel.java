package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * classe représentant un modèle complexe de recherche de TarifComplexModel
 * 
 * @author PTA
 * 
 */
public class TarifComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur le critère de capacité d'éxercer
     */
    private Boolean forCapableExercer = null;
    /**
     * Recherche sur la catégorie de résident
     */
    private String forCategorieResident = null;
    /**
     * Recherche sur le critère d'âge
     */
    private String forCritereAge = null;
    /**
     * Recherche sur le critère de nombre
     */
    private String forCritereNombre = null;
    /**
     * Recherche sur le critère de rang
     */
    private String forCritereRang = null;
    /**
     * Recherche sur le critère de revenu
     */
    private String forCritereRevenuIndependant = null;
    /**
     * Recherche sur le critère de revenu
     */
    private String forCritereRevenuNonActif = null;

    /**
     * Permet la recherche d'un tarif dans les cas ou un critère de séparation "mois" ou "année" doit être utilisé. Il
     * permet d'indiquer l'âge de l'enfant
     * 
     * A utiliser en combinaison avec {@link TarifComplexSearchModel#forMoisSeparationMois}
     */
    private String forMoisSeparationAge = null;
    /**
     * Permet la recherche d'un tarif dans les cas ou un critère de séparation "mois" ou "année" doit être utilisé. Il
     * permet d'indiquer le mois du calcul
     * 
     * A utiliser en combinaison avec {@link TarifComplexSearchModel#forMoisSeparationAge}
     */
    private String forMoisSeparationMois = null;
    /**
     * Recherche sur le type de prestations
     * 
     * @see ch.globaz.al.business.constantes.ALCSDroit#GROUP_ETAT
     */
    private String forTypePrestation = null;
    /**
     * Recherche du tarif couvrant la date indiquée
     */
    private String forValidite = null;
    /**
     * Recherche des tarif correspondant à une liste de catégories
     */
    private Collection<String> inCategoriesTarif = null;
    /**
     * Recherche des tarif correspondant à une liste de législation
     */
    private Collection<String> inLegislations = null;

    /**
     * @return the forCapableExercer
     */
    public Boolean getForCapableExercer() {
        return forCapableExercer;
    }

    /**
     * @return the forCategorieResident
     */
    public String getForCategorieResident() {
        return forCategorieResident;
    }

    /**
     * @return the forCritereAge
     */
    public String getForCritereAge() {
        return forCritereAge;
    }

    /**
     * @return the forCritereNombre
     */
    public String getForCritereNombre() {
        return forCritereNombre;
    }

    /**
     * @return the forCritereRang
     */
    public String getForCritereRang() {
        return forCritereRang;
    }

    /**
     * @return the forCritereRevenuIndependant
     */
    public String getForCritereRevenuIndependant() {
        return forCritereRevenuIndependant;
    }

    /**
     * @return the forCritereRevenuNonActif
     */
    public String getForCritereRevenuNonActif() {
        return forCritereRevenuNonActif;
    }

    /**
     * @return the forMoisSeparationAge
     */
    public String getForMoisSeparationAge() {
        return forMoisSeparationAge;
    }

    /**
     * @return the forMoisSeparationMois
     */
    public String getForMoisSeparationMois() {
        return forMoisSeparationMois;
    }

    /**
     * @return the forTypePrestation
     */
    public String getForTypePrestation() {
        return forTypePrestation;
    }

    /**
     * @return la date pour laquelle la recherche est effectuée
     */
    public String getForValidite() {
        return forValidite;
    }

    /**
     * @return the inCategoriesTarif
     */
    public Collection<String> getInCategoriesTarif() {
        return inCategoriesTarif;
    }

    /**
     * @return the inLegislations
     */
    public Collection<String> getInLegislations() {
        return inLegislations;
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
     * @param forCritereAge
     *            the forCritereAge to set
     */
    public void setForCritereAge(String forCritereAge) {
        this.forCritereAge = forCritereAge;
    }

    /**
     * @param forCritereNombre
     *            the forCritereNombre to set
     */
    public void setForCritereNombre(String forCritereNombre) {
        this.forCritereNombre = forCritereNombre;
    }

    /**
     * @param forCritereRang
     *            the forCritereRang to set
     */
    public void setForCritereRang(String forCritereRang) {
        this.forCritereRang = forCritereRang;
    }

    /**
     * @param forCritereRevenuIndependant
     *            the forCritereRevenuIndependant to set
     */
    public void setForCritereRevenuIndependant(String forCritereRevenuIndependant) {
        this.forCritereRevenuIndependant = forCritereRevenuIndependant;
    }

    /**
     * @param forCritereRevenuNonActif
     *            the forCritereRevenuNonActif to set
     */
    public void setForCritereRevenuNonActif(String forCritereRevenuNonActif) {
        this.forCritereRevenuNonActif = forCritereRevenuNonActif;
    }

    /**
     * Permet la recherche d'un tarif dans les cas ou un critère de séparation "mois" ou "année" doit être utilisé. Il
     * permet d'indiquer l'âge de l'enfant
     * 
     * A utiliser en combinaison avec {@link TarifComplexSearchModel#setForMoisSeparationMois}
     * 
     * @param forMoisSeparationAge
     *            the forMoisSeparationAge to set
     */
    public void setForMoisSeparationAge(String forMoisSeparationAge) {
        this.forMoisSeparationAge = forMoisSeparationAge;
    }

    /**
     * Permet la recherche d'un tarif dans les cas ou un critère de séparation "mois" ou "année" doit être utilisé. Il
     * permet d'indiquer le mois du calcul
     * 
     * A utiliser en combinaison avec {@link TarifComplexSearchModel#setForMoisSeparationAge}
     * 
     * @param forMoisSeparationMois
     *            the forMoisSeparationMois to set
     */
    public void setForMoisSeparationMois(String forMoisSeparationMois) {
        this.forMoisSeparationMois = forMoisSeparationMois;
    }

    /**
     * @param forTypePrestation
     *            the forTypePrestation to set
     */
    public void setForTypePrestation(String forTypePrestation) {
        this.forTypePrestation = forTypePrestation;
    }

    /**
     * @param forValidite
     *            the forValidite to set
     */
    public void setForValidite(String forValidite) {
        this.forValidite = forValidite;
    }

    /**
     * @param inCategoriesTarif
     *            the inCategoriesTarif to set
     */
    public void setInCategoriesTarif(Collection<String> inCategoriesTarif) {
        this.inCategoriesTarif = inCategoriesTarif;
    }

    /**
     * @param inLegislations
     *            the inLegislations to set
     */
    public void setInLegislations(Collection<String> inLegislations) {
        this.inLegislations = inLegislations;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<TarifComplexModel> whichModelClass() {

        return TarifComplexModel.class;
    }

}
