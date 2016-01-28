package globaz.helios.helpers.parammodeles;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.modeles.CGEnteteModeleEcriture;
import globaz.helios.db.modeles.CGGestionModeleViewBean;
import globaz.helios.db.modeles.CGLigneModeleEcriture;
import globaz.helios.helpers.parammodeles.utils.CGGestionModeleUtils;
import java.util.Iterator;

public class CGGestionModeleDelete {

    /**
     * Suppression de l'entête.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    private static void deleteEntete(BISession session, BTransaction transaction, CGGestionModeleViewBean modeles)
            throws Exception {
        CGEnteteModeleEcriture entete = CGGestionModeleUtils.getEntete(session, transaction,
                modeles.getIdEnteteModeleEcriture());
        entete.setSession((BSession) session);

        entete.delete(transaction);
    }

    /**
     * Suppression des lignes.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    private static void deleteLignes(BISession session, BTransaction transaction, CGGestionModeleViewBean modeles)
            throws Exception {
        Iterator it = modeles.getLignes().iterator();
        while (it.hasNext()) {
            CGLigneModeleEcriture ligne = (CGLigneModeleEcriture) it.next();
            ligne.setSession((BSession) session);
            ligne.retrieve(transaction);

            ligne.delete(transaction);
        }
    }

    /**
     * Suppression de l'entête et des lignes du modèles d'écritures.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void deleteModele(BISession session, FWViewBeanInterface viewBean) throws Exception {
        CGGestionModeleViewBean modeles = (CGGestionModeleViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            validate(session, modeles);

            deleteLignes(session, transaction, modeles);

            deleteEntete(session, transaction, modeles);
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
     * Validation des informations saisies par l'utilisateur.
     * 
     * @param session
     * @param modeles
     * @throws Exception
     */
    private static void validate(BISession session, CGGestionModeleViewBean modeles) throws Exception {
        // TODO dda : 23 avr. 08
        // A implémenter
    }
}
