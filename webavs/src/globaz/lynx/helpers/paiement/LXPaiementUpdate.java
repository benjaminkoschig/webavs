package globaz.lynx.helpers.paiement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationViewBean;
import globaz.lynx.db.paiement.LXPaiementViewBean;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.helpers.utils.LXPaiementUtils;
import globaz.lynx.utils.LXUtils;

public class LXPaiementUpdate {

    /**
     * Mise a jour du libelle. Ajout de "#1" a la suite si c'est le premier paiement. (exemple "libelle #1")
     * 
     * @param session
     * @param paiement
     * @param transaction
     * @throws Exception
     */
    private static void updateLibelle(BISession session, LXPaiementViewBean paiement, BTransaction transaction)
            throws Exception {
        LXOperation opTemp = LXHelperUtils.getOperation(session, transaction, paiement.getIdOperation());
        LXOperation opTempSrc = LXHelperUtils.getOperation(session, transaction, paiement.getIdOperationSrc());
        if (opTemp.getMontant().compareTo(paiement.getMontant()) != 0 && "1".equals(opTempSrc.getCountPmt())) {
            paiement.setLibelle(paiement.getLibelle() + " #1");
        }
    }

    /**
     * Modification d'un paiement depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void updatePaiement(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXPaiementViewBean paiement = (LXPaiementViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXPaiementUtils.validate(session, transaction, paiement);

            LXPaiementUtils.updateFactureBase(session, transaction, paiement);

            LXHelperUtils.updateSection(session, transaction, paiement);

            paiement.setMontant(LXUtils.getMontantNegatif(paiement.getMontant()));

            LXPaiementUtils.updateEtatBloque(session, transaction, paiement);

            updateLibelle(session, paiement, transaction);

            LXOperationViewBean operation = LXHelperUtils.updateOperation(session, transaction, paiement);

            LXHelperUtils.updateVentilations(session, transaction, paiement, operation);

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
