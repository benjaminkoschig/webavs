package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modele des SimpleallocationImpotents 6.2010
 * 
 * @author SCE
 * 
 */
public class SimpleAllocationImpotent extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csDegre = null; // degré de l'invalidité
    private String csGenre = null; // genre rente vieillesse, survivant, etc
    private String csTypeRente = null; // type de la rente, cs n° avec infobule?
    private String dateDecision = null; // date de la décision
    private String dateDepot = null; // date du depot
    private String idAllocationImpotent = null; // clé primaire
    private String idDonneeFinanciereHeader = null; // clé donnee financiere
    private String montant = null; // montant de l'allocation

    /**
     * @return the csDegre
     */
    public String getCsDegre() {
        return csDegre;
    }

    /**
     * @return the csGenre
     */
    public String getCsGenre() {
        return csGenre;
    }

    /**
     * @return the csTypeRente
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
     * return the id
     */
    @Override
    public String getId() {
        return idAllocationImpotent;
    }

    /**
     * @return the idAllocationImpotent
     */
    public String getIdAllocationImpotent() {
        return idAllocationImpotent;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @param csDegre
     *            the csDegre to set
     */
    public void setCsDegre(String csDegre) {
        this.csDegre = csDegre;
    }

    /**
     * @param csGenre
     *            the csGenre to set
     */
    public void setCsGenre(String csGenre) {
        this.csGenre = csGenre;
    }

    /**
     * @param csTypeRente
     *            the csTypeRente to set
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
     * set the id
     */
    @Override
    public void setId(String id) {
        idAllocationImpotent = id;

    }

    /**
     * @param idAllocationImpotent
     *            the idAllocationImpotent to set
     */
    public void setIdAllocationImpotent(String idAllocationImpotent) {
        this.idAllocationImpotent = idAllocationImpotent;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

}
