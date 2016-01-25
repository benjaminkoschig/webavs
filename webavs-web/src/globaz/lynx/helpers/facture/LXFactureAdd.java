package globaz.lynx.helpers.facture;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.facture.LXFactureViewBean;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.helpers.utils.LXFactureUtils;
import globaz.lynx.helpers.utils.LXHelperUtils;

/**
 * Permet d'ajouter des factures à un journal.
 * 
 * @author DDA
 */
public class LXFactureAdd {

    /**
     * Ajout de factures depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void addFacture(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXFactureViewBean facture = (LXFactureViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXFactureUtils.validate(session, transaction, facture);

            LXSection section = LXHelperUtils.getSection(session, facture, transaction, LXSection.CS_TYPE_FACTURE);

            LXOperation operation = LXHelperUtils.createOperation(session, facture, transaction, section);

            LXHelperUtils.addVentilations(session, transaction, facture, operation);

            facture.setIdSection(section.getIdSection());
            facture.setIdOperation(operation.getIdOperation());

        } catch (Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
            }

            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }

                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }
}
