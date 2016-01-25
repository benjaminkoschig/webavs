/*
 * Créé le 16 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.util;

import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.operation.COOperationContentieuxManager;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.batch.COTransitionViewBean;
import globaz.aquila.service.taxes.COTaxe;
import globaz.globall.api.BIApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAAuxiliaire;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAGroupement;
import globaz.osiris.db.comptes.CAGroupementOperation;
import globaz.osiris.db.comptes.CAGroupementOperationManager;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationContentieuxAquila;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import globaz.osiris.translation.CACodeSystem;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COJournalAdapter {

    public static final String CS_JOURNAL = "5500001";
    public static final String CS_JOURNAL_JOURNALIER = "5500002";

    /**
     * Efface ou réinitialise l'idJournalFacturation et la dateFacturation
     * 
     * @param transaction
     * @param contentieux
     * @param historique
     * @param sessionOsiris
     * @throws Exception
     */
    private static void annulerCalculIM(BTransaction transaction, COContentieux contentieux, COHistorique historique,
            BSession sessionOsiris) throws Exception {
        CAInteretMoratoireManager mgim = new CAInteretMoratoireManager();
        mgim.setSession(sessionOsiris);
        mgim.setForIdJournalFacturation(historique.getIdJournal());
        mgim.setForIdSection(contentieux.getIdSection());
        mgim.find(transaction);

        Iterator<CAInteretMoratoire> it = mgim.iterator();
        while (it.hasNext()) {
            CAInteretMoratoire im = it.next();
            if (im.getIdJournalCalcul().equals(im.getIdJournalFacturation())) {
                im.delete(transaction);
            } else {
                im.setDateFacturation("");
                im.setIdJournalFacturation("");
                im.update(transaction);
            }
        }
    }

    /**
     * Annule les écritures comptables liées à la transition annulée.
     * <p>
     * Attention ! Le processus de comptabilisation de Osiris va mettre à jour la section et le compte annexe dans un
     * contexte transactionnel mal défini ! Tenter de modifier la section ou le compte annexe après l'appel de cette
     * méthode entraînera presque certainement un dead-lock !
     * </p>
     * 
     * @param session
     * @param transaction
     * @param contentieux
     *            le contentieux dont une étape est annulée
     * @param historique
     *            l'historique qui est annulé
     * @throws Exception
     */
    public static void annulerEcritures(BSession session, BTransaction transaction, COContentieux contentieux,
            COHistorique historique, boolean extourner) throws Exception {
        BSession sessionOsiris = COJournalAdapter.createSessionOsiris(session);

        if (COJournalAdapter.CS_JOURNAL.equals(historique.getTypeJournal())) {
            COOperationContentieuxManager operationManager = new COOperationContentieuxManager();
            operationManager.setForIdJournal(historique.getIdJournal());
            operationManager.setForIdSection(contentieux.getIdSection());
            operationManager.setForIdContrePartie(historique.getIdEtape());
            operationManager.setForEtat(APIOperation.ETAT_COMPTABILISE);
            operationManager.setSession(sessionOsiris);
            operationManager.find(BManager.SIZE_NOLIMIT);

            for (int idOperation = 0; idOperation < operationManager.size(); ++idOperation) {
                /*
                 * Bien que CAEcriture et CAContentieuxAquila etendent CAOperation, il est nécessaire de recharger
                 * l'opération car le manager retourne des instances de CAOperation et non des instances de CAEcriture
                 * ou CAContentieuxAquila castées en CAOperation... Aucunes des méthodes redéfinies dans ces deux
                 * classes ne seraient donc exécutées si on ne recharge pas la bonne instance.
                 */
                CAOperation operation = ((CAOperation) operationManager.get(idOperation))
                        .getOperationFromType(transaction);

                if (operation instanceof CAOperationContentieuxAquila) {
                    COJournalAdapter.effacerOperationContentieux((CAOperationContentieuxAquila) operation, transaction);
                } else {
                    if (extourner) {
                        COJournalAdapter.extourneOuEffaceEcriture(operation, sessionOsiris, transaction);
                    }
                }

                if (transaction.hasErrors()) {
                    // erreur d'annulation dans Osiris, on interomp l'annulation
                    return;
                }
            }
        } else {
            // retrouver les opérations du groupement
            CAGroupementOperationManager operationManager = new CAGroupementOperationManager();

            operationManager.setSession(sessionOsiris);
            operationManager.setForIdGroupement(historique.getIdGroupement());
            operationManager.find();

            for (int idOperation = 0; idOperation < operationManager.size(); ++idOperation) {
                CAGroupementOperation groupe = (CAGroupementOperation) operationManager.get(idOperation);

                /*
                 * Bien que CAEcriture et CAContentieuxAquila etendent CAOperation, il est nécessaire de recharger
                 * l'opération car le manager retourne des instances de CAOperation et non des instances de CAEcriture
                 * ou CAContentieuxAquila castées en CAOperation... Aucunes des méthodes redéfinies dans ces deux
                 * classes ne seraient donc exécutées si on ne recharge pas la bonne instance.
                 */
                CAOperation operation = groupe.getOperation().getOperationFromType(transaction);

                if (operation instanceof CAOperationContentieuxAquila) {
                    COJournalAdapter.effacerOperationContentieux((CAOperationContentieuxAquila) operation, transaction);
                } else {
                    if (extourner) {
                        COJournalAdapter.extourneOuEffaceEcriture(operation, sessionOsiris, transaction);
                    }
                }

                if (transaction.hasErrors()) {
                    // erreur d'annulation dans Osiris, on interomp l'annulation
                    return;
                }
            }
        }
        if (extourner) {
            COJournalAdapter.annulerCalculIM(transaction, contentieux, historique, sessionOsiris);
        }
    }

    /**
     * @param session
     * @return
     * @throws Exception
     */
    private static BSession createSessionOsiris(BSession session) throws Exception {
        BIApplication appOsiris = GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
        BSession remoteSession = (BSession) appOsiris.newSession();

        session.connectSession(remoteSession);

        return remoteSession;
    }

    /**
     * @param operationContentieuxAquila
     * @param transaction
     * @throws Exception
     */
    private static void effacerOperationContentieux(CAOperationContentieuxAquila operationContentieuxAquila,
            BTransaction transaction) throws Exception {
        operationContentieuxAquila.wantAnnulerEtapesContentieux(false);

        if (operationContentieuxAquila.getEstComptabilise().booleanValue()) {
            operationContentieuxAquila.desactiver(transaction);
        }

        operationContentieuxAquila.delete(transaction);
    }

    /**
     * @param operation
     * @param sessionOsiris
     * @param transaction
     * @throws Exception
     */
    private static void extourneOuEffaceEcriture(CAOperation operation, BSession sessionOsiris, BTransaction transaction)
            throws Exception {
        if (!operation.getEstComptabilise().booleanValue()) {
            /*
             * on extourne pas des opérations non comptablisées, on les efface mais le journal ne doit pas être en
             * traitement
             */
            if (!CAJournal.OUVERT.equals(operation.getJournal().getEtat())) {
                /*
                 * l'opération n'est pas comptabilisée mais le journal l'a été, il est désormais en erreur, en
                 * traitement ou comptabilisé, dans ce cas, on ne peut pas effacer l'opération car des écritures
                 * partielles ont pu être faites dans la comptabilité, il faut interompre le processus d'annulation et
                 * remonter une erreur vers l'utilisateur qu'il doit aller voir lui-même ce qu'il se passe
                 */
                transaction.addErrors(sessionOsiris.getLabel("AQUILA_ERREUR_ANNULATION_JOURNAL_TRAITEMENT"));

                return;
            }

            // désactiver l'opération si elle est active pour permettre de
            // l'effacer
            if (operation.getEstActive().booleanValue()) {
                operation.desactiver(transaction);
            }

            operation.delete(transaction);

            return;
        }

        // on extourne les opérations comptabilisées
        CAJournal journalExtourne = COJournalAdapter.fetchJournalJournalier(sessionOsiris, transaction);
        CAEcriture extourne = new CAEcriture();

        extourne.setSession(operation.getSession());
        extourne.setIdCompteAnnexe(operation.getIdCompteAnnexe());
        extourne.setIdSection(operation.getIdSection());
        extourne.setDate(JACalendar.todayJJsMMsAAAA());
        extourne.setIdJournal(journalExtourne.getIdJournal());
        extourne.setMontant(operation.getMontant());
        extourne.setCodeDebitCredit(APIEcriture.DEBIT.equals(operation.getCodeDebitCredit()) ? APIEcriture.CREDIT
                : APIEcriture.DEBIT);
        extourne.setIdCompte(operation.getIdCompte());
        extourne.setIdSectionAux(operation.getIdSectionAux());
        extourne.add(transaction);

        // Fin si Erreur
        if (extourne.isNew()) {
            throw new Exception(transaction.getSession().getLabel("OPERATION_IMPOSSIBLE_DE_CREER_OP_EXTOURNE"));
        }
    }

    /**
     * @param sessionOsiris
     * @param transaction
     * @return
     * @throws Exception
     */
    private static CAJournal fetchJournalJournalier(BSession sessionOsiris, BTransaction transaction) throws Exception {
        return CAJournal.fetchJournalJournalier(sessionOsiris, transaction, CAJournal.TYPE_JOURNALIER_CONTENTIEUX);
    }

    private CAGroupement groupement;

    private CAJournal journal;

    private boolean journalJournalier;

    private boolean previsionnel;

    /** session OSIRIS. */
    private BSession session;

    /**
     * Crée une nouvelle instance de la classe COJournalAdapter permettant de gérer un journal de comptabilité pour
     * plusieurs contentieux.
     * 
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public COJournalAdapter(BSession session) throws Exception {
        this.session = COJournalAdapter.createSessionOsiris(session);
        journalJournalier = false;
    }

    /**
     * Crée une nouvelle instance de la classe COJournalAdapter permettant de gérer un journal de comptabilité pour un
     * seul contentieux.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param contentieux
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public COJournalAdapter(BSession session, COContentieux contentieux) throws Exception {
        this.session = COJournalAdapter.createSessionOsiris(session);
        journalJournalier = true;
    }

    /**
     * Crée l'écriture
     * 
     * @param transaction
     * @param contentieux
     * @param montant
     * @param idRubrique
     * @throws Exception
     */
    private void addEcriture(BTransaction transaction, COContentieux contentieux, String montant, String idRubrique)
            throws Exception {
        this.addEcriture(transaction, contentieux, montant, idRubrique, "");
    }

    /**
     * @param transaction
     * @param contentieux
     * @param montant
     * @param idRubrique
     * @param libelle
     * @throws Exception
     */
    private void addEcriture(BTransaction transaction, COContentieux contentieux, String montant, String idRubrique,
            String libelle) throws Exception {
        CAOperation ecriture = createEcriture(contentieux.getCompteAnnexe(), contentieux.getSection());

        ecriture.setSession(session);
        ecriture.setIdJournal(journal.getIdJournal());

        if (ICOSequenceConstante.CS_SEQUENCE_ARD.equals(contentieux.getSequence().getLibSequence())) {
            /*
             * si la séquence est ARD, on crée les écritures comptables sur la section principale avec le compte annexe
             * de la société, Osiris se charge de créer des écritures mirroirs sur la sous section
             */
            ecriture.setIdSection(contentieux.getSection().getIdSectionPrincipal());
            ecriture.setIdSectionAux(contentieux.getIdSection());
            ecriture.setIdCompteAnnexe(contentieux.getIdCompteAnnexePrincipal());
        } else {
            // sinon on fait normal.
            ecriture.setIdSection(contentieux.getIdSection());
            ecriture.setIdCompteAnnexe(contentieux.getIdCompteAnnexe());
        }

        ecriture.setDate(journal.getDateValeurCG());
        ecriture.setMontant(montant);
        ecriture.setCodeDebitCredit(APIEcriture.DEBIT);
        ecriture.setIdCompte(idRubrique);
        ecriture.setLibelle(libelle);
        ecriture.add(transaction);

        // regrouper
        if (journalJournalier) {
            regrouper(ecriture, transaction);
        }

        // Fin si erreur
        if (ecriture.isNew()) {
            throw new Exception(transaction.getSession().getLabel("OPERATION_IMPOSSIBLE_IMPUTER") + " ["
                    + contentieux.getIdSection() + "-" + idRubrique + "]");
        }
    }

    /**
     * comptabilise le journal.
     * <p>
     * Note: un journal journalier n'a pas besoin d'etre comptabilisé.
     * </p>
     * 
     * @param transaction
     *            DOCUMENT ME!
     * @param parent
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void comptabiliser(BTransaction transaction, BProcess parent) throws Exception {
        if (!previsionnel && !journalJournalier) {
            new CAComptabiliserJournal().comptabiliser(parent, journal);
        }
    }

    /**
     * Crée une écriture comptable ou auxilliaire en fonction du genre de compte annexe.
     * 
     * @param compteAnnexe
     *            le compte annexe pour lequel créer l'opération
     * @return une {@link CAEcriture} ou {@link CAAuxiliaire}
     */
    private CAOperation createEcriture(CACompteAnnexe compteAnnexe, CASection section) {
        if (CACodeSystem.COMPTE_AUXILIAIRE.equals(compteAnnexe.getIdGenreCompte())
                && JadeStringUtil.isIntegerEmpty(section.getIdSectionPrincipal())) {
            return new CAAuxiliaire();
        } else {
            return new CAEcriture();
        }
    }

    /**
     * cree les ecriture pour la transition.
     * 
     * @param transaction
     *            DOCUMENT ME!
     * @param contentieux
     *            DOCUMENT ME!
     * @param transition
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void creerEcritures(BTransaction transaction, COContentieux contentieux, COTransition transition)
            throws Exception {
        if (!previsionnel) {
            // Création d'une opération événement contentieux
            CAOperationContentieuxAquila evt = new CAOperationContentieuxAquila();

            evt.setSession(session);
            evt.setIdCompteAnnexe(contentieux.getCompteAnnexe().getIdCompteAnnexe());
            evt.setIdSection(contentieux.getIdSection());
            evt.setDate(contentieux.getDateExecution());
            evt.setIdJournal(journal.getIdJournal());
            evt.setJournal(journal);

            // HACK: on stocke l'id de l'etape suivante dans l'operation
            evt.setIdContrepartie(transition.getIdEtapeSuivante());
            evt.add(transaction);

            // regrouper
            if (journalJournalier) {
                regrouper(evt, transaction);
            }

            // Fin si Erreur
            if (evt.isNew()) {
                throw new Exception(transaction.getSession().getLabel("OPERATION_IMPOSSIBLE_DE_CREER_OP_AQUILA"));
            }
        }
    }

    /**
     * crée un journal de comptabilité du type contentieux.
     * 
     * @param transaction
     *            DOCUMENT ME!
     * @param previsionnel
     *            DOCUMENT ME!
     * @param dateSurDocument
     *            DOCUMENT ME!
     * @param libelleJournal
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void creerJournal(BTransaction transaction, boolean previsionnel, String dateSurDocument,
            String libelleJournal) throws Exception {
        this.previsionnel = previsionnel;

        if (!previsionnel && (journal == null)) {
            if (journalJournalier) {
                // obtenir le journal journalier d'Osiris
                // (CAJournal.fetchJournalJournalier)
                journal = COJournalAdapter.fetchJournalJournalier(session, transaction);

                // créer un groupement
                groupement = new CAGroupement();
                groupement.setSession(session);
                groupement.setTypeGroupement(CAGroupement.SERIE);

                groupement.add(transaction);
            } else {
                journal = new CAJournal();
                journal.setTypeJournal(CAJournal.TYPE_CONTENTIEUX);
                journal.setSession(session);
                journal.setDateValeurCG(dateSurDocument);
                journal.setLibelle(libelleJournal);
                journal.add(transaction);
            }

            // Si erreur
            if (journal.isNew()) {
                throw new Exception(transaction.getSession().getLabel("JOURNAL_IMPOSSIBLE_CREER_JOURNAL"));
            }
        }
    }

    /**
     * Return le journal de compta. aux. Utile pour tester les erreurs après comptabilisation voire
     * COProcessContentieux.
     * 
     * @return le journal
     */
    public CAJournal getJournal() {
        return journal;
    }

    /**
     * Impute les frais (les inscrits dans le journal).
     * 
     * @param transaction
     * @param viewBean
     * @param contentieux
     * @throws Exception
     */
    public void imputerFraisVariables(BTransaction transaction, COTransitionViewBean viewBean, COContentieux contentieux)
            throws Exception {
        if (!previsionnel && (viewBean != null)) {
            if (!viewBean.getFraisEtInterets().isEmpty()) {
                for (int i = 0; i < viewBean.getFraisEtInterets().size(); i++) {
                    this.addEcriture(transaction, contentieux, viewBean.getFraisEtInteretsMontant(i),
                            viewBean.getFraisEtInteretsIdRubrique(i), viewBean.getFraisEtInteretsLibelle(i));
                }
            }
        }
    }

    /**
     * Cérer les écritures d'IM
     * 
     * @param transaction
     * @param contentieux
     * @param interet
     * @throws Exception
     */
    public void imputerInteretManuel(BTransaction transaction, COContentieux contentieux,
            List<CAInteretManuelVisualComponent> interets) throws Exception {
        if ((interets != null) && !interets.isEmpty()) {
            for (CAInteretManuelVisualComponent im : interets) {
                this.addEcriture(transaction, contentieux, im.montantInteretTotalCalcule(), im.getInteretMoratoire()
                        .getIdRubrique());
            }
        }
    }

    /**
     * impute les taxes (les inscrits dans le journal).
     * 
     * @param transaction
     * @param contentieux
     * @param taxes
     * @throws Exception
     */
    public void imputerTaxes(BTransaction transaction, COContentieux contentieux, List<COTaxe> taxes) throws Exception {
        if (!previsionnel && (taxes != null) && !taxes.isEmpty()) {
            for (COTaxe taxe : taxes) {
                if (taxe.isImputerTaxe() && !taxe.getMontantTaxeToCurrency().isZero()) {
                    this.addEcriture(transaction, contentieux, taxe.getMontantTaxe(), taxe.loadCalculTaxe(session)
                            .getIdRubrique());
                }
            }
        }
    }

    /**
     * @return DOCUMENT ME!
     */
    public boolean isPrevisionnel() {
        return previsionnel;
    }

    private void regrouper(CAOperation operation, BTransaction transaction) throws Exception {
        CAGroupementOperation grpOperation = new CAGroupementOperation();

        grpOperation.setSession(session);
        grpOperation.setIdGroupement(groupement.getIdGroupement());
        grpOperation.setIdOperation(operation.getIdOperation());
        grpOperation.add(transaction);
    }

    /**
     * insére dans l'historique les informations relatives au journal de comptabilité.
     * 
     * @param historique
     *            l'historique à modifier
     * @return l'historique modifié
     */
    public COHistorique renseignerInfosJournal(COHistorique historique) {
        if (journal != null) {
            historique.setIdJournal(journal.getIdJournal());
        }

        if (journalJournalier) {
            historique.setTypeJournal(COJournalAdapter.CS_JOURNAL_JOURNALIER);

            if (groupement != null) {
                historique.setIdGroupement(groupement.getIdGroupement());
            }
        } else {
            historique.setTypeJournal(COJournalAdapter.CS_JOURNAL);
        }

        return historique;
    }
}