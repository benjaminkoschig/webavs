package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * 
 * @author MBO
 * 
 */
public class SimpleDecisionSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande = null;

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
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    @Override
    public Class whichModelClass() {
        return SimpleDecision.class;
    }

}
