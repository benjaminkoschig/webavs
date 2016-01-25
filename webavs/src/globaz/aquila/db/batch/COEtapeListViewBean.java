package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <h1>Description</h1>
 * <p>
 * Un manager pour l'écran de configuration des étapes.
 * </p>
 * <p>
 * Représente le model de la vue "_rcListe"
 * </p>
 * 
 * @author Arnaud Dostes, 12-oct-2004
 */
public class COEtapeListViewBean extends COEtapeManager implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -6492059506738865132L;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COEtapeViewBean();
    }

}
