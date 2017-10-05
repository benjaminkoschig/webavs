/**
 * 
 */
package ch.globaz.corvus.business.models.ventilation;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author est
 * 
 */
public class SimpleVentilationSearch extends JadeSearchSimpleModel {

    private static final long serialVersionUID = 1L;
    private String forIdPrestationAccordee = null;

    public String getForIdPrestationAccordee() {
        return forIdPrestationAccordee;
    }

    public void setForIdPrestationAccordee(String forIdPrestationAccordee) {
        this.forIdPrestationAccordee = forIdPrestationAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimpleVentilation> whichModelClass() {
        return SimpleVentilation.class;
    }

}
