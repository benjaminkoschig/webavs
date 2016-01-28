package globaz.phenix.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageModule;
import globaz.musca.db.facturation.FAPassageModuleManager;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.utils.CAUtil;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDecisionAffiliationTiers;
import globaz.phenix.db.principale.CPDecisionAffiliationTiersManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPSortie;
import globaz.phenix.db.principale.CPSortieManager;
import globaz.phenix.db.principale.CPSortieMontant;
import globaz.phenix.db.principale.CPSortieMontantManager;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public final class CPProcessFacturation extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation affiliation = null;
    private String anneeEncours = "";
    private CACompteAnnexe compteAnnexe = null;
    private CPDecisionAffiliationTiers decision = null;
    private IFAPassage dernierPassageInd = null;
    private IFAPassage dernierPassageNac = null;
    private boolean factureParAnnee = false;

    private java.lang.String fromNumAffilie = "";

    private java.lang.String idAffiliationPrec = "";
    // dernier passage de facturation contenant le module cot.pers.
    private String idModuleFacturation;
    private java.lang.String idPassage = "";
    private java.lang.String idTiersPrec = "";
    private Vector messages = new Vector();
    private FAPassage passagePeriodique = null;
    private boolean remboursementFraisAdmin = false;
    private String role = ""; // Recherche du compte du tiers
    private BSession sessionOsiris = null;
    private TITiersViewBean tiers = null;
    private java.lang.String tillNumAffilie = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessFacturation() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessFacturation(BProcess parent) {
        super(parent);
    }

    /**
     * Création de l'entête de facture
     * 
     * @return FAEnteteFacture
     */
    protected FAEnteteFacture _enteteFacturation(String referenceFacture) {
        try {
            // Création entete facture si inexistantes
            FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
            entFactureManager.setSession(getSession());
            entFactureManager.setForIdTiers(getDecision().getIdTiers());
            entFactureManager.setForIdPassage(getIdPassage());
            if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(getDecision().getGenreAffilie())) {
                entFactureManager.setForIdTypeFacture(APISection.ID_TYPE_SECTION_ETUDIANTS);
                entFactureManager.setForIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT);
            } else {
                entFactureManager.setForIdTypeFacture(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
                entFactureManager.setForIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS);
            }
            entFactureManager.setForIdExterneRole(getAffiliation().getAffilieNumero());
            if (isFactureParAnnee() || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getDecision().getGenreAffilie())) {
                entFactureManager.setLikeIdExterneFacture(decision.getAnneeDecision());
            }
            // Osiris CS_DECISION_COT_PERS
            entFactureManager.setForIdRole(getRole());
            entFactureManager.find(getTransaction());
            if (entFactureManager.size() == 0) {
                FAEnteteFacture nouveauEntFacture = new FAEnteteFacture();
                nouveauEntFacture.setSession(getSession());
                nouveauEntFacture.setIdPassage(getIdPassage());
                nouveauEntFacture.setIdTiers(getDecision().getIdTiers());
                nouveauEntFacture.setIdRole(getRole());
                nouveauEntFacture.setIdExterneRole(getAffiliation().getAffilieNumero());
                // constante
                if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(getDecision().getGenreAffilie())) {
                    nouveauEntFacture.setIdTypeFacture(APISection.ID_TYPE_SECTION_ETUDIANTS);
                    nouveauEntFacture.setIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT);
                } else {
                    nouveauEntFacture.setIdTypeFacture(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
                    nouveauEntFacture.setIdSousType(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS);
                }
                // Recherche du n° de facture
                String numFacture = "";
                // Pour les étudiants, générer une section sur l'année de
                // décision -> Voeux de AGLAU
                if (isFactureParAnnee() || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getDecision().getGenreAffilie())) {
                    numFacture = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(), getRole(),
                            getAffiliation().getAffilieNumero(), nouveauEntFacture.getIdTypeFacture(),
                            decision.getAnneeDecision(), nouveauEntFacture.getIdSousType());
                } else {
                    Calendar c = Calendar.getInstance();
                    numFacture = CAUtil.creerNumeroSectionUnique(sessionOsiris, getTransaction(), getRole(),
                            getAffiliation().getAffilieNumero(), nouveauEntFacture.getIdTypeFacture(),
                            Integer.toString(c.get(Calendar.YEAR)), nouveauEntFacture.getIdSousType());
                }
                nouveauEntFacture.setIdExterneFacture(numFacture);
                nouveauEntFacture.setIdSoumisInteretsMoratoires(getDecision().getInteret());
                nouveauEntFacture.setNonImprimable(new Boolean(false));
                // DGI init plan
                nouveauEntFacture.initDefaultPlanValue(getRole());
                if (CPDecision.CS_REMISE.equalsIgnoreCase(getDecision().getTypeDecision())) {
                    nouveauEntFacture.setNonImprimable(Boolean.TRUE);
                }
                if (!JadeStringUtil.isEmpty(referenceFacture)) {
                    nouveauEntFacture.setReferenceFacture(referenceFacture); // cas ou l'on vient des sorties
                } else {
                    nouveauEntFacture.setReferenceFacture(getDecision().getCollaborateur());
                }
                nouveauEntFacture.add(getTransaction());
                return nouveauEntFacture;
            } else {
                FAEnteteFacture entity = (FAEnteteFacture) entFactureManager.getFirstEntity();
                return entity;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return null;
        }
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
        // Lire les décisions du passage
        CPDecisionAffiliationTiersManager decManager = new CPDecisionAffiliationTiersManager();
        decManager.setSession(getSession());
        decManager.setForIdPassage(getIdPassage());
        decManager.setForIsFacturation(true);
        decManager.setSelectMaxDateInformation(new Boolean(false));
        decManager.orderByNumAffilie();
        decManager.orderByAnnee();
        decManager.orderByIdDecision();

        BStatement statement = null;
        BTransaction transactionLecture = null;
        // Sous controle d'exceptions
        try {
            factureParAnnee = ((CPApplication) getSession().getApplication()).isFactureParAnnee();
            remboursementFraisAdmin = ((CPApplication) getSession().getApplication()).isRemboursementFraisAdmin();
            // Création d'une session Osiris
            sessionOsiris = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            getSession().connectSession(sessionOsiris);
            transactionLecture = (BTransaction) getSession().newTransaction();
            transactionLecture.openTransaction();
            statement = decManager.cursorOpen(transactionLecture);
            CPDecisionAffiliationTiers myDecision = null;
            anneeEncours = Integer.toString(JACalendar.getYear(JACalendar.today().toString()));
            // compteur du progress
            long progressCounter = 0;
            // Recherche du role
            setRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication()));
            // Permet d'afficher les données
            setProgressScaleValue(decManager.size());
            FAPassageModuleManager modPass = new FAPassageModuleManager();
            modPass.setSession(getSession());
            modPass.setForIdPassage(getIdPassage());
            modPass.setInTypeModule(FAModuleFacturation.CS_MODULE_COT_PERS + ", "
                    + FAModuleFacturation.CS_MODULE_COT_PERS_IND + ", " + FAModuleFacturation.CS_MODULE_COT_PERS_NAC
                    + ", " + FAModuleFacturation.CS_MODULE_STANDARD);
            modPass.find();
            if (modPass.size() > 0) {
                // -------------------------------------------------------------------------
                // Recherche si périodique personnelle en cours (hormis
                // éventuellement celle de ce passage)
                findPeriodiqueEnCours();
                // Recherche de la date de dernière facturation cot pers.
                setDernierPassageInd(ServicesFacturation.getDernierPassageFacturation(getSession(), getTransaction(),
                        FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS + ", "
                                + FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_IND));
                setDernierPassageNac(ServicesFacturation.getDernierPassageFacturation(getSession(), getTransaction(),
                        FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS + ", "
                                + FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_NAC));
            }
            if (JadeStringUtil.isEmpty(getIdModuleFacturation())) {
                this._addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0193"));
            }

            // -------------------------------------------------------------------------
            // itérer sur toutes les décisions de factures
            /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
             * Facturer les décisions
             **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
            while (((myDecision = (CPDecisionAffiliationTiers) decManager.cursorReadNext(statement)) != null)
                    && (!myDecision.isNew()) && !isAborted()) {
                // ---------------------------------------------------------------------
                setProgressCounter(progressCounter++);
                // ---------------------------------------------------------------------

                setDecision(myDecision);

                if (myDecision.isFacturation().equals(new Boolean(true))
                        || (myDecision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION))) {

                    String etat = myDecision.getEtat();

                    if ((CPDecision.CS_VALIDATION.equalsIgnoreCase(etat) || CPDecision.CS_REPRISE
                            .equalsIgnoreCase(etat))
                            && (!CPDecision.CS_NON_SOUMIS.equalsIgnoreCase(getDecision().getGenreAffilie()))) {

                        // Traitement pour une décision
                        _executeProcessParDecision();
                    }
                }
            }
            /***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
             * Facturer les sorties
             **************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
            _executeProcessSortieDecision();
            // Remettre les erreurs des process dans le log
            for (Iterator<?> iter = messages.iterator(); iter.hasNext();) {
                getMemoryLog().getMessagesToVector().add(iter.next());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure

        finally {
            try {
                try {
                    decManager.cursorClose(statement);
                } finally {
                    if (transactionLecture != null) {
                        transactionLecture.closeTransaction();
                    }
                    statement = null;
                    // Remettre les erreurs des process dans le log
                    for (Iterator<?> iter = messages.iterator(); iter.hasNext();) {
                        getMemoryLog().getMessagesToVector().add(iter.next());
                    }
                }
            } catch (Exception e) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0132") + " " + e.getMessage());
                this._addError(getTransaction(), e.getMessage());
            }
        }
        return !isOnError();

    }

    /**
     * Création des factures Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    protected boolean _executeProcessParDecision() { // Vérifier la décision
        if (getDecision().isNew() || JadeStringUtil.isIntegerEmpty(decision.getIdDecision())) {
            return false;
        }
        // Sous controle d'exceptions
        try {
            // Extraction tiers
            TITiersViewBean tiersAvs = new TITiersViewBean();
            CPDecision decEnCours = null;
            boolean reduction = false;
            if (!idTiersPrec.equalsIgnoreCase(getDecision().getIdTiers())) {
                setIdTiersPrec(getDecision().getIdTiers());
                tiersAvs.setSession(getSession());
                tiersAvs.setIdTiers(getDecision().getIdTiers());
                tiersAvs.retrieve(getTransaction());
            } else {
                tiersAvs = getTiers();
            }
            if ((tiersAvs != null) && !tiersAvs.isNew()) {
                // Extraction donnée base
                CPDonneesBase donneesBase = new CPDonneesBase();
                donneesBase.setSession(getSession());
                donneesBase.setIdDecision(decision.getIdDecision());
                donneesBase.retrieve();
                // Extraction affiliation
                AFAffiliation affiliation = new AFAffiliation();
                affiliation = _getAffiliation();
                if ((affiliation != null) && !affiliation.isNew()) {
                    try {
                        setTiers(tiersAvs);
                        setAffiliation(affiliation);
                        // Tests si la décision peut être facturée
                        boolean aFacturer = true;
                        if (getDecision().getAnneeDecision().equalsIgnoreCase(anneeEncours)
                                && (passagePeriodique != null)
                                && !CPToolBox.controlePeriodique(getSession(), getDecision().getDebutDecision(),
                                        getDecision().getFinDecision(), passagePeriodique, anneeEncours,
                                        getAffiliation())) {
                            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0191"));
                            aFacturer = false;
                        }
                        if (aFacturer) {
                            String numCaisse = AFAffiliationUtil.getIdCaissePrincipale(affiliation, false,
                                    decision.getDebutDecision());
                            // Création Afact pour Mise en compte
                            if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
                                // Créé ou retourne l'entête de facture si il en
                                // existe
                                // une pour le tiers
                                FAEnteteFacture enteteFacture = _enteteFacturation("");
                                // Recherche du montant à mettre en compte
                                // (encodage)
                                CPDonneesBase donneeBase = new CPDonneesBase();
                                donneeBase.setSession(getSession());
                                donneeBase.setIdDecision(decision.getIdDecision());
                                donneeBase.retrieve();
                                // Création afact
                                FAAfact afact = new FAAfact();
                                afact.setNoCheckPlausiRubriqueMasse(true);
                                afact.setSession(getSession());
                                // compte de cotisation personnelle :
                                // 2110.4000.0000
                                // frais d'administration cotisation personnelle
                                // 9100.6000.1000
                                CAReferenceRubrique refRub = new CAReferenceRubrique();
                                refRub.setSession(getSession());
                                afact.setIdRubrique(refRub.getRubriqueByCodeReference(
                                        APIReferenceRubrique.IMPUTATION_DE_COTISATION_PERSONNELLE).getIdRubrique());
                                if (JadeStringUtil.isIntegerEmpty(afact.getIdRubrique())) {
                                    this._addError(getTransaction(), getSession().getLabel("CP_MSG_0109") + " "
                                            + affiliation.getAffilieNumero() + " " + tiersAvs.getNom()
                                            + getSession().getLabel("CP_MSG_0109A") + " "
                                            + getDecision().getAnneeDecision());
                                }
                                // afact.setIdExterneRubrique(Constante.COMPTE_MISE_ENCOMPTE);
                                afact.setAnneeCotisation(getDecision().getAnneeDecision());
                                afact.setIdPassage(getIdPassage());
                                afact.setIdEnteteFacture(enteteFacture.getIdEntete());
                                afact.setNonComptabilisable(new Boolean(false));
                                float miseEnCompte = 0;
                                miseEnCompte = Float.parseFloat(JANumberFormatter.deQuote(donneeBase.getCotisation1()));
                                miseEnCompte = 0 - miseEnCompte;
                                afact.setIdModuleFacturation(getIdModuleFacturation());
                                afact.setNonImprimable(new Boolean(false));
                                if (CAInteretMoratoire.CS_EXEMPTE.equalsIgnoreCase(getDecision().getInteret())) {
                                    afact.setReferenceExterne(getDecision().getInteret() + ".");
                                } else {
                                    afact.setReferenceExterne(getDecision().getTypeDecision() + "."
                                            + getDecision().getIdDecision());
                                }
                                afact.setNumCaisse(numCaisse);
                                afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
                                if (Boolean.TRUE.equals(getDecision().isComplementaire())) {
                                    float miseEnCompteDejaFacture = Float.parseFloat(JANumberFormatter
                                            .deQuote(CPToolBox.rechMontantFacture(getSession(), getTransaction(),
                                                    getCompteAnnexe().getIdCompteAnnexe(), afact.getIdRubrique(),
                                                    getDecision().getAnneeDecision())));
                                    // miseEnCompteDejaFacture = (float) 0 -
                                    // miseEnCompteDejaFacture;
                                    FWCurrency varCurrency = new FWCurrency(miseEnCompte);
                                    varCurrency.sub(new FWCurrency(miseEnCompteDejaFacture));
                                    miseEnCompte = varCurrency.floatValue();
                                    afact.setMontantFacture(Float.toString(miseEnCompte));
                                } else {
                                    afact.setMontantFacture(Float.toString(miseEnCompte));

                                }
                                afact.add(getTransaction());
                                // Si remboursement des frais pour les
                                // imputations
                                if (isRemboursementFraisAdmin()) {
                                    String fraisImputation = CPToolBox.calculFraisImputation(
                                            donneeBase.getCotisation1(), decision, this);
                                    try {
                                        afact.setIdRubrique(refRub.getRubriqueByCodeReference(
                                                APIReferenceRubrique.IMPUTATION_DE_FAD_COTISATION_PERSONNELLE)
                                                .getIdRubrique());
                                    } catch (Exception e) {
                                        afact.setIdRubrique("");
                                    }
                                    if (JadeStringUtil.isIntegerEmpty(afact.getIdRubrique())) {
                                        this._addError(getTransaction(), getSession().getLabel("CP_MSG_0109") + " "
                                                + affiliation.getAffilieNumero() + " " + tiersAvs.getNom()
                                                + getSession().getLabel("CP_MSG_0109A") + " "
                                                + getDecision().getAnneeDecision());
                                    }
                                    float fraisImp = 0;
                                    fraisImp = Float.parseFloat(JANumberFormatter.deQuote(fraisImputation));
                                    fraisImp = 0 - fraisImp;
                                    if (Boolean.TRUE.equals(getDecision().isComplementaire())) {
                                        float fraisImpDejaFacture = Float.parseFloat(JANumberFormatter
                                                .deQuote(CPToolBox.rechMontantFacture(getSession(), getTransaction(),
                                                        getCompteAnnexe().getIdCompteAnnexe(), afact.getIdRubrique(),
                                                        getDecision().getAnneeDecision())));
                                        // fraisImpDejaFacture = (float) 0 -
                                        // fraisImpDejaFacture;
                                        // fraisImp = fraisImp -
                                        // fraisImpDejaFacture;
                                        FWCurrency varCurrency = new FWCurrency(fraisImp);
                                        varCurrency.sub(new FWCurrency(fraisImpDejaFacture));
                                        fraisImp = varCurrency.floatValue();
                                        afact.setMontantFacture(Float.toString(fraisImp));
                                    } else {
                                        afact.setMontantFacture(Float.toString(fraisImp));
                                    }
                                    afact.add(getTransaction());
                                }
                            } else {
                                // PO 2360 - Ne pas calculer d'intérêt 25% pour
                                // ceux qui n'avaient pas de décision de base
                                // et ceux qui avaient une activité accessoire.
                                // Aller rechercher l'ancienne décision pour
                                // réduction et facturation des intérêts (PO
                                // 2360)
                                boolean wantInteretRetro = true;
                                decEnCours = CPDecision._returnDecisionBase(getSession(), decision.getIdDecision());
                                if (decEnCours == null) { // Affiliation
                                    // rétroactive
                                    wantInteretRetro = false;
                                } else if (!decEnCours.isNonActif()) { // ->
                                    // test
                                    // si
                                    // activité
                                    // accessoire,
                                    // ne
                                    // concerne
                                    // pas
                                    // les
                                    // non-actifs
                                    // Le 05.09.2011 - CPDonneesCalcul donnee = new CPDonneesCalcul();
                                    // donnee.setSession(this.getSession());
                                    // if (donnee.existeDonnee(decEnCours.getIdDecision(),
                                    // CPDonneesCalcul.CS_ACT_ACCESSOIRE)) {
                                    // wantInteretRetro = false;
                                    // }
                                }
                                // Si réduction ou remise => recherche de la
                                // dernière décision
                                if (CPDecision.CS_REDUCTION.equalsIgnoreCase(decision.getTypeDecision())) {
                                    reduction = true;
                                }
                                // Création pour chaque cotisation si le nombre
                                // de mois à facturer est > à 0
                                CPCotisationManager cotiManager = new CPCotisationManager();
                                cotiManager.setSession(getSession());
                                cotiManager.setForIdDecision(decision.getIdDecision());
                                cotiManager.find(getTransaction());
                                // Création ligne facture (afacts)
                                boolean imputationTraitee = false;
                                for (int i = 0; i < cotiManager.size(); i++) {
                                    boolean casAssuranceUniquementRadiee = false; // PO
                                    // 2036
                                    CPCotisation coti = (CPCotisation) cotiManager.getEntity(i);
                                    // Détermination du nombre de mois à
                                    // facturer
                                    int nbMoisPeriode = 0;
                                    if (coti.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                                        nbMoisPeriode = 3;
                                    } else if (coti.getPeriodicite().equalsIgnoreCase(CodeSystem.PERIODICITE_MENSUELLE)) {
                                        nbMoisPeriode = 1;
                                    } else {
                                        nbMoisPeriode = 12;
                                    } // Recherche du nombre de mois à facturer
                                    CPToolBox toolBox = new CPToolBox();
                                    int nbMois = 0;
                                    // Si Etudiant => facturer le montant annuel
                                    // (facturation immédinate) car
                                    // l'affiliation
                                    // n'est pas mise à jour => pas de
                                    // périodique
                                    if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())
                                            || CPDecision.CS_REDUCTION.equalsIgnoreCase(decision.getTypeDecision())
                                            || CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) {
                                        nbMois = 99;
                                    } else {
                                        if (getDecision().isNonActif()) {
                                            nbMois = toolBox.nbMoisFacture(getSession(), coti, affiliation,
                                                    getDernierPassageNac());
                                        } else {
                                            nbMois = toolBox.nbMoisFacture(getSession(), coti, affiliation,
                                                    getDernierPassageInd());
                                        }
                                    }
                                    if (nbMois > 0) {
                                        // Si le nombre de mois n'est pas
                                        // divisible par le nombre de mois de la
                                        // période
                                        // de facturation de l'affilié, il faut
                                        // prendre le montant mensuel.
                                        // Calcul du nombre de période à
                                        // facturer
                                        int nbPeriode = 0;
                                        if ((nbMois % nbMoisPeriode) == 0) {
                                            nbPeriode = nbMois / nbMoisPeriode;
                                        } else {
                                            nbPeriode = nbMois;
                                        }
                                        if (nbPeriode > 0) {
                                            // Créé ou retourne l'entête de
                                            // facture si il en existe
                                            // une pour le tiers
                                            FAEnteteFacture enteteFacture = _enteteFacturation("");
                                            FAAfact afact = new FAAfact();
                                            afact.setNoCheckPlausiRubriqueMasse(true);
                                            afact.setSession(getSession());
                                            // PO 2036
                                            if (Integer.parseInt(decision.getAnneeDecision()) != JACalendar
                                                    .getYear(coti.getFinCotisation())) {
                                                casAssuranceUniquementRadiee = true;
                                            }
                                            if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) {
                                                afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
                                            } else {
                                                afact.setIdTypeAfact(FAAfact.CS_AFACT_TABLEAU);
                                            }
                                            if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())
                                                    || CPDecision.CS_REDUCTION.equalsIgnoreCase(decision
                                                            .getTypeDecision())) {
                                                CAReferenceRubrique refRub = new CAReferenceRubrique();
                                                refRub.setSession(getSession());
                                                AFCotisation cotiAf = new AFCotisation();
                                                cotiAf.setSession(getSession());
                                                cotiAf.setCotisationId(coti.getIdCotiAffiliation());
                                                cotiAf.retrieve();
                                                String rubExterne = "";
                                                if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) {
                                                    rubExterne = cotiAf.getAssurance().getParametreAssuranceValeur(
                                                            CodeSystem.GEN_PARAM_ASS_REMISE, coti.getDebutCotisation(),
                                                            "");
                                                } else {
                                                    rubExterne = cotiAf.getAssurance().getParametreAssuranceValeur(
                                                            CodeSystem.GEN_PARAM_ASS_REDUCTION,
                                                            coti.getDebutCotisation(), "");
                                                }
                                                if (!JadeStringUtil.isNull(rubExterne)
                                                        && !JadeStringUtil.isEmpty(rubExterne)) {
                                                    afact.setIdExterneRubrique(rubExterne);
                                                    afact.setIdRubrique("");
                                                } else {
                                                    afact.setIdRubrique(AFCotisation._getRubrique(
                                                            coti.getIdCotiAffiliation(), getSession()));
                                                }
                                            } else {
                                                afact.setIdRubrique(AFCotisation._getRubrique(
                                                        coti.getIdCotiAffiliation(), getSession()));
                                                afact.setIdExterneRubrique("");
                                            }
                                            if (JadeStringUtil.isIntegerEmpty(afact.getIdRubrique())
                                                    && JadeStringUtil.isIntegerEmpty(afact.getIdExterneRubrique())) {
                                                this._addError(
                                                        getTransaction(),
                                                        getSession().getLabel("CP_MSG_0109") + " "
                                                                + affiliation.getAffilieNumero() + " "
                                                                + tiersAvs.getNom()
                                                                + getSession().getLabel("CP_MSG_0109A") + " "
                                                                + getDecision().getAnneeDecision());
                                            }
                                            afact.setAnneeCotisation(getDecision().getAnneeDecision());
                                            afact.setIdPassage(getIdPassage());
                                            afact.setIdEnteteFacture(enteteFacture.getIdEntete());
                                            afact.setNonComptabilisable(new Boolean(false));
                                            if (CAInteretMoratoire.CS_EXEMPTE.equalsIgnoreCase(getDecision()
                                                    .getInteret())) {
                                                afact.setReferenceExterne(getDecision().getInteret() + ".");
                                            } else {
                                                afact.setReferenceExterne(getDecision().getTypeDecision() + "."
                                                        + getDecision().getIdDecision());
                                            }
                                            // PO 2360 - Ne pas calculer d'intérêt 25% pour ceux qui n'avaient pas de
                                            // décision de base
                                            // et ceux qui avaient une activité accessoire.
                                            // PO 7688 - Inforom290 - Intêret spécial pour reprise d'impôt
                                            if (!donneesBase.isNew()
                                                    && CPDonneesBase.CS_REPRISE_IMPOT.equalsIgnoreCase(donneesBase
                                                            .getSourceInformation())) {
                                                afact.setTypeCalculInteretMoratoire(CAInteretMoratoire.CS_REPRISE_IMPOT);
                                            } else if (wantInteretRetro) {
                                                afact.setTypeCalculInteretMoratoire(CAInteretMoratoire.CS_AFFILIATION_RETROACTIVE);
                                            } else {
                                                afact.setTypeCalculInteretMoratoire("");
                                            }
                                            // Calcul du montant initial
                                            float montant = 0;
                                            // Si tout est rétro => prendre le
                                            // montant annuel de la cotisation -
                                            // Indiquer par nbMois=99
                                            if (nbMois == 99) {
                                                if (!JadeStringUtil.isBlank(coti.getMontantAnnuel())) {
                                                    montant = Float.parseFloat(JANumberFormatter.deQuote(coti
                                                            .getMontantAnnuel()));
                                                }
                                                nbPeriode = 1;
                                            } else {
                                                if ((nbMois % nbMoisPeriode) == 0) {
                                                    if (coti.getPeriodicite().equalsIgnoreCase(
                                                            CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                                                        if (!JadeStringUtil.isBlank(coti.getMontantTrimestriel())) {
                                                            montant = Float.parseFloat(JANumberFormatter.deQuote(coti
                                                                    .getMontantTrimestriel()));
                                                        }
                                                    } else if (coti.getPeriodicite().equalsIgnoreCase(
                                                            CodeSystem.PERIODICITE_MENSUELLE)) {
                                                        if (!JadeStringUtil.isBlank(coti.getMontantMensuel())) {
                                                            montant = Float.parseFloat(JANumberFormatter.deQuote(coti
                                                                    .getMontantMensuel()));
                                                        }
                                                    } else {
                                                        if (!JadeStringUtil.isBlank(coti.getMontantAnnuel())) {
                                                            montant = Float.parseFloat(JANumberFormatter.deQuote(coti
                                                                    .getMontantAnnuel()));
                                                        }
                                                    }
                                                } else if (!JadeStringUtil.isBlank(coti.getMontantMensuel())) {
                                                    montant = Float.parseFloat(JANumberFormatter.deQuote(coti
                                                            .getMontantMensuel()));
                                                }
                                            } // -- calcul
                                            FWCurrency montantInt = new FWCurrency("" + montant);
                                            montantInt = new FWCurrency(montantInt.doubleValue() * nbPeriode);
                                            montant = Float.parseFloat(montantInt.toString());
                                            // Forcer le montant initial à 0 si
                                            // il est infèrieur au montant
                                            // minimum défini dans l'assurance
                                            if (Boolean.TRUE.equals(coti.getAForceAZero())) {
                                                montant = 0;
                                            }
                                            setMontantAfact(decEnCours, reduction, getCompteAnnexe(), coti, afact,
                                                    montant, donneesBase);
                                            if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact
                                                    .getMontantInitial()))
                                                    || !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact
                                                            .getMontantDejaFacture()))
                                                    || !JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(afact
                                                            .getMontantFacture()))) {
                                                afact.setIdModuleFacturation(getIdModuleFacturation());
                                                afact.setNonImprimable(new Boolean(false));
                                                // PO 2036
                                                if (!casAssuranceUniquementRadiee) {
                                                    afact.setDebutPeriode(coti.getDebutCotisation());
                                                    if (nbMois != 99) {
                                                        JACalendar cal = new JACalendarGregorian();
                                                        afact.setFinPeriode(cal.lastInMonth(cal.addMonths(
                                                                coti.getDebutCotisation(), nbMois - 1)));
                                                    } else {
                                                        afact.setFinPeriode(coti.getFinCotisation());
                                                    }
                                                } else {
                                                    afact.setDebutPeriode(decision.getDebutDecision());
                                                    if (Integer.parseInt(decision.getAnneeDecision()) == JACalendar
                                                            .getYear(JACalendar.todayJJsMMsAAAA())) {
                                                        if (getDecision().isNonActif()) {
                                                            nbMois = toolBox.nbMoisFacture(getSession(), decision,
                                                                    affiliation, getDernierPassageNac());
                                                        } else {
                                                            nbMois = toolBox.nbMoisFacture(getSession(), decision,
                                                                    affiliation, getDernierPassageInd());
                                                        }
                                                    }
                                                    if (nbMois != 99) {
                                                        JACalendar cal = new JACalendarGregorian();
                                                        afact.setFinPeriode(cal.lastInMonth(cal.addMonths(
                                                                decision.getDebutDecision(), nbMois - 1)));
                                                    } else {
                                                        afact.setFinPeriode(decision.getFinDecision());
                                                    }
                                                }
                                                afact.setNumCaisse(numCaisse);
                                                // DGI ajout du libellé de
                                                // l'assurance
                                                AFCotisation cotiAf = new AFCotisation();
                                                cotiAf.setSession(getSession());
                                                cotiAf.setCotisationId(coti.getIdCotiAffiliation());
                                                cotiAf.retrieve();
                                                if (!cotiAf.isNew()) {
                                                    afact.setLibelle(cotiAf.getAssurance().getAssuranceLibelle(
                                                            getTiers().getLangueIso()));
                                                }
                                                afact.add(getTransaction());
                                            }
                                            coti.setMontantFacture(afact.getMontantDejaFacture());
                                            coti.update(getTransaction());
                                            // Si dispensé => annuler les
                                            // éventuelles imputations
                                            // Ne faire qu'une fois par décision
                                            if (CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification())
                                                    && !imputationTraitee) {
                                                imputationTraitee = true;
                                                FAAfact afact1 = new FAAfact();
                                                afact1.setNoCheckPlausiRubriqueMasse(true);
                                                afact1.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
                                                CAReferenceRubrique refRub = new CAReferenceRubrique();
                                                refRub.setSession(getSession());
                                                afact1.setIdRubrique(refRub.getRubriqueByCodeReference(
                                                        APIReferenceRubrique.IMPUTATION_DE_COTISATION_PERSONNELLE)
                                                        .getIdRubrique());
                                                if (JadeStringUtil.isIntegerEmpty(afact1.getIdRubrique())) {
                                                    this._addError(
                                                            getTransaction(),
                                                            getSession().getLabel("CP_MSG_0109") + " "
                                                                    + affiliation.getAffilieNumero() + " "
                                                                    + tiersAvs.getNom()
                                                                    + getSession().getLabel("CP_MSG_0109A") + " "
                                                                    + getDecision().getAnneeDecision());
                                                }
                                                afact1.setAnneeCotisation(getDecision().getAnneeDecision());
                                                afact1.setNonComptabilisable(new Boolean(false));
                                                afact1.setMontantDejaFacture("");
                                                afact1.setIdPassage(getIdPassage());
                                                afact1.setIdEnteteFacture(enteteFacture.getIdEntete());
                                                afact1.setMontantFacture(CPToolBox.rechMontantFacture(getSession(),
                                                        getTransaction(), getCompteAnnexe().getIdCompteAnnexe(),
                                                        afact1.getIdRubrique(), getDecision().getAnneeDecision()));
                                                afact1.setNonImprimable(new Boolean(false));
                                                afact1.setIdModuleFacturation(getIdModuleFacturation());
                                                afact1.setReferenceExterne(getDecision().getTypeDecision() + "."
                                                        + getDecision().getIdDecision());
                                                afact1.setNumCaisse(numCaisse);
                                                if (!JadeStringUtil.isIntegerEmpty(afact1.getMontantFacture())) {
                                                    // Extourner (multiplier par
                                                    // -1
                                                    afact1.setMontantFacture(new BigDecimal(JANumberFormatter
                                                            .deQuote(afact1.getMontantFacture())).multiply(
                                                            new BigDecimal(-1)).toString());
                                                    afact1.add(getTransaction());
                                                    if (isRemboursementFraisAdmin()) {
                                                        try {
                                                            afact1.setIdRubrique(refRub
                                                                    .getRubriqueByCodeReference(
                                                                            APIReferenceRubrique.IMPUTATION_DE_FAD_COTISATION_PERSONNELLE)
                                                                    .getIdRubrique());
                                                        } catch (Exception e) {
                                                            afact1.setIdRubrique("");
                                                        }
                                                        if (JadeStringUtil.isIntegerEmpty(afact1.getIdRubrique())) {
                                                            this._addError(getTransaction(),
                                                                    getSession().getLabel("CP_MSG_0109") + " "
                                                                            + affiliation.getAffilieNumero() + " "
                                                                            + tiersAvs.getNom()
                                                                            + getSession().getLabel("CP_MSG_0109A")
                                                                            + " " + getDecision().getAnneeDecision());
                                                        }
                                                        afact1.setMontantFacture(CPToolBox.rechMontantFacture(
                                                                getSession(), getTransaction(), getCompteAnnexe()
                                                                        .getIdCompteAnnexe(), afact1.getIdRubrique(),
                                                                getDecision().getAnneeDecision()));
                                                        afact1.setMontantFacture(new BigDecimal(JANumberFormatter
                                                                .deQuote(afact1.getMontantFacture())).multiply(
                                                                new BigDecimal(-1)).toString());
                                                        if (!JadeStringUtil.isBlankOrZero(afact1.getMontantFacture())) {
                                                            afact1.add(getTransaction());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        this._addError(getTransaction(), e.toString());
                    }
                }
            }

            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(
                        getSession().getErrors().toString() + " - " + getSession().getLabel("CP_MSG_0110") + " "
                                + affiliation.getAffilieNumero() + " " + tiersAvs.getNom()
                                + getSession().getLabel("CP_MSG_0109A") + " " + getDecision().getAnneeDecision(),
                        FWMessage.ERREUR, "");
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
            }
            if (getTransaction().hasErrors()) {
                getMemoryLog().logMessage(
                        getTransaction().getErrors().toString() + " - " + getSession().getLabel("CP_MSG_0110") + " "
                                + affiliation.getAffilieNumero() + " " + tiersAvs.getNom()
                                + getSession().getLabel("CP_MSG_0109A") + " " + getDecision().getAnneeDecision(),
                        FWMessage.ERREUR, "");
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
            } else {
                getTransaction().commit();
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0111") + " " + decision.getIdDecision());
        } finally {
            for (Iterator<?> iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                messages.add(iter.next());
            }
            getMemoryLog().clear();
        }
        return !isOnError();
    }

    /**
     * Extourner les décisions du tiers
     */
    protected boolean _executeProcessSortieDecision() {
        try {
            CPSortieManager manager = new CPSortieManager();
            manager.setSession(getSession());
            manager.changeManagerSize(0);
            manager.setForIdPassage(getIdPassage());
            manager.find(getTransaction());
            // Créer ou rechercher toutes les sorties liées au passage
            String anneeTraiteeImputation = "";
            if (manager.getSize() != 0) {

                // Créer des afacts standard
                for (int i = 0; i < manager.getSize(); i++) {

                    CPSortie sortieDecision = (CPSortie) manager.getEntity(i);
                    // PO 5625 Si il existe une décision dans le même passage
                    // => ne rien faire car le montant a déjà été pris dans les montants déjà facturé.
                    CPDecisionAffiliationManager decM = new CPDecisionAffiliationManager();
                    decM.setSession(getSession());
                    decM.setForNoAffilie(sortieDecision.getNoAffilie());
                    decM.setForAnneeDecision(sortieDecision.getAnnee());
                    decM.setForIdPassage(sortieDecision.getIdPassage());
                    if (decM.getCount() == 0) {
                        // Extraction tiers
                        TITiersViewBean tiersAvs = new TITiersViewBean();
                        if (!idTiersPrec.equalsIgnoreCase(sortieDecision.getIdTiers())) {
                            setIdTiersPrec(sortieDecision.getIdTiers());
                            tiersAvs.setSession(getSession());
                            tiersAvs.setIdTiers(sortieDecision.getIdTiers());
                            tiersAvs.retrieve(getTransaction());
                        } else {
                            tiersAvs = getTiers();
                        }
                        if ((tiersAvs != null) && !tiersAvs.isNew()) {
                            // Extraction affiliation
                            AFAffiliation affiliation = new AFAffiliation();
                            if (!idAffiliationPrec.equalsIgnoreCase(sortieDecision.getIdAffiliation())) {
                                setIdAffiliationPrec(sortieDecision.getIdAffiliation());
                                affiliation.setSession(getSession());
                                affiliation.setAffiliationId(sortieDecision.getIdAffiliation());
                                affiliation.retrieve(getTransaction());
                                CACompteAnnexe compte = new CACompteAnnexe();
                                compte.setSession(getSession());
                                compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                                compte.setIdRole(getRole());
                                compte.setIdExterneRole(affiliation.getAffilieNumero());
                                compte.wantCallMethodBefore(false);
                                compte.retrieve(getTransaction());
                                setCompteAnnexe(compte);
                                setAffiliation(affiliation);
                            } else {
                                affiliation = getAffiliation();
                            }
                            if ((affiliation != null) && !affiliation.isNew()) {
                                setTiers(tiersAvs);
                                setAffiliation(affiliation);
                            } else {
                                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0105") + " "
                                        + sortieDecision.getIdDecision());
                                return false;
                            }
                        } else {
                            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0106") + " "
                                    + sortieDecision.getIdDecision());
                            return false;
                        }
                        // Charger la décision originale
                        CPDecisionAffiliationTiersManager decisionManager = new CPDecisionAffiliationTiersManager();
                        decisionManager.setSession(getSession());
                        decisionManager.setForIdDecision(sortieDecision.getIdDecision());
                        decisionManager.setSelectMaxDateInformation(new Boolean(false));
                        decisionManager.find(getTransaction());
                        // il ne peut y avoir une décision avec cet idDecision (clef
                        // primaire)
                        CPDecisionAffiliationTiers decisionOrg = (CPDecisionAffiliationTiers) decisionManager
                                .getEntity(0);
                        if ((decisionOrg == null) || decisionOrg.isNew()) {
                            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0107") + " "
                                    + sortieDecision.getNoAffilie());
                        }
                        // setter la variable d'instance décision avec la décision
                        // originale
                        decisionOrg.setTiers(tiers);
                        decisionOrg.setIdTiers(tiers.getIdTiers());
                        setDecision(decisionOrg);
                        // Créer l'entête de facture
                        FAEnteteFacture facture = _enteteFacturation(sortieDecision.getReferenceFacture());
                        // Si le montant à rembourser n'est pas vide
                        // Création afact
                        FAAfact afact = new FAAfact();
                        afact.setNoCheckPlausiRubriqueMasse(true);
                        afact.setSession(getSession());
                        // si montant sortie > 0 => refacturation. Dans ce cas on regarde ce qu'il y a dans les
                        // compteurs
                        afact.setIdPassage(getIdPassage());
                        afact.setIdEnteteFacture(facture.getIdEntete());
                        afact.setAnneeCotisation(sortieDecision.getAnnee());
                        afact.setNonComptabilisable(new Boolean(false));
                        afact.setIdModuleFacturation(getIdModuleFacturation());
                        afact.setNonImprimable(new Boolean(false));
                        if (CAInteretMoratoire.CS_EXEMPTE.equalsIgnoreCase(decisionOrg.getInteret())) {
                            afact.setReferenceExterne(decisionOrg.getInteret() + ".");
                        } else {
                            afact.setReferenceExterne(decisionOrg.getTypeDecision() + "." + decisionOrg.getIdDecision());
                        }
                        /*
                         * BTC: 20.05.2005 Rajout de la période de sortie dans l'afact
                         */
                        afact.setDebutPeriode(decisionOrg.getDebutDecision());
                        afact.setFinPeriode(decisionOrg.getFinDecision());
                        afact.setNumCaisse(AFAffiliationUtil.getIdCaissePrincipale(affiliation, false,
                                decisionOrg.getDebutDecision()));
                        // Lecture des montants de la sortie
                        CPSortieMontantManager sortieMontantManager = new CPSortieMontantManager();
                        sortieMontantManager.setSession(getSession());
                        sortieMontantManager.setForIdSortie(sortieDecision.getIdSortie());
                        sortieMontantManager.find();
                        for (int j = 0; j < sortieMontantManager.size(); j++) {
                            CPSortieMontant sortieMontant = (CPSortieMontant) sortieMontantManager.getEntity(j);
                            // si le montant n'est pas vide, ajouter l'afact
                            if (!"".equals(sortieMontant.getMontant()) && !"0.00".equals(sortieMontant.getMontant())) {
                                // Si l'assurance est négative => c'est id rubrique
                                // qui est stocké
                                // Ceci est utile par exemple pour les imputations,
                                // les remises et réduction
                                float varTravail = Float.parseFloat(sortieMontant.getAssurance());
                                if (varTravail < 0) {
                                    afact.setIdRubrique(CPToolBox.multString(sortieMontant.getAssurance(), "-1"));
                                } else {
                                    AFCotisation coti = new AFCotisation();
                                    coti.setCotisationId(sortieMontant.getAssurance());
                                    coti.setSession(getSession());
                                    coti.retrieve();
                                    if (!coti.isNew()) {
                                        afact.setIdRubrique(coti.getAssurance().getRubriqueId());
                                    }
                                    // DGI ajout du libellé de l'assurance
                                    afact.setLibelle(coti.getAssurance().getAssuranceLibelle(getTiers().getLangueIso()));
                                }
                                // Test inversé pour l'imputation car la rubrique est négative
                                if (((Float.parseFloat(JANumberFormatter.deQuote(sortieMontant.getMontant())) > 0)
                                        && !CPDecision.CS_IMPUTATION.equalsIgnoreCase(decisionOrg.getTypeDecision()) && !CPDecision.CS_REMISE
                                            .equalsIgnoreCase(decisionOrg.getTypeDecision()))
                                        || ((Float.parseFloat(JANumberFormatter.deQuote(sortieMontant.getMontant())) < 0)
                                                && CPDecision.CS_IMPUTATION.equalsIgnoreCase(decisionOrg
                                                        .getTypeDecision()) && CPDecision.CS_REMISE
                                                    .equalsIgnoreCase(decisionOrg.getTypeDecision()))) {
                                    afact.setIdTypeAfact(FAAfact.CS_AFACT_TABLEAU);
                                    afact.setMontantInitial(sortieMontant.getMontant());
                                    // En cas d'annulation de radiation et s'il y a plusieurs imputations pour une annee
                                    // => aller rechercher dans les compteurs uniqument pour la première
                                    if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decisionOrg.getTypeDecision())
                                            && anneeTraiteeImputation.equalsIgnoreCase(decisionOrg.getAnneeDecision())) {
                                        afact.setMontantDejaFacture("0");
                                    } else {
                                        afact.setMontantDejaFacture(CPToolBox.rechMontantFacture(getSession(),
                                                getTransaction(), compteAnnexe.getIdCompteAnnexe(),
                                                afact.getIdRubrique(), sortieDecision.getAnnee()));
                                    }
                                } else {
                                    afact.setIdTypeAfact(FAAfact.CS_AFACT_STANDART);
                                    afact.setMontantInitial("0");
                                    afact.setMontantDejaFacture("0");
                                    afact.setMontantFacture(sortieMontant.getMontant());
                                }
                                afact.setDebutPeriode(sortieMontant.getDebutSortie());
                                afact.setFinPeriode(sortieMontant.getFinSortie());
                                if (!afact.getMontantInitial().equalsIgnoreCase(afact.getMontantDejaFacture())
                                        || !FAAfact.CS_AFACT_TABLEAU.equalsIgnoreCase(afact.getIdTypeAfact())) {
                                    afact.add(getTransaction());
                                }
                            }
                        }
                        if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decisionOrg.getTypeDecision())) {
                            anneeTraiteeImputation = decisionOrg.getAnneeDecision();
                        }
                        if (getSession().hasErrors()) {
                            getMemoryLog().logMessage(
                                    getSession().getErrors().toString() + " - " + affiliation.getAffilieNumero()
                                            + " - " + sortieDecision.getAnnee(), FWMessage.ERREUR, "");
                            getTransaction().rollback();
                            getTransaction().clearErrorBuffer();
                        }
                        if (getTransaction().hasErrors()) {
                            getMemoryLog().logMessage(
                                    getTransaction().getErrors().toString() + " - " + affiliation.getAffilieNumero()
                                            + " - " + sortieDecision.getAnnee(), FWMessage.ERREUR, "");
                            getTransaction().rollback();
                            getTransaction().clearErrorBuffer();
                        } else {
                            getTransaction().commit();
                        }
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0108") + " " + getDecision().getIdDecision()
                    + " - " + getAffiliation().getAffilieNumero());
        } finally {
            for (Iterator<?> iter = getMemoryLog().getMessagesToVector().iterator(); iter.hasNext();) {
                messages.add(iter.next());
            }
            getMemoryLog().clear();
        }
        return true;
    }

    public AFAffiliation _getAffiliation() throws Exception {
        AFAffiliation affiliation = new AFAffiliation();

        if (!idAffiliationPrec.equalsIgnoreCase(getDecision().getIdAffiliation())) {
            setIdAffiliationPrec(getDecision().getIdAffiliation());
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(getDecision().getIdAffiliation());
            affiliation.retrieve(getTransaction());
            setAffiliation(affiliation);
            CACompteAnnexe compte = new CACompteAnnexe();
            compte.setSession(getSession());
            compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compte.setIdRole(getRole());
            compte.setIdExterneRole(affiliation.getAffilieNumero());
            compte.wantCallMethodBefore(false);
            compte.retrieve(getTransaction());
            setCompteAnnexe(compte);
        } else {
            affiliation = getAffiliation();
        }
        return affiliation;
    }

    protected void findPeriodiqueEnCours() throws Exception {
        // Recherche si périodique personnelle en cours (hormis
        // éventuellement celle de ce passage)
        FAPassageModuleManager mPMng = new FAPassageModuleManager();
        mPMng.setSession(getSession());
        FAModuleFacturation module = new FAModuleFacturation();
        module.setSession(getSession());
        module.setIdModuleFacturation(getIdModuleFacturation());
        module.retrieve();
        if (module.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_COT_PERS_NAC)) {
            mPMng.setInTypeModule(FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS + ", "
                    + FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_NAC);
        } else if (module.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_COT_PERS_IND)) {
            mPMng.setInTypeModule(FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS + ", "
                    + FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_IND);
        } else {
            /* Important de tester les 3 modules dans le cas on la passe repasse en un seule périodique */
            mPMng.setInTypeModule(FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS + ", "
                    + FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_NAC + ", "
                    + FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_IND);
        }
        mPMng.setForExceptStatus(FAPassage.CS_ETAT_OUVERT);
        mPMng.setForStatus(FAPassage.CS_ETAT_NON_COMPTABILISE);
        mPMng.setForExceptIdPassage(getIdPassage());
        mPMng.find();
        if (mPMng.size() > 0) {
            passagePeriodique = new FAPassage();
            passagePeriodique.setIdPassage(((FAPassageModule) mPMng.getFirstEntity()).getIdPassage());
            passagePeriodique.setSession(getSession());
            passagePeriodique.retrieve();
        }
    }

    /**
     * Returns the affiliation.
     * 
     * @return AFAffiliation
     */
    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    public CACompteAnnexe getCompteAnnexe() {
        return compteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 10:02:40)
     * 
     * @return globaz.phenix.db.principale.CPDecision
     */
    public globaz.phenix.db.principale.CPDecisionAffiliationTiers getDecision() {
        return decision;
    }

    public IFAPassage getDernierPassageInd() {
        return dernierPassageInd;
    }

    /**
     * Returns the dernierPassageNac.
     * 
     * @return IFAPassage
     */
    public IFAPassage getDernierPassageNac() {
        return dernierPassageNac;
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
            obj = "Echec lors de la facturation du passage " + getIdPassage();
        }
        // else
        // obj = FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
        // Restituer l'objet
        return obj;
    }

    /**
     * Returns the fromNumAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * Returns the idAffiliationPrec.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAffiliationPrec() {
        return idAffiliationPrec;
    }

    /**
     * Returns the idModuleFacturation.
     * 
     * @return String
     */
    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 11:15:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    /**
     * Returns the idTiers.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTiersPrec() {
        return idTiersPrec;
    }

    public String getRole() {
        return role;
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
     * Returns the tillNumAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTillNumAffilie() {
        return tillNumAffilie;
    }

    /**
     * @return
     */
    public boolean isFactureParAnnee() {
        return factureParAnnee;
    }

    /**
     * @return
     */
    public boolean isRemboursementFraisAdmin() {
        return remboursementFraisAdmin;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Sets the affiliation.
     * 
     * @param affiliation
     *            The affiliation to set
     */
    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setCompteAnnexe(CACompteAnnexe compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 10:02:40)
     * 
     * @param newDecision
     *            globaz.phenix.db.principale.CPDecision
     */
    public void setDecision(globaz.phenix.db.principale.CPDecisionAffiliationTiers newDecision) {
        decision = newDecision;
    }

    public void setDernierPassageInd(IFAPassage dernierPassageInd) {
        this.dernierPassageInd = dernierPassageInd;
    }

    /**
     * Sets the dernierPassageNac.
     * 
     * @param dernierPassageNac
     *            The dernierPassageNac to set
     */
    public void setDernierPassageNac(IFAPassage dernierPassage) {
        dernierPassageNac = dernierPassage;
    }

    /**
     * @param b
     */
    public void setFactureParAnnee(boolean b) {
        factureParAnnee = b;
    }

    /**
     * Sets the fromNumAffilie.
     * 
     * @param fromNumAffilie
     *            The fromNumAffilie to set
     */
    public void setFromNumAffilie(java.lang.String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    /**
     * Sets the idAffiliationPrec.
     * 
     * @param idAffiliationPrec
     *            The idAffiliationPrec to set
     */
    public void setIdAffiliationPrec(java.lang.String idAffiliationPrec) {
        this.idAffiliationPrec = idAffiliationPrec;
    }

    /**
     * Sets the idModuleFacturation.
     * 
     * @param idModuleFacturation
     *            The idModuleFacturation to set
     */
    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
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
     * Sets the idTiers.
     * 
     * @param idTiers
     *            The idTiers to set
     */
    public void setIdTiersPrec(java.lang.String newIdTiersPrec) {
        idTiersPrec = newIdTiersPrec;
    }

    private void setMontantAfact(CPDecision decEnCours, boolean reduction, CACompteAnnexe compte, CPCotisation coti,
            FAAfact afact, float montant, CPDonneesBase donneesBase) throws Exception, NumberFormatException {
        // Extraction du compte annexe et recherche du montant déjà
        // facturé si c'est pas la même année pour le même tiers
        if (CPDecision.CS_REMISE.equalsIgnoreCase(decision.getTypeDecision())) {
            afact.setMontantInitial("0");
            afact.setMontantDejaFacture("0");
            // PO 5067
            // Dans le cas d'un montant de remise négatif (correction), on ne peut annuler
            // au max ce qu'il y a dans les compteurs
            if (montant < 0) {
                String varMontant = CPToolBox.compteurRubrique(getSession(), getTransaction(),
                        compte.getIdCompteAnnexe(), afact.getIdExterneRubrique(), getDecision().getAnneeDecision());
                if (Math.abs(montant) > Math.abs(Float.parseFloat(JANumberFormatter.deQuote(varMontant)))) {
                    montant = new FWCurrency(varMontant).floatValue();
                }
            }
            afact.setMontantFacture(Float.toString(0 - montant));
        } else {
            if (compte != null) {
                if (decision.isComplementaire().equals(new Boolean(false))) {
                    if (reduction) {
                        afact.setMontantDejaFacture(CPCotisation._returnCotisation(getSession(),
                                decEnCours.getIdDecision(), coti.getGenreCotisation()).getMontantAnnuel());
                    } else {
                        afact.setMontantDejaFacture(CPToolBox.rechMontantFacture(getSession(), getTransaction(),
                                compte.getIdCompteAnnexe(), afact.getIdRubrique(), getDecision().getAnneeDecision()));
                    }
                } else {
                    afact.setMontantDejaFacture("0");
                }
            } else {
                afact.setMontantDejaFacture("0");
            }
            // Recherche si cotisation salarié est renseignée,
            // si oui il faut l'ajouter au montant déjà facturé
            // Si dispensé ou franchise => il faut rembourser les montants éventuellements
            // facturés
            if (!CPDecision.CS_SALARIE_DISPENSE.equals(decision.getSpecification())
                    && !CPDecision.CS_FRANCHISE.equalsIgnoreCase(decision.getSpecification())) {
                if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equalsIgnoreCase(coti.getGenreCotisation())
                        || CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(coti.getGenreCotisation())) {
                    // Si salarié dans la même année
                    if ((donneesBase != null) && !donneesBase.isNew()) {
                        if (!JadeStringUtil.isIntegerEmpty(donneesBase.getCotisationSalarie())) {
                            float montantDejaFacture = 0;
                            if (!JadeStringUtil.isIntegerEmpty(afact.getMontantDejaFacture())) {
                                montantDejaFacture = Float.parseFloat(JANumberFormatter.deQuote(afact
                                        .getMontantDejaFacture()));
                            }
                            float cotisationSalarie = Float.parseFloat(JANumberFormatter.deQuote(donneesBase
                                    .getCotisationSalarie()));
                            // Si frais administratif => calculer les frais sur
                            // la cotisation salariée
                            if (coti.getGenreCotisation().equalsIgnoreCase(CodeSystem.TYPE_ASS_FRAIS_ADMIN)) {
                                float taux = Float.parseFloat(JANumberFormatter.deQuote(coti.getTaux()));
                                cotisationSalarie = (new FWCurrency(cotisationSalarie).floatValue() * taux) / 100;
                                cotisationSalarie = JANumberFormatter.round(cotisationSalarie, 0.05, 2,
                                        JANumberFormatter.NEAR);
                            }
                            afact.setMontantDejaFacture(montantDejaFacture + cotisationSalarie + "");
                        }
                    }
                }
                afact.setMontantInitial(Float.toString(montant));
            } else {
                afact.setMontantInitial("0");
            }
        }
    }

    /**
     * @param b
     */
    public void setRemboursementFraisAdmin(boolean b) {
        remboursementFraisAdmin = b;
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
    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    /**
     * Sets the tillNumAffilie.
     * 
     * @param tillNumAffilie
     *            The tillNumAffilie to set
     */
    public void setTillNumAffilie(java.lang.String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

}
