package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

/**
 * Classe de recherche pour l'impression des récapitulatifs entreprise
 * 
 * @author PTA
 * 
 */
public class RecapitulatifEntrepriseImpressionComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * etat dela récap
     */
    private String forEtatRecap = null;
    /**
     * identifiant de la recap
     */
    private String forIdRecap = null;
    /**
     * recherche sur l'idtiersBenficiaire ( =0)
     */
    private String forIdTiersBeneficiaire = null;

    /**
     * numéro du lot de la génération
     */
    private String forNumeroLot = null;

    /**
     * numéro de lot à null ou à zéro
     */
    private String forNumeroLotNull = null;

    /**
     * rechercher sur les récap lié au processus
     */
    private String forNumProcessus = null;

    /**
     * fin de la période pour la recherche
     */
    private String forPeriodeA = null;
    /**
     * Début de la période pour la recherche
     */
    private String forPeriodeDe = null;
    /**
     * recherche sur l'activité de l'allocataire
     */
    private String forTypeAlloc = null;
    /**
     * type de liaison pour notamment l'agence communale
     */
    private String forTypeLiaison = null;
    /**
     * recherche sur les types de bonification
     */
    private Collection<String> inTypeBonification = null;
    /**
     * recherche sur idTiersBeneficiaire différent de
     */
    private String notForIdtiersBeneficiaire = null;
    /**
     * recherche sur les activités allocataires
     */
    private Collection<String> notInActiviteAlloc = null;

    /**
     * @return the forEtatRecap
     */
    public String getForEtatRecap() {
        return forEtatRecap;
    }

    /**
     * @return the foridRecap
     */
    public String getForIdRecap() {
        return forIdRecap;
    }

    /**
     * @return the idTiersBeneficiaire
     */
    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    /**
     * @return the forNumeroLot
     */
    public String getForNumeroLot() {
        return forNumeroLot;
    }

    /**
     * @return the forNumeroLotNull
     */
    public String getForNumeroLotNull() {
        return forNumeroLotNull;
    }

    /**
     * 
     * @return forNumProcessus
     */
    public String getForNumProcessus() {
        return forNumProcessus;
    }

    /**
     * @return the forPeriodeA
     */
    public String getForPeriodeA() {
        return forPeriodeA;
    }

    // /**
    // * @return the typeBonification
    // */
    // public String getForTypeBonification() {
    // return this.forTypeBonification;
    // }

    /**
     * @return the forPeriodeDe
     */
    public String getForPeriodeDe() {
        return forPeriodeDe;
    }

    /**
     * @return the forTypeAlloc
     */
    public String getForTypeAlloc() {
        return forTypeAlloc;
    }

    /**
     * @return the forTypeLiaison
     */
    public String getForTypeLiaison() {
        return forTypeLiaison;
    }

    /**
     * @return the inTypeBonification
     */
    public Collection<String> getInTypeBonification() {
        return inTypeBonification;
    }

    /**
     * @return the notForIdtiersBeneficiaire
     */
    public String getNotForIdtiersBeneficiaire() {
        return notForIdtiersBeneficiaire;
    }

    /**
     * @return the notInActiviteAlloc
     */
    public Collection<String> getNotInActiviteAlloc() {
        return notInActiviteAlloc;
    }

    /**
     * @param forEtatRecap
     *            the notForEtatRecap to set
     */
    public void setForEtatRecap(String forEtatRecap) {
        this.forEtatRecap = forEtatRecap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */

    /**
     * @param forIdRecap
     *            the forIdRecap to set
     */
    public void setForIdRecap(String forIdRecap) {
        this.forIdRecap = forIdRecap;
    }

    /**
     * @param forIdTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    // /**
    // * @param forTypeBonification
    // * the typeBonification to set
    // */
    // public void setForTypeBonification(String forTypeBonification) {
    // this.forTypeBonification = forTypeBonification;
    // }

    /**
     * @param forNumeroLot
     *            the forNumeroLot to set
     */
    public void setForNumeroLot(String forNumeroLot) {
        this.forNumeroLot = forNumeroLot;
    }

    /**
     * @param forNumeroLotNull
     *            the forNumeroLotNull to set
     */
    public void setForNumeroLotNull(String forNumeroLotNull) {
        this.forNumeroLotNull = forNumeroLotNull;
    }

    /**
     * 
     * @param forNumProcessus
     *            the forNumProcessus
     */
    public void setForNumProcessus(String forNumProcessus) {
        this.forNumProcessus = forNumProcessus;
    }

    /**
     * @param forPeriodeA
     *            the forPeriodeA to set
     */
    public void setForPeriodeA(String forPeriodeA) {
        this.forPeriodeA = forPeriodeA;
    }

    /**
     * @param forPeriodeDe
     *            the forPeriodeDe to set
     */
    public void setForPeriodeDe(String forPeriodeDe) {
        this.forPeriodeDe = forPeriodeDe;
    }

    /**
     * @param forTypeAlloc
     *            the forTypeAlloc to set
     */
    public void setForTypeAlloc(String forTypeAlloc) {
        this.forTypeAlloc = forTypeAlloc;
    }

    /**
     * @param forTypeLiaison
     *            the forTypeLiaison to set
     */
    public void setForTypeLiaison(String forTypeLiaison) {
        this.forTypeLiaison = forTypeLiaison;
    }

    /**
     * @param inTypeBonification
     *            the inTypeBonification to set
     */
    public void setInTypeBonification(Collection<String> inTypeBonification) {
        this.inTypeBonification = inTypeBonification;
    }

    /**
     * @param notForIdtiersBeneficiaire
     *            the notEqualsIdtiersBeneficiaire to set
     */
    public void setNotForIdtiersBeneficiaire(String notForIdtiersBeneficiaire) {
        this.notForIdtiersBeneficiaire = notForIdtiersBeneficiaire;
    }

    /**
     * @param notInActiviteAlloc
     *            the notInActiviteAlloc to set
     */
    public void setNotInActiviteAlloc(Collection<String> notInActiviteAlloc) {
        this.notInActiviteAlloc = notInActiviteAlloc;
    }

    @Override
    public Class<RecapitulatifEntrepriseImpressionComplexModel> whichModelClass() {
        return RecapitulatifEntrepriseImpressionComplexModel.class;
    }

}
