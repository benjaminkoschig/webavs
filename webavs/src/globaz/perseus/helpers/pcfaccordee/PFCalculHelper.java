package globaz.perseus.helpers.pcfaccordee;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.context.JadeThread;
import globaz.perseus.vb.pcfaccordee.PFCalculViewBean;

public class PFCalculHelper extends FWHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // TODO Auto-generated method stub
        super._init(viewBean, action, session);
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("calculer".equals(action.getActionPart())) {
            try {
                ((PFCalculViewBean) viewBean).calculer();
            } catch (Exception e) {
                JadeThread.logError(
                        e.getClass().getName(),
                        "Erreur technique, merci d'envoyer un PrintScreen à Globaz (PFCalculHelper.execute) : "
                                + e.getMessage());
                e.printStackTrace();
            }
        } else {
            viewBean = super.execute(viewBean, action, session);
        }
        return viewBean;
    }

}
