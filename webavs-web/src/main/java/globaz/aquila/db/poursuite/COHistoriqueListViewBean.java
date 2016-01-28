package globaz.aquila.db.poursuite;

import globaz.aquila.db.access.poursuite.COHistoriqueManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * Représente le model de la vue "_rcListe"
 * 
 * @author Pascal Lovy, 06-oct-2004
 */
public class COHistoriqueListViewBean extends COHistoriqueManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COHistoriqueViewBean();
    }

}
