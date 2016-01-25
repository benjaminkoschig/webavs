package globaz.phenix.process.decision;

import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.divers.CPTableAFI;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.db.divers.CPTableNonActif;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationPassageManager;
import globaz.phenix.db.principale.CPDecisionNonComptabilisee;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.toolbox.CPToolBox;
import java.util.Iterator;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (18.11.2004 14:00:00)
 * 
 * @author: acr
 */
public class CPProcessReporterDecisionPreEncodee extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    AFAffiliation aff = null;
    // private int nbDecisionsComptabilises = 0;
    private String anneeATraiter = "";

    private boolean auMoinsUneError = false;
    private float cotiAfiMinimum = 0;
    private float cotiIndMinimum = 0;
    private float cotiNacMinimum = 0;
    private float cotiNEAnnuelleMin = 0;
    private String dateDebutAnnee = "";
    private String dateFinAnnee = "";
    private java.lang.String idPassage = "";
    private int maxScale = 0;
    private Vector messages = new Vector();
    private int modeArrondiFadCotPers;
    private String numAffTraite = "";
    private long progressCounter = 0;
    private boolean revAf = false;
    private BStatement statement = null;

    // private CIJournal journal = null;
    // private String erreurs = "";
    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessReporterDecisionPreEncodee() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessReporterDecisionPreEncodee(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessReporterDecisionPreEncodee(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Traitement de facturation pour les décisions qui ont été prises pour l'année suivante et qui sont déjà à l'état
     * comptabilisé mais qui n'ont pas encore été facturées. Ce traitement utilise un paramêtre "année limite" qui doit
     * être modifié avant son exécution. Date de création : (19.11.2004 08:00:00)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        CPDecisionNonComptabilisee decisionAffiliation = new CPDecisionNonComptabilisee();
        CPDecisionAffiliationPassageManager manager = null;
        BTransaction transactionLecture = null;

        try {
            int anneeLimite = Integer.parseInt(CPToolBox.anneeLimite(getTransaction()));
            if (JadeStringUtil.isIntegerEmpty(Integer.toString(anneeLimite))) {
                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0116"), FWMessage.FATAL,
                        this.getClass().getName());
                return false;
            }
            // initialisation du manger
            manager = initManager(anneeLimite);
            // Initialisation des variables de travail
            initVariable(anneeLimite, manager);
            // Init statement
            statement = initStatement(manager, transactionLecture);

            while (((decisionAffiliation = (CPDecisionNonComptabilisee) manager.cursorReadNext(statement)) != null)
                    && (!decisionAffiliation.isNew()) && !isAborted()) {
                // chargement de la décision
                CPDecision decision = loadDecision(decisionAffiliation);
                // chargement de la cotisation avs
                AFCotisation cotiAvs = loadCotisationAvs(decision);

                CPProcessCalculCotisation processCalcul = new CPProcessCalculCotisation();
                processCalcul.setTransaction(getTransaction());
                processCalcul.setAffiliation(decision.loadAffiliation());
                if (((processCalcul.getAffiliation() != null)
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), processCalcul.getAffiliation()
                                .getDateDebut(), dateFinAnnee) && (BSessionUtil.compareDateFirstGreater(getSession(),
                        processCalcul.getAffiliation().getDateFin(), dateDebutAnnee)))
                        || JadeStringUtil.isBlankOrZero(processCalcul.getAffiliation().getDateFin())) {
                    // Si même affiliation => plusieurs décisions préencodées : ignorer la 2ème et la mettre à l'état
                    // comptabilisé.
                    if (numAffTraite.equalsIgnoreCase(processCalcul.getAffiliation().getAffilieNumero())) {
                        decision.setDernierEtat(CPDecision.CS_FACTURATION);
                        decision.setImpression(Boolean.FALSE);
                        decision.update(getTransaction());
                    } else {
                        // Recalcul de la décision
                        calculDecision(decision, cotiAvs, processCalcul);
                        // Tester si il y a eu des cotisations de calculé
                        testPresenceCotisation(getTransaction(), decision);
                        // Création des remarques
                        if (getTransaction().hasErrors() == false) {
                            createRemark(decision, processCalcul);
                        }
                    }
                } else {
                    // Cas encodés en avance puis radiéS => les remettre à
                    // l'état calcul, enlever le n° de passage pour qu'il ne
                    // soit pas imprimé et comptabilisé.
                    traiterDecisionRadieeOuEnErreur(decision);
                }

                manageOrCommit(decision);

                setProgressCounter(progressCounter++);
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
        } finally {
            try {
                try {
                    manager.cursorClose(statement);
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
        for (Iterator<?> iter = messages.iterator(); iter.hasNext();) {
            getMemoryLog().getMessagesToVector().add(iter.next());
        }
        if (auMoinsUneError) {
            getMemoryLog().logMessage(getSession().getLabel("CP_CAS_MANUEL"), FWMessage.INFORMATION, "");
        }
        return getMemoryLog().hasErrors();
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
        }
        if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0102"));
        } else {
            FAPassage passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getIdPassage());
            passage.retrieve();
            if (passage.getStatus().equals(FAPassage.CS_ETAT_COMPTABILISE) || passage.isEstVerrouille()) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0177"));
            } else {
                // PO 3916 - Tester si module cot. pers de précent dans le passage sélectionné
                testPresenceModulePhenix();
            }
        }
    }

    protected void calculDecision(CPDecision decision, AFCotisation cotiAvs, CPProcessCalculCotisation processCalcul)
            throws Exception {
        numAffTraite = processCalcul.getAffiliation().getAffilieNumero();
        CPDonneesBase donneeBase = initVariableProcessCalcul(decision, cotiAvs, processCalcul);
        // Calcul selon le type
        if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
            processCalcul.calculBonifMiseEnCompte(processCalcul);
            // Pas la peine de lancer le calcul si l'idCoti
            // vela est vide
        } else if (decision.isNonActif() && (cotiAvs != null)) {
            processCalcul.calculNonActif(processCalcul);
        } else if (decision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                || decision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)
                || decision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_RENTIER)
                || decision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
            if (revAf && !JadeStringUtil.isEmpty(donneeBase.getRevenuAutre1())) {
                // Calcul en ignorant revenu autre 1 et
                // en ne créant pas la coti AF
                processCalcul.calculIndependant(processCalcul, 1);
                // Calcul en tenant compte uniquement de
                // revenu autre 1 et en créant
                // uniquement la coti AF
                processCalcul.calculIndependant(processCalcul, 2);
            } else {
                // calcul normal
                processCalcul.calculIndependant(processCalcul, 0);
            }

        } else {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0135") + decision.getAnneeDecision()
                    + getSession().getLabel("CP_MSG_0135A") + " " + decision.loadAffiliation().getAffilieNumero()
                    + " - " + decision.loadTiers().getDescriptionTiers());
        }
    }

    protected void createRemark(CPDecision decision, CPProcessCalculCotisation processCalcul) throws Exception {
        if (!getTransaction().hasErrors()) {
            processCalcul.createRemarqueAutomatique(processCalcul.getTransaction(), decision);
            decision.setSession(getSession());
            decision.setIdPassage(getIdPassage());
            decision.setDernierEtat(CPDecision.CS_VALIDATION);
            decision.update(processCalcul.getTransaction());
        }
    }

    @Override
    protected java.lang.String getEMailObject() {
        if (!isAborted() && !isOnError()) {
            return getSession().getLabel("CPREPORT") + "  " + anneeATraiter + " "
                    + getSession().getLabel("CPPROCESSOK");
        } else {
            return getSession().getLabel("CPREPORT") + "  " + anneeATraiter + " "
                    + getSession().getLabel("CPPROCESSKO");
        }
    }

    /**
     * Returns the idPassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    protected CPDecisionAffiliationPassageManager initManager(int anneeLimite) {
        CPDecisionAffiliationPassageManager manager = new CPDecisionAffiliationPassageManager();
        manager.setSession(getSession());
        manager.setForAnneeDecision(Integer.toString(anneeLimite));
        manager.setForEtat(CPDecision.CS_VALIDATION);
        manager.setForInStatus(FAPassage.CS_ETAT_COMPTABILISE);
        manager.orderByNoAffilie();
        manager.orderByIdDecisionDesc();
        return manager;
    }

    protected BStatement initStatement(CPDecisionAffiliationPassageManager manager, BTransaction transactionLecture)
            throws Exception {
        transactionLecture = (BTransaction) getSession().newTransaction();
        transactionLecture.openTransaction();
        statement = manager.cursorOpen(transactionLecture);
        return statement;
    }

    protected void initVariable(int anneeLimite, CPDecisionAffiliationPassageManager manager) throws Exception,
            NumberFormatException {
        maxScale = manager.getCount(getTransaction());
        if (maxScale > 0) {
            setProgressScaleValue(maxScale);
        } else {
            setProgressScaleValue(1);
        }
        modeArrondiFadCotPers = CPToolBox.getModeArrondiFad(getTransaction());
        dateDebutAnnee = "01.01." + anneeLimite;
        dateFinAnnee = "31.12." + anneeLimite;
        cotiIndMinimum = CPTableIndependant.getCotisationMinimum(getTransaction(), dateDebutAnnee);
        cotiAfiMinimum = CPTableAFI.getCotisationMinimum(getTransaction(), dateDebutAnnee);
        cotiNEAnnuelleMin = new Float(CPTableNonActif.getRevenuMin(getSession(), Float.toString(anneeLimite)))
                .floatValue();
        cotiNacMinimum = CPTableNonActif.getCotisationMin(getSession(), Float.toString(anneeLimite));
        revAf = ((CPApplication) getSession().getApplication()).isRevenuAf();
    }

    protected CPDonneesBase initVariableProcessCalcul(CPDecision decision, AFCotisation cotiAvs,
            CPProcessCalculCotisation processCalcul) throws Exception {
        CPDonneesBase donneeBase = new CPDonneesBase();
        donneeBase.setIdDecision(decision.getIdDecision());
        donneeBase.setSession(getSession());
        donneeBase.retrieve();

        processCalcul.setModeArrondiFad(modeArrondiFadCotPers);
        processCalcul.setISession(getSession());
        processCalcul.setTransaction(getTransaction());
        processCalcul.setCalculForReprise(true);
        processCalcul.setTiers(decision.loadTiers());
        processCalcul.setIdDecision(decision.getIdDecision());
        processCalcul.setCotiAf(cotiAvs);
        processCalcul.setDonneeBase(donneeBase);
        processCalcul.setDecision(decision);
        processCalcul.setCotiAfiMinimum(cotiAfiMinimum);
        processCalcul.setCotiIndMinimum(cotiIndMinimum);
        processCalcul.setRevenuNacMin(cotiNEAnnuelleMin);
        processCalcul.setCotiNacMinimum(cotiNacMinimum);
        processCalcul.suppressionAnciennesDonnees();
        return donneeBase;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    protected AFCotisation loadCotisationAvs(CPDecision decision) throws Exception {
        aff = new AFAffiliation();
        aff.setSession(getSession());
        AFCotisation cotiAvs = aff._cotisation(getTransaction(), decision.getIdAffiliation(),
                CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AVS_AI, decision.getDebutDecision(),
                decision.getFinDecision(), 1);
        return cotiAvs;
    }

    protected CPDecision loadDecision(CPDecisionNonComptabilisee decisionAffiliation) throws Exception {
        CPDecision decision = new CPDecision();
        decision.setSession(getSession());
        decision.setIdDecision(decisionAffiliation.getIdDecision());
        decision.retrieve();
        return decision;
    }

    protected void manageOrCommit(CPDecision decision) throws Exception {
        if (getTransaction().hasErrors() || getSession().hasErrors()) {
            auMoinsUneError = true;
            if (getTransaction().hasErrors()) {
                getMemoryLog().logMessage(
                        getTransaction().getErrors().toString() + " - " + getSession().getLabel("NUM_AFFILIE") + ": "
                                + decision.loadAffiliation().getAffilieNumero() + " - " + decision.getIdDecision(),
                        FWMessage.ERREUR, "");
            } else if (getSession().hasErrors()) {
                getMemoryLog().logMessage(
                        getSession().getErrors().toString() + " - " + getSession().getLabel("NUM_AFFILIE") + ": "
                                + decision.loadAffiliation().getAffilieNumero() + " - " + decision.getIdDecision(),
                        FWMessage.ERREUR, "");
            }
            getTransaction().rollback();
            getTransaction().clearErrorBuffer();
            // En cas d'erreur remettre le cas à l'état calcul
            traiterDecisionRadieeOuEnErreur(decision);
        }
        getTransaction().commit();
        for (Iterator<?> iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
            messages.add(iter.next());
        }
        getMemoryLog().clear();
    }

    /**
     * Sets the idPassage.
     * 
     * @param idPassage
     *            The idPassage to set
     */
    public void setIdPassage(java.lang.String idPassage) {
        this.idPassage = idPassage;
    }

    protected void testPresenceCotisation(BTransaction transaction, CPDecision decision) throws Exception {
        if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
            if (decision.getImpression().equals(Boolean.TRUE)) {
                CPCotisationManager cotiManager = new CPCotisationManager();
                cotiManager.setSession(getSession());
                cotiManager.setForIdDecision(decision.getIdDecision());
                if (cotiManager.getCount() == 0) {
                    this._addError(transaction, decision.getAffiliation().getAffilieNumero() + " : "
                            + getSession().getLabel("CP_MSG_0178"));
                }
            }
        }
    }

    /**
     * Tester si module cot. pers de précent dans le passage sélectionné - PO 3916
     * 
     * @author hna
     * @throws Exception
     */
    protected void testPresenceModulePhenix() throws Exception {
        FAPassageModuleManager modPassManager = new FAPassageModuleManager();
        modPassManager.setSession(getSession());
        modPassManager.setForIdPassage(getIdPassage());
        Boolean isSeprationIndNac = Boolean.FALSE;
        try {
            isSeprationIndNac = new Boolean(GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                    .getProperty(FAApplication.SEPARATION_IND_NA));
        } catch (Exception e) {
            isSeprationIndNac = Boolean.FALSE;
        }
        if (isSeprationIndNac) {
            modPassManager.setInTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS_NAC + ", "
                    + FAModuleFacturation.CS_MODULE_COT_PERS_IND + ", " + FAModuleFacturation.CS_MODULE_COT_PERS);
        } else {
            modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS);
        }
        modPassManager.find();
        if (modPassManager.getSize() == 0) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0193"));
        }
    }

    protected void traiterDecisionRadieeOuEnErreur(CPDecision decision) throws Exception {
        decision.setIdPassage("");
        decision.setDernierEtat(CPDecision.CS_CALCUL);
        decision.update(getTransaction());
    }

}
