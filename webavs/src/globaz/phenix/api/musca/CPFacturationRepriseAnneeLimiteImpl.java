package globaz.phenix.api.musca;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.phenix.process.decision.CPProcessReporterDecisionPreEncodee;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (24.04.2003 12:51:01)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.04.2003 08:52:52)
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
        // contr�ler si le process a fonctionn�
        if (!context.isAborted() && !context.getTransaction().hasErrors()) {
            return true;
        } else {
            return false;
        }
    }
}
