package globaz.pavo.helpers.inscriptions;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.inscriptions.CIJournalViewBean;
import globaz.pavo.process.CISupprimerJournal;

/**
 * Pour g�rer les fonsctions sp�ciales sur les journaux. Date de cr�ation : (26.11.2002 09:18:17)
 * 
 * @author: ema
 */
public class CIJournalHelper extends FWHelper {
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        CIJournalViewBean vb = (CIJournalViewBean) viewBean;
        try {
            CISupprimerJournal process = new CISupprimerJournal(vb.getSession());
            process.setIdJournal(vb.getId());
            process.setSession((BSession) session);
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

    /**
     * Ex�cution des m�thodes sp�cifiques au journal. Date de cr�ation : (03.05.2002 16:23:10)
     * 
     * @return le viewBean en question
     * @param viewBean
     *            bean associ� � l'action en court
     * @param action
     *            action en court
     * @param session
     *            session de l'utilisateur
     */
    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {
        try {
            // Pour afficher l'�cran de comptabilisation
            if ("chercherComptabilisation".equals(action.getActionPart())) {
                ((CIJournalViewBean) viewBean).retrieve();
            } else
            // Lancement de la comptabilisation
            if ("comptabilisation".equals(action.getActionPart())) {
                ((CIJournalViewBean) viewBean).retrieve();
                // } else
                // Pour afficher l'�cran d'impression
                // if ("chercherImpression".equals(action.getActionPart())) {
                // ((CIJournalViewBean) viewBean).retrieve();
                // } else
                // Lancement de l'impression
                // if ("impression".equals(action.getActionPart())) {
                // ((CIJournalViewBean) viewBean).retrieve();
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }
}
