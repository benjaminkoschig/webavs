package globaz.pavo.helpers.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BSession;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.compte.CICompteIndividuelViewBean;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureEclaterViewBean;
import globaz.pavo.db.compte.CIEcritureListViewBean;
import globaz.pavo.db.compte.CIEcritureViewBean;
import globaz.pavo.db.inscriptions.CIJournalViewBean;

/**
 * Insérez la description du type ici. Date de création : (26.11.2002 15:17:43)
 * 
 * @author: ema
 */
public class CIEcritureHelper extends FWHelper {
    /**
     * Exécution des méthodes spécifiques aux écritures. Date de création : (03.05.2002 16:23:10)
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
            if ("listerSurPage".equals(action.getActionPart())) {
                // Pour afficher l'écran des inscriptions au journal
                ((CIEcritureListViewBean) viewBean).find();
            } else if ("afficherSurPage".equals(action.getActionPart())) {
                // Pour afficher les détails d'une inscription
                ((CIEcritureViewBean) viewBean).retrieve();
            } else if ("chercherEcriture".equals(action.getActionPart())) {
                // charge l'en-tête de recherche
                CICompteIndividuelViewBean vb = (CICompteIndividuelViewBean) viewBean;
                if (JAUtil.isStringEmpty(vb.getCompteIndividuelId())) {
                    String id = vb.loadCI(session);
                    if (id == null) {
                        return viewBean;
                    }
                    vb.setCompteIndividuelId(id);
                }
                vb.retrieve();

            } else if ("chercherSurPage".equals(action.getActionPart())) {
                // retieve du journal
                ((CIJournalViewBean) viewBean).retrieve();
            } else if ("ajouterSurPage".equals(action.getActionPart())) {
                // ajout
                ((CIEcritureViewBean) viewBean).add();
            } else if ("copie".equals(action.getActionPart())) {
                // copie
                ((CIEcritureViewBean) viewBean).copie();
            } else if ("comptabiliser".equals(action.getActionPart())) {
                // comptabiliser
                CIEcritureViewBean obj = (CIEcritureViewBean) viewBean;
                obj.retrieve();
                obj.comptabiliser();
            } else if ("extourne".equals(action.getActionPart())) {
                // copie
                ((CIEcritureViewBean) viewBean).extourne();
            } else if ("eclaterExecuter".equals(action.getActionPart())) {
                // copie
                CIEcriture ecritureAEclater = new CIEcriture();
                ecritureAEclater.setEcritureId(((CIEcritureEclaterViewBean) viewBean).getEcritureIdAEclater());
                ecritureAEclater.setSession((BSession) viewBean.getISession());
                ecritureAEclater.retrieve();
                ecritureAEclater.eclater(((CIEcritureEclaterViewBean) viewBean).getDateClotureEclatement(),
                        ((CIEcritureEclaterViewBean) viewBean).getMoisDebut1(),
                        ((CIEcritureEclaterViewBean) viewBean).getMoisDebut2(),
                        ((CIEcritureEclaterViewBean) viewBean).getMoisFin1(),
                        ((CIEcritureEclaterViewBean) viewBean).getMoisFin2(),
                        ((CIEcritureEclaterViewBean) viewBean).getMontant1(),
                        ((CIEcritureEclaterViewBean) viewBean).getMontant2());

            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        return viewBean;
    }
}
