/*
 * Créé le 29 juin 07
 */

package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @deprecatedd
 * @author HPE
 * 
 */

public class REHistoriqueRentesCalculAcorHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_update(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        // Création de la transaction

        BSession session1 = (BSession) session;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) (session1).newTransaction();
            transaction.openTransaction();

            // récupération du viewBean et retrieve de la rente accordée, puis
            // update
            RERenteAccordeeJointDemandeRenteViewBean renteAccordeeVb = (RERenteAccordeeJointDemandeRenteViewBean) viewBean;

            RERenteAccordee renteAccordee = new RERenteAccordee();
            renteAccordee.setIdPrestationAccordee(renteAccordeeVb.getIdPrestationAccordee());
            renteAccordee.setSession((BSession) session);
            renteAccordee.retrieve(transaction);
            renteAccordee.wantCallValidate(false);

            renteAccordee.update(transaction);

            if (!renteAccordee.hasErrors() && !renteAccordeeVb.hasErrors() && !transaction.hasErrors()) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

}
