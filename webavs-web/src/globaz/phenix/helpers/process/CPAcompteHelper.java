package globaz.phenix.helpers.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.phenix.process.acompte.CPProcessAcompteCreationAnnuelle;
import globaz.phenix.process.acompte.CPProcessAcompteSupprimer;
import globaz.phenix.vb.acompte.CPAcompteCreationAnnuelleViewBean;
import globaz.phenix.vb.acompte.CPAcompteSuppressionViewBean;

public class CPAcompteHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("executerCreation".equals(action.getActionPart())
                && (viewBean instanceof CPAcompteCreationAnnuelleViewBean)) {
            CPAcompteCreationAnnuelleViewBean vb = (CPAcompteCreationAnnuelleViewBean) viewBean;

            CPProcessAcompteCreationAnnuelle process = new CPProcessAcompteCreationAnnuelle();
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdPassage(vb.getIdPassage());
            process.setForAnneeReprise(vb.getForAnneeReprise());
            process.setForGenreAffilie(vb.getForGenreAffilie());
            process.setForPeriodicite(vb.getForPeriodicite());
            process.setFromAffilieDebut(vb.getFromAffilieDebut());
            process.setFromAffilieFin(vb.getFromAffilieFin());
            process.setSession(vb.getSession());
            process.start();
            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }

        if ("executerSuppression".equals(action.getActionPart()) && (viewBean instanceof CPAcompteSuppressionViewBean)) {
            CPAcompteSuppressionViewBean vb = (CPAcompteSuppressionViewBean) viewBean;
            // Crée le process qui inserere le fichier dans la BD
            CPProcessAcompteSupprimer process = new CPProcessAcompteSupprimer();
            process.setEMailAddress(vb.getEMailAddress());
            process.setIdPassage(vb.getIdPassage());
            process.setForGenreAffilie(vb.getForGenreAffilie());
            process.setForTypeDecision(vb.getForTypeDecision());
            process.setSession(vb.getSession());
            process.start();
            if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                viewBean.setMsgType(process.getMsgType());
                viewBean.setMessage(process.getMessage());
            }
        }
        return super.execute(viewBean, action, session);
    }
}
