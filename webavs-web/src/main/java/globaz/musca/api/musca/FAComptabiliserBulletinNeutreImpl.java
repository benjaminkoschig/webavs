package globaz.musca.api.musca;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.external.IntModuleFacturation;
import globaz.musca.process.FAPassageComptabiliserBulletinNeutreProcess;

/**
 * Insérez la description du type ici. Date de création : (24.04.2003 12:51:01)
 * 
 * @author: btc
 */
public class FAComptabiliserBulletinNeutreImpl implements IntModuleFacturation {
    /**
     * Commentaire relatif au constructeur FAListGenericImpl.
     */
    public FAComptabiliserBulletinNeutreImpl() {
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
            globaz.musca.process.FAPassageComptabiliserBulletinNeutreProcess cptProc = new FAPassageComptabiliserBulletinNeutreProcess();
            // copier le process parent
            cptProc.setParentWithCopy(context);
            cptProc.setMemoryLog(context.getMemoryLog());
            //
            cptProc.setSession((BSession) passage.getISession());
            cptProc.setIdPassage(passage.getIdPassage());
            cptProc.setEMailAddress(context.getEMailAddress());
            cptProc._executeComptabiliserProcess(passage);
            cptProc.getMemoryLog();

            // if (cptProc.getParent() != null &&
            // cptProc.hasAttachedDocuments()) {
            // List e = cptProc.getAttachedDocuments();
            // for (Iterator iter = e.iterator(); iter.hasNext();) {
            // JadePublishDocument doc = (JadePublishDocument)iter.next();
            // cptProc.getParent().registerAttachedDocument(doc.getPublishJobDefinition().getDocumentInfo(),
            // doc.getDocumentLocation());
            // }
            // }
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
