package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Permet d'effectuer des recherches dans les en-tête de prestations
 * 
 * Critères supportés :
 * <ul>
 * <li>forIdDossier</li>
 * <li>forIdEntete</li>
 * <li>forIdRecap</li>
 * <li>forPeriode</li>
 * <li>forMontantTotal</li>
 * </ul>
 * 
 * @author jts
 * 
 */
public class EntetePrestationSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère état
     */
    private String forEtat = null;
    /**
     * Sélection sur l'id de dossier
     */
    private String forIdDossier = null;
    /**
     * Sélection sur l'id de l'en-tête
     */
    private String forIdEntete = null;
    /**
     * Recherche sur un numéro de journal OSIRIS
     */
    private String forIdJournal = null;
    /**
     * Recherche sur le numéro de passage de la facturation
     */
    private String forIdPassage = null;
    /**
     * Sélection sur l'id de la récap
     */
    private String forIdRecap = null;
    /**
     * Recherche sur le montant de la prestation
     */
    private String forMontantTotal = null;

    /**
     * Recherche sur une période
     */
    private String forPeriode = null;

    /**
     * critère recherche période fin
     */
    private String forPeriodeA = null;

    /**
     * Critère recherche période début
     */
    private String forPeriodeDe = null;

    /**
     * Critère recherche statut
     */
    private String forStatut = null;

    /**
     * @return forEtat
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdEntete
     */
    public String getForIdEntete() {
        return forIdEntete;
    }

    /**
     * @return the forIdPassage
     */
    public String getForIdPassage() {
        return forIdPassage;
    }

    /**
     * @return the forIdRecap
     */
    public String getForIdRecap() {
        return forIdRecap;
    }

    /**
     * @return the forMontantTotal
     */
    public String getForMontantTotal() {
        return forMontantTotal;
    }

    /**
     * @return the forPeriode
     */
    public String getForPeriode() {
        return forPeriode;
    }

    /**
     * 
     * @return forPeriodeA
     */
    public String getForPeriodeA() {
        return forPeriodeA;
    }

    /**
     * @return forPeriodeDe
     */
    public String getForPeriodeDe() {
        return forPeriodeDe;
    }

    /**
     * @return forStatut
     */
    public String getForStatut() {
        return forStatut;
    }

    /**
     * @param forEtat
     *            the etat to define
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdEntete
     *            the forIdEntete to set
     */
    public void setForIdEntete(String forIdEntete) {
        this.forIdEntete = forIdEntete;
    }

    /**
     * @param forIdPassage
     *            the forIdPassage to set
     */
    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    /**
     * @param forIdRecap
     *            the forIdRecap to set
     */
    public void setForIdRecap(String forIdRecap) {
        this.forIdRecap = forIdRecap;
    }

    /**
     * @param forMontantTotal
     *            the forMontant to set
     */
    public void setForMontantTotal(String forMontantTotal) {
        this.forMontantTotal = forMontantTotal;
    }

    /**
     * @param forPeriode
     *            the forPeriode to set
     */
    public void setForPeriode(String forPeriode) {
        this.forPeriode = forPeriode;
    }

    /**
     * @param forPeriodeA
     *            la période fin à définir
     */
    public void setForPeriodeA(String forPeriodeA) {
        this.forPeriodeA = forPeriodeA;
    }

    /**
     * @param forPeriodeDe
     *            la période début à définir
     */
    public void setForPeriodeDe(String forPeriodeDe) {
        this.forPeriodeDe = forPeriodeDe;
    }

    /**
     * @param forStatut
     *            le statut à définir
     */
    public void setForStatut(String forStatut) {
        this.forStatut = forStatut;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<EntetePrestationModel> whichModelClass() {
        return EntetePrestationModel.class;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

}
