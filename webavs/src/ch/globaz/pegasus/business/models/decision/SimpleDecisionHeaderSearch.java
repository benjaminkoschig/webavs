/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author SCE Modele de recherche simple pour les header des décisions 14 juil. 2010
 */
public class SimpleDecisionHeaderSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String DEVALIDATION_WHERE_KEY = "devalidation";

    private String forIdDecisionHeader = null;
    private List<String> forInIdDecisionHeader = null;

    /**
     * @return the forIdDecisionHeader
     */
    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    public List<String> getForInIdDecisionHeader() {
        return forInIdDecisionHeader;
    }

    /**
     * @param forIdDecisionHeader
     *            the forIdDecisionHeader to set
     */
    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    public void setForInIdDecisionHeader(List<String> forInIdDecisionHeader) {
        this.forInIdDecisionHeader = forInIdDecisionHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleDecisionHeader.class;
    }

}
