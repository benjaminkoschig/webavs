package globaz.aquila.process.batch;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.process.COContentieuxInfoManager;
import globaz.aquila.db.process.COProcessContentieuxInfo;
import globaz.aquila.process.batch.utils.COImprimerJournalContentieuxExcelml;
import globaz.aquila.process.batch.utils.COImprimerListPourOP;
import globaz.aquila.process.batch.utils.COImprimerListeDeclenchement;
import globaz.aquila.process.batch.utils.COJournalAdapterBatch;
import globaz.aquila.process.batch.utils.COLogMessageUtil;
import globaz.aquila.process.batch.utils.COProcessContentieuxUtils;
import globaz.aquila.process.batch.utils.COTransitionUtil;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.COTaxe;
import globaz.aquila.vb.process.COSelection;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class COAmorcerContentieux {

    private String beforeNoAffilie;
    private String dateDelaiPaiement;
    private String dateReference;
    private String dateSurDocument;
    private String forIdCategorie;
    private String forIdGenreCompte;
    private String fromNoAffilie;

    private boolean imprimerDocument = false;

    private boolean imprimerJournalContentieuxExcelml = false;

    private boolean imprimerListeDeclenchement = false;
    private boolean imprimerListePourOP = false;
    private COJournalAdapterBatch journalAdapterBatch;

    private COImprimerJournalContentieuxExcelml journalContentieuxExcelml;

    private COImprimerListeDeclenchement listeDeclenchement;

    private COImprimerListPourOP listePourOP;
    private boolean previsionnel;

    private List<String> roles;
    private Map<String, COSelection> selections;

    private String selectionTriListeCA;

    private String selectionTriListeSection;
    private List<COTaxe> taxes;

    private List<String> typesSections;

    /**
     * crée les amorces de contentieux.
     * <p>
     * sélectionne les sections candidates au contentieux et crée des contentieux pour elles.
     * </p>
     * 
     * @param parent
     *            process
     * @param session
     * @param transaction
     * @throws Exception
     */
    public void amorcerContentieux(BProcess parent, BSession session, BTransaction transaction) throws Exception {
        String currentUserName = session.getUserName();
        parent.setState(session.getLabel("AQUILA_AMORCE_CONTENTIEUX"));

        LinkedList<String> seqAAmorcer = getSequencesSelectionnees(session);

        if (!seqAAmorcer.isEmpty()) {
            COContentieuxInfoManager ctxInfoManager = initContentieuxManager(session, seqAAmorcer);
            BTransaction readTransaction = null;

            HashMap<String, COTransitionUtil> transitionUtilCache = new HashMap<String, COTransitionUtil>();

            try {
                readTransaction = (BTransaction) (session).newTransaction();
                readTransaction.openTransaction();

                parent.setProgressScaleValue(ctxInfoManager.getCount(readTransaction));

                // itérer sur tous les candidats au contentieux et créer des
                // contentieux correspondants
                BStatement statement = ctxInfoManager.cursorOpen(readTransaction);
                COProcessContentieuxInfo ctxInfo;
                boolean candidats = false;
                while (((ctxInfo = (COProcessContentieuxInfo) ctxInfoManager.cursorReadNext(statement)) != null)
                        && !parent.isAborted()) {
                    parent.setProgressDescription(ctxInfo.getIdExterneRoleCA() + "<br/>"
                            + ctxInfo.getIdExterneSection() + "<br/>");
                    parent.incProgressCounter();

                    // On a un candidat pour le contentieux => Utilisé pour
                    // message dans email Aucun contentieux à traiter
                    candidats = true;

                    if (!transitionUtilCache.containsKey(ctxInfo.getIdSequenceContentieux())) {
                        transitionUtilCache.put(ctxInfo.getIdSequenceContentieux(), new COTransitionUtil(session,
                                transaction, ctxInfo.getIdSequenceContentieux()));
                    }
                    createAndValidateContentieux(parent, session, transaction, ctxInfo,
                            transitionUtilCache.get(ctxInfo.getIdSequenceContentieux()), currentUserName);
                }

                // Si on a aucun candidat au contentieux, on ajoute un message
                // dans le mail pour avertir.
                if (!candidats) {
                    parent.getMemoryLog().logMessage(session.getLabel("AQUILA_PAS_DE_CONTENTIEUX_A_CREER"),
                            FWMessage.AVERTISSEMENT, this.getClass().getName());
                }
            } catch (Exception e) {
                parent.getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());

                if (readTransaction != null) {
                    readTransaction.addErrors(e.getMessage());
                }

                throw e;
            } finally {
                if (readTransaction != null) {
                    try {
                        readTransaction.rollback();
                    } finally {
                        readTransaction.closeTransaction();
                    }
                }
            }
        }
    }

    /**
     * Ajoute le cas au contentieux et dans le journal y relatif. <br/>
     * Confirme, en mode non prévisionnel, le cas par un commit sur la transaction. <br/>
     * Si non rollback et clear errors car commit par cas (ligne).
     * 
     * @param parent
     * @param session
     * @param transaction
     * @param ctxInfo
     * @throws Exception
     */
    private void createAndValidateContentieux(BProcess parent, BSession session, BTransaction transaction,
            COProcessContentieuxInfo ctxInfo, COTransitionUtil transitionUtil, String userName) throws Exception {
        try {
            // créer l'opération de création dans le journal
            COTransition transition = transitionUtil.getTransitionForCreerCtx();
            COContentieux contentieux = createContentieux(session, transition, ctxInfo, userName);

            if (!isPrevisionnel()) {
                contentieux.add(transaction);

                getJournalAdapterBatch().getJournal(session, transaction).creerEcritures(transaction, contentieux,
                        transition);

                if (transaction.hasErrors()) {
                    parent.getMemoryLog()
                            .logMessage(
                                    COLogMessageUtil.formatMessage(
                                            new StringBuffer(session
                                                    .getLabel("AQUILA_PROCESS_CREER_CONTENTIEUX_ERREUR_CREER")),
                                            new Object[] { ctxInfo.getDateSection(), ctxInfo.getIdExterneRoleCA(),
                                                    transaction.getErrors().toString() }), FWMessage.AVERTISSEMENT,
                                    this.getClass().getName());

                    transaction.rollback();
                    transaction.clearErrorBuffer();
                } else {
                    transaction.commit();
                }
            }

            enregistrerCreation(session, contentieux, ctxInfo, transition);

            if (isPrevisionnel() && !COProcessContentieuxUtils.isOnlyCreerContentieux(session, getSelections())) {
                simulerTransitions(parent, session, transaction, contentieux);
            }

        } catch (COTransitionException e) {
            if ((e.getMessageId() != null) && !e.getMessageId().equals("SEUIL_MINIMAL_INFERIEUR")
                    && !e.getMessageId().equals("AQUILA_ERR_CONTENTIEUX_SUSPENDU")) {
                parent.getMemoryLog().logMessage(
                        COLogMessageUtil.formatMessage(
                                new StringBuffer(session.getLabel("AQUILA_PROCESS_CREER_CONTENTIEUX_ERREUR_CREER")),
                                new Object[] { ctxInfo.getSection().getIdExterne(),
                                        ctxInfo.getCompteAnnexe().getIdExterneRole(), e.getMessage() }),
                        FWMessage.AVERTISSEMENT, this.getClass().getName());
            }

            transaction.rollback();
            transaction.clearErrorBuffer();
        } catch (Exception e) {
            parent.getMemoryLog().logMessage(
                    COLogMessageUtil.formatMessage(
                            new StringBuffer(session.getLabel("AQUILA_PROCESS_CREER_CONTENTIEUX_ERREUR_CREER")),
                            new Object[] { ctxInfo.getSection().getIdExterne(),
                                    ctxInfo.getCompteAnnexe().getIdExterneRole(), e.toString() }),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());

            transaction.rollback();
            transaction.clearErrorBuffer();
        }
    }

    /**
     * Création du contentieux. Validation dans la méthode appelante.
     * 
     * @param session
     * @param transition
     * @param ctxInfo
     * @param sequence
     * @return
     * @throws Exception
     * @throws COTransitionException
     */
    private COContentieux createContentieux(BSession session, COTransition transition,
            COProcessContentieuxInfo ctxInfo, String userName) throws Exception, COTransitionException {
        COContentieux contentieux = new COContentieux();
        contentieux.setSession(session);

        // TODO sel : 31 oct. 2008 getSoldeInitial
        contentieux.setMontantInitial(ctxInfo.getSolde());

        contentieux.setIdCompteAnnexe(ctxInfo.getIdCompteAnnexe());
        contentieux.setIdSection(ctxInfo.getIdSection());
        contentieux.setIdSequence(ctxInfo.getIdSequenceContentieux());

        contentieux.setDateExecution(getDateSurDocument());
        contentieux.setDateOuverture(getDateSurDocument());
        contentieux.setDateDeclenchement(ctxInfo.getDateEcheance());

        // Charge le compte annexe pour pouvoir calculer la date de prochain
        // declenchement
        contentieux.setCompteAnnexe(ctxInfo.getCompteAnnexe());

        // Charge la section pour pouvoir calculer la date de prochain
        // declenchement
        contentieux.setSection(ctxInfo.getSection());

        contentieux.setProchaineDateDeclenchement(transition.calculerDateProchainDeclenchement(contentieux));
        contentieux.setIdEtape(transition.getIdEtapeSuivante());
        contentieux.setUser(userName);

        return contentieux;
    }

    /**
     * Ajoute le cas à la liste des cas traités.
     * 
     * @param session
     * @param contentieux
     * @param ctxInfo
     * @param transition
     * @throws Exception
     */
    private void enregistrerCreation(BSession session, COContentieux contentieux, COProcessContentieuxInfo ctxInfo,
            COTransition transition) throws Exception {
        if (isImprimerListeDeclenchement()) {
            listeDeclenchement.insertRowCasAmorce(session, contentieux, ctxInfo, transition);
        }

        if (isImprimerJournalContentieuxExcelml()) {
            journalContentieuxExcelml.addRowInExcelml(session, contentieux, transition.getEtape());
        }
    }

    /**
     * Enregistre le passage a l'étape suivante dans les documents de liste, doit etre appele APRES que la transition
     * est effectuee.
     * 
     * @param session
     * @param contentieux
     * @param transition
     * @throws Exception
     */
    private void enregistrerTransitionPourImpression(BSession session, COContentieux contentieux,
            COTransition transition, String messageId) throws Exception {
        FWCurrency totalTaxeListe = new FWCurrency(0);
        FWCurrency totalFraisListe = new FWCurrency(0);
        if ((taxes != null) && !taxes.isEmpty()) {
            for (Iterator<COTaxe> iterTaxes = taxes.iterator(); iterTaxes.hasNext();) {
                COTaxe frais = iterTaxes.next();
                totalTaxeListe.add(frais.getMontantTaxeToCurrency());
                // TODO sel : 2 retrieve
                if (frais.loadCalculTaxe(session).isFraisPoursuite()) {
                    // Frais pour la liste par offices.
                    totalFraisListe = frais.getMontantTaxeToCurrency();
                }
            }
        }

        if (isImprimerListeDeclenchement()) {
            listeDeclenchement.insertRowTransitionEffectue(session, contentieux, transition, totalTaxeListe, messageId);
        }

        if (isImprimerListePourOP()) {
            listePourOP.insertRowTransitionEffectue(session, contentieux, transition, totalTaxeListe, totalFraisListe);
        }

        if (isImprimerJournalContentieuxExcelml()) {
            journalContentieuxExcelml.addRowInExcelml(session, contentieux, transition.getEtapeSuivante());
        }
    }

    public String getBeforeNoAffilie() {
        return beforeNoAffilie;
    }

    public String getDateDelaiPaiement() {
        return dateDelaiPaiement;
    }

    public String getDateReference() {
        return dateReference;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    public COJournalAdapterBatch getJournalAdapterBatch() {
        return journalAdapterBatch;
    }

    public COImprimerJournalContentieuxExcelml getJournalContentieuxExcelml() {
        return journalContentieuxExcelml;
    }

    public COImprimerListeDeclenchement getListeDeclenchement() {
        return listeDeclenchement;
    }

    public COImprimerListPourOP getListePourOP() {
        return listePourOP;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Map<String, COSelection> getSelections() {
        return selections;
    }

    public String getSelectionTriListeCA() {
        return selectionTriListeCA;
    }

    public String getSelectionTriListeSection() {
        return selectionTriListeSection;
    }

    /**
     * Retrouver les séquences pour lesquelles créer les contentieux. Séquence (AVS, ARD,...) dont l'étape
     * "Créer contentieux" a été choisie.
     * 
     * @param session
     * @return
     * @throws Exception
     */
    private LinkedList<String> getSequencesSelectionnees(BSession session) throws Exception {
        LinkedList<String> seqAAmorcer = new LinkedList<String>();

        for (Iterator<COSelection> selectionIter = selections.values().iterator(); selectionIter.hasNext();) {
            COSelection selection = selectionIter.next();

            // Détermine si l'étape Créer contentieux est selectionnée
            if (selection.isCreerAmorcesContentieux(session)) {
                seqAAmorcer.add(selection.getIdSequence());
            }
        }

        return seqAAmorcer;
    }

    /**
     * @return
     */
    public List<String> getTypesSections() {
        return typesSections;
    }

    /**
     * Charger la liste des sections candidates à la création d'un contentieux
     * 
     * @param session
     * @param seqAAmorcer
     * @return
     */
    private COContentieuxInfoManager initContentieuxManager(BSession session, LinkedList<String> seqAAmorcer) {
        COContentieuxInfoManager ctxInfoMgr = new COContentieuxInfoManager();

        // ctxInfoMgr.setForContentieuxNonBloqueADAte(getDateReference());
        ctxInfoMgr.setForDelaiEchuADate(getDateReference());
        ctxInfoMgr.setForIdGenreCompte(getForIdGenreCompte());
        ctxInfoMgr.setForIdsRoles(getRoles());
        ctxInfoMgr.setForIdsSequencesAquila(seqAAmorcer);

        if (JadeStringUtil.contains(getTypesSections().toString(), APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE)
                && (seqAAmorcer.contains("4"))) {
            ctxInfoMgr.setForSoldePositif(null);
            ctxInfoMgr.setForSoldeSection("0");
            ctxInfoMgr.setForBulletinNeutre(true);
        } else {
            ctxInfoMgr.setForSoldePositif(Boolean.TRUE);
        }

        if (JadeStringUtil.isDigit(getForIdCategorie())) {
            ctxInfoMgr.setForIdCategorie(getForIdCategorie());
        }

        ctxInfoMgr.setForTypesSections(getTypesSections());
        ctxInfoMgr.setFromNoAffilie(getFromNoAffilie());
        ctxInfoMgr.setBeforeNoAffilie(getBeforeNoAffilie());
        ctxInfoMgr.setForNotModeCompensation(APISection.MODE_REPORT);
        ctxInfoMgr.setForTriListeCA(getSelectionTriListeCA());
        ctxInfoMgr.setForTriListeSection(getSelectionTriListeSection());

        // TODO sel tester le plus optimal.
        ctxInfoMgr.setForNotInContentieux(Boolean.FALSE);
        ctxInfoMgr.setForIdLastEtatAquila("0");

        ctxInfoMgr.setSession(session);

        return ctxInfoMgr;
    }

    /**
     * @return the imprimerDocument
     */
    public boolean isImprimerDocument() {
        return imprimerDocument;
    }

    public boolean isImprimerJournalContentieuxExcelml() {
        return imprimerJournalContentieuxExcelml;
    }

    public boolean isImprimerListeDeclenchement() {
        return imprimerListeDeclenchement;
    }

    public boolean isImprimerListePourOP() {
        return imprimerListePourOP;
    }

    public boolean isPrevisionnel() {
        return previsionnel;
    }

    public void setBeforeNoAffilie(String beforeNoAffilie) {
        this.beforeNoAffilie = beforeNoAffilie;
    }

    public void setDateDelaiPaiement(String dateDelaiPaiement) {
        this.dateDelaiPaiement = dateDelaiPaiement;
    }

    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public void setForIdGenreCompte(String forIdGenreCompte) {
        this.forIdGenreCompte = forIdGenreCompte;
    }

    public void setFromNoAffilie(String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }

    /**
     * @param imprimerDocument
     *            the imprimerDocument to set
     */
    public void setImprimerDocument(boolean imprimerDocument) {
        this.imprimerDocument = imprimerDocument;
    }

    public void setImprimerJournalContentieuxExcelml(boolean imprimerJournalContentieuxExcelml) {
        this.imprimerJournalContentieuxExcelml = imprimerJournalContentieuxExcelml;
    }

    public void setImprimerListeDeclenchement(boolean imprimerListeDeclenchement) {
        this.imprimerListeDeclenchement = imprimerListeDeclenchement;
    }

    public void setImprimerListePourOP(boolean imprimerListePourOP) {
        this.imprimerListePourOP = imprimerListePourOP;
    }

    public void setJournalAdapterBatch(COJournalAdapterBatch journalBatch) {
        journalAdapterBatch = journalBatch;
    }

    public void setJournalContentieuxExcelml(COImprimerJournalContentieuxExcelml journalContentieuxExcelml) {
        this.journalContentieuxExcelml = journalContentieuxExcelml;
    }

    public void setListeDeclenchement(COImprimerListeDeclenchement listeDeclenchement) {
        this.listeDeclenchement = listeDeclenchement;
    }

    public void setListePourOP(COImprimerListPourOP listePourOP) {
        this.listePourOP = listePourOP;
    }

    public void setPrevisionnel(boolean previsionnel) {
        this.previsionnel = previsionnel;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setSelections(Map<String, COSelection> selections) {
        this.selections = selections;
    }

    public void setSelectionTriListeCA(String selectionTriListeCA) {
        this.selectionTriListeCA = selectionTriListeCA;
    }

    public void setSelectionTriListeSection(String selectionTriListeSection) {
        this.selectionTriListeSection = selectionTriListeSection;
    }

    /**
     * Crée les taxes et les renseigne dans l'action.
     * 
     * @param session
     * @param contentieux
     * @param transition
     * @param action
     * @throws Exception
     */
    private void setTaxes(BSession session, COContentieux contentieux, COTransition transition,
            COTransitionAction action) throws Exception {
        taxes = COProcessContentieuxUtils.getTaxes(session, contentieux, transition, action);
        action.setTaxes(taxes);
    }

    /**
     * @param typesSections
     */
    public void setTypesSections(List<String> typesSections) {
        this.typesSections = typesSections;
    }

    /**
     * En mode prévisionnel simulation des transitions qui auraient dû être effectuées s'ils avaient été sauvés en base.
     * 
     * @param parent
     * @param session
     * @param transaction
     * @param contentieux
     * @throws Exception
     */
    private void simulerTransitions(BProcess parent, BSession session, BTransaction transaction,
            COContentieux contentieux) throws Exception {
        contentieux.refreshLinks(transaction);

        // Prochaine date declenchement <= date reference
        JACalendarGregorian calendar = new JACalendarGregorian();
        if (calendar.compare(new JADate(contentieux.getProchaineDateDeclenchement()), new JADate(getDateReference())) > JACalendar.COMPARE_FIRSTLOWER) {
            return;
        }

        // verifier que la séquence a été sélectionnée
        COSelection selection = getSelections().get(contentieux.getIdSequence());

        if (selection == null) {
            return;
        }

        // tester l'exécution des transitions
        Iterator<COTransition> transitions = COProcessContentieuxUtils.loadTransitions(session, transaction,
                contentieux).iterator();

        while (transitions.hasNext() && !parent.isAborted()) {
            COTransition transition = transitions.next();

            // instancier l'action de transition a effectuer
            COTransitionAction action = COProcessContentieuxUtils.getTransitionAction(parent, selection, transition,
                    getDateSurDocument(), getDateDelaiPaiement());
            try {
                if (action != null) {

                    action.canExecute(contentieux, transaction);

                    setTaxes(session, contentieux, transition, action);

                    if (isImprimerDocument()) {
                        COServiceLocator.getTransitionService().executerTransition(session, transaction, contentieux,
                                action, getJournalAdapterBatch().getJournal(session, transaction));
                    }

                    // la transition aurait dû être effectuée si le contentieux
                    // avait été sauvé en base, on enregistre.
                    enregistrerTransitionPourImpression(session, contentieux, transition, "");

                    return;
                }
            } catch (COTransitionException e) {
                if ((e.getMessageId() != null) && e.getMessageId().equals("SEUIL_MINIMAL_INFERIEUR")) {
                    enregistrerTransitionPourImpression(session, contentieux, transition, e.getMessageId());
                }

                if ((e.getMessageId() != null) && e.getMessageId().equals("RDP_TAXESRESTANTES_NON_ACCEPTER")) {
                    enregistrerTransitionPourImpression(session, contentieux, transition, e.getMessageId());
                }

                throw e;
            }
        }
    }
}
