package ch.globaz.amal.business.models.detailfamille;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.ArrayList;
import java.util.Collection;

public class SimpleDetailFamilleSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeHistorique = null;
    private Boolean forAnnonceCaisseMaladie = null;
    private Boolean forCodeActif = null;
    private String forCodeTraitement = null;
    private String forCodeTraitementDossier = null;
    private String forDateAnnonceCaisseMaladie = null;
    private String forDebutDroit = null;
    private String forFinDroit = null;
    private String forGOEAnneeHistorique = null;
    private String forGOEDebutDroit = null;
    private String forGOEFinDroit = null;
    private String forIdContribuable = null;
    private String forIdDetailFamille = null;
    private String forIdFamille = null;
    private String forLOEFinDroit = null;
    private String forNoCaisseMaladies = null;
    private Boolean forRefus = null;
    private String forTypeDemande = null;
    private ArrayList<String> inCodeTraitementDossier = null;
    private ArrayList<String> inIdFamille = null;
    private Collection<String> inNoCaisseMaladies = null;
    private Collection<String> inNoModeles = null;
    private ArrayList<String> inTypeDemande = null;

    /**
     * @return the forAnneeHistorique
     */
    public String getForAnneeHistorique() {
        return forAnneeHistorique;
    }

    /**
     * @return the forAnnonceCaisseMaladie
     */
    public Boolean getForAnnonceCaisseMaladie() {
        return forAnnonceCaisseMaladie;
    }

    public Boolean getForCodeActif() {
        return forCodeActif;
    }

    public String getForCodeTraitement() {
        return forCodeTraitement;
    }

    public String getForCodeTraitementDossier() {
        return forCodeTraitementDossier;
    }

    /**
     * @return the forDateAnnonceCaisseMaladie
     */
    public String getForDateAnnonceCaisseMaladie() {
        return forDateAnnonceCaisseMaladie;
    }

    /**
     * @return the forDebutDroit
     */
    public String getForDebutDroit() {
        return forDebutDroit;
    }

    public String getForFinDroit() {
        return forFinDroit;
    }

    public String getForGOEAnneeHistorique() {
        return forGOEAnneeHistorique;
    }

    public String getForGOEDebutDroit() {
        return forGOEDebutDroit;
    }

    public String getForGOEFinDroit() {
        return forGOEFinDroit;
    }

    /**
     * @return the forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @return the forIdDetailFamille
     */
    public String getForIdDetailFamille() {
        return forIdDetailFamille;
    }

    /**
     * @return the forIdFamille
     */
    public String getForIdFamille() {
        return forIdFamille;
    }

    public String getForLOEFinDroit() {
        return forLOEFinDroit;
    }

    public String getForNoCaisseMaladies() {
        return forNoCaisseMaladies;
    }

    /**
     * @return the forRefus
     */
    public Boolean getForRefus() {
        return forRefus;
    }

    public String getForTypeDemande() {
        return forTypeDemande;
    }

    public ArrayList<String> getInCodeTraitementDossier() {
        return inCodeTraitementDossier;
    }

    public ArrayList<String> getInIdFamille() {
        return inIdFamille;
    }

    public Collection<String> getInNoCaisseMaladies() {
        return inNoCaisseMaladies;
    }

    /**
     * @return the forNoModeles
     */
    public Collection<String> getInNoModeles() {
        return inNoModeles;
    }

    public ArrayList<String> getInTypeDemande() {
        return inTypeDemande;
    }

    /**
     * @param forAnneeHistorique
     *            the forAnneeHistorique to set
     */
    public void setForAnneeHistorique(String forAnneeHistorique) {
        this.forAnneeHistorique = forAnneeHistorique;
    }

    /**
     * @param forAnnonceCaisseMaladie
     *            the forAnnonceCaisseMaladie to set
     */
    public void setForAnnonceCaisseMaladie(Boolean forAnnonceCaisseMaladie) {
        this.forAnnonceCaisseMaladie = forAnnonceCaisseMaladie;
    }

    public void setForCodeActif(Boolean forCodeActif) {
        this.forCodeActif = forCodeActif;
    }

    public void setForCodeTraitement(String forCodeTraitement) {
        this.forCodeTraitement = forCodeTraitement;
    }

    public void setForCodeTraitementDossier(String forCodeTraitementDossier) {
        this.forCodeTraitementDossier = forCodeTraitementDossier;
    }

    /**
     * @param forDateAnnonceCaisseMaladie
     *            the forDateAnnonceCaisseMaladie to set
     */
    public void setForDateAnnonceCaisseMaladie(String forDateAnnonceCaisseMaladie) {
        this.forDateAnnonceCaisseMaladie = forDateAnnonceCaisseMaladie;
    }

    /**
     * @param forDebutDroit
     *            the forDebutDroit to set
     */
    public void setForDebutDroit(String forDebutDroit) {
        this.forDebutDroit = forDebutDroit;
    }

    public void setForFinDroit(String forFinDroit) {
        this.forFinDroit = forFinDroit;
    }

    public void setForGOEAnneeHistorique(String forGOEAnneeHistorique) {
        this.forGOEAnneeHistorique = forGOEAnneeHistorique;
    }

    public void setForGOEDebutDroit(String forGOEDebutDroit) {
        this.forGOEDebutDroit = forGOEDebutDroit;
    }

    public void setForGOEFinDroit(String forGOEFinDroit) {
        this.forGOEFinDroit = forGOEFinDroit;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /**
     * @param forIdDetailFamille
     *            the forIdDetailFamille to set
     */
    public void setForIdDetailFamille(String forIdDetailFamille) {
        this.forIdDetailFamille = forIdDetailFamille;
    }

    /**
     * @param forIdFamille
     *            the forIdFamille to set
     */
    public void setForIdFamille(String forIdFamille) {
        this.forIdFamille = forIdFamille;
    }

    public void setForLOEFinDroit(String forLOEFinDroit) {
        this.forLOEFinDroit = forLOEFinDroit;
    }

    /**
     * @param forNoCaisseMaladies
     *            the forNoCaisseMaladies to set
     */
    public void setForNoCaisseMaladies(String forNoCaisseMaladies) {
        this.forNoCaisseMaladies = forNoCaisseMaladies;
    }

    /**
     * @param forRefus
     *            the forRefus to set
     */
    public void setForRefus(Boolean forRefus) {
        this.forRefus = forRefus;
    }

    public void setForTypeDemande(String forTypeDemande) {
        this.forTypeDemande = forTypeDemande;
    }

    public void setInCodeTraitementDossier(ArrayList<String> inCodeTraitementDossier) {
        this.inCodeTraitementDossier = inCodeTraitementDossier;
    }

    public void setInIdFamille(ArrayList<String> inIdFamille) {
        this.inIdFamille = inIdFamille;
    }

    public void setInNoCaisseMaladies(Collection<String> inNoCaisseMaladies) {
        this.inNoCaisseMaladies = inNoCaisseMaladies;
    }

    /**
     * @param forNoModeles
     *            the forNoModeles to set
     */
    public void setInNoModeles(Collection<String> inNoModeles) {
        this.inNoModeles = inNoModeles;
    }

    public void setInTypeDemande(ArrayList<String> inTypeDemande) {
        this.inTypeDemande = inTypeDemande;
    }

    @Override
    public Class whichModelClass() {
        return SimpleDetailFamille.class;
    }

}
