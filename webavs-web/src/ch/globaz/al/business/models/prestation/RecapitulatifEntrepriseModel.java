package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.al.business.constantes.ALCSPrestation;

/**
 * Modèle simple du Récapitulatif entreprise
 * 
 * @author PTA
 * 
 */
public class RecapitulatifEntrepriseModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Type de bonification de la prestation (code système)
     */
    private String bonification = null;
    /**
     * Etat de la récap (SA, CO, ...), code système
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT
     */
    private String etatRecap = null;
    /**
     * Genre d'assurance
     * 
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_INDEP
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_PARITAIRE
     */
    private String genreAssurance = null;
    /**
     * Numéro du processus lié à la récap
     */
    private String idProcessusPeriodique = null;
    /**
     * id de la récap
     */
    private String idRecap = null;
    /**
     * numéro de l'affilié
     */
    private String numeroAffilie = null;
    /**
     * Numéro de la facture
     */
    private String numeroFacture = null;

    /**
     * Fin de la période pendant laquelle les prestations sont versées
     */
    private String periodeA = null;

    /**
     * Début de la période pendant laquelle les prestations sont versées
     */
    private String periodeDe = null;

    /**
     * @return the bonification
     */
    public String getBonification() {
        return bonification;
    }

    /**
     * @return the etatRecap
     */
    public String getEtatRecap() {
        return etatRecap;
    }

    /**
     * @return the genreAssurance
     */
    public String getGenreAssurance() {
        return genreAssurance;
    }

    /**
     * retourne l'idRecap
     */
    @Override
    public String getId() {
        return idRecap;
    }

    /**
     * 
     * @return idProcessusPeriodique
     */
    public String getIdProcessusPeriodique() {
        return idProcessusPeriodique;
    }

    /**
     * @return the idRecap
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return the numAffilie
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * @return the numeroFacture
     */
    public String getNumeroFacture() {
        return numeroFacture;
    }

    /**
     * @return the periodeA
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * @return the periodeDe
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /***
     * Définit si la récapitulation est saisie {@link ALCSPrestation#ETAT_SA}
     * 
     * @return true si saisie
     */
    public boolean isSaisie() {
        return ALCSPrestation.ETAT_SA.equals(getEtatRecap());
    }

    /***
     * Définit si la recapitulation est à l'état {@link ALCSPrestation#ETAT_PR} ou {@link ALCSPrestation#ETAT_SA}
     */
    public boolean isSaisieOuProvisoire() {
        return ALCSPrestation.ETAT_PR.equals(getEtatRecap()) || isSaisie();
    }

    /**
     * @param bonification
     *            the bonification to set
     */
    public void setBonification(String bonification) {
        this.bonification = bonification;
    }

    /**
     * @param etatRecap
     *            the etatRecap to set
     */
    public void setEtatRecap(String etatRecap) {
        this.etatRecap = etatRecap;
    }

    /**
     * @param genreAssurance
     *            the genreAssurance to set
     */
    public void setGenreAssurance(String genreAssurance) {
        this.genreAssurance = genreAssurance;
    }

    /**
     * Définit l'idRecap
     */
    @Override
    public void setId(String id) {
        idRecap = id;

    }

    /**
     * 
     * @param idProcessusPeriodique
     *            le processus périodique lié
     */
    public void setIdProcessusPeriodique(String idProcessusPeriodique) {
        this.idProcessusPeriodique = idProcessusPeriodique;
    }

    /**
     * @param idRecap
     *            the idRecap to set
     */
    public void setIdRecap(String idRecap) {
        this.idRecap = idRecap;
    }

    /**
     * @param numAffilie
     *            the numAffilie to set
     */
    public void setNumeroAffilie(String numAffilie) {
        numeroAffilie = numAffilie;
    }

    /**
     * @param numeroFacture
     *            the numeroFacture to set
     */
    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    /**
     * @param periodeA
     *            the periodeA to set
     */
    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    /**
     * @param periodeDe
     *            the periodeDe to set
     */
    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
    }

}
