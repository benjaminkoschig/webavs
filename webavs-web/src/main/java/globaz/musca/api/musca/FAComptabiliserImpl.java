package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.process.FAPassageComptabiliserProcess;

public class FAComptabiliserImpl implements IntModuleFacturation {

    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public FAComptabiliserImpl() {
        super();
    }

    @Override
    public boolean avantRecomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean avantRegenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return true;
    }

    @Override
    public boolean avantRepriseErrCom(IFAPassage passage, BProcess context) throws Exception {
        return false;
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
        try {
            globaz.musca.process.FAPassageComptabiliserProcess cptProc = new FAPassageComptabiliserProcess();
            // copier le process parent
            cptProc.setParentWithCopy(context);
            cptProc.setMemoryLog(context.getMemoryLog());
            cptProc.setSession((BSession) passage.getISession());
            cptProc.setIdPassage(passage.getIdPassage());
            cptProc.setEMailAddress(context.getEMailAddress());
            cptProc._executeComptabiliserProcess(passage);
            cptProc.getMemoryLog();

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        // contrôler si le process a fonctionné
        if (!context.isAborted()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean generer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return false;
    }

    @Override
    public boolean imprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }

    @Override
    public boolean recomptabiliser(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean regenerer(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean repriseOnErrorCompta(IFAPassage passage, BProcess context) throws Exception {
        return comptabiliser(passage, context);
    }

    @Override
    public boolean repriseOnErrorGen(IFAPassage passage, BProcess context, String idModuleFacturation) throws Exception {
        return generer(passage, context, idModuleFacturation);
    }

    @Override
    public boolean supprimer(IFAPassage passage, BProcess context) throws Exception {
        return false;
    }
}
