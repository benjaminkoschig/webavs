package globaz.helios.helpers.ecritures;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.helpers.ecritures.detteavoir.CGGestionEcritureDetteAvoir;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;

/*
 * Class permettant l'ajout des écritures, entêtes et mise à jour des soldes.
 */
public class CGGestionEcritureAdd {

    /**
     * Ajout des écritures dans la base de données et mise à jour des soldes et de l'entête avec un libellé et total
     * débit-crédit.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @param entete
     * @param generateDetteAvoir
     * @throws Exception
     */
    private static void addEcritures(BISession session, BTransaction transaction, CGGestionEcritureViewBean ecritures,
            CGEnteteEcritureViewBean entete, boolean generateDetteAvoir) throws Exception {
        String lastLibelleUsed = "";

        FWCurrency totalDebit = new FWCurrency();
        FWCurrency totalCredit = new FWCurrency();
        int countDebit = 0;
        int countCredit = 0;
        String lastIdDebit = "0";
        String lastIdCredit = "0";

        Iterator<?> it = ecritures.getEcritures().iterator();
        while (it.hasNext()) {

            CGGestionEcritureUtils.testSaisieEcranEtJournalOuvert(session, ecritures);

            CGEcritureViewBean ecriture = (CGEcritureViewBean) it.next();

            if (!JadeStringUtil.isDecimalEmpty(ecriture.getMontantBase())
                    || !JadeStringUtil.isDecimalEmpty(ecriture.getMontantBaseMonnaie())) {
                if (JadeStringUtil.isBlank(ecriture.getLibelle())) {
                    ecriture.setLibelle(lastLibelleUsed);
                } else {
                    lastLibelleUsed = ecriture.getLibelle();
                }

                ecriture = CGGestionEcritureUtils.createEcriture(session, transaction, ecritures, entete, ecriture);

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

                CGGestionEcritureUtils.updateSoldesProvisoire(session, transaction, ecritures.getJournal(), ecriture);

                if (generateDetteAvoir) {
                    CGGestionEcritureDetteAvoir.manageDetteAvoir((BSession) session, transaction,
                            ecritures.getJournal(), ecriture, true);
                }
            }
        }

        CGGestionEcritureUtils.updateEnteteWithEcritures(transaction, entete, lastLibelleUsed, totalDebit, totalCredit,
                countDebit, countCredit, lastIdDebit, lastIdCredit);
    }

    /**
     * Ajout d'écriture depuis un processus. <br/>
     * Process : Extourne
     * 
     * @param session
     * @param transaction
     * @param viewBean
     * @throws Exception
     */
    public static void addEcritures(BISession session, BTransaction transaction, FWViewBeanInterface viewBean,
            boolean generateDetteAvoir) throws Exception {
        CGGestionEcritureViewBean ecritures = (CGGestionEcritureViewBean) viewBean;

        CGGestionEcritureAdd.validate(session, transaction, ecritures);

        CGEnteteEcritureViewBean entete = CGGestionEcritureAdd.addEntete(session, transaction, ecritures);

        CGGestionEcritureAdd.addEcritures(session, transaction, ecritures, entete, generateDetteAvoir);
    }

    /**
     * Ajout des écritures depuis l'écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void addEcritures(BISession session, FWViewBeanInterface viewBean) throws Exception {
        CGGestionEcritureViewBean ecritures = (CGGestionEcritureViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            CGGestionEcritureAdd.validate(session, transaction, ecritures);

            CGEnteteEcritureViewBean entete = CGGestionEcritureAdd.addEntete(session, transaction, ecritures);

            CGGestionEcritureAdd.addEcritures(session, transaction, ecritures, entete, true);
        } catch (Exception e) {
            if (transaction != null) {
                if (!JadeStringUtil.isBlank(e.getMessage())) {
                    transaction.addErrors(e.getMessage());
                }
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
     * Ajout de l'entête de l'écriture collective.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    private static CGEnteteEcritureViewBean addEntete(BISession session, BTransaction transaction,
            CGGestionEcritureViewBean ecritures) throws Exception {
        CGEnteteEcritureViewBean entete = new CGEnteteEcritureViewBean();

        entete.setSession((BSession) session);

        entete.setIdJournal(ecritures.getJournal().getIdJournal());

        entete.setDate(ecritures.getDateValeur());
        entete.setDateValeur(ecritures.getDateValeur());

        entete.setPiece(ecritures.getPiece());

        entete.setIdFournisseur(ecritures.getIdFournisseur());
        entete.setIdSection(ecritures.getIdSection());

        entete.setIdTypeEcriture(CGEcritureViewBean.CS_TYPE_ECRITURE_COLLECTIVE);

        entete.add(transaction);

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
