/**
 * 
 */
package ch.globaz.pegasus.business.models.transfert;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author SCE Modele de recherche simple pour les header des décisions 14 juil. 2010
 */
public class TransfertDossierSuppressionSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionHeader = null;

    /**
     * @return the forIdDecisionHeader
     */
    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
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
        return TransfertDossierSuppression.class;
    }

}
