/*
 * Créé le 10 juin 05
 */
package globaz.apg.vb.annonces;

import globaz.apg.db.annonces.APAnnonceAPGHierarchiqueManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APAnnonceAPGListViewBean extends APAnnonceAPGHierarchiqueManager implements FWListViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APAnnonceAPGViewBean();
    }
}
