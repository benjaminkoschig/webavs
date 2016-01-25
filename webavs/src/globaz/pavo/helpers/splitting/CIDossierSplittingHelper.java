package globaz.pavo.helpers.splitting;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.pavo.db.splitting.CIDossierSplitting;
import globaz.pavo.db.splitting.CIDossierSplittingViewBean;
import globaz.pavo.db.splitting.CIImprimerAnalyseViewBean;
import globaz.pavo.db.splitting.CIImprimerApercuViewBean;

/**
 * Controlleur gérant les fonctions spéciales du dossier de splitting. Date de création : (16.10.2002 08:36:43)
 * 
 * @author: dgi
 */
public class CIDossierSplittingHelper extends FWHelper {
    /**
     * Constructeur.
     * 
     * @param action
     *            java.lang.String
     */
    public CIDossierSplittingHelper() {
        super();
    }

    /**
     * Exécution des méthodes spécifiques au dossier de splitting. Date de création : (03.05.2002 16:23:10)
     * 
     * @return le viewBean en question
     * @param viewBean
     *            bean associé à l'action en cout
     * @param action
     *            action en court
     * @param session
     *            session de l'utilisateur
     */
    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        try {
            CIDossierSplitting dossier = (CIDossierSplitting) viewBean;
            if (dossier.isNew()) {
                dossier.retrieve();
            }
            // option ouvrir
            if ("ouvrir".equals(action.getActionPart())) {
                dossier.ouvrir();
            } else if ("revoquerDossier".equals(action.getActionPart())) {
                // revoquer le dossier de splitting
                dossier.revoquer();
            } else if ("executerSplitting".equals(action.getActionPart())) {
                // exécute le dossier de splitting
                dossier.executerSplitting();
            } else if ("rouvrirDossier".equals(action.getActionPart())) {
                // ré-ouvre le dossier de splitting
                dossier.rouvrir();
            } else if ("imprimerAnalyse".equals(action.getActionPart())) {
                // imprime l'aperçu du RCI avant splitting
                CIImprimerAnalyseViewBean rci = ((CIImprimerAnalyseViewBean) viewBean);
                dossier.imprimerAnalyse(rci.getEmail());
            } else if ("imprimerApercu".equals(action.getActionPart())) {
                // imprime l'aperçu des revenus pour l'assuré et son conjoint
                CIImprimerApercuViewBean apercu = ((CIImprimerApercuViewBean) viewBean);
                dossier.imprimerApercu(apercu.getEmail(), apercu.isCheck());
            } else if ("apercuRCI".equals(action.getActionPart())) {
                // recherche de l'annonce
                String[] ids = dossier.rechercheAnnonceRCI(null);
                if ((ids != null) && (ids.length == 2)) {
                    ((CIDossierSplittingViewBean) viewBean).setIdAnnonceRCI(ids[0]);
                    ((CIDossierSplittingViewBean) viewBean).setRefUniqueRCI(ids[1]);
                    ((CIDossierSplittingViewBean) viewBean).setIsArchivage(dossier.getIsArchivage());
                }
            } else if ("annulerDossierExecuter".equals(action.getActionPart())) {
                dossier.annuleDossierSplitting();
                dossier.save();
            } else if ("imprimerAccuseInvitation".equalsIgnoreCase(action.getActionPart())) {
                dossier.imprimerApercuAndInvit();
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }

}
