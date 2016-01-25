/*
 * Créé le 31 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.helpers.droits;

import globaz.apg.vb.droits.APPereMatListViewBean;
import globaz.apg.vb.droits.APPereMatViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APPereMatHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        APPereMatViewBean pere = (APPereMatViewBean) viewBean;

        // n'ajoute pas un pere si le no AVS est vide (le pere est optionnel)
        if (!JadeStringUtil.isEmpty(pere.getNoAVS())) {
            super._add(viewBean, action, session);
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // Charge le pere s'il existe déjà
        APPereMatViewBean pere = (APPereMatViewBean) viewBean;
        APPereMatListViewBean mgr = new APPereMatListViewBean();

        mgr.setISession(session);
        mgr.setForIdDroitMaternite(pere.getIdDroitMaternite());
        mgr.find();

        if (!mgr.isEmpty()) {
            pere.copyDataFromEntity((APPereMatViewBean) mgr.get(0));
        }
    }
}
