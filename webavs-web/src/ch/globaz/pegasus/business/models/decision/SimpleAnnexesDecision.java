/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCE
 * 
 *         14 juil. 2010
 */
public class SimpleAnnexesDecision extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csType = null;
    private String idAnnexesDecision = null;
    private String idDecisionHeader = null;
    private String valeur = null;

    public String getCsType() {
        return csType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idAnnexesDecision;
    }

    /**
     * @return the idAnnexesDecision
     */
    public String getIdAnnexesDecision() {
        return idAnnexesDecision;
    }

    /**
     * @return the idDecision
     */
    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    /**
     * @return the valeur
     */
    public String getValeur() {
        return valeur;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAnnexesDecision = id;

    }

    /**
     * @param idAnnexesDecision
     *            the idAnnexesDecision to set
     */
    public void setIdAnnexesDecision(String idAnnexesDecision) {
        this.idAnnexesDecision = idAnnexesDecision;
    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    /**
     * @param valeur
     *            the valeur to set
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

}
