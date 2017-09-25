package globaz.osiris.process.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.export.FWIExportManager;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.api.ICGEcritureDouble;
import globaz.helios.process.journal.CAJournalProcessUtils;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.LotExec;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.musca.application.FAApplication;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.application.CAParametres;
import globaz.osiris.db.bulletinSolde.CABulletinSolde;
import globaz.osiris.db.bulletinSolde.CABulletinSoldeManager;
import globaz.osiris.db.comptes.CACompensationForCalculIMManager;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAGroupement;
import globaz.osiris.db.comptes.CAGroupementOperation;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAPaiement;
import globaz.osiris.db.comptes.CAPaiementForCalculIMManager;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.interet.tardif.CAInteretTardif;
import globaz.osiris.db.interet.tardif.CAInteretTardifFactory;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.db.journal.comptecourant.CAJoinCompteCourantOperation;
import globaz.osiris.db.journal.comptecourant.CAJoinCompteCourantOperationManager;
import globaz.osiris.db.journal.operation.CASumCompensationManager;
import globaz.osiris.db.journal.operation.CASumOperation;
import globaz.osiris.db.journal.operation.CASumOperationManager;
import globaz.osiris.db.journal.operation.CAUpdateOperationNotLikeE;
import globaz.osiris.db.journal.section.CASectionJournal;
import globaz.osiris.db.journal.section.CASectionJournalManager;
import globaz.osiris.db.print.CAListBulletinSoldeViewBean;
import globaz.osiris.external.IntJournalCG;
import globaz.osiris.external.IntRole;
import globaz.osiris.impl.helios.CAHeliosEcritureDouble;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;
import globaz.osiris.process.interetmanuel.CAProcessInteretMoratoireManuel;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import globaz.osiris.utils.CACachedManager;
import globaz.pyxis.api.osiris.TITiersAdministrationOSI;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Comptabilisation d'un journal.
 * 
 * @author dda
 * @see Fonctionnement : Voire javadoc de méthode public comptabiliser.
 */
public class CAComptabiliserJournal {
    private static final String LABEL_5009 = "5009";
    private static final String LABEL_5010 = "5010";

    private static final String LABEL_5023 = "5023";
    private static final String LABEL_5033 = "5033";
    private static final String LABEL_5035 = "5035";
    private static final String LABEL_5036 = "5036";
    private static final String LABEL_5126 = "5126";
    private static final String LABEL_5177 = "5177";
    private static final String LABEL_5191 = "5191";
    private static final String LABEL_5192 = "5192";
    private static final String LABEL_5193 = "5193";
    private static final String LABEL_5337 = "5337";
    private static final String LABEL_6109 = "6109";
    private static final String LABEL_6217 = "6217";
    private static final String LABEL_6218 = "6218";
    private static final String LABEL_7026 = "7026";
    private static final String LABEL_ACTIVATION_DES_OPERATIONS = "ACTIVATION_DES_OPERATIONS";
    private static final String LABEL_ACTIVATION_OPERATIONS_ERROR = "ACTIVATION_OPERATIONS_ERROR";
    private static final String LABEL_CODE_REFERENCE_NON_ATTRIBUE = "CODE_REFERENCE_NON_ATTRIBUE";
    private static final String LABEL_ECRITURE_SANS_CONTREPARTIE = "ECRITURE_SANS_CONTREPARTIE";
    private static final String LABEL_RUBRIQUE_NON_RESOLU = "5016";
    // Nombre d'erreurs max admise lors de la boucle interne d'activation
    private static final int MAX_ACTIVATION_ERRORS_ALLOWED = 100;
    // Pourcentage d'erreurs admise lors de la boucle interne d'activation
    private static final double MAX_ACTIVATION_ERRORS_ALLOWED_PERCENT = 0.1;

    /*
     * Some utils
     */
    private static String _join(Collection<String> col) {
        StringBuffer buf = new StringBuffer();
        for (Iterator<String> it = col.iterator(); it.hasNext();) {
            buf.append(it.next());
            if (it.hasNext()) {
                buf.append(",");
            }
        }
        return buf.toString();
    }

    FWIImportManager importManager = null;

    private APIRubrique rubriqueDeLissage = null;

    private CAUtilsJournal utils;

    /**
     * Constructeur de classe.
     */
    public CAComptabiliserJournal() {
        utils = new CAUtilsJournal();
    }

    /**
     * Crée l'écriture d'interet, l'active et l'ajoute dans un nouveau groupement.
     * 
     * @param transaction
     * @param interet
     * @return
     * @throws Exception
     */
    private CAEcriture addEcritureInteret(BProcess context, CAInteretMoratoire interet, CAPaiement pmt)
            throws Exception {

        // Créer un groupement afin que l'écriture soir supprimée si le journal est annulé.
        CAGroupement groupement = null;
        groupement = new CAGroupement();
        groupement.setSession(context.getSession());
        groupement.setTypeGroupement(CAGroupement.SERIE);
        groupement.add(context.getTransaction());
        if (context.getTransaction().hasErrors()) {
            context.getTransaction().addErrors(context.getSession().getLabel(CAComptabiliserJournal.LABEL_5035));
            return null;
        }

        CAEcriture ecriture = new CAEcriture();
        ecriture.setSession(context.getSession());
        ecriture.setIdJournal(pmt.getIdJournal());
        ecriture.setIdSection(pmt.getIdSection());
        ecriture.setIdCompteAnnexe(pmt.getIdCompteAnnexe());

        ecriture.setDate(pmt.getDate());
        ecriture.setMontant((new BigDecimal(pmt.getSection().getSolde())).abs().toString());
        ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
        ecriture.setIdCompte(interet.getIdRubrique());
        ecriture.activer(context.getTransaction());
        ecriture.add(context.getTransaction());

        // Ajoute l'écriture au groupement
        CAGroupementOperation groupementOperation = new CAGroupementOperation();
        groupementOperation.setSession(context.getSession());
        groupementOperation.setIdGroupement(groupement.getIdGroupement());
        groupementOperation.setIdOperation(ecriture.getIdOperation());
        groupementOperation.add(context.getTransaction());
        if (context.getTransaction().hasErrors()) {
            context.getTransaction().addErrors(context.getSession().getLabel(CAComptabiliserJournal.LABEL_5036));
        }

        return ecriture;
    }

    /**
     * Créer une écriture de lissage (débit ou crédit). Le montant sera ajouté en absolu.
     * 
     * @param context
     * @param transaction
     * @param journal
     * @param idSection
     * @param idGroupement
     * @param idCompteAnnexe
     * @param joinCompteCourant
     * @param montant
     * @param codeDebitCredit
     * @throws Exception
     */
    private void addEcritureLissage(BProcess context, BTransaction transaction, CAJournal journal, String idSection,
            String idGroupement, String idCompteAnnexe, CAJoinCompteCourantOperation joinCompteCourant,
            FWCurrency montant, String codeDebitCredit) throws Exception {

        CAEcriture ecritureLissage = new CAEcriture();
        ecritureLissage.setSession(journal.getSession());

        ecritureLissage.setDate(journal.getDateValeurCG());

        montant.abs();
        ecritureLissage.setMontant(montant.toString());

        ecritureLissage.setIdCompteCourant(joinCompteCourant.getIdCompteCourant());

        // Chargement de la rubrique de lissage
        if (rubriqueDeLissage == null) {
            CAReferenceRubrique ref = new CAReferenceRubrique();
            ref.setSession(journal.getSession());
            rubriqueDeLissage = ref.getRubriqueByCodeReference(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);
            if (rubriqueDeLissage == null) {
                transaction.addErrors(journal.getSession().getLabel(
                        CAComptabiliserJournal.LABEL_CODE_REFERENCE_NON_ATTRIBUE)
                        + APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);
            }
        }

        if (rubriqueDeLissage != null) {
            ecritureLissage.setIdCompte(rubriqueDeLissage.getIdRubrique());
        }

        ecritureLissage.setIdCompteAnnexe(idCompteAnnexe);
        ecritureLissage.setIdSection(idSection);

        ecritureLissage.setCodeDebitCredit(codeDebitCredit);

        ecritureLissage.setIdJournal(journal.getIdJournal());
        ecritureLissage.activer(transaction);
        ecritureLissage.add(transaction);

        CAGroupementOperation groupementOperation = new CAGroupementOperation();
        groupementOperation.setSession(journal.getSession());
        groupementOperation.setIdGroupement(idGroupement);
        groupementOperation.setIdOperation(ecritureLissage.getIdOperation());
        groupementOperation.add(transaction);
        if (transaction.hasErrors()) {
            transaction.addErrors(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5036));
        }
    }

    /**
     * Si l'interface de la comptabilité générale est active => Ajoute une écriture à la comptabilité général (HELIOS). <br/>
     * A commité ou rollbacké après l'appel de la méthode.
     * 
     * @param session
     * @param transaction
     * @param sumOperation
     * @param journal
     * @param journalCg
     * @return
     * @throws Exception
     */
    private String addHeliosEcriture(BSession session, BTransaction transaction, CASumOperation sumOperation,
            CAJournal journal, IntJournalCG journalCg) throws Exception {
        if (utils.isInterfaceCgActive(session)) {
            ICGEcritureDouble heliosEcriture = (ICGEcritureDouble) journalCg.getSessionHelios(transaction.getSession())
                    .getAPIFor(ICGEcritureDouble.class);

            heliosEcriture.setIdJournal(journalCg.getIdJournal());
            heliosEcriture.setIdExerciceComptable(journalCg.getIdExerciceComptable());
            heliosEcriture.setDate(journal.getDateValeurCG());
            heliosEcriture.setLibelle(journalCg.getLibelle());

            if (sumOperation.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)
                    || sumOperation.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_CREDIT)) {
                FWCurrency tmp = new FWCurrency(sumOperation.getMontantAbs());
                tmp.negate();
                heliosEcriture.setMontant(tmp.toString());
            } else {
                heliosEcriture.setMontant(sumOperation.getMontantAbs());
            }

            heliosEcriture.setPiece(sumOperation.getPiece());

            String numeroExterneContreEcriture;
            if (!JadeStringUtil.isIntegerEmpty(sumOperation.getNumCompteCG())) {
                numeroExterneContreEcriture = sumOperation.getNumCompteCG();
            } else {
                numeroExterneContreEcriture = sumOperation.getIdExterne();
            }

            CARubrique rubrique = new CARubrique();
            rubrique.setISession(session);
            rubrique.setIdRubrique(sumOperation.getIdCompte());

            rubrique.retrieve();
            if (rubrique.isNew()) {
                throw new Exception(session.getLabel(CAComptabiliserJournal.LABEL_RUBRIQUE_NON_RESOLU) + " [id="
                        + sumOperation.getIdCompte() + "]");
            }

            if (sumOperation.getCodeDebitCredit().equals(APIEcriture.DEBIT)
                    || sumOperation.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                heliosEcriture.setNumeroCompteDebite(numeroExterneContreEcriture);
                heliosEcriture.setNumeroCompteCredite(rubrique.getNumeroComptePourCG());

                if (!JadeStringUtil.isIntegerEmpty(sumOperation.getIdCaisseProfessionnelle())) {
                    BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                            CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(session, true);
                    heliosEcriture.setNumeroCentreChargeCredite(TITiersAdministrationOSI.getAdministrationNumero(
                            pyxisSession, sumOperation.getIdCaisseProfessionnelle()));
                }
            } else {
                heliosEcriture.setNumeroCompteCredite(numeroExterneContreEcriture);
                heliosEcriture.setNumeroCompteDebite(rubrique.getNumeroComptePourCG());

                if (!JadeStringUtil.isIntegerEmpty(sumOperation.getIdCaisseProfessionnelle())) {
                    BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                            CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(session, true);
                    heliosEcriture.setNumeroCentreChargeDebite(TITiersAdministrationOSI.getAdministrationNumero(
                            pyxisSession, sumOperation.getIdCaisseProfessionnelle()));
                }
            }

            heliosEcriture.add(transaction);

            heliosEcriture.setIdExerciceComptable(journalCg.getIdExerciceComptable());
            CAHeliosEcritureDouble ecritureDouble = new CAHeliosEcritureDouble(heliosEcriture);

            if (ecritureDouble.getCompteCG(transaction).isNew()
                    || ecritureDouble.getContreEcriture(transaction).isNew()) {
                throw new Exception(session.getLabel(CAComptabiliserJournal.LABEL_ECRITURE_SANS_CONTREPARTIE));
            }

            return ecritureDouble.getIdEcritureCollective();
        } else {
            return "";
        }
    }

    /**
     * Calcul des intérêts moratoires pour la comptabilité avs. <br/>
     * Traite les intérêts tardifs.
     * 
     * @param context
     * @param readTransaction
     * @param journal
     * @throws Exception
     */
    private void calculerInteretsMoratoires(BProcess context, BTransaction readTransaction, CAJournal journal)
            throws Exception {
        if ((journal.getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) < 0)
                && utils.isComptabiliteAvs(journal.getSession())) {
            BStatement statement = null;

            CASectionJournalManager mgr = new CASectionJournalManager();
            mgr.setSession(journal.getSession());
            mgr.setForIdJournal(journal.getIdJournal());
            try {
                context.setState(journal.getSession().getLabel("IM_CALCULER"));
                context.setProgressScaleValue(mgr.getCount(readTransaction));

                statement = mgr.cursorOpen(readTransaction);

                CASectionJournal sectionDuJournal = null;
                while (((sectionDuJournal = (CASectionJournal) mgr.cursorReadNext(statement)) != null)
                        && !context.isAborted()) {

                    calculerInteretTardif(context, journal, sectionDuJournal);

                    context.setProgressDescription("Journal:" + journal.getIdJournal() + " <br/>");
                    context.incProgressCounter();
                }

            } catch (Exception e) {
                context.getTransaction().addErrors(e.getMessage());
                context.getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            } finally {
                if (statement != null) {
                    mgr.cursorClose(statement);
                }

                statement = null;
            }
        }
    }

    /**
     * Calcule l'interet de l'OP et le comptabilise s'il correspond au solde de la section (interet de l'OP)
     * 
     * @param context
     * @param idJournal
     * @throws Exception
     */
    private void calculerInteretsOP(BProcess context, String idJournal) throws Exception {
        CAPaiementForCalculIMManager manager = new CAPaiementForCalculIMManager();
        manager.setSession(context.getSession());
        manager.setForIdJournal(idJournal);
        manager.find(BManager.SIZE_NOLIMIT);

        @SuppressWarnings("unchecked")
        Iterator<CAPaiement> it = manager.iterator();
        while (it.hasNext()) {
            CAPaiement ecrPmt = it.next();

            if (ecrPmt == null) {
                throw new Exception("Error calculerInteretsOP - ecrPmt not found !");
            }

            FWCurrency soldeSection = new FWCurrency(ecrPmt.getSection().getSolde());
            if (APIOperation.PROVPMT_SOLDEOP.equals(ecrPmt.getProvenancePmt())
                    || APIOperation.PROVPMT_SOLDEOF.equals(ecrPmt.getProvenancePmt())) {

                ArrayList<CAInteretManuelVisualComponent> liste = calculIMManuel(context, context.getSession(), ecrPmt,
                        false);
                if (soldeSection.isNegative() && !liste.isEmpty()) {
                    FWCurrency totalIM = interetTotalIM(liste);
                    BigDecimal diff = soldeSection.getBigDecimalValue().abs().subtract(totalIM.getBigDecimalValue());
                    BigDecimal taux = interetTauxEcart(totalIM, soldeSection);

                    if ((taux.abs().doubleValue() <= CAParametres.getTauxMargeDeltaInteretCalcule(context
                            .getTransaction()))
                            && (diff.abs().doubleValue() <= CAParametres.getMontantMaxDeltaInteretCalcule(context
                                    .getTransaction()))) {
                        interetImputer(context, liste, ecrPmt);
                    } else {
                        interetRejet(context, liste);
                    }
                } else {
                    interetRejet(context, liste);
                }
            } else if (!soldeSection.isPositive()) {
                // POAVS-223
                if (CAInteretTardif.isNouveauCalculPoursuite(context.getSession(), ecrPmt.getSection())) {
                    // Simuler IM
                    calculIMManuel(context, context.getSession(), ecrPmt, true);
                }
            }
        }
    }

    private void calculerInteretsFromCompensation(BProcess context, String idJournal) throws Exception {
        CACompensationForCalculIMManager manager = new CACompensationForCalculIMManager();
        manager.setSession(context.getSession());
        manager.setForIdJournal(idJournal);
        manager.find(BManager.SIZE_NOLIMIT);

        @SuppressWarnings("unchecked")
        Iterator<CAEcriture> it = manager.iterator();
        while (it.hasNext()) {
            CAEcriture ecr = it.next();

            if (ecr == null) {
                throw new Exception("Error calculerInteretsFromCompensation - ecr not found !");
            }

            FWCurrency soldeSection = new FWCurrency(ecr.getSection().getSolde());
            if (!soldeSection.isPositive()) {
                // POAVS-223
                if (CAInteretTardif.isNouveauCalculPoursuite(context.getSession(), ecr.getSection())) {
                    // Simuler IM
                    calculIMManuel(null, context.getSession(), ecr, true);
                }
            }
        }
    }

    /**
     * Calcule et ajoute les interets tardifs pour une section.
     * 
     * @param context
     * @param journal
     * @param sectionDuJournal
     * @throws Exception
     */
    private void calculerInteretTardif(BProcess context, CAJournal journal, CASectionJournal sectionDuJournal)
            throws Exception {
        CAInteretTardif interetTardif = CAInteretTardifFactory.getInteretTardif(sectionDuJournal.getCategorieSection());
        if (interetTardif == null) {
            return;
        }

        interetTardif.setIdSection(sectionDuJournal.getIdSection());

        if (!CAInteretTardif.isNouveauCalculPoursuite(context.getSession(),
                interetTardif.getSection(context.getSession(), context.getTransaction()))) {
            interetTardif.setIdJournal(journal.getIdJournal());
            interetTardif.calculer(context.getSession(), context.getTransaction());
        }
    }

    /**
     * Execute le process de calcul d'interet tardif manuel.
     * 
     * @param transaction
     * @return
     * @throws Exception
     */
    private ArrayList<CAInteretManuelVisualComponent> calculIMManuel(BProcess context, BSession session,
            CAEcriture ecr, boolean forceExempte) throws Exception {
        // Calcul IM
        CAProcessInteretMoratoireManuel process = new CAProcessInteretMoratoireManuel();
        process.setSession(session);
        process.setParent(context);
        process.setDateFin(ecr.getDate());
        process.setIdSection(ecr.getIdSection());
        process.setIdJournal(ecr.getIdJournal());
        process.setSimulationMode(false);
        process.setForceExempte(forceExempte);

        try {
            process.executeProcess();
            if (context == null && process.getMemoryLog().hasMessages()) {
                throw new Exception(process.getMemoryLog().getMessagesInString());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            throw new Exception("Error : lors du calcul d'interet manuel à l'activation de l'écriture. "
                    + ecr.getIdOperation() + " - " + e);
        }

        return process.getVisualComponents();
    }

    /**
     * Vérifie que la somme des compensations du journal soit à zéro
     * 
     * @param context
     * @param journal
     * @return false s'il y a une différence dans les compensation
     * @throws Exception
     */
    private boolean checkEcritureCompensation(BProcess context, String idJournal) throws Exception {
        CASumCompensationManager mg = new CASumCompensationManager();
        mg.setSession(context.getSession());
        mg.setForIdJournal(idJournal);
        BigDecimal sumCompensation = mg.getSum(CAOperation.FIELD_MONTANT);

        if (sumCompensation.signum() != 0) {
            context.getTransaction().addErrors(
                    context.getSession().getLabel("ERREUR_COMPTABILISER_JOURNAL_COMPENSATION_INEGALE") + " "
                            + sumCompensation);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Vérifier l'état. Comptabilisation impossible uniquement pour les journaux dont l'état est égal à ANNULE ou
     * COMPTABILISE.
     * 
     * @param context
     * @param journal
     * @return True : Si l'état du journal n'est pas COMPTABILISE ou ANNULE.
     */
    private boolean checkEtatJournal(BProcess context, CAJournal journal) {
        if (journal.getEtat().equals(CAJournal.ANNULE) || journal.getEtat().equals(CAJournal.COMPTABILISE)) {
            context.getTransaction().addErrors(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5009));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Comptabilise un journal. Quick how-to : <br>
     * 1. Itération des opérations pour validation (ordrer par état de ventilation et idsection) <br>
     * 2. Itération des opérations pour comptablisation <br>
     * 2.1 Mise à jour des opérations de type non-écriture <br>
     * 2.2 Group by sur les opérations de type écriture (et enfants) avec somme du montant pour les passées dans la
     * comptabilité générale (HELIOS) <br>
     * 2.3 Mise à jour (état + noEcritureCollective) des opérations de type écriture (et enfants) <br>
     * 2.4 Mise à jour des comptes courants touchés par l'itération de comptabilisation <br>
     * <br>
     * 3. Calcul des intérêts moratoires <br>
     * 4. Lisser sections auxiliaires <br>
     * <br>
     * 8. Mise à jour de l'état du journal et de l'état du journal de la comptabilité générale
     * 
     * @param context
     * @param journal
     * @param emailAddress L'adresse Email pour le process de la comptabilisation générale HELIOS.
     * @return
     */
    public boolean comptabiliser(BProcess context, CAJournal journal) {
        if (!checkEtatJournal(context, journal)) {
            return false;
        }

        BTransaction readTransaction = null;

        try {
            readTransaction = (BTransaction) journal.getSession().newTransaction();
            readTransaction.openTransaction();

            if (!checkEcritureCompensation(context, journal.getIdJournal())) {
                return false;
            }

            if (utils.isInterfaceCgActive(journal.getSession())
                    && !utils.isPeriodeComptableOuverte(journal.getSession(), readTransaction,
                            journal.getDateValeurCG())) {
                journal.getMemoryLog().logMessage(readTransaction.getErrors().toString(), FWMessage.FATAL,
                        this.getClass().getName());
                return false;
            }

            if (!setEtatJournalToTraitement(context, journal)) {
                return false;
            }

            // recherche du mode de traitement par défaut des bulletins neutres (inactif, ventilation, crédit)
            String modeTraitementBulletinNeutreParDefaut = CAApplication.getApplicationOsiris().getCAParametres()
                    .getModeParDefautBulletinNeutre();

            if (!processIterationForActivation(context, readTransaction, journal, modeTraitementBulletinNeutreParDefaut)
                    || context.isAborted()) {
                return false;
            }

            // Calcul des intérets sur les paiements
            calculerInteretsOP(context, journal.getIdJournal());

            // Calcul des intérets sur les compensations
            calculerInteretsFromCompensation(context, journal.getIdJournal());

            processIterationForLissage(context, readTransaction, journal);

            if (context.isAborted()) {
                return false;
            }

            IntJournalCG journalCg = null;
            // Si l'interface CG est active
            if (utils.isInterfaceCgActive(journal.getSession())) {
                journalCg = getOrCreateJournalCG(context.getTransaction(), journal);
                if (journalCg == null) {
                    return false;
                }
            }

            processIterationForComptabilisation(context, readTransaction, journal, journalCg);

            if (context.isAborted()) {
                return false;
            }
            calculerInteretsMoratoires(context, readTransaction, journal);

            if (context.isAborted()) {
                return false;
            }

            lisserSectionsAux(context.getTransaction(), journal);

            if (context.isAborted()) {
                return false;
            }

            setEtatJournalDoJournalCG(context, readTransaction, journal, journalCg);

            if (CAApplication.getApplicationOsiris().getCAParametres().isImpressionBulletinSoldeApresComptabilisation()) {
                imprimerBulletinSolde(context, readTransaction, journal);
            }

        } catch (Exception e) {
            context.getTransaction().addErrors(e.getMessage());
            context.getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, this.getClass().getName());
            JadeLogger.error(this, e);

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

                        // Ajout de plus de clarté dans les erreurs
                        JadeBusinessMessage[] messages = JadeThread
                                .logMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
                        StringBuilder message = new StringBuilder();
                        for (int i = 0; i < messages.length; i++) {
                            message.append(JadeI18n.getInstance().getMessage(journal.getSession().getIdLangueISO(),
                                    messages[i].getMessageId()));
                            if (i < messages.length - 1) {
                                message.append("\n");
                            }
                        }
                        context.getTransaction().addErrors(message.toString());
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

        // Vérification et modification si besoin du montant de la section si elle n'est pas en ligne avec les sommes de
        // ces opérations
        CAJournalProcessUtils.manageControleDesSoldesOperationACompteAnnexe(journal.getSession(),
                journal.getIdJournal());

        // Retourner vrai si le journal est comptabilisé
        return journal.getEtat().equals(CAJournal.COMPTABILISE);
    }

    /**
     * Lecture des opérations déjà comptabilisées lors d'une précédante comptabilisation non terminée/non réussi. Si le
     * total est égal à zéro ajoute un warning au memoryLog du journal.
     * 
     * @param readTransaction
     * @param journal
     * @return Long : Le nombre d'opérations comptabilisées.
     * @throws Exception
     */
    private long countOperationsAlreadyComptabilise(BTransaction readTransaction, CAJournal journal) throws Exception {
        CAOperationManager managerControl = new CAOperationManager();
        managerControl.setSession(journal.getSession());
        managerControl.setOrderBy(CAOperationManager.ORDER_IDOPERATION);
        managerControl.setForIdJournal(journal.getIdJournal());
        managerControl.setForEtat(APIOperation.ETAT_COMPTABILISE);

        long totalOK = managerControl.getCount(readTransaction);

        if (totalOK == 0) {
            journal.getMemoryLog().logMessage(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5177),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        return totalOK;
    }

    /**
     * Lecture des opérations dont l'état n'est pas comptabilisé malgré l'activation.
     * 
     * @param readTransaction
     * @param journal
     * @return True : Si aucune opérations en état ouvert ou en erreur n'a été trouvé.
     * @throws Exception
     */
    private boolean countOperationsNotComptabilise(BTransaction readTransaction, CAJournal journal) throws Exception {
        CAOperationManager managerControl = new CAOperationManager();
        managerControl.setSession(journal.getSession());
        managerControl.setOrderBy(CAOperationManager.ORDER_IDOPERATION);
        managerControl.setForIdJournal(journal.getIdJournal());

        ArrayList<String> etatNotIn = new ArrayList<String>();
        etatNotIn.add(APIOperation.ETAT_COMPTABILISE);
        etatNotIn.add(APIOperation.ETAT_INACTIF);
        managerControl.setForEtatNotIn(etatNotIn);

        return managerControl.getCount(readTransaction) > 0;
    }

    /**
     * Annulé si le journal est vide.
     * 
     * @param context
     * @param session
     * @param transaction
     * @param journalCg
     * @throws Exception
     */
    private void doJournalCGAnnulation(BProcess context, BSession session, BTransaction transaction,
            IntJournalCG journalCg) throws Exception {
        if (utils.isInterfaceCgActive(session)) {
            context.setState(session.getLabel(CAComptabiliserJournal.LABEL_6217));

            if (JadeStringUtil.isBlank(context.getEMailAddress())) {
                journalCg.annuler(context.getTransaction());
            } else {
                journalCg.annuler(context.getTransaction(), context.getEMailAddress());
            }

            // Vérifier les erreurs
            if (journalCg.isOnError()) {
                transaction.addErrors(session.getLabel(CAComptabiliserJournal.LABEL_5193));
            }
        }
    }

    /**
     * Comptabiliser le journal CG de manière définitive (=> journalCG partiel ou comptabilisé).
     * 
     * @param context
     * @param session
     * @param transaction
     * @param journalCg
     * @param print
     * @throws RemoteException
     * @throws Exception
     */
    private void doJournalCGComptabilisation(BProcess context, BSession session, BTransaction transaction,
            IntJournalCG journalCg, boolean print) throws RemoteException, Exception {
        if (utils.isInterfaceCgActive(session)) {
            context.setState(session.getLabel(CAComptabiliserJournal.LABEL_6217));

            if (JadeStringUtil.isBlank(context.getEMailAddress())) {
                journalCg.comptabiliser(transaction);
            } else {
                journalCg.comptabiliser(transaction, context.getEMailAddress());
            }

            // Vérifier les erreurs
            if (journalCg.isOnError()) {
                transaction.addErrors(session.getLabel(CAComptabiliserJournal.LABEL_5192));
            } else {
                if (print) {
                    // Imprimer le journal
                    context.setState(session.getLabel(CAComptabiliserJournal.LABEL_6218));
                    journalCg.imprimer();

                    if (journalCg.isOnError()) {
                        transaction.addErrors(session.getLabel(CAComptabiliserJournal.LABEL_5193));
                    }
                }
            }
        }
    }

    /**
     * Activation d'une opération.
     * 
     * @param transaction
     * @param journal
     * @param oper
     * @throws Exception
     */
    private void doOperationActivation(BSession session, BTransaction transaction, CAJournal journal, CAOperation oper)
            throws Exception {
        // Les activer
        oper.activer(transaction);

        // en cas d'erreur, on charge le log
        if (transaction.hasErrors()) {
            transaction.addErrors(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5023) + ": "
                    + oper.toString());
            // S'il y a des erreurs de logique, l'indiquer dans le journal
        } else {
            oper.update(transaction);

            if ((oper.getMemoryLog() != null) && (oper.getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0)) {
                journal.getMemoryLog().logMessage(
                        session.getLabel(CAComptabiliserJournal.LABEL_5010) + " " + oper.getIdOperation(),
                        oper.getMemoryLog().getErrorLevel(), this.getClass().getName());
                journal.getMemoryLog().logMessage(oper.getMemoryLog());
            }
        }
    }

    private void exportDocument(BProcess context, BTransaction readTransaction, CAJournal journal) throws Exception {
        if (importManager.size() > 0) {
            FWIExportManager exportManager = new FWIExportManager();
            exportManager.setExportFileName(CAListBulletinSoldeViewBean.LIST_BULLETIN_DE_SOLDE);
            exportManager.setExportApplicationRoot(CAApplication.DEFAULT_OSIRIS_ROOT);
            exportManager.deleteAll();
            exportManager.addAll(importManager.getList());
            exportManager.exportReport();

            context.registerAttachedDocument(exportManager.getExportNewFilePath());
        } else {
            context.getMemoryLog().logMessage(null,
                    journal.getSession().getLabel("BULLETIN_SOLDE_CPT_ANNEXE_NOT_FOUND"), FWMessage.AVERTISSEMENT,
                    this.getClass().getName());
        }
    }

    /**
     * Retrouve le journal de la comptabilité générale. <br>
     * Si aucun journalCG n'est attaché au journal alors le journalCG sera créé. <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param transaction
     * @param journal
     * @return IntJournalCg : Le journal de la compta général. Null si le chargement n'a pas fonctionné.
     * @throws RemoteException
     * @throws Exception
     */
    private IntJournalCG getOrCreateJournalCG(BTransaction transaction, CAJournal journal) throws RemoteException,
            Exception {
        // Récupérer le journal
        IntJournalCG journalCg = utils.getJournalCG(journal.getSession(), transaction, journal.getNoJournalCG());
        // Si le journal n'existe pas
        if (journalCg == null) {
            // Ouvrir un nouveau journal
            journalCg = (IntJournalCG) GlobazServer
                    .getCurrentSystem()
                    .getApplication(
                            utils.getCurrentApplication(journal.getSession()).getCAParametres().getApplicationExterne())
                    .getImplementationFor(journal.getSession(), IntJournalCG.class);
            GlobazValueObject journalVo = new GlobazValueObject(null);
            journalVo.setParameter(GlobazValueObject.PARAMETER_NEW, "true");
            journalVo.setParameter("application.name", utils.getCurrentApplication(journal.getSession()).getName());
            // Contrôler si le journal en compta auxiliaire dépasse 40 positions
            if (journal.getLibelle().length() >= 40) {
                journalVo.setProperty("libelle", journal.getLibelle().substring(0, 40));
            } else {
                journalVo.setProperty("libelle", journal.getLibelle());
            }
            journalVo.setProperty("date", journal.getDate());
            journalVo.setProperty("dateValeur", journal.getDateValeurCG());
            journalVo.setProperty("referenceExterne", journal.getIdJournal());

            journalVo.setProperty("estConfidentiel", journal.getEstConfidentiel());
            journalVo.setProperty("estPublic", new Boolean(true));

            journalCg = journalCg.addJournal(transaction, journalVo);

            if (journalCg.isOnError()) {
                transaction.addErrors(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5191));
            } else {
                // Mise à jour du numéro de journal fourni
                journal.setNoJournalCG(journalCg.getIdJournal());
            }

            journal.update(transaction);

            if (transaction.hasErrors()) {
                transaction.rollback();
                return null;
            } else {
                transaction.commit();
            }
        }
        return journalCg;
    }

    /**
     * Le journal a-t'il des intérêts moratoires MANUEL liés ?
     * 
     * @param readTransaction
     * @param journal
     * @return
     * @throws Exception
     */
    private boolean hasInteretMoratoireManuel(BTransaction readTransaction, CAJournal journal) throws Exception {
        CAInteretMoratoireManager managerInteret = new CAInteretMoratoireManager();
        managerInteret.setSession(journal.getSession());
        managerInteret.setForIdJournalCalcul(journal.getIdJournal());
        managerInteret.setForMotifCalcul(CAInteretMoratoire.CS_MANUEL);

        managerInteret.find(readTransaction);

        return !managerInteret.isEmpty();
    }

    /**
     * Impression des bulletins de soldes en fonction du journal concerné.
     * 
     * @param context
     * @param readTransaction
     * @param journal
     */
    private void imprimerBulletinSolde(BProcess context, BTransaction readTransaction, CAJournal journal) {
        CABulletinSoldeManager manager = new CABulletinSoldeManager();
        try {
            manager.setSession(journal.getSession());
            manager.setForIdJournal(journal.getIdJournal());
            manager.find();

            if (context.isAborted()) {
                return;
            }

            // String montantMinimeBulletinSolde =
            // CAParametres.getMontantMinimeBulletinSolde(context.getTransaction());
            BSession sessionMusca = new BSession("MUSCA");
            context.getSession().connectSession(sessionMusca);
            String montantMinimeNeg = sessionMusca.getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
            String montantMinimePos = sessionMusca.getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);

            if (!manager.isEmpty()) {
                importManager = new FWIImportManager();

                for (int i = 0; (i < manager.size()) && !context.isAborted(); i++) {
                    CABulletinSolde bulletin = (CABulletinSolde) manager.getEntity(i);
                    // mergePDF(docInfo, replace, nbDocsByFile,
                    // separateMultiSheets, sortByProperty)
                    CAImpressionBulletinsSoldes_Doc doc = new CAImpressionBulletinsSoldes_Doc();
                    doc.setIdSection(bulletin.getIdSection());
                    doc.setMontantMinimePos(montantMinimePos);
                    doc.setMontantMinimeNeg(montantMinimeNeg);
                    doc.setSession(journal.getSession());
                    doc.executeProcess();

                    importManager.addDocument(doc.getImporter().getDocument());
                }
                if (!context.isAborted()) {
                    exportDocument(context, readTransaction, journal);
                }
            }

        } catch (Exception e) {
            context.getTransaction().addErrors(e.getMessage());
            context.getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * Initialise le manager d'opérations pour la boucle d'activation des opérations.
     * 
     * @param transaction
     * @param journal
     * @param mgr
     * @return int : Le nombre d'opérations trouvées.
     */
    private int initOperationManagerForActivation(BTransaction transaction, CAJournal journal, CAOperationManager mgr)
            throws Exception {
        mgr.setSession(journal.getSession());

        // Récupérer les opérations associées au journal
        mgr.setOrderBy(CAOperationManager.ORDER_SECTION_IDOPERATION);
        mgr.setForIdJournal(journal.getIdJournal());
        mgr.setSortByEstVentileDesc(true);
        ArrayList<String> etatNotIn = new ArrayList<String>();
        etatNotIn.add(APIOperation.ETAT_COMPTABILISE);
        etatNotIn.add(APIOperation.ETAT_PROVISOIRE);
        etatNotIn.add(APIOperation.ETAT_INACTIF);
        mgr.setForEtatNotIn(etatNotIn);

        ArrayList<String> etatIn = new ArrayList<String>();
        etatIn.add(APIOperation.ETAT_OUVERT);
        etatIn.add(APIOperation.ETAT_ERREUR);
        mgr.setForEtatIn(etatIn);

        return mgr.getCount(transaction);
    }

    /**
     * Initialise le manager d'opérations pour la boucle de lissage des opérations provisoires.
     * 
     * @param transaction
     * @param journal
     * @param mgr
     * @return
     * @throws Exception
     */
    private int initOperationManagerForLissage(BTransaction transaction, CAJournal journal, CAOperationManager mgr)
            throws Exception {
        mgr.setSession(journal.getSession());

        // Récupérer les opérations associées au journal
        mgr.setOrderBy(CAOperationManager.ORDER_SECTION_IDOPERATION);
        mgr.setForIdJournal(journal.getIdJournal());
        mgr.setSortByEstVentileDesc(true);
        ArrayList<String> etatNotIn = new ArrayList<String>();
        etatNotIn.add(APIOperation.ETAT_COMPTABILISE);
        etatNotIn.add(APIOperation.ETAT_INACTIF);
        etatNotIn.add(APIOperation.ETAT_OUVERT);
        etatNotIn.add(APIOperation.ETAT_ERREUR);
        mgr.setForEtatNotIn(etatNotIn);

        ArrayList<String> etatIn = new ArrayList<String>();
        etatIn.add(APIOperation.ETAT_PROVISOIRE);
        mgr.setForEtatIn(etatIn);

        mgr.setLikeIdTypeOperation(APIOperation.CAECRITURE);

        return mgr.getCount(transaction);
    }

    /**
     * Comptabilise l'interet
     * 
     * @param transaction
     * @param liste
     * @throws Exception
     */
    private void interetImputer(BProcess context, ArrayList<CAInteretManuelVisualComponent> liste, CAPaiement pmt)
            throws Exception {
        Boolean avertissement = false;
        for (CAInteretManuelVisualComponent im : liste) {
            CAInteretMoratoire interet = im.getInteretMoratoire();
            try {
                interet.retrieve(context.getTransaction());
                if (liste.size() == 1 && !interet.isNouveauRegime()) {
                    interet.setMotifcalcul(CAInteretMoratoire.CS_SOUMIS);
                } else {
                    interet.setMotifcalcul(CAInteretMoratoire.CS_EXEMPTE);
                }
                interet.setRemarque(context.getSession().getLabel("INTERET_OP_REMARQUE_IMPUTER"));
                interet.setDateFacturation(JACalendar.todayJJsMMsAAAA());
                interet.setIdJournalFacturation(pmt.getIdJournal());
                // interet.setIdSectionFacture(pmt.getIdSection()); // Ne pas renseigner sinon, l'interet n'est plus
                // visible !
                interet.update(context.getTransaction());
            } catch (Exception e) {
                throw new Exception("interetImputer : Problème lors de la mise à jour de l'interet : " + e);
            }

            try {
                if (liste.size() == 1) {
                    addEcritureInteret(context, interet, pmt);
                } else {
                    avertissement = true;
                }
            } catch (Exception e) {
                throw new Exception("ImputerInteret : Problème lors de la création de l'interet : " + e);
            }
        }

        if (avertissement) {
            context.getMemoryLog().logMessage(context.getSession().getLabel("PLUSIEURS_PLANS_IM"),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

    }

    /**
     * Met à jour le calcul d'interet avec pour motif "A controler".
     * 
     * @param transaction
     * @param liste
     * @throws Exception
     */
    private void interetRejet(BProcess context, ArrayList<CAInteretManuelVisualComponent> liste) throws Exception {
        for (CAInteretManuelVisualComponent im : liste) {
            CAInteretMoratoire interet = im.getInteretMoratoire();
            try {
                interet.retrieve(context.getTransaction());
                interet.setMotifcalcul(CAInteretMoratoire.CS_EXEMPTE);
                interet.setRemarque(context.getSession().getLabel("INTERET_OP_REMARQUE_REJET"));
                interet.update(context.getTransaction());
            } catch (Exception e) {
                throw new Exception("interetRejet : Problème lors de la mise à jour de l'interet : " + e);
            }
        }
    }

    /**
     * @param totalIM
     * @param soldeSection
     * @return le taux correspondant à l'écart entre l'IM calculé et le solde de la section (IM OP)
     * @throws Exception
     */
    private BigDecimal interetTauxEcart(FWCurrency totalIM, FWCurrency soldeSection) throws Exception {
        if ((totalIM == null) || (soldeSection == null)) {
            throw new Exception("Error in CAComptabiliserJournal.interetTauxEcart() : parameter is null !");
        }
        if (!totalIM.isPositive()) {
            throw new Exception("Error in CAComptabiliserJournal.interetTauxEcart() : totalIM is not positiv !");
        }
        BigDecimal diff = soldeSection.getBigDecimalValue().abs().subtract(totalIM.getBigDecimalValue());
        BigDecimal taux = diff.multiply(new BigDecimal("100")).divide(totalIM.getBigDecimalValue(),
                RoundingMode.HALF_UP);
        return taux;
    }

    /**
     * @param liste
     * @return le montant total d'interet calculé.
     */
    private FWCurrency interetTotalIM(ArrayList<CAInteretManuelVisualComponent> liste) {
        FWCurrency total = new FWCurrency("0");
        for (CAInteretManuelVisualComponent im : liste) {
            total.add(new FWCurrency(im.montantInteretTotalCalcule()));
        }
        return total;
    }

    /**
     * Lissage des comptes courants de la section. <br>
     * But : Unifié les signes des soldes des secteurs compte courants. <br>
     * Exemple : Obtenir une section contenant des secteurs compte courant dont le solde est négatif ou zéro. <br>
     * <br>
     * Si la somme des comptes courants de la section (solde positif) est plus grand que la somme des comptes courants
     * de la section (solde négatif) => <br>
     * Des écritures de compensation seront générées pour mettre à zéro les soldes des comptes courants de la section
     * (solde négatif). <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param context
     * @param transaction
     * @param journal
     * @param idSection
     * @param idCompteAnnexe
     * @throws Exception
     */
    private void lisserCompteCourant(BProcess context, BTransaction transaction, CAJournal journal, String idSection,
            String idCompteAnnexe) throws Exception {
        CAGroupement groupement = null;

        // Recherche montant à ventiler
        CAJoinCompteCourantOperationManager manager = new CAJoinCompteCourantOperationManager();
        manager.setSession(journal.getSession());
        manager.setForIdSection(idSection);
        manager.setForSumNegative(true);
        manager.setForVentilationAccepter(true);
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        manager.find(context.getTransaction());

        for (int i = 0; i < manager.getSize(); i++) {
            CAJoinCompteCourantOperation compteCourantALisser = (CAJoinCompteCourantOperation) manager.get(i);

            FWCurrency soldeALisser = new FWCurrency(compteCourantALisser.getMontant());
            FWCurrency totalLisse = new FWCurrency();

            // Somme les écritures par compte courant positif
            CAJoinCompteCourantOperationManager managerCCSumBiggerThanZero = new CAJoinCompteCourantOperationManager();
            managerCCSumBiggerThanZero.setSession(journal.getSession());
            managerCCSumBiggerThanZero.setForIdSection(idSection);
            managerCCSumBiggerThanZero.setForSumPositive(true);
            managerCCSumBiggerThanZero.setForVentilationAccepter(true);
            managerCCSumBiggerThanZero.changeManagerSize(BManager.SIZE_NOLIMIT);
            managerCCSumBiggerThanZero.find(context.getTransaction());

            // totalLissable = l'addition de tout les soldes positifs des
            // comptes courants de la section
            FWCurrency totalLissable = new FWCurrency();
            for (int j = 0; j < managerCCSumBiggerThanZero.size(); j++) {
                CAJoinCompteCourantOperation compteCourantPositif = (CAJoinCompteCourantOperation) managerCCSumBiggerThanZero
                        .get(j);

                FWCurrency tmp = new FWCurrency(compteCourantPositif.getMontant());
                totalLissable.add(tmp);
            }

            for (int j = 0; j < managerCCSumBiggerThanZero.size(); j++) {
                CAJoinCompteCourantOperation compteCourantPositif = (CAJoinCompteCourantOperation) managerCCSumBiggerThanZero
                        .get(j);

                if (groupement == null) {
                    groupement = new CAGroupement();
                    groupement.setSession(journal.getSession());
                    groupement.setTypeGroupement(CAGroupement.SERIE);
                    groupement.add(transaction);
                    if (transaction.hasErrors()) {
                        transaction.addErrors(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5035));
                        return;
                    }
                }

                FWCurrency soldeCompteCourant = new FWCurrency(compteCourantPositif.getMontant());
                FWCurrency soldeALisserTest = soldeALisser;
                soldeALisserTest.abs();

                FWCurrency montantALisser;
                if (soldeALisserTest.compareTo(totalLissable) != -1) {
                    montantALisser = soldeCompteCourant;
                } else {
                    // Calculer le prorata à 5ct
                    float fMontant = (soldeALisser.floatValue() * soldeCompteCourant.floatValue())
                            / totalLissable.floatValue();
                    if (fMontant >= 0.03) {
                        montantALisser = new FWCurrency(JANumberFormatter.formatNoQuote(fMontant));
                    } else {
                        montantALisser = new FWCurrency(fMontant);
                    }
                }

                if (!montantALisser.isZero() && (totalLisse.compareTo(soldeALisser) == -1)) {
                    totalLisse.add(montantALisser);

                    float fResteAVentiler = soldeALisser.floatValue() - totalLisse.floatValue();
                    FWCurrency cur = new FWCurrency(fResteAVentiler);
                    cur.abs();

                    if (cur.doubleValue() >= 0.03) {
                        cur.round(0.05);
                    }

                    if (!montantALisser.isZero()) {
                        addEcritureLissage(context, transaction, journal, idSection, groupement.getIdGroupement(),
                                idCompteAnnexe, compteCourantPositif, montantALisser, APIEcriture.CREDIT);
                    }
                }
            }

            if (!totalLisse.isZero()) {
                addEcritureLissage(context, transaction, journal, idSection, groupement.getIdGroupement(),
                        idCompteAnnexe, compteCourantALisser, totalLisse, APIEcriture.DEBIT);

                // Vérifier les erreurs
                if (transaction.hasErrors()) {
                    transaction.rollback();
                    transaction.addErrors(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5033));
                    return;
                } else {
                    transaction.commit();
                }

            } else {
                if (groupement != null) {
                    transaction.rollback();
                }
            }
        }

    }

    /**
     * Si le journal contient des opérations auxiliaires générées en copie d'écritures => Il faut mettre à jour le solde
     * des sections auxiliaires.
     * 
     * @param transaction
     * @param journal
     * @throws Exception
     */
    private void lisserSectionsAux(BTransaction transaction, CAJournal journal) throws Exception {
        CAOperationManager opAuxManager = new CAOperationManager();
        opAuxManager.setSession(journal.getSession());
        opAuxManager.setForIdJournal(journal.getIdJournal());
        opAuxManager.setLikeIdTypeOperation(APIOperation.CAAUXILIAIRE);
        opAuxManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        opAuxManager.find(transaction);

        for (int i = 0; i < opAuxManager.size(); i++) {
            CAOperation auxiliaire = (CAOperation) opAuxManager.get(i);

            CASection sectionOpAuxiliaire = new CASection();
            sectionOpAuxiliaire.setSession(journal.getSession());
            sectionOpAuxiliaire.setIdSection(auxiliaire.getIdSection());

            sectionOpAuxiliaire.retrieve(transaction);

            if (sectionOpAuxiliaire.isNew()) {
                throw new Exception(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5126) + " "
                        + sectionOpAuxiliaire.getIdSection());
            }

            if (!JadeStringUtil.isIntegerEmpty(sectionOpAuxiliaire.getIdSectionPrincipal())) {
                CASection sectionPrincipal = new CASection();
                sectionPrincipal.setSession(journal.getSession());
                sectionPrincipal.setIdSection(sectionOpAuxiliaire.getIdSectionPrincipal());

                sectionPrincipal.retrieve(transaction);

                if (sectionPrincipal.isNew()) {
                    throw new Exception(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5126) + " "
                            + sectionPrincipal.getIdSection());
                }

                CASectionManager sectionManager = new CASectionManager();
                sectionManager.setSession(journal.getSession());
                sectionManager.setForIdSectionPrinc(sectionPrincipal.getIdSection());
                sectionManager.changeManagerSize(BManager.SIZE_NOLIMIT);
                sectionManager.find(transaction);

                for (int j = 0; j < sectionManager.size(); j++) {
                    CASection sectionAuxiliaire = (CASection) sectionManager.get(j);

                    if (sectionPrincipal.getSoldeToCurrency().compareTo(sectionAuxiliaire.getSoldeToCurrency()) == -1) {
                        if (IntRole.ROLE_ADMINISTRATEUR.equals(sectionAuxiliaire.getCompteAnnexe().getIdRole())) {
                            /*
                             * La section concerne une ARD. Dans ce cas: - S'il s'agit d'un paiement d'un des
                             * administrateurs, les soldes des sections des autres administrateurs ne changent que s'ils
                             * sont supérieurs au solde de la section principale. - Si l'on impute des frais à l'un des
                             * administrateurs, les soldes des sections des autres administrateur ne sont pas modifiées
                             * (les administrateurs sont solidairement responsables de la dette mais pas des frais). -
                             * Dans tous les cas, l'opération a été dupliquée sur la section auxilliaire, donc la
                             * section de l'administrateur qui a effectué le paiement ou à qui l'on impute des frais est
                             * à jour.
                             */
                            if (APIOperation.CAAUXILIAIRE_PAIEMENT.equals(auxiliaire.getIdTypeOperation())
                                    && (sectionAuxiliaire.getSoldeToCurrency().compareTo(
                                            sectionPrincipal.getSoldeToCurrency()) == 1)) {
                                sectionAuxiliaire.setSolde(sectionPrincipal.getSolde());
                            }
                        } else {
                            FWCurrency montant = sectionAuxiliaire.getSoldeToCurrency();
                            montant.sub(sectionPrincipal.getSoldeToCurrency());

                            sectionAuxiliaire.addAuxiliaire(transaction, sectionAuxiliaire.getIdCompteAnnexe(),
                                    journal, montant);
                        }
                    }
                }
            }
        }
    }

    /**
     * Boucle d'activation des opérations (tout types). <br>
     * Attention : Double boucle avec réinitialisation du manager (certaine opération génére d'autres opérations lors de
     * leur activation). <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param context
     * @param readTransaction
     * @param journal
     * @return True : Si toutes les opérations ont pu être activées.
     * @throws Exception
     */
    private boolean processIterationForActivation(BProcess context, BTransaction readTransaction, CAJournal journal,
            String modeTraitementBulletinNeutreParDefaut) throws Exception {
        // Instancier un nouveau manager
        CAOperationManager mgr = new CAOperationManager();
        int countTotal = initOperationManagerForActivation(readTransaction, journal, mgr);

        while ((countTotal > 0) && !context.isAborted()) {
            // Compter le nombre d'opération
            context.setState(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_ACTIVATION_DES_OPERATIONS));

            context.setProgressScaleValue(countTotal);

            // Contrôle des erreurs. Si suite à la boucle plus de
            // MAX_ACTIVATION_ERRORS_ALLOWED_PERCENT ou
            // MAX_ACTIVATION_ERRORS_ALLOWED d'opérations restent en état ERREUR
            // ou OUVERT le processus de comptabilisation s'arrêtera
            int controlErrors = 0;

            CAOperation operX = null;
            BStatement statement = mgr.cursorOpen(readTransaction);

            // Boucle d'activation
            while (((operX = (CAOperation) mgr.cursorReadNext(statement)) != null) && !context.isAborted()) {
                CAOperation oper = operX.getOperationFromType(readTransaction);

                // Si l'opération n'a pas été convertie
                if (oper == null) {
                    journal.getMemoryLog().logMessage(
                            journal.getSession().getLabel(CAUtilsJournal.LABEL_5013) + " " + operX.toString(),
                            FWViewBeanInterface.WARNING, this.getClass().getName());
                } else {
                    oper.setModeTraitementBulletinNeutreParDefaut(modeTraitementBulletinNeutreParDefaut);
                    testCompteForOperationEcriture(journal.getSession(), context.getTransaction(), journal, oper);

                    if (!context.getTransaction().hasErrors()) {
                        oper.setSession(journal.getSession());

                        doOperationActivation(journal.getSession(), context.getTransaction(), journal, oper);

                        if (context.getTransaction().hasErrors()) {
                            journal.getMemoryLog().logMessage(context.getTransaction().getErrors().toString(),
                                    FWMessage.ERREUR, this.getClass().getName());

                            context.getTransaction().rollback();
                            context.getTransaction().clearErrorBuffer();
                        } else {
                            context.getTransaction().commit();
                        }

                        if (oper.getEtat().equals(APIOperation.ETAT_OUVERT)
                                || oper.getEtat().equals(APIOperation.ETAT_ERREUR)) {
                            controlErrors++;
                        }
                    } else {
                        context.getTransaction().clearErrorBuffer();
                        controlErrors++;
                    }

                    if ((controlErrors >= CAComptabiliserJournal.MAX_ACTIVATION_ERRORS_ALLOWED)
                            || (((double) controlErrors / countTotal) >= (countTotal * CAComptabiliserJournal.MAX_ACTIVATION_ERRORS_ALLOWED_PERCENT))) {
                        journal.getMemoryLog()
                                .logMessage(
                                        journal.getSession().getLabel(
                                                CAComptabiliserJournal.LABEL_ACTIVATION_OPERATIONS_ERROR),
                                        FWMessage.ERREUR, this.getClass().getName());
                        return false;
                    }

                    // BZ 8534 - Si le montant de la section n'est pas soldé ET qu'il est autre qu'en mode standard ET
                    // qu'il contient un passage de facturation. Nous allons le reporter pour une prochaine facturation
                    // en lui enlevant l'id passage.
                    String sectionId = oper.getIdSectionCompensation();

                    if (!JadeStringUtil.isBlankOrZero(sectionId)) {
                        CASectionManager manager = new CASectionManager();

                        manager.setForIdSection(sectionId);
                        manager.setSession(readTransaction.getSession());
                        manager.find();

                        if (manager.size() == 1) {
                            CASection section = (CASection) manager.get(0);
                            FWCurrency solde = new FWCurrency(section.getSolde());

                            // Ceci afin de permettre de reporter à nouveau un montant pas totalement reporter ou
                            // compenser.
                            if ((solde.isZero() == false)
                                    && !APISection.MODE_COMPENSATION_STANDARD.equals(section.getIdModeCompensation())
                                    && !JadeStringUtil.isBlankOrZero(section.getIdPassageComp())) {

                                // On enlève le passage afin qu il soit repris dans une prochaine facture.
                                section.setIdPassageComp("0");
                                section.update(readTransaction);
                                readTransaction.commit();
                            }
                        }
                    }
                    // FIN BZ 8534
                }

                context.setProgressDescription("Journal:" + oper.getIdJournal() + " <br/>opération:"
                        + oper.getIdOperation() + "<br/>");
                context.incProgressCounter();
            }

            mgr.cursorClose(statement);

            if (controlErrors > 0) {
                journal.getMemoryLog().logMessage(
                        journal.getSession().getLabel(CAComptabiliserJournal.LABEL_ACTIVATION_OPERATIONS_ERROR),
                        FWMessage.ERREUR, this.getClass().getName());
                return false;
            }

            // Réinitialisation du manager des opérations
            // Exemple : L'opération de type paiement créé des opérations slave
            // non prisent en compte par la 1ère initialisation du manager
            mgr = new CAOperationManager();
            countTotal = initOperationManagerForActivation(readTransaction, journal, mgr);
        }

        return true;
    }

    /**
     * oca - EN COURS DE DEV pour optimization factu version 1-7
     * 
     * 
     * 
     * 
     * Boucle d'activation des opérations (tout types). <br>
     * Attention : Double boucle avec réinitialisation du manager (certaine opération génére d'autres opérations lors de
     * leur activation). <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param context
     * @param readTransaction
     * @param journal
     * @return True : Si toutes les opérations ont pu être activées.
     * @throws Exception
     */
    private boolean processIterationForActivation2(final BProcess context, final BTransaction transaction,
            final CAJournal journal) throws Exception {

        final int dateDelaiValeur = CAParametres.getDelaiDateValeur(transaction);

        LotExec<List<CAOperation>> executor = new JadeListUtil.LotExec<List<CAOperation>>() {
            @Override
            public void exec(List<List<CAOperation>> lot, long numLot, long nbLot) throws Exception {
                // Code de preparation pour le lot //
                /*
                 * Code commun pour ce lot
                 */
                Set<String> idSectionForLot = new HashSet<String>();
                Set<String> idCompteAnnexeForLot = new HashSet<String>();

                // Mise en cache :
                for (List<CAOperation> ops : lot) {
                    for (CAOperation op : ops) {
                        idSectionForLot.add(op.getIdSection());
                        idCompteAnnexeForLot.add(op.getIdCompteAnnexe());
                    }
                }
                CASectionManager sectionsForLot = new CASectionManager();
                sectionsForLot.setSession(transaction.getSession());
                sectionsForLot.setForIdSectionIn(idSectionForLot);
                sectionsForLot.find(0);
                CACachedManager<CASection> cacheSectionManager = new CACachedManager<CASection>(sectionsForLot);

                String forIdCompteAnnexeIn = CAComptabiliserJournal._join(idCompteAnnexeForLot);
                CACompteAnnexeManager compteAnnexesForLot = new CACompteAnnexeManager();
                compteAnnexesForLot.setSession(transaction.getSession());
                compteAnnexesForLot.setForIdCompteAnnexeIn(forIdCompteAnnexeIn);
                compteAnnexesForLot.find(0);
                CACachedManager<CACompteAnnexe> cacheCompteAnnexeManager = new CACachedManager<CACompteAnnexe>(
                        compteAnnexesForLot);

                System.out.println("Traitement du lot : " + numLot + "/" + nbLot);
                for (List<CAOperation> ops : lot) {
                    // Code pour chaque item du lot //
                    /*
                     * Code commun pour les opérations du compte annexe
                     */
                    // System.out.println(ops.size());
                    if (ops.size() > 0) {
                        CACompteAnnexe ca = cacheCompteAnnexeManager.findById(ops.get(0).getIdCompteAnnexe());
                        Set<String> idSectionForCompteAnnexe = new HashSet<String>();

                        // System.out.println(ca.getIdExterneRole() + " : SOLDE AVANT : " + ca.getSolde());
                        FWCurrency sumMontantsOperations = new FWCurrency(0);
                        Map<String, List<CAOperation>> opByIdSection = new HashMap<String, List<CAOperation>>();
                        for (CAOperation op : ops) {

                            idSectionForCompteAnnexe.add(op.getIdSection());
                            /*
                             * Pour chaque opération
                             */
                            CAComptabiliserJournal.this.testCompteForOperationEcriture(op.getSession(), transaction,
                                    journal, op);

                            op.setSection(cacheSectionManager.findById(op.getIdSection()));
                            op.setCompteAnnexe(cacheCompteAnnexeManager.findById(op.getIdCompteAnnexe()));
                            op.setJournal(journal);
                            if (op instanceof CAEcriture) {
                                ((CAEcriture) op)._setDateDelaiValeur(dateDelaiValeur);
                            }
                            // System.out.println("activation opération ---> " + op.getIdOperation() + " "
                            // + op.getCompte().getIdExterne() + " " + op.getMontant());
                            op.activerLite(transaction);
                            // System.out.println("update operation     ---> " + op.getIdOperation() + " "
                            // + op.getCompte().getIdExterne());
                            op.update(transaction);

                            if (opByIdSection.get(op.getIdSection()) == null) {
                                List<CAOperation> l = new ArrayList<CAOperation>();
                                l.add(op);
                                opByIdSection.put(op.getIdSection(), l);
                            } else {
                                opByIdSection.get(op.getIdSection()).add(op);
                            }
                            try {
                                if ((op.isInstanceOrSubClassOf(APIOperation.CAECRITURE))
                                        || (op.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE))) {
                                    sumMontantsOperations.add(op.getMontant());

                                }
                            } catch (Exception e) {
                                FWMessage msg = new FWMessage();

                                if (op.isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE)) {
                                    msg.setMessageId(CACompteAnnexe.LABEL_OPERATION_AUXILIAIRE_NON_INSEREE);
                                } else {
                                    msg.setMessageId("5131");
                                }

                                msg.setComplement(e.getMessage());
                                msg.setIdSource("CACompteAnnexe");
                                msg.setTypeMessage(FWMessage.ERREUR);

                                journal.getMemoryLog().logMessage(msg);
                            }

                        }

                        // System.out.println(ca.getIdExterneRole() + " : SOLDE APRES : " + ca.getSolde());
                        // System.out.println("MAJ Compte Annexe");

                        ca.retrieve();
                        FWCurrency soldeCompteAnnexe = new FWCurrency(ca.getSolde());
                        soldeCompteAnnexe.add(sumMontantsOperations);
                        ca.setSolde(soldeCompteAnnexe.toString());

                        ca.update(transaction);

                        for (String idSection : idSectionForCompteAnnexe) {
                            CASection section = cacheSectionManager.findById(idSection);
                            section.retrieve();
                            if (opByIdSection.get(section.getIdSection()) != null) {
                                for (CAOperation o : opByIdSection.get(section.getIdSection())) {
                                    section.addOperation(o);
                                }
                            }
                            section.update(transaction);
                        }
                    }
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                    // System.out.println("FIN COMPTE ANNEXE--------------------------------------------");
                }
                // System.out.println("FIN LOT ===============================================");
            }
        };

        String prevIdCompteAnnexe = null;
        List<CAOperation> operationForCompteAnnexe = new ArrayList<CAOperation>();

        CAOperationManager mgr = new CAOperationManager();
        mgr.setSession(transaction.getSession());

        // Récupérer les opérations associées au journal
        mgr.setForIdJournal(journal.getIdJournal());
        // mgr.setVueOperationCpteAnnexe("true");
        mgr.setSortByCompteAnnexeAndVentile(true);
        ArrayList<String> etatIn = new ArrayList<String>();
        etatIn.add(APIOperation.ETAT_OUVERT);
        etatIn.add(APIOperation.ETAT_ERREUR);
        mgr.setForEtatIn(etatIn);
        int countTotal = mgr.getCount();
        while ((countTotal > 0) && !transaction.hasErrors()) {

            int taille = Integer.parseInt((String) TISQL.querySingleField(
                    transaction.getSession(),
                    "count(DISTINCT IDCOMPTEANNEXE) as CPT",
                    "FROM " + TIToolBox.getCollection() + "CAOPERP   WHERE " + "IDJOURNAL=" + journal.getIdJournal()
                            + " AND ETAT IN (205001,205003)").get(0));

            int tailleLot = 200;
            System.out.println("Nbr compte annexe" + taille);
            JadeListUtil.Lot<List<CAOperation>> lot = new JadeListUtil.Lot<List<CAOperation>>(taille, tailleLot,
                    executor);

            CAOperation operX = null;
            BStatement statement = mgr.cursorOpen(transaction);

            // Boucle d'activation
            while (((operX = (CAOperation) mgr.cursorReadNext(statement)) != null)) {

                CAOperation oper = operX.getOperationFromType(transaction);

                if (oper == null) {
                    // FAIL...
                    System.out.println("Err : " + operX.getIdOperation());
                } else {
                    if ((prevIdCompteAnnexe != null) && !prevIdCompteAnnexe.equals(oper.getIdCompteAnnexe())) {
                        // TestTISQL._execute(operationForCompteAnnexe);
                        lot.add(operationForCompteAnnexe);
                        operationForCompteAnnexe = new ArrayList();
                    }
                    // pour taitement par compte annexe
                    operationForCompteAnnexe.add(oper);

                }
                prevIdCompteAnnexe = oper.getIdCompteAnnexe();
            }
            if (operationForCompteAnnexe.size() > 0) {
                // TestTISQL._execute(operationForCompteAnnexe);
                lot.add(operationForCompteAnnexe);
            }

            mgr = new CAOperationManager();
            mgr.setSession(transaction.getSession());
            mgr.setForIdJournal(journal.getIdJournal());
            mgr.setSortByCompteAnnexeAndVentile(true);
            mgr.setForEtatIn(etatIn);
            countTotal = mgr.getCount();

        }
        return true;

    }

    /**
     * Exécute la comptabilisation des opérations, passe les opérations en compta générale et mise à jour des comptes
     * courants. <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param context
     * @param readTransaction
     * @param journal
     * @param journalCg
     * @throws Exception
     */
    private void processIterationForComptabilisation(BProcess context, BTransaction readTransaction, CAJournal journal,
            IntJournalCG journalCg) throws Exception {
        updateOperationNotLikeEcriture(context, journal);

        CASumOperationManager sumOperationManager = new CASumOperationManager();
        sumOperationManager.setSession(journal.getSession());
        sumOperationManager.setForIdJournal(journal.getIdJournal());
        sumOperationManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        sumOperationManager.find(readTransaction);

        context.setState(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_6109));
        context.setProgressScaleValue(sumOperationManager.size());

        for (int i = 0; (i < sumOperationManager.size()) && !context.isAborted(); i++) {
            CASumOperation sumOperation = (CASumOperation) sumOperationManager.get(i);
            context.setProgressDescription("Journal:" + sumOperation.getIdJournal() + " <br/>" + " [IdCpte="
                    + sumOperation.getIdCompte() + ", IdCc=" + sumOperation.getIdCompteCourant() + "]" + "<br/>");

            try {
                // Les montants = 0 ne sont pas passés dans Helios mais sont
                // affectés à comptabilisés.
                if (!sumOperation.isMontantZero()) {
                    // Mise à jour des opérations en passant écriture dans
                    // Helios.
                    sumOperation.setNoEcritureDouble(addHeliosEcriture(journal.getSession(), context.getTransaction(),
                            sumOperation, journal, journalCg));
                }

                sumOperation.update(context.getTransaction());

                if (!sumOperation.isMontantZero()) {
                    utils.updateCompteCourantForComptabilisation(journal.getSession(), context.getTransaction(),
                            sumOperation.getIdCompteCourant(), sumOperation.getMontant());
                }

                if (!context.getTransaction().hasErrors() && !context.isAborted()) {
                    context.getTransaction().commit();
                } else {
                    journal.getMemoryLog().logMessage(
                            context.getTransaction().getErrors() + " [IdCpte=" + sumOperation.getIdCompte() + ", IdCc="
                                    + sumOperation.getIdCompteCourant() + "]", FWMessage.ERREUR,
                            this.getClass().getName());
                    context.getTransaction().rollback();
                    context.getTransaction().clearErrorBuffer();
                }
            } catch (Exception e) {
                // Si il y a des exceptions dans helios continué et mettre
                // journal en mode partiel
                JadeLogger.warn(this, e);
                journal.getMemoryLog().logMessage(
                        e.getMessage() + " [IdCpte=" + sumOperation.getIdCompte() + ", IdCc="
                                + sumOperation.getIdCompteCourant() + "]", FWMessage.ERREUR, this.getClass().getName());
                context.getTransaction().rollback();
                context.getTransaction().clearErrorBuffer();
            }

            context.incProgressCounter();
        }
    }

    /**
     * Boucle de lissage des opérations (provisoire). Cette boucle va créer si nécessaire des écritures (ouvertes) qui
     * devront être activées par la suite.
     * 
     * @param context
     * @param readTransaction
     * @param journal
     * @throws Exception
     */
    private void processIterationForLissage(BProcess context, BTransaction readTransaction, CAJournal journal)
            throws Exception {
        // Instancier un nouveau manager
        CAOperationManager mgr = new CAOperationManager();
        int countTotal = initOperationManagerForLissage(readTransaction, journal, mgr);

        BStatement statement = mgr.cursorOpen(readTransaction);

        // Initialisation des variables nécessaire à l'appel de la méthode de
        // lissage
        String lastSectionProcessed = "";
        String lastIdCompteAnnexeProcessed = "";
        int countProcessed = 0;

        CAOperation operation = null;
        while ((operation = (CAOperation) mgr.cursorReadNext(statement)) != null) {
            countProcessed++;

            // if nextSection != lastSection && oper.
            if (!JadeStringUtil.isBlank(lastSectionProcessed) && !lastSectionProcessed.equals(operation.getIdSection())) {
                lisserCompteCourant(context, context.getTransaction(), journal, lastSectionProcessed,
                        lastIdCompteAnnexeProcessed);
            }

            if (countProcessed == countTotal) {
                lisserCompteCourant(context, context.getTransaction(), journal, operation.getIdSection(),
                        operation.getIdCompteAnnexe());
            }

            lastSectionProcessed = operation.getIdSection();
            lastIdCompteAnnexeProcessed = operation.getIdCompteAnnexe();
        }

        mgr.cursorClose(statement);
    }

    /**
     * Mise à jour de l'état du journal (annulé, partiel, partiel ou comptabilisé). <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param context
     * @param readTransaction
     * @param journal
     * @param journalCg
     * @throws Exception
     * @throws RemoteException
     */
    private void setEtatJournalDoJournalCG(BProcess context, BTransaction readTransaction, CAJournal journal,
            IntJournalCG journalCg) throws Exception, RemoteException {
        if (!context.getTransaction().hasErrors()) {
            long totalOK = countOperationsAlreadyComptabilise(readTransaction, journal);

            if ((journal.getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0)
                    || countOperationsNotComptabilise(readTransaction, journal)) {
                if (totalOK > 0) {
                    journal.setEtat(CAJournal.PARTIEL);
                } else {
                    // XXX : Provisoire pour débugger les journaux en erreur des Rentes à la FER-CIAM
                    // context.getMemoryLog().logMessage(
                    // "Journal error in CAComptabiliserJournal.setEtatJournalDoJournalCG() - "
                    // + journal.getMemoryLog().getMessagesInString(), FWViewBeanInterface.WARNING,
                    // this.getClass().getName());

                    journal.setEtat(CAJournal.ERREUR);
                }
            } else {
                if ((totalOK > 0) || hasInteretMoratoireManuel(readTransaction, journal)
                        || CAJournal.TYPE_BULLETIN_NEUTRE.equals(journal.getTypeJournal())) {
                    doJournalCGComptabilisation(context, journal.getSession(), context.getTransaction(), journalCg,
                            true);
                    journal.setEtat(CAJournal.COMPTABILISE);
                } else {
                    doJournalCGAnnulation(context, journal.getSession(), context.getTransaction(), journalCg);
                    journal.setEtat(CAJournal.ANNULE);
                }
            }

            journal.update(context.getTransaction());

            if (context.getTransaction().hasErrors()) {
                context.getTransaction().rollback();

                journal.getMemoryLog().logStringBuffer(context.getTransaction().getErrors(),
                        journal.getClass().getName());
                journal.getMemoryLog().logMessage(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5337),
                        FWMessage.FATAL, this.getClass().getName());
            } else {
                context.getTransaction().commit();
            }
        }
    }

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
            journal.getMemoryLog().logMessage(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_5337),
                    FWMessage.FATAL, this.getClass().getName());
            return false;
        } else {
            context.getTransaction().commit();
            return true;
        }
    }

    /**
     * Si comptabilité avs et qu'il s'agit d'une écriture le compte ne doit pas être null.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param oper
     */
    private void testCompteForOperationEcriture(BSession session, BTransaction transaction, CAJournal journal,
            CAOperation oper) {
        if (utils.isComptabiliteAvs(session) && oper.isInstanceOrSubClassOf(APIOperation.CAECRITURE)) {
            CAEcriture ecr = (CAEcriture) oper;
            // Vérifier le compte
            if (ecr.getCompte() == null) {
                transaction.addErrors(journal.getSession().getLabel(CAComptabiliserJournal.LABEL_7026));
            }
        }
    }

    /**
     * Mise à jour des opérations de type non écriture. <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param context
     * @param journal
     * @throws Exception
     */
    private void updateOperationNotLikeEcriture(BProcess context, CAJournal journal) throws Exception {
        CAUpdateOperationNotLikeE updateOperation = new CAUpdateOperationNotLikeE();
        updateOperation.setForIdJournal(journal.getIdJournal());
        if (updateOperation.update(journal.getSession(), context.getTransaction())) {
            context.getTransaction().commit();
        } else {
            context.getTransaction().rollback();
        }
    }
}
