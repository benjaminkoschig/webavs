package globaz.helios.process.journal;

import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.api.ICGJournal;
import globaz.helios.db.comptes.CGJournal;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAControleOperationSection;
import globaz.osiris.db.comptes.CAControleOperationSectionManager;
import globaz.osiris.db.comptes.CAControleSectionCompteAnnexe;
import globaz.osiris.db.comptes.CAControleSectionCompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;

public class CAJournalProcessUtils {

    private CAJournalProcessUtils() {
        // Nothing to do
    }

    /**
     * Remise à zéro du log des messages du journal.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @return
     */
    public static boolean resetJournalLog(BSession session, BTransaction transaction, CGJournal journal) {
        try {
            if (!JadeStringUtil.isIntegerEmpty(journal.getIdLog())) {
                FWLog log = new FWLog();
                log.setSession(session);
                log.setIdLog(journal.getIdLog());

                log.retrieve(transaction);

                if (!log.hasErrors() && !log.isNew()) {
                    log.delete(transaction);
                }

                journal.setIdLog("0");
                journal.update(transaction);

                if (transaction.hasErrors()) {
                    throw (new Exception(session.getLabel("COMPTABILISER_JOURNAL_LOG_ERROR")));
                } else {
                    transaction.commit();
                }
            }
        } catch (Exception e) {
            session.addError(e.getMessage());
            try {
                transaction.rollback();
            } catch (Exception ee) {
                return false;
            }
            return false;
        }

        return true;
    }

    /**
     * Vérification et modification si besoin du montant de la section si elle n'est pas en ligne avec les sommes de ces
     * opérations.
     * ET
     * Vérification et modification si besoin du montant du compte annexe si elle n'est pas en ligne avec les sommes de
     * ces sections.
     * 
     * @param session La session.
     * @param idJournal L'id du journal CA.
     */
    public static void manageControleDesSoldesOperationACompteAnnexe(final BSession session, final String idJournal) {
        if (CAApplication.getApplicationOsiris().getCAParametres().isModeRecalculSoldesSectionsCompteAnnexes()) {
            manageSoldeOperationAndSection(session, idJournal);
            manageSoldeSectionAndCompteAnnexe(session, idJournal);
        }
    }

    /**
     * Vérification et modification si besoin du montant du compte annexe si elle n'est pas en ligne avec les sommes de
     * ces sections.
     * 
     * @param session La session.
     * @param idJournal L'id du journal CA.
     */
    public static void manageSoldeSectionAndCompteAnnexe(final BSession session, final String idJournal) {
        try {
            // Contrôle entre la somme des montants des sections et le solde de leur compte annexe
            final CAControleSectionCompteAnnexeManager controleSectionsComptes = new CAControleSectionCompteAnnexeManager();
            controleSectionsComptes.setSession(session);
            controleSectionsComptes.setForIdJournal(idJournal);

            controleSectionsComptes.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < controleSectionsComptes.size(); i++) {
                final CAControleSectionCompteAnnexe sectionCompte = (CAControleSectionCompteAnnexe) controleSectionsComptes
                        .getContainer().get(i);

                changeAmountCompteAnnexe(session, sectionCompte);
            }
        } catch (Exception e) { // Si nous avons un problème lors de la recherche, nous quittons sans bloquer les
                                // processus appellant, nous ne faisons que de loguer.
            JadeLogger.error(e, "We can not do the check between section and compte annexe" + e.getMessage());
            return;
        }
    }

    private static void changeAmountCompteAnnexe(final BSession session,
            final CAControleSectionCompteAnnexe sectionCompte) {

        try {
            // Recherche du compte annexe en question
            final CACompteAnnexe compteAnnexeACorriger = new CACompteAnnexe();
            compteAnnexeACorriger.setSession(session);
            compteAnnexeACorriger.setIdCompteAnnexe(sectionCompte.getIdCompteAnnexe());
            compteAnnexeACorriger.retrieve();

            if (!compteAnnexeACorriger.isNew()) {
                // Modification du solde du compte annexes avec la somme de ces sections
                compteAnnexeACorriger.setSolde(sectionCompte.getSommeSections());
                compteAnnexeACorriger.update();
            }

        } catch (Exception e) { // Si nous avons un problème lors de la recherche, nous quittons sans bloquer les
                                // processus appellant, nous ne faisons que de loguer.
            final String message = "We can not update the balance of compte annexe with id : "
                    + sectionCompte.getIdCompteAnnexe() + " for amount of : " + sectionCompte.getSommeSections();
            JadeLogger.error(e, message + e.getMessage());
        }
    }

    /**
     * Vérification et modification si besoin du montant de la section si elle n'est pas en ligne avec les sommes de ces
     * opérations.
     * 
     * @param session La session.
     * @param idJournal L'id du journal CA.
     */
    public static void manageSoldeOperationAndSection(final BSession session, final String idJournal) {
        try {
            // Contrôle entre la somme des montants des opérations et le solde de leur section (chaque section du
            // journal)
            final CAControleOperationSectionManager controleOperationsSections = new CAControleOperationSectionManager();
            controleOperationsSections.setSession(session);
            controleOperationsSections.setForIdJournal(idJournal);

            controleOperationsSections.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < controleOperationsSections.size(); i++) {
                final CAControleOperationSection operationSection = (CAControleOperationSection) controleOperationsSections
                        .getContainer().get(i);

                changeAmountSection(session, operationSection);
            }
        } catch (Exception e) { // Si nous avons un problème lors de la recherche, nous quittons sans bloquer les
                                // processus appellant, nous ne faisons que de loguer.
            JadeLogger.error(e, "We can not do the check between operation and section" + e.getMessage());
        }
    }

    private static void changeAmountSection(final BSession session, final CAControleOperationSection operationSection) {
        try {
            // Recherche de la section en question
            final CASection sectionACorriger = new CASection();
            sectionACorriger.setSession(session);
            sectionACorriger.setIdSection(operationSection.getIdSection());
            sectionACorriger.retrieve();

            if (!sectionACorriger.isNew()) {
                // Modification du solde de la section avec la somme de ces opérations
                sectionACorriger.setSolde(operationSection.getSommeOperations());
                sectionACorriger.update();
            }
        } catch (Exception e) { // Si nous avons un problème lors de la recherche, nous quittons sans bloquer les
                                // processus appellant, nous ne faisons que de loguer.
            final String message = "We can not update the balance of section with id : "
                    + operationSection.getIdSection() + " for amount of : " + operationSection.getSommeOperations();
            JadeLogger.error(e, message + e.getMessage());
        }
    }

    /**
     * Mise à jour de l'état du journal à traitement.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @return
     */
    public static boolean setEtatJournalToTraitement(BSession session, BTransaction transaction, CGJournal journal) {
        try {
            journal.setIdEtat(ICGJournal.CS_ETAT_TRAITEMENT);
            journal.update(transaction);

            if (transaction.hasErrors()) {
                throw new Exception(session.getLabel("COMPTABILISER_JOURNAL_ERROR_1"));
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            session.addError(e.getMessage());
            try {
                transaction.rollback();
            } catch (Exception ee) {
                return false;
            }
            return false;
        }

        return true;
    }

    /**
     * Mise à jour de l'état du journal.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param etat
     * @throws Exception
     */
    public static void updateEtatJournal(BSession session, BTransaction transaction, CGJournal journal, String etat)
            throws Exception {
        journal.setIdEtat(etat);
        journal.update(transaction);

        if (transaction.hasErrors()) {
            transaction.rollback();
            throw new Exception(session.getLabel("COMPTABILISER_JOURNAL_ERR_1"));
        } else {
            transaction.commit();
        }
    }

    /**
     * Mise à jour du log du journal après traitement.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param journalLog
     * @param memoryLog
     * @return
     */
    public static boolean updateLogJournal(BSession session, BTransaction transaction, CGJournal journal,
            FWMemoryLog journalLog, FWMemoryLog memoryLog) {
        try {
            if (journalLog.hasMessages()) {
                FWLog log = journalLog.saveToFWLog(transaction);

                if (log == null || log.isNew()) {
                    throw new Exception(session.getLabel("COMPTABILISER_JOURNAL_ERROR_6"));
                } else {
                    journal.setIdLog(log.getIdLog());
                    journal.update(transaction);
                }
            }

            if (transaction.hasErrors()) {
                throw new Exception(session.getLabel("COMPTABILISER_JOURNAL_ERROR_6"));
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            memoryLog.logMessage(e.getMessage(), FWMessage.AVERTISSEMENT, session.getLabel("GLOBAL_JOURNAL") + " N°"
                    + journal.getNumero());
            try {
                transaction.rollback();
            } catch (Exception ee) {
                return false;
            }
        }

        return true;
    }
}
