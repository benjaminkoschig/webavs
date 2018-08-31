package globaz.vulpecula.helpers.ctrlemployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.ctrlemployeur.PTLettreControleViewBean;
import ch.globaz.vulpecula.documents.ctrlemployeur.DocumentLettreControleEmployeurPrinter;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.ctrlemployeur.LettreControle;

public class PTLettreControleHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTLettreControleViewBean vb = (PTLettreControleViewBean) viewBean;
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
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
