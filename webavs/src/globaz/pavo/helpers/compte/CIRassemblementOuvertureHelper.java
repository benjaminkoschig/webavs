package globaz.pavo.helpers.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.pavo.db.compte.CICompteIndividuelViewBean;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.process.CIReenvoiCIProcess;

/**
 * Controlleur gérant les fonctions spéciales des rassemblements/ouvetures Date de création : (16.10.2002 08:36:43)
 * 
 * @author: dgi
 */
public class CIRassemblementOuvertureHelper extends FWHelper {
    /**
     * Constructeur.
     * 
     * @param action
     *            java.lang.String
     */
    public CIRassemblementOuvertureHelper() {
        super();
    }

    /**
     * Exécution des méthodes spécifiques au rassemblement/ouverture des CI. Date de création : (03.05.2002 16:23:10)
     * 
     * @return le viewBean en question
     * @param viewBean
     *            bean associé à l'action en court
     * @param action
     *            action en court
     * @param session
     *            session de l'utilisateur
     */
    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {
        try {
            if ("chercherRassemblementOuverture".equals(action.getActionPart())) {
                // charge l'en-tête
                ((CICompteIndividuelViewBean) viewBean).retrieve();
            } else if ("envoiCI".equals(action.getActionPart())) {

                CIRassemblementOuverture annonce = (CIRassemblementOuverture) viewBean;
                if (annonce.isNew()) {
                    annonce.retrieve();
                }
                // annonce.annonceEcritures("True");
                CIReenvoiCIProcess process = new CIReenvoiCIProcess();
                // process.setAnnonce(annonce);
                process.setSession((BSession) session);
                process.setIdAnnonce(annonce.getRassemblementOuvertureId());
                process.setPerson(CIReenvoiCIProcess.ASSURE);
                BProcessLauncher.start(process);
            } else if ("envoiCIConjoint".equals(action.getActionPart())) {
                CIRassemblementOuverture annonce = (CIRassemblementOuverture) viewBean;
                if (annonce.isNew()) {
                    annonce.retrieve();
                }
                // annonce.annonceEcritures("False");
                // BSession sessionProc = new BSession((BSession)session);
                CIReenvoiCIProcess process = new CIReenvoiCIProcess();
                // process.setAnnonce(annonce);
                process.setSession((BSession) session);
                process.setIdAnnonce(annonce.getRassemblementOuvertureId());
                process.setPerson(CIReenvoiCIProcess.CONJOINT);
                BProcessLauncher.start(process);
            } else if ("chercherEcrituresRassemblement".equals(action.getActionPart())) {
                ((CIRassemblementOuverture) viewBean).retrieve();
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }
}
