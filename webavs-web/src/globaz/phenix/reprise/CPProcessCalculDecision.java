package globaz.phenix.reprise;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.db.principale.CPDonneesCalculManager;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author btc
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CPProcessCalculDecision extends BProcess {
    /**
     * Lancement de la facturation pour un passage Date de création : (29.04.2003 09:14:04)
     * 
     * @param args
     *            java.lang.String[] - N° de passage
     */
    public static void main(String[] args) {
        CPProcessCalculDecision process = null;
        String user = "";
        String pwd = "";
        String email = "hna@globaz.ch";
        try {
            user = args[0];
            pwd = args[1];
            email = args[2];
            System.out.println("User : " + user);
            System.out.println("Password : " + pwd);
            System.out.println("Email : " + email);
            System.out.println("passage: " + args[3]);
            System.out.println("genre: " + args[4]);
            System.out.println("type: " + args[5]);
            BSession session = (BSession) GlobazSystem.getApplication("PHENIX").newSession(user, pwd);
            System.out.println("Reprise started...");
            session.connect(user, pwd);
            process = new CPProcessCalculDecision();
            process.setSession(session);
            process.setEMailAddress(email);
            if (!"0".equalsIgnoreCase(args[3])) {
                process.setIdPassage(args[3]);
            }
            if (!"0".equalsIgnoreCase(args[4])) {
                process.setForGenreAffilie(args[4]);
            }
            if (!"0".equalsIgnoreCase(args[5])) {
                process.setForTypeDecision(args[5]);
            }
            if (!"0".equalsIgnoreCase(args[6])) {
                process.setIdDecision(args[6]);
            }
            process.executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("Le calcul des décisions est terminé.");
        }
        System.exit(0);
    }

    private CACompteAnnexe compteAnnexe = null;
    private AFCotisation cotisation = null;
    private CPDecisionViewBean decision = null;
    private java.lang.String descriptionTiers = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String forTypeDecision = "";
    private java.lang.String fromNumAffilie = "";
    private String idAffPrec = "";
    private java.lang.String idDecision = "";
    // Paramètres utiliser par l'écran du decisionRepriseViewBean
    private java.lang.String idPassage = "";
    private String idTiersPrec = "";
    private Boolean impressionMontantIdentique = Boolean.FALSE;
    private Vector messages = new Vector();
    private int modeArrondiFadCotPers = 0;
    private String role = "";
    private TITiersViewBean tiers = null;

    private java.lang.String typeRevenu = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessCalculDecision() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessCalculDecision(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessCalculDecision(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Calcul des cotisations à partir de la réception des communications fiscales
     * 
     * @param myDecision
     * @param retour
     */
    public void _calculRetour(CPDecisionViewBean newDecision, boolean revAf) {

        // Sous contrôle d'exception
        try {
            // recherhe des données de Base de la décision
            CPDonneesBase donneesBase = new CPDonneesBase();
            donneesBase.setSession(getSession());
            donneesBase.setIdDecision(newDecision.getIdDecision());
            donneesBase.retrieve(getTransaction());
            // Suppression des anciennes données
            suppressionAnciennesDonnees();
            // Initialiser le process de calcul
            CPProcessCalculCotisation calcul = new CPProcessCalculCotisation();
            calcul.setIdDecision(newDecision.getIdDecision());
            calcul.setSession(getSession());
            calcul.setModeArrondiFad(getModeArrondiFadCotPers());
            calcul.setSendMailOnError(true);
            calcul.setSendCompletionMail(false);
            calcul.setTransaction(getTransaction());
            calcul.setIdDecision(newDecision.getIdDecision());
            if (cotisation != null) {
                calcul.setCotiAf(cotisation);
            }
            if (newDecision.getAffiliation() != null) {
                calcul.setAffiliation(newDecision.loadAffiliation());
            }
            calcul.setDecision(newDecision);
            calcul.setTiers(getTiers());
            calcul.setDonneeBase(donneesBase);
            calcul.setCompteAnnexe(getCompteAnnexe());
            if (decision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                calcul.calculBonifMiseEnCompte(this);
            } else if (newDecision.isNonActif()) {
                if (calcul.getCotiAf() != null) {
                    // Initialiser la zone cotisation sur salaire à 10100 pour
                    // les cas qui n'ont pas de compteur ou à 0
                    // car cela indique que la décision a été extournée suite à
                    // des imputations
                    // du coup avec cette zone renseignée à 10100 il viendre
                    // l'annotation "assez cotisé comme salarié".
                    String montant = CPToolBox.rechMontantFacture(getSession(), getTransaction(),
                            compteAnnexe.getIdCompteAnnexe(),
                            AFCotisation._getRubrique(calcul.getCotiAf().getCotisationId(), getSession()),
                            decision.getAnneeDecision());
                    if (JadeStringUtil.isIntegerEmpty(montant)
                            && (JACalendar.today().getYear() != Integer.parseInt(decision.getAnneeDecision()))) {
                        donneesBase.setCotisationSalarie("12000");
                    } else {
                        donneesBase.setCotisationSalarie("");
                        decision.setTypeDecision(CPDecision.CS_PROVISOIRE);
                        decision.setSpecification("");
                    }
                    calcul.setDonneeBase(donneesBase);
                    calcul.setDecision(newDecision);
                    // calcul.setCalculForReprise(true);
                    calcul.calculNonActif(this);
                }
            } else {
                if (JadeStringUtil.isBlankOrZero(newDecision.getGenreAffilie())
                        || "888888".equalsIgnoreCase(newDecision.getGenreAffilie())) {
                    if (globaz.naos.translation.CodeSystem.BRANCHE_ECO_AGRICULTURE.equalsIgnoreCase(newDecision
                            .getAffiliation().getBrancheEconomique())) {
                        newDecision.setGenreAffilie(CPDecision.CS_AGRICULTEUR);
                    }
                    String dateAvs = newDecision.getAffiliation().getTiers().getDateAvs();
                    int anneeAvs = JACalendar.getYear(dateAvs);
                    int anneeDec = JACalendar.getYear(newDecision.getDebutDecision());
                    if ((anneeAvs < anneeDec)
                            || ((anneeAvs == anneeDec) && BSessionUtil.compareDateFirstGreater(getSession(),
                                    newDecision.getFinDecision(), dateAvs))) {
                        newDecision.setGenreAffilie(CPDecision.CS_RENTIER);
                        calcul.setDecision(newDecision);
                    }
                }
                if (newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                        || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_RENTIER)
                        || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)
                        || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                    if (!donneesBase.isNew()) {
                        if (JadeStringUtil.isBlank(donneesBase.getDebutExercice1())) {
                            donneesBase.setDebutExercice1(decision.getDebutDecision());
                        }
                        if (JadeStringUtil.isBlank(donneesBase.getFinExercice1())) {
                            donneesBase.setFinExercice1(decision.getFinDecision());
                        }
                    }
                    calcul.setDonneeBase(donneesBase);
                    calcul.setDecision(newDecision);
                    if (revAf && !JadeStringUtil.isEmpty(newDecision.getRevenuAutre1())) {
                        // Calcul en ignorant revenu autre 1 et en ne créant pas
                        // la coti AF
                        calcul.calculIndependant(this, 1);
                        // Calcul en tenant compte uniquement de revenu autre 1
                        // et en créant uniquement la coti AF
                        calcul.calculIndependant(this, 2);
                    } else {
                        // calcul normal
                        calcul.calculIndependant(this, 0);
                    }
                }
            }
            // Création des remarques
            // calcul.createRemarqueAutomatique(getTransaction(), newDecision);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param manager
     * @return
     */
    public boolean _executeBoucleRetour(CPDecisionAffiliationManager manager) throws Exception {

        BStatement statement = null;
        // Sous controle d'exceptions
        BTransaction transactionLecture = (BTransaction) getSession().newTransaction();
        try {
            int maxScale = manager.getCount(getTransaction());
            if (maxScale > 0) {
                setProgressScaleValue(maxScale);
            } else {
                setProgressScaleValue(1);
            }
            // disabler le spy
            getTransaction().disableSpy();
            // itérer sur toutes les affiliations
            statement = manager.cursorOpen(getTransaction());
            // Recherche si revenuAutre = revenu AF (ex cas spéciaux AF pour GE)
            boolean revAf = ((CPApplication) getSession().getApplication()).isRevenuAf();
            CPDecisionAffiliation decisionAffiliation = null;
            transactionLecture = (BTransaction) getSession().newTransaction();
            transactionLecture.openTransaction();
            statement = manager.cursorOpen(transactionLecture);
            setModeArrondiFadCotPers(CPToolBox.getModeArrondiFad(getTransaction()));
            while (((decisionAffiliation = (CPDecisionAffiliation) manager.cursorReadNext(statement)) != null)
                    && (!decisionAffiliation.isNew()) && !isAborted()) {
                try {
                    decision = new CPDecisionViewBean();
                    decision.setIdDecision(decisionAffiliation.getIdDecision());
                    decision.setSession(getSession());
                    decision.retrieve();
                    if (!getTransaction().hasErrors()) {
                        if (JadeStringUtil.isBlankOrZero(decisionAffiliation.getFinAffiliation())
                                || BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                                        decision.getFinDecision(), decisionAffiliation.getFinAffiliation())) {
                            if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
                                    decision.getDebutDecision(), decisionAffiliation.getDebutAffiliation())
                                    && BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                                            decision.getDebutDecision(), decision.getFinDecision())) {
                                _executeProcessRetourGenererDecision(decision, revAf);
                            } else {
                                decision.wantCallValidate(false);
                                decision.setDernierEtat(CPDecision.CS_CREATION);
                                decision.update(getTransaction());
                            }
                        } else {
                            decision.wantCallValidate(false);
                            decision.setDernierEtat(CPDecision.CS_CREATION);
                            decision.update(getTransaction());
                        }
                    }
                    incProgressCounter();

                    if (getSession().hasErrors()) {
                        getMemoryLog().logMessage(getTransaction().getErrors().toString(), FWMessage.ERREUR,
                                this.getClass().getName());
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                    }
                    if (getTransaction().hasErrors()) {
                        getMemoryLog().logMessage(getTransaction().getErrors().toString(), FWMessage.ERREUR,
                                this.getClass().getName());
                        getTransaction().rollback();
                        getTransaction().clearErrorBuffer();
                    } else if (isAborted()) {
                        getTransaction().rollback();
                    } else {
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    getMemoryLog().logMessage(e.getMessage() + "\n" + decision.toString(), FWMessage.FATAL,
                            this.getClass().getName());
                    getTransaction().rollback();
                } finally {
                    for (Iterator iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                        messages.add(iter.next());
                    }
                    getMemoryLog().clear();
                }

            }
            // rétablir le contrôle du spy
            getTransaction().enableSpy();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            System.out.println(e.toString());
        } // Fin de la procédure
        catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                try {
                    manager.cursorClose(statement);
                } finally {
                    if (transactionLecture != null) {
                        transactionLecture.closeTransaction();
                    }
                    manager = null;
                    statement = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return !isOnError();
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            // type de revenu depuis les properties
            String typeRevenu = ((CPApplication) getSession().getApplication()).getTypeRevenu();
            setTypeRevenu(typeRevenu);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0137"));

        }
        // Recherche du role
        setRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication()));

        CPDecisionAffiliationManager decManager = new CPDecisionAffiliationManager();
        decManager.setSession(getSession());
        decManager.setForIdPassage(getIdPassage());
        // decManager.setNotInGenreAffilie(CPDecision.CS_NON_ACTIF);
        // decManager.setForAnneeDecision("2010");
        decManager.setForGenreAffilie(getForGenreAffilie());
        decManager.setForTypeDecision(getForTypeDecision());
        // decManager.setForAnneeDecision("2009");
        decManager.setForIdDecision(getIdDecision());
        // decManager.setForIdPassage("2");
        // decManager.setFromNoAffilie("015.118.007001");
        // decManager.setTillNoAffilie("015.118.007001");
        decManager.setInEtat(CPDecision.CS_REPRISE + ", " + CPDecision.CS_CALCUL + ", " + CPDecision.CS_FACTURATION
                + ", " + CPDecision.CS_VALIDATION);
        decManager.orderByNoAffilie();
        decManager.orderByAnnee();
        decManager.changeManagerSize(0);
        _executeBoucleRetour(decManager);
        // Remettre les erreurs des process dans le log
        for (Iterator iter = messages.iterator(); iter.hasNext();) {
            getMemoryLog().getMessagesToVector().add(iter.next());
        }
        // Arrêter le traitement à la première exception de la transaction
        if (getTransaction().hasErrors()) {
            return false;
        }
        return !isOnError();
    }

    /**
     * @param retour
     * @return
     */
    public boolean _executeProcessRetourGenererDecision(CPDecisionViewBean decision, boolean revAf) {
        try {
            impressionMontantIdentique = ((CPApplication) getSession().getApplication())
                    .isImpressionMontantIdentique();
        } catch (Exception e) {
            impressionMontantIdentique = Boolean.FALSE;
        }
        try {
            // Chargement du tiers
            if (!decision.getIdTiers().equalsIgnoreCase(idTiersPrec)) {
                setTiers(null);
                idTiersPrec = decision.getIdTiers();
                TITiersViewBean persAvs = new TITiersViewBean();
                persAvs.setSession(getSession());
                persAvs.setIdTiers(decision.getIdTiers());
                persAvs.retrieve();
                if (!persAvs.isNew()) {
                    setTiers(persAvs);
                } else {
                    setTiers(null);
                }
            }
            // Recherche de l'idCoti dans naos
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            cotisation = aff._cotisation(getTransaction(), decision.getIdAffiliation(),
                    CodeSystem.GENRE_ASS_PERSONNEL, CodeSystem.TYPE_ASS_COTISATION_AVS_AI, decision.getDebutDecision(),
                    decision.getFinDecision(), 1);
            if (!decision.getIdAffiliation().equalsIgnoreCase(idAffPrec)) {
                compteAnnexe = null;
                idAffPrec = decision.getIdAffiliation();
                aff.setAffiliationId(decision.getIdAffiliation());
                aff.retrieve();
                if (!aff.isNew()) {
                    compteAnnexe = new CACompteAnnexe();
                    compteAnnexe.setSession(getSession());
                    compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compteAnnexe.setIdRole(getRole());
                    compteAnnexe.setIdExterneRole(aff.getAffilieNumero());
                    compteAnnexe.wantCallMethodBefore(false);
                    compteAnnexe.retrieve(getTransaction());
                }
            }
            _calculRetour(decision, revAf);
            // ---------------------------------------------------------------------------------
            // Si cas sans anomalie et validation directe => mettre état
            // décision et com. fiscale à l'état valider sinon
            // alimenter la table validation des communications fiscales pour
            // validation "manuelle".
            // Mise à jour état
            CPDecision decis = new CPDecision();
            decis.setIdDecision(decision.getIdDecision());
            decis.setSession(getSession());
            decis.retrieve();

            if (!getTransaction().hasErrors()) {
                // decis.setDernierEtat(CPDecision.CS_VALIDATION);
                try {
                    if (impressionMontantIdentique.booleanValue()) {
                        if (!CPDecision.CS_IMPUTATION.equalsIgnoreCase(decis.getTypeDecision())
                                && isDecisionIdentiqueProvisoire(decis)) {
                            decis.setImpression(Boolean.FALSE);
                            decis.setFacturation(Boolean.FALSE);
                        }
                    }
                    if (decis.getDernierEtat().equalsIgnoreCase(CPDecision.CS_FACTURATION)
                            || decis.getDernierEtat().equalsIgnoreCase(CPDecision.CS_PB_COMPTABILISATION)) {
                        decis.setDernierEtat(CPDecision.CS_REPRISE);
                    }
                    if (JadeStringUtil.isBlankOrZero(decis.getGenreAffilie())
                            || "888888".equalsIgnoreCase(decis.getGenreAffilie())) {
                        decis.setGenreAffilie(decision.getGenreAffilie());
                    }
                    decis.setSpecification(decision.getSpecification());
                    decis.setTypeDecision(decision.getTypeDecision());

                    decis.update(getTransaction());
                } catch (Exception e) {
                    this._addError(getTransaction(), getSession().getLabel("CP_MSG_0140")
                            + decision.getAffiliation().getAffilieNumero() + " - " + e.getMessage());
                }
            }

            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
                if (!CPDecision.CS_NON_ACTIF.equals(decis.getGenreAffilie())) {
                    CPDonneesBase doba = new CPDonneesBase();
                    doba.setIdDecision(decis.getIdDecision());
                    doba.setSession(getSession());
                    doba.retrieve();
                    if (!doba.isNew()) {
                        if (JadeStringUtil.isBlank(doba.getDebutExercice1())) {
                            doba.setDebutExercice1(decis.getDebutDecision());
                        }
                        if (JadeStringUtil.isBlank(doba.getFinExercice1())) {
                            doba.setFinExercice1(decis.getFinDecision());
                        }
                        doba.update(getTransaction());
                    }
                }
                if (!getTransaction().hasErrors()) {
                    getTransaction().commit();
                } else {
                    String msg = "";
                    if (decision.getAffiliation() != null) {
                        msg = decision.getAffiliation().getAffilieNumero();
                    }
                    msg = msg + " - " + decision.getIdDecision();
                    this._addError(getTransaction(), msg);
                    getTransaction().rollback();
                }
            } else {
                String msg = "";
                if (decision.getAffiliation() != null) {
                    msg = decision.getAffiliation().getAffilieNumero();
                }
                msg = msg + " - " + decision.getIdDecision();
                this._addError(getTransaction(), msg);
                getTransaction().rollback();
            }
            // ------------------------------------------------------------------------------------

        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0141") + decision.getIdDecision());
        }

        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    public CACompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:59:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDescriptionTiers() {
        return descriptionTiers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";
        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("PROCRECEPTGENDEC_EMAIL_OBJECT_FAILED");
        } else {
            obj = getSession().getLabel("SUJET_EMAIL_RECEPTION_CALCUL");
        }
        // Restituer l'objet
        return obj;
    }

    /**
     * @return
     */
    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getForTypeDecision() {
        return forTypeDecision;
    }

    /**
     * @return
     */
    public java.lang.String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * * Returns the idPassage.
     * 
     * @return java.lang.Strin
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public int getModeArrondiFadCotPers() {
        return modeArrondiFadCotPers;
    }

    public String getRole() {
        return role;
    }

    /**
     * Returns the tiers.
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
    public globaz.pyxis.db.tiers.TITiersViewBean getTiers() {
        return tiers;
    }

    /**
     * Returns the typeRevenu.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeRevenu() {
        return typeRevenu;
    }

    private boolean isDecisionIdentiqueProvisoire(CPDecision decision) {
        CPDonneesCalcul donCalcul = new CPDonneesCalcul();
        donCalcul.setSession(getSession());
        CPDecision decisionDeBase;
        try {
            decisionDeBase = CPDecision._returnDecisionBase(getSession(), decision.getIdDecision());
            if (decisionDeBase == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        String montant;
        String montantDeBase;
        if (decision.isNonActif()) {
            montant = donCalcul.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_FORTUNE_TOTALE);
        } else {
            montant = donCalcul.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
        }
        if (decisionDeBase.isNonActif()) {
            montantDeBase = donCalcul.getMontant(decisionDeBase.getIdDecision(), CPDonneesCalcul.CS_FORTUNE_TOTALE);
        } else {
            montantDeBase = donCalcul.getMontant(decisionDeBase.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
        }
        if (montant.equals(montantDeBase)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setCompteAnnexe(CACompteAnnexe compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 10:59:30)
     * 
     * @param newDescriptionTiers
     *            java.lang.String
     */
    public void setDescriptionTiers(java.lang.String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    /**
     * @param string
     */
    public void setForGenreAffilie(java.lang.String string) {
        forGenreAffilie = string;
    }

    /**
     * @param string
     */
    public void setForTypeDecision(java.lang.String string) {
        forTypeDecision = string;
    }

    /**
     * @param string
     */
    public void setFromNumAffilie(java.lang.String string) {
        fromNumAffilie = string;
    }

    public void setIdDecision(java.lang.String idDecision) {
        this.idDecision = idDecision;
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

    public void setModeArrondiFadCotPers(int modeArrondiFadCotPers) {
        this.modeArrondiFadCotPers = modeArrondiFadCotPers;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Sets the tiers.
     * 
     * @param tiers
     *            The tiers to set
     */
    public void setTiers(globaz.pyxis.db.tiers.TITiersViewBean tiers) {
        this.tiers = tiers;
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

    public void suppressionAnciennesDonnees() throws Exception {
        // Données du calcul
        CPDonneesCalculManager donCalculManager = new CPDonneesCalculManager();
        donCalculManager.setSession(getSession());
        donCalculManager.setForIdDecision(decision.getIdDecision());
        donCalculManager.delete(getTransaction());
        // Cotisation
        CPCotisationManager cotiManager = new CPCotisationManager();
        cotiManager.setSession(getSession());
        cotiManager.setForIdDecision(decision.getIdDecision());
        cotiManager.delete(getTransaction());
        // Contrôle cotisation salarié
        if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(decision.getSpecification())) {
            decision.setSpecification("");
        }
    }
}
