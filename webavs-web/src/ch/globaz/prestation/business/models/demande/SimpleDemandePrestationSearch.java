/**
 * 
 */
package ch.globaz.prestation.business.models.demande;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author ECO
 * 
 */
public class SimpleDemandePrestationSearch extends JadeSearchSimpleModel {

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
        // TODO Auto-generated method stub
        return SimpleDemandePrestation.class;
    }

}
