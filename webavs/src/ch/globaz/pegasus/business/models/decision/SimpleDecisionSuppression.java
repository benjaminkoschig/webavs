/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCE Modèle simple pour les décision de suppressions 14 juil. 2010
 */
public class SimpleDecisionSuppression extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csMotif = null;
    private String csSousMotif = null;
    private String champLibreMotif = "";
    private String dateSuppression = null;

    private String idDecisionHeader = null;

    private String idDecisionSuppression = null;
    private String idVersionDroit = null;

    private Boolean isRestitution = null;

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
     * @return the dateSuppression
     */
    public String getDateSuppression() {
        return dateSuppression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDecisionSuppression;
    }

    /**
     * @return the idDecisionHeader
     */
    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    /**
     * @return the idDecisionSuppression
     */
    public String getIdDecisionSuppression() {
        return idDecisionSuppression;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public Boolean getIsRestitution() {
        return isRestitution;
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
     * @param dateSuppression
     *            the dateSuppression to set
     */
    public void setDateSuppression(String dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDecisionSuppression = id;

    }

    /**
     * @param idDecisionHeader
     *            the idDecisionHeader to set
     */
    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    /**
     * @param idDecisionSuppression
     *            the idDecisionSuppression to set
     */
    public void setIdDecisionSuppression(String idDecisionSuppression) {
        this.idDecisionSuppression = idDecisionSuppression;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsRestitution(Boolean isRestitution) {
        this.isRestitution = isRestitution;
    }

    public String getChampLibreMotif() {
        return champLibreMotif;
    }

    public void setChampLibreMotif(String champLibreMotif) {
        this.champLibreMotif = champLibreMotif;
    }

}
