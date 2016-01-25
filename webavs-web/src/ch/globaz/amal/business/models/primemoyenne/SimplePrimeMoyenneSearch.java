/**
 * 
 */
package ch.globaz.amal.business.models.primemoyenne;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimplePrimeMoyenneSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeSubside = null;

    public String getForAnneeSubside() {
        return forAnneeSubside;
    }

    public void setForAnneeSubside(String forAnneeSubside) {
        this.forAnneeSubside = forAnneeSubside;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimplePrimeMoyenne.class;
    }

}
