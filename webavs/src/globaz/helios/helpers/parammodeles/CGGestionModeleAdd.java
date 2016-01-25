package globaz.helios.helpers.parammodeles;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.modeles.CGEnteteModeleEcriture;
import globaz.helios.db.modeles.CGGestionModeleViewBean;
import globaz.helios.db.modeles.CGLigneModeleEcriture;
import globaz.helios.helpers.parammodeles.utils.CGGestionModeleUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;

public class CGGestionModeleAdd {

    /**
     * Ajout de l'entête du modèle.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    private static CGEnteteModeleEcriture addEntete(BISession session, BTransaction transaction,
            CGGestionModeleViewBean modeles) throws Exception {
        CGEnteteModeleEcriture entete = new CGEnteteModeleEcriture();
        entete.setSession((BSession) session);

        entete.setIdTypeEcriture(CGEcritureViewBean.CS_TYPE_ECRITURE_COLLECTIVE);
        entete.setIdModeleEcriture(modeles.getIdModeleEcriture());
        entete.setIdMandat(modeles.getIdMandat());

        entete.add(transaction);

        return entete;
    }

    /**
     * Ajout des lignes.
     * 
     * @param session
     * @param transaction
     * @param modeles
     * @param entete
     * @param generateDetteAvoir
     * @throws Exception
     */
    private static void addLignes(BISession session, BTransaction transaction, CGGestionModeleViewBean modeles,
            CGEnteteModeleEcriture entete, boolean generateDetteAvoir) throws Exception {
        String lastLibelleUsed = "";

        Iterator it = modeles.getLignes().iterator();
        while (it.hasNext()) {
            CGLigneModeleEcriture ligne = (CGLigneModeleEcriture) it.next();

            if (!JadeStringUtil.isIntegerEmpty(ligne.getIdCompte())) {
                if (JadeStringUtil.isBlank(ligne.getLibelle())) {
                    ligne.setLibelle(lastLibelleUsed);
                } else {
                    lastLibelleUsed = ligne.getLibelle();
                }

                CGGestionModeleUtils.createLigne(session, transaction, modeles, entete, ligne);
            }
        }

        entete.setLibelle(lastLibelleUsed);
        entete.update(transaction);
    }

    /**
     * Ajout du modele depuis l'écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void addModele(BISession session, FWViewBeanInterface viewBean) throws Exception {
        CGGestionModeleViewBean modeles = (CGGestionModeleViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            CGEnteteModeleEcriture entete = addEntete(session, transaction, modeles);

            validate(session, modeles, entete);

            addLignes(session, transaction, modeles, entete, true);
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
     * @param entete
     * @throws Exception
     */
    private static void validate(BISession session, CGGestionModeleViewBean modeles, CGEnteteModeleEcriture entete)
            throws Exception {
        CGGestionModeleUtils.testCompteAffilie(session, modeles, entete);
    }
}
