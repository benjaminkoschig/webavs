/*
 * Créé le 12 juil. 06
 */
package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.util.JADate;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJRecapitulatifPrononce;
import globaz.ij.vb.prononces.IJTerminerPrononceViewBean;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author hpe
 * 
 *         Helper pour terminer un prononcé
 */
public class IJTerminerPrononceHelper extends PRAbstractHelper {

    // Méthode d'affichage de la JSP avec les détails souhaités
    public FWViewBeanInterface afficherDateFin(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {

        IJTerminerPrononceViewBean vb = (IJTerminerPrononceViewBean) viewBean;
        IJRecapitulatifPrononce prononce = new IJRecapitulatifPrononce();

        prononce.setISession(session);
        prononce.setId(vb.getIdPrononce());
        prononce.retrieve();

        vb.setId(prononce.getId());
        vb.setNom(prononce.getNom());
        vb.setPrenom(prononce.getPrenom());
        vb.setNoAVS(prononce.getNoAVS());
        vb.setDateFinPrononce(prononce.getDateFinPrononce());

        return vb;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("afficherDateFin".equals(action.getActionPart())) {
            try {
                afficherDateFin(viewBean, action, session);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if ("terminerPrononce".equals(action.getActionPart())) {
            try {
                terminerPrononce(viewBean, action, session);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if ("terminerPrononceFinal".equals(action.getActionPart())) {
            try {
                terminerPrononceFinal(viewBean, action, session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.execute(viewBean, action, session);
        }
        return viewBean;
    }

    // Pour terminer le prononcé
    public void terminerPrononce(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        IJTerminerPrononceViewBean vb = (IJTerminerPrononceViewBean) viewBean;

        // Rechercher dans les bases d'indemnisations s'il y en a après la date
        // de fin entrée...
        // Si c'est le cas, afficher un message "WARNING"
        IJBaseIndemnisationManager biman = new IJBaseIndemnisationManager();
        biman.setISession(session);
        biman.setForIdPrononce(vb.getIdPrononce());
        biman.find(BManager.SIZE_NOLIMIT);

        vb.setBaseIndAfterEnd(false);
        for (int i = 0; i < biman.size(); i++) {

            IJBaseIndemnisation bi = (IJBaseIndemnisation) biman.getEntity(i);

            JADate dateFinBaseInd = new JADate(bi.getDateFinPeriode());
            JADate dateFinPrononce = new JADate(vb.getDateFinPrononce());

            if (dateFinPrononce.toInt() < dateFinBaseInd.toInt()) {
                vb.setBaseIndAfterEnd(true);
                break;
            }
        }

        if (!vb.isBaseIndAfterEnd()) {

            // Mettre à jour le prononcé
            IJPrononce prononce = new IJPrononce();

            prononce.setISession(session);
            prononce.setId(vb.getIdPrononce());
            prononce.retrieve();

            prononce.setDateFinPrononce(vb.getDateFinPrononce());
            prononce.update();

        }
    }

    // Pour terminer le prononcé
    public void terminerPrononceFinal(FWViewBeanInterface viewBean, FWAction action, BISession session)
            throws Exception {

        IJTerminerPrononceViewBean vb = (IJTerminerPrononceViewBean) viewBean;

        // Mettre à jour le prononcé
        IJPrononce prononce = new IJPrononce();

        prononce.setISession(session);
        prononce.setId(vb.getIdPrononce());
        prononce.retrieve();

        prononce.setDateFinPrononce(vb.getDateFinPrononce());
        prononce.update();

    }

}
