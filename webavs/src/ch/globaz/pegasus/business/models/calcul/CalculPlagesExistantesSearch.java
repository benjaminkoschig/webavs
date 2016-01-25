/**
 * 
 */
package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author ECO
 * 
 */
public class CalculPlagesExistantesSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdRequerant = null;

    public String getForIdRequerant() {
        return forIdRequerant;
    }

    public void setForIdRequerant(String forIdRequerant) {
        this.forIdRequerant = forIdRequerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return CalculPlagesExistantes.class;
    }

}
