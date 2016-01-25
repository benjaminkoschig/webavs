/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public class DecisionRefusSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionHeader = null;
    private String forIdDecisionRefus = null;
    private String forIdDemande = null;

    /**
     * @return the forIdDemande
     */
    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    /**
     * @return the forIdDecisionRefus
     */
    public String getForIdDecisionRefus() {
        return forIdDecisionRefus;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    /**
     * @param forIdDecisionRefus
     *            the forIdDecisionRefus to set
     */
    public void setForIdDecisionRefus(String forIdDecisionRefus) {
        this.forIdDecisionRefus = forIdDecisionRefus;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return DecisionRefus.class;
    }

}
