/**
 * 
 */
package ch.globaz.perseus.business.models.pcfaccordee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimplePCFAccordeeSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande = null;
    private Boolean forOnError = null;

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forOnError
     */
    public Boolean getForOnError() {
        return forOnError;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forOnError
     *            the forOnError to set
     */
    public void setForOnError(Boolean forOnError) {
        this.forOnError = forOnError;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimplePCFAccordee.class;
    }

}
