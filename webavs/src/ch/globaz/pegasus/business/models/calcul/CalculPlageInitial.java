/**
 * 
 */
package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author ECO
 * 
 */
public class CalculPlageInitial extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeDonneeFinanciere = null;
    private String dateDebut = null;
    private String dateDecisionApiAvsAi = null;
    private String dateDecisionAvsAi = null;
    private String dateDecisionIjAi = null;
    private String dateDepotApiAvsAi = null;
    private String dateDepotAvsAi = null;
    private String dateDepotIjAi = null;
    private String idDonneeFinanciereHeader = null;
    private String idVersionDroit = null;

    /**
     * @return the csTypeDonneeFinanciere
     */
    public String getCsTypeDonneeFinanciere() {
        return csTypeDonneeFinanciere;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDecisionApiAvsAi() {
        return dateDecisionApiAvsAi;
    }

    public String getDateDecisionAvsAi() {
        return dateDecisionAvsAi;
    }

    public String getDateDecisionIjAi() {
        return dateDecisionIjAi;
    }

    /**
     * @return the dateDepotApiAvsAi
     */
    public String getDateDepotApiAvsAi() {
        return dateDepotApiAvsAi;
    }

    /**
     * @return the dateDepotAvsAi
     */
    public String getDateDepotAvsAi() {
        return dateDepotAvsAi;
    }

    /**
     * @return the dateDepotIjAi
     */
    public String getDateDepotIjAi() {
        return dateDepotIjAi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param csTypeDonneeFinanciere
     *            the csTypeDonneeFinanciere to set
     */
    public void setCsTypeDonneeFinanciere(String csTypeDonneeFinanciere) {
        this.csTypeDonneeFinanciere = csTypeDonneeFinanciere;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDecisionApiAvsAi(String dateDecisionApiAvsAi) {
        this.dateDecisionApiAvsAi = dateDecisionApiAvsAi;
    }

    public void setDateDecisionAvsAi(String dateDecisionAvsAi) {
        this.dateDecisionAvsAi = dateDecisionAvsAi;
    }

    public void setDateDecisionIjAi(String dateDecisionIjAi) {
        this.dateDecisionIjAi = dateDecisionIjAi;
    }

    /**
     * @param dateDepotApiAvsAi
     *            the dateDepotApiAvsAi to set
     */
    public void setDateDepotApiAvsAi(String dateDepotApiAvsAi) {
        this.dateDepotApiAvsAi = dateDepotApiAvsAi;
    }

    /**
     * @param dateDepotAvsAi
     *            the dateDepotAvsAi to set
     */
    public void setDateDepotAvsAi(String dateDepotAvsAi) {
        this.dateDepotAvsAi = dateDepotAvsAi;
    }

    /**
     * @param dateDepotIjAi
     *            the dateDepotIjAi to set
     */
    public void setDateDepotIjAi(String dateDepotIjAi) {
        this.dateDepotIjAi = dateDepotIjAi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idDonneeFinanciereHeader = id;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
