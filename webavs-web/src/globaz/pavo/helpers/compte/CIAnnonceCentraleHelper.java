package globaz.pavo.helpers.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CIAnnonceCentraleViewBean;

/**
 * 
 * @author: mmo
 */
public class CIAnnonceCentraleHelper extends FWHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._retrieve(viewBean, action, session);

        if (CIAnnonceCentraleViewBean.class.isAssignableFrom(viewBean.getClass())) {

            CIAnnonceCentraleViewBean vb = (CIAnnonceCentraleViewBean) viewBean;
            vb.setLectureSeule(true);
            if (JadeStringUtil.isBlankOrZero(vb.getIdEtat())) {
                vb.setLectureSeule(false);
            }

        }

    }

}
