package globaz.phenix.process.acompte;

import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.divers.CPPeriodeFiscaleManager;
import globaz.phenix.db.divers.CPTableAFI;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.db.divers.CPTableNonActif;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.toolbox.CPToolBox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CPProcessAcompteCreationAnnuelle extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean acompteSeBasantSurDerniereDecision = false;
    private AFCotisation cotiAf = null;
    private float cotiAfiMinimum = 0;

    private float cotiIndMinimum = 0;

    private float cotiNacMinimum = 0;
    private float cotiNEAnnuelleMin = 0;
    private String dateAvs = "";

    private CPDecisionViewBean decisionLue = new CPDecisionViewBean();
    private boolean detailCalcul = false;
    private boolean errorProcess = false;
    private java.lang.String forAnneeReprise = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String forPeriodicite = "";

    private java.lang.String fromAffilieDebut = "";

    private java.lang.String fromAffilieFin = "";
    // Param�tres utiliser par l'�cran du decisionRepriseViewBean
    private java.lang.String idPassage = "";
    private List<String> messages = new ArrayList<String>();
    private String montantAnnuel = "0";
    private String montantMensuel = "0";
    private String montantSemestriel = "0";
    private String montantTrimestriel = "0";
    private java.lang.String nbMoisExercice1 = "";
    private java.lang.String nbMoisExercice2 = "";
    private String saveAnneeDebut = "";
    private String saveAnneeFin = "";
    private String saveDateFortune = "";
    private String saveIdIfd = "";
    private String saveNumIfd = "";
    private boolean traitementIndependant = false;
    private java.lang.String typeRevenu = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessAcompteCreationAnnuelle() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            globaz.framework.process.BProcess
     */
    public CPProcessAcompteCreationAnnuelle(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilis�e par le process
     */
    public CPProcessAcompteCreationAnnuelle(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Calcul des cotisations de reprise pour un non actif Date de cr�ation : (25.02.2002 12:58:23)
     * 
     * @param process
     *            BProcess le processus d'ex�cution
     */
    public void _calculReprise(CPDecisionViewBean orgDecision, CPDecisionViewBean copyDecision, boolean revAf) {

        // Sous contr�le d'exception
        try {
            CPDonneesBase donneeBase = determineDonneeDeBasePourCalcul(orgDecision, copyDecision);
            // PO 5104
            copyDecision.setRevenu1(donneeBase.getRevenu1());
            copyDecision.setRevenuAutre1(donneeBase.getRevenuAutre1());
            copyDecision.setCapital(donneeBase.getCapital());
            copyDecision.setDebutExercice1(donneeBase.getDebutExercice1());
            copyDecision.setFinExercice1(donneeBase.getFinExercice1());
            // Calcul des cotisations
            calculerCotisation(copyDecision, revAf, donneeBase);
        } catch (Exception e) {

        }
    }

    public boolean _executeBoucleReprise(CPDecisionManager deciManager) {
        BStatement statement = null;
        try {
            // Sous controle d'exceptions
            CPAcompteCreationAnnuelle decisionReprise = null;
            CPDecisionViewBean decision = new CPDecisionViewBean();
            decision.setSession(getSession());
            long progressCounter = 0;
            int maxScale = deciManager.getCount(getTransaction());
            if (maxScale > 0) {
                setProgressScaleValue(maxScale);
            } else {
                setProgressScaleValue(1);
            }
            // disabler le spy
            getTransaction().disableSpy();
            // it�rer sur toutes les affiliations
            statement = deciManager.cursorOpen(getTransaction());
            String savIdAffiliation = "";
            // Recherche si revenuAutre = revenu AF (ex cas sp�ciaux AF pour GE)
            boolean revAf = ((CPApplication) getSession().getApplication()).isRevenuAf();
            boolean aTraite = true;
            while (((decisionReprise = (CPAcompteCreationAnnuelle) deciManager.cursorReadNext(statement)) != null)
                    && (!decisionReprise.isNew()) && !isAborted()) {
                setCotiAf(null);
                // Regarder s'il n'y a pas d�j� une d�cision pour l'ann�e
                // acompte
                aTraite = existanceDecisionPourAnnee(deciManager, decisionReprise);
                // Pour les cas de plusieurs d�cisions dans l'ann�e -> si m�me
                // idAffiliation-> ne rien faire
                if (!savIdAffiliation.equalsIgnoreCase(decisionReprise.getIdAffiliation()) && aTraite) {
                    // if (!savIdAffiliation.equalsIgnoreCase(decisionReprise.getIdAffiliation())) {
                    // charger la vraie d�cision � partir du fake entity
                    decision.setAffiliation(null);
                    decision.setIdDecision(decisionReprise.getIdDecision());
                    decision.setProcessExterne(Boolean.TRUE);
                    try {
                        decision.retrieve(getTransaction());
                        // Si d�cision de remise => aller rechercher la d�cision pr�c�dente
                        if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) {
                            returnDecisionPrecedente(decision);
                        }
                        if (!getTransaction().hasErrors()) {
                            // Tests si la d�cision appartient bien au genre du
                            // manager
                            // Ex: Si on traite les ind�pendant, la d�cision ne
                            // doit pas �tre de type non actif.
                            // Ce test est n�cessaire pour les caisses qui
                            // n'avaient pas d'historique d'affiliation et qui
                            // se retrouve
                            // apr�s migration avec des d�cision non actif sous
                            // une affiliation ind�pendant.
                            if ((traitementIndependant && !decision.isNonActif())
                                    || (!traitementIndependant && decision.isNonActif())) {
                                _executeProcessParDecision(decision, decisionReprise, revAf);
                            }
                        }
                    } catch (Exception e) {
                        this._addError(
                                getTransaction(),
                                getSession().getLabel("CP_MSG_0130") + " " + decisionReprise.getNoAffilie() + " - "
                                        + e.getMessage());
                    }
                    setProgressCounter(progressCounter++);
                    // Sauvegarde de l'id affiliation que l'on vient de traiter
                    savIdAffiliation = decisionReprise.getIdAffiliation();
                    if (getTransaction().hasErrors()) {
                        getMemoryLog().logMessage(
                                getTransaction().getErrors().toString() + " - " + decisionReprise.getNoAffilie(),
                                FWMessage.ERREUR, "");
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                    } else if (getSession().hasErrors()) {
                        getMemoryLog().logMessage(
                                getSession().getErrors().toString() + " - " + decisionReprise.getNoAffilie(),
                                FWMessage.ERREUR, "");
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                    } else {
                        getTransaction().commit();
                    }
                }
            }
            // r�tablir le contr�le du spy
            getTransaction().enableSpy();
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
        } finally {
            try {
                deciManager.cursorClose(statement);
                deciManager = null;
                statement = null;
                for (Iterator<?> iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                    messages.add((String) iter.next());
                }
                getMemoryLog().clear();
            } catch (Exception e) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0132") + e.getMessage());
            }
        }
        return !isOnError();
    }

    /**
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        try {
            // type de revenu depuis les properties
            String typeRevenu = ((CPApplication) getSession().getApplication()).getTypeRevenu();
            detailCalcul = ((CPApplication) getSession().getApplication()).isAcompteDetailCalcul();
            String modeCalcul = "";
            try {
                modeCalcul = FWFindParameter.findParameter(getTransaction(), "10503000", "CPVMODACP", "0", "", 2);
            } catch (Exception e) {
                modeCalcul = "";
            }
            if ("1".equalsIgnoreCase(modeCalcul)) {
                acompteSeBasantSurDerniereDecision = true;
            } else {
                acompteSeBasantSurDerniereDecision = false;
            }
            setTypeRevenu(typeRevenu);
            // recherche p�riode IFD d�finitive correspondant � l'ann�e de l'acompte
            CPPeriodeFiscaleManager perFis = new CPPeriodeFiscaleManager();
            perFis.setSession(getSession());
            perFis.setForAnneeDecisionDebut(getForAnneeReprise());
            perFis.find();
            if (perFis.getSize() > 0) {
                CPPeriodeFiscale periode = (CPPeriodeFiscale) perFis.getEntity(0);
                saveNumIfd = periode.getNumIfd();
                saveIdIfd = periode.getIdIfd();
                saveAnneeDebut = periode.getAnneeRevenuDebut();
                saveAnneeFin = periode.getAnneeRevenuFin();
            } else {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0059"));
                errorProcess = true;
                return isOnError();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0127"));

        }

        // Annul� par le PO 8518
        // - T�l de Mr Burki - Mettre la date de fortune au 31.12 de
        // l'ann�e pr�c�dant l'acompte
        // int anneeFortune = Integer.parseInt(this.getForAnneeReprise()) - 1;
        // this.saveDateFortune = "31.12." + anneeFortune;
        //
        // PO 8518 : Mettre au 31.12. de l'ann�e de l'acompte (idem encodage mabuel)
        saveDateFortune = "31.12." + getForAnneeReprise();

        /*
         * rechercher toutes les d�cisions dont les affiliations sont actives (date affiliation vide ou date de fin
         * sup�rieure 01.01.<ANNEE ECRAN> Attention: l'entity renvoy� est CPAcompteCreationAnnuelle (fake entity)
         */
        if (!CPDecision.CS_INDEPENDANT.equalsIgnoreCase(forGenreAffilie)) {
            // -----------------------------------------------------------------------------------
            // 1ere partie : Non Actif (reprise cas normaux) - sans mise en
            // compte
            // ----------------------------------------------------------------------------------
            traitementIndependant = false;
            CPDecisionManager deciManager = initManagerForNonActif();
            setMontantMinimumNonActif();
            _executeBoucleReprise(deciManager);
        }
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(forGenreAffilie)
                && !CPDecision.CS_ETUDIANT.equalsIgnoreCase(forGenreAffilie)) {
            // -----------------------------------------------------------------------------------
            // 3�me partie : Independant (reprise cas normaux) - sans
            // imputations
            // ----------------------------------------------------------------------------------
            traitementIndependant = true;
            CPDecisionManager deciManager2 = initManagerForIndependant();
            setMontantMinimumIndependant();
            _executeBoucleReprise(deciManager2);
        }
        // Remettre les erreurs des process dans le log
        for (Iterator<?> iter = messages.iterator(); iter.hasNext();) {
            getMemoryLog().getMessagesToVector().add(iter.next());
            errorProcess = true;

        }
        return !isOnError();
    }

    public boolean _executeProcessParDecision(CPDecisionViewBean orgDecision,
            CPAcompteCreationAnnuelle decisionReprise, boolean revAf) {

        // setIdDecision(orgDecision.getIdDecision());

        CPDecisionViewBean copyDecision = null;

        // Copier la d�cision
        try {
            copyDecision = (CPDecisionViewBean) orgDecision.clone();

            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            setCotiAf(aff._cotisation(getTransaction(), copyDecision.getIdAffiliation(),
                    CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AVS_AI, "01.01."
                            + getForAnneeReprise(), "31.12." + getForAnneeReprise(), 1));
            // PO 9183 - Prendre en compte les affili�s qui n'ont pas l'AVS
            if (getCotiAf() == null) {
                setCotiAf(aff._cotisation(getTransaction(), copyDecision.getIdAffiliation(),
                        CodeSystem.GENRE_ASS_PERSONNEL, "", "01.01." + getForAnneeReprise(), "31.12."
                                + getForAnneeReprise(), 1));
            }
            // Test sur la p�riodicit� et non dans le manager pour les assurances qui auraient une p�riodicit�
            // diff�rente de l'affiliation
            // (Ex cas ind�pendant et employeur (mensuelle pour cot. pers. mais trimestrielle pour paritaire
            if (JadeStringUtil.isBlankOrZero(getForPeriodicite())
                    || (!JadeStringUtil.isBlankOrZero(getForPeriodicite()) && ((getCotiAf() != null) && getCotiAf()
                            .getPeriodicite().equalsIgnoreCase(getForPeriodicite())))) {

                decisionLue = copyDecision;
                copyDecision = creationAcompte(decisionReprise, copyDecision);
                if (!getTransaction().hasErrors()) {
                    _calculReprise(orgDecision, copyDecision, revAf);
                }

                if (!getTransaction().hasErrors()) {
                    // Mettre � jour les remarques
                    createRemarqueDecision(copyDecision);
                }
                // PO 6172
                // Si la d�cision n'a pas de cotisation => supression sinon on met � jour l'�tat
                // Ce test ne peut pas �tre fait avant car il peu y avoir une assurance de d�fini qui ne donne pas de
                // calcul (ex: AC pour TSE)
                CPCotisationManager cotiM = new CPCotisationManager();
                cotiM.setSession(getSession());
                cotiM.setForIdDecision(copyDecision.getIdDecision());
                if (cotiM.getCount() == 0) {
                    copyDecision.delete(getTransaction());
                } else {
                    CPDecision decision = new CPDecision();
                    decision.setSession(getSession());
                    decision.setIdDecision(copyDecision.getIdDecision());
                    decision.retrieve();
                    if (!decision.isNew()) {
                        decision.setSpecification(copyDecision.getSpecification());
                        decision.setDernierEtat(CPDecision.CS_VALIDATION);
                        decision.update();
                    }
                }
            }

        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0133") + orgDecision.getIdDecision() + " -"
                    + e.getMessage());
        }

        return true;
    }

    /*
     * Cette m�thode est �quivalente � CPDecisionViewBean _initEcran
     */
    public CPDecisionViewBean _initDecision(CPDecisionViewBean copyDecision, CPAcompteCreationAnnuelle decisionReprise) {

        try {
            copyDecision.setImpression(Boolean.TRUE);
            copyDecision.setFacturation(Boolean.TRUE);
            copyDecision.setAnneeDecision(getForAnneeReprise());
            copyDecision.setDebutDecision("01.01." + getForAnneeReprise());
            copyDecision.setNumIfdDefinitif(saveNumIfd);
            copyDecision.setIdIfdDefinitif(saveIdIfd);
            copyDecision.setIdIfdProvisoire(saveIdIfd);
            copyDecision.setAnneeRevenuDebut(saveAnneeDebut);
            copyDecision.setAnneeRevenuFin(saveAnneeFin);
            // Cas �ge AVS
            int anneeAvs = JACalendar.getYear(dateAvs);
            int anneeDec = JACalendar.getYear(copyDecision.getAnneeDecision());
            // sinon mettre la date de fin de d�cision au 31.12.<annee reprise>
            // ou date de fin d'affiliation si cette derni�re est en cours
            // d'ann�e
            if (!JadeStringUtil.isIntegerEmpty(copyDecision.getAffiliation().getDateFin())
                    && BSessionUtil.compareDateFirstLower(getSession(), copyDecision.getAffiliation().getDateFin(),
                            "31.12." + getForAnneeReprise())) {
                copyDecision.setFinDecision(copyDecision.getAffiliation().getDateFin());
            } else {
                copyDecision.setFinDecision("31.12." + getForAnneeReprise());
            }
            // Mettre date max � l'�ge avs pour les non actifs.
            if ((anneeAvs == anneeDec) && copyDecision.isNonActif()
                    && BSessionUtil.compareDateFirstGreater(getSession(), copyDecision.getFinDecision(), dateAvs)) {
                copyDecision.setFinDecision(dateAvs);
            }
        } catch (Exception e) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0134") + " "
                    + copyDecision.loadAffiliation().getAffilieNumero() + " - " + e.getMessage());
        }
        return copyDecision;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
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
                // PO 3916
                FAPassageModuleManager modPassManager = new FAPassageModuleManager();
                modPassManager.setSession(getSession());
                modPassManager.setForIdPassage(getIdPassage());
                Boolean isSeprationIndNac = Boolean.FALSE;
                try {
                    isSeprationIndNac = new Boolean(GlobazSystem
                            .getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA).getProperty(
                                    FAApplication.SEPARATION_IND_NA));
                } catch (Exception e) {
                    isSeprationIndNac = Boolean.FALSE;
                }
                if (isSeprationIndNac) {
                    if (!CPDecision.CS_INDEPENDANT.equalsIgnoreCase(forGenreAffilie)) {
                        modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
                    } else {
                        modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS_IND);
                    }
                } else {
                    modPassManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS);
                }
                modPassManager.find();
                if (modPassManager.getSize() == 0) {
                    this._addError(getTransaction(), getSession().getLabel("CP_MSG_0193"));
                }
            }
        }
        if (JadeStringUtil.isIntegerEmpty(getForAnneeReprise())) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0126"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    protected void ajouterFranchiseAuRevenu(CPDecisionViewBean orgDecision, CPDonneesBase donneeBase)
            throws JAException, NumberFormatException, Exception {
        int anneeAvs = JACalendar.getYear(dateAvs);
        boolean exerciceSur2Annee = false;
        float mFranchise = Float.parseFloat(FWFindParameter.findParameter(getTransaction(), "10500030", "FRANCHISE",
                orgDecision.getDebutDecision(), "", 0));
        if (orgDecision.getTaxation().equalsIgnoreCase("A")) {
            mFranchise = mFranchise * 12;
        } else {
            // Recherche de l'�ge AVS
            int moisDebut = 0;
            int moisFin = 0;
            int anneeDebutExercice = JACalendar.getYear(orgDecision.getDebutExercice1());
            int anneeFinExercice = JACalendar.getYear(orgDecision.getFinExercice1());
            if ((anneeDebutExercice != anneeFinExercice) && orgDecision.getTaxation().equalsIgnoreCase("N")
                    && orgDecision.getDebutActivite().equals(new Boolean(true))) {
                exerciceSur2Annee = true;
            }
            if (exerciceSur2Annee) {
                if (BSessionUtil.compareDateBetweenOrEqual(getSession(), orgDecision.getDebutExercice1(),
                        orgDecision.getFinExercice1(), dateAvs)) {
                    moisDebut = JACalendar.getMonth(dateAvs) + 1;
                    moisFin = JACalendar.getMonth(orgDecision.getFinExercice1());
                    if (JACalendar.getYear(dateAvs) < JACalendar.getYear(orgDecision.getFinExercice1())) {
                        moisFin = moisFin + 12;
                    }
                } else {
                    moisDebut = JACalendar.getMonth(orgDecision.getDebutExercice1());
                    moisFin = JACalendar.getMonth(orgDecision.getFinExercice1()) + 12;
                }
            } else {
                moisFin = JACalendar.getMonth(orgDecision.getFinDecision());
                if (orgDecision.getAnneeDecision().equalsIgnoreCase(Integer.toString(anneeAvs))
                        && BSessionUtil.compareDateFirstLower(getSession(), orgDecision.getDebutDecision(), dateAvs)) {
                    moisDebut = JACalendar.getMonth(dateAvs) + 1;
                } else {
                    moisDebut = JACalendar.getMonth(orgDecision.getDebutDecision());
                }
            }
            // Calcul de la franchise
            mFranchise = mFranchise * ((moisFin - moisDebut) + 1);
            float revenuAvecFranchise = 0;
            if (!JadeStringUtil.isEmpty(donneeBase.getRevenu1())) {
                revenuAvecFranchise = Float.parseFloat(JANumberFormatter.deQuote(donneeBase.getRevenu1()));
            }
            revenuAvecFranchise = mFranchise + revenuAvecFranchise;
            donneeBase.setRevenu1(Float.toString(revenuAvecFranchise));
        }
    }

    protected void calculerCotisation(CPDecisionViewBean copyDecision, boolean revAf, CPDonneesBase donneeBase) {
        // Recherche mode d'arrondi pour les frais
        int modeArrondiFad = CPToolBox.getModeArrondiFad(getTransaction());
        // D�termination de la fortune - Test si fortune d�taill�e (ex: CFC)
        CPProcessCalculCotisation processCalcul = new CPProcessCalculCotisation();
        // Si il ny a pas d'assurance AVS, se baser sur l'affiliation
        // Ex: Cas de la FER qui n'a que l'AF (m�decin vaudois qui paye sa
        // cotisation � la caisse des m�decins)
        // if (cotiAf == null) {
        // aff.setAffiliationId(copyDecision.getIdAffiliation());
        // aff.retrieve();
        processCalcul.setAffiliation(copyDecision.loadAffiliation());
        // }
        // D�termination de la fortune - Test si fortune d�taill�e (ex: CFC)
        processCalcul.setISession(getSession());
        processCalcul.setTransaction(getTransaction());
        processCalcul.setCalculForReprise(true);
        processCalcul.setTiers(decisionLue.loadTiers());
        processCalcul.setIdDecision(copyDecision.getIdDecision());
        processCalcul.setCotiAf(getCotiAf());
        processCalcul.setDonneeBase(donneeBase);
        processCalcul.setModeArrondiFad(modeArrondiFad);
        processCalcul.setDecision(copyDecision);
        processCalcul.setCotiAfiMinimum(cotiAfiMinimum);
        processCalcul.setCotiIndMinimum(cotiIndMinimum);
        processCalcul.setRevenuNacMin(cotiNEAnnuelleMin);
        processCalcul.setCotiNacMinimum(cotiNacMinimum);
        // Calcul selon le type
        if (copyDecision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
            processCalcul.calculBonifMiseEnCompte(processCalcul);
            // Pas la peine de lancer le calcul si l'idCoti vela est vide
        } else { // if (cotiAf != null) {
            if (copyDecision.isNonActif()) {
                processCalcul.calculNonActif(processCalcul);
            } else if (copyDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                    || copyDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)
                    || copyDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_RENTIER)
                    || copyDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                if (revAf && !JadeStringUtil.isEmpty(copyDecision.getRevenuAutre1())) {
                    // Calcul en ignorant revenu autre 1 et en ne cr�ant pas
                    // la coti AF
                    processCalcul.calculIndependant(processCalcul, 1);
                    // Calcul en tenant compte uniquement de revenu autre 1
                    // et en cr�ant uniquement la coti AF
                    if (getCotiAf().getAssurance().getTypeAssurance()
                            .equalsIgnoreCase(CodeSystem.TYPE_ASS_COTISATION_AVS_AI)) {
                        processCalcul.calculIndependant(processCalcul, 2);
                    }
                } else {
                    // calcul normal
                    processCalcul.calculIndependant(processCalcul, 0);
                }

            } else {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0135") + copyDecision.getAnneeDecision()
                        + getSession().getLabel("CP_MSG_0135A") + " "
                        + decisionLue.loadAffiliation().getAffilieNumero() + " - "
                        + decisionLue.loadTiers().getDescriptionTiers());
            }
        }
    }

    protected void createRemarqueDecision(CPDecisionViewBean copyDecision) throws Exception {
        CPProcessCalculCotisation processCalcul = new CPProcessCalculCotisation();
        processCalcul.setISession(getSession());
        processCalcul.setTransaction(getTransaction());
        processCalcul.setTiers(copyDecision.loadTiers());
        processCalcul.setIdDecision(copyDecision.getIdDecision());
        processCalcul.setAffiliation(copyDecision.getAffiliation());
        processCalcul.setDecision(copyDecision);
        processCalcul.createRemarqueAutomatique(getTransaction(), copyDecision);
    }

    protected CPDecisionViewBean creationAcompte(CPAcompteCreationAnnuelle decisionReprise,
            CPDecisionViewBean copyDecision) throws Exception {
        copyDecision.setId("");
        copyDecision.setIdDecision("");
        copyDecision.setIdPassage(getIdPassage());
        // Initialiser la copyDecision avec les valeurs de la reprise
        dateAvs = copyDecision.loadTiers().getDateAvs();
        copyDecision = _initDecision(copyDecision, decisionReprise);
        // Si il y a la particularit� "Pas de demande de com. fiscale" =>
        // cr�er directement une d�cision de type d�finitive
        // au lieu d'acompte
        if (AFParticulariteAffiliation.existeParticularite(getTransaction(), copyDecision.getIdAffiliation(),
                CodeSystem.PARTIC_AFFILIE_SANS_COMM_FISC, copyDecision.getDebutDecision())) {
            copyDecision.setTypeDecision(CPDecision.CS_DEFINITIVE);
        } else {
            copyDecision.setTypeDecision(CPDecision.CS_ACOMPTE);
        }
        /* Assist� => d�cision d�finitive et pas d'int�r�t */
        if (CPToolBox
                .isAffilieAssiste(getTransaction(), copyDecision.getAffiliation(), copyDecision.getDebutDecision())) {
            copyDecision.setInteret(CAInteretMoratoire.CS_EXEMPTE);
            copyDecision.setTypeDecision(CPDecision.CS_DEFINITIVE);
        }
        copyDecision.setDateFacturation("");
        copyDecision.setActive(new Boolean(false));
        // vider l'id de la communication fiscale
        copyDecision.setIdCommunication("");
        // Responsable = user par d�faut
        copyDecision.setResponsable(CPToolBox.getUserByCanton(copyDecision.loadTiers().getIdCantonDomicile(),
                getTransaction()));
        // date d'information = date du jour
        copyDecision.setDateInformation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        copyDecision.setTaxation("N");
        copyDecision.setCotisation1("");
        copyDecision.setCotisation2("");
        copyDecision.setCotisationSalarie("");
        copyDecision.setRevenuCiForce("");
        copyDecision.setOpposition(Boolean.FALSE);
        copyDecision.setRecours(Boolean.FALSE);
        copyDecision.setPremiereAssurance(Boolean.FALSE);
        copyDecision.setDebutActivite(Boolean.FALSE);
        copyDecision.setRevenuCiForce0(new Boolean(false));
        copyDecision.setNombreMoisTotalDecision("0");
        copyDecision.setComplementaire(Boolean.FALSE);
        if (!copyDecision.isNonActif()) {
            copyDecision.setDebutExercice1(copyDecision.getDebutDecision());
            copyDecision.setFinExercice1(copyDecision.getFinDecision());
        } else {
            copyDecision.setDebutExercice1("");
            copyDecision.setFinExercice1("");
        }
        copyDecision.setSpecification("0");
        copyDecision.setSourceInformation(CPDonneesBase.CS_NOTRE_ESTIMATION);
        copyDecision.add(getTransaction());
        return copyDecision;
    }

    protected CPDonneesBase determineDonneeDeBasePourCalcul(CPDecisionViewBean orgDecision,
            CPDecisionViewBean copyDecision) throws Exception, JAException, NumberFormatException {
        // Charge les donn�es encod�es
        CPDonneesBase donneeBase = new CPDonneesBase();
        donneeBase.setSession(getSession());
        donneeBase.setIdDecision(copyDecision.getIdDecision());
        donneeBase.retrieve(getTransaction());
        if (copyDecision.isNonActif()) {
            /*
             * attention reprise: copier la fortune d�terminante dans la fortune totale car tout les cas n'ont pas
             * forcement le d�tail de rempli (pb de reprise)
             */
            if (!detailCalcul) {
                donneeBase.setRevenu1("");
                donneeBase.setRevenuAutre1("");
                donneeBase.setFortuneTotale(orgDecision.getFortuneDeterminante());
            } else {
                donneeBase.setRevenu1(CPToolBox.annualisationRevenu(orgDecision.getDebutDecision(),
                        orgDecision.getFinDecision(), copyDecision.getDebutDecision(), copyDecision.getFinDecision(),
                        orgDecision.getRevenu1(), orgDecision.getNbMoisExercice1()));
                donneeBase.setRevenuAutre1(CPToolBox.annualisationRevenu(orgDecision.getDebutDecision(),
                        orgDecision.getFinDecision(), copyDecision.getDebutDecision(), copyDecision.getFinDecision(),
                        orgDecision.getRevenuAutre1(), orgDecision.getNbMoisRevenuAutre1()));
                donneeBase.setFortuneTotale(orgDecision.getFortuneTotale());
            }
            // T�l de Mr Burki - Mettre la date de fortune au 31.12 de
            // l'ann�e pr�c�dant l'acompte
            donneeBase.setDateFortune(saveDateFortune);
            // PO 3829
            if ("Periode".equalsIgnoreCase(typeRevenu)
                    || (((JACalendar.getYear(dateAvs) == JACalendar.getYear(copyDecision.getFinDecision())) && copyDecision
                            .isNonActif()))) {
                int dureeDecision = JACalendar.getMonth(copyDecision.getFinDecision())
                        - JACalendar.getMonth(copyDecision.getDebutDecision()) + 1;
                donneeBase.setNbMoisExercice1(Integer.toString(dureeDecision));
                donneeBase.setNbMoisRevenuAutre1(Integer.toString(dureeDecision));
            } else {
                donneeBase.setNbMoisExercice1("12");
                donneeBase.setNbMoisRevenuAutre1("12");
            }
        } else {
            // Annualisation des donn�es si exercice � cheval sur 2 ans
            int moisD = JACalendar.getMonth(orgDecision.getDebutExercice1());
            int moisF = JACalendar.getMonth(orgDecision.getFinExercice1());
            // if
            // ("1".equalsIgnoreCase(orgDecision.getProrata())||"3".equalsIgnoreCase(orgDecision.getProrata())){
            if ((moisD != 1) || (moisF != 12) || "1".equalsIgnoreCase(orgDecision.getProrata())
                    || "3".equalsIgnoreCase(orgDecision.getProrata())) {
                donneeBase.setRevenu1(CPToolBox.annualisationRevenu(orgDecision.getDebutExercice1(),
                        orgDecision.getFinExercice1(), copyDecision.getDebutExercice1(),
                        copyDecision.getFinExercice1(), orgDecision.getRevenu1(), orgDecision.getNbMoisExercice1()));
                donneeBase.setRevenuAutre1(CPToolBox.annualisationRevenu(orgDecision.getDebutExercice1(),
                        orgDecision.getFinExercice1(), copyDecision.getDebutExercice1(),
                        copyDecision.getFinExercice1(), orgDecision.getRevenuAutre1(),
                        orgDecision.getNbMoisRevenuAutre1()));
                // PO 6450 - Ne pas annualise pour les cas en cours d'ann�e
                if ("1".equalsIgnoreCase(orgDecision.getProrata()) || "3".equalsIgnoreCase(orgDecision.getProrata())) {
                    donneeBase.setCapital(CPToolBox.annualisationRevenu(orgDecision.getDebutExercice1(),
                            orgDecision.getFinExercice1(), copyDecision.getDebutExercice1(),
                            copyDecision.getFinExercice1(), orgDecision.getCapital(), "0"));
                } else {
                    donneeBase.setCapital(orgDecision.getCapital());
                }
            } else {
                donneeBase.setRevenu1(orgDecision.getRevenu1());
                donneeBase.setRevenuAutre1(orgDecision.getRevenuAutre1());
                donneeBase.setCapital(orgDecision.getCapital());
            }
            donneeBase.setDebutExercice1(copyDecision.getDebutExercice1());
            donneeBase.setFinExercice1(copyDecision.getFinExercice1());
            /*
             * Pour les cas ou il y a un revenu d�terminant mais qu'il n'y a pas de d�tail des revenus (Ex: caisse qui
             * ne stockait pas les informations), on copie le revenu d�terminant dans le revenu pour pas que ces cas
             * sortent avec la cotisation minimum
             */
            // recherche du revenu d�terminant
            if (JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(donneeBase.getRevenu1()))
                    && JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(donneeBase.getRevenuAutre1()))
                    && JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(donneeBase.getCapital()))
                    && JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(donneeBase.getCotisation1()))) {
                // Recherche du revenu d�terminant
                CPDonneesCalcul donnee = new CPDonneesCalcul();
                donnee.setSession(getSession());
                String revenuDeterninant = donnee.getMontant(orgDecision.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
                // PO 5110
                if (!JadeStringUtil.isBlankOrZero(revenuDeterninant)) {
                    donneeBase.setRevenu1(revenuDeterninant);
                    // Pour les cas d�j� rentier, il faut ajouter la franchise
                    // au revenu
                    if (CPDecision.CS_RENTIER.equalsIgnoreCase(orgDecision.getGenreAffilie())) {
                        ajouterFranchiseAuRevenu(orgDecision, donneeBase);
                    }
                }
            }
        }
        donneeBase.setSourceInformation(copyDecision.getSourceInformation());
        donneeBase.update(getTransaction());
        return donneeBase;
    }

    protected boolean existanceDecisionPourAnnee(CPDecisionManager deciManager,
            CPAcompteCreationAnnuelle decisionReprise) throws Exception {
        boolean aTraite = true;
        if (deciManager.getUseManagerForRepriseCotPersIndependantHorsAcompte().equals(Boolean.TRUE)
                || deciManager.getUseManagerForRepriseCotPersNonActifHorsAcompte().equals(Boolean.TRUE)) {
            CPDecisionManager decMng1 = new CPDecisionManager();
            decMng1.setSession(getSession());
            decMng1.setForAnneeDecision(deciManager.getForAnneeDecision());
            decMng1.setForIdAffiliation(decisionReprise.getIdAffiliation());
            decMng1.setInEtat(CPDecision.CS_VALIDATION + ", " + CPDecision.CS_PB_COMPTABILISATION + ", "
                    + CPDecision.CS_FACTURATION);
            decMng1.find();
            if (decMng1.size() > 0) {
                aTraite = false;
            }
        }
        return aTraite;
    }

    public AFCotisation getCotiAf() {
        return cotiAf;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // D�terminer l'objet du message en fonction du code erreur
        String obj = "";
        if (errorProcess) {
            obj = getSession().getLabel("SUJET_EMAIL_ACOMPTE_CREATION_FAILED");
        } else {
            obj = getSession().getLabel("SUJET_EMAIL_ACOMPTE_CREATION");
        }
        // Restituer l'objet
        return obj;
    }

    /**
     * Returns the forAnneeReprise.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForAnneeReprise() {
        return forAnneeReprise;
    }

    /**
     * @return
     */
    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public java.lang.String getForPeriodicite() {
        return forPeriodicite;
    }

    /**
     * Returns the fromAffilieDebut.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAffilieDebut() {
        return fromAffilieDebut;
    }

    /**
     * Returns the fromAffilieFin.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromAffilieFin() {
        return fromAffilieFin;
    }

    /**
     * Returns the idPassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 13:05:41)
     * 
     * @return float
     */
    public String getMontantAnnuel() {
        return montantAnnuel;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 13:04:19)
     * 
     * @return float
     */
    public String getMontantMensuel() {
        return montantMensuel;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 13:05:15)
     * 
     * @return float
     */
    public String getMontantSemestriel() {
        return montantSemestriel;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 13:04:47)
     * 
     * @return float
     */
    public String getMontantTrimestriel() {
        return montantTrimestriel;
    }

    /**
     * Returns the nbMoisExercice1.
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisExercice1() {
        return nbMoisExercice1;
    }

    /**
     * Returns the nbMoisExercice2.
     * 
     * @return java.lang.String
     */
    public java.lang.String getNbMoisExercice2() {
        return nbMoisExercice2;
    }

    /**
     * Returns the typeRevenu.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeRevenu() {
        return typeRevenu;
    }

    protected CPDecisionManager initManagerForIndependant() {
        CPDecisionManager deciManager2 = new CPDecisionManager();
        deciManager2.setSession(getSession());
        if (acompteSeBasantSurDerniereDecision) {
            deciManager2.setUseManagerForRepriseCotPersIndependantHorsAcompte(Boolean.TRUE);
        } else {
            deciManager2.setUseManagerForRepriseCotPersIndependant(Boolean.TRUE);
        }
        deciManager2.setForAnneeDecision(getForAnneeReprise());
        deciManager2.setForFinAffiliation("0");
        deciManager2.setFromFinAffiliation("01.01." + getForAnneeReprise());
        if (!JadeStringUtil.isEmpty(getFromAffilieDebut())) {
            deciManager2.setFromNoAffilie(getFromAffilieDebut());
        }
        if (!JadeStringUtil.isEmpty(getFromAffilieFin())) {
            deciManager2.setTillNoAffilie(getFromAffilieFin());
        }
        deciManager2.orderByNoAffilie(); // ordre affili�s ascendant
        // deciManager2.orderByDateDecision(); //ordre date d�cision
        // descendant
        deciManager2.orderByIdDecision(); // ordre idD�cision descendant
        return deciManager2;
    }

    protected CPDecisionManager initManagerForNonActif() {
        CPDecisionManager deciManager = new CPDecisionManager();
        deciManager.setSession(getSession());
        if (acompteSeBasantSurDerniereDecision) {
            deciManager.setUseManagerForRepriseCotPersNonActifHorsAcompte(Boolean.TRUE);
        } else {
            deciManager.setUseManagerForRepriseCotPersNonActif(Boolean.TRUE);
        }
        deciManager.setForGenreAffilie(getForGenreAffilie());
        deciManager.setForAnneeDecision(getForAnneeReprise());
        deciManager.setForFinAffiliation("0");
        deciManager.setFromFinAffiliation("01.01." + getForAnneeReprise());
        if (!JadeStringUtil.isEmpty(getFromAffilieDebut())) {
            deciManager.setFromNoAffilie(getFromAffilieDebut());
        }
        if (!JadeStringUtil.isEmpty(getFromAffilieFin())) {
            deciManager.setTillNoAffilie(getFromAffilieFin());
        }
        deciManager.orderByNoAffilie(); // ordre affili�s ascendant
        // deciManager.orderByDateDecision(); //ordre date d�cision
        // descendant
        deciManager.orderByIdDecision(); // ordre idD�cision descendant
        return deciManager;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    protected void returnDecisionPrecedente(CPDecisionViewBean decision) throws Exception {
        CPDecision dec = CPDecision._returnDecisionBase(getSession(), decision, true, false);
        decision.setIdDecision(dec.getIdDecision());
        decision.retrieve(getTransaction());
    }

    public void setCotiAf(AFCotisation cotiAf) {
        this.cotiAf = cotiAf;
    }

    /**
     * Sets the forAnneeReprise.
     * 
     * @param forAnneeReprise
     *            The forAnneeReprise to set
     */
    public void setForAnneeReprise(java.lang.String forAnneeReprise) {
        this.forAnneeReprise = forAnneeReprise;
    }

    /**
     * @param string
     */
    public void setForGenreAffilie(java.lang.String string) {
        forGenreAffilie = string;
    }

    public void setForPeriodicite(java.lang.String forPeriodicite) {
        this.forPeriodicite = forPeriodicite;
    }

    /**
     * Sets the fromAffilieDebut.
     * 
     * @param fromAffilieDebut
     *            The fromAffilieDebut to set
     */
    public void setFromAffilieDebut(java.lang.String fromAffilieDebut) {
        this.fromAffilieDebut = fromAffilieDebut;
    }

    /**
     * Sets the fromAffilieFin.
     * 
     * @param fromAffilieFin
     *            The fromAffilieFin to set
     */
    public void setFromAffilieFin(java.lang.String fromAffilieFin) {
        this.fromAffilieFin = fromAffilieFin;
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

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 13:05:41)
     * 
     * @param newMontantAnnuel
     *            float
     */
    public void setMontantAnnuel(String newMontantAnnuel) {
        montantAnnuel = newMontantAnnuel;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 13:04:19)
     * 
     * @param newMontantMensuel
     *            float
     */
    public void setMontantMensuel(String newMontantMensuel) {
        montantMensuel = newMontantMensuel;
    }

    protected void setMontantMinimumIndependant() {
        try {
            cotiIndMinimum = CPTableIndependant.getCotisationMinimum(getTransaction(), "01.01." + getForAnneeReprise());
            cotiAfiMinimum = CPTableAFI.getCotisationMinimum(getTransaction(), "01.01." + getForAnneeReprise());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0129"));

        }
    }

    protected void setMontantMinimumNonActif() {
        try {
            cotiNEAnnuelleMin = new Float(CPTableNonActif.getRevenuMin(getSession(), getForAnneeReprise()))
                    .floatValue();
            cotiNacMinimum = CPTableNonActif.getCotisationMin(getSession(), getForAnneeReprise());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0128"));

        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 13:05:15)
     * 
     * @param newMontantSemestriel
     *            float
     */
    public void setMontantSemestriel(String newMontantSemestriel) {
        montantSemestriel = newMontantSemestriel;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 13:04:47)
     * 
     * @param newMontantTrimestriel
     *            float
     */
    public void setMontantTrimestriel(String newMontantTrimestriel) {
        montantTrimestriel = newMontantTrimestriel;
    }

    /**
     * Sets the nbMoisExercice1.
     * 
     * @param nbMoisExercice1
     *            The nbMoisExercice1 to set
     */
    public void setNbMoisExercice1(java.lang.String nbMoisExercice1) {
        this.nbMoisExercice1 = nbMoisExercice1;
    }

    /**
     * Sets the nbMoisExercice2.
     * 
     * @param nbMoisExercice2
     *            The nbMoisExercice2 to set
     */
    public void setNbMoisExercice2(java.lang.String nbMoisExercice2) {
        this.nbMoisExercice2 = nbMoisExercice2;
    }

    /**
     * Sets the typeRevenu.
     * 
     * @param typeRevenu
     *            The typeRevenu to set
     */
    public void setTypeRevenu(java.lang.String typeRevenu) {
        this.typeRevenu = typeRevenu;
    }

}
