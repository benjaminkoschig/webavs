/**
 * 
 */
package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author BSC
 * 
 */
public class SimpleInformationsComptabiliteSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleInformationsComptabilite.class;
    }

}
