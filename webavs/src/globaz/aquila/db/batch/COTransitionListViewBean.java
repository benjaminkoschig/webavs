package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * Représente le model de la vue "_rcListe"
 * 
 * @author Pascal Lovy, 29-nov-2004
 */
public class COTransitionListViewBean extends COTransitionManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COTransitionViewBean();
    }

}
