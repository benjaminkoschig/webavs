/**
 * 
 */
package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * mode�le simple de recherches dans TransfertTucana
 * 
 * @author PTA
 * 
 */
public class TransfertTucanaSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur l'id de d�tail de prestation � laquelle est li� l'enregistrement
     */
    private String forIdDetailPrestation = null;
    /**
     * Recherche sur le num�ro de bouclement
     */
    private String forNumBouclement = null;

    /**
     * @return the forIdDetailPrestation
     */
    public String getForIdDetailPrestation() {
        return forIdDetailPrestation;
    }

    /**
     * 
     * @return
     */
    public String getForNumBouclement() {
        return forNumBouclement;
    }

    /**
     * @param forIdDetailPrestation
     *            the forIdDetailPrestation to set
     */
    public void setForIdDetailPrestation(String forIdDetailPrestation) {
        this.forIdDetailPrestation = forIdDetailPrestation;
    }

    /**
     * Recherche sur le num�ro de bouclement
     * 
     * @param forNumBouclement
     *            num�ro de bouclement
     */
    public void setForNumBouclement(String forNumBouclement) {
        this.forNumBouclement = forNumBouclement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<TransfertTucanaModel> whichModelClass() {
        return TransfertTucanaModel.class;
    }

}
