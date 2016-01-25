/*
 * Créé le 23 mai 05
 */
package globaz.apg.vb.droits;

import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APSituationProfessionnelleListViewBean extends APSituationProfessionnelleManager implements
        FWListViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APSituationProfessionnelleViewBean();
    }
}
