/*
 * Créé le 12 juil. 06
 */
package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.vb.prononces.IJInfoComplViewBean;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author hpe
 * 
 *         Helper pour terminer un prononcé
 */
public class IJInfoComplHelper extends PRAbstractHelper {

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
        // creation d'une transaction pour ajouter l'annonces et les périodes en
        // même temps
        BTransaction transaction = null;

        try {
            IJInfoComplViewBean infoComplViewBean = (IJInfoComplViewBean) viewBean;
            transaction = new BTransaction(infoComplViewBean.getSession());
            transaction.openTransaction();

            // on cherche le prononce
            IJPrononce pr = new IJPrononce();
            pr.setSession(infoComplViewBean.getSession());
            pr.setIdPrononce(infoComplViewBean.getIdPrononce());
            pr.retrieve(transaction);

            // sauver l'etat du prononce dans l'info compl
            infoComplViewBean.setAncienEtatDroitOuPrononce(pr.getCsEtat());

            // ajout de l'information complementaire
            infoComplViewBean.add(transaction);

            // mise a jours des information du prononce
            pr.setIdInfoCompl(infoComplViewBean.getIdInfoCompl());
            pr.setCsEtat(IIJPrononce.CS_TANSFERE);
            pr.update(transaction);

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

        // on supprime le lien du prononce sur l'info. compl. et l'inf. compl.
        BTransaction transaction = null;

        try {
            IJInfoComplViewBean infoComplViewBean = (IJInfoComplViewBean) viewBean;
            transaction = new BTransaction(infoComplViewBean.getSession());
            transaction.openTransaction();

            // on cherche le prononce
            IJPrononce pr = new IJPrononce();
            pr.setSession(infoComplViewBean.getSession());
            pr.setIdPrononce(infoComplViewBean.getIdPrononce());
            pr.retrieve(transaction);

            // suppression du lien sur l'info. compl.
            pr.setIdInfoCompl("");
            // restaurer l'ancien etat du prononce
            pr.setCsEtat(infoComplViewBean.getAncienEtatDroitOuPrononce());
            pr.update(transaction);

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

    /**
     * Rechercher dans les bases d'indemnisations s'il y en a après la date de fin entrée
     * 
     * @param idPrononce
     * @param endDate
     * @param session
     * @return
     * @throws Exception
     */
    private boolean hasBaseIndAfterEndDate(String idPrononce, String endDate, BISession session) throws Exception {

        IJBaseIndemnisationManager biman = new IJBaseIndemnisationManager();
        biman.setISession(session);
        biman.setForIdPrononce(idPrononce);
        biman.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < biman.size(); i++) {

            IJBaseIndemnisation bi = (IJBaseIndemnisation) biman.getEntity(i);

            JADate dateFinBaseInd = new JADate(bi.getDateFinPeriode());
            JADate dateFinPrononce = new JADate(endDate);

            if (dateFinPrononce.toInt() < dateFinBaseInd.toInt()) {
                return true;
            }
        }
        return false;
    }

}
