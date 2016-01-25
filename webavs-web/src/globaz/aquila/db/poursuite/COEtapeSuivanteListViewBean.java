package globaz.aquila.db.poursuite;

import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <h1>Description</h1>
 * <p>
 * Un viewBean pour l'affichage de la liste des étapes suivantes de l'écran de suivi des étapes.
 * </p>
 * 
 * @author Pascal Lovy, 27-sep-2005
 */
public class COEtapeSuivanteListViewBean extends COTransitionManager implements FWListViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 4023769719409070609L;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COEtapeSuivanteViewBean();
    }

}
