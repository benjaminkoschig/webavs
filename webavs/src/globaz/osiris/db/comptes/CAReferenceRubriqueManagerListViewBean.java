/*
 * Créé le 3 oct. 05
 */
package globaz.osiris.db.comptes;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author sch date : 3 oct. 05
 */
public class CAReferenceRubriqueManagerListViewBean extends CAReferenceRubriqueManager implements
        FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAReferenceRubriqueViewBean();
    }
}
