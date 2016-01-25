package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.al.business.constantes.ALCSPrestation;

/**
 * Mod�le simple du R�capitulatif entreprise
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
     * Type de bonification de la prestation (code syst�me)
     */
    private String bonification = null;
    /**
     * Etat de la r�cap (SA, CO, ...), code syst�me
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
     * Num�ro du processus li� � la r�cap
     */
    private String idProcessusPeriodique = null;
    /**
     * id de la r�cap
     */
    private String idRecap = null;
    /**
     * num�ro de l'affili�
     */
    private String numeroAffilie = null;
    /**
     * Num�ro de la facture
     */
    private String numeroFacture = null;

    /**
     * Fin de la p�riode pendant laquelle les prestations sont vers�es
     */
    private String periodeA = null;

    /**
     * D�but de la p�riode pendant laquelle les prestations sont vers�es
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
     * D�finit si la r�capitulation est saisie {@link ALCSPrestation#ETAT_SA}
     * 
     * @return true si saisie
     */
    public boolean isSaisie() {
        return ALCSPrestation.ETAT_SA.equals(getEtatRecap());
    }

    /***
     * D�finit si la recapitulation est � l'�tat {@link ALCSPrestation#ETAT_PR} ou {@link ALCSPrestation#ETAT_SA}
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
     * D�finit l'idRecap
     */
    @Override
    public void setId(String id) {
        idRecap = id;

    }

    /**
     * 
     * @param idProcessusPeriodique
     *            le processus p�riodique li�
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
