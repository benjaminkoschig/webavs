package globaz.hermes.helpers.gestion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

public class HELotHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (action.getActionPart().endsWith("chooseType")) {
            try {
                _retrieve(viewBean, action, session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.execute(viewBean, action, session);
    }

}
