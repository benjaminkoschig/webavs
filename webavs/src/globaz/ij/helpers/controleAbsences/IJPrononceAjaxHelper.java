package globaz.ij.helpers.controleAbsences;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.vb.controleAbsences.IJPrononceAjaxViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

public class IJPrononceAjaxHelper extends PRAbstractHelper {

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJPrononceAjaxViewBean vb = (IJPrononceAjaxViewBean) viewBean;
        if (JadeStringUtil.isEmpty(vb.getIdPrononceSelectionne())) {
            throw new Exception("Can not update prononce because viewBean.getIdPrononceSelectionne() return empty");
        }
        if (JadeStringUtil.isEmpty(vb.getPrononceSelectionne())) {
            throw new Exception("Can not update prononce because viewBean.getPrononceSelectionne() return empty");
        }

        IJPrononce prononce = new IJPrononce();
        prononce.setSession((BSession) session);
        prononce.setId(vb.getIdPrononceSelectionne());
        prononce.retrieve();

        if (prononce.isNew()) {
            throw new Exception("Can not retreive pronce with id : " + vb.getIdPrononceSelectionne());
        }

        if (Boolean.valueOf(vb.getPrononceSelectionne())) {
            prononce.setIsPrononceSelectionne("1");
        } else {
            prononce.setIsPrononceSelectionne("2");
        }

        prononce.update();

        // super._update(viewBean, action, session);
    }
}
