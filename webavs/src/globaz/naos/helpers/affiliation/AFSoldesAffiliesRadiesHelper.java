package globaz.naos.helpers.affiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFSoldesAffiliesRadiesViewBean;
import globaz.naos.itext.affiliation.AFSoldesAffiliesRadies_DocListe;
import globaz.naos.process.AFSoldesAffiliesRadiesProcess;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class AFSoldesAffiliesRadiesHelper extends FWHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        ((AFSoldesAffiliesRadiesViewBean) viewBean).setEMailAddress(session.getUserEMail());
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFSoldesAffiliesRadiesViewBean afViewBean = (AFSoldesAffiliesRadiesViewBean) viewBean;
        try {

            if ("pdf".equals(afViewBean.getTypeImpression())) {
                AFSoldesAffiliesRadies_DocListe doc = new AFSoldesAffiliesRadies_DocListe();

                doc.setISession(session);
                doc.setEMailAddress(afViewBean.getEMailAddress());
                doc.setFromDate(afViewBean.getFromDate());

                BProcessLauncher.start(doc);
            } else {
                AFSoldesAffiliesRadiesProcess process = new AFSoldesAffiliesRadiesProcess();

                process.setSession((BSession) session);
                process.setEMailAddress(afViewBean.getEMailAddress());
                process.setFromDate(afViewBean.getFromDate());

                BProcessLauncher.start(process);
            }

        } catch (Exception e) {
            afViewBean.setMessage(e.getMessage());
            afViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
