package globaz.helios.helpers.parammodeles;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.modeles.CGEnteteModeleEcriture;
import globaz.helios.db.modeles.CGGestionModeleViewBean;
import globaz.helios.db.modeles.CGLigneModeleEcriture;
import globaz.helios.helpers.parammodeles.utils.CGGestionModeleUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;

public class CGGestionModeleUpdate {

    /**
     * Suppression d'une ligne.
     * 
     * @param session
     * @param transaction
     * @param modeles
     * @param ligne
     * @throws Exception
     */
    private static void deleteLigne(BISession session, BTransaction transaction, CGGestionModeleViewBean modeles,
            CGLigneModeleEcriture ligne) throws Exception {
        ligne.setSession((BSession) session);
        ligne.retrieve(transaction);

        ligne.delete(transaction);
    }

    /**
     * Mise � jour d'une ligne du mod�le.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @param ecriture
     * @throws Exception
     */
    private static void updateLigne(BISession session, BTransaction transaction, CGGestionModeleViewBean modeles,
            CGLigneModeleEcriture ligne) throws Exception {
        CGLigneModeleEcriture newLigne = new CGLigneModeleEcriture();
        newLigne.setIdCompte(ligne.getIdCompte());
        newLigne.setIdExterneCompte(ligne.getIdExterneCompte());
        newLigne.setIdCentreCharge(ligne.getIdCentreCharge());
        newLigne.setLibelle(ligne.getLibelle());
        newLigne.setCodeDebitCredit(ligne.getCodeDebitCredit());
        newLigne.setMontant(ligne.getMontant());
        newLigne.setMontantMonnaie(ligne.getMontantMonnaie());
        newLigne.setCoursMonnaie(ligne.getCoursMonnaie());

        ligne.setSession((BSession) session);
        ligne.retrieve(transaction);

        if (ligne.hasErrors() || ligne.isNew()) {
            throw new Exception(((BSession) session).getLabel("GESTION_MODELES_CREATION_PROBLEME")
                    + " (line not found)");
        }

        ligne.setIdExterneCompte(ligne.getIdExterne());

        if (!ligne.isEqualsTo(newLigne)) {
            ligne.setIdCompte(newLigne.getIdCompte());
            ligne.setIdExterneCompte(newLigne.getIdExterneCompte());
            ligne.setIdCentreCharge(newLigne.getIdCentreCharge());
            ligne.setLibelle(newLigne.getLibelle());
            ligne.setCodeDebitCredit(newLigne.getCodeDebitCredit());

            ligne.setMontant(newLigne.getMontant());
            ligne.setMontantMonnaie(newLigne.getMontantMonnaie());
            ligne.setCoursMonnaie(newLigne.getCoursMonnaie());

            ligne.update(transaction);
        }
    }

    /**
     * Mise � jour du mod�le.
     * 
     * @param session
     * @param transaction
     * @param modeles
     * @param entete
     * @throws Exception
     */
    private static void updateLignes(BISession session, BTransaction transaction, CGGestionModeleViewBean modeles,
            CGEnteteModeleEcriture entete) throws Exception {
        String lastLibelleUsed = "";

        Iterator it = modeles.getLignes().iterator();
        while (it.hasNext()) {
            CGLigneModeleEcriture ligne = (CGLigneModeleEcriture) it.next();

            if (JadeStringUtil.isBlank(ligne.getLibelle())) {
                ligne.setLibelle(lastLibelleUsed);
            } else {
                lastLibelleUsed = ligne.getLibelle();
            }

            if (JadeStringUtil.isIntegerEmpty(ligne.getIdLigneModeleEcriture())) {
                CGGestionModeleUtils.createLigne(session, transaction, modeles, entete, ligne);
            } else if (JadeStringUtil.isIntegerEmpty(ligne.getIdExterneCompte())) {
                deleteLigne(session, transaction, modeles, ligne);
            } else {
                updateLigne(session, transaction, modeles, ligne);
            }
        }

        entete.setLibelle(lastLibelleUsed);
        entete.update(transaction);
    }

    /**
     * Mise � jour de l'ent�te et des lignes du mod�les depuis l'�cran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void updateModele(BISession session, FWViewBeanInterface viewBean) throws Exception {
        CGGestionModeleViewBean modeles = (CGGestionModeleViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            CGEnteteModeleEcriture entete = CGGestionModeleUtils.getEntete(session, transaction,
                    modeles.getIdEnteteModeleEcriture());

            validate(session, modeles, entete);

            updateLignes(session, transaction, modeles, entete);
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
