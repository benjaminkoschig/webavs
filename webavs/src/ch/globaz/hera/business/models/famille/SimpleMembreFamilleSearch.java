/**
 * 
 */
package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author BSC
 * 
 */
public class SimpleMembreFamilleSearch extends JadeSearchSimpleModel {

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
        return SimpleMembreFamille.class;
    }

}
