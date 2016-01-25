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
public class SimpleAnnexesDecisionSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAnnexesDecision = null;
    private String forIdDecisionHeader = null;
    private Collection<String> forInIdDecisionHeader = null;

    /**
     * @return the forIdAnnexesDecision
     */
    public String getForIdAnnexesDecision() {
        return forIdAnnexesDecision;
    }

    /**
     * @return the forIdDecisionHeader
     */
    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    public Collection<String> getForInIdDecisionHeader() {
        return forInIdDecisionHeader;
    }

    /**
     * @param forIdAnnexesDecision
     *            the forIdAnnexesDecision to set
     */
    public void setForIdAnnexesDecision(String forIdAnnexesDecision) {
        this.forIdAnnexesDecision = forIdAnnexesDecision;
    }

    /**
     * @param forIdDecisionHeader
     *            the forIdDecisionHeader to set
     */
    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
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
    public Class<SimpleAnnexesDecision> whichModelClass() {
        return SimpleAnnexesDecision.class;
    }

}
