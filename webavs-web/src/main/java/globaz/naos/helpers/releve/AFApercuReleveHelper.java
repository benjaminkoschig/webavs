package globaz.naos.helpers.releve;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.naos.db.releve.AFApercuReleveViewBean;

public class AFApercuReleveHelper extends FWHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if ((viewBean instanceof AFApercuReleveViewBean) && ((AFApercuReleveViewBean) viewBean).isDoCalculCotisation()) {
            ((AFApercuReleveViewBean) viewBean).calculeCotisation();
            // Le remet à false -> pas 2 appels ;-)
            ((AFApercuReleveViewBean) viewBean).setDoCalculCotisation(false);
        }
        super._add(viewBean, action, session);

    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if ((viewBean instanceof AFApercuReleveViewBean) && ((AFApercuReleveViewBean) viewBean).isDoCalculCotisation()) {
            ((AFApercuReleveViewBean) viewBean).calculeCotisation();
            // Le remet à false -> pas 2 appels ;-)
            ((AFApercuReleveViewBean) viewBean).setDoCalculCotisation(false);
        }
        super._update(viewBean, action, session);
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ((viewBean instanceof AFApercuReleveViewBean) && ((AFApercuReleveViewBean) viewBean).isDoCalculCotisation()) {
            try {
                ((AFApercuReleveViewBean) viewBean).calculeCotisation();
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
            // Le remet à false -> pas 2 appels ;-)
            ((AFApercuReleveViewBean) viewBean).setDoCalculCotisation(false);
        }
        return super.execute(viewBean, action, session);
    }

}
