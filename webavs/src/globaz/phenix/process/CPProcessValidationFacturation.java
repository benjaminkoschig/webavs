package globaz.phenix.process;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.facturation.FAPassage;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.db.principale.CPSortie;
import globaz.phenix.db.principale.CPSortieManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class CPProcessValidationFacturation extends BProcess {

    private static final long serialVersionUID = -7369682629541191477L;
    private AFAffiliation affiliation = null;
    private AFAffiliation affiliationPrec = null;
    private int anneeLimite = 0;
    private String dateNNSS = "";
    private CPDecision decision = null;
    private String idAffiliationPrec = "";
    private String idDecision = "";
    private String idPassage = "";
    private String idTiersPrec = "";
    private CIJournal journal = null;
    private CIJournal journalAnneeLimite = null;
    private ArrayList<String> listIdJournalRetour = new ArrayList();
    private Vector messages = new Vector();
    private boolean optionReComptabiliser = false;
    private FAPassage passageFacturation = null;
    private BSession sessionNaos = null;
    private BSession sessionPavo = null;
    private TITiersViewBean tiers = null;
    private boolean transfertAnneePreEncodee = false;

    private boolean wantMajCI = true;

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessValidationFacturation() {
        super();
    }

    public CPProcessValidationFacturation(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessValidationFacturation(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Validation des décisions - Traitement final qui implique pour chaque décision Mettre à jour le CI de l'affilié
     * suivant l'année et le genre Mettre à jour la communication fiscale Mettre à jour les montants pour la facturation
     * périodique dans l'affiliation Mettre à jour l'état de la décision ------------------------------------- Ne
     * prendre que les décisions concernées par le passage et qui n'ont pas été traitées Date de création : (14.02.2002
     * 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // Sous controle d'exceptions
        try {
            // Recherche de la date d'introduction du NNSS
            try {
                setDateNNSS(((CPApplication) getSession().getApplication()).getDateNNSS());
            } catch (Exception e) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0115"));
            }
            // Création session PAVO pour find (écite classeCastException...
            sessionPavo = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(
                    getSession());
            sessionNaos = (BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(
                    getSession());
            // Recherche libelle et date de facturation du passage de
            // facturation
            FAPassage passageFact = new FAPassage();
            if (!isTransfertAnneePreEncodee() && !isOptionReComptabiliser()) {
                retrievePassageFact(passageFact);
            }
            // Recherche de l'année limite d'inscription CI - Paramètre
            if (JadeStringUtil.isIntegerEmpty(Integer.toString(getAnneeLimite()))) {
                setAnneeLimite(Integer.parseInt(CPToolBox.anneeLimite(getTransaction())));
            }
            if (!JadeStringUtil.isIntegerEmpty(Integer.toString(getAnneeLimite()))) {
                // Création du journal CI si le passage comporte des décisions
                // dont l'année est inférieure à l'année limite - Cas
                // rétroactifs
                // et s'il n'en existe déjà pas un pour ce n° de passage à
                // l'état ouvert (cas échec comptabilisation)
                boolean noNeedJournal = hasNeedJournalCIPourDecisionRectro(Integer.toString(getAnneeLimite() - 1));
                if (noNeedJournal) {
                    createJournal(passageFact);
                }
                // Recherche du journalAnneeLimite qui doit être unique et de
                // type COTISATIONS PERSONNELLES
                if (journalAnneeLimite == null) {
                    searchJournalAnneeLimite(Integer.toString(getAnneeLimite()));
                }
                // PO 4239 - Hotline Numéro 10AX59675 - 24.01.08 -
                // Problème sortie indépendant puis nouvelle affiliation non
                // actif => les
                // montants de sortie étaient "déjà" traités par la décision =>
                // erreur dans les cis
                if (!isTransfertAnneePreEncodee() && !optionReComptabiliser) {
                    majSortieCIetComFis();
                }
                processDecisions();
                // Remettre les erreurs des process dans le log
                for (Iterator<?> iter = messages.iterator(); iter.hasNext();) {
                    getMemoryLog().getMessagesToVector().add(iter.next());
                }
                if (!isTransfertAnneePreEncodee()) {
                    // mise à jour de l'état des journaux en retour du fisc
                    majJournalRetour();
                }
                // Mise à jour du total des inscriptions CI dans le journal
                if ((getJournal() != null) && !JadeStringUtil.isBlankOrZero(getJournal().getIdJournal())) {
                    // PO 2389: Si le journal ne contient aucune inscription =>
                    // suppression
                    CIEcritureManager ecrMng = new CIEcritureManager();
                    ecrMng.setSession(sessionPavo);
                    ecrMng.setForIdJournal(getJournal().getIdJournal());
                    if (ecrMng.getCount() == 0) {
                        getJournal().delete();
                    } else {
                        // Sinon test s'il faut comptabiliser le journal CI
                        getJournal().updateInscription(getTransaction());
                        // Comptabilisation du journal (dépend paramétrage de la
                        // caisse)
                        Boolean comptabiliserJournal = Boolean.FALSE;
                        try {
                            comptabiliserJournal = ((CPApplication) getSession().getApplication())
                                    .isComptabiliseJournalCi();
                        } catch (Exception e) {
                            comptabiliserJournal = Boolean.FALSE;
                        }
                        if (comptabiliserJournal.equals(Boolean.TRUE)) {
                            // Modif jmc : gestion des abort, si le process
                            // parent est à abort, on sort de la méthode
                            // comptabiliser
                            StringBuffer errors = getJournal().comptabiliser("", "", getTransaction(), this);
                            if (getTransaction().hasErrors() || (errors.length() > 0)) {
                                getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
                                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0152"), FWMessage.INFORMATION,
                                        this.getClass().getName());

                            }
                            if (errors.length() > 0) {
                                getMemoryLog().logMessage(errors.toString(), FWMessage.INFORMATION,
                                        this.getClass().getName());

                            }
                        }
                    }
                }
                // Mise à jour du total des inscriptions CI dans le journal
                // année limite
                getTransaction().disableSpy();
                getJournalAnneeLimite().updateInscription(getTransaction());
                getTransaction().enableSpy();
            } else {
                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0116"), FWMessage.ERREUR,
                        this.getClass().getName());
            }
            return !isOnError();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return isOnError();
        }
    }

    /**
     * Calcul des montants de cotisation.
     * 
     * @param dateFacturation
     * @param updateTransaction
     * @return
     */
    protected boolean _executeProcessParDecision() throws Exception {
        // Vérifier la décision
        if (JadeStringUtil.isIntegerEmpty(decision.getIdDecision())) {
            getMemoryLog().logMessage("5032", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }
        // Récupérer les données de la décision
        if (getDecision() == null) {
            CPDecision decis = null;
            decis = new CPDecision();
            decis.setSession(getSession());
            decis.setIdDecision(decision.getIdDecision());
            decis.retrieve();
            if (decis.hasErrors()) {
                this._addError(getTransaction(), decis.getMessage());
            } else {
                setDecision(decis);
            }
        }
        if (isOptionReComptabiliser() || isTransfertAnneePreEncodee()) {
            FAPassage passage = new FAPassage();
            passage.setIdPassage(decision.getIdPassage());
            passage.setSession(getSession());
            passage.retrieve();
            setPassageFacturation(passage);
        }
        // Récupération du tiers
        if (!getIdTiersPrec().equalsIgnoreCase(getDecision().getIdTiers())) {
            setIdTiersPrec(getDecision().getIdTiers());
            TITiersViewBean newTiers = new TITiersViewBean();
            newTiers.setSession(getSession());
            newTiers.setIdTiers(getDecision().getIdTiers());
            newTiers.retrieve();
            setTiers(newTiers);
        }
        // Récupération de l'affiliation
        if (!getIdAffiliationPrec().equalsIgnoreCase(getDecision().getIdAffiliation())) {
            setIdAffiliationPrec(getDecision().getIdAffiliation());
            AFAffiliation affi = new AFAffiliation();
            affi.setSession(getSession());
            affi.setAffiliationId(getDecision().getIdAffiliation());
            affi.setIdTiers(getDecision().getIdAffiliation());
            affi.retrieve();
            setAffiliation(affi);
        }
        getDecision().setDateFacturation(getPassageFacturation().getDateFacturation());
        getDecision().setDernierEtat(CPDecision.CS_FACTURATION);
        getTransaction().disableSpy();
        getDecision().setAffiliation(getAffiliation());
        getDecision().wantCallValidate(false);
        getDecision().update(getTransaction());
        getTransaction().enableSpy();
        // Mise à jour du CI si le genre est différent de non soumis
        // Ne pas mettre au CI également les étudiants sauf si c'est une
        // imutation
        int anneeDec = Integer.parseInt(decision.getAnneeDecision());
        if (isWantMajCI()
                && ((!CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                        && !CPDecision.CS_NON_SOUMIS.equalsIgnoreCase(decision.getGenreAffilie()) && !getAffiliation()
                        .getTypeAffiliation().equalsIgnoreCase(
                                globaz.naos.translation.CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) || CPDecision.CS_IMPUTATION
                            .equalsIgnoreCase(decision.getTypeDecision()))) {
            if ((anneeDec <= getAnneeLimite()) && !CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
                majCI();
            }
        }
        // Mise à jour du montant de facturation dans naos
        // Pas de mise à jour dans l'affiliation et dans les communications
        // fiscales
        // si c'est une décision de type BONIFICATION_MISE_EN_COMPTE
        // Le 08.12.2011: PB MEROBA qui met le code AUTRE_AGENCE après avoir pris la décision... PO 6028
        if (!isOnError() && !decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)
                && !CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())
                && !CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) { // PO 5934
            if ((anneeDec <= getAnneeLimite())
                    && (decision.getFacturation().equals(Boolean.TRUE) || CPDecision.CS_FRANCHISE
                            .equalsIgnoreCase(decision.getSpecification()))) {
                majAffiliation();
            }
            if (!isOnError()) {
                // Mise à jour communication fiscale
                CPProcessMiseAjourComFisc process = new CPProcessMiseAjourComFisc();
                process.setSession(getSession());
                process.setTransaction(getTransaction());
                process.setSimulation(new Boolean(false));
                String idJrn = process.majCommunicationFiscale(decision, true, null, null);
                if (!JadeStringUtil.isEmpty(idJrn)) {
                    Iterator iter = listIdJournalRetour.iterator();
                    // Insérer le journal si il n'existe pas
                    boolean ok = true;
                    while (iter.hasNext() && ok) {
                        String element = (String) iter.next();
                        if (element.equalsIgnoreCase(idJrn)) {
                            ok = false;
                        }
                    }
                    // Stockage de l'id journal à traiter
                    if (ok) {
                        listIdJournalRetour.add(idJrn);
                    }
                }
            }
        }
        if (!isOnError()) {
            // Mise à jour code actif des décisions de l'année
            majCodeActifDecision(decision.getAnneeDecision());
        }
        if (getTransaction().hasErrors()) {
            this._addError(getTransaction(),
                    getSession().getLabel("CP_MSG_0110") + " " + getAffiliation().getAffilieNumero() + " "
                            + getTiers().getNom() + getSession().getLabel("CP_MSG_0109A") + " "
                            + getDecision().getAnneeDecision());
        }
        return !isOnError();
    }

    /**
     * Création du journal.
     * 
     * @param passageFact
     * @throws Exception
     */
    private void createJournal(FAPassage passageFact) throws Exception {
        if (journal == null) {
            CIJournal journalCi = new CIJournal();
            journalCi.setSession(sessionPavo);
            journalCi.setIdTypeInscription(CIJournal.CS_DECISION_COT_PERS);
            journalCi.setRefExterneFacturation(getIdPassage());
            // On met le libellé du passage dans le libellé du journal
            if (passageFact.getLibelle().length() > 50) {
                journalCi.setLibelle(passageFact.getLibelle().substring(0, 10));
            } else {
                journalCi.setLibelle(passageFact.getLibelle());
            }
            journalCi.add(getTransaction());
            // Obligation de commiter car peut être rollbacker lors de la
            // première erreur dans sortie ou décision
            // ce ui signifie que l'on aurait pas de journal pour les
            // suivants...
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            }
            // disabler le spy car on fait des update plus loin
            getTransaction().disableSpy();
            setJournal(journalCi);
        }
    }

    protected String determinerGenreEcritureCI() throws Exception, NumberFormatException {
        String genre = "";
        if (decision.isNonActif()) {
            if (getTiers().isRentier(Integer.parseInt(decision.getAnneeDecision()))) {
                genre = CIEcriture.CS_CIGENRE_7;
            } else {
                genre = CIEcriture.CS_CIGENRE_4;
            }
        } else if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(decision.getGenreAffilie())) {
            genre = CIEcriture.CS_CIGENRE_3;
        } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())) {
            genre = CIEcriture.CS_CIGENRE_7;
        } else if (CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(decision.getGenreAffilie())) {
            genre = CIEcriture.CS_CIGENRE_9;
        } else if (CPDecision.CS_TSE.equalsIgnoreCase(decision.getGenreAffilie())) {
            genre = CIEcriture.CS_CIGENRE_2;
        }
        return genre;
    }

    /**
     * @return
     */
    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    /**
     * @return
     */
    public AFAffiliation getAffiliationPrec() {
        return affiliationPrec;
    }

    /**
     * Returns the anneeLimite.
     * 
     * @return int
     */
    public int getAnneeLimite() {
        return anneeLimite;
    }

    public String getDateNNSS() {
        return dateNNSS;
    }

    public globaz.phenix.db.principale.CPDecision getDecision() {
        return decision;
    }

    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";
        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("CP_MSG_0121") + " " + getIdPassage();
        }
        // else
        // obj = FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
        // Restituer l'objet
        return obj;
    }

    /**
     * @return
     */
    public String getIdAffiliationPrec() {
        return idAffiliationPrec;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public String getIdTiersPrec() {
        return idTiersPrec;
    }

    public globaz.pavo.db.inscriptions.CIJournal getJournal() {
        return journal;
    }

    /**
     * Returns the journalAnneeLimite.
     * 
     * @return globaz.pavo.db.inscriptions.CIJournal
     */
    public globaz.pavo.db.inscriptions.CIJournal getJournalAnneeLimite() {
        return journalAnneeLimite;
    }

    public FAPassage getPassageFacturation() {
        return passageFacturation;
    }

    /**
     * Returns the tiers.
     * 
     * @return TITiersViewBean
     */
    public TITiersViewBean getTiers() {
        return tiers;
    }

    /**
     * Util pour : Doit-on créé un journal de semaine ou non ? Test s'il existe déjà un journal à l'état ouvert et s'il
     * y a des décisions rétroactives
     * 
     * @param year
     * @return False Si toutes les années de décisions sont égales à l'année sélectionnée.
     * @throws Exception
     */
    private boolean hasNeedJournalCIPourDecisionRectro(String year) throws Exception {
        // Test si journal CI déjà existant à l'état ouvert pour ce passage de
        // facturation
        if ((getPassageFacturation() != null) && !JadeStringUtil.isBlankOrZero(getPassageFacturation().getIdPassage())) {
            CIJournalManager jourManager = new CIJournalManager();
            jourManager.setSession(sessionPavo);
            jourManager.getSession().connectSession(getSession());
            jourManager.setForReferenceExterneFacturation(getPassageFacturation().getIdPassage());
            jourManager.setForIdEtat(CIJournal.CS_OUVERT);
            jourManager.find(getTransaction());
            // prendre le premier journal du manager
            if (jourManager.getSize() > 0) {
                setJournal((CIJournal) jourManager.getEntity(0));
                return false;
            }
        }
        // Test s'il y a des décisions rétroactives
        CPDecisionManager decisionManager = new CPDecisionManager();
        initDecisionTiersManager(decisionManager);
        decisionManager.setToAnneeDecision(year);
        decisionManager.find(getTransaction());

        if (decisionManager.size() > 0) {
            return true;
        } else {
            // Recherche si il y a une sortie
            CPSortieManager sMng = new CPSortieManager();
            sMng.setSession(getSession());
            sMng.setUntilAnnee(year);
            sMng.find(getTransaction());
            if (sMng.size() > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Initialiase le manager servant à lire les décisions du passage.
     * 
     * @param decManager
     */
    private void initDecisionTiersManager(CPDecisionManager manager) {
        manager.setSession(getSession());
        if (!isTransfertAnneePreEncodee()) {
            if (!isOptionReComptabiliser()) {
                manager.setForIdPassage(getIdPassage());
            } else {
                manager.setForIdDecision(getIdDecision());
            }
            manager.setToAnneeDecision(Integer.toString(getAnneeLimite()));
            manager.setInEtat(CPDecision.CS_PB_COMPTABILISATION + ", " + CPDecision.CS_VALIDATION + ", "
                    + CPDecision.CS_REPRISE);
        } else {
            manager.setForAnneeDecision(Integer.toString(getAnneeLimite()));
            manager.setForEtat(CPDecision.CS_VALIDATION);
        }
        manager.orderByIdTiers();
        manager.orderByAnneeDecisionAsc();
        manager.orderByDateDecisionAsc();
        manager.orderByIdDecisionAsc();
    }

    protected CPSortieManager initManagerSortie() throws Exception {
        CPSortieManager sortieManager = new CPSortieManager();
        sortieManager.setSession(getTransaction().getSession());
        sortieManager.setForIdPassage(getIdPassage());
        sortieManager.setForChecked(new Boolean(true));
        sortieManager.orderByNoAffilie();
        sortieManager.orderByAnnee();
        sortieManager.orderByMontantCiDESC();
        sortieManager.changeManagerSize(0);
        sortieManager.find(getTransaction());
        return sortieManager;
    }

    public boolean isOptionReComptabiliser() {
        return optionReComptabiliser;
    }

    public boolean isTransfertAnneePreEncodee() {
        return transfertAnneePreEncodee;
    }

    public boolean isWantMajCI() {
        return wantMajCI;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Mise à jour des montants de cotisation dans l'affiliation afin qu'il soit repris pour la facturation périodique
     * Maj uniquement si la décision est plus récente Date de création : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     */
    public void majAffiliation() {
        // Sous contrôle d'exception
        // ne pas mettre à jour si c'est une sortie
        if ((getDecision().getDernierEtat() != null)
                && (getDecision().getDernierEtat().equalsIgnoreCase(CPDecision.CS_SORTIE))) {
            return;
        }
        try {
            // Il faut que l'année de décision soit plus récente que celle qui a
            // permis de mettre
            // à jour l'affiliation
            // Récupérer les données des cotisations
            CPCotisationManager cotiManager = new CPCotisationManager();
            cotiManager.setSession(sessionNaos);
            cotiManager.setForIdDecision(decision.getIdDecision());
            cotiManager.find(getTransaction());
            CPCotisation coti = null;
            AFCotisation afCoti = null;
            for (int i = 0; i < cotiManager.size(); i++) {
                coti = ((CPCotisation) cotiManager.getEntity(i));
                afCoti = new AFCotisation();
                afCoti.setSession(sessionNaos);
                afCoti.setCotisationId(coti.getIdCotiAffiliation());
                afCoti.retrieve(getTransaction());
                int anDecision = 0;
                int anCoti = 0;
                if (JadeStringUtil.isNull(decision.getAnneeDecision())
                        || JadeStringUtil.isEmpty(decision.getAnneeDecision())) {
                    anDecision = 0;
                } else {
                    anDecision = Integer.parseInt(decision.getAnneeDecision());
                }

                if (JadeStringUtil.isIntegerEmpty(afCoti.getAnneeDecision())
                        || JadeStringUtil.isNull(afCoti.getAnneeDecision())
                        || JadeStringUtil.isEmpty(afCoti.getAnneeDecision())) {
                    anCoti = 0;
                } else {
                    anCoti = Integer.parseInt(afCoti.getAnneeDecision());
                }

                if (anDecision >= anCoti) {
                    // Remise à zéro des montants si salarié dispensé
                    // 1-7-2: Ajout autre_agence.. PO 6028
                    if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(getDecision().getSpecification())
                            || CPDecision.CS_FRANCHISE.equalsIgnoreCase(getDecision().getSpecification())
                            || AFParticulariteAffiliation.existeParticularite(getSession(),
                                    decision.getIdAffiliation(), CodeSystem.PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE,
                                    decision.getDebutDecision())) {
                        afCoti.setMontantAnnuel("0");
                        afCoti.setMontantSemestriel("0");
                        afCoti.setMontantTrimestriel("0");
                        afCoti.setMontantMensuel("0");
                    } else {
                        afCoti.setMontantAnnuel(coti.getMontantAnnuel());
                        afCoti.setMontantSemestriel(coti.getMontantSemestriel());
                        afCoti.setMontantTrimestriel(coti.getMontantTrimestriel());
                        afCoti.setMontantMensuel(coti.getMontantMensuel());
                    }
                    afCoti.setAnneeDecision(decision.getAnneeDecision());
                    afCoti.wantCallValidate(false);
                    afCoti.wantCallExternalServices(false);
                    afCoti.update(getTransaction());
                    afCoti.wantCallExternalServices(true);
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0122") + " " + decision.getIdDecision()
                    + " " + e.toString());
            return;
        }
    }

    /**
     * Mise à jour des CI dans le fichier de saisie Si on a forcé un montant CI à l'encodage, on inscrit directement ce
     * CI sinon si le type de décision est une bonification on inscrit sans autre le montant CI qui a été calculé sinon
     * pour les autres genres de décision si un CI existe avec la même période, on met le complément sinon on extourne
     * le montant existant et on remplace par l'écriture générée par la décision Date de création : (03.08.2003
     * 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'exécution
     */
    public void majCI() {
        // Sous contrôle d'exception
        try {
            float varMontantCI = 0;
            // Recherche du n° avs correspondant à l'année de décision
            // Mandat 236 : Si période de décision < date NNSS
            // => prendre n° avs correspondant à la période décision dans
            // l'historique n° avs
            // sinon si aucun n° avs trouvé ou période de décision >= date NNSS
            // => prendre dernier n° valable
            String numAvs = majCINumAvs();
            if (!JadeStringUtil.isEmpty(numAvs)) {
                String montantCI = "";
                CPDonneesCalcul donnee = new CPDonneesCalcul();
                // Si montant forcé à l'encodage, prendre celui-ci sinon prendre
                // le montant calculé
                CPDonneesBase base = new CPDonneesBase();
                base.setSession(getSession());
                base.setIdDecision(decision.getIdDecision());
                base.retrieve(getTransaction());
                if (!base.isNew()) {
                    if (base.isRevenuCiForce0().equals(new Boolean(true))) {
                        return;
                    }
                    if (!"".equalsIgnoreCase(base.getRevenuCiForce())) {
                        montantCI = base.getRevenuCiForce();
                    } else {
                        donnee.setSession(getSession());
                        montantCI = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                    }
                } else {
                    donnee.setSession(getSession());
                    montantCI = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                }
                // Récupération période CI
                String moisDebut = Integer.toString(JACalendar.getMonth(decision.getDebutDecision()));
                String moisFin = Integer.toString(JACalendar.getMonth(decision.getFinDecision()));
                if (!base.isNew() && CPDonneesBase.CS_BENEFICE_CAP.equalsIgnoreCase(base.getSourceInformation())) {
                    moisDebut = "77";
                    moisFin = "77";
                }
                // mettre le journal CI correspondant à l'année de la décision
                CICompteIndividuelUtil ci = majCIInitJournalCI(); // Parallèle
                // entre le
                // genre de
                // décision
                // et le
                // genre
                // d'écriture
                // CI
                String genre = majCIInitGenre();
                boolean autreNumAffilie = rechercheAutreAffilie();
                if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
                    majCIImputation(numAvs, decision.getAffiliation().getAffilieNumero(), montantCI, moisDebut,
                            moisFin, ci, genre, autreNumAffilie);
                } else if (decision.getComplementaire().equals(new Boolean(true))) {
                    // Inscription directe de la décision complémentaire
                    if (!JadeStringUtil.isEmpty(montantCI) && !montantCI.equalsIgnoreCase("0.00")
                            && !montantCI.equalsIgnoreCase("-0.00") && !montantCI.equalsIgnoreCase("0")
                            && !montantCI.equalsIgnoreCase("-0")) {
                        ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                decision.getAnneeDecision(), montantCI, genre, CICompteIndividuelUtil.MODE_DIRECT,
                                null, CPToolBox._returnCodeSpecial(decision), "");
                    }
                } else {
                    // Recherche si il y a déjà eu une décision
                    // Si le genre ou la période sont différents => annulation
                    // et rempacement (2 lignes) sinon compensation (1 ligne)
                    // Si décision dispensé => annulation de l'éventuel CI (cas
                    // ou l'affilié avertie après qu'une décision a été prise
                    // qu'il a cotisé comme salarié)
                    // Tester s'il y a une décision pour le même tiers et la
                    // même année qui remplace une décision complémentaire -> PO
                    // 2091
                    CPDecision decBase = CPDecision._returnDecisionBase(getSession(), decision, true, true);
                    if ((decBase != null)
                            && (!decBase.getGenreAffilie().equalsIgnoreCase(decision.getGenreAffilie())
                                    || !decBase.getDebutDecision().substring(3)
                                            .equalsIgnoreCase(decision.getDebutDecision().substring(3))
                                    || !decBase.getFinDecision().substring(3)
                                            .equalsIgnoreCase(decision.getFinDecision().substring(3))
                                    || CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification()) || CPDecision.CS_FRANCHISE
                                        .equals(decision.getSpecification()))) {
                        // Recherche du montant CI de base
                        float varMontantCIBase = 0;
                        donnee.setSession(getSession());
                        String montCiBase = donnee.getMontant(decBase.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                        if (!JadeStringUtil.isBlankOrZero(montCiBase)) {
                            varMontantCIBase = Float.parseFloat(JANumberFormatter.deQuote(montCiBase));
                        }

                        String idAff = "";
                        java.math.BigDecimal montantEnCI;

                        // Annulation de l'imputation en premier lieu pour ne pas avoir de CI négatif
                        if (CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification())
                                || CPDecision.CS_FRANCHISE.equals(decision.getSpecification())) {
                            if (autreNumAffilie) {
                                montantEnCI = ci.getSommeParAnneeCodeAmortissement(numAvs, getDecision()
                                        .getAffiliation().getAffilieNumero(), genre, decision.getAnneeDecision(),
                                        CIEcriture.CS_CODE_MIS_EN_COMTE);
                            } else {
                                montantEnCI = ci.getSommeParAnneeCodeAmortissement(numAvs, "", genre,
                                        decision.getAnneeDecision(), CIEcriture.CS_CODE_MIS_EN_COMTE);
                            }
                            if (montantEnCI.floatValue() != 0) {
                                montantEnCI = montantEnCI.negate();
                                ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                        decision.getAnneeDecision(), montantEnCI.toString(), genre,
                                        CICompteIndividuelUtil.MODE_DIRECT, CIEcriture.CS_CODE_MIS_EN_COMTE,
                                        CPToolBox._returnCodeSpecial(decision), "");
                            }
                        }

                        if (autreNumAffilie) {
                            montantEnCI = ci.getSommeParAnneeNoAffilie(numAvs, decision.getAffiliation()
                                    .getAffilieNumero(), genre, decBase.getAnneeDecision(),
                                    CIEcriture.CS_CODE_AMORTISSEMENT + ", " + CIEcriture.CS_CODE_MIS_EN_COMTE);
                            if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                                montantEnCI = montantEnCI.add(ci.getSommeParAnneeNoAffilie(numAvs, decision
                                        .getAffiliation().getAffilieNumero(), CIEcriture.CS_CIGENRE_7, decision
                                        .getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT + ", "
                                        + CIEcriture.CS_CODE_MIS_EN_COMTE));
                            }
                        } else {
                            montantEnCI = ci.getSommeParAnnee(numAvs, idAff, genre, decision.getAnneeDecision(),
                                    CIEcriture.CS_CODE_AMORTISSEMENT + ", " + CIEcriture.CS_CODE_MIS_EN_COMTE);
                            if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                                montantEnCI = montantEnCI.add(ci.getSommeParAnnee(numAvs, idAff,
                                        CIEcriture.CS_CIGENRE_7, decBase.getAnneeDecision(),
                                        CIEcriture.CS_CODE_AMORTISSEMENT + ", " + CIEcriture.CS_CODE_MIS_EN_COMTE));
                            }
                        }
                        if ((!JadeStringUtil.isEmpty(montCiBase) && (montantEnCI.floatValue() < Math.abs(Float
                                .parseFloat(JANumberFormatter.deQuote(montCiBase)))))
                                || !JadeStringUtil.isBlankOrZero(idAff)) {
                            montCiBase = montantEnCI.toString(); // Au max on
                            // met le
                            // montant
                            // au CI
                        }
                        // Inscription du CI - Modif tests PO 6399
                        if (!CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification())
                                && !CPDecision.CS_FRANCHISE.equals(decision.getSpecification())) {
                            if (!JadeStringUtil.isBlankOrZero(montantCI)) {
                                varMontantCI = Float.parseFloat(JANumberFormatter.deQuote(montantCI));
                            } else {
                                varMontantCI = 0;
                            }
                            // Ne pas inscrire en mode direct si montant = 0 ou
                            // si ancien montant = nouveau montant avec périodes
                            // identiques
                            if (varMontantCI != 0) {
                                if ((varMontantCI != varMontantCIBase)
                                        || !decBase.getDebutDecision().substring(3)
                                                .equalsIgnoreCase(decision.getDebutDecision().substring(3))
                                        || !decBase.getFinDecision().substring(3)
                                                .equalsIgnoreCase(decision.getFinDecision().substring(3))) {
                                    ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut,
                                            moisFin, decision.getAnneeDecision(), montantCI, genre,
                                            CICompteIndividuelUtil.MODE_DIRECT, null,
                                            CPToolBox._returnCodeSpecial(decision), "");
                                }
                            }
                            // calculer et mettre en négatif le CI lié au
                            // montant payé comme salarié
                            if (!JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())) {
                                majCICotisationSalarie(numAvs, montantCI, base, moisDebut, moisFin, ci, genre);
                            }
                        }
                        if (!JadeStringUtil.isBlankOrZero(montCiBase)) {
                            varMontantCIBase = Float.parseFloat(JANumberFormatter.deQuote(montCiBase));
                        } else {
                            varMontantCIBase = 0;
                        }
                        // Ne pas annuler en mode direct si montant de base = 0
                        // ou si ancien montant = nouveau montant avec périodes
                        // identiques
                        if (varMontantCIBase != 0) {
                            if ((varMontantCI != varMontantCIBase)
                                    || !decBase.getDebutDecision().substring(3)
                                            .equalsIgnoreCase(decision.getDebutDecision().substring(3))
                                    || !decBase.getFinDecision().substring(3)
                                            .equalsIgnoreCase(decision.getFinDecision().substring(3))) {
                                // Annulation du CI de l'ancienne décision
                                ci.verifieCI(getTransaction(), decBase.getIdAffiliation(), numAvs,
                                        Integer.toString(JACalendar.getMonth(decBase.getDebutDecision())),
                                        Integer.toString(JACalendar.getMonth(decBase.getFinDecision())),
                                        decBase.getAnneeDecision(), "-" + montCiBase, genre,
                                        CICompteIndividuelUtil.MODE_DIRECT, null,
                                        CPToolBox._returnCodeSpecial(decBase), "");
                            }
                        }
                    } else {
                        // Si un CI existe avec la même période, on met le
                        // complément sinon on extourne
                        // le montant existant et on remplace par l'écriture
                        // générée par la décision
                        if (JadeStringUtil.isEmpty(montantCI)
                                || CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification())
                                || CPDecision.CS_FRANCHISE.equals(decision.getSpecification())) { // PO 6399
                            montantCI = "0";
                        }
                        if (autreNumAffilie) {
                            ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                    decision.getAnneeDecision(), montantCI, genre,
                                    CICompteIndividuelUtil.MODE_COMPENSATION, false,
                                    CPToolBox._returnCodeSpecial(decision), getAffiliation().getAffilieNumero());
                        } else {
                            ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                                    decision.getAnneeDecision(), montantCI, genre,
                                    CICompteIndividuelUtil.MODE_COMPENSATION, false,
                                    CPToolBox._returnCodeSpecial(decision), "");
                        }
                        // calculer et mettre en négatif le CI lié au
                        // montant payé comme salarié
                        if (!JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())
                                && !CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification())
                                && !CPDecision.CS_FRANCHISE.equals(decision.getSpecification())) { // PO 6399
                            majCICotisationSalarie(numAvs, montantCI, base, moisDebut, moisFin, ci, genre);
                        }
                    }
                }
            } else {
                this._addError(getTransaction(),
                        getSession().getLabel("CP_MSG_0196") + " - " + getSession().getLabel("NUM_AFFILIE") + ": "
                                + getAffiliation().getAffilieNumero());
            }
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0124") + "(" + e.toString() + ")");
        }
    }

    private void majCICotisationSalarie(String numAvs, String montantCI, CPDonneesBase base, String moisDebut,
            String moisFin, CICompteIndividuelUtil ci, String genre) throws Exception {
        float revenuCiImputation = 0;
        float varMontCI = 0;
        // recherche coti payé en tant que salarié
        float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(base.getCotisationSalarie()));
        // Calcul du Ci qui doit être imputer selon le montant de cotisation
        // payé en tant que salarié
        CPCotisation coti = CPCotisation._returnCotisation(getSession(), decision.getIdDecision(),
                CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        if (coti != null) {
            // Calcul du CI
            revenuCiImputation = (Float.parseFloat(JANumberFormatter.deQuote(coti.getMontantAnnuel())) - cotiEncode)
                    * (float) 9.9;
            revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2, JANumberFormatter.NEAR);
            revenuCiImputation = Float.parseFloat(JANumberFormatter.deQuote(montantCI)) - revenuCiImputation;
        } else {
            revenuCiImputation = cotiEncode * (float) 9.9;
            revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2, JANumberFormatter.NEAR);
        }
        montantCI = Float.toString(revenuCiImputation);
        if (!JadeStringUtil.isBlankOrZero(montantCI)) {
            varMontCI = Math.abs(Float.parseFloat(JANumberFormatter.deQuote(montantCI)));
        }
        if (varMontCI != 0) {
            ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                    decision.getAnneeDecision(), "-" + montantCI, genre, CICompteIndividuelUtil.MODE_DIRECT,
                    CIEcriture.CS_CODE_MIS_EN_COMTE, CPToolBox._returnCodeSpecial(decision), "");
        }
    }

    private void majCIImputation(String numAvs, String numAffilie, String montantCI, String moisDebut, String moisFin,
            CICompteIndividuelUtil ci, String genre, boolean autreNumAffilie) throws Exception {
        // Si annule et remplace
        if (Boolean.TRUE.equals(decision.getComplementaire())) {
            // Extourne du total
            java.math.BigDecimal montantEnCI = new BigDecimal(0);
            if (autreNumAffilie) {
                montantEnCI = ci.getSommeParAnneeCodeAmortissement(numAvs, numAffilie, genre,
                        decision.getAnneeDecision(), CIEcriture.CS_CODE_MIS_EN_COMTE);
            } else {
                montantEnCI = ci.getSommeParAnneeCodeAmortissement(numAvs, "", genre, decision.getAnneeDecision(),
                        CIEcriture.CS_CODE_MIS_EN_COMTE);
            }
            if (montantEnCI.floatValue() != 0) {
                montantEnCI = montantEnCI.negate();
                ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                        decision.getAnneeDecision(), montantEnCI.toString(), genre, CICompteIndividuelUtil.MODE_DIRECT,
                        CIEcriture.CS_CODE_MIS_EN_COMTE, CPToolBox._returnCodeSpecial(decision), "");
            }
            if (!JadeStringUtil.isEmpty(montantCI) && !montantCI.equalsIgnoreCase("0.00")
                    && !montantCI.equalsIgnoreCase("-0.00") && !montantCI.equalsIgnoreCase("0")
                    && !montantCI.equalsIgnoreCase("-0")) {
                // Inscription du nouveau montant
                ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                        decision.getAnneeDecision(), "-" + montantCI, genre, CICompteIndividuelUtil.MODE_DIRECT,
                        CIEcriture.CS_CODE_MIS_EN_COMTE, CPToolBox._returnCodeSpecial(decision), "");
            }
        } else {
            // Inscription directe de la bonification
            java.math.BigDecimal montantEnCI = ci.getSommeParAnnee(numAvs, "", genre, decision.getAnneeDecision(),
                    CIEcriture.CS_CODE_AMORTISSEMENT);
            if (!CIEcriture.CS_CIGENRE_7.equalsIgnoreCase(genre)) {
                montantEnCI = montantEnCI.add(ci.getSommeParAnnee(numAvs, "", CIEcriture.CS_CIGENRE_7,
                        decision.getAnneeDecision(), CIEcriture.CS_CODE_AMORTISSEMENT));
            }
            if (!JadeStringUtil.isEmpty(montantCI)
                    && (montantEnCI.floatValue() < Math.abs(Float.parseFloat(JANumberFormatter.deQuote(montantCI))))) {
                montantCI = "-" + montantEnCI.toString(); // Au max on met le
                // montant au CI
            }
            if (!JadeStringUtil.isEmpty(montantCI) && !montantCI.equalsIgnoreCase("0.00")
                    && !montantCI.equalsIgnoreCase("-0.00") && !montantCI.equalsIgnoreCase("0")
                    && !montantCI.equalsIgnoreCase("-0")) {
                ci.verifieCI(getTransaction(), decision.getIdAffiliation(), numAvs, moisDebut, moisFin,
                        decision.getAnneeDecision(), "-" + montantCI, genre, CICompteIndividuelUtil.MODE_DIRECT,
                        CIEcriture.CS_CODE_MIS_EN_COMTE, CPToolBox._returnCodeSpecial(decision), "");
            }
        }
    }

    private String majCIInitGenre() throws Exception {
        String genre = determinerGenreEcritureCI();
        return genre;
    }

    private CICompteIndividuelUtil majCIInitJournalCI() {
        CICompteIndividuelUtil ci = new CICompteIndividuelUtil();
        if ((journalAnneeLimite != null) && (!journalAnneeLimite.isNew())) {
            if (Integer.parseInt(decision.getAnneeDecision()) >= getAnneeLimite()) {
                ci.setSession(getJournalAnneeLimite().getSession());
                ci.setIdJournal(getJournalAnneeLimite().getIdJournal());
            } else {
                ci.setSession(getJournal().getSession());
                ci.setIdJournal(getJournal().getIdJournal());
            }
        } else {
            ci.setIdJournal(getJournal().getIdJournal());
        }
        return ci;
    }

    private String majCINumAvs() throws Exception {
        String numAvs = "";
        if (BSessionUtil.compareDateFirstLower(getSession(), decision.getFinDecision(), getDateNNSS())) {
            TIHistoriqueAvs hist = new TIHistoriqueAvs();
            hist.setSession(getSession());
            try {
                numAvs = hist.findPrevKnownNumAvs(decision.getIdTiers(), decision.getFinDecision());
                if (JadeStringUtil.isEmpty(numAvs)) {
                    numAvs = hist.findNextKnownNumAvs(decision.getIdTiers(), decision.getDebutDecision());
                }
            } catch (Exception e) {
                numAvs = "";
            }
        }
        // Si aucun n° trouvé dans historique ou NNSS => prendre l'actuel n° avs
        if (JadeStringUtil.isEmpty(numAvs)) {
            numAvs = getTiers().getNumAvsActuel();
        }
        return numAvs;
    }

    /**
     * Mise à jour du code actif des décisions de l'année Date de création : (16.12.2005 12:58:23)
     */
    public void majCodeActifDecision(String anneeDecision) {
        try {
            CPToolBox.miseAjourDecisionActive(getTransaction(), getAffiliation(), anneeDecision, getAnneeLimite());
            CPToolBox.miseAjourImputation(getTransaction(), getAffiliation(), anneeDecision, getAnneeLimite());
            CPToolBox.miseAjourRemise(getTransaction(), getAffiliation(), anneeDecision, getAnneeLimite());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0123") + " "
                    + getAffiliation().getAffilieNumero() + "année: " + anneeDecision + e.toString());
            return;
        }
    }

    /**
     * Mise à jour du status des éventuels journaux si des décisions ont été générées par des données du fisc.
     */
    public void majJournalRetour() throws Exception {
        Iterator<String> iter = listIdJournalRetour.iterator();
        // Insérer le journal si il n'existe pas
        while (iter.hasNext()) {
            // Mise à jour du journal
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession(getSession());
            jrn.setIdJournalRetour(iter.next());
            jrn.retrieve(getTransaction());
            if (!jrn.isNew()) {
                jrn.setStatus(CPJournalRetour.CS_COMPTABILISE_TOTAL);
                jrn.update(getTransaction());
            }
        }
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    protected boolean majSortieCIetComFis() {
        try {
            // Pour la mise à jour des communications fiscales
            CPProcessMiseAjourComFisc process = new CPProcessMiseAjourComFisc();
            process.setSession(getSession());
            process.setSimulation(new Boolean(false));
            process.setTransaction(getTransaction());
            // Récupération période CI
            CICompteIndividuelUtil ci = new CICompteIndividuelUtil();
            if (getJournal() != null) {
                ci.setSession(getJournal().getSession());
            } else if (getJournalAnneeLimite() != null) {
                ci.setSession(getJournalAnneeLimite().getSession());
            } else {
                this._addError(getTransaction(),
                        getSession().getLabel("CP_MSG_0119") + " " + decision.getAnneeDecision());
            }
            CPSortieManager sortieManager = initManagerSortie();
            // itérer sur toutes les décisions de factures
            CPSortie mySortie = null;
            for (int i = 0; i < sortieManager.size(); i++) {
                try {
                    String numAvs = "";
                    getTransaction().clearErrorBuffer();
                    mySortie = (CPSortie) sortieManager.getEntity(i);
                    // mettre le journal CI correspondant suivant l'année de la
                    // sortie
                    if ((journalAnneeLimite != null) && (!journalAnneeLimite.isNew())) {
                        if (Integer.parseInt(mySortie.getAnnee()) >= getAnneeLimite()) {
                            ci.setIdJournal(getJournalAnneeLimite().getIdJournal());
                        } else {
                            ci.setIdJournal(getJournal().getIdJournal());
                        }
                    } else {
                        ci.setIdJournal(getJournal().getIdJournal());
                    }
                    // Extraction de l'affiliation
                    if ((getAffiliationPrec() == null)
                            || !getAffiliationPrec().getAffiliationId().equalsIgnoreCase(mySortie.getIdAffiliation())) {
                        AFAffiliation myAffiliation = new AFAffiliation();
                        myAffiliation.setSession(getSession());
                        myAffiliation.setAffiliationId(mySortie.getIdAffiliation());
                        myAffiliation.retrieve(getTransaction());
                        setAffiliationPrec(myAffiliation);
                    }
                    boolean aTraiter = true;
                    decision = new CPDecision();
                    decision.setIdDecision(mySortie.getIdDecision());
                    decision.setSession(getSession());
                    decision.retrieve();
                    if (!decision.isNew()) {
                        if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
                            // Regarder si l'état dans Campus est "Inscrit au CI"
                            GEAnnoncesManager annMan = new GEAnnoncesManager();
                            annMan.setSession(getSession());
                            annMan.setForIdDecision(decision.getIdDecision());
                            annMan.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_COMPTABILISE);
                            annMan.find();
                            if (annMan.getSize() == 0) {
                                aTraiter = false;
                            }
                        }
                    } else {
                        aTraiter = false;
                    }
                    if (aTraiter) {
                        // Récupération du tiers
                        if ((getIdTiersPrec() == null) || !getIdTiersPrec().equalsIgnoreCase(mySortie.getIdTiers())) {
                            setIdTiersPrec(mySortie.getIdTiers());
                            TITiersViewBean tiers = new TITiersViewBean();
                            tiers.setSession(getSession());
                            tiers.setIdTiers(mySortie.getIdTiers());
                            tiers.retrieve();
                            setTiers(tiers);
                        }
                        // Mandat 236 : Si période de décision < date NNSS
                        // => prendre n° avs correspondant à la période décision
                        // dans l'historique n° avs
                        // sinon si aucun n° avs trouvé ou période de décision
                        // >= date NNSS
                        // => prendre dernier n° valable
                        if (BSessionUtil.compareDateFirstLower(getSession(), decision.getFinDecision(), getDateNNSS())) {
                            TIHistoriqueAvs hist = new TIHistoriqueAvs();
                            hist.setSession(getSession());
                            try {
                                numAvs = hist
                                        .findPrevKnownNumAvs(mySortie.getIdTiers(), "31.12." + mySortie.getAnnee());
                                if (JadeStringUtil.isEmpty(numAvs)) {
                                    numAvs = hist.findNextKnownNumAvs(decision.getIdTiers(),
                                            "01.01." + mySortie.getAnnee());
                                }
                            } catch (Exception e) {
                                numAvs = "";
                            }
                        }
                        // Si aucun n° trouvé dans historique ou NNSS => prendre
                        // l'actuel n° avs
                        if (JadeStringUtil.isEmpty(numAvs)) {
                            numAvs = getTiers().getNumAvsActuel();
                        }
                        // Récupération période CI
                        String moisDebut = "66";
                        String moisFin = "66";
                        if (!JadeStringUtil.isEmpty(mySortie.getIdDecision())) {
                            moisDebut = Integer.toString(JACalendar.getMonth(decision.getDebutDecision()));
                            moisFin = Integer.toString(JACalendar.getMonth(decision.getFinDecision()));
                            // Détermination du genre
                            String genre = determinerGenreEcritureCI();
                            if (BSessionUtil.compareDateBetween(getSession(), decision.getDebutDecision(),
                                    decision.getFinDecision(), getAffiliationPrec().getDatePrecFin())
                                    && (new FWCurrency(mySortie.getMontantCI())).isNegative()) {
                                moisFin = Integer.toString(JACalendar.getMonth(getAffiliationPrec().getDatePrecFin()));
                            } else if (BSessionUtil.compareDateBetween(getSession(), decision.getDebutDecision(),
                                    decision.getFinDecision(), getAffiliationPrec().getDateFin())
                                    && (new FWCurrency(mySortie.getMontantCI())).isPositive()) {
                                moisFin = Integer.toString(JACalendar.getMonth(getAffiliationPrec().getDateFin()));
                            }
                            if (!JadeStringUtil.isEmpty(numAvs)) {
                                // Mettre le montant de la décision que si il
                                // est infèrieure à celui qui est au CI pour
                                // l'année concernée
                                // sinon mettre le montant du CI
                                // Recherche du montant CI de base
                                String montCiBase = mySortie.getMontantCI();
                                if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())
                                        && new FWCurrency(montCiBase).isPositive()) {
                                    java.math.BigDecimal montantEnCI = ci.getSommeParAnneeCodeAmortissement(numAvs,
                                            genre, mySortie.getAnnee(), CIEcriture.CS_CODE_MIS_EN_COMTE);
                                    if (!JadeStringUtil.isEmpty(montCiBase)
                                            && (Math.abs(montantEnCI.floatValue()) < Math.abs(Float
                                                    .parseFloat(JANumberFormatter.deQuote(montCiBase))))) {
                                        montCiBase = montantEnCI.multiply(new BigDecimal(-1)).toString();
                                    }
                                } else if (new FWCurrency(montCiBase).isNegative()) {
                                    java.math.BigDecimal montantEnCI = ci.getSommeParAnnee(numAvs, "", genre,
                                            mySortie.getAnnee(), CIEcriture.CS_CODE_AMORTISSEMENT + ", "
                                                    + CIEcriture.CS_CODE_MIS_EN_COMTE);
                                    montantEnCI = montantEnCI.add(ci.getSommeParAnnee(numAvs, "",
                                            CIEcriture.CS_CIGENRE_7, mySortie.getAnnee(),
                                            CIEcriture.CS_CODE_AMORTISSEMENT + ", " + CIEcriture.CS_CODE_MIS_EN_COMTE));
                                    if (!JadeStringUtil.isEmpty(montCiBase)
                                            && (montantEnCI.floatValue() < Math.abs(Float.parseFloat(JANumberFormatter
                                                    .deQuote(montCiBase))))) {
                                        montCiBase = "-" + montantEnCI.toString(); // Au
                                    }
                                }
                                String code = null;
                                if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
                                    code = CIEcriture.CS_CODE_MIS_EN_COMTE;
                                } else {
                                    // Test si bénéfice en capital
                                    CPDonneesBase base = new CPDonneesBase();
                                    base.setSession(getSession());
                                    base.setIdDecision(decision.getIdDecision());
                                    base.retrieve(getTransaction());
                                    if (!base.isNew()
                                            && CPDonneesBase.CS_BENEFICE_CAP.equalsIgnoreCase(base
                                                    .getSourceInformation())) {
                                        moisDebut = "77";
                                        moisFin = "77";
                                    }
                                }
                                if (!JadeStringUtil.isEmpty(montCiBase) && !montCiBase.equalsIgnoreCase("0.00")
                                        && !montCiBase.equalsIgnoreCase("-0.00") && !montCiBase.equalsIgnoreCase("0")
                                        && !montCiBase.equalsIgnoreCase("-0")) {
                                    ci.verifieCI(getTransaction(), mySortie.getIdAffiliation(), numAvs, moisDebut,
                                            moisFin, mySortie.getAnnee(), montCiBase, genre,
                                            CICompteIndividuelUtil.MODE_DIRECT, code,
                                            CPToolBox._returnCodeSpecial(decision), "");
                                }
                            }
                            // mise à jour de la communication fiscale
                            process.majCommunicationFiscale(decision, true, null, null);
                            // Mettre la décision inactive - Evite problème
                            // nullPointerExcepion lors la mise à jour des CI
                            // des décisions qui sont dans le même passage et
                            // complémentaire (CCVD)
                            if (!decision.isNew()) {
                                decision.setActive(Boolean.FALSE);
                                decision.update();
                            }
                        }
                    }
                    // On efface plus la sortie, mais on met a jour le flag, afin de garder un historique
                    // mySortie.delete(this.getTransaction());
                    mySortie.setChecked(false);
                    mySortie.update(getTransaction());
                    setAffiliation(getAffiliationPrec());
                    if (!getSession().hasErrors() && !getTransaction().hasErrors()) {
                        // Mise à jour code actif des décisions de l'année
                        majCodeActifDecision(decision.getAnneeDecision());
                    }
                    if (getSession().hasErrors()) {
                        getMemoryLog().logMessage(
                                getTransaction().getErrors().toString() + " - " + mySortie.getNoAffilie() + " - "
                                        + mySortie.getAnnee(), FWMessage.INFORMATION, "");
                        getTransaction().rollback();
                    }
                    if (getTransaction().hasErrors()) {
                        getMemoryLog().logMessage(
                                getTransaction().getErrors().toString() + " - " + mySortie.getNoAffilie() + " - "
                                        + mySortie.getAnnee(), FWMessage.INFORMATION, "");
                        getTransaction().rollback();
                    } else {
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(mySortie.getNoAffilie() + " - " + mySortie.getAnnee(), FWMessage.ERREUR,
                            e.toString());
                } finally {
                    for (Iterator iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                        messages.add(iter.next());
                    }
                    getMemoryLog().clear();
                }
            }
        } catch (Exception e) {
            this._addError(getSession().getLabel("CP_MSG_0120") + " " + "(" + e.toString() + ")");
        }
        return !isOnError();
    }

    /**
     * Traitement des décisions.
     * 
     * @param decManager
     * @param passageFact
     * @throws Exception
     */
    private void processDecisions() throws Exception {
        // Permet d'afficher les données
        // this.setProgressScaleValue();

        // compteur du progress
        long progressCounter = -1;
        BStatement statement = null;

        // Lire les décisions du passage
        CPDecisionManager decManager = new CPDecisionManager();
        initDecisionTiersManager(decManager);
        /*
         * ******************************************************************
         */
        decManager.changeManagerSize(BManager.SIZE_NOLIMIT);
        BTransaction transactionLecture = (BTransaction) getSession().newTransaction();
        try {
            transactionLecture.openTransaction();
            statement = decManager.cursorOpen(transactionLecture);
            CPDecision myDecision = null;

            int nbreCas = 0;
            boolean finProcess = false;
            while (((myDecision = (CPDecision) decManager.cursorReadNext(statement)) != null) && !finProcess
                    && (!myDecision.isNew()) && !isAborted()) {
                nbreCas = nbreCas + 1;
                myDecision.attachTransactionForExternalService(getTransaction());
                try {
                    setProgressCounter(progressCounter++);
                    setDecision(myDecision);
                    // Traitement pour une décision
                    _executeProcessParDecision();

                    if (getSession().hasErrors() || getTransaction().hasErrors()) {
                        getMemoryLog().logMessage(
                                myDecision.getAffiliation().getAffilieNumero() + " - " + myDecision.getAnneeDecision()
                                        + ": " + getTransaction().getErrors().toString(), FWMessage.INFORMATION,
                                this.getClass().getName()); // PO 6102
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                        getTransaction().disableSpy();
                        getDecision().setDernierEtat(CPDecision.CS_PB_COMPTABILISATION);
                        getDecision().setAffiliation(getAffiliation());
                        getDecision().wantCallValidate(false);
                        getDecision().update(getTransaction());
                        getTransaction().enableSpy();
                        getTransaction().commit();
                        // if (nbreCas==1){
                        // finProcess=true;
                        // }
                    } else if (isAborted()) {
                        getTransaction().rollback();
                    } else {
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage() + "\n" + myDecision.toString(), FWMessage.FATAL,
                            this.getClass().getName());
                    getTransaction().rollback();
                } finally {
                    for (Iterator iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                        messages.add(iter.next());
                    }
                    getMemoryLog().clear();
                }
            }
        } finally {
            try {
                try {
                    decManager.cursorClose(statement);
                } finally {
                    if (transactionLecture != null) {
                        transactionLecture.closeTransaction();
                    }
                    statement = null;
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            }
        }
    }

    private boolean rechercheAutreAffilie() throws Exception {
        // Contrôler le montant // à celui existant au CI - Evite d'avoir le
        // message: Le revenu doit être positif... Ceci concerne
        // des cas incoherant entre le montant au Ci et les décisions (ex pb de
        // migration)
        // Si autre décision active avec autre num affilié -> ne prendre que la
        // somme pour le num affilié (ex: cas assisté)
        // Test si autre décision active avec un autre n° d'affilié
        boolean autreNumAffilie = false;
        CPDecisionAffiliationManager dMng = new CPDecisionAffiliationManager();
        dMng.setSession(getSession());
        dMng.setForIdTiers(decision.getIdTiers());
        dMng.setForAnneeDecision(decision.getAnneeDecision());
        dMng.setForExceptNoAffilie(decision.getAffiliation().getAffilieNumero());
        dMng.setIsActiveOrRadie(Boolean.TRUE);
        dMng.find();
        if (dMng.getSize() > 0) {
            autreNumAffilie = true;
        }
        return autreNumAffilie;
    }

    /**
     * Recherche libelle et date de facturation du passage de facturation.
     * 
     * @param passageFact
     * @throws Exception
     */
    private void retrievePassageFact(FAPassage passageFact) throws Exception {
        passageFact.setSession(getSession());
        passageFact.setIdPassage(getIdPassage());
        passageFact.retrieve(getTransaction());
        setPassageFacturation(passageFact);
    }

    /**
     * Recherche du journalAnneeLimite qui doit être unique et de type COTISATIONS PERSONNELLES.
     * 
     * @param annee
     * @throws Exception
     */
    private void searchJournalAnneeLimite(String annee) throws Exception {
        BSession pavoSession = (BSession) GlobazSystem.getApplication(CIApplication.DEFAULT_APPLICATION_PAVO)
                .newSession(getSession());

        CIJournalManager jourManager = new CIJournalManager();
        jourManager.setSession(pavoSession);
        // jourManager.getSession().connectSession(this.getSession());
        jourManager.setForAnneeCotisation(annee);
        jourManager.setForIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
        try {
            jourManager.find(getTransaction());
            // prendre le premier journal du manager
            if (jourManager.getSize() > 0) {
                journalAnneeLimite = (CIJournal) jourManager.getEntity(0);
                journalAnneeLimite.setSession(sessionPavo);
            } else {
                // On créer un journal d'inscription CI
                CIJournal ciJournal = new CIJournal();
                ciJournal.setSession(pavoSession);
                ciJournal.setAnneeCotisation(annee);
                ciJournal.setIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
                try {
                    ciJournal.add(getTransaction());
                    journalAnneeLimite = ciJournal;
                } catch (Exception e2) {
                    this._addError(getTransaction(), getSession().getLabel("CP_MSG_0117") + " " + getAnneeLimite()
                            + " " + getSession().getLabel("CP_MSG_0117A"));
                }

            }
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0118") + " " + getAnneeLimite() + " "
                    + getSession().getLabel("CP_MSG_0117A"));
        }
    }

    /**
     * @param affiliation
     */
    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * @param affiliation
     */
    public void setAffiliationPrec(AFAffiliation affiliation) {
        affiliationPrec = affiliation;
    }

    /**
     * Sets the anneeLimite.
     * 
     * @param anneeLimite
     *            The anneeLimite to set
     */
    public void setAnneeLimite(int anneeLimite) {
        this.anneeLimite = anneeLimite;
    }

    public void setDateNNSS(String dateNNSS) {
        this.dateNNSS = dateNNSS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 10:02:40)
     * 
     * @param newDecision
     *            globaz.phenix.db.principale.CPDecision
     */
    public void setDecision(globaz.phenix.db.principale.CPDecision newDecision) {
        decision = newDecision;
    }

    /**
     * @param string
     */
    public void setIdAffiliationPrec(String string) {
        idAffiliationPrec = string;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 11:15:23)
     * 
     * @param newIdPassage
     *            java.lang.String
     */
    public void setIdPassage(java.lang.String newIdPassage) {
        idPassage = newIdPassage;
    }

    /**
     * @param string
     */
    public void setIdTiersPrec(String string) {
        idTiersPrec = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 14:10:18)
     * 
     * @param newJournal
     *            globaz.pavo.db.inscriptions.CIJournal
     */
    public void setJournal(globaz.pavo.db.inscriptions.CIJournal newJournal) {
        journal = newJournal;
    }

    /**
     * Sets the journalAnneeLimite.
     * 
     * @param journalAnneeLimite
     *            The journalAnneeLimite to set
     */
    public void setJournalAnneeLimite(globaz.pavo.db.inscriptions.CIJournal journalAnneeLimite) {
        this.journalAnneeLimite = journalAnneeLimite;
    }

    public void setOptionReComptabiliser(boolean optionReComptabiliser) {
        this.optionReComptabiliser = optionReComptabiliser;
    }

    /**
     * @param passage
     */
    public void setPassageFacturation(FAPassage passage) {
        passageFacturation = passage;
    }

    /**
     * Sets the tiers.
     * 
     * @param tiers
     *            The tiers to set
     */
    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    public void setTransfertAnneePreEncodee(boolean transfertAnneePreEncodee) {
        this.transfertAnneePreEncodee = transfertAnneePreEncodee;
    }

    public void setWantMajCI(boolean wantMajCI) {
        this.wantMajCI = wantMajCI;
    }

}
