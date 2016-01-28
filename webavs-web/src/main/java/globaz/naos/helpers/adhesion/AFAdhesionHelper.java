package globaz.naos.helpers.adhesion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.naos.servlet.AFActionAdhesion;

public class AFAdhesionHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (AFActionAdhesion.ACTION_AJOUTER_COTISATION.equals(action.getActionPart())) {
            try {
                super._add(viewBean, action, session);
            } catch (Exception e) {
                // TODO Faire un message propre
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }
        return super.execute(viewBean, action, session);
    }

}
