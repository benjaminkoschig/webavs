package globaz.helios.helpers.ecritures;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.helpers.ecritures.detteavoir.CGGestionEcritureDetteAvoir;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;

/*
 * Class permettant la modification des écritures, entêtes et mise à jour des soldes.
 */
public class CGGestionEcritureUpdate {

    /**
     * Suppression de l'écriture si son montant est transmis à zéro.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @param ecriture
     * @param totalDebit
     * @param totalCredit
     * @throws Exception
     */
    private static void deleteEcriture(BISession session, BTransaction transaction,
            CGGestionEcritureViewBean ecritures, CGEcritureViewBean ecriture, FWCurrency totalDebit,
            FWCurrency totalCredit) throws Exception {
        ecriture.setSession((BSession) session);
        ecriture.retrieve(transaction);

        if (ecriture.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)
                || ecriture.getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)) {
            totalDebit.sub(ecriture.getMontantBase());
        } else {
            totalCredit.sub(ecriture.getMontantBase());
        }

        ecriture.delete(transaction);

        CGGestionEcritureDetteAvoir.manageDetteAvoir((BSession) session, transaction, ecritures.getJournal(), ecriture,
                false);

        CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, ecritures.getJournal(), ecriture);
    }

    /**
     * Mise à jour de l'écriture avec les nouvelles valeurs. Si nécessaire mise à jour des soldes sur l'ancien compte
     * (si ce dernier a changé).
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @param ecriture
     * @throws Exception
     */
    private static void updateEcriture(BISession session, BTransaction transaction,
            CGGestionEcritureViewBean ecritures, CGEcritureViewBean ecriture, FWCurrency totalDebit,
            FWCurrency totalCredit) throws Exception {
        CGEcritureViewBean newEcritureValue = new CGEcritureViewBean();
        newEcritureValue.setIdCompte(ecriture.getIdCompte());
        newEcritureValue.setIdExterneCompte(ecriture.getIdExterneCompte());
        newEcritureValue.setIdCentreCharge(ecriture.getIdCentreCharge());
        newEcritureValue.setLibelle(ecriture.getLibelle());
        newEcritureValue.setCodeDebitCredit(ecriture.getCodeDebitCredit());
        newEcritureValue.setMontant(ecriture.getMontantAffiche());
        newEcritureValue.setMontantMonnaie(ecriture.getMontantAfficheMonnaie());
        newEcritureValue.setCoursMonnaie(ecriture.getCoursMonnaie());

        newEcritureValue.setDate(ecritures.getDateValeur());
        newEcritureValue.setDateValeur(ecritures.getDateValeur());

        newEcritureValue.setPiece(ecritures.getPiece());

        newEcritureValue.setRemarque(ecritures.getRemarque());

        ecriture.setSession((BSession) session);
        ecriture.retrieve(transaction);

        if (ecriture.hasErrors() || ecriture.isNew()) {
            throw new Exception(((BSession) session).getLabel("GESTION_ECRITURES_ECRITURE_NON_RESOLUE"));
        }

        ecriture.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(session, ecriture));

        if (!ecriture.isEqualsTo(newEcritureValue)) {
            if (ecriture.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)
                    || ecriture.getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)) {
                totalDebit.sub(ecriture.getMontantBase());
            } else {
                totalCredit.sub(ecriture.getMontantBase());
            }

            CGGestionEcritureDetteAvoir.manageDetteAvoir((BSession) session, transaction, ecritures.getJournal(),
                    ecriture, false);

            String oldIdCompte = ecriture.getIdCompte();
            String oldIdCentreCharge = ecriture.getIdCentreCharge();

            ecriture.setIdCompte(newEcritureValue.getIdCompte());
            ecriture.setIdExterneCompte(newEcritureValue.getIdExterneCompte());
            ecriture.setIdCentreCharge(newEcritureValue.getIdCentreCharge());
            ecriture.setLibelle(newEcritureValue.getLibelle());
            ecriture.setCodeDebitCredit(newEcritureValue.getCodeDebitCredit());

            ecriture.setMontant(newEcritureValue.getMontantAffiche());
            ecriture.setMontantMonnaie(newEcritureValue.getMontantAfficheMonnaie());
            ecriture.setCoursMonnaie(newEcritureValue.getCoursMonnaie());

            ecriture.setDate(newEcritureValue.getDateValeur());
            ecriture.setDateValeur(newEcritureValue.getDateValeur());

            ecriture.setPiece(newEcritureValue.getPiece());
            ecriture.setRemarque(newEcritureValue.getRemarque());

            ecriture.update(transaction);

            if (!oldIdCompte.equals(newEcritureValue.getIdCompte())) {
                CGGestionEcritureUtils.updateSolde(session, transaction, ecritures.getJournal(), oldIdCompte, ecritures
                        .getJournal().getIdPeriodeComptable(), CGCompte.AUCUN_CENTRE_CHARGE, true);
                CGGestionEcritureUtils.updateSolde(session, transaction, ecritures.getJournal(), oldIdCompte,
                        CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, CGCompte.AUCUN_CENTRE_CHARGE, true);
            }

            if (!oldIdCentreCharge.equals(newEcritureValue.getIdCentreCharge())
                    && !JadeStringUtil.isIntegerEmpty(oldIdCentreCharge)) {
                CGGestionEcritureUtils.updateSolde(session, transaction, ecritures.getJournal(), oldIdCompte, ecritures
                        .getJournal().getIdPeriodeComptable(), oldIdCentreCharge, true);
                CGGestionEcritureUtils.updateSolde(session, transaction, ecritures.getJournal(), oldIdCompte,
                        CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, oldIdCentreCharge, true);
            }

            CGGestionEcritureDetteAvoir.manageDetteAvoir((BSession) session, transaction, ecritures.getJournal(),
                    ecriture, true);

            CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, ecritures.getJournal(), ecriture);

            if (ecriture.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)
                    || ecriture.getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)) {
                totalDebit.add(ecriture.getMontantBase());
            } else {
                totalCredit.add(ecriture.getMontantBase());
            }
        } else {
            CGGestionEcritureDetteAvoir.manageDetteAvoir((BSession) session, transaction, ecritures.getJournal(),
                    ecriture, false);
            CGGestionEcritureDetteAvoir.manageDetteAvoir((BSession) session, transaction, ecritures.getJournal(),
                    ecriture, true);
        }
    }

    /**
     * Ajout des écritures dans la base de données et mise à jour des soldes et de l'entête avec un libellé et total
     * débit-crédit.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @param entete
     * @throws Exception
     */
    private static void updateEcritures(BISession session, BTransaction transaction,
            CGGestionEcritureViewBean ecritures, CGEnteteEcritureViewBean entete) throws Exception {
        String lastLibelleUsed = "";

        FWCurrency totalDebit = new FWCurrency(entete.getTotalDoit());
        FWCurrency totalCredit = new FWCurrency(entete.getTotalAvoir());
        int countDebit = Integer.parseInt(entete.getNombreDoit());
        int countCredit = Integer.parseInt(entete.getNombreAvoir());
        String lastIdDebit = "0";
        String lastIdCredit = "0";

        Iterator it = ecritures.getEcritures().iterator();
        while (it.hasNext()) {
            CGGestionEcritureUtils.testSaisieEcranEtJournalOuvert(session, ecritures);
            CGEcritureViewBean ecriture = (CGEcritureViewBean) it.next();

            if (JadeStringUtil.isBlank(ecriture.getLibelle())) {
                ecriture.setLibelle(lastLibelleUsed);
            } else {
                lastLibelleUsed = ecriture.getLibelle();
            }

            if (JadeStringUtil.isIntegerEmpty(ecriture.getIdEcriture())) {
                ecriture = CGGestionEcritureUtils.createEcriture(session, transaction, ecritures, entete, ecriture);

                CGGestionEcritureDetteAvoir.manageDetteAvoir((BSession) session, transaction, ecritures.getJournal(),
                        ecriture, true);

                CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, ecritures.getJournal(), ecriture);

                if (ecriture.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)
                        || ecriture.getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)) {
                    totalDebit.add(ecriture.getMontantBase());
                    countDebit++;
                    lastIdDebit = ecriture.getIdEcriture();
                } else {
                    totalCredit.add(ecriture.getMontantBase());
                    countCredit++;
                    lastIdCredit = ecriture.getIdEcriture();
                }

            } else {
                if (!JadeStringUtil.isDecimalEmpty(ecriture.getMontantBase())
                        || !JadeStringUtil.isDecimalEmpty(ecriture.getMontantBaseMonnaie())) {
                    CGGestionEcritureUpdate.updateEcriture(session, transaction, ecritures, ecriture, totalDebit,
                            totalCredit);

                    if (ecriture.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)
                            || ecriture.getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)) {
                        lastIdDebit = ecriture.getIdEcriture();
                    } else {
                        lastIdCredit = ecriture.getIdEcriture();
                    }
                } else {
                    CGGestionEcritureUpdate.deleteEcriture(session, transaction, ecritures, ecriture, totalDebit,
                            totalCredit);

                    if (ecriture.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)
                            || ecriture.getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)) {
                        countDebit--;
                    } else {
                        countCredit--;
                    }
                }
            }
        }

        CGGestionEcritureUtils.updateEnteteWithEcritures(transaction, entete, lastLibelleUsed, totalDebit, totalCredit,
                countDebit, countCredit, lastIdDebit, lastIdCredit);
    }

    /**
     * Mise à jour des écritures depuis l'écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void updateEcritures(BISession session, FWViewBeanInterface viewBean) throws Exception {
        CGGestionEcritureViewBean ecritures = (CGGestionEcritureViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            CGGestionEcritureUpdate.validate(session, transaction, ecritures);

            CGEnteteEcritureViewBean entete = CGGestionEcritureUpdate.updateEntete(session, transaction, ecritures);

            CGGestionEcritureUpdate.updateEcritures(session, transaction, ecritures, entete);
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
     * Mise à jour de l'entête de l'écriture collective.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    private static CGEnteteEcritureViewBean updateEntete(BISession session, BTransaction transaction,
            CGGestionEcritureViewBean ecritures) throws Exception {
        CGEnteteEcritureViewBean entete = CGGestionEcritureUtils.getEntete(session, transaction,
                ecritures.getIdEnteteEcriture());
        entete.setSession((BSession) session);

        entete.setDate(ecritures.getDateValeur());
        entete.setDateValeur(ecritures.getDateValeur());

        entete.setPiece(ecritures.getPiece());

        entete.setIdFournisseur(ecritures.getIdFournisseur());
        entete.setIdSection(ecritures.getIdSection());

        entete.update(transaction);

        return entete;
    }

    /**
     * Validation des informations saisies par l'utilisateur.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    private static void validate(BISession session, BTransaction transaction, CGGestionEcritureViewBean ecritures)
            throws Exception {
        CGGestionEcritureUtils.testJournalAnnule(session, ecritures.getJournal());
        CGGestionEcritureUtils.testDateValeur(session, ecritures);
        CGGestionEcritureUtils.testMinimumDebitCredit(session, ecritures);
        CGGestionEcritureUtils.testTotalDebitCredit(session, ecritures);
        CGGestionEcritureUtils.testSaisieAutresUtilisateurs(session, ecritures.getJournal());
        CGGestionEcritureUtils.testCompteAffilie(session, transaction, ecritures);
        CGGestionEcritureUtils.testSaisieEcranEtJournalOuvert(session, ecritures);
    }
}
