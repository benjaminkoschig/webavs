package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle pour la table IndeminiteJounaliere AI 6.2010
 * 
 * @author SCE
 * 
 */
public class SimpleIndemniteJournaliereAi extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeIjai = null; // type d'indemnite cs PCTYPIJAI
    private String dateDecision = null; // date de la décision
    private String dateDepot = null; // date de dépot
    private String dateEcheance = null; // date d'échéance
    private String idDonneeFinanciereHeader = null; // clé donnee financiere
    // header
    private String idIndemniteJournaliereAi = null; // clé primaire
    private String montant = null; // montant
    private String nbreJours = null; // /nombre de jours

    /**
     * @return the csTypeIJAI
     */
    public String getCsTypeIjai() {
        return csTypeIjai;
    }

    /**
     * @return the dateDecision
     */
    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return the dateDepot
     */
    public String getDateDepot() {
        return dateDepot;
    }

    /**
     * @return the dateEcheance
     */
    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * Retourne l'id
     */
    @Override
    public String getId() {
        return idIndemniteJournaliereAi;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idIndemniteJournaliereAi
     */
    public String getIdIndemniteJournaliereAi() {
        return idIndemniteJournaliereAi;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the nbreJours
     */
    public String getNbreJours() {
        return nbreJours;
    }

    /**
     * @param csTypeIJAI
     *            the csTypeIJAI to set
     */
    public void setCsTypeIjai(String csTypeIjai) {
        this.csTypeIjai = csTypeIjai;
    }

    /**
     * @param dateDecision
     *            the dateDecision to set
     */
    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    /**
     * @param dateDepot
     *            the dateDepot to set
     */
    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    /**
     * @param dateEcheance
     *            the dateEcheance to set
     */
    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /**
     * set l'id
     */
    @Override
    public void setId(String id) {
        idIndemniteJournaliereAi = id;

    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idIndemniteJournaliereAi
     *            the idIndemniteJournaliereAi to set
     */
    public void setIdIndemniteJournaliereAi(String idIndemniteJournaliereAi) {
        this.idIndemniteJournaliereAi = idIndemniteJournaliereAi;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param nbreJours
     *            the nbreJours to set
     */
    public void setNbreJours(String nbreJours) {
        this.nbreJours = nbreJours;
    }

}
