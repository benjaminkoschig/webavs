package globaz.draco.db.declaration;

import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.assurance.AFCalculAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxVariableUtil;
import globaz.naos.db.tauxAssurance.AFTauxVariableUtil.TauxCalcul;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CACompteur;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.Vector;

public class DSLigneDeclarationViewBean extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CACompteur _compteur = null;
    private CACompteur _compteurAC = null;
    private CACompteur _compteurForTaux = null;
    private AFCotisation _cotisation = null;
    private DSDeclarationViewBean _declaration = null;
    /** (tbnann) */
    private String anneCotisation = "";
    private AFAssurance assurance = null;
    /** (MBIASS) */
    private String assuranceId = new String();
    /** (TBMCMA) */
    private String cumulCotisationAnneeCourante = new String();
    /** (TBMCMD) */
    private String cumulCotisationDeclaration = new String();
    // Ce flag indique que la déclaration est en cours de facturation
    private boolean enFacturation = true;
    /** (TBMFAA) */
    private String fractionAssuranceAnneeCourante = new String();
    /** (TBMFAD) */
    private String fractionAssuranceDeclaration = new String();
    /** (TAIDDE) */
    private String idDeclaration = new String();
    /** Fichier DSLIDEP */
    /** (TBILIDE) */
    private String idLigneDeclaration = new String();
    private boolean isTauxCache = false;
    /** (TBMDEC) */
    private String montantDeclaration = new String();
    /** (TBMFAC) */
    private String montantFactureACEJour = new String();
    /** (TBMFOR) */
    private String montantForfaitaire = new String();
    /** (TBMFAJ) */
    private String montantForfaitaireACEJour = new String();
    /** (TBMTAC) */
    private String tauxAssuranceAnneeCourante = new String();
    /** (TBMTAD) */
    private String tauxAssuranceDeclaration = new String();

    // code systeme
    // private double montant =-1 ;
    /**
     * Commentaire relatif au constructeur DSLigneDeclaration
     */
    public DSLigneDeclarationViewBean() {
        super();
    }

    /**
     * Cette méthode permet d'effectuer des opérations après d'exécuter la suppresion
     */
    @Override
    protected void _afterDelete(globaz.globall.db.BTransaction transaction) throws Exception {
        // Initialisation des zones
        _compteur = null;
        _compteurAC = null;
        _cotisation = null;
    }

    /**
     * Permet d'effectuer des opérations avant de lancer l'ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdLigneDeclaration(this._incCounter(transaction, "0"));
        // Contrôler que la déclaration est ouverte, sinon on n'autorise pas les
        // modifcations
        DSDeclarationViewBean decl = new DSDeclarationViewBean();
        decl.setSession(getSession());
        decl.setIdDeclaration(getIdDeclaration());
        decl.retrieve(transaction);
        if (!decl.getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_OUVERT)) {
            _addError(transaction, getSession().getLabel("LIGNE_DECL_MODIF_NON_AUTORISEE"));
        }
        // Contrôler que l'assurance n'existe pas déjà pour la déclaration de
        // salaires
        DSLigneDeclarationListViewBean ligneDec = new DSLigneDeclarationListViewBean();
        ligneDec.setSession(getSession());
        ligneDec.setForAssuranceId(getAssuranceId());
        ligneDec.setForIdDeclaration(getIdDeclaration());
        if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getDeclaration().getTypeDeclaration())) {
            ligneDec.setForAnnee(anneCotisation);
        }
        if (ligneDec.getCount(transaction) != 0) {
            _addError(transaction, getSession().getLabel("LIGNE_DECL_ASSURANCE_EXISTE_DEJA"));
        }
        // L'année de cotisation est obligatoire
        if (JadeStringUtil.isBlankOrZero(anneCotisation)) {
            _addError(transaction, getSession().getLabel("ANNEE_OBLIGATOIRE_LIGNE"));
        }
    }

    /**
     * Cette méthode permet d'effectuer des opérations avant d'exécuter la suppresion
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws Exception {
        // Contrôler que la déclaration soit ouverte pour permettre la
        // suppression
        if (!getDeclaration().getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_OUVERT)) {
            _addError(transaction, getSession().getLabel("LIGNE_DECL_SUPPRESSION_NON_AUTORISEE_ETAT_NON_OUVERT"));
        }
    }

    /**
     * Permet d'effectuer des opérations avant de lancer la mise à jour
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // Contrôler que la déclaration est ouverte, sinon on n'autorise pas les
        // modifcations
        DSDeclarationViewBean decl = new DSDeclarationViewBean();
        decl.setSession(getSession());
        decl.setIdDeclaration(getIdDeclaration());
        decl.retrieve(transaction);
        if (decl.getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)
                || (decl.getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_AFACTURER) && !isEnFacturation())) {
            _addError(transaction, getSession().getLabel("DECL_EST_TRAITEE"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "DSLIDEP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLigneDeclaration = statement.dbReadNumeric("TBILIDE");
        idDeclaration = statement.dbReadNumeric("TAIDDE");
        assuranceId = statement.dbReadNumeric("MBIASS");
        montantDeclaration = statement.dbReadNumeric("TBMDEC", 2);
        montantFactureACEJour = statement.dbReadNumeric("TBMFAC", 2);
        cumulCotisationDeclaration = statement.dbReadNumeric("TBMCMD", 2);
        tauxAssuranceDeclaration = statement.dbReadNumeric("TBMTAD", 5);
        fractionAssuranceDeclaration = statement.dbReadNumeric("TBMFAD", 5);
        montantForfaitaire = statement.dbReadNumeric("TBMFOR", 2);
        montantForfaitaireACEJour = statement.dbReadNumeric("TBMFAJ", 2);
        cumulCotisationAnneeCourante = statement.dbReadNumeric("TBMCMA", 2);
        tauxAssuranceAnneeCourante = statement.dbReadNumeric("TBMTAC", 5);
        fractionAssuranceAnneeCourante = statement.dbReadNumeric("TBMFAA", 5);
        anneCotisation = statement.dbReadNumeric("TBNANN");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // La fraction doit être renseignée lors de la saisie d'un taux
        if (!JadeStringUtil.isBlank(getTauxAssuranceDeclaration())) {
            _propertyMandatory(statement.getTransaction(), getFractionAssuranceDeclaration(),
                    getSession().getLabel("LIGNE_DECL_TAUX_NON_CORRECT"));
        }
        // Le taux doit être renseigné lors de la saisie de la fraction
        if (!JadeStringUtil.isBlank(getFractionAssuranceDeclaration())) {
            _propertyMandatory(statement.getTransaction(), getTauxAssuranceDeclaration(),
                    getSession().getLabel("LIGNE_DECL_FRACTION_NON_CORRECT"));
        }
    }

    /**

	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("TBILIDE", this._dbWriteNumeric(statement.getTransaction(), getIdLigneDeclaration(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("TBILIDE",
                this._dbWriteNumeric(statement.getTransaction(), getIdLigneDeclaration(), "idLigneDeclaration"));
        statement.writeField("TAIDDE",
                this._dbWriteNumeric(statement.getTransaction(), getIdDeclaration(), "idDeclaration"));
        statement.writeField("MBIASS",
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "assuranceId"));
        statement.writeField("TBMDEC",
                this._dbWriteNumeric(statement.getTransaction(), getMontantDeclaration(), "montantDeclaration"));
        statement.writeField("TBMFAC",
                this._dbWriteNumeric(statement.getTransaction(), getMontantFactureACEJour(), "montantFactureACEJour"));
        statement.writeField("TBMCMD", this._dbWriteNumeric(statement.getTransaction(),
                getCumulCotisationDeclaration(), "cumulCotisationDeclaration"));
        statement.writeField("TBMTAD", this._dbWriteNumeric(statement.getTransaction(), getTauxAssuranceDeclaration(),
                "tauxAssuranceDeclaration"));
        statement.writeField("TBMFAD", this._dbWriteNumeric(statement.getTransaction(),
                getFractionAssuranceDeclaration(), "fractionAssuranceDeclaration"));
        statement.writeField("TBMFOR",
                this._dbWriteNumeric(statement.getTransaction(), getMontantForfaitaire(), "montantForfaitaire"));
        statement.writeField("TBMFAJ", this._dbWriteNumeric(statement.getTransaction(), getMontantForfaitaireACEJour(),
                "montantForfaitaireACEJour"));
        statement.writeField("TBMCMA", this._dbWriteNumeric(statement.getTransaction(),
                getCumulCotisationAnneeCourante(), "cumulCotisationAnneeCourante"));
        statement.writeField("TBMTAC", this._dbWriteNumeric(statement.getTransaction(),
                getTauxAssuranceAnneeCourante(), "tauxAssuranceAnneeCourante"));
        statement.writeField("TBMFAA", this._dbWriteNumeric(statement.getTransaction(),
                getFractionAssuranceAnneeCourante(), "fractionAssuranceAnneeCourante"));
        statement.writeField("TBNANN",
                this._dbWriteNumeric(statement.getTransaction(), getAnneCotisation(), "anneeCotisation"));
    }

    /**
     * Calcul le montant de la cotisation due en fonction de la cotisation donnée
     * 
     * @param taux
     *            le taux donné par la ligne ou 0 pour taux par défaut (celui de la cotisation)
     * @param dateDebut
     *            la date de début
     * @param dateFin
     *            la date de fin
     * @return le montant de la cotisation
     * @throws Exception
     *             si une exception survient
     */
    private BigDecimal calculCotisationDueSurCoti(double tauxLigne, String dateDebut, String dateFin) throws Exception {
        double masseTotal = new Double(JANumberFormatter.deQuote(getMontantDeclaration())).doubleValue();
        if (tauxLigne == 0) {
            AFTauxAssurance taux = getTauxLigne(dateFin);
            FWCurrency cotiCompteur = new FWCurrency();
            if (!taux.isAffichageTaux()) {
                // si le taux doit être caché, on initialise la variable
                isTauxCache = true;
            }
            // si taux vaiable et complément ou ctrl employeur, on cumule le
            // montant de la décl avec le compteur
            if (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(taux.getGenreValeur())
                    && "true".equals(getSession().getApplication().getProperty(
                            AFApplication.PROPERTY_IS_TAUX_PAR_PALIER))) {
                if (DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(getDeclaration().getTypeDeclaration())
                        || DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getDeclaration().getTypeDeclaration())
                        || DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(getDeclaration().getTypeDeclaration())) {
                    FWCurrency masse = new FWCurrency("0");
                    // Modif JMC 1-5-5, si une princ est arrivée à 0, on doit
                    // quand même pouvoir facturer
                    if (getCompteur() != null) {
                        masse = new FWCurrency(getCompteur().getCumulMasse());
                    }
                    masse.add(getMontantDeclaration());
                    masseTotal = masse.doubleValue();
                    if (getCompteur() != null) {
                        cotiCompteur = new FWCurrency(getCompteur().getCumulCotisation());
                    } else {
                        cotiCompteur = new FWCurrency("0");
                    }
                }
            }
            // calcul du montant à facturer
            FWCurrency result = new FWCurrency(AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, taux,
                    masseTotal, getSession()));
            // soustraire le montant déjà en comptabilité
            result.sub(cotiCompteur);
            return result.getBigDecimalValue();
        } else {
            // on calcule selon les données de la ligne (pas de taux variable
            // car jamais assigné à la ligne)
            return new BigDecimal(masseTotal * tauxLigne);
        }

    }

    private BigDecimal calculImpotSourceCantonalLTN(String dateDebut, String dateFin) throws Exception {
        DSInscriptionsIndividuellesManager inscriptionsIndividuellesMng = null;
        DSInscriptionsIndividuelles inscriptionsIndividuelles = null;
        TIPersonneAvsManager tiersAssureMng = null;
        TITiersViewBean tiersAssure = null;
        BigDecimal totalImpotSource = new BigDecimal(0);
        BigDecimal impotParSalarie = new BigDecimal(0);
        AFAssuranceManager assuranceImpotSourceMng = null;
        AFAssurance assuranceImpotSource = null;
        AFTauxAssurance taux = null;
        try {
            // Recherche des assurés de la déclaration pour déterminé le taux
            // par canton lié à l'assuré
            // Recherche des ligne de déclarations de salaire
            inscriptionsIndividuellesMng = new DSInscriptionsIndividuellesManager();
            inscriptionsIndividuellesMng.setSession(getSession());
            inscriptionsIndividuellesMng.setForIdDeclaration(getIdDeclaration());
            inscriptionsIndividuellesMng.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < inscriptionsIndividuellesMng.size(); i++) {
                inscriptionsIndividuelles = (DSInscriptionsIndividuelles) inscriptionsIndividuellesMng.getEntity(i);
                if (JadeStringUtil.isBlank(inscriptionsIndividuelles.getNumeroAvs())) {
                    throw new Exception(getSession().getLabel("NSS_OBLIGATOIRE"));
                }
                // Recherche du tiers correspondant à la ligne de déclaration de
                // salaire
                tiersAssureMng = new TIPersonneAvsManager();
                tiersAssureMng.setSession(getSession());
                tiersAssureMng.setForNumAvsActuel(inscriptionsIndividuelles.getNssFormate());
                tiersAssureMng.setForIncludeInactif(new Boolean(false));
                tiersAssureMng.find();
                if (tiersAssureMng.size() == 1) {
                    tiersAssure = (TITiersViewBean) tiersAssureMng.getFirstEntity();
                } else if (tiersAssureMng.size() == 0) {
                    // si le tiers n'est pas trouvé on regarde dans l'historique
                    System.out.println("tiers non trouvé -> recherche dans l'historique : "
                            + inscriptionsIndividuelles.getNssFormate());
                    TIHistoriqueAvsManager historiqueAVSManager = new TIHistoriqueAvsManager();
                    historiqueAVSManager.setSession(getSession());
                    historiqueAVSManager.setForNumAvs(inscriptionsIndividuelles.getNssFormate());
                    historiqueAVSManager.find();
                    if (historiqueAVSManager.size() > 0) {
                        TIHistoriqueAvs historiqueAvs = (TIHistoriqueAvs) historiqueAVSManager.getFirstEntity();
                        tiersAssure = new TITiersViewBean();
                        tiersAssure.setSession(getSession());
                        tiersAssure.setIdTiers(historiqueAvs.getIdTiers());
                        tiersAssure.retrieve();
                    }
                    if (tiersAssure == null) {
                        throw new Exception(getSession().getLabel("AUCUN_TIERS_POUR_NSS") + " "
                                + inscriptionsIndividuelles.getNssFormate());
                    }
                } else if (tiersAssureMng.size() > 1) {
                    throw new Exception(getSession().getLabel("PLUSIEURS_TIERS_POUR_NSS") + " "
                            + inscriptionsIndividuelles.getNssFormate());
                }
                // Déterminer l'assurance en fonction du canton du tiers
                if (!JadeStringUtil.isBlankOrZero(tiersAssure.getIdCantonDomicile())) {
                    assuranceImpotSourceMng = new AFAssuranceManager();
                    assuranceImpotSourceMng.setSession(getSession());
                    assuranceImpotSourceMng.setForTypeAssurance(CodeSystem.TYPE_ASS_IMPOT_SOURCE);
                    assuranceImpotSourceMng.setForCanton(tiersAssure.getIdCantonDomicile());
                    assuranceImpotSourceMng.find();
                    if (assuranceImpotSourceMng.size() >= 1) {
                        assuranceImpotSource = (AFAssurance) assuranceImpotSourceMng.getFirstEntity();
                    } else {
                        throw new Exception(getSession().getLabel("AUCUNE_ASSURANCE_LTN_POUR_TIERS") + " "
                                + inscriptionsIndividuelles.getNssFormate());
                    }
                    taux = assuranceImpotSource.getTaux(dateFin);
                } else {
                    // Si pas de canton de domicile pour l'assuré, prendre le
                    // taux d'assurance cantonal de l'employeur
                    taux = assurance.getTaux(dateFin);
                }
                if (taux == null) {
                    throw new Exception(getSession().getLabel("AUCUN_TAUX_POUR_CANTON") + " "
                            + tiersAssure.getCantonDomicile());
                }
                impotParSalarie = new BigDecimal(AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, taux,
                        new Double(JANumberFormatter.deQuote(inscriptionsIndividuelles.getMontant())).doubleValue(),
                        getSession()));
                if (inscriptionsIndividuelles.getGenreEcriture().equals("11")) {
                    totalImpotSource = totalImpotSource.subtract(impotParSalarie);
                } else {
                    totalImpotSource = totalImpotSource.add(impotParSalarie);
                }

            }
        } catch (Exception e) {
            throw new Exception("calculImpotSourceCantonalLTN: " + e.toString() + " " + e.getMessage());
        }
        return totalImpotSource;
    }

    public String getAnneCotisation() {
        return anneCotisation;
    }

    /**
     * @return
     */
    public AFAssurance getAssurance() {
        if (assurance == null) {
            assurance = new AFAssurance();
            assurance.setSession(getSession());
            assurance.setAssuranceId(getAssuranceId());
            try {
                assurance.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return assurance;
    }

    public String getAssuranceId() {
        return assuranceId;
    }

    public String getAssuranceLibelle() {
        String libelle = "";
        if (getCotisation() != null) {
            libelle = getCotisation().getAssurance().getAssuranceLibelleCourt();
        }
        assurance = getAssurance();
        if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE))
                || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))
                || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_IMPOT_SOURCE))) {
            libelle = assurance.getAssuranceLibelleCourt();
        }
        return libelle;
    }

    /**
     * Permet de récuperer un compteur Date de création : (22.05.2003 10:12:15)
     * 
     * @param app
     * 
     * @return globaz.osiris.db.comptes.CACompteur
     */
    public globaz.osiris.db.comptes.CACompteur getCompteur() throws Exception {
        // Contrôler si déjà instancié
        if (_compteur == null) {
            // Chargement du compteur
            try {
                CACompteur compteur = new CACompteur();
                compteur.setSession(getSession());
                compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
                if (!JadeStringUtil.isBlankOrZero(anneCotisation)) {
                    compteur.setAnnee(anneCotisation);
                } else {
                    compteur.setAnnee(getDeclaration().getAnnee());
                }
                compteur.setIdCompteAnnexe(getDeclaration().getCompteAnnexe().getIdCompteAnnexe());
                assurance = getAssurance();
                if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))) {
                    compteur.setIdRubrique(assurance.getRubriqueId());
                } else {
                    compteur.setIdRubrique(getCotisation().getAssurance().getRubriqueId());
                }
                compteur.retrieve();
                if (!compteur.isNew()) {
                    _compteur = compteur;
                } else {
                    _compteur = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _compteur = null;
            }
        }
        return _compteur;
    }

    /**
     * Permet de récuperer le compteur de l'année courante en passant l'année en paramètre Date de création :
     * (22.05.2003 10:12:15)
     * 
     * @return globaz.osiris.db.comptes.CACompteur
     * @param annee
     *            java.lang.String
     */
    public globaz.osiris.db.comptes.CACompteur getCompteurAC(String annee) throws Exception {
        // Contrôler si déjà instancié
        if (_compteurAC == null) {
            // Chargement du compteur
            CACompteur compteur = new CACompteur();
            compteur.setSession(getSession());
            compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
            compteur.setAnnee(annee);
            compteur.setIdCompteAnnexe(getDeclaration().getCompteAnnexe().getIdCompteAnnexe());
            assurance = getAssurance();
            if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))) {
                compteur.setIdRubrique(assurance.getRubriqueId());
            } else {
                compteur.setIdRubrique(getCotisation().getAssurance().getRubriqueId());
            }
            try {
                compteur.retrieve();
                if (!compteur.isNew()) {
                    _compteurAC = compteur;
                } else {
                    _compteurAC = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _compteurAC = null;
            }
        }
        return _compteurAC;
    }

    public globaz.osiris.db.comptes.CACompteur getCompteurForTaux() throws Exception {
        AFApplication app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS);
        // Contrôler taux si déjà instancié
        if (_compteurForTaux == null) {
            // Chargement du compteur
            try {
                CACompteur compteur = new CACompteur();
                compteur.setSession(getSession());
                compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
                if (!JadeStringUtil.isBlankOrZero(anneCotisation)) {
                    compteur.setAnnee(anneCotisation);
                } else {
                    compteur.setAnnee(getDeclaration().getAnnee());
                }
                compteur.setIdCompteAnnexe(getDeclaration().getCompteAnnexe().getIdCompteAnnexe());
                assurance = getAssurance();
                if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))) {
                    compteur.setIdRubrique(assurance.getRubriqueId());
                } else {
                    compteur.setIdRubrique(getCotisation().getAssurance().getRubriqueId());
                }
                // Si taux variable, il faut prendre la masse de l'assurance de référence
                if ((getCotisation().getAssurance().getAssuranceReference() != null)
                        && ("true".equals(app.getProperty("factuCoti")))) {
                    compteur.setIdRubrique(getCotisation().getAssurance().getAssuranceReference().getRubriqueId());
                }
                compteur.retrieve();
                if (!compteur.isNew()) {
                    _compteurForTaux = compteur;
                } else {
                    _compteurForTaux = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _compteurForTaux = null;
            }
        }
        return _compteurForTaux;
    }

    /**
     * Permet de récupérer une affiliation
     * 
     * @return Returns a AFAffiliation
     */
    public AFCotisation getCotisation() {
        // Si assurance n'est pas déjà instanciée
        if (_cotisation == null) {
            // Chargement du manager
            AFCotisationManager manager = new AFCotisationManager();
            manager.setSession(getSession());
            manager.setForAffiliationId(getDeclaration().getAffiliationId());
            manager.setForAssuranceId(getAssuranceId());
            manager.setForAnneeActive(getAnneCotisation());
            manager.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
            try {
                manager.find();
                if (!manager.isEmpty()) {
                    _cotisation = (AFCotisation) manager.getEntity(0);
                } else {
                    _cotisation = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _cotisation = null;
            }
        }
        return _cotisation;
    }

    private AFTauxAssurance giveMeTauxForReductionPFADSEVariable(String assuranceId, String date, String masse)
            throws Exception {

        AFTauxVariableUtil tauxVarUtil = AFTauxVariableUtil.getInstance(assuranceId);
        TauxCalcul tauxCalcul = tauxVarUtil.getCalcul(getSession(), JANumberFormatter.deQuote(masse), date);

        if (tauxCalcul == null || tauxCalcul.getTauxRang() == null) {
            throw new Exception("DSLigneDeclarationViewBean.giveMeTauxForReductionPFADSEVariable(...) : taux is null ");
        }

        return tauxCalcul.getTauxRang();

    }

    /**
     * Cette méthode retourne la cotisation due elle se calcule comme suit : masse salariale effective * taux /fraction
     */
    public String getCotisationDue() throws Exception {
        BigDecimal _bCotisation = new BigDecimal("0");
        String dateDebut = "01.01.";
        String dateFin = "31.12.";

        // Si déclaration salaire différés, on prend l'année spécifiée pour le taux
        if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(getDeclaration().getTypeDeclaration())) {
            dateDebut += getDeclaration().getAnneeTaux();
            dateFin += getDeclaration().getAnneeTaux();

        } else if (!JadeStringUtil.isBlankOrZero(anneCotisation)) {
            dateDebut += anneCotisation;
            dateFin += anneCotisation;
        } else {
            dateDebut += getDeclaration().getAnnee();
            dateFin += getDeclaration().getAnnee();
        }
        assurance = getAssurance();
        if (!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(getMontantDeclaration()))
                && !CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(assurance.getTypeAssurance())) {
            if (JadeStringUtil.isIntegerEmpty(getTauxAssuranceDeclaration())
                    && JadeStringUtil.isIntegerEmpty(getFractionAssuranceDeclaration())) {
                if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE))
                        || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))) {

                    if (CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE.equalsIgnoreCase(assurance.getTypeAssurance())) {
                        AFTauxAssurance tauxAssurance = giveMeTauxForReductionPFADSEVariable(
                                assurance.getAssuranceId(), dateFin, getMontantDeclaration());
                        FWCurrency coti = new FWCurrency(
                                new Double(JANumberFormatter.deQuote(getMontantDeclaration())).doubleValue()
                                        * tauxAssurance.getTauxDouble());
                        _bCotisation = new BigDecimal(coti.doubleValue());

                    } else {
                        _bCotisation = new BigDecimal(AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin,
                                assurance.getTaux(dateFin),
                                new Double(JANumberFormatter.deQuote(getMontantDeclaration())).doubleValue(),
                                getSession()));
                    }

                    _bCotisation = JANumberFormatter.round(_bCotisation, 0.05, 2, JANumberFormatter.NEAR);
                    return _bCotisation.toString();
                }
                if (assurance.getAssuranceReference() == null) {
                    if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                            || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                            || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                            || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE))
                            || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))) {
                        // DGI refactoring: est-ce que ce code est appelé une
                        // fois???

                        if (CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE.equalsIgnoreCase(assurance.getTypeAssurance())) {
                            AFTauxAssurance tauxAssurance = giveMeTauxForReductionPFADSEVariable(
                                    assurance.getAssuranceId(), dateFin, getMontantDeclaration());
                            FWCurrency coti = new FWCurrency(new Double(
                                    JANumberFormatter.deQuote(getMontantDeclaration())).doubleValue()
                                    * tauxAssurance.getTauxDouble());
                            _bCotisation = new BigDecimal(coti.doubleValue());
                        } else {
                            _bCotisation = new BigDecimal(AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin,
                                    assurance.getTaux(dateFin),
                                    new Double(JANumberFormatter.deQuote(getMontantDeclaration())).doubleValue(),
                                    getSession()));
                        }

                        _bCotisation = JANumberFormatter.round(_bCotisation, 0.05, 2, JANumberFormatter.NEAR);
                        return _bCotisation.toString();
                    }

                    _bCotisation = calculCotisationDueSurCoti(0, dateDebut, dateFin);

                } else {
                    _bCotisation = new BigDecimal(AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin,
                            // getCotisation().getTauxList(dateFin),
                            getTauxLigne(dateFin),
                            new Double(JANumberFormatter.deQuote(getMontantDeclaration())).doubleValue(), getSession()));
                }
            } else {
                // les lignes sont déjà créées et on utilise les valeurs
                // présentes pour effectuer le calcul
                BigDecimal assuranceDeclaration = new BigDecimal(
                        JANumberFormatter.deQuote(getTauxAssuranceDeclaration()));
                BigDecimal fractionAssurance = new BigDecimal(
                        JANumberFormatter.deQuote(getFractionAssuranceDeclaration()));
                _bCotisation = calculCotisationDueSurCoti(
                        assuranceDeclaration.doubleValue() / fractionAssurance.doubleValue(), dateDebut, dateFin);
            }
        } else if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(assurance.getTypeAssurance())) {
            if (JadeStringUtil.isBlankOrZero(assurance.getAssuranceCanton())) {
                // Si le canton de l'assurance de type impôt à la source est
                // vide,
                // calcul de l'impôt à la source fédéral direct.
                AFTauxAssurance taux = assurance.getTaux(dateFin);
                if (taux == null) {
                    throw new Exception(getSession().getLabel("AUCUN_TAUX_FEDERAL"));
                }
                _bCotisation = new BigDecimal(AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, taux,
                        new Double(JANumberFormatter.deQuote(getMontantDeclaration())).doubleValue(), getSession()));
            } else {
                // Si le canton de l'assurance de type impôt à la source est
                // renseigné,
                // calcul de l'impôt à la source cantonal.
                _bCotisation = calculImpotSourceCantonalLTN(dateDebut, dateFin);
            }
        }
        _bCotisation = JANumberFormatter.round(_bCotisation, 0.05, 2, JANumberFormatter.NEAR);
        return _bCotisation.toString();
    }

    /**
     * Retourne la liste d'assurances pour {@link globaz.jsp.taglib.FWListSelectTag} Date de création : (10.12.2002
     * 08:55:41)
     * 
     * @return java.util.Vector
     */
    public Vector<String[]> getCotisations() throws Exception {
        Vector<String[]> list = new Vector<String[]>();
        // chercher toutes les assurances de l'affilié pour l'année de la
        // déclaration
        AFCotisationManager cotisations = new AFCotisationManager();
        cotisations.setSession(getSession());
        if (getDeclaration() != null) {
            cotisations.setForAffiliationId(getDeclaration().getAffiliationId());
            if (!DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getDeclaration().getTypeDeclaration())) {
                cotisations.setForAnneeDeclaration(getDeclaration().getAnnee());
            }
            cotisations.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
            cotisations.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < cotisations.size(); i++) {
                AFCotisation cotisation = (AFCotisation) cotisations.getEntity(i);
                // On ne prend que les assurances paritaires
                if (cotisation.getAssurance().getAssuranceGenre().equalsIgnoreCase(CodeSystem.GENRE_ASS_PARITAIRE)) {
                    list.add(new String[] { cotisation.getAssuranceId(),
                            cotisation.getAssurance().getAssuranceLibelleCourt() });
                }
            }
        }
        if ("true".equalsIgnoreCase((getSession().getApplication()).getProperty("bonusPFA"))) {
            // modif jmc 1.4.11
            AFAssuranceManager assMan = new AFAssuranceManager();
            assMan.setSession(getSession());
            assMan.setForInTypeAssurance(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF + ", "
                    + CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF + ", " + CodeSystem.TYPE_ASS_REDPFA_DSE + ","
                    + CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE + "," + CodeSystem.TYPE_ASS_REDCOTI_DSE);
            assMan.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < assMan.size(); i++) {
                AFAssurance assurancePfa = (AFAssurance) assMan.get(i);
                list.add(new String[] { assurancePfa.getAssuranceId(), assurancePfa.getAssuranceLibelleCourt() });
            }
        }
        if (DSDeclarationViewBean.CS_LTN.equals(getDeclaration().getTypeDeclaration())
                || DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(getDeclaration().getTypeDeclaration())) {
            AFAssuranceManager assMan = new AFAssuranceManager();
            assMan.setSession(getSession());
            assMan.setForTypeAssurance(CodeSystem.TYPE_ASS_IMPOT_SOURCE);
            assMan.find();
            boolean findCanto = false;
            boolean findFed = false;
            for (int i = 0; i < assMan.size(); i++) {
                AFAssurance assuranceLtn = (AFAssurance) assMan.get(i);
                if (!JadeStringUtil.isBlankOrZero(assuranceLtn.getAssuranceCanton()) && !findCanto) {
                    list.add(new String[] { assuranceLtn.getAssuranceId(), assuranceLtn.getAssuranceLibelleCourt() });
                    findCanto = true;
                } else if (!findFed) {
                    list.add(new String[] { assuranceLtn.getAssuranceId(), assuranceLtn.getAssuranceLibelleCourt() });
                    findFed = true;
                }
            }
        }
        return list;
    }

    /**
     * Returns the cumulCotisationAnneeCourante.
     * 
     * @return String
     */
    public String getCumulCotisationAnneeCourante() {
        return cumulCotisationAnneeCourante;
    }

    /**
     * Returns the cumulCotisationDeclaration.
     * 
     * @return String
     */
    public String getCumulCotisationDeclaration() {
        return cumulCotisationDeclaration;
    }

    /**
     * Permet de récupérer une déclaration
     * 
     * @return Returns a DSDeclarationViewBean
     */
    public DSDeclarationViewBean getDeclaration() {
        // Si Declaration n'est pas déjà instanciée
        if (_declaration == null) {
            // Chargement du manager
            DSDeclarationViewBean entity = new DSDeclarationViewBean();
            entity.setSession(getSession());
            entity.setIdDeclaration(getIdDeclaration());
            try {
                entity.retrieve();
                if (!entity.isNew()) {
                    _declaration = entity;
                } else {
                    throw new Exception(getSession().getLabel("Declaration non trouvé"));
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _declaration = null;
            }
        }
        return _declaration;
    }

    /**
     * Cette méthode calcule la différence entre le montant de la déclaration de salaire saisi et le cumul déjà facturé.
     * Date de création : (26.05.2003 09:18:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDecompte() throws Exception {
        FWCurrency _montant = new FWCurrency(getMontantDeclaration());
        FWCurrency _facturation;
        // Si l'état est comptabilisé on prend le montant stocké
        if (getDeclaration().getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)) {
            _facturation = new FWCurrency(getMontantFactureACEJour());
        } else {
            // sinon on prend le montant de la comptabilité auxiliaire
            if (getCompteur() != null) {
                _facturation = new FWCurrency(getCompteur().getCumulMasse());
            } else {
                _facturation = new FWCurrency("0");
            }
        }
        _montant.sub(_facturation);
        return _montant.toString();
    }

    /**
     * Cette méthode calcule la différence entre le montant de la déclaration de salaire saisi et le cumul déjà facturé.
     * Date de création : (26.05.2003 09:18:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFacturationForfaitaireRestante() throws Exception {
        FWCurrency _montant = new FWCurrency(getMontantForfaitaire());
        FWCurrency _facturation;
        int _annee = 0;
        if (!JadeStringUtil.isBlankOrZero(anneCotisation)) {
            _annee = Integer.parseInt(anneCotisation) + 1;
        } else {
            _annee = Integer.parseInt(getDeclaration().getAnnee()) + 1;
        }
        if (getCompteurAC(Integer.toString(_annee)) != null) {
            _facturation = new FWCurrency(getCompteurAC(Integer.toString(_annee)).getCumulMasse());
        } else {
            _facturation = new FWCurrency("0");
        }
        _montant.sub(_facturation);
        return _montant.toString();
    }

    /**
     * Retourne la fraction de l'assurance.
     * 
     * @return String
     */
    public String getFractionAssurance() {
        try {
            String dateFin = "31.12.";
            if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(getDeclaration().getTypeDeclaration())) {
                dateFin += getDeclaration().getAnneeTaux();
            } else if (!JadeStringUtil.isBlankOrZero(anneCotisation)) {
                dateFin += anneCotisation;
            } else {
                dateFin += getDeclaration().getAnnee();
            }
            AFTauxAssurance premierTaux;
            assurance = getAssurance();
            if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(assurance.getTypeAssurance())) {
                return "";
            }
            if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))) {
                premierTaux = assurance.getTaux(dateFin);
            } else if (CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE.equalsIgnoreCase(assurance.getTypeAssurance())) {
                premierTaux = giveMeTauxForReductionPFADSEVariable(assurance.getAssuranceId(), dateFin,
                        getMontantDeclaration());
            } else {
                premierTaux = getTauxLigne(dateFin);
            }
            if (premierTaux != null) {
                return premierTaux.getFraction();
            } else {
                return "";
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return "";
        }
    }

    /**
     * Returns the fractionAssuranceAnneeCourante.
     * 
     * @return String
     */
    public String getFractionAssuranceAnneeCourante() {
        return JANumberFormatter.fmt(fractionAssuranceAnneeCourante, true, false, true, 5);
    }

    /**
     * Returns the fractionAssuranceDeclaration.
     * 
     * @return String
     */
    public String getFractionAssuranceDeclaration() {
        return JANumberFormatter.fmt(fractionAssuranceDeclaration, true, false, true, 5);
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdLigneDeclaration() {
        return idLigneDeclaration;
    }

    public String getMontantDeclaration() {
        if (JadeStringUtil.isDecimalEmpty(montantDeclaration)) {
            return "0.00";
        } else {
            return JANumberFormatter.fmt(montantDeclaration.toString(), true, true, true, 2);
        }
    }

    /**
     * Returns the montantFactureACEJour.
     * 
     * @return String
     */
    public String getMontantFactureACEJour() {
        return montantFactureACEJour;
    }

    public String getMontantForfaitaire() {
        return JANumberFormatter.fmt(montantForfaitaire.toString(), true, true, true, 2);
    }

    /**
     * Returns the montantForfaitaireACEJour.
     * 
     * @return String
     */
    public String getMontantForfaitaireACEJour() {
        return montantForfaitaireACEJour;
    }

    public String getSoldeCotisation() {
        BigDecimal _soldeCotisation = new BigDecimal("0");
        try {
            if (getDeclaration().getTypeDeclaration().equals(globaz.draco.translation.CodeSystem.CS_PRINCIPALE)
                    || getDeclaration().getTypeDeclaration().equals(
                            globaz.draco.translation.CodeSystem.CS_BOUCLEMENT_ACCOMPTE)) {
                if (getDeclaration().getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)) {
                    if (!JadeStringUtil.isBlank(getCotisationDue())) {
                        _soldeCotisation = new BigDecimal(JANumberFormatter.deQuote(getCotisationDue()));
                        if (!JadeStringUtil.isBlank(getCumulCotisationDeclaration())) {
                            _soldeCotisation = _soldeCotisation.subtract(new BigDecimal(JANumberFormatter
                                    .deQuote(getCumulCotisationDeclaration())));
                        }
                    }
                } else {
                    if (!JadeStringUtil.isBlank(getCotisationDue())) {
                        _soldeCotisation = new BigDecimal(JANumberFormatter.deQuote(getCotisationDue()));
                        if ((getCompteur() != null)
                                && !JadeStringUtil.isDecimalEmpty(getCompteur().getCumulCotisation())) {
                            _soldeCotisation = _soldeCotisation.subtract(new BigDecimal(JANumberFormatter
                                    .deQuote(getCompteur().getCumulCotisation())));
                        }
                    }
                }
            } else {
                if (!JadeStringUtil.isBlank(getCotisationDue())) {
                    _soldeCotisation = new BigDecimal(JANumberFormatter.deQuote(getCotisationDue()));
                }
            }
            return _soldeCotisation.toString();
        } catch (Exception e) {
            _addError(null, e.getMessage());
            e.printStackTrace();
            return "0.00";
        }
    }

    /**
     * Retourne le taux de l'assurance.
     * 
     * @return String
     */
    public String getTauxAssurance() {
        try {
            String dateFin = "31.12.";

            // Si déclaration salaire différés, on prend l'année spécifiée pour le taux
            if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(getDeclaration().getTypeDeclaration())) {
                dateFin += getDeclaration().getAnneeTaux();

            } else if (!JadeStringUtil.isBlankOrZero(anneCotisation)) {
                dateFin += anneCotisation;
            } else {
                dateFin += getDeclaration().getAnnee();
            }
            AFTauxAssurance premierTaux;
            assurance = getAssurance();
            if (CodeSystem.TYPE_ASS_IMPOT_SOURCE.equals(assurance.getTypeAssurance())) {
                return "";

            }
            if ((assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DOUBLE_AFF))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DOUBLE_AFF))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDPFA_DSE))
                    || (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_REDCOTI_DSE))) {
                premierTaux = assurance.getTaux(dateFin);
            } else if (CodeSystem.TYPE_ASS_REDPFA_DSE_VARIABLE.equalsIgnoreCase(assurance.getTypeAssurance())) {
                premierTaux = giveMeTauxForReductionPFADSEVariable(assurance.getAssuranceId(), dateFin,
                        getMontantDeclaration());
            } else {
                premierTaux = getTauxLigne(dateFin);
            }

            if (premierTaux != null) {
                return premierTaux.getValeurTotal();
            } else {
                return "";
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return "";
        }
    }

    /**
     * Returns the tauxAssuranceAnneeCourante.
     * 
     * @return String
     */
    public String getTauxAssuranceAnneeCourante() {
        return JANumberFormatter.fmt(tauxAssuranceAnneeCourante, true, true, true, 5);
    }

    /**
     * Returns the tauxAssuranceDeclaration.
     * 
     * @return String
     */
    public String getTauxAssuranceDeclaration() {
        return JANumberFormatter.fmt(tauxAssuranceDeclaration, true, true, true, 5);
    }

    /**
     * Recherche du taux à prendre en compte en fonction du type d'assurance et du type de déclaration.<br>
     * Pour les déclarations principales et bouclements d'acomptes, utiliser la masse de l'assurance pour la recherche
     * du taux.<br>
     * Pour les déclaration complémentaires, et contrôle d'employeur, c'est le cumul du complément et celui du compteur
     * qui est alors utilié.
     * 
     * @param dateFin
     *            la date concernée par la déclaration
     * @return le taux à utiliser
     * @throws Exception
     *             si une exception survient
     */
    public AFTauxAssurance getTauxLigne(String dateFin) throws Exception {
        AFApplication app = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS);
        boolean wantAnnualisation = app.wantAnnualiserMasse();

        if (DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(getDeclaration().getTypeDeclaration())
                || DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(getDeclaration().getTypeDeclaration())
                || DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(getDeclaration().getTypeDeclaration())
                || DSDeclarationViewBean.CS_ICI.equals(getDeclaration().getTypeDeclaration())
                || DSDeclarationViewBean.CS_DIVIDENDE.equals(getDeclaration().getTypeDeclaration())) {
            // si déclaration complémentaire ou ctrl employeur, recherche du
            // compteur
            FWCurrency masse = null;
            if (getCompteurForTaux() != null) {
                masse = new FWCurrency(getCompteurForTaux().getCumulMasse());
            } else {
                masse = new FWCurrency("0");
            }
            masse.add(getMontantDeclaration());
            // Si taux variable, il faut prendre la masse de l'assurance de référence
            if ((getCotisation().getAssurance().getAssuranceReference() != null)
                    && ("true".equals(app.getProperty("factuCoti")))) {
                String montant = returnMontantAssuranceReference();
                masse.add(montant);
            } else {
                masse.add(getMontantDeclaration());
            }

            Boolean wantRecalcul = !DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(getDeclaration()
                    .getTypeDeclaration())
                    && !DSDeclarationViewBean.CS_ICI.equals(getDeclaration().getTypeDeclaration())
                    && !DSDeclarationViewBean.CS_DIVIDENDE.equals(getDeclaration().getTypeDeclaration())
                    && !getDeclaration().getTypeDeclaration().equals(DSDeclarationViewBean.CS_COMPLEMENTAIRE)
                    && !getDeclaration().getTypeDeclaration().equals(DSDeclarationViewBean.CS_SALAIRE_DIFFERES);

            return getCotisation().findTaux(dateFin, masse.toString(), getDeclaration().getTypeDeclaration(),
                    wantRecalcul, wantAnnualisation);
        } else {
            String montant = "";
            // Si taux variable, il faut prendre la masse de l'assurance de référence
            if ((getCotisation().getAssurance().getAssuranceReference() != null)
                    && ("true".equals(app.getProperty("factuCoti")))) {
                montant = returnMontantAssuranceReference();
            } else {
                montant = getMontantDeclaration();
            }
            // si déclaration principale ou boulcement d'acomptes, recherche du
            // taux en fonction de la masse de la ligne
            Boolean wantRecalcul = !DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(getDeclaration()
                    .getTypeDeclaration())
                    && !DSDeclarationViewBean.CS_ICI.equals(getDeclaration().getTypeDeclaration())
                    && !DSDeclarationViewBean.CS_DIVIDENDE.equals(getDeclaration().getTypeDeclaration());
            //ESVE afficher le taux moyen spécifique à la FERCIAM
            if (DSDeclarationViewBean.CS_PRINCIPALE.equals(getDeclaration().getTypeDeclaration())
                    && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equals(getAssurance().getTypeAssurance()) && CodeSystem.GENRE_ASS_PARITAIRE.equals(getAssurance().getAssuranceGenre())
                    && "true".equals(getSession().getApplication().getProperty(AFApplication.PROPERTY_IS_TAUX_PAR_PALIER, "false"))) {
                wantRecalcul = false;
            }
            return getCotisation().findTaux(dateFin, montant, getDeclaration().getTypeDeclaration(), wantRecalcul,
                    wantAnnualisation);
        }
    }

    /**
     * Returns the enFacturation.
     * 
     * @return boolean
     */
    public boolean isEnFacturation() {
        return enFacturation;
    }

    public boolean isTauxCache() {
        return isTauxCache;
    }

    private String returnMontantAssuranceReference() throws Exception {
        String montant = "";
        DSLigneDeclarationListViewBean vb = new DSLigneDeclarationListViewBean();
        vb.setSession(getSession());
        vb.setForIdDeclaration(getIdDeclaration());
        vb.setForAssuranceId(getCotisation().getAssurance().getIdAssuranceReference());
        vb.find();
        if (vb.size() > 0) {
            DSLigneDeclarationViewBean dsLigne = (DSLigneDeclarationViewBean) vb.getFirstEntity();
            montant = dsLigne.getMontantDeclaration();
        }
        return montant;
    }

    public void setAnneCotisation(String anneCotisation) {
        this.anneCotisation = anneCotisation;
    }

    public void setAssuranceId(String newAssuranceId) {
        assuranceId = newAssuranceId;
    }

    /**
     * Sets the cumulCotisationAnneeCourante.
     * 
     * @param cumulCotisationAnneeCourante
     *            The cumulCotisationAnneeCourante to set
     */
    public void setCumulCotisationAnneeCourante(String cumulCotisationAnneeCourante) {
        this.cumulCotisationAnneeCourante = cumulCotisationAnneeCourante;
    }

    /**
     * Sets the cumulCotisationDeclaration.
     * 
     * @param cumulCotisationDeclaration
     *            The cumulCotisationDeclaration to set
     */
    public void setCumulCotisationDeclaration(String cumulCotisationDeclaration) {
        this.cumulCotisationDeclaration = cumulCotisationDeclaration;
    }

    /**
     * Sets the isEnFacturation.
     * 
     * @param isEnFacturation
     *            The isEnFacturation to set
     */
    public void setEnFacturation(boolean EnFacturation) {
        enFacturation = EnFacturation;
    }

    /**
     * Sets the fractionAssuranceAnneeCourante.
     * 
     * @param fractionAssuranceAnneeCourante
     *            The fractionAssuranceAnneeCourante to set
     */
    public void setFractionAssuranceAnneeCourante(String fractionAssuranceAnneeCourante) {
        this.fractionAssuranceAnneeCourante = JANumberFormatter.deQuote(fractionAssuranceAnneeCourante);
    }

    /**
     * Sets the fractionAssuranceDeclaration.
     * 
     * @param fractionAssuranceDeclaration
     *            The fractionAssuranceDeclaration to set
     */
    public void setFractionAssuranceDeclaration(String fractionAssuranceDeclaration) {
        this.fractionAssuranceDeclaration = JANumberFormatter.deQuote(fractionAssuranceDeclaration);
    }

    public void setIdDeclaration(String newIdDeclaration) {
        idDeclaration = newIdDeclaration;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newD
     *            String
     */
    public void setIdLigneDeclaration(String newIdLigneDeclaration) {
        idLigneDeclaration = newIdLigneDeclaration;
    }

    public void setMontantDeclaration(String newMontantDeclaration) {
        montantDeclaration = JANumberFormatter.deQuote(newMontantDeclaration);
    }

    /**
     * Sets the montantFactureACEJour.
     * 
     * @param montantFactureACEJour
     *            The montantFactureACEJour to set
     */
    public void setMontantFactureACEJour(String montantFactureACEJour) {
        this.montantFactureACEJour = montantFactureACEJour;
    }

    public void setMontantForfaitaire(String newMontantForfaitaire) {
        montantForfaitaire = JANumberFormatter.deQuote(newMontantForfaitaire);
    }

    /**
     * Sets the montantForfaitaireACEJour.
     * 
     * @param montantForfaitaireACEJour
     *            The montantForfaitaireACEJour to set
     */
    public void setMontantForfaitaireACEJour(String montantForfaitaireACEJour) {
        this.montantForfaitaireACEJour = montantForfaitaireACEJour;
    }

    /**
     * Sets the tauxAssuranceAnneeCourante.
     * 
     * @param tauxAssuranceAnneeCourante
     *            The tauxAssuranceAnneeCourante to set
     */
    public void setTauxAssuranceAnneeCourante(String tauxAssuranceAnneeCourante) {
        this.tauxAssuranceAnneeCourante = JANumberFormatter.deQuote(tauxAssuranceAnneeCourante);
    }

    /**
     * Sets the tauxAssuranceDeclaration.
     * 
     * @param tauxAssuranceDeclaration
     *            The tauxAssuranceDeclaration to set
     */
    public void setTauxAssuranceDeclaration(String tauxAssuranceDeclaration) {
        this.tauxAssuranceDeclaration = JANumberFormatter.deQuote(tauxAssuranceDeclaration);
    }

}
