package globaz.vulpecula.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.vulpecula.vb.listes.PTListePersonnesAvecAgeViewBean;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.process.listepersonnesavecage.ListePersonnesAvecAgeProcess;
import com.sun.star.lang.IllegalArgumentException;

public class PTListePersonnesAvecAgeHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            PTListePersonnesAvecAgeViewBean vb = (PTListePersonnesAvecAgeViewBean) viewBean;
            ListePersonnesAvecAgeProcess process = new ListePersonnesAvecAgeProcess();
            try {
                if (vb.getAnnee() != null && vb.getAnnee().length() == 4) {
                    process.setAnnee(new Annee(vb.getAnnee()));
                } else {
                    throw new IllegalArgumentException("L'année n'est pas saisie");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("L'année n'est pas saisie correctement");
            }
            process.setEMailAddress(vb.getEmail());
            process.setSendCompletionMail(true);
            process.setSendMailOnError(true);
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}