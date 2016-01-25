/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCE Modele simple pour les decisions de refus 14 juil. 2010
 */
public class SimpleDecisionRefus extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csMotif = null;
    private String csSousMotif = null;
    private String champLibreMotif = "";
    private String dateRefus = null;
    private String idDecisionHeader = null;
    private String idDecisionRefus = null;
    private String idDemandePc = null;

    /**
     * @return the csMotif
     */
    public String getCsMotif() {
        return csMotif;
    }

    /**
     * @return the csSousMotif
     */
    public String getCsSousMotif() {
        return csSousMotif;
    }

    /**
     * @return the dateRefus
     */
    public String getDateRefus() {
        return dateRefus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDecisionRefus;
    }

    /**
     * @return the idDecisionHeader
     */
    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    /**
     * @return the idDecisionRefus
     */
    public String getIdDecisionRefus() {
        return idDecisionRefus;
    }

    /**
     * @return the idDemandePc
     */
    public String getIdDemandePc() {
        return idDemandePc;
    }

    /**
     * @param csMotif
     *            the csMotif to set
     */
    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    /**
     * @param csSousMotif
     *            the csSousMotif to set
     */
    public void setCsSousMotif(String csSousMotif) {
        this.csSousMotif = csSousMotif;
    }

    /**
     * @param dateRefus
     *            the dateRefus to set
     */
    public void setDateRefus(String dateRefus) {
        this.dateRefus = dateRefus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDecisionRefus = id;

    }

    /**
     * @param idDecisionHeader
     *            the idDecisionHeader to set
     */
    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    /**
     * @param idDecisionRefus
     *            the idDecisionRefus to set
     */
    public void setIdDecisionRefus(String idDecisionRefus) {
        this.idDecisionRefus = idDecisionRefus;
    }

    /**
     * @param idDemandePc
     *            the idDemandePc to set
     */
    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public String getChampLibreMotif() {
        return champLibreMotif;
    }

    public void setChampLibreMotif(String champLibreMotif) {
        this.champLibreMotif = champLibreMotif;
    }

}
