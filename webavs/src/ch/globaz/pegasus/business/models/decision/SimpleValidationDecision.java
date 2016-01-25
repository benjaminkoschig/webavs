/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCE Modele simple de validation des decisions 14 juil. 2010
 */
public class SimpleValidationDecision extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDecisionHeader = null;
    private String idPCAccordee = null;
    private String idValidationDecision = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idValidationDecision;
    }

    /**
     * @return the idDecisionHeader
     */
    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    /**
     * @return the idPCAccordee
     */
    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    /**
     * @return the idValidationDecision
     */
    public String getIdValidationDecision() {
        return idValidationDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idValidationDecision = id;

    }

    /**
     * @param idDecisionHeader
     *            the idDecisionHeader to set
     */
    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    /**
     * @param idPCAccordee
     *            the idPCAccordee to set
     */
    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    /**
     * @param idValidationDecision
     *            the idValidationDecision to set
     */
    public void setIdValidationDecision(String idValidationDecision) {
        this.idValidationDecision = idValidationDecision;
    }

}
