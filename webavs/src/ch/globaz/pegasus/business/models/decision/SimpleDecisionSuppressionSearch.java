/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author SCE Modele de recherche simple pour les décisions des suppression 14 juil. 2010
 */
public class SimpleDecisionSuppressionSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionSuppression = null;
    private String forIdVersionDroit = null;

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
        return SimpleDecisionSuppression.class;
    }

}
