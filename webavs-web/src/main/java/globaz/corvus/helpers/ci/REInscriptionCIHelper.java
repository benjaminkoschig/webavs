/*
 * Créé le 13 juil. 07
 */

package globaz.corvus.helpers.ci;

import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.corvus.vb.ci.REInscriptionCIListViewBean;
import globaz.corvus.vb.ci.REInscriptionCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author BSC
 * 
 */

public class REInscriptionCIHelper extends PRAbstractHelper {

    // ~ Fields
    // -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_delete(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        REInscriptionCIViewBean iciViewBean = (REInscriptionCIViewBean) viewBean;

        BTransaction transaction = null;

        try {
            // Création de la transaction
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            // on efface l'annonce de l'inscription CI
            REAnnonceInscriptionCI annCI = iciViewBean.getAnnonceInscriptionCI();
            annCI.delete(transaction);

            // on efface l'inscription
            iciViewBean.delete(transaction);

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

    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        REInscriptionCIListViewBean viewBean = (REInscriptionCIListViewBean) persistentList;
        viewBean.setLabelProvisoire(((BSession) session).getLabel("PROVISOIRE"));
        viewBean.setLabelTraite(((BSession) session).getLabel("TRAITE"));
        super._find(viewBean, action, session);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        REInscriptionCIViewBean iciViewBean = (REInscriptionCIViewBean) viewBean;

        BTransaction transaction = null;

        try {
            // Création de la transaction
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            // update l'annonce de l'inscription CI
            REAnnonceInscriptionCI annCI = iciViewBean.getAnnonceInscriptionCI();

            if (JadeStringUtil.isEmpty(annCI.getNumeroAgence())) {
                annCI.setNumeroAgence("000");
            } else {
                annCI.setNumeroAgence(annCI.getNumeroAgence());
            }

            annCI.update(transaction);

            // update de l'inscription
            iciViewBean.update(transaction);

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
