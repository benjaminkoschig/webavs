package globaz.helios.helpers.ecritures;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.helpers.ecritures.detteavoir.CGGestionEcritureDetteAvoir;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import java.util.Iterator;

/*
 * Class permettant la modification des écritures, entêtes et mise à jour des soldes.
 */
public class CGGestionEcritureDelete {

    /**
     * Ajout des écritures dans la base de données et mise à jour des soldes et de l'entête avec un libellé et total
     * débit-crédit.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    private static void deleteEcritures(BISession session, BTransaction transaction, CGGestionEcritureViewBean ecritures)
            throws Exception {
        Iterator it = ecritures.getEcritures().iterator();
        while (it.hasNext()) {
            CGEcritureViewBean ecriture = (CGEcritureViewBean) it.next();
            ecriture.setSession((BSession) session);
            ecriture.retrieve(transaction);

            ecriture.delete(transaction);

            CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, ecritures.getJournal(), ecriture);

            CGGestionEcritureDetteAvoir.manageDetteAvoir((BSession) session, transaction, ecritures.getJournal(),
                    ecriture, false);
        }
    }

    /**
     * Suppression des écritures depuis l'écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void deleteEcritures(BISession session, FWViewBeanInterface viewBean) throws Exception {
        CGGestionEcritureViewBean ecritures = (CGGestionEcritureViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            validate(session, ecritures);

            deleteEcritures(session, transaction, ecritures);

            deleteEntete(session, transaction, ecritures);
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

    /**
     * Suppression de l'entête de l'écriture collective.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    private static void deleteEntete(BISession session, BTransaction transaction, CGGestionEcritureViewBean ecritures)
            throws Exception {
        CGEnteteEcritureViewBean entete = CGGestionEcritureUtils.getEntete(session, transaction,
                ecritures.getIdEnteteEcriture());
        entete.setSession((BSession) session);

        entete.delete(transaction);
    }

    /**
     * Validation des informations saisies par l'utilisateur.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    private static void validate(BISession session, CGGestionEcritureViewBean ecritures) throws Exception {
        CGGestionEcritureUtils.testSaisieAutresUtilisateurs(session, ecritures.getJournal());
    }
}
