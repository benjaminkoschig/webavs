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
public class SimpleDecisionRefusSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionRefus = null;
    private String forIdDecisionHeader = null;

    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    /**
     * @return the forIdDecisionRefus
     */
    public String getForIdDecisionRefus() {
        return forIdDecisionRefus;
    }

    /**
     * @param forIdDecisionRefus
     *            the forIdDecisionRefus to set
     */
    public void setForIdDecisionRefus(String forIdDecisionRefus) {
        this.forIdDecisionRefus = forIdDecisionRefus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimpleDecisionRefus> whichModelClass() {
        return SimpleDecisionRefus.class;
    }

}
