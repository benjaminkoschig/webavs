package globaz.aquila.process.batch;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COJournalBatch;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxFactory;
import globaz.aquila.db.access.poursuite.COEffetcuerTransitionsManager;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.db.access.traitspec.COTraitementSpecifique;
import globaz.aquila.db.access.traitspec.COTraitementSpecifiqueManager;
import globaz.aquila.print.list.COListParOPException;
import globaz.aquila.process.batch.utils.COImprimerJournalContentieuxExcelml;
import globaz.aquila.process.batch.utils.COImprimerListPourOP;
import globaz.aquila.process.batch.utils.COImprimerListeDeclenchement;
import globaz.aquila.process.batch.utils.COJournalAdapterBatch;
import globaz.aquila.process.batch.utils.COLogMessageUtil;
import globaz.aquila.process.batch.utils.COProcessContentieuxUtils;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.taxes.COTaxe;
import globaz.aquila.service.taxes.COTaxeException;
import globaz.aquila.ts.COTSPredicate;
import globaz.aquila.vb.process.COSelection;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class COEffectuerTransitions {

    private String beforeNoAffilie;
    private String dateDelaiPaiement;
    private String dateReference;
    private String dateSurDocument;
    private boolean executeTraitementSpecifique = false;
    private String forIdCategorie;
    private String forIdGenreCompte;
    private String forIdSequence;

    private String fromNoAffilie;

    private boolean imprimerDocument = false;

    private boolean imprimerJournalContentieuxExcelml = false;
    private boolean imprimerListeDeclenchement = false;

    private boolean imprimerListePourOP = false;

    private COJournalAdapterBatch journalAdapterBatch;

    private COImprimerJournalContentieuxExcelml journalContentieuxExcelml;
    private HashMap journauxTraitementSpecifique = null;

    private COImprimerListeDeclenchement listeDeclenchement;
    private COImprimerListPourOP listePourOP;
    private boolean previsionnel;

    private List roles;

    private Map selections;
    private String selectionTriListeCA;

    private String selectionTriListeSection;

    private List taxes;
    private List typesSections;

    /**
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    private COJournalBatch createJournalSpecifique(BSession session, BTransaction transaction) throws Exception {
        COJournalBatch journalSpec = new COJournalBatch();
        journalSpec.setSession(session);

        journalSpec.setDateCreation(getDateSurDocument());
        journalSpec.setLibelle(getJournalAdapterBatch().getLibelle());

        if (!isPrevisionnel()) {
            journalSpec.add(transaction);
        }

        if (journalSpec.hasErrors() || transaction.hasErrors()) {
            throw new Exception(journalSpec.getErrors().toString());
        }

        return journalSpec;
    }

    /**
     * Effectue les transitions pour tous les contentieux sélectionné.
     * 
     * @param parent
     * @param session
     * @param transaction
     * @throws Exception
     */
    public void effectuerTransitions(BProcess parent, BSession session, BTransaction transaction) throws Exception {
        parent.setState(session.getLabel("AQUILA_EFFECTUE_TRANSITIONS"));
        COEffetcuerTransitionsManager contentieuxMan = initContentieuxManager(session);
        BTransaction readTransaction = null;

        try {
            readTransaction = (BTransaction) (session).newTransaction();
            readTransaction.openTransaction();
            parent.setProgressScaleValue(contentieuxMan.getCount(readTransaction));
            BStatement statement = contentieuxMan.cursorOpen(readTransaction);

            Boolean transitionEffectuee = false;
            COContentieux contentieux = null;
            int lastDocNumber = 0;
            while (((contentieux = (COContentieux) contentieuxMan.cursorReadNext(statement)) != null)
                    && !parent.isAborted()) {

                parent.setProgressDescription(contentieux.getCompteAnnexe().getIdExterneRole() + "<br/>"
                        + contentieux.getSection().getIdExterne() + "<br/>");
                parent.incProgressCounter();

                try {
                    COTransition transition = passerEtapeSuivante(parent, session, transaction, contentieux);

                    if (transition != null) {
                        if (!isPrevisionnel()) {
                            if (transaction.hasErrors()) {
                                throw new COEffectuerTransitionException(transaction.getErrors().toString());
                            } else {
                                transaction.commit();
                            }
                        }

                        transitionEffectuee = true;
                        lastDocNumber = parent.getAttachedDocuments().size();

                        enregistrerTransitionPourImpression(session, contentieux, transition);
                    } else if (transaction.hasErrors()) {
                        throw new COEffectuerTransitionException(transaction.getErrors().toString());
                    }
                } catch (COTransitionException e) {
                    if ((e.getMessageId() != null)) {
                        if (!e.getMessageId().equals("SEUIL_MINIMAL_INFERIEUR")
                                && !e.getMessageId().equals("AQUILA_ERR_CONTENTIEUX_SUSPENDU")) {
                            if (!e.getMessageId().equals("RDP_TAXESRESTANTES_NON_ACCEPTER")) {
                                parent.getMemoryLog().logMessage(
                                        e.getMessage() + " - "
                                                + session.getLabel("AQUILA_ERREURS_DANS_LEXECUTION_TRANSITIONS")
                                                + " [A] : " + contentieux.getCompteAnnexe().getIdExterneRole() + "/"
                                                + contentieux.getSection().getIdExterne() + ")",
                                        FWMessage.AVERTISSEMENT, this.getClass().getName());
                            }
                            removeDocument(parent, lastDocNumber);
                        }
                    } else {
                        parent.getMemoryLog().logMessage(
                                e.toString() + " - " + session.getLabel("AQUILA_ERREURS_DANS_LEXECUTION_TRANSITIONS")
                                        + " [C] : " + contentieux.getCompteAnnexe().getIdExterneRole() + "/"
                                        + contentieux.getSection().getIdExterne() + ")", FWMessage.AVERTISSEMENT,
                                this.getClass().getName());

                        removeDocument(parent, lastDocNumber);
                    }

                    transaction.rollback();
                    transaction.clearErrorBuffer();

                } catch (COListParOPException e) {
                    parent.getMemoryLog().logMessage(
                            e.toString() + " - " + session.getLabel("AQUILA_ERREURS_DANS_LEXECUTION_TRANSITIONS")
                                    + " : " + contentieux.getCompteAnnexe().getIdExterneRole() + "/"
                                    + contentieux.getSection().getIdExterne() + ")", FWMessage.AVERTISSEMENT,
                            this.getClass().getName());
                } catch (COTaxeException e) {
                    parent.getMemoryLog().logMessage(
                            e.toString() + " - " + session.getLabel("AQUILA_ERREURS_DANS_LEXECUTION_TRANSITIONS")
                                    + " : " + contentieux.getCompteAnnexe().getIdExterneRole() + "/"
                                    + contentieux.getSection().getIdExterne() + ")", FWMessage.AVERTISSEMENT,
                            this.getClass().getName());
                } catch (COEffectuerTransitionException e) {
                    parent.getMemoryLog().logMessage(
                            e.toString() + " - " + session.getLabel("AQUILA_ERREURS_DANS_LEXECUTION_TRANSITIONS")
                                    + " : " + contentieux.getCompteAnnexe().getIdExterneRole() + "/"
                                    + contentieux.getSection().getIdExterne() + ")", FWMessage.AVERTISSEMENT,
                            this.getClass().getName());

                    transaction.rollback();
                    transaction.clearErrorBuffer();
                    removeDocument(parent, lastDocNumber);
                } catch (Exception e) {
                    parent.getMemoryLog().logMessage(
                            e.toString() + " - " + session.getLabel("AQUILA_ERREURS_DANS_LEXECUTION_TRANSITIONS")
                                    + " [B] : " + contentieux.getCompteAnnexe().getIdExterneRole() + "/"
                                    + contentieux.getSection().getIdExterne() + ")", FWMessage.AVERTISSEMENT,
                            this.getClass().getName());

                    transaction.rollback();
                    transaction.clearErrorBuffer();
                    removeDocument(parent, lastDocNumber);
                }
            }

            if (!transitionEffectuee) {
                parent.getMemoryLog().logMessage(session.getLabel("AQUILA_WARN_PAS_TRANSITIONS"),
                        FWViewBeanInterface.WARNING, this.getClass().getName());
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

    /**
     * Enregistre le passage a l'étape suivante dans les documents de liste, doit etre appele APRES que la transition
     * est effectuee.
     * 
     * @param session
     * @param contentieux
     * @param transition
     * @throws COTaxeException
     * @throws Exception
     */
    private void enregistrerTransitionPourImpression(BSession session, COContentieux contentieux,
            COTransition transition, String messageId) throws COListParOPException, COTaxeException {
        FWCurrency totalTaxeListe = new FWCurrency(0);
        FWCurrency totalFraisListe = new FWCurrency(0);
        if ((taxes != null) && !taxes.isEmpty()) {
            for (Iterator iterTaxes = taxes.iterator(); iterTaxes.hasNext();) {
                COTaxe frais = (COTaxe) iterTaxes.next();
                totalTaxeListe.add(frais.getMontantTaxeToCurrency());
                if (frais.loadCalculTaxe(session).isFraisPoursuite()) {
                    totalFraisListe = frais.getMontantTaxeToCurrency(); // Frais pour la liste par offices.
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

    /**
     * Enregistre le passage a l'étape suivante dans les documents de liste, doit etre appele APRES que la transition
     * est effectuee.
     * 
     * @param session
     * @param contentieux
     * @param transition
     * @throws COTaxeException
     * @throws Exception
     */
    private void enregistrerTransitionPourImpression(BSession session, COContentieux contentieux,
            COTransition transition) throws COListParOPException, COTaxeException {
        enregistrerTransitionPourImpression(session, contentieux, transition, "");
    }

    /**
     * Exécute le traitement spécifique si il y en a un à exécuter.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param transition
     * @param action
     * @return
     * @throws COTransitionException
     */
    private boolean executeTraitementSpecifique(BSession session, BTransaction transaction, COContentieux contentieux,
            COTransition transition, COTransitionAction action) throws Exception {
        if (action.isIgnoreCarMontantMinimal()) {
            return false;
        }

        COTraitementSpecifiqueManager traitementSpecManager = new COTraitementSpecifiqueManager();
        traitementSpecManager.setSession(session);
        traitementSpecManager.find(transaction, BManager.SIZE_NOLIMIT);

        boolean hasAtLeastOnePredicate = false;
        for (int i = 0; i < traitementSpecManager.size(); i++) {
            COTraitementSpecifique traitementSpecifique = (COTraitementSpecifique) traitementSpecManager.get(i);

            COTSPredicate predicate = traitementSpecifique.getPredicate();

            if (predicate.evaluate(contentieux, transition, getDateSurDocument())) {
                hasAtLeastOnePredicate = true;

                COElementJournalBatch elementJournal = traitementSpecifique.getValidator().validate(session,
                        transaction, contentieux, transition);
                elementJournal.setSession(session);
                elementJournal.setIdJournal(getJournalTraitementSpecifique(session, transaction,
                        traitementSpecifique.getIdTraitementSpecifique()).getIdJournal());
                elementJournal.setIdTraitementSpecifique(traitementSpecifique.getIdTraitementSpecifique());

                if (elementJournal.isErreur()) {
                    setEtatEnErreurJournalTraitementSpecifique(session, transaction, traitementSpecifique);
                }

                if (!isPrevisionnel()) {
                    elementJournal.add(transaction);
                }

                if (elementJournal.hasErrors()) {
                    throw new Exception(elementJournal.getErrors().toString());
                }
            }
        }

        return hasAtLeastOnePredicate;
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

    /**
     * @return the forIdSequence
     */
    public String getForIdSequence() {
        return forIdSequence;
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

    /**
     * Un journal aquila par traitement spécifique.
     * 
     * @param session
     * @param transaction
     * @param idTraitementSpecifique
     * @param journal
     * @return
     * @throws Exception
     */
    private COJournalBatch getJournalTraitementSpecifique(BSession session, BTransaction transaction,
            String idTraitementSpecifique) throws Exception {
        COJournalBatch journalSpec = null;

        if (journauxTraitementSpecifique == null) {
            journauxTraitementSpecifique = new HashMap();

            journalSpec = createJournalSpecifique(session, transaction);

            journauxTraitementSpecifique.put(idTraitementSpecifique, journalSpec);
        } else {
            journalSpec = (COJournalBatch) journauxTraitementSpecifique.get(idTraitementSpecifique);

            if (journalSpec == null) {
                journalSpec = createJournalSpecifique(session, transaction);

                journauxTraitementSpecifique.put(idTraitementSpecifique, journalSpec);
            }
        }

        return journalSpec;
    }

    public HashMap getJournauxTraitementSpecifique() {
        return journauxTraitementSpecifique;
    }

    public COImprimerListeDeclenchement getListeDeclenchement() {
        return listeDeclenchement;
    }

    public COImprimerListPourOP getListePourOP() {
        return listePourOP;
    }

    public List getRoles() {
        return roles;
    }

    public Map getSelections() {
        return selections;
    }

    public String getSelectionTriListeCA() {
        return selectionTriListeCA;
    }

    public String getSelectionTriListeSection() {
        return selectionTriListeSection;
    }

    public List getTypesSections() {
        return typesSections;
    }

    /**
     * Initialise le manager principal afin de traité tout les cas de contentieux.
     * 
     * @param session
     * @return
     */
    private COEffetcuerTransitionsManager initContentieuxManager(BSession session) {
        COEffetcuerTransitionsManager contentieuxMan = new COEffetcuerTransitionsManager();

        contentieuxMan.setSession(session);

        contentieuxMan.setBeforeProchaineDateDeclenchement(getDateReference());
        contentieuxMan.setFromNumAffilie(getFromNoAffilie());
        contentieuxMan.setBeforeNumAffilie(getBeforeNoAffilie());

        contentieuxMan.setForTriListeCA(getSelectionTriListeCA());
        contentieuxMan.setForTriListeSection(getSelectionTriListeSection());

        if (JadeStringUtil.isDigit(getForIdCategorie())) {
            contentieuxMan.setForIdCategorie(getForIdCategorie());
        }

        contentieuxMan.setForIdGenreCompte(getForIdGenreCompte());
        contentieuxMan.setForNotModeCompensation(APISection.MODE_REPORT);

        contentieuxMan.setForIdsRoles(getRoles());
        contentieuxMan.setForTypesSections(getTypesSections());

        if (JadeStringUtil.contains(getTypesSections().toString(), APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE)
                && (selections.containsKey("4"))) {
            contentieuxMan.setForSoldeOperator("=");
            contentieuxMan.setForSolde("0");
            contentieuxMan.setForBulletinNeutre(true);
        } else {
            contentieuxMan.setForSoldeOperator(">");
            contentieuxMan.setForSolde("0");
        }

        contentieuxMan.setForIdSequence(getForIdSequence());

        ArrayList<String> details = new ArrayList<String>();
        for (Iterator<COSelection> selectionIter = selections.values().iterator(); selectionIter.hasNext();) {
            COSelection selection = selectionIter.next();
            details.addAll(selection.getDetailEtapes());
        }
        contentieuxMan.setForIdsEtapeshistorique(details);

        return contentieuxMan;
    }

    public boolean isExecuteTraitementSpecifique() {
        return executeTraitementSpecifique;
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

    /**
     * Effectue la transition pour le contentieux donné.
     * 
     * @param parent
     * @param session
     * @param transaction
     * @param contentieux
     * @return la transition qui vient d'être effectuée ou null si aucune transition n'a été effectuée ou si l'on est en
     *         mode prévisionnel.
     * @throws Exception
     */
    private COTransition passerEtapeSuivante(BProcess parent, BSession session, BTransaction transaction,
            COContentieux contentieux) throws Exception {
        // verifier que la séquence a été sélectionnée
        COSelection selection = (COSelection) getSelections().get(contentieux.getIdSequence());

        if (selection == null) {
            return null;
        }

        // Recharger le contentieux
        contentieux = COContentieuxFactory.loadContentieux(session, contentieux.getIdContentieux());

        Iterator transitions = COProcessContentieuxUtils.loadTransitions(session, transaction, contentieux).iterator();
        while (transitions.hasNext()) {
            COTransition transition = (COTransition) transitions.next();

            COTransitionAction action = COProcessContentieuxUtils.getTransitionAction(parent, selection, transition,
                    getDateSurDocument(), getDateDelaiPaiement());

            if (action != null) {
                try {

                    // Traitement effectué uniquement pour la RV en automatique.
                    if (ICOEtape.CS_REQUISITION_DE_VENTE_SAISIE.equals(action.getTransition().getEtapeSuivante()
                            .getLibEtape())
                            || ICOEtape.CS_1ER_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE.equals(action.getTransition()
                                    .getEtapeSuivante().getLibEtape())
                            || ICOEtape.CS_2EME_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE.equals(action.getTransition()
                                    .getEtapeSuivante().getLibEtape())
                            || ICOEtape.CS_3EME_RAPPEL_DE_REQUISITION_DE_VENTE_ENVOYE.equals(action.getTransition()
                                    .getEtapeSuivante().getLibEtape())) {
                        // retrouve les infos par étapes
                        List etapeInfos = action.getTransition().getEtapeSuivante().loadEtapeInfoConfigs();
                        if (!etapeInfos.isEmpty()) {
                            COHistorique historique = COServiceLocator.getHistoriqueService().getHistoriqueForLibEtape(
                                    session, contentieux, transition.getEtape().getLibEtape());// ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION);
                            for (Iterator infoIter = etapeInfos.iterator(); infoIter.hasNext();) {
                                COEtapeInfoConfig infoConfig = (COEtapeInfoConfig) infoIter.next();
                                if (COEtapeInfoConfig.CS_TYPE_SAISIE.equals(infoConfig.getCsLibelle())) {
                                    COEtapeInfo etapeInfo = historique.loadEtapeInfo(COEtapeInfoConfig.CS_TYPE_SAISIE);
                                    String valeur = etapeInfo.getValeur();
                                    action.addEtapeInfo(infoConfig, valeur);
                                }
                            }
                        }
                    }

                    // Vérifier que l'on peut exécuter l'action...else throw exception
                    action.canExecute(contentieux, transaction);

                    if (isExecuteTraitementSpecifique()) {
                        if (executeTraitementSpecifique(session, transaction, contentieux, transition, action)) {
                            return transition;
                        }
                    } else {
                        // Ne pas exécuter l'étape si le montant est minime
                        FWCurrency solde = new FWCurrency(contentieux.getSolde());
                        if (solde.compareTo(new FWCurrency(transition.getEtapeSuivante().getMontantMinimal())) < 0) {
                            parent.getMemoryLog().logMessage(
                                    COLogMessageUtil.formatMessage(
                                            new StringBuffer(session.getLabel("AQUILA_IGNORE_CAR_MONTANT_MINIMAL")),
                                            new Object[] { contentieux.getSection().getFullDescription(),
                                                    contentieux.getCompteAnnexe().getDescription() }),
                                    FWMessage.INFORMATION, this.getClass().getName());
                        } else {
                            setTaxes(session, contentieux, transition, action);

                            if (!(isPrevisionnel() && !isImprimerDocument())) {
                                // exécuter l'action
                                COServiceLocator.getTransitionService().executerTransition(session, transaction,
                                        contentieux, action, getJournalAdapterBatch().getJournal(session, transaction));
                            }
                        }

                        if (transaction.hasErrors()) {
                            throw new Exception(COLogMessageUtil.formatMessage(
                                    new StringBuffer(session
                                            .getLabel("AQUILA_PROCESS_CREER_CONTENTIEUX_ERREUR_EXECUTER")),
                                    new Object[] { transition.getEtapeSuivante().getLibActionLibelle(),
                                            contentieux.getSection().getDateSection(),
                                            contentieux.getCompteAnnexe().getIdExterneRole(),
                                            transaction.getErrors().toString() }));
                        }

                        return transition;
                    }
                } catch (COTransitionException e) {
                    if ((e.getMessageId() != null)) {
                        if (e.getMessageId().equals("SEUIL_MINIMAL_INFERIEUR")) {
                            enregistrerTransitionPourImpression(session, contentieux, transition);
                        }
                        if (e.getMessageId().equals("RDP_TAXESRESTANTES_NON_ACCEPTER")) {
                            enregistrerTransitionPourImpression(session, contentieux, transition, e.getMessageId());
                        }

                        if (e.getMessageId().equals("RDP_IMEXISTANT_EXTRAITCOMPTE")) {
                            enregistrerTransitionPourImpression(session, contentieux, transition, e.getMessageId());
                        }
                    }

                    throw e;
                }
            }
        }

        return null;
    }

    /**
     * @param parent
     * @param lastDocNumber
     */
    private void removeDocument(BProcess parent, int lastDocNumber) {
        // on supprime les documents produit durant la boucle courante
        for (int i = parent.getAttachedDocuments().size() - 1; i >= lastDocNumber; i--) {
            parent.getAttachedDocuments().remove(i);
        }
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

    /**
     * Mise à jour état du journal spécifique en erreur s'il contient une erreur.
     * 
     * @param session
     * @param transaction
     * @param traitementSpecifique
     * @throws Exception
     */
    private void setEtatEnErreurJournalTraitementSpecifique(BSession session, BTransaction transaction,
            COTraitementSpecifique traitementSpecifique) throws Exception {
        getJournalTraitementSpecifique(session, transaction, traitementSpecifique.getIdTraitementSpecifique()).setEtat(
                COJournalBatch.ERREUR);

        if (!isPrevisionnel()) {
            getJournalTraitementSpecifique(session, transaction, traitementSpecifique.getIdTraitementSpecifique())
                    .update(transaction);
        }
    }

    public void setExecuteTraitementSpecifique(boolean executeTraitementSpecifique) {
        this.executeTraitementSpecifique = executeTraitementSpecifique;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public void setForIdGenreCompte(String forIdGenreCompte) {
        this.forIdGenreCompte = forIdGenreCompte;
    }

    /**
     * @param forIdSequence
     *            the forIdSequence to set
     */
    public void setForIdSequence(String forIdSequence) {
        this.forIdSequence = forIdSequence;
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

    public void setJournauxTraitementSpecifique(HashMap journauxTraitementSpecifique) {
        this.journauxTraitementSpecifique = journauxTraitementSpecifique;
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

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public void setSelections(Map selections) {
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

    public void setTypesSections(List typesSections) {
        this.typesSections = typesSections;
    }
}
