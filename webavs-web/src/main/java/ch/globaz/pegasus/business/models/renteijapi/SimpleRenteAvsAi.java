package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle simple pour la gestion des rentes avs/ai 6.2010
 * 
 * @author SCE
 * 
 */
public class SimpleRenteAvsAi extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypePc = null;
    private String csTypeRente = null; // type de la rente
    private String dateDecision = null; // date décision
    private String dateDepot = null; // date de depot
    private String dateEcheance = null; // date échéance
    private String degreInvalidite = null; // degré d'invalidité
    private String idDonneeFinanciereHeader = null; // clé Donnee
    // FinanciereHeader
    private String idRenteAvsAi = null; // clé primaire
    private String montant = null; // montant
    private String imputationFortune = null;

    /**
     * @return the csTypePc
     */
    public String getCsTypePc() {
        return csTypePc;
    }

    /**
     * @return the typeRente
     */
    public String getCsTypeRente() {
        return csTypeRente;
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

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    /**
     * Return the idRenteAvsAi
     */
    @Override
    public String getId() {
        return idRenteAvsAi;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idRentesAvsAi
     */
    public String getIdRenteAvsAi() {
        return idRenteAvsAi;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @param csTypePc
     *            the csTypePc to set
     */
    public void setCsTypePc(String csTypePc) {
        this.csTypePc = csTypePc;
    }

    /**
     * @param typeRente
     *            the typeRente to set
     */
    public void setCsTypeRente(String csTypeRente) {
        this.csTypeRente = csTypeRente;
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

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    /**
     * Set the id
     */
    @Override
    public void setId(String id) {
        idRenteAvsAi = id;

    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idRentesAvsAi
     *            the idRentesAvsAi to set
     */
    public void setIdRenteAvsAi(String idRenteAvsAi) {
        this.idRenteAvsAi = idRenteAvsAi;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getImputationFortune() {
        return imputationFortune;
    }

    public void setImputationFortune(String imputationFortune) {
        this.imputationFortune = imputationFortune;
    }

}
