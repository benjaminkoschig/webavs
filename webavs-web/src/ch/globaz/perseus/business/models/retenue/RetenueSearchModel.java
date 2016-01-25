/**
 * 
 */
package ch.globaz.perseus.business.models.retenue;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author dde
 * 
 */
public class RetenueSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPcfAccordee = null;

    /**
     * @return the forIdPcfAccordee
     */
    public String getForIdPcfAccordee() {
        return forIdPcfAccordee;
    }

    /**
     * @param forIdPcfAccordee
     *            the forIdPcfAccordee to set
     */
    public void setForIdPcfAccordee(String forIdPcfAccordee) {
        this.forIdPcfAccordee = forIdPcfAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return Retenue.class;
    }

}
