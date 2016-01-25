/*
 * Créé le 20 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.helpers.prestation;

import globaz.apg.vb.prestation.APRepartitionJointPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APCotisationJointRepartitionHelper extends FWHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     */
    public static final String ACTION_CHARGER_REPARTITION = "chargerRepartition";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected FWViewBeanInterface chargerRepartition(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {
        ((APRepartitionJointPrestationViewBean) viewBean).setISession(session);
        ((APRepartitionJointPrestationViewBean) viewBean).retrieve();

        return viewBean;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (ACTION_CHARGER_REPARTITION.equals(action.getActionPart())) {
            try {
                return chargerRepartition(viewBean, action, session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return viewBean;
    }
}
