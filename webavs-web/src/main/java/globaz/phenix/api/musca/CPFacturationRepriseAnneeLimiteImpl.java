package globaz.phenix.api.musca;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.phenix.process.decision.CPProcessReporterDecisionPreEncodee;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: acr
 */
public class CPFacturationRepriseAnneeLimiteImpl extends CPFacturationGenericImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public CPFacturationRepriseAnneeLimiteImpl() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.04.2003 08:52:52)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        CPProcessReporterDecisionPreEncodee procReprise = new CPProcessReporterDecisionPreEncodee();
        // copier le process parent
        BSession sessionPhenix = new globaz.globall.db.BSession(
                globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX);
        context.getSession().connectSession(sessionPhenix);

        procReprise.setSession(sessionPhenix);
        procReprise.setIdPassage(passage.getIdPassage());
        procReprise.setEMailAddress(context.getEMailAddress());
        procReprise.setSendCompletionMail(false);
        procReprise.setSendMailOnError(false);
        procReprise.executeProcess();
        // contrôler si le process a fonctionné
        if (!context.isAborted() && !context.getTransaction().hasErrors()) {
            return true;
        } else {
            return false;
        }
    }
}
