package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTRevisionViewBean;
import ch.globaz.vulpecula.documents.ctrlemployeur.DocumentLettreControleEmployeurPrinter;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.ctrlemployeur.LettreControle;
import ch.globaz.vulpecula.process.revision.ListRevisionExcelProcess;

public class PTRevisionHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTRevisionViewBean vb = (PTRevisionViewBean) viewBean;

            // On génère une liste par année
            int anneeDebut = Integer.valueOf(vb.getAnneeDebut());
            int anneeFin = Integer.valueOf(vb.getAnneeFin());

            while (anneeDebut <= anneeFin) {
                ListRevisionExcelProcess process = new ListRevisionExcelProcess();
                process.setEMailAddress(vb.getEmail());
                process.setSendCompletionMail(true);
                process.setSendMailOnError(true);
                process.setAnneeDebut(String.valueOf(anneeDebut));
                process.setAnneeFin(String.valueOf(anneeDebut));
                process.setIdEmployeur(vb.getIdEmployeur());
                BProcessLauncher.start(process);
                anneeDebut++;
            }
            if (vb.isPrintLettre()) {
                LettreControle lettreControle = new LettreControle();
                lettreControle.setAnneeDebut(Integer.parseInt(vb.getAnneeDebut()));
                lettreControle.setAnneeFin(Integer.parseInt(vb.getAnneeFin()));
                lettreControle.setDate(new Date(vb.getDate()));
                lettreControle.setDateReference(new Date(vb.getDateReference()));
                lettreControle.setHeure(vb.getHeure());
                lettreControle.setEmail(vb.getEmail());
                lettreControle.setIdEmployeur(vb.getIdEmployeur());
                lettreControle.setReviseur(vb.getReviseur());
                DocumentLettreControleEmployeurPrinter docs = new DocumentLettreControleEmployeurPrinter(lettreControle);
                docs.setEMailAddress(vb.getEmail());
                BProcessLauncher.start(docs);
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
