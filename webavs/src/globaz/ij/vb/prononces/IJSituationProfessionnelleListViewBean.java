/*
 * Créé le 28 sept. 05
 */
package globaz.ij.vb.prononces;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.prononces.IJSituationProfessionnelleManager;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author dvh
 */
public class IJSituationProfessionnelleListViewBean extends IJSituationProfessionnelleManager implements
        FWListViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJSituationProfessionnelleViewBean();
    }
}
