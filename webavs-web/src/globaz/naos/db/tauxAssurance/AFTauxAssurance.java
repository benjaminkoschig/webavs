package globaz.naos.db.tauxAssurance;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.api.helper.IAFTauxAssuranceHelper;
import globaz.naos.application.AFApplication;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CATauxRubriques;
import globaz.osiris.db.comptes.CATauxRubriquesManager;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * La classe définissant l'entité TauxAssurance.
 * 
 * @author administrator
 */
public class AFTauxAssurance extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int ALT_KEY_TAUX_ASSURANCE = 1;
    public final static String SAISIE_BLOQUER = "0";

    public final static String SAISIE_LIBRE = "1";
    private AFAssurance _assurance = null;
    // variable pour l'affichage ou non du taux sur facture
    private boolean affichageTaux = true;
    // Foreign Key
    private java.lang.String assuranceId = new String();
    private java.lang.String categorieId = new String();
    // Fields
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();
    private java.lang.String fraction = new String();
    private java.lang.String genreValeur = new String();
    private java.lang.String periodiciteMontant = new String();
    private java.lang.String rang = new String();
    private java.lang.String saisieCanton = new String();
    private java.lang.String saisieCategorie = new String();
    private java.lang.String saisieCodeAdministration = new String();

    private java.lang.String saisieGenreValeur = new String();

    private java.lang.String saisieSexe = new String();

    private java.lang.String sexe = new String();
    // DB
    // Primary Key
    private java.lang.String tauxAssuranceId = new String();
    private java.lang.String tranche = new String();

    private java.lang.String typeId = CodeSystem.TYPE_TAUX_DEFAUT;

    private java.lang.String valeurEmploye = new String();
    private java.lang.String valeurEmployeur = new String();

    /**
     * Constructeur d'AFTauxAssurance.
     */
    public AFTauxAssurance() {
        super();
        setMethodsToLoad(IAFTauxAssuranceHelper.METHODS_TO_LOAD);
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        if ("true".equals(((AFApplication) getSession().getApplication()).getSynchroTaux())) {
            if (getTypeId().equalsIgnoreCase(CodeSystem.TYPE_TAUX_DEFAUT)
                    || (getTypeId().equalsIgnoreCase(CodeSystem.TYPE_TAUX_CAISSE))) {

                if ((getGenreValeur().equalsIgnoreCase(CodeSystem.GEN_VALEUR_ASS_TAUX))
                        && (!JadeStringUtil.isBlankOrZero(getValeurEmploye()))) {

                    FWCurrency tauxEmpl = new FWCurrency(getValeurEmployeur());

                    if (!tauxEmpl.isNegative()) {
                        CATauxRubriques tauxRubrique = new CATauxRubriques();

                        tauxRubrique.setIdCaisseProf(getCategorieId());
                        tauxRubrique.setIdRubrique(getAssurance().getRubriqueId());
                        tauxRubrique.setDate(getDateDebut());
                        tauxRubrique.setTauxEmployeur(getValeurEmployeur());
                        tauxRubrique.setTauxSalarie(getValeurEmploye());
                        tauxRubrique.setSession(getSession());
                        tauxRubrique.add();
                    }
                }
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {

        // Si l'utilisateur a sélectionné un taux groupé
        if (CodeSystem.TYPE_TAUX_GROUPE.equals(getTypeId())) {
            // On renseigne la catégorie
            setSaisieCategorie(getCategorieId());

            // Si l'utilisateur a sélectionné un taux par caisse
        } else if (CodeSystem.TYPE_TAUX_CAISSE.equals(getTypeId())) {

            TIAdministrationViewBean caisse = null;
            // Si l'id est renseigné
            if (!JadeStringUtil.isBlankOrZero(getCategorieId())) {
                // On recherche d'après l'id et on récupère le code
                if ((caisse = findCaisseById(getCategorieId(), transaction)) != null) {
                    setSaisieCodeAdministration(caisse.getCodeAdministration());
                }
            }

            // Si l'utilisateur a sélectionné un taux par canton
        } else if (CodeSystem.TYPE_TAUX_CANTON.equals(getTypeId())) {
            // On renseigne le canton
            setSaisieCanton(getCategorieId());
        }

    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {

        if (((AFApplication) getSession().getApplication()).getSynchroTaux().equals("true")) {

            CATauxRubriquesManager tauxmanager = new CATauxRubriquesManager();

            tauxmanager.setForIdRubrique(getAssurance().getRubriqueId());
            tauxmanager.setFromDate(getDateDebut());

            tauxmanager.setSession(getSession());
            tauxmanager.find();

            if (tauxmanager.isEmpty()) {

                if (getTypeId().equalsIgnoreCase(CodeSystem.TYPE_TAUX_DEFAUT)
                        || (getTypeId().equalsIgnoreCase(CodeSystem.TYPE_TAUX_CAISSE))) {

                    if ((getGenreValeur().equalsIgnoreCase(CodeSystem.GEN_VALEUR_ASS_TAUX))
                            && (!JadeStringUtil.isBlankOrZero(getValeurEmploye()))) {

                        CATauxRubriques tauxRubrique = new CATauxRubriques();

                        tauxRubrique.setIdCaisseProf(getCategorieId());
                        tauxRubrique.setIdRubrique(getAssurance().getRubriqueId());
                        tauxRubrique.setDate(getDateDebut());
                        tauxRubrique.setTauxEmployeur(getValeurEmployeur());
                        tauxRubrique.setTauxSalarie(getValeurEmploye());
                        tauxRubrique.setSession(getSession());
                        tauxRubrique.add();
                    }
                }
            } else {

                CATauxRubriques tauxRubriquesModif = new CATauxRubriques();
                for (int i = 0; i < tauxmanager.size(); i++) {
                    tauxRubriquesModif = (CATauxRubriques) tauxmanager.getEntity(i);

                    tauxRubriquesModif.setDate(getDateDebut());
                    tauxRubriquesModif.setTauxEmployeur(getValeurEmployeur());
                    tauxRubriquesModif.setTauxSalarie(getValeurEmploye());
                    tauxRubriquesModif.setSession(getSession());
                    tauxRubriquesModif.update();
                }
            }
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setTauxAssuranceId(this._incCounter(transaction, "0"));
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFTAUXP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        tauxAssuranceId = statement.dbReadNumeric("MCITAU");
        assuranceId = statement.dbReadNumeric("MBIASS");
        typeId = statement.dbReadNumeric("MCTTYP");
        categorieId = statement.dbReadNumeric("MCTCAT");
        dateDebut = statement.dbReadDateAMJ("MCDDEB");
        // dateFin = statement.dbReadDateAMJ("MCDFIN");
        sexe = statement.dbReadNumeric("MCTSEX");
        genreValeur = statement.dbReadNumeric("MCTGEN");
        valeurEmployeur = statement.dbReadNumeric("MCMVER", 5);
        valeurEmploye = statement.dbReadNumeric("MCMVEE", 5);
        fraction = statement.dbReadNumeric("MCNFRA");
        periodiciteMontant = statement.dbReadNumeric("MCTPER");
        rang = statement.dbReadNumeric("MCNRAN");
        tranche = statement.dbReadNumeric("MCMTRA", 2);
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getGenreValeur(), getSession().getLabel("1320"));

        validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));

        // si taux pour ctrl employeur, on force le genre à taux fixe et on
        // effacer les rangs et tranche
        if (CodeSystem.TYPE_TAUX_CTRL_EMP.equals(getTypeId())) {
            setGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
            setRang("");
            setTranche("");
        }
        if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(getGenreValeur())) {
            if (JadeStringUtil.isEmpty(valeurEmployeur) && JadeStringUtil.isEmpty(valeurEmploye)) {

                _addError(statement.getTransaction(), getSession().getLabel("1380"));
                validationOK = false;
            }
        } else if (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(getGenreValeur())) {
            if (JadeStringUtil.isEmpty(valeurEmployeur) && JadeStringUtil.isEmpty(valeurEmploye)) {

                _addError(statement.getTransaction(), getSession().getLabel("1380"));
                validationOK = false;
            }

            validationOK &= _propertyMandatory(statement.getTransaction(), getRang(), getSession().getLabel("1400"));

            if (validationOK) {

                // Vérifier qu'il n'y as pas une tranche sans limite superieur
                // (=0) de rang inférieur
                AFTauxAssuranceManager manager = new AFTauxAssuranceManager();
                manager.setSession(getSession());
                manager.setForIdAssurance(getAssuranceId());
                manager.setForGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE);
                manager.setToRang(getRang());
                manager.setForTranche("0");
                manager.find();

                if (manager.size() > 0) {
                    _addError(statement.getTransaction(), getSession().getLabel("1790"));
                } else {
                    // Tranche = 0 => pas de limite superieur
                    if (JadeStringUtil.isIntegerEmpty(getTranche())) {
                        // Vérifier qu'il n'y as pas une tranche de rang
                        // supérieur
                        manager.setToRang("");
                        manager.setFromRang(getRang());
                        manager.setForTranche("");
                        manager.find();

                        if (manager.size() > 0) {
                            validationOK &= _propertyMandatory(statement.getTransaction(), getTranche(), getSession()
                                    .getLabel("1410"));
                        }
                    }
                }
            }

        } else if (CodeSystem.GEN_VALEUR_ASS_MONTANT.equals(getGenreValeur())) {
            if (JadeStringUtil.isEmpty(valeurEmployeur) && JadeStringUtil.isEmpty(valeurEmploye)) {

                _addError(statement.getTransaction(), getSession().getLabel("1390"));
                validationOK = false;
            }
        }

        try {
            if (validationOK) {
                String dateLimiteInf = "01.01.1900";
                String dateInitiale = JACalendar.todayJJsMMsAAAA();
                String dateLimiteSup = getSession().getApplication().getCalendar().addYears(dateInitiale, 5);

                // Control que la date de début soit comprise entre le
                // 01.01.1948 et la date du jour + 5ans
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebut(), dateLimiteInf)) {
                    if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(), dateLimiteSup)) {
                        _addError(statement.getTransaction(), getSession().getLabel("770"));
                    }
                } else {
                    _addError(statement.getTransaction(), getSession().getLabel("60"));
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        if (validationOK) {
            if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(getGenreValeur())
                    || CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(getGenreValeur())) {

                if (!JadeStringUtil.isEmpty(valeurEmployeur)) {
                    try {
                        new BigDecimal(valeurEmployeur);
                    } catch (NumberFormatException e) {
                        _addError(statement.getTransaction(), getSession().getLabel("1240"));
                        setValeurEmployeur("");
                    }
                }

                if (!JadeStringUtil.isEmpty(valeurEmploye)) {
                    try {
                        new BigDecimal(valeurEmploye);
                    } catch (NumberFormatException e) {
                        _addError(statement.getTransaction(), getSession().getLabel("1240"));
                        setValeurEmploye("");
                    }
                }

                if (JadeStringUtil.isEmpty(fraction)) {
                    _addError(statement.getTransaction(), getSession().getLabel("1930"));
                } else {
                    try {
                        new BigDecimal(fraction);
                    } catch (NumberFormatException e) {
                        _addError(statement.getTransaction(), getSession().getLabel("1240"));
                        setFraction("");
                    }
                }

                if ((!JadeStringUtil.isEmpty(valeurEmployeur) || !JadeStringUtil.isEmpty(valeurEmployeur))
                        && !JadeStringUtil.isEmpty(fraction)) {

                    BigDecimal bTauxEmployeur = null;
                    BigDecimal bTauxEmploye = null;
                    BigDecimal bFraction = new BigDecimal(fraction);

                    if (!JadeStringUtil.isEmpty(valeurEmployeur)) {
                        bTauxEmployeur = new BigDecimal(valeurEmployeur);
                    }
                    if (!JadeStringUtil.isEmpty(valeurEmploye)) {
                        bTauxEmploye = new BigDecimal(valeurEmploye);
                    }
                    // La fraction doit être plus grande ou égale au Taux

                    if (((bTauxEmployeur != null) && (bFraction.compareTo(bTauxEmployeur) == -1))
                            || ((bTauxEmploye != null) && (bFraction.compareTo(bTauxEmploye) == -1))) {

                        _addError(statement.getTransaction(), getSession().getLabel("800"));
                    }
                }

                if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(getGenreValeur())) {
                    setRang("");
                    setTranche("");
                }

            } else if (CodeSystem.GEN_VALEUR_ASS_MONTANT.equals(getGenreValeur())) {

                setFraction("");
                setRang("");
                setTranche("");

                if (JadeStringUtil.isIntegerEmpty(valeurEmployeur) && JadeStringUtil.isIntegerEmpty(valeurEmploye)) {

                    // Si montant pas renseigné
                    if (!JadeStringUtil.isEmpty(getPeriodiciteMontant())) {
                        _addError(statement.getTransaction(), getSession().getLabel("811"));
                    }
                } else {
                    // Si montant est renseigné, la périodicité doit l'être
                    // aussi
                    if (JadeStringUtil.isEmpty(getPeriodiciteMontant())) {
                        _addError(statement.getTransaction(), getSession().getLabel("821"));
                    }
                }
            }
        }

        /* Validation du type de taux */

        // InfoRom 354 Lot 2 autorise une assurance fad pers avec taux par caisse et un taux groupé
        boolean isAssFADPers = CodeSystem.GENRE_ASS_PERSONNEL.equalsIgnoreCase(getAssurance().getAssuranceGenre())
                && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(getAssurance().getTypeAssurance());

        // Si l'utilisateur a sélectionné un taux groupé
        if (CodeSystem.TYPE_TAUX_GROUPE.equals(getTypeId())) {
            if (!isAssFADPers && getAssurance().isTauxParCaisse().booleanValue()) {
                // Si l'assurance gère un taux par caisse, le taux groupé ne
                // peut pas être sélectionné
                _addError(statement.getTransaction(), getSession().getLabel("2080"));
            } else {
                // La catégorie doit être renseignée
                _propertyMandatory(statement.getTransaction(), getSaisieCategorie(), getSession().getLabel("2090"));
                // On enregistre l'id du groupe
                setCategorieId(getSaisieCategorie());
            }

            // Si l'utilisateur a sélectionné un taux par caisse
        } else if (CodeSystem.TYPE_TAUX_CAISSE.equals(getTypeId())) {

            TIAdministrationViewBean caisse = null;
            // Si le code est renseigné
            if (!JadeStringUtil.isBlankOrZero(getSaisieCodeAdministration())) {
                // ... et que l'id est renseigné aussi
                if (!JadeStringUtil.isBlankOrZero(getCategorieId())) {
                    // Si l'id existe
                    if ((caisse = findCaisseById(getCategorieId(), statement.getTransaction())) != null) {
                        // Si le code ne correspond pas à l'id
                        if (!caisse.getCodeAdministration().equals(getSaisieCodeAdministration())) {
                            // On recherche d'après le code et on récupère l'id
                            if ((caisse = findCaisseByCodeAdministration(getSaisieCodeAdministration(),
                                    statement.getTransaction())) != null) {
                                setCategorieId(caisse.getIdTiersAdministration());
                            }
                        }
                        // Si l'id n'existe pas
                    } else {
                        // On recherche d'après le code et on récupère l'id
                        if ((caisse = findCaisseByCodeAdministration(getSaisieCodeAdministration(),
                                statement.getTransaction())) != null) {
                            setCategorieId(caisse.getIdTiersAdministration());
                        }
                    }
                    // ... mais que l'id n'est pas renseigné
                } else {
                    // On recherche d'après le code et on récupère l'id
                    if ((caisse = findCaisseByCodeAdministration(getSaisieCodeAdministration(),
                            statement.getTransaction())) != null) {
                        setCategorieId(caisse.getIdTiersAdministration());
                    }
                }
                // Si le code n'est pas renseigné
            } else {
                // ... mais que l'id est renseigné
                if (!JadeStringUtil.isBlankOrZero(getCategorieId())) {
                    // On recherche d'après l'id et on récupère le code
                    if ((caisse = findCaisseById(getCategorieId(), statement.getTransaction())) != null) {
                        setSaisieCodeAdministration(caisse.getCodeAdministration());
                    }
                }
            }
            // La caisse doit être renseignée
            _propertyMandatory(statement.getTransaction(), getCategorieId(), getSession().getLabel("2100"));

            // Si l'utilisateur a sélectionné un taux par canton
        } else if (CodeSystem.TYPE_TAUX_CANTON.equals(getTypeId())) {
            // Le canton doit être renseigné
            _propertyMandatory(statement.getTransaction(), getSaisieCanton(), getSession().getLabel("2110"));
            // On enregistre l'id du canton
            setCategorieId(getSaisieCanton());

            // Si c'est une autre sélection
        } else {
            // On initialise l'id
            setCategorieId("");
        }

        // Une assurance référencée ne doit pas avoir de taux forcé, de taux par
        // catégorie ou de taux par caisse
        if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(getGenreValeur())
                && (CodeSystem.TYPE_TAUX_FORCE.equals(getTypeId()) || CodeSystem.TYPE_TAUX_GROUPE.equals(getTypeId()) || CodeSystem.TYPE_TAUX_CAISSE
                        .equals(getTypeId()))) {
            AFAssuranceManager manager = new AFAssuranceManager();
            manager.setSession(getSession());
            manager.setForIdAssuranceReference(getAssuranceId());
            manager.find(statement.getTransaction());
            if (manager.size() > 0) {
                _addError(statement.getTransaction(), getSession().getLabel("2111"));
            }
        }

        // InfoRom354 Lot 2
        if (CodeSystem.GENRE_ASS_PERSONNEL.equalsIgnoreCase(getAssurance().getAssuranceGenre())
                && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(getAssurance().getTypeAssurance())) {

            if (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equalsIgnoreCase(getGenreValeur())
                    && !(CodeSystem.TYPE_TAUX_GROUPE.equalsIgnoreCase(getTypeId()) && CodeSystem.CATEGORIE_TAUX_IND_TSE
                            .equalsIgnoreCase(getCategorieId()))) {
                _addError(
                        statement.getTransaction(),
                        getSession().getLabel(
                                "TAUX_FOR_ASSURANCE_FRAIS_ADMINISTRATION_PERSONNELLE_GENRE_CATEGORIE_NON_VALIDE"));
            }

        }

    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MCITAU", this._dbWriteNumeric(statement.getTransaction(), getTauxAssuranceId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MCITAU",
                this._dbWriteNumeric(statement.getTransaction(), getTauxAssuranceId(), "TauxAssuranceID"));
        statement.writeField("MBIASS",
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "AssuranceID"));
        statement.writeField("MCTTYP", this._dbWriteNumeric(statement.getTransaction(), getTypeId(), "TypeId"));
        statement.writeField("MCTCAT",
                this._dbWriteNumeric(statement.getTransaction(), getCategorieId(), "CategorieId"));
        statement.writeField("MCDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "DateDebut"));
        // statement.writeField("MCDFIN",_dbWriteDateAMJ(statement.getTransaction(),
        // getDateFin(),"DateFin"));
        statement.writeField("MCTSEX", this._dbWriteNumeric(statement.getTransaction(), getSexe(), "Sexe"));
        statement.writeField("MCTGEN",
                this._dbWriteNumeric(statement.getTransaction(), getGenreValeur(), "GenreValeur"));
        statement.writeField("MCMVER",
                this._dbWriteNumeric(statement.getTransaction(), getValeurEmployeur(), "ValeurEmployeur"));
        statement.writeField("MCMVEE",
                this._dbWriteNumeric(statement.getTransaction(), getValeurEmploye(), "ValeurEmploye"));
        statement.writeField("MCNFRA", this._dbWriteNumeric(statement.getTransaction(), getFraction(), "Fraction"));
        statement.writeField("MCTPER",
                this._dbWriteNumeric(statement.getTransaction(), getPeriodiciteMontant(), "PeriodiciteMontant"));
        statement.writeField("MCNRAN", this._dbWriteNumeric(statement.getTransaction(), getRang(), "Rang"));
        statement.writeField("MCMTRA", this._dbWriteNumeric(statement.getTransaction(), getTranche(), "Tranche"));
    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find();
        return manager;
    }

    private TIAdministrationViewBean findCaisseByCodeAdministration(String codeAdministration, BTransaction transaction)
            throws Exception {
        TIAdministrationManager manager = new TIAdministrationManager();
        manager.setSession(transaction.getSession());
        manager.setForCodeAdministration(codeAdministration);
        manager.setForGenreAdministration(CodeSystem.GENRE_ADMIN_CAISSE_PROF);
        manager.find(transaction);
        if (manager.size() > 0) {
            return (TIAdministrationViewBean) manager.get(0);
        } else {
            return null;
        }
    }

    private TIAdministrationViewBean findCaisseById(String id, BTransaction transaction) throws Exception {
        TIAdministrationViewBean entity = new TIAdministrationViewBean();
        entity.setIdTiersAdministration(id);
        entity.retrieve(transaction);
        if (entity.isNew()) {
            return null;
        } else {
            return entity;
        }
    }

    /**
     * Rechercher l'assurance du Taux en fonction de son ID.
     * 
     * @return l'assurance
     */
    public AFAssurance getAssurance() {

        // Si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAssuranceId())) {
            return null;
        }

        if (_assurance == null) {

            _assurance = new AFAssurance();
            _assurance.setSession(getSession());
            _assurance.setAssuranceId(getAssuranceId());
            try {
                _assurance.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _assurance = null;
            }
        }
        return _assurance;
    }

    public java.lang.String getAssuranceId() {
        return assuranceId;
    }

    public java.lang.String getCategorieId() {
        return categorieId;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    /**
     * Retour les "Code System" a exclure pour la sélection de la "Periodicité" du monatnt.
     * 
     * @return
     */
    public HashSet getExceptPeriodicite() {
        HashSet except = new HashSet();
        // liste des cs qui ne devront pas figurér dans la liste
        // except.add(CodeSystem.PERIODICITE_ANNUELLE_31_MARS);
        // except.add(CodeSystem.PERIODICITE_ANNUELLE_30_JUIN);
        // except.add(CodeSystem.PERIODICITE_ANNUELLE_30_SEPT);
        return except;
    }

    public java.lang.String getFraction() {
        // return JANumberFormatter.fmt("100",true,false,true,5);
        return JANumberFormatter.fmt(fraction.toString(), true, false, true, 0);
    }

    public java.lang.String getGenreValeur() {
        return genreValeur;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFTauxAssuranceManager();
    }

    public java.lang.String getPeriodiciteMontant() {
        return periodiciteMontant;
    }

    public java.lang.String getRang() {
        return JANumberFormatter.fmt(rang.toString(), false, false, true, 0);
    }

    public java.lang.String getSaisieCanton() {
        return saisieCanton;
    }

    public java.lang.String getSaisieCategorie() {
        return saisieCategorie;
    }

    public java.lang.String getSaisieCodeAdministration() {
        return saisieCodeAdministration;
    }

    public java.lang.String getSaisieGenreValeur() {
        return saisieGenreValeur;
    }

    public java.lang.String getSaisieSexe() {
        return saisieSexe;
    }

    public java.lang.String getSexe() {
        return sexe;
    }

    public java.lang.String getTauxAssuranceId() {
        return tauxAssuranceId;
    }

    public double getTauxDouble() {
        if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(getGenreValeur())
                || CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(getGenreValeur())) {
            double tauxER = Double.parseDouble(getValeurEmployeur());
            double tauxEE = Double.parseDouble(getValeurEmploye());
            double fraction = Double.parseDouble(getFraction());
            return (tauxER + tauxEE) / (fraction);
        } else {
            // pas un taux
            return 0;
        }
    }

    public String getTauxSansFraction() {
        if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(getGenreValeur())
                || CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(getGenreValeur())) {
            double tauxER = Double.parseDouble(getValeurEmployeur());
            double tauxEE = Double.parseDouble(getValeurEmploye());
            return String.valueOf(tauxER + tauxEE);
        } else {
            // pas un taux
            return "0";
        }

    }

    public java.lang.String getTranche() {
        return JANumberFormatter.fmt(tranche.toString(), true, false, true, 2);
    }

    public java.lang.String getTypeId() {
        return typeId;
    }

    public java.lang.String getValeurEmploye() {
        return JANumberFormatter.fmt(valeurEmploye.toString(), true, false, false, 5);
    }

    public java.lang.String getValeurEmployeur() {
        return JANumberFormatter.fmt(valeurEmployeur.toString(), true, false, false, 5);
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public java.lang.String getValeurTotal() {
        String result = "";

        if (CodeSystem.GEN_VALEUR_ASS_MONTANT.equals(getGenreValeur())) {

            BigDecimal bMontantEmployeur = new BigDecimal("0");
            BigDecimal bMontantEmploye = new BigDecimal("0");

            if (!JadeStringUtil.isEmpty(valeurEmployeur) && JadeStringUtil.isEmpty(valeurEmploye)) {

                bMontantEmployeur = JAUtil.createBigDecimal(valeurEmployeur);

            } else if (JadeStringUtil.isEmpty(valeurEmployeur) && !JadeStringUtil.isEmpty(valeurEmploye)) {

                bMontantEmploye = JAUtil.createBigDecimal(valeurEmploye);

            } else if (!JadeStringUtil.isEmpty(valeurEmployeur) && !JadeStringUtil.isEmpty(valeurEmploye)) {

                bMontantEmployeur = JAUtil.createBigDecimal(valeurEmployeur);
                bMontantEmploye = JAUtil.createBigDecimal(valeurEmploye);
            }
            result = JANumberFormatter.fmt(bMontantEmployeur.add(bMontantEmploye).toString(), true, false, false, 2);

        } else if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(getGenreValeur())
                || CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(getGenreValeur())) {

            BigDecimal taux = JAUtil.createBigDecimal("0");
            BigDecimal bFraction = JAUtil.createBigDecimal(fraction);

            if (!JadeStringUtil.isEmpty(valeurEmployeur) && JadeStringUtil.isEmpty(valeurEmploye)) {

                BigDecimal bTauxEmployeur = JAUtil.createBigDecimal(valeurEmployeur);

                taux = bTauxEmployeur.divide(bFraction, 2, BigDecimal.ROUND_DOWN);

            } else if (JadeStringUtil.isEmpty(valeurEmployeur) && !JadeStringUtil.isEmpty(valeurEmploye)) {

                BigDecimal bTauxEmploye = JAUtil.createBigDecimal(valeurEmploye);

                taux = bTauxEmploye.divide(bFraction, 2, BigDecimal.ROUND_DOWN);

            } else if (!JadeStringUtil.isEmpty(valeurEmployeur) && !JadeStringUtil.isEmpty(valeurEmploye)) {

                BigDecimal bTauxEmployeur = JAUtil.createBigDecimal(valeurEmployeur);
                BigDecimal bTauxEmploye = JAUtil.createBigDecimal(valeurEmploye);

                BigDecimal bTaux = bTauxEmployeur.multiply(bFraction).add(bTauxEmploye.multiply(bFraction));
                taux = bTaux.divide(bFraction, 5, BigDecimal.ROUND_DOWN);
            }

            result = JANumberFormatter.fmt(taux.toString(), true, false, false, 5);
        }
        return result;
    }

    public boolean isAffichageTaux() {
        return affichageTaux;
    }

    public void setAffichageTaux(boolean affichageTaux) {
        this.affichageTaux = affichageTaux;
    }

    public void setAssuranceId(java.lang.String newAssuranceId) {
        assuranceId = newAssuranceId;
    }

    public void setCategorieId(java.lang.String newCategorieId) {
        categorieId = newCategorieId;
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateFin(java.lang.String newDateFin) {
        dateFin = newDateFin;
    }

    public void setFraction(java.lang.String newFraction) {
        fraction = JANumberFormatter.deQuote(newFraction);
    }

    public void setGenreValeur(java.lang.String string) {
        genreValeur = string;
    }

    public void setPeriodiciteMontant(java.lang.String newPeriodiciteMontant) {
        periodiciteMontant = newPeriodiciteMontant;
    }

    public void setRang(java.lang.String string) {
        rang = string;
    }

    public void setSaisieCanton(java.lang.String string) {
        saisieCanton = string;
    }

    public void setSaisieCategorie(java.lang.String string) {
        saisieCategorie = string;
    }

    public void setSaisieCodeAdministration(java.lang.String string) {
        saisieCodeAdministration = string;
    }

    public void setSaisieGenreValeur(java.lang.String string) {
        saisieGenreValeur = string;
    }

    public void setSaisieSexe(java.lang.String string) {
        saisieSexe = string;
    }

    public void setSexe(java.lang.String string) {
        sexe = string;
    }

    public void setTauxAssuranceId(java.lang.String newTauxAssuranceId) {
        tauxAssuranceId = newTauxAssuranceId;
    }

    public void setTranche(java.lang.String string) {
        tranche = JANumberFormatter.deQuote(string);
    }

    public void setTypeId(java.lang.String newTypeId) {
        typeId = newTypeId;
    }

    public void setValeurEmploye(java.lang.String string) {
        valeurEmploye = JANumberFormatter.deQuote(string);
    }

    public void setValeurEmployeur(java.lang.String string) {
        valeurEmployeur = JANumberFormatter.deQuote(string);
    }

}
