package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle de recherche de dossier pour l'impression des déclarations de versement
 * 
 * @author PTA
 * 
 */
public class DossierDeclarationVersementComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * type d'activité de l'allocataire
     */
    public String forActiviteAlloc = null;
    /**
     * date de départ de la période à prendre en compte pour la déclaration de versement
     */
    private String forDateDebut = null;
    /**
     * date de fin de la période à prendre en compte pour la déclaration de versment
     */
    private String forDateFin = null;
    /**
     * Etat de la prestation
     */
    private String forEtatPrestation = null;
    /**
     * identifiant du dossier pour la recherche par dossier
     */
    private String forIdDossier = null;
    /**
     * recherche sur l'identifiant du bénéficiaire
     */
    private String forIdTiersBeneficaire = null;
    /**
     * imposition à la source
     */
    private Boolean forImpotSource = null;
    /**
     * recherche sur impôt à la source à false
     */
    private Boolean forImpotSourceFalse = null;

    /**
     * Recherche sur impot à la source à true
     */
    private Boolean forImpotSourceTrue = null;

    /**
     * recherche sur idTiersBenefciaire pas égal
     */
    private String forNotIdTiersBeneficiaire = null;
    /**
     * recherche par le numéro de l'affilié
     */
    private String forNumAffilie = null;
    /**
     * type de permis
     */
    public String forPermis = null;

    /**
     * type de bonification
     */
    private String forTypeBonification = null;

    /**
     * Recherche sur type de bonification à direc
     */

    private String forTypeBonificationDir = null;

    /**
     * Recherche sur type bonification à indirec
     */

    private String forTypeBonificationInd = null;

    /**
     * @return the forActiviteAlloc
     */
    public String getForActiviteAlloc() {
        return forActiviteAlloc;
    }

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forEtatPrestation
     */
    public String getForEtatPrestation() {
        return forEtatPrestation;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdTiersBeneficaire
     */
    public String getForIdTiersBeneficaire() {
        return forIdTiersBeneficaire;
    }

    /**
     * @return the forImpotSource
     */
    public Boolean getForImpotSource() {
        return forImpotSource;
    }

    /**
     * @return the forImpotSourceFalse
     */
    public Boolean getForImpotSourceFalse() {
        return forImpotSourceFalse;
    }

    /**
     * @return the forImpotSourceTrue
     */
    public Boolean getForImpotSourceTrue() {
        return forImpotSourceTrue;
    }

    /**
     * @return the forNotIdTiersBeneficiaire
     */
    public String getForNotIdTiersBeneficiaire() {
        return forNotIdTiersBeneficiaire;
    }

    /**
     * recherche sur les types de bonification
     */
    // private Collection inTypeBonification = null;

    /**
     * @return the forNumAffilie
     */
    public String getForNumAffilie() {
        return forNumAffilie;
    }

    /**
     * @return the forPermis
     */
    public String getForPermis() {
        return forPermis;
    }

    /**
     * @return the forTypeBonification
     */
    public String getForTypeBonification() {
        return forTypeBonification;
    }

    /**
     * @return the forTypeBonificationDir
     */
    public String getForTypeBonificationDir() {
        return forTypeBonificationDir;
    }

    /**
     * @return the forTypeBonificationInd
     */
    public String getForTypeBonificationInd() {
        return forTypeBonificationInd;
    }

    // /**
    // * @return the forNotPermis
    // */
    // public String getForNotPermis() {
    // return this.forNotPermis;
    // }

    /**
     * @param forActiviteAlloc
     *            the forActiviteAlloc to set
     */
    public void setForActiviteAlloc(String forActiviteAlloc) {
        this.forActiviteAlloc = forActiviteAlloc;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    // /**
    // * @return the inTypeBonification
    // */
    // public Collection getInTypeBonification() {
    // return this.inTypeBonification;
    // }

    /**
     * @param forEtatPrestation
     *            the forEtatPrestation to set
     */
    public void setForEtatPrestation(String forEtatPrestation) {
        this.forEtatPrestation = forEtatPrestation;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdTiersBeneficaire
     *            the forIdTiersBeneficaire to set
     */
    public void setForIdTiersBeneficaire(String forIdTiersBeneficaire) {
        this.forIdTiersBeneficaire = forIdTiersBeneficaire;
    }

    /**
     * @param forImpotSource
     *            the forImpotSource to set
     */
    public void setForImpotSource(Boolean forImpotSource) {
        this.forImpotSource = forImpotSource;
    }

    /**
     * @param forImpotSourceFalse
     *            the forImpotSourceFalse to set
     */
    public void setForImpotSourceFalse(Boolean forImpotSourceFalse) {
        this.forImpotSourceFalse = forImpotSourceFalse;
    }

    /**
     * @param forImpotSourceTrue
     *            the forImpotSourceTrue to set
     */
    public void setForImpotSourceTrue(Boolean forImpotSourceTrue) {
        this.forImpotSourceTrue = forImpotSourceTrue;
    }

    /**
     * @param forNotIdTiersBeneficiaire
     *            the forNotIdTiersBeneficiaire to set
     */
    public void setForNotIdTiersBeneficiaire(String forNotIdTiersBeneficiaire) {
        this.forNotIdTiersBeneficiaire = forNotIdTiersBeneficiaire;
    }

    /**
     * @param forNumAffilie
     *            the forNumAffilie to set
     */
    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    // /**
    // * @param forNotPermis
    // * the forNotPermis to set
    // */
    // public void setForNotPermis(String forNotPermis) {
    // this.forNotPermis = forNotPermis;
    // }

    /**
     * @param forPermis
     *            the forPermis to set
     */
    public void setForPermis(String forPermis) {
        this.forPermis = forPermis;
    }

    /**
     * @param forTypeBonification
     *            the forTypeBonification to set
     */
    public void setForTypeBonification(String forTypeBonification) {
        this.forTypeBonification = forTypeBonification;
    }

    /**
     * @param forTypeBonificationDir
     *            the forTypeBonificationDir to set
     */
    public void setForTypeBonificationDir(String forTypeBonificationDir) {
        this.forTypeBonificationDir = forTypeBonificationDir;
    }

    // /**
    // * @param inTypeBonification
    // * the inTypeBonification to set
    // */
    // public void setInTypeBonification(Collection inTypeBonification) {
    // this.inTypeBonification = inTypeBonification;
    // }

    /**
     * @param forTypeBonificationInd
     *            the forTypeBonificationInd to set
     */
    public void setForTypeBonificationInd(String forTypeBonificationInd) {
        this.forTypeBonificationInd = forTypeBonificationInd;
    }

    @Override
    public Class<DossierDeclarationVersementComplexModel> whichModelClass() {
        return DossierDeclarationVersementComplexModel.class;
    }

}
