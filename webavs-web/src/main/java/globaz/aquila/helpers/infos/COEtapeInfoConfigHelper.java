package globaz.aquila.helpers.infos;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.vb.infos.COEtapeInfoConfigViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COEtapeInfoConfigHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // retourver l'étape pour le code système saisi
        COEtapeInfoConfigViewBean configViewBean = (COEtapeInfoConfigViewBean) viewBean;
        COEtapeManager etapeManager = new COEtapeManager();

        etapeManager.setForLibEtape(configViewBean.getLibEtape());
        etapeManager.setForLibSequence(configViewBean.getLibSequence());
        etapeManager.setISession(session);
        etapeManager.find();

        if (etapeManager.size() == 1) {
            configViewBean.setIdEtape(((COEtape) etapeManager.get(0)).getIdEtape());
        }

        super._add(configViewBean, action, session);
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        ((COEtapeInfoConfigViewBean) viewBean).wantLoadEtape(true);
        super._retrieve(viewBean, action, session);
    }
}
