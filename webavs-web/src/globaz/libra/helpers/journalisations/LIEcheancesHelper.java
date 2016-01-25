package globaz.libra.helpers.journalisations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.libra.process.LIAnnulerReceptionProcess;
import globaz.libra.process.LIDocumentsProcess;
import globaz.libra.process.LIReceptionProcess;
import globaz.libra.vb.journalisations.LIJournalisationsDetailViewBean;
import java.util.ArrayList;
import java.util.Collection;

public class LIEcheancesHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        LIJournalisationsDetailViewBean vb = (LIJournalisationsDetailViewBean) viewBean;

        try {

            if (vb.getTypeAction().equals("reception")) {

                LIReceptionProcess process = new LIReceptionProcess();
                process.setSession((BSession) vb.getISession());
                process.setEMailAddress(session.getUserEMail());
                process.setIdJournalisation(vb.getJournalisation().getIdJournalisation());
                process.executeProcess();

            } else if (vb.getTypeAction().equals("rappel")) {

                LIDocumentsProcess process = new LIDocumentsProcess();
                process.setSession((BSession) vb.getISession());
                process.setEMailAddress(session.getUserEMail());
                process.setDateDocuments(JACalendar.todayJJsMMsAAAA());

                Collection idsJO = new ArrayList();
                idsJO.add(vb.getJournalisation().getIdJournalisation());
                process.setIdsJournalisation(idsJO);

                process.executeProcess();

            } else if (vb.getTypeAction().equals("annulerReception")) {

                LIAnnulerReceptionProcess process = new LIAnnulerReceptionProcess();
                process.setSession((BSession) vb.getISession());
                process.setEMailAddress(session.getUserEMail());
                process.setIdJournalisation(vb.getJournalisation().getIdJournalisation());
                process.executeProcess();

            }

        } catch (Exception e) {
            vb.setMsgType(FWViewBeanInterface.ERROR);
            vb.setMessage(e.toString());
        }

        return viewBean;
    }

}
