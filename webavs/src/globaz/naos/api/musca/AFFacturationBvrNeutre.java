package globaz.naos.api.musca;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.naos.process.AFProcessFacturationBvrNeutre;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class AFFacturationBvrNeutre extends AFFacturationAffiliationParImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public AFFacturationBvrNeutre() {
        super();
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean avantRepriseErrGen(IFAPassage passage, BProcess context, String idModuleFacturation)
            throws Exception {
        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.04.2003 08:52:52)
     */
    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        // AFProcessFacturationBvrNeutre procFacturation =
        // new AFProcessFacturationBvrNeutre();
        // //copier le process parent
        // procFacturation.setSession(context.getSession());
        // procFacturation.setSendCompletionMail(false);
        // procFacturation.setSendMailOnError(false);
        // procFacturation.executeProcess();
        // context.getMemoryLog().logMessage(procFacturation.getMemoryLog());
        // //contrôler si le process a fonctionné
        // if (!procFacturation.isAborted() &&
        // !context.getTransaction().hasErrors()) {
        // return true;
        // } else
        // return false;
        return false;
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {

        AFProcessFacturationBvrNeutre procFacturation = new AFProcessFacturationBvrNeutre();
        // copier le process parent
        procFacturation.setSession(context.getSession());
        procFacturation.setSendCompletionMail(false);
        procFacturation.setSendMailOnError(false);
        procFacturation.setPassage(passage);
        procFacturation.setIdModuleFacturationBvrNeutre(idModuleFacturation);
        procFacturation.executeProcess();
        context.getMemoryLog().logMessage(procFacturation.getMemoryLog());
        // contrôler si le process a fonctionné
        if (!procFacturation.isAborted() && !context.getTransaction().hasErrors()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

}
