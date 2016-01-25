package globaz.osiris.helpers.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.osiris.db.print.CAListCumulCotisationsParAnneeViewBean;
import globaz.osiris.print.list.CAListCumulCotisationsParAnnee;
import globaz.osiris.process.CAProcessListCumulCotisationParAnnee;

public class CAListCumulCotisationsParAnneeHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CAListCumulCotisationsParAnneeViewBean bean = (CAListCumulCotisationsParAnneeViewBean) viewBean;

        BProcess process = null;

        if ("pdf".equals(bean.getTypeImpression())) {

            process = new CAListCumulCotisationsParAnnee((BSession) session);
            process.setEMailAddress(bean.getEMailAddress());
            ((CAListCumulCotisationsParAnnee) process).setFromDateValeur(bean.getFromDateValeur());
            ((CAListCumulCotisationsParAnnee) process).setFromIdExterne(bean.getFromIdExterne());
            ((CAListCumulCotisationsParAnnee) process).setToDateValeur(bean.getToDateValeur());
            ((CAListCumulCotisationsParAnnee) process).setToIdExterne(bean.getToIdExterne());
            ((CAListCumulCotisationsParAnnee) process).setTypeImpression(bean.getTypeImpression());
        } else {

            process = new CAProcessListCumulCotisationParAnnee();
            process.setISession(session);
            process.setEMailAddress(bean.getEMailAddress());
            ((CAProcessListCumulCotisationParAnnee) process).setFromDateValeur(bean.getFromDateValeur());
            ((CAProcessListCumulCotisationParAnnee) process).setFromIdExterne(bean.getFromIdExterne());
            ((CAProcessListCumulCotisationParAnnee) process).setToDateValeur(bean.getToDateValeur());
            ((CAProcessListCumulCotisationParAnnee) process).setToIdExterne(bean.getToIdExterne());
            ((CAProcessListCumulCotisationParAnnee) process).setTypeImpression(bean.getTypeImpression());
        }

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            bean.setMessage(e.toString());
            bean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
