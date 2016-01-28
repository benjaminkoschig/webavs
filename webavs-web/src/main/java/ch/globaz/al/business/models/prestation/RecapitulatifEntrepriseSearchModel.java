package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Permet la recherche d'une récap
 * 
 * @author jts
 * @see RecapitulatifEntrepriseModel
 */
public class RecapitulatifEntrepriseSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur le type de bonification
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI
     */
    private String forBonification = null;
    /**
     * Recherche sur l'état de la récap
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT
     */
    private String forEtatRecap = null;
    /**
     * recherche sur le genre d'assurance
     * 
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_INDEP
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_PARITAIRE
     */
    private String forGenreAssurance = null;
    /**
     * Recherche sur l'id de la récap
     */
    private String forIdRecap = null;
    /**
     * Recherche sur le numéro de l'affilié
     */
    private String forNumeroAffilie = null;

    /**
     * Recherche sur le numéro de facture
     */
    private String forNumeroFacture = null;
    /**
     * Recherche sur le processus lié
     */
    private String forNumProcessusLie = null;
    /**
     * Recherche sur le début de la période
     */
    private String forPeriodeA = null;

    /**
     * Recherche sur la fin de la période
     */
    private String forPeriodeDe = null;

    /**
     * @return the forBonification
     */
    public String getForBonification() {
        return forBonification;
    }

    /**
     * @return the forEtatRecap
     */
    public String getForEtatRecap() {
        return forEtatRecap;
    }

    /**
     * @return the forGenreAssurance
     */
    public String getForGenreAssurance() {
        return forGenreAssurance;
    }

    /**
     * @return the forIdRecap
     */
    public String getForIdRecap() {
        return forIdRecap;
    }

    /**
     * @return the forNumeroAffilie
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * @return the forNumeroFacture
     */
    public String getForNumeroFacture() {
        return forNumeroFacture;
    }

    public String getForNumProcessusLie() {
        return forNumProcessusLie;
    }

    /**
     * @return the forPeriodeA
     */
    public String getForPeriodeA() {
        return forPeriodeA;
    }

    /**
     * @return the forPeriodeDe
     */
    public String getForPeriodeDe() {
        return forPeriodeDe;
    }

    /**
     * @param forBonification
     *            the forBonification to set
     */
    public void setForBonification(String forBonification) {
        this.forBonification = forBonification;
    }

    /**
     * @param forEtatRecap
     *            the forEtatRecap to set
     */
    public void setForEtatRecap(String forEtatRecap) {
        this.forEtatRecap = forEtatRecap;
    }

    /**
     * @param forGenreAssurance
     *            le genre d'assurance
     * 
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_INDEP
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_PARITAIRE
     */
    public void setForGenreAssurance(String forGenreAssurance) {
        this.forGenreAssurance = forGenreAssurance;
    }

    /**
     * @param forIdRecap
     *            the forIdRecap to set
     */
    public void setForIdRecap(String forIdRecap) {
        this.forIdRecap = forIdRecap;
    }

    /**
     * @param forNumeroAffilie
     *            the forNumeroAffilie to set
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * @param forNumeroFacture
     *            the forNumeroFacture to set
     */
    public void setForNumeroFacture(String forNumeroFacture) {
        this.forNumeroFacture = forNumeroFacture;
    }

    public void setForNumProcessusLie(String forNumProcessusLie) {
        this.forNumProcessusLie = forNumProcessusLie;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<RecapitulatifEntrepriseModel> whichModelClass() {
        return RecapitulatifEntrepriseModel.class;
    }

}
