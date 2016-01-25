/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class DecisionSuppressionSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionHeader = null;
    private String forIdDecisionSuppression = null;
    private String forIdVersionDroit = null;

    /**
     * @return the forIdDecisionHeader
     */
    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    /**
     * @return the forIdDecisionSuppression
     */
    public String getForIdDecisionSuppression() {
        return forIdDecisionSuppression;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    /**
     * @param forIdDecisionHeader
     *            the forIdDecisionHeader to set
     */
    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    /**
     * @param forIdDecisionSuppression
     *            the forIdDecisionSuppression to set
     */
    public void setForIdDecisionSuppression(String forIdDecisionSuppression) {
        this.forIdDecisionSuppression = forIdDecisionSuppression;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return DecisionSuppression.class;
    }

}
