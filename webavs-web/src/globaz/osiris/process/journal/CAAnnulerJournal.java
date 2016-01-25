package globaz.osiris.process.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.helios.api.ICGJournal;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAGroupement;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.external.IntJournalCG;
import java.util.ArrayList;

/**
 * Annulation d'un journal de comptabilisation.
 * 
 * @author dda
 */
public class CAAnnulerJournal {
    private static final String LABEL_5025 = "5025";
    private static final String LABEL_5026 = "5026";
    private static final String LABEL_5027 = "5027";
    private static final String LABEL_6111 = "6111";
    private boolean orderDescending = false;
    private CAUtilsJournal utils;

    /**
     * Constructeur de classe.
     */
    public CAAnnulerJournal() {
        utils = new CAUtilsJournal();
    }

    /**
     * Annulation le journal de comptabilisation. Annulation des opérations contenues dans le journal + annulation
     * Journal Helios.
     * 
     * @param context
     * @param journal
     */
    public void annuler(BProcess context, CAJournal journal) {
        if (!checkEtatJournal(context, journal)) {
            return;
        }

        BTransaction readTransaction = null;

        try {
            readTransaction = (BTransaction) journal.getSession().newTransaction();
            readTransaction.openTransaction();

            if (utils.isInterfaceCgActive(journal.getSession())
                    && !utils.isPeriodeComptableOuverte(journal.getSession(), readTransaction,
                            journal.getDateValeurCG())) {
                journal.getMemoryLog().logMessage(readTransaction.getErrors().toString(), FWMessage.FATAL,
                        this.getClass().getName());
                return;
            }

            if (utils.hasInteretsComptabilises(journal.getSession(), readTransaction, journal.getIdJournal())) {
                journal.getMemoryLog().logMessage(journal.getSession().getLabel("JOURNAL_INTERET_COMPTABILISE"),
                        FWMessage.FATAL, this.getClass().getName());
                return;
            }

            if (!setEtatJournalToTraitement(context, journal)) {
                return;
            }

            doJournalCGAnnulation(context, journal);

            if (!processIterationForDesactivation(context, readTransaction, journal)) {
                return;
            }

            annulerInteretsMoratoires(context, journal);

            journal.setEtat(CAJournal.ANNULE);

        } catch (Exception e) {
            context.getTransaction().addErrors(e.getMessage());
            if (readTransaction != null) {
                try {
                    readTransaction.rollback();
                } catch (Exception eTransactionRollback) {
                    JadeLogger.error(this, eTransactionRollback);
                }
            }
        } finally {
            if (readTransaction != null) {
                try {
                    if (readTransaction.hasErrors()) {
                        readTransaction.rollback();
                    }
                } catch (Exception e) {
                    JadeLogger.warn(this, "Problem in closeTransaction()");
                } finally {
                    try {
                        readTransaction.closeTransaction();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * Annuler les intérêts moratoires (comptabilité AVS uniquement).
     * 
     * @param context
     * @param journal
     * @throws Exception
     */
    private void annulerInteretsMoratoires(BProcess context, CAJournal journal) throws Exception {
        if (utils.isComptabiliteAvs(journal.getSession())) {
            CAInteretMoratoireManager mgr = new CAInteretMoratoireManager();
            mgr.setSession(context.getSession());

            mgr.setForIdJournalCalcul(journal.getIdJournal());

            ArrayList<String> forIdGenreInteretIn = new ArrayList<String>();
            forIdGenreInteretIn.add(CAGenreInteret.CS_TYPE_TARDIF);
            forIdGenreInteretIn.add(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES);
            mgr.setForIdGenreInteretIn(forIdGenreInteretIn);

            context.setState(journal.getSession().getLabel("IM_ANNULER"));
            context.setProgressScaleValue(mgr.getCount(context.getTransaction()));

            mgr.find(context.getTransaction(), BManager.SIZE_NOLIMIT);

            for (int i = 0; i < mgr.size(); i++) {
                CAInteretMoratoire im = (CAInteretMoratoire) mgr.get(i);
                im.setSession(context.getSession());

                if (im.getIdGenreInteret().equals(CAGenreInteret.CS_TYPE_TARDIF)) {
                    im.delete(context.getTransaction());
                    // Inforom321 - suppression du test
                    // } else {
                    // this.resetInteretMoratoireToAttentePaiement(context, im);
                }

                context.incProgressCounter();
            }
        }
    }

    /**
     * Vérifier l'état. Annulation impossible pour les journaux dont l'état est égal à ANNULE ou de type BULLETIN
     * NEUTRE.
     * 
     * @param context
     * @param journal
     * @return True : Si l'état du journal n'est pas ANNULE ou de type BULLETIN NEUTRE.
     */
    private boolean checkEtatJournal(BProcess context, CAJournal journal) {
        if (journal.getEtat().equals(CAJournal.ANNULE)) {
            context.getTransaction().addErrors(journal.getSession().getLabel(CAAnnulerJournal.LABEL_5026));
            return false;
        } else if (CAJournal.TYPE_BULLETIN_NEUTRE.equals(journal.getTypeJournal())) {
            context.getTransaction().addErrors(journal.getSession().getLabel("JOURNAL_ANNULE_BULLETIN_NEUTRE"));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Annuler le journal de comptabilité générale si nécessaire.
     * 
     * @param context
     * @param journal
     * @throws Exception
     */
    private void doJournalCGAnnulation(BProcess context, CAJournal journal) throws Exception {
        if (utils.isInterfaceCgActive(journal.getSession())) {
            IntJournalCG journalCg = utils.getJournalCG(journal.getSession(), context.getTransaction(),
                    journal.getNoJournalCG());
            if (journalCg != null) {

                if (JadeStringUtil.isBlank(context.getEMailAddress())) {
                    journalCg.annuler(context.getTransaction());
                } else {
                    journalCg.annuler(context.getTransaction(), context.getEMailAddress());
                }

                if ((journalCg.isOnError()) || (journalCg.getIdEtat().equals(ICGJournal.CS_ETAT_ERREUR))) {
                    context.getTransaction().addErrors(journal.getSession().getLabel("5190"));
                } else {
                    journal.setNoJournalCG("0");
                }
            }
        }
    }

    /**
     * Initialise le manager d'opérations pour la boucle de désactivations des opérations.
     * 
     * @param journal
     * @param mgr
     */
    private void initOperationManager(CAJournal journal, CAOperationManager mgr) {
        mgr.setSession(journal.getSession());

        // Récupérer les opérations associées au journal
        if (CAJournal.TYPE_CONTENTIEUX.equals(journal.getTypeJournal())
                || CAJournal.TYPE_JOURNALIER_CONTENTIEUX.equals(journal.getTypeJournal())) {
            /*
             * dans le cas de l'annulation d'un journal de contentieux (aquila), il faut désactiver les opérations dans
             * l'ordre inverse d'ajout de manière à pouvoir annuler les étapes du contentieux dans l'ordre inverse de
             * passage, sinon erreur.
             */
            mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION_DESC);
            orderDescending = true;
        } else {
            mgr.setOrderBy(CAOperationManager.ORDER_IDOPERATION);
            orderDescending = false;
        }
        mgr.setForIdJournal(journal.getIdJournal());

        ArrayList etatIn = new ArrayList();
        etatIn.add(APIOperation.ETAT_INACTIF);
        etatIn.add(APIOperation.ETAT_PROVISOIRE);
        etatIn.add(APIOperation.ETAT_COMPTABILISE);
        etatIn.add(APIOperation.ETAT_VERSE);
        etatIn.add(APIOperation.ETAT_ERREUR_VERSEMENT);
        mgr.setForEtatIn(etatIn);
    }

    /**
     * Boucle de désactivation des opérations (tout types). While boolean + for ... on traitera toutes les opérations
     * par paquet de 100. <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param context
     * @param readTransaction
     * @param journal
     * @return True : Si aucune erreur.
     * @throws Exception
     */
    private boolean processIterationForDesactivation(BProcess context, BTransaction readTransaction, CAJournal journal)
            throws Exception {
        // Instancier un nouveau manager
        CAOperationManager mgr = new CAOperationManager();
        initOperationManager(journal, mgr);

        // Compter le nombre d'opération
        context.setState(journal.getSession().getLabel(CAAnnulerJournal.LABEL_6111));

        boolean allOperationsProcessed = false;
        String lastIdOperation = "0";

        while (!allOperationsProcessed) {
            try {
                mgr.clear();
                mgr.find(readTransaction);
            } catch (Exception e) {
                context.getTransaction().addErrors(e.getMessage());
                return false;
            }

            context.setProgressScaleValue(mgr.size());

            allOperationsProcessed = (mgr.size() == 0);

            // Vérifier la condition de sortie
            if (context.isAborted()) {
                return false;
            }

            for (int i = 0; i < mgr.size(); i++) {
                // Récupérer une opération et la convertir dans le type
                // d'opération
                CAOperation operX = (CAOperation) mgr.getEntity(i);
                CAOperation oper = operX.getOperationFromType(readTransaction);

                // Si l'opération n'a pas été convertie
                if (oper == null) {
                    journal.getMemoryLog().logMessage(
                            journal.getSession().getLabel(CAUtilsJournal.LABEL_5013) + " " + operX.toString(),
                            FWViewBeanInterface.WARNING, this.getClass().getName());
                } else {
                    // Conserver le dernier ID
                    lastIdOperation = oper.getIdOperation();

                    String etatBeforeDesactivation = oper.getEtat();

                    // On tente de la désactiver
                    oper.desactiver(context.getTransaction());

                    // Signaler l'erreur de désactivation
                    if (context.getTransaction().hasErrors()) {
                        journal.getMemoryLog().logMessage(
                                journal.getSession().getLabel(CAAnnulerJournal.LABEL_5027) + " " + oper.toString(),
                                FWMessage.ERREUR, this.getClass().getName());
                        context.getTransaction().rollback();
                        context.getTransaction().clearErrorBuffer();
                    } else {
                        try {
                            if (oper.getCodeMaster().equals(APIOperation.SLAVE)
                                    || oper.hasGroupementOperation(context.getTransaction(), CAGroupement.MASTER)
                                    || oper.isInstanceOrSubClassOf(APIOperation.CAECRITURELISSAGE)) {
                                oper.delete(context.getTransaction());
                            } else {
                                oper.update(context.getTransaction());
                            }

                            if (etatBeforeDesactivation.equals(APIOperation.ETAT_COMPTABILISE)) {
                                utils.updateCompteCourantForAnnulation(journal.getSession(), context.getTransaction(),
                                        oper.getIdCompteCourant(), oper.getMontant());
                            }

                            // Vérifier les erreurs
                            if (context.getTransaction().hasErrors()) {
                                journal.getMemoryLog().logMessage(
                                        journal.getSession().getLabel(CAAnnulerJournal.LABEL_5025) + " "
                                                + oper.toString(), FWMessage.ERREUR, this.getClass().getName());
                                context.getTransaction().rollback();
                                context.getTransaction().clearErrorBuffer();
                            } else {
                                context.getTransaction().commit();
                            }
                        } catch (Exception e) {
                            journal.getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                                    this.getClass().getName());
                        }
                    }
                }

                context.incProgressCounter();
            }
            if (orderDescending) {
                mgr.setBeforeIdOperation(lastIdOperation);
            } else {
                mgr.setAfterIdOperation(lastIdOperation);
            }
        }

        if (journal.getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Inforom321 - suppression de la méthode<br>
     * Efface les détails créés par la compta. aux et reset l'intérêt moratoire en attente de paiement.
     * 
     * @param context
     * @param im
     * @throws Exception
     */
    // private void resetInteretMoratoireToAttentePaiement(BProcess context, CAInteretMoratoire im) throws Exception {
    // if (!im.getIdJournalCalcul().equals(im.getIdJournalFacturation())
    // && !im.getIdSection().equals(im.getIdSectionFacture())) {
    // CADetailInteretMoratoireManager manager = new CADetailInteretMoratoireManager();
    // manager.setSession(context.getSession());
    //
    // manager.setForIdInteretMoratoire(im.getIdInteretMoratoire());
    //
    // manager.find(context.getTransaction(), BManager.SIZE_NOLIMIT);
    //
    // String saveIdPassageFacturation = "";
    //
    // if (!manager.isEmpty()) {
    // for (int j = 0; j < manager.size(); j++) {
    // CADetailInteretMoratoire detail = (CADetailInteretMoratoire) manager.get(j);
    //
    // if (JadeStringUtil.isIntegerEmpty(detail.getIdJournalFacturation())) {
    // detail.delete(context.getTransaction());
    // } else {
    // saveIdPassageFacturation = detail.getIdJournalFacturation();
    // }
    // }
    //
    // im.setIdJournalCalcul(saveIdPassageFacturation);
    // im.setIdJournalFacturation(saveIdPassageFacturation);
    // im.setIdSection(im.getIdSectionFacture());
    // im.setMotifcalcul(CAInteretMoratoire.CS_ATTENTE_PAIEMENT);
    //
    // im.update(context.getTransaction());
    // }
    // }
    // }

    /**
     * Set l'état du journal à "Traitement". <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param context
     * @param journal
     * @return True : Si aucun problème et commit effectué.
     * @throws Exception
     */
    private boolean setEtatJournalToTraitement(BProcess context, CAJournal journal) throws Exception {
        journal.setEtat(CAJournal.TRAITEMENT);
        journal.update(context.getTransaction());
        if (context.getTransaction().hasErrors()) {
            context.getTransaction().rollback();
            journal.getMemoryLog().logMessage(journal.getSession().getLabel("5337"), FWMessage.FATAL,
                    this.getClass().getName());
            return false;
        } else {
            context.getTransaction().commit();
            return true;
        }
    }
}
