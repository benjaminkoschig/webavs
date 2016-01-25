package globaz.pavo.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.pavo.db.process.CIImprimerListJournauxPartielsProcessViewBean;
import globaz.pavo.print.list.CIImprimerListJournauxPartielsProcess;

public class CIImprimerListJournauxPartielsProcessHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIImprimerListJournauxPartielsProcessViewBean vb = (CIImprimerListJournauxPartielsProcessViewBean) viewBean;
        try {
            CIImprimerListJournauxPartielsProcess process = new CIImprimerListJournauxPartielsProcess(new BSession(
                    CIApplication.DEFAULT_APPLICATION_PAVO), "Ci", "GLOBAZ", vb.getSession().getLabel(
                    "MSG_JOURNAUX_PARTIEL"), new CIJournalManager(), "PAVO");
            process.setForAnnee(vb.getForAnnee());
            process.setForIdType(vb.getForIdType());
            process.setLikeIdAffiliation(vb.getLikeIdAffiliation());
            process.setForDate(vb.getForDate());
            process.setFromUser(vb.getFromUser());
            process.setFordateInscription(vb.getFordateInscription());
            process.setEMailAddress(vb.getEmailAddress());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }
}
