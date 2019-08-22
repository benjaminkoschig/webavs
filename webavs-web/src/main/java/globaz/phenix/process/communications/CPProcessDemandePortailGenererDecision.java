package globaz.phenix.process.communications;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import ch.globaz.common.domaine.Date;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;
import ch.globaz.orion.db.EBDemandeModifAcompteEntity;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.db.divers.CPTableIndependantManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Génération des décisions lors de la validation d'une décision depuis ORION
 *
 */
public class CPProcessDemandePortailGenererDecision extends BProcess {
    private static final long serialVersionUID = -1161457272987315919L;
    private AFCotisation cotisation = null;
    private String descriptionTiers = "";
    private String forIdPlausibilite = "";
    private String forStatus = "";
    private String fromNumAffilie = "";
    private String idDecision = "";
    // Paramètres utiliser par l'écran du decisionRepriseViewBean
    private String idPassage = "";
    private String idRetour = "";
    private CPJournalRetour journal = null;
    private Boolean lettreSignature = null;
    private ArrayList<String> listIdNonTraite = null;
    private String[] listIdDemande = null;

    private ArrayList<String> listIdTraite = null;
    private Vector messages = new Vector();
    private int modeCalculArrondiCotPers = 0;
    private CPDecisionViewBean newDecision = null;

    boolean processOk = true;
    private TITiersViewBean tiers = null;

    private String typeRevenu = "";
    private boolean validationDecision = false;

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessDemandePortailGenererDecision() {
        super();
    }

    /**
     * Constructeur du type BProcess.
     *
     * @param parent
     *            BProcess
     */
    public CPProcessDemandePortailGenererDecision(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BSession.
     *
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessDemandePortailGenererDecision(BSession session) {
        super(session);
    }

    /**
     * Calcul des cotisations à partir de la réception des communications fiscales
     *
     * @param myDecision
     * @param retour
     */
    public void _calculDemande(CPDecisionViewBean newDecision, boolean revAf) {

        // Sous contrôle d'exception
        try {
            // Mise à jour cotisation (WEBAVS-5955 - K181109_001)
            // WEBAVS-6104 : calculRevenuAvecCotisation : à calculer avant calculIndependant pour mettre à jour les cotisation des donneesBases
            Date date = new Date();
            if (EBProperties.ADI_CALCUL_COTISATION.getBooleanValue()
                    && !newDecision.getAnneeDecision().equals(date.getAnnee())) {
                calculRevenuAvecCotisation(newDecision);
            }
            // recherhe des données de Base de la décision
            CPDonneesBase donneesBase = new CPDonneesBase();
            donneesBase.setSession(getSession());
            donneesBase.setIdDecision(newDecision.getIdDecision());
            donneesBase.retrieve(getTransaction());
            // Initialiser le process de calcul
            CPProcessCalculCotisation calcul = new CPProcessCalculCotisation();
            calcul.setIdDecision(newDecision.getIdDecision());
            calcul.setSession(getSession());
            calcul.setModeArrondiFad(getModeCalculArrondiCotPers());
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
            calcul.setTiers(this.getTiers());
            calcul.setDonneeBase(donneesBase);
            if (newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                    || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_RENTIER)
                    || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)
                    || newDecision.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                if (revAf && !JadeStringUtil.isEmpty(newDecision.getRevenuAutre1())) {
                    // Calcul en ignorant revenu autre 1 et en ne créant pas la
                    // coti AF
                    calcul.calculIndependant(this, 1);
                    // Calcul en tenant compte uniquement de revenu autre 1 et
                    // en créant uniquement la coti AF
                    calcul.calculIndependant(this, 2);
                } else {
                    // calcul normal
                    calcul.calculIndependant(this, 0);
                }
            }
            // Création des remarques
            calcul.createRemarqueAutomatique(getTransaction(), newDecision);

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    protected void calculRevenuAvecCotisation(CPDecisionViewBean newDecision) throws Exception {
        // Recherche taux dans la table des indépendant qui permet de
        // recalculer la revenu (modification AVS 2012)
        if (!JadeStringUtil.isBlankOrZero(newDecision.getRevenu1())
                || !JadeStringUtil.isBlankOrZero(newDecision.getRevenuAutre1())) {
            String revenu = "0";
            // Si les 2 revenus (Exemple revenu et revenu agricole sont renseignés
            // il ne faut pas calculer les cotisations pour chaque revenus mais prendre le total
            // et ajouter les cotisation sur le revenu non agricole => PO 6336 (I120216_000026)
            FWCurrency varTemp = new FWCurrency(revenu);
            if (!JadeStringUtil.isBlankOrZero(newDecision.getRevenu1())) {
                varTemp.add(JANumberFormatter.deQuote(newDecision.getRevenu1()));
            }
            if (!JadeStringUtil.isBlankOrZero(newDecision.getRevenuAutre1())) {
                varTemp.add(JANumberFormatter.deQuote(newDecision.getRevenuAutre1()));
            }
            revenu = varTemp.toString();
            if (varTemp.isNegative()) { // PO 6469
                return; // PO 6469
            }
            // Nouvelle directive au 01.01.2013 (tenir compte de la franchise pour les rentiers
            String montantPourTaux = revenu;
            float mFranchise = 0;
            if (CPDecision.CS_RENTIER.equalsIgnoreCase(newDecision.getGenreAffilie())) {
                // Détermination du montant de franchise
                mFranchise = getFranchise(this);
                varTemp = new FWCurrency(JANumberFormatter.deQuote(revenu));
                varTemp.sub(mFranchise);
                if (varTemp.isNegative()) { // PO 8272
                    return; // PO 8272
                }
                montantPourTaux = varTemp.toString();
            }

            // S150911_009
            float mInteretcapital = 0;
            if (!JadeStringUtil.isBlankOrZero(newDecision.getCapital())) {
                // Détermination du montant d'intérêt
                mInteretcapital = CPDonneesCalcul.calculInteretCapital(this, newDecision);
                varTemp = new FWCurrency(JANumberFormatter.deQuote(revenu));
                varTemp.sub(mFranchise);
                varTemp.sub(mInteretcapital);
                if (varTemp.isNegative()) { // PO 8272
                    return; // PO 8272
                }
                montantPourTaux = varTemp.toString();
            }

            // S171221_015
            if (!JadeStringUtil.isBlankOrZero(newDecision.getRachatLPP())) {
                FWCurrency varTempRachat = new FWCurrency(JANumberFormatter.deQuote(newDecision.getRachatLPP()));
                FWCurrency varTempMontantTaux = new FWCurrency(JANumberFormatter.deQuote(montantPourTaux));
                varTempMontantTaux.sub(varTempRachat);
                montantPourTaux = varTempMontantTaux.toString();
            }

            // Pas de calcul de cotisation si l'affiliié a une activité accessoire
            if (determinineSiActiviteAccessoire(newDecision, revenu)) {
                return;
            }
            //
            CPTableIndependant tInd = null;
            float tauxCalcul = 0;
            CPTableIndependantManager tIndManager = new CPTableIndependantManager();
            tIndManager.setSession(getSession());
            tIndManager.setFromAnneeInd(newDecision.getAnneeDecision());
            if (Float.parseFloat(JANumberFormatter.deQuote(montantPourTaux)) < 0) {
                tIndManager.setFromRevenuInd("0");
            } else {
                tIndManager.setFromRevenuInd(montantPourTaux);
            }
            tIndManager.orderByAnneeDescendant();
            tIndManager.orderByRevenuDescendant();
            tIndManager.find();
            if (tIndManager.size() == 0) {
                this._addError(getTransaction(), "cotisation non trouvée: " + newDecision.getAnneeDecision() + "  "
                        + newDecision.getRevenu1() + " " + getDescriptionTiers());
            } else {
                tInd = ((CPTableIndependant) tIndManager.getEntity(0));
            }
            if (tInd == null) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0110") + " " + getDescriptionTiers());
            }
            if (!getTransaction().hasErrors()) {
                if (JadeStringUtil.isEmpty(tInd.getTaux())) {
                    tauxCalcul = 100;
                } else {
                    tauxCalcul = Float.parseFloat(
                            new FWCurrency(100 - Float.parseFloat(JANumberFormatter.deQuote(tInd.getTaux())), 4)
                                    .toString());
                }

                float calculCotisation = 0f;
                if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(newDecision.getRachatLPP()))) {
                    float rachatLpp = Float.parseFloat(JANumberFormatter.deQuote(newDecision.getRachatLPP()));
                    float revenuPourCalcul = Float.parseFloat(JANumberFormatter.deQuote(revenu)) - rachatLpp;

                    calculCotisation = (((revenuPourCalcul * 100)
                            - (mFranchise * Float.parseFloat(JANumberFormatter.deQuote(tInd.getTaux())))
                            - (mInteretcapital * Float.parseFloat(JANumberFormatter.deQuote(tInd.getTaux()))))
                            / tauxCalcul) - revenuPourCalcul;
                } else {

                    calculCotisation = (((Float.parseFloat(JANumberFormatter.deQuote(revenu)) * 100)
                            - (mFranchise * Float.parseFloat(JANumberFormatter.deQuote(tInd.getTaux())))
                            - (mInteretcapital * Float.parseFloat(JANumberFormatter.deQuote(tInd.getTaux()))))
                            / tauxCalcul) - Float.parseFloat(JANumberFormatter.deQuote(revenu));
                }
                newDecision.setCotisation1(
                        JANumberFormatter.round(Float.toString(calculCotisation), 1, 2, JANumberFormatter.INF));
                newDecision.update(getTransaction());
            }
        }
    }

    /**
     * Determine si l'affilié à une activité accessoire
     *
     * @param newDecision
     * @param revenu
     * @return
     * @throws Exception
     */
    private boolean determinineSiActiviteAccessoire(CPDecisionViewBean newDecision, String revenu) throws Exception {
        // PO 8272 - Si activité accessoire ne pas ajouter les cotisations
        if (AFParticulariteAffiliation.existeParticularite(getTransaction(), this.newDecision.getIdAffiliation(),
                AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, this.newDecision.getDebutDecision())) {
            // Recherche montant maximum pour activité accessoire
            float revenuActviteAccessoire = Float.parseFloat(FWFindParameter.findParameter(getTransaction(), "10500130",
                    "REVACTACC", this.newDecision.getDebutDecision(), "", 0));

            float interet = calculInteret(newDecision);
            float montantActiviteAccessoire = Float.parseFloat(JANumberFormatter.deQuote(revenu.toString())) - interet;
            // Arrondir au 100 CHF pour les tests si le genre est différents de TSE
            if (!CPDecision.CS_TSE.equalsIgnoreCase(newDecision.getGenreAffilie()) && (JANumberFormatter
                    .round(montantActiviteAccessoire, 100, 0, JANumberFormatter.INF) <= revenuActviteAccessoire)) {
                return true;
            }
            // ne pas arrondir au 100 CHF pour les tests pour les TSE
            if (CPDecision.CS_TSE.equalsIgnoreCase(newDecision.getGenreAffilie())
                    && (montantActiviteAccessoire <= revenuActviteAccessoire)) {
                return true;
            }
        }
        return false;
    }

    protected float calculInteret(CPDecisionViewBean newDecision) throws Exception {
        float capital = 0;
        float interet = 0;
        String tauxInteret = "";
        // Détermination des intérêts du capital investi
        // interet = capital arrondi au 1000fr. supérieur * taux
        if (!JadeStringUtil.isBlank(newDecision.getCapital())) {
            capital = Float.parseFloat(JANumberFormatter.deQuote(newDecision.getCapital()));
            // Arrondir le capital au 1000 fr. supérieur
            capital = JANumberFormatter.round(Float.parseFloat(Float.toString(capital)), 1000, 0,
                    JANumberFormatter.SUP);
            // Recherche du taux dans la table des paramètres correspondant
            // au début de décision (praenumerando) ou à la date de fin
            // d'exercice (postnumerando)
            if (newDecision.getTaxation().equalsIgnoreCase("A")) {
                tauxInteret = FWFindParameter.findParameter(getTransaction(), "10500020", "TAUXINTERE",
                        newDecision.getDebutDecision(), "", 2);
            } else {
                tauxInteret = FWFindParameter.findParameter(getTransaction(), "10500020", "TAUXINTERE",
                        newDecision.getFinExercice1(), "", 2);
            }
            // Calcul des intêrets
            if (!JadeStringUtil.isBlank(tauxInteret)) {
                interet = (capital * Float.parseFloat(tauxInteret)) / 100;
            }
            // Ramener au prorata de la période de l'exercice pour le
            // mode de taxation postnumerando
            interet = Float.parseFloat(CPToolBox.prorataInteret(newDecision.getDebutExercice1(),
                    newDecision.getFinExercice1(), Float.toString(interet)));
            interet = JANumberFormatter.round(interet, 1, 0, JANumberFormatter.INF);
            if (interet < 0) {
                interet = 0;
            }
        }
        return interet;
    }

    public float getFranchise(BProcess process) {
        // Pour le mode preanumerando, le montant de la franchise était valable
        // pour toute l'année soit 12* montant mensuel.
        // Depuis le mode postnumerando, le nombre de mois est déterminer depuis
        // le
        // début de la période de décision (ou le début de l'âge AVS si c'est
        // l'année ou
        // l'affilié atteint l'âge AVS) jusqu'à la fin de la decision.
        float mFranchise = 0;
        try {
            // Recherche du montant de franchise mensuel
            mFranchise = Float.parseFloat(FWFindParameter.findParameter(getTransaction(), "10500030", "FRANCHISE",
                    newDecision.getDebutDecision(), "", 0));
            String dateAvs = this.getTiers().getDateAvs();
            int anneeAvs = JACalendar.getYear(dateAvs);
            // Recherche de l'âge AVS
            int moisDebut = 0;
            int moisFin = 0;
            boolean exerciceSur2Annee = false;
            int anneeDebutExercice = JACalendar.getYear(newDecision.getDebutExercice1());
            int anneeFinExercice = JACalendar.getYear(newDecision.getFinExercice1());
            if ((anneeDebutExercice != anneeFinExercice) && newDecision.getTaxation().equalsIgnoreCase("N")
                    && newDecision.getDebutActivite().equals(new Boolean(true))) {
                exerciceSur2Annee = true;
            }
            if (exerciceSur2Annee) {
                if (BSessionUtil.compareDateBetweenOrEqual(process.getSession(), newDecision.getDebutExercice1(),
                        newDecision.getFinExercice1(), dateAvs)) {
                    moisDebut = JACalendar.getMonth(dateAvs) + 1;
                    moisFin = JACalendar.getMonth(newDecision.getFinExercice1());
                    if (JACalendar.getYear(dateAvs) < JACalendar.getYear(newDecision.getFinExercice1())) {
                        moisFin = moisFin + 12;
                    }
                } else {
                    moisDebut = JACalendar.getMonth(newDecision.getDebutExercice1());
                    moisFin = JACalendar.getMonth(newDecision.getFinExercice1()) + 12;
                }
            } else {
                // Voir CPProcessReceptionGenererDecisionTest, si un changement se fait dans cette partie de code, il
                // faudrait adapter le
                // test unitaire
                moisDebut = JACalendar.getMonth(newDecision.getDebutDecision());
                moisFin = JACalendar.getMonth(newDecision.getFinDecision());
                int varNum = Integer.parseInt(newDecision.getNombreMoisTotalDecision());
                if (varNum != 0) {
                    // Recaler la date de début et de fin par rapport à la
                    // période totale
                    int vNum = (moisDebut + varNum) - 1;
                    if (vNum <= 12) { // décalage du mois de fin
                        moisFin = vNum;
                    } else { // Décalage du mois de début
                        vNum = (moisFin - varNum) + 1;
                        if (vNum >= 1) {
                            moisDebut = vNum;
                        } else { // Période ne tenant pas dans la décision
                            moisDebut = 1;
                            moisFin = varNum;
                        }
                    }
                    // Nouveau code pour corriger la problématique des rentiers (K160704_001)
                    if (newDecision.getAnneeDecision().equalsIgnoreCase(Integer.toString(anneeAvs))
                            && moisDebut < (JACalendar.getMonth(dateAvs) + 1)) {
                        moisDebut = JACalendar.getMonth(dateAvs) + 1;
                    }
                }
                if (newDecision.getAnneeDecision().equalsIgnoreCase(Integer.toString(anneeAvs))
                        && BSessionUtil.compareDateFirstLower(getSession(), newDecision.getDebutDecision(), dateAvs)) {
                    moisDebut = JACalendar.getMonth(dateAvs) + 1;
                }
            }
            // Calcul de la franchise
            return mFranchise * ((moisFin - moisDebut) + 1);

        } catch (Exception e) {
            this._addError(process.getTransaction(), " Echec lors du calcul de la franchise pour "
                    + getDescriptionTiers() + " année " + newDecision.getAnneeDecision());
            this._addError(process.getTransaction(), e.toString());
            return 0;
        }
    }

    public boolean genererDecisionByIdDemande(String idDemande) {
        // Sous controle d'exceptions
        try {
            // disable le spy
            getTransaction().disableSpy();
            // Recherche si revenuAutre = revenu AF (ex cas spéciaux AF pour GE)
            boolean revAf = new CPApplication().isRevenuAf();
            // Informations venant de la demande
            EBDemandeModifAcompteEntity demande = new EBDemandeModifAcompteEntity();
            demande.setSession(getSession());
            demande.setIdEntity(idDemande);
            demande.retrieve();

            // Génération de la décision
            if (DemandeModifAcompteStatut.A_TRAITER
                    .equals(DemandeModifAcompteStatut.fromValue(demande.getCsStatut()))) {
                _executeProcessDemandeGenererDecision(demande, revAf);
            }

            // rétablir le contrôle du spy
            getTransaction().enableSpy();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure
        catch (Throwable t) {
            JadeLogger.error(this, t);
        }
        return !isOnError();
    }

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
        listIdTraite = new ArrayList<String>();
        listIdNonTraite = new ArrayList<String>();

        try {
            // Chargement des propriétés

            chargementProprietes();
            // Soit on donne une liste d'id retour
            if ((getListIdDemande() != null) && (getListIdDemande().length != 0)) {
                genererByIdDemande();
                // Soit on donne un id de journal
            } else {
                this._addError(getTransaction(), getSession().getLabel("DEMANDE_PORTAIL_SELECTIONNE"));
            }
        } catch (Exception e) {
            this._addError(getTransaction(),
                    getSession().getLabel("PROCRECEPTGENDEC_EMAIL_OBJECT_FAILED") + " - " + e.toString());
        } finally {

            // Remettre les erreurs des process dans le log
            for (Iterator<?> iter = messages.iterator(); iter.hasNext();) {
                getMemoryLog().getMessagesToVector().add(iter.next());
                processOk = false;
            }
        }
        return processOk;
    }

    public boolean _executeProcessDemandeGenererDecision(EBDemandeModifAcompteEntity demande, boolean revAf) {
        try {
            // Si traitement conjoint => Test si le conjoint est aussi non actif
            // et non radié sinon ignorer
            AFAffiliation affiliation = null;
            // recherche de l'affiliation
            affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(demande.getIdAffiliation());
            affiliation.retrieve();

            this.getTiers(affiliation.getIdTiers());

            // Initialiser la décision
            _initDecision(demande, affiliation);
            // Si décision == null (Ex: caisse externe => ne pas traiter
            if (newDecision == null) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0199"));
            }
            if (!getTransaction().hasErrors()) {
                // recherche de la décisiond de base pour reprendre des données
                // d'encodage
                _calculDemande(newDecision, revAf);
                // Mise à jour décision
                CPDecision decision = new CPDecision();
                decision.setIdDecision(newDecision.getIdDecision());
                decision.setSession(getSession());
                decision.retrieve();
                decision.setIdPassage(getIdPassage());
                decision.setIdDemandePortail(demande.getId());
                decision.setDernierEtat(CPDecision.CS_VALIDATION);
                decision.update();
                EBDemandeModifAcompteEntity dem = new EBDemandeModifAcompteEntity();
                dem.setSession(getSession());
                dem.setIdEntity(demande.getId());
                dem.retrieve();
                dem.setCsStatut(DemandeModifAcompteStatut.VALIDE.getValue());
                dem.update();

            }

            String msg = "";
            if (getSession().hasErrors() || getTransaction().hasErrors()) {
                msg = formatInfoMessageErreur(demande);
                if (getSession().hasErrors()) {
                    getMemoryLog().logMessage(msg + getSession().getErrors().toString(), FWMessage.ERREUR, "");
                } else if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage(msg + getTransaction().getErrors().toString(), FWMessage.ERREUR, "");
                }
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
                getListIdNonTraite().add(demande.getId());
            } else {
                getTransaction().commit();
                getListIdTraite().add(demande.getId());
            }
            // ------------------------------------------------------------------------------------

        } catch (Exception e) {
            String msg = "";
            try {
                msg = formatInfoMessageErreur(demande);
            } catch (Exception e1) {
                msg = demande.getId() + " - ";
            }
            getMemoryLog().logMessage(msg + e.toString(), FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(),
                    getSession().getLabel("PROCDEMANDEGENDEC_EMAIL_OBJECT_FAILED") + ": " + msg + e.toString());

        }
        return true;
    }

    /**
     * Cette méthode est équivalente à CPDecisionViewBean _initEcran
     *
     * @param myDecision
     * @param retour
     * @return
     */
    public CPDecisionViewBean _initDecision(EBDemandeModifAcompteEntity demande, AFAffiliation affiliation) {
        // Reprise de la décision de base
        // charger la vraie décision à partir du fake entity
        cotisation = new AFCotisation();
        // Copier la décision
        try {
            CPDecisionViewBean decisionActuelle = null;
            if (!JadeStringUtil.isBlankOrZero(demande.getIdDecision())) {
                decisionActuelle = new CPDecisionViewBean();
                decisionActuelle.setAffiliation(null);
                decisionActuelle.setIdDecision(demande.getIdDecision());
                decisionActuelle.setProcessExterne(Boolean.TRUE);
                decisionActuelle.retrieve(getTransaction());
                newDecision = (CPDecisionViewBean) decisionActuelle.clone();
            } else {
                newDecision = new CPDecisionViewBean();
            }

            // Si pas de cotisation sous affiliation (ex: caisse externe) => Ne
            // pas traiter
            if (!AFAffiliationUtil.hasCotPersActif(affiliation, "01.01." + demande.getAnnee(),
                    "31.12." + demande.getAnnee())) {
                newDecision = null;
                return null;
            }
            newDecision.setIdDecision("");
            newDecision.setIdDemandePortail("");
            newDecision.setSession(getSession());
            newDecision.setProcessExterne(new Boolean(true));
            // newDecision.setIdTiers(this.getTiers().getIdTiers());
            newDecision.setTiers(this.getTiers());
            if (decisionActuelle != null) {
                newDecision.setTypeDecision(
                        CPUtil.determinerTypeDecision(demande.getAnnee(), decisionActuelle.getTypeDecision()));
            } else {
                newDecision.setGenreAffilie(CPToolBox.conversionTypeAffiliationEnGenreAffilie(affiliation));
                newDecision.setTypeDecision(CPUtil.determinerTypeDecision(demande.getAnnee(), null));
            }
            newDecision.setLettreSignature(lettreSignature);
            try {
                newDecision.setResponsable(getIdUserByVisa(CPApplication.getNomUserPortail()));
            } catch (Exception e) {
                newDecision.setResponsable("");
            }
            // date d'information = date du jour
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            newDecision.setDateInformation(sdf.format(demande.getDateReception()));
            newDecision.setDateDecision(JACalendar.todayJJsMMsAAAA());
            newDecision.setAffiliation(affiliation);
            newDecision.setIdAffiliation(affiliation.getAffiliationId());
            newDecision.setIdTiers(affiliation.getIdTiers());
            if (JadeStringUtil.isBlankOrZero(newDecision.getTaxation())) {
                newDecision.setTaxation("N");
            }
            newDecision.setDebutDecision("01.01." + demande.getAnnee());
            newDecision.setFinDecision("31.12." + demande.getAnnee());

            // Initialisation des données pour le calcul
            _initDonneeBase(newDecision, demande);

            // Recherche période AVS
            cotisation.setSession(getSession());
            cotisation = cotisation._retourCotisation(getTransaction(), affiliation.getAffiliationId(),
                    newDecision.getAnneeDecision(), globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                    globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);

            String varDate = "";
            int anneeFin = 0;
            int anneeDebut = 0;
            if ((cotisation != null) && !JadeStringUtil.isEmpty(cotisation.getDateDebut())) {
                anneeDebut = globaz.globall.util.JACalendar.getYear(cotisation.getDateDebut());
                varDate = cotisation.getDateDebut();
            } else if ((affiliation != null) && !JadeStringUtil.isEmpty(affiliation.getDateDebut())) {
                anneeDebut = globaz.globall.util.JACalendar.getYear(affiliation.getDateDebut());
                varDate = affiliation.getDateDebut();
            }
            // Init date début décision
            if (anneeDebut == Integer.parseInt(newDecision.getAnneeDecision())) {
                newDecision.setDebutDecision(varDate);
            }
            // Si fin d'activité
            if ((cotisation != null) && !JadeStringUtil.isEmpty(cotisation.getDateFin())) {
                anneeFin = globaz.globall.util.JACalendar.getYear(cotisation.getDateFin());
                varDate = cotisation.getDateFin();
            } else if ((affiliation != null) && !JadeStringUtil.isEmpty(affiliation.getDateFin())) {
                anneeFin = globaz.globall.util.JACalendar.getYear(affiliation.getDateFin());
                varDate = affiliation.getDateFin();
            }
            // année de décision = année de fin d'activité => fin période
            // décision = fin d'activité
            if (anneeFin == Integer.parseInt(newDecision.getAnneeDecision())) {
                newDecision.setFinDecision(varDate);
            }
            // Recherche période fiscale et date de fortune
            newDecision._initPeriodeFiscale(newDecision, demande.getAnnee().toString(), anneeDebut);
            String dateAgeAvs = this.getTiers().getDateAvs();
            int anneeAvs = JACalendar.getYear(dateAgeAvs);
            int anneeDec = JACalendar.getYear(newDecision.getDebutDecision());
            // Détermination si rentier
            if ((anneeAvs < anneeDec) || ((anneeAvs == anneeDec)
                    && BSessionUtil.compareDateFirstGreater(getSession(), newDecision.getFinDecision(), dateAgeAvs))) {
                newDecision.setGenreAffilie(CPDecision.CS_RENTIER);
            }
            newDecision.setBloque(new Boolean(false));
            // Si année d'activité = année de décision => debut période
            // décision = début d'activité
            if (!JadeStringUtil.isEmpty(newDecision.getDebutExercice1())
                    && (anneeDebut == JACalendar.getYear(newDecision.getDebutExercice1()))) {
                newDecision.setDebutActivite(new Boolean(true));
            }
            newDecision.setFacturation(new Boolean(true));
            newDecision.setImpression(new Boolean(true));
            // Pas d'intérêt pour ceux qui sont assistés
            if (CPToolBox.isAffilieAssiste(getTransaction(), affiliation, newDecision.getDebutDecision())) {
                newDecision.setInteret(CAInteretMoratoire.CS_EXEMPTE);
            } else {
                newDecision.setInteret(CAInteretMoratoire.CS_AUTOMATIQUE);
            }
            // PCA-619 Calcul des intérets au prorata
            int dureeDecision = JACalendar.getMonth(newDecision.getFinDecision())
                    - JACalendar.getMonth(newDecision.getDebutDecision()) + 1;
            newDecision.setNbMoisExercice1(Integer.toString(dureeDecision));
            newDecision.setNbMoisRevenuAutre1(Integer.toString(dureeDecision));
            newDecision.setDebutExercice1(newDecision.getDebutDecision());
            newDecision.setFinExercice1(newDecision.getFinDecision());

            newDecision.add(getTransaction());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(),
                    getSession().getLabel("CP_MSG_0142") + demande.getNumAffilie() + " " + demande.getId());
        }
        return newDecision;
    }

    private String getIdUserByVisa(String nomUserPortail) {
        String idUser = "";
        try {
            JadeUser user;
            JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
            user = service.loadForVisa(nomUserPortail);
            if (user != null) {
                return user.getIdUser();
            }
        } catch (Exception e) {
            return idUser;
        }
        return idUser;
    }

    /**
     * Cette méthode initialise les données nécessaire pour le calcul de la décision selon la demande en prevenance
     * d'Ebusiness.
     *
     * @param newDecision
     * @param retour
     */
    public void _initDonneeBase(CPDecisionViewBean newDecision, EBDemandeModifAcompteEntity demande) throws Exception {
        newDecision.setComplementaire(new Boolean(false));
        newDecision.setCapital(demande.getCapital().toString());
        newDecision.setRevenu1(demande.getRevenu().toString());
        newDecision.setRevenu2("");
        newDecision.setRevenuAutre1("");
        newDecision.setRevenuAutre2("");
        newDecision.setCapital(demande.getCapital().toString());
        newDecision.setOpposition(Boolean.FALSE);
        newDecision.setRecours(Boolean.FALSE);
        newDecision.setSourceInformation(CPDonneesBase.CS_VOTRE_ESTIMATION);
        // si il n'y a pas de décision de base (cas du 1er acompte de l'année ou nouvel indépendant
        if (JadeStringUtil.isBlankOrZero(demande.getIdDecision())) {
            newDecision.setAnneeDecision(demande.getAnnee().toString());
            newDecision.setNombreMoisTotalDecision("0");
        }
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
        }

        // si pas d'idPassage on essai de le retrouver
        if (JadeStringUtil.isBlankOrZero(idPassage)) {
            // Recherche du prochain passage de facturation
            IFAPassage passage = ServicesFacturation.getProchainPassageFacturation(getSession(), null,
                    FAModuleFacturation.CS_MODULE_COT_PERS_PORTAIL);

            if (passage == null || JadeStringUtil.isIntegerEmpty(passage.getIdPassage())) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0102"));
            } else {
                setIdPassage(passage.getIdPassage());
            }
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    /**
     * Ajoute des informations dans l'email.
     */
    private void addMailInformations() throws Exception {
        getMemoryLog().logMessage(getSession().getLabel("PROCESS_DONNEES_TRAITEES") + " : " + getListIdTraite().size(),
                FWMessage.INFORMATION, this.getClass().getName());
        getMemoryLog().logMessage(
                getSession().getLabel("PROCESS_DONNEES_NON_TRAITEES") + " : " + getListIdNonTraite().size(),
                FWMessage.INFORMATION, this.getClass().getName());
    }

    /**
     * Chargement des propriétés
     *
     * @throws Exception
     */
    protected void chargementProprietes() throws Exception {
        try {
            // type de revenu depuis les properties
            String typeRevenu = new CPApplication().getTypeRevenu();
            setTypeRevenu(typeRevenu);
            lettreSignature = new CPApplication().isLettreSignature();
            setModeCalculArrondiCotPers(CPToolBox.getModeArrondiFad(getTransaction()));
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0137"));
        }
    }

    private String formatInfoMessageErreur(EBDemandeModifAcompteEntity demande) throws Exception {
        return demande.getNumAffilie() + " - " + demande.getAnnee() + " - " + demande.getId() + ": ";
    }

    /**
     * Permet la génération en partant d'une liste d'id de retour
     *
     * @return
     */
    protected boolean genererByIdDemande() {
        try {
            // Init du compteur de progression
            initProgressCounter(getListIdDemande().length);

            // Pour chaque idRetour donné
            for (int i = 0; i < getListIdDemande().length; i++) {
                String idDemande = getListIdDemande()[i];
                try {
                    if (!JadeStringUtil.isEmpty(idDemande)) {
                        genererDecisionByIdDemande(idDemande);
                    }
                } catch (Exception e) {
                    this._addError(getTransaction(),
                            getSession().getLabel("CP_MSG_0139") + idDemande + " - " + e.toString());
                }
                if (isAborted()) {
                    return false;
                }
                // On incremente le compteur
                incProgressCounter();
                if (getSession().hasErrors()) {
                    getMemoryLog().logMessage(getSession().getErrors().toString() + " - " + idRetour, FWMessage.ERREUR,
                            "");
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                }
                if (getTransaction().hasErrors()) {
                    getMemoryLog().logMessage(getTransaction().getErrors().toString() + " - " + idDemande,
                            FWMessage.ERREUR, "");
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                } else {
                    getTransaction().commit();
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, this.getClass().getName());
        } finally {
            for (Iterator iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                messages.add(iter.next());
            }
            getMemoryLog().clear();
        }
        return true;
    }

    public String getDescriptionTiers() {
        return descriptionTiers;
    }

    @Override
    protected String getEMailObject() {

        if (getMemoryLog().hasErrors() || !processOk) {
            return getSession().getLabel("PROCDEMANDEGENDEC_EMAIL_OBJECT_FAILED");
        }

        return getSession().getLabel("SUJET_EMAIL_RECEPTION_CALCUL");
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    public String getForStatus() {
        return forStatus;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public CPJournalRetour getJournal() {
        return journal;
    }

    public ArrayList<String> getListIdNonTraite() {
        return listIdNonTraite;
    }

    public String[] getListIdDemande() {
        return listIdDemande;
    }

    public ArrayList<String> getListIdTraite() {
        return listIdTraite;
    }

    public int getModeCalculArrondiCotPers() {
        return modeCalculArrondiCotPers;
    }

    public TITiersViewBean getTiers() {
        return tiers;
    }

    /**
     * Récupération d'un tiers
     *
     * @param idTiers
     * @return
     * @throws Exception
     */
    protected TITiersViewBean getTiers(String idTiers) throws Exception {

        if ((this.getTiers() == null) || !this.getTiers().getIdTiers().equals(idTiers)) {
            TITiersViewBean persAvs = new TITiersViewBean();
            persAvs.setSession(getSession());
            persAvs.setIdTiers(idTiers);
            persAvs.retrieve();

            if (!persAvs.isNew()) {
                setTiers(persAvs);
            } else {
                setTiers(null);
            }
        }

        return this.getTiers();
    }

    public String getTypeRevenu() {
        return typeRevenu;
    }

    /**
     * Permet d'initialiser la progress bar
     *
     * @param nbOccurence
     */
    protected void initProgressCounter(int nbOccurence) {

        if (nbOccurence > 0) {
            setProgressScaleValue(nbOccurence);
        } else {
            setProgressScaleValue(1);
        }
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setDescriptionTiers(String newDescriptionTiers) {
        descriptionTiers = newDescriptionTiers;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    // **************************************
    // * Setter
    // **************************************

    public void setForStatus(String string) {
        forStatus = string;
    }

    public void setFromNumAffilie(String string) {
        fromNumAffilie = string;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setJournal(CPJournalRetour journal) {
        this.journal = journal;
    }

    public void setListIdNonTraite(ArrayList<String> listIdNonTraite) {
        this.listIdNonTraite = listIdNonTraite;
    }

    public void setListIdDemande(String[] listIdDemande) {
        this.listIdDemande = listIdDemande;
    }

    public void setListIdTraite(ArrayList<String> listIdTraite) {
        this.listIdTraite = listIdTraite;
    }

    public void setModeCalculArrondiCotPers(int modeCalculArrondiCotPers) {
        this.modeCalculArrondiCotPers = modeCalculArrondiCotPers;
    }

    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    public void setTypeRevenu(String typeRevenu) {
        this.typeRevenu = typeRevenu;
    }

}
