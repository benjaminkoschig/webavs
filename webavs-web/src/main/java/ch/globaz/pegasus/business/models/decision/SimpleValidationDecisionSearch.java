/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

/**
 * @author SCE
 * 
 *         14 juil. 2010
 */
public class SimpleValidationDecisionSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionHeader = null;
    private String forIdValidationDecision = null;
    private Collection<String> forInIdDecisionHeader = null;

    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    /**
     * @return the forIdValidationDecision
     */
    public String getForIdValidationDecision() {
        return forIdValidationDecision;
    }

    public Collection<String> getForInIdDecisionHeader() {
        return forInIdDecisionHeader;
    }

    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    /**
     * @param forIdValidationDecision
     *            the forIdValidationDecision to set
     */
    public void setForIdValidationDecision(String forIdValidationDecision) {
        this.forIdValidationDecision = forIdValidationDecision;
    }

    public void setForInIdDecisionHeader(Collection<String> forInIdDecisionHeader) {
        this.forInIdDecisionHeader = forInIdDecisionHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimpleValidationDecision> whichModelClass() {
        return SimpleValidationDecision.class;
    }

}
