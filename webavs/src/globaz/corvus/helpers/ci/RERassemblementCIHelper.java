/*
 * Créé le 21 janv. 08
 */

package globaz.corvus.helpers.ci;

import globaz.corvus.db.ci.RECompteIndividuel;
import globaz.corvus.db.ci.RECompteIndividuelManager;
import globaz.corvus.vb.ci.RERassemblementCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author BSC
 * 
 */

public class RERassemblementCIHelper extends PRAbstractHelper {

    // ~ Fields
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RERassemblementCIViewBean rciViewBean = (RERassemblementCIViewBean) viewBean;

        BTransaction transaction = null;

        try {
            // Création de la transaction
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            // on cherche le CI de ce tiers
            RECompteIndividuelManager ciManager = new RECompteIndividuelManager();
            ciManager.setSession((BSession) session);
            ciManager.setForIdTiers(rciViewBean.getIdTiersAyantDroit());
            ciManager.find(transaction);
            RECompteIndividuel ci = (RECompteIndividuel) ciManager.getFirstEntity();

            rciViewBean.setIdCI(ci.getIdCi());

            // ajout du rassemblement
            rciViewBean.add(transaction);

            if (transaction.hasErrors()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
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
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RERassemblementCIViewBean ciViewBean = (RERassemblementCIViewBean) viewBean;

        BTransaction transaction = null;

        try {
            // Création de la transaction
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            // update du rassemblement
            ciViewBean.update(transaction);

            if (transaction.hasErrors()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
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
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }
}
