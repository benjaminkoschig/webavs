/**
 * 
 */
package ch.globaz.perseus.business.models.pcfaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author DDE
 * 
 */
public class PCFAccordeeForDetailsCalculSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PCFAccordeeForDetailsCalculSearchModel() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return PCFAccordeeForDetailsCalcul.class;
    }

}
