package globaz.libra.helpers.journalisations;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.libra.process.LIDocumentsProcess;
import globaz.libra.vb.journalisations.LIDocumentsExecutionViewBean;

public class LIDocumentsHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (action.getActionPart().equals("genererDocuments")) {

            LIDocumentsExecutionViewBean vb = (LIDocumentsExecutionViewBean) viewBean;

            try {

                LIDocumentsProcess process = new LIDocumentsProcess();
                process.setSession(vb.getSession());
                process.setEMailAddress(vb.getAdresseEmail());
                process.setDateDocuments(vb.getDateExecution());
                process.setIdsJournalisation(vb.getListeIdRappel());

                BProcessLauncher.start(process);

            } catch (Exception e) {
                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage(e.toString());
            }

        }

        return viewBean;
    }

}
