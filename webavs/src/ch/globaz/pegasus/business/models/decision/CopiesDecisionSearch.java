/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author SCE
 * 
 *         14 juil. 2010
 */
public class CopiesDecisionSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCopiesDecision = null;
    private String forIdDecisionHeader = null;

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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return CopiesDecision.class;
    }

}
