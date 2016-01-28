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
 * Class permettant la modification des �critures, ent�tes et mise � jour des soldes.
 */
public class CGGestionEcritureDelete {

    /**
     * Ajout des �critures dans la base de donn�es et mise � jour des soldes et de l'ent�te avec un libell� et total
     * d�bit-cr�dit.
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
     * Suppression des �critures depuis l'�cran.
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
     * Suppression de l'ent�te de l'�criture collective.
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
