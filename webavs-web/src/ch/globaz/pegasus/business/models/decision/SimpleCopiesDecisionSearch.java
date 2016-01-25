/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author SCE
 * 
 *         14 juil. 2010
 */
public class SimpleCopiesDecisionSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCopiesDecision = null;
    private String forIdDecisionHeader = null;
    private java.util.Collection<String> forInIdDecisionHeader = null;

    /**
     * @return the forIdCopiesDecision
     */
    public String getForIdCopiesDecision() {
        return forIdCopiesDecision;
    }

    /**
     * @return the forIdDecisionHeader
     */
    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    public java.util.Collection<String> getForInIdDecisionHeader() {
        return forInIdDecisionHeader;
    }

    /**
     * @param forIdCopiesDecision
     *            the forIdCopiesDecision to set
     */
    public void setForIdCopiesDecision(String forIdCopiesDecision) {
        this.forIdCopiesDecision = forIdCopiesDecision;
    }

    /**
     * @param forIdDecisionHeader
     *            the forIdDecisionHeader to set
     */
    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    public void setForInIdDecisionHeader(java.util.Collection<String> forInIdDecisionHeader) {
        this.forInIdDecisionHeader = forInIdDecisionHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleCopiesDecision.class;
    }

}
