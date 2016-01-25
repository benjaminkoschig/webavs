/*
 * Créé le 12 juil. 06
 */
package globaz.apg.helpers.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.vb.droits.APInfoComplViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BTransaction;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author hpe
 * 
 *         Helper pour terminer un prononcé
 */
public class APInfoComplHelper extends PRAbstractHelper {

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BTransaction transaction = null;

        try {
            APInfoComplViewBean infoComplViewBean = (APInfoComplViewBean) viewBean;
            transaction = new BTransaction(infoComplViewBean.getSession());
            transaction.openTransaction();

            // on cherche le droit
            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(infoComplViewBean.getSession());
            droit.setIdDroit(infoComplViewBean.getIdPrononce());
            droit.retrieve(transaction);

            // sauver l'etat du droit dans l'info compl
            infoComplViewBean.setAncienEtatDroitOuPrononce(droit.getEtat());

            // ajout de l'information complementaire
            infoComplViewBean.add(transaction);

            // mise a jours des information du droit
            droit.setIdInfoCompl(infoComplViewBean.getIdInfoCompl());
            if (infoComplViewBean.getTypeInfoCompl().equals(IAPDroitLAPG.CS_TRANSFER_DOSSIER)) {
                droit.setEtat(IAPDroitLAPG.CS_TANSFERE);
            } else {
                droit.setEtat(IAPDroitLAPG.CS_REFUSE);
            }
            droit.update(transaction);

            if (transaction.hasErrors()) {
                infoComplViewBean.setMessage(transaction.getErrors().toString());
                infoComplViewBean.setMsgType(FWViewBeanInterface.ERROR);
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        // on supprime le lien du droit sur l'info. compl. et l'inf. compl.
        BTransaction transaction = null;

        try {
            APInfoComplViewBean infoComplViewBean = (APInfoComplViewBean) viewBean;
            transaction = new BTransaction(infoComplViewBean.getSession());
            transaction.openTransaction();

            // on cherche le droit
            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(infoComplViewBean.getSession());
            droit.setIdDroit(infoComplViewBean.getIdPrononce());
            droit.retrieve(transaction);

            // suppression du lien sur l'info. compl.
            droit.setIdInfoCompl("");
            // restaurer l'ancien etat du droit
            droit.setEtat(infoComplViewBean.getAncienEtatDroitOuPrononce());
            droit.update(transaction);

            // suppression de l'info compl.
            infoComplViewBean.delete(transaction);

            if (transaction.hasErrors()) {
                infoComplViewBean.setMessage(transaction.getErrors().toString());
                infoComplViewBean.setMsgType(FWViewBeanInterface.ERROR);
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

}
