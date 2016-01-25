package globaz.aquila.helpers.batch;

import globaz.aquila.db.batch.COEtapeViewBean;
import globaz.aquila.db.batch.COSequenceListViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import java.util.Collections;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COEtapeHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Charge la liste des des séquences.
     * 
     * @see globaz.framework.controller.FWHelper#_chercher(globaz.framework.bean.FWViewBeanInterface,globaz.framework.controller.FWAction,
     *      globaz.globall.api.BISession)
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        COSequenceListViewBean sequenceListViewBean = (COSequenceListViewBean) viewBean;

        sequenceListViewBean.setISession(session);
        sequenceListViewBean.find();
    }

    /**
     * Prépare le {@link COEtapeViewBean} en chargeant les séquences.
     * 
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,globaz.framework.controller.FWAction,
     *      globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        // charger les séquences
        COSequenceListViewBean listViewBean = new COSequenceListViewBean();

        listViewBean.setISession(session);
        listViewBean.find();

        ((COEtapeViewBean) viewBean).setSequences(Collections.unmodifiableList(listViewBean.getContainer()));
    }
}
