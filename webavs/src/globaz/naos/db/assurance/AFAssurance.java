package globaz.naos.db.assurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.helper.IAFAssuranceHelper;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.couverture.AFCouvertureManager;
import globaz.naos.db.nombreAssures.AFNombreAssuresManager;
import globaz.naos.db.parametreAssurance.AFParametreAssurance;
import globaz.naos.db.parametreAssurance.AFParametreAssuranceManager;
import globaz.naos.db.suiviAssurance.AFSuiviAssurance;
import globaz.naos.db.suiviAssurance.AFSuiviAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CARubriqueViewBean;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class AFAssurance extends BEntity {

    private static final long serialVersionUID = -5588063743028164332L;
    public static final String FIELD_ASSURANCE_13 = "MBBTRE";
    public static final String FIELD_ASSURANCE_CANTON = "MBTCAN";
    public static final String FIELD_ASSURANCE_LIBELLE_AL = "MBLLID";
    public static final String FIELD_ASSURANCE_LIBELLE_COURT_AL = "MBLLCD";
    public static final String FIELD_ASSURANCE_LIBELLE_COURT_FR = "MBLLCF";
    public static final String FIELD_ASSURANCE_LIBELLE_COURT_IT = "MBLLCI";
    public static final String FIELD_ASSURANCE_LIBELLE_FR = "MBLLIF";
    public static final String FIELD_ASSURANCE_LIBELLE_IT = "MBLLII";
    public static final String FIELD_GENRE_ASSURANCE = "MBTGEN";
    public static final String FIELD_ID_ASSURANCE = "MBIASS";
    public static final String FIELD_ID_ASSURANCE_REFERENCE = "MBIREA";
    public static final String FIELD_ID_RUBRIQUE = "MBIRUB";
    public static final String FIELD_SUR_DOC_ACOMPTE = "MBBSDA";
    public static final String FIELD_TAUX_PAR_CAISSE = "MBBCAI";
    public static final String FIELD_TYPE_ASSURANCE = "MBTTYP";
    public static final String FIELD_TYPE_CALCUL = "MBTTCA";
    public static final String FIELD_DECOMPTE_13_RELEVE = "MBB13R";

    public static final String TABLE_NAME = "AFASSUP";
    private AFAssurance _assuranceReference = null;
    private CARubriqueViewBean _rubriqueComptable = null;
    private Boolean assurance13 = new Boolean(true);
    private String assuranceCanton = new String();
    private String assuranceGenre = new String();
    private String assuranceId = new String();
    private String assuranceLibelleAl = new String();
    private String assuranceLibelleCourtAl = new String();
    private String assuranceLibelleCourtFr = new String();
    private String assuranceLibelleCourtIt = new String();
    // Fields
    private String assuranceLibelleFr = new String();
    private String assuranceLibelleIt = new String();
    private String idAssuranceReference = new String();
    private String rubriqueId = new String();
    private Boolean surDocAcompte = new Boolean(true);

    private Boolean tauxParCaisse = new Boolean(false);
    private String typeAssurance = new String();

    private String typeCalcul = new String();

    private Boolean decompte13Releve = new Boolean(true);

    /**
     * Constructeur d'AFAssurance.
     */
    public AFAssurance() {
        super();
        setMethodsToLoad(IAFAssuranceHelper.METHODS_TO_LOAD);
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {

        AFTauxAssuranceManager tauxManager = new AFTauxAssuranceManager();
        tauxManager.setForIdAssurance(getAssuranceId());
        tauxManager.setSession(getSession());
        tauxManager.find();

        for (int i = 0; i < tauxManager.size(); i++) {
            AFTauxAssurance tauxAssurance = (AFTauxAssurance) tauxManager.getEntity(i);
            tauxAssurance.delete();
        }

        AFSuiviAssuranceManager suiviManager = new AFSuiviAssuranceManager();
        suiviManager.setForAssuranceId(getAssuranceId());
        suiviManager.setSession(getSession());
        suiviManager.find();

        for (int i = 0; i < suiviManager.size(); i++) {
            AFSuiviAssurance suiviAssurance = (AFSuiviAssurance) suiviManager.getEntity(i);
            suiviAssurance.delete();
        }
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setAssuranceId(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        AFAssuranceManager assuranceManager = new AFAssuranceManager();
        assuranceManager.setSession(transaction.getSession());
        assuranceManager.setForIdAssuranceReference(getAssuranceId());
        assuranceManager.find();

        if (assuranceManager.size() > 0) {
            _addError(transaction, transaction.getSession().getLabel("1760"));
        }

        AFCotisationManager cotisationManager = new AFCotisationManager();
        cotisationManager.setForAssuranceId(getAssuranceId());
        cotisationManager.setSession(transaction.getSession());
        cotisationManager.find();

        if (cotisationManager.size() > 0) {
            _addError(transaction, transaction.getSession().getLabel("1290"));
        }

        AFCouvertureManager couvertureManager = new AFCouvertureManager();
        couvertureManager.setForAssuranceId(getAssuranceId());
        couvertureManager.setSession(transaction.getSession());
        couvertureManager.find();

        if (couvertureManager.size() > 0) {
            _addError(transaction, transaction.getSession().getLabel("1300"));
        }

        AFNombreAssuresManager nombreAssureManager = new AFNombreAssuresManager();
        nombreAssureManager.setForAssuranceId(getAssuranceId());
        nombreAssureManager.setSession(getSession());
        nombreAssureManager.find();

        if (nombreAssureManager.size() > 0) {
            _addError(transaction, transaction.getSession().getLabel("1310"));
        }
    }

    @Override
    protected String _getTableName() {
        return AFAssurance.TABLE_NAME; // "AFASSUP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        assuranceId = statement.dbReadNumeric(AFAssurance.FIELD_ID_ASSURANCE);
        idAssuranceReference = statement.dbReadNumeric(AFAssurance.FIELD_ID_ASSURANCE_REFERENCE);
        assuranceLibelleAl = statement.dbReadString(AFAssurance.FIELD_ASSURANCE_LIBELLE_AL);
        assuranceLibelleFr = statement.dbReadString(AFAssurance.FIELD_ASSURANCE_LIBELLE_FR);
        assuranceLibelleIt = statement.dbReadString(AFAssurance.FIELD_ASSURANCE_LIBELLE_IT);
        assuranceLibelleCourtAl = statement.dbReadString(AFAssurance.FIELD_ASSURANCE_LIBELLE_COURT_AL);
        assuranceLibelleCourtFr = statement.dbReadString(AFAssurance.FIELD_ASSURANCE_LIBELLE_COURT_FR);
        assuranceLibelleCourtIt = statement.dbReadString(AFAssurance.FIELD_ASSURANCE_LIBELLE_COURT_IT);
        rubriqueId = statement.dbReadNumeric(AFAssurance.FIELD_ID_RUBRIQUE);
        assuranceCanton = statement.dbReadNumeric(AFAssurance.FIELD_ASSURANCE_CANTON);
        assuranceGenre = statement.dbReadNumeric(AFAssurance.FIELD_GENRE_ASSURANCE);
        typeAssurance = statement.dbReadNumeric(AFAssurance.FIELD_TYPE_ASSURANCE);
        typeCalcul = statement.dbReadNumeric(AFAssurance.FIELD_TYPE_CALCUL);
        assurance13 = statement.dbReadBoolean(AFAssurance.FIELD_ASSURANCE_13);
        tauxParCaisse = statement.dbReadBoolean(AFAssurance.FIELD_TAUX_PAR_CAISSE);
        surDocAcompte = statement.dbReadBoolean(AFAssurance.FIELD_SUR_DOC_ACOMPTE);
        decompte13Releve = statement.dbReadBoolean(AFAssurance.FIELD_DECOMPTE_13_RELEVE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        boolean validationOK = true;

        // Contrôle que les gens obligatoire soient renseignés
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceLibelleFr(),
                getSession().getLabel("380"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceLibelleCourtFr(), getSession()
                .getLabel("390"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceLibelleAl(),
                getSession().getLabel("400"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceLibelleCourtAl(), getSession()
                .getLabel("410"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceLibelleIt(),
                getSession().getLabel("420"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceLibelleCourtIt(), getSession()
                .getLabel("430"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getTypeAssurance(), getSession().getLabel("440"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getRubriqueId(), getSession().getLabel("441"));

        if (validationOK && getAssuranceId().equals(getIdAssuranceReference())) {
            _addError(statement.getTransaction(), getSession().getLabel("1770"));
            validationOK = false;
        }

        if (validationOK && (getAssuranceReference() != null)
                && !JadeStringUtil.isIntegerEmpty(getAssuranceReference().getIdAssuranceReference())) {
            _addError(statement.getTransaction(), getSession().getLabel("1780"));
            validationOK = false;
        }

        // L'assurance de référence ne doit pas avoir de taux forcé, de taux par
        // catégorie ou de taux par caisse
        if (validationOK && (getAssuranceReference() != null)) {
            List<?> tauxList = getAssuranceReference().getTauxListAll(null);
            for (int i = 0; i < tauxList.size(); i++) {
                AFTauxAssurance taux = (AFTauxAssurance) tauxList.get(i);
                if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(taux.getGenreValeur())
                        && (CodeSystem.TYPE_TAUX_FORCE.equals(taux.getTypeId())
                                || CodeSystem.TYPE_TAUX_GROUPE.equals(taux.getTypeId()) || CodeSystem.TYPE_TAUX_CAISSE
                                    .equals(taux.getTypeId()))) {
                    _addError(statement.getTransaction(), getSession().getLabel("1781"));
                    validationOK = false;
                    break;
                }
            }
        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(AFAssurance.FIELD_ID_ASSURANCE,
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(AFAssurance.FIELD_ID_ASSURANCE,
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "assuranceId"));
        statement.writeField(AFAssurance.FIELD_ID_ASSURANCE_REFERENCE,
                this._dbWriteNumeric(statement.getTransaction(), getIdAssuranceReference(), "idAssuranceReference"));
        statement.writeField(AFAssurance.FIELD_ID_RUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getRubriqueId(), "rubriqueId"));
        statement.writeField(AFAssurance.FIELD_ASSURANCE_CANTON,
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceCanton(), "assuranceCanton"));
        statement.writeField(AFAssurance.FIELD_GENRE_ASSURANCE,
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceGenre(), "assuranceGenre"));
        statement.writeField(AFAssurance.FIELD_ASSURANCE_LIBELLE_FR,
                this._dbWriteString(statement.getTransaction(), getAssuranceLibelleFr(), "assuranceLibelleFrançais"));
        statement.writeField(AFAssurance.FIELD_ASSURANCE_LIBELLE_AL,
                this._dbWriteString(statement.getTransaction(), getAssuranceLibelleAl(), "assuranceLibelleAllemand"));
        statement.writeField(AFAssurance.FIELD_ASSURANCE_LIBELLE_IT,
                this._dbWriteString(statement.getTransaction(), getAssuranceLibelleIt(), "assuranceLibelleItalien"));
        statement.writeField(AFAssurance.FIELD_ASSURANCE_LIBELLE_COURT_FR, this._dbWriteString(
                statement.getTransaction(), getAssuranceLibelleCourtFr(), "assuranceLibelleCourtFrançais"));
        statement.writeField(AFAssurance.FIELD_ASSURANCE_LIBELLE_COURT_IT, this._dbWriteString(
                statement.getTransaction(), getAssuranceLibelleCourtIt(), "assuranceLibelleCourtItalien"));
        statement.writeField(AFAssurance.FIELD_ASSURANCE_LIBELLE_COURT_AL, this._dbWriteString(
                statement.getTransaction(), getAssuranceLibelleCourtAl(), "assuranceLibelleCourtAllemand"));
        statement.writeField(AFAssurance.FIELD_TYPE_ASSURANCE,
                this._dbWriteNumeric(statement.getTransaction(), getTypeAssurance(), "typeAssurance"));
        statement.writeField(AFAssurance.FIELD_TYPE_CALCUL,
                this._dbWriteNumeric(statement.getTransaction(), getTypeCalcul(), "typeCalcul"));
        statement.writeField(AFAssurance.FIELD_ASSURANCE_13,
                this._dbWriteBoolean(statement.getTransaction(), isAssurance13(), "assurance13"));
        statement.writeField(AFAssurance.FIELD_TAUX_PAR_CAISSE,
                this._dbWriteBoolean(statement.getTransaction(), isTauxParCaisse(), "tauxParCaisse"));
        statement.writeField(AFAssurance.FIELD_SUR_DOC_ACOMPTE,
                this._dbWriteBoolean(statement.getTransaction(), isSurDocAcompte(), "surDocAcompte"));
        statement.writeField(AFAssurance.FIELD_DECOMPTE_13_RELEVE,
                this._dbWriteBoolean(statement.getTransaction(), getDecompte13Releve(), "decompte13Releve"));
    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable<?, ?> params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration<?> methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find(BManager.SIZE_NOLIMIT);
        return manager;
    }

    public java.lang.String getAssuranceCanton() {
        return assuranceCanton;
    }

    public java.lang.String getAssuranceGenre() {
        return assuranceGenre;
    }

    public java.lang.String getAssuranceId() {
        return assuranceId;
    }

    /**
     * Retourne le libelle d'assurance en fonction de la langue definie dans la session utilisateur.
     * 
     * @return le libelle d'assurance
     */
    public java.lang.String getAssuranceLibelle() {

        String langue = getSession().getIdLangueISO();

        if (JACalendar.LANGUAGE_DE.equals(langue)) {
            return assuranceLibelleAl;
        } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
            return assuranceLibelleIt;
        }
        return assuranceLibelleFr;
    }

    public java.lang.String getAssuranceLibelle(String langue) {
        if (JACalendar.LANGUAGE_DE.equals(langue)) {
            return assuranceLibelleAl;
        } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
            return assuranceLibelleIt;
        }
        return assuranceLibelleFr;
    }

    public java.lang.String getAssuranceLibelleAl() {
        return assuranceLibelleAl;
    }

    /**
     * Retourne le libelle court d'assurance en fonction de la langue definie dans la session utilisateur.
     * 
     * @return le libelle court d'assurance
     */
    public java.lang.String getAssuranceLibelleCourt() {

        String langue = getSession().getIdLangueISO();

        if (JACalendar.LANGUAGE_DE.equals(langue)) {
            return assuranceLibelleCourtAl;
        } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
            return assuranceLibelleCourtIt;
        }
        return assuranceLibelleCourtFr;
    }

    public String getAssuranceLibelleCourt(String langue) {
        if (JACalendar.LANGUAGE_DE.equals(langue)) {
            return assuranceLibelleCourtAl;
        } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
            return assuranceLibelleCourtIt;
        }
        return assuranceLibelleCourtFr;
    }

    public java.lang.String getAssuranceLibelleCourtAl() {
        return assuranceLibelleCourtAl;
    }

    public java.lang.String getAssuranceLibelleCourtFr() {
        return assuranceLibelleCourtFr;
    }

    public java.lang.String getAssuranceLibelleCourtIt() {
        return assuranceLibelleCourtIt;
    }

    public java.lang.String getAssuranceLibelleFr() {
        return assuranceLibelleFr;
    }

    public java.lang.String getAssuranceLibelleIt() {
        return assuranceLibelleIt;
    }

    /**
     * Rechercher l'assurance de Référence en fonction de son ID.
     * 
     * @return la Couverture
     */
    public AFAssurance getAssuranceReference() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdAssuranceReference())) {
            return null;
        }

        if ((_assuranceReference == null)
                || (!getIdAssuranceReference().equals(_assuranceReference.getIdAssuranceReference()))) {

            _assuranceReference = new AFAssurance();
            _assuranceReference.setSession(getSession());
            _assuranceReference.setAssuranceId(getIdAssuranceReference());
            try {
                _assuranceReference.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _assuranceReference = null;
            }
        }
        return _assuranceReference;
    }

    /**
     * Retourne le libelle de l'assurance de reference en fonction de la langue definie dans la session utilisateur.
     * 
     * @return le libelle d'assurance
     */
    public java.lang.String getAssuranceReferenceLibelle() {

        getAssuranceReference();
        String langue = getSession().getIdLangueISO();

        if (_assuranceReference != null) {
            if (JACalendar.LANGUAGE_DE.equals(langue)) {
                return _assuranceReference.getAssuranceLibelleAl();
            } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
                return _assuranceReference.getAssuranceLibelleIt();
            }
            return _assuranceReference.getAssuranceLibelleFr();
        }
        return "";
    }

    /**
     * Retourne le libelle court de l'assurance de reference en fonction de la langue definie dans la session
     * utilisateur.
     * 
     * @return le libelle d'assurance
     */
    public java.lang.String getAssuranceReferenceLibelleCourt() {

        getAssuranceReference();
        String langue = getSession().getIdLangueISO();

        if (_assuranceReference != null) {
            if (JACalendar.LANGUAGE_DE.equals(langue)) {
                return _assuranceReference.getAssuranceLibelleCourtAl();
            } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
                return _assuranceReference.getAssuranceLibelleCourtIt();
            }
            return _assuranceReference.getAssuranceLibelleCourtFr();
        }
        return "";
    }

    public Boolean getDecompte13Releve() {
        return decompte13Releve;
    }

    public java.lang.String getIdAssuranceReference() {
        return idAssuranceReference;
    }

    /**
     * Renvoie la liste des catégories de personnel à exclure pour cette assurance
     * 
     * @param date
     *            la date de validité
     * @return un tableau de String contenant les codes système des catégories de personnel à exclure
     * @throws Exception
     *             se une exception survient
     */
    public String[] getListExclusionsCatPers(String date) throws Exception {
        if (JadeStringUtil.isEmpty(date)) {
            throw new NumberFormatException();
        }
        HashSet<String> set = new HashSet<String>();
        AFParametreAssuranceManager manager = new AFParametreAssuranceManager();
        manager.setSession(getSession());
        manager.setForIdAssurance(getAssuranceId());
        manager.setForGenre(CodeSystem.GEN_PARAM_ASS_EXCLUSION_CAT);
        manager.setForDate(date);
        manager.setOrderByDateDebutDesc();
        manager.find();
        for (int i = 0; i < manager.size(); i++) {
            AFParametreAssurance entity = (AFParametreAssurance) manager.getEntity(i);
            set.add(entity.getValeurAlpha());
        }
        String[] result = new String[set.size()];
        Object[] resultObj = set.toArray();
        for (int i = 0; i < set.size(); i++) {
            result[i] = (String) resultObj[i];
        }
        return result;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFAssuranceManager();
    }

    /**
     * Retourne un paramètre associé à une assurance du genre demandé.
     * 
     * @param genre
     *            genre du paramètre, obligatoire
     * @param date
     *            la date de valeur, peut être laissée à blanc dans quel cas le paramètre le plus récent sera retourné
     * @param sexe
     *            le sexe, peut être laissé à blanc
     * @return le paramètre demandé ou null si inapplicable
     * @throws Exception
     *             si une erreur survient
     */
    public AFParametreAssurance getParametreAssurance(String genre, String date, String sexe) throws Exception {
        // pré-conditions
        if (JadeStringUtil.isEmpty(getAssuranceId()) || (getSession() == null) || JadeStringUtil.isEmpty(genre)) {
            return null;
        }
        AFParametreAssuranceManager manager = new AFParametreAssuranceManager();
        manager.setSession(getSession());
        manager.setForIdAssurance(getAssuranceId());
        manager.setForGenre(genre);
        if (!JadeStringUtil.isEmpty(date)) {
            manager.setForDate(date);
        }
        if (!JadeStringUtil.isEmpty(sexe)) {
            manager.setForSexe(sexe);
        }
        manager.setOrderByDateDebutDesc();
        manager.find();
        return (AFParametreAssurance) manager.getFirstEntity();

    }

    /**
     * Retourne la valeur d'un paramètre associé à une assurance du genre demandé.
     * 
     * @param genre
     *            genre du paramètre, obligatoire
     * @param date
     *            la date de valeur, peut être laissée à blanc dans quel cas le paramètre le plus récent sera retourné
     * @param sexe
     *            le sexe, peut être laissé à blanc
     * @return la valeur du paramètre demandé ou null si inapplicable
     * @throws Exception
     *             si une erreur survient
     */
    public String getParametreAssuranceValeur(String genre, String date, String sexe) throws Exception {
        AFParametreAssurance param = getParametreAssurance(genre, date, sexe);
        if (param == null) {
            return null;
        } else {
            return param.getValeur();
        }

    }

    /**
     * Rechercher la rubrique Comptable de l'assurance en fonction de son ID.
     * 
     * @return la rubrique Comptable
     */
    public CARubriqueViewBean getRubriqueComptable() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getRubriqueId())) {
            return null;
        }

        if (_rubriqueComptable == null) {

            _rubriqueComptable = new CARubriqueViewBean();
            _rubriqueComptable.setSession(getSession());
            _rubriqueComptable.setIdRubrique(getRubriqueId());
            try {
                _rubriqueComptable.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _rubriqueComptable = null;
            }
        }
        return _rubriqueComptable;
    }

    public java.lang.String getRubriqueId() {
        return rubriqueId;
    }

    /**
     * Retourne le taux de cette assurance. Remarques: - Lors de la recherche le sexe n'est pas pris en compte - Le type
     * "canton" n'est pas supporté
     * 
     * @param date
     *            La pour laquelle les taux doivent être valides
     * @return le taux de type AFTauxAssurance
     * @throws Exception
     */
    public AFTauxAssurance getTaux(String date) throws Exception {

        AFTauxAssuranceManager manager = new AFTauxAssuranceManager();
        manager.setSession(getSession());
        manager.setForIdAssurance(getAssuranceId());
        manager.setForDate(date);
        manager.setOrderByGenreAndDateDebutDesc();
        manager.find();

        if (manager.size() > 0) {
            return (AFTauxAssurance) manager.getEntity(0);
        }
        return null;
    }

    /**
     * Retourne la liste des Taux pour la Période et le sexe demandé. Attention !!! Ne fonctionne que pour des
     * "Taux Fixe".
     * 
     * @param sexe
     *            Le Code System du sexe (Assurance Personnel) ou null (Assurance paritaire)
     * @param dateDebut
     *            Le début de la période
     * @param dateFin
     *            La fin de la période
     * @return Une Liste de AFTauxAssurance par date croissante.
     * @throws Exception
     */
    public List<AFTauxAssurance> getTauxList(String sexe, String dateDebut, String dateFin) throws Exception {
        List<AFTauxAssurance> tauxList = new ArrayList<AFTauxAssurance>();

        AFTauxAssuranceManager manager = new AFTauxAssuranceManager();
        manager.setSession(getSession());
        manager.setForIdAssurance(getAssuranceId());
        manager.setForDate(dateFin);
        manager.setForSexe(sexe);
        manager.setOrderByGenreAndDateDebutDesc();
        manager.find();

        for (int i = 0; i < manager.size(); i++) {
            AFTauxAssurance taux = (AFTauxAssurance) manager.get(i);
            tauxList.add(0, taux);

            // Si la date de début d'assurance est <= à la date de début de la
            // période voulu,
            // on a trouvé toutes les assurances => break
            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), taux.getDateDebut(), dateDebut)) {
                break;
            }
        }
        return tauxList;
    }

    /**
     * Retourne la liste des taux relatifs à cette assurance. Cette liste est triée par rang croissant puis par date
     * décroissant de manière à ce que le plus récent des taux valides se trouve au début de la liste. Remarques: - Lors
     * de la recherche le sexe n'est pas pris en compte - Le type "canton" n'est pas supporté
     * 
     * @param date
     *            La pour laquelle les taux doivent être valides
     * @return Une liste de AFTauxAssurance
     * @throws Exception
     */
    public List<?> getTauxListAll(String date) throws Exception {

        AFTauxAssuranceManager manager = new AFTauxAssuranceManager();
        manager.setSession(getSession());
        manager.setForIdAssurance(getAssuranceId());
        manager.setForDate(date);
        manager.setOrderByGenreAndDateDebutDesc();
        manager.find();

        return manager.getContainer();
    }

    public java.lang.String getTypeAssurance() {
        return typeAssurance;
    }

    public java.lang.String getTypeCalcul() {
        return typeCalcul;
    }

    public java.lang.Boolean isAssurance13() {
        return assurance13;
    }

    public Boolean isAssuranceDeReference() throws Exception {

        Boolean result = new Boolean(false);
        AFAssuranceManager assuranceManager = new AFAssuranceManager();
        assuranceManager.setSession(getSession());
        assuranceManager.setForIdAssuranceReference(getAssuranceId());
        assuranceManager.find();

        if (assuranceManager.size() > 0) {
            result = new Boolean(true);
        }
        return result;
    }

    public java.lang.Boolean isSurDocAcompte() {
        return surDocAcompte;
    }

    public java.lang.Boolean isTauxParCaisse() {
        return tauxParCaisse;
    }

    public void setAssurance13(java.lang.Boolean newAssurance13) {
        assurance13 = newAssurance13;
    }

    public void setAssuranceCanton(java.lang.String newAssuranceCanton) {
        assuranceCanton = newAssuranceCanton;
    }

    public void setAssuranceGenre(java.lang.String newAssuranceGenre) {
        assuranceGenre = newAssuranceGenre;
    }

    public void setAssuranceId(java.lang.String newAssuranceId) {
        assuranceId = newAssuranceId;
    }

    public void setAssuranceLibelleAl(java.lang.String newAssuranceLibelleAl) {
        assuranceLibelleAl = newAssuranceLibelleAl;
    }

    public void setAssuranceLibelleCourtAl(java.lang.String newAssuranceLibelleCourtAl) {
        assuranceLibelleCourtAl = newAssuranceLibelleCourtAl;
    }

    public void setAssuranceLibelleCourtFr(java.lang.String newAssuranceLibelleCourtFr) {
        assuranceLibelleCourtFr = newAssuranceLibelleCourtFr;
    }

    public void setAssuranceLibelleCourtIt(java.lang.String newAssuranceLibelleCourtIt) {
        assuranceLibelleCourtIt = newAssuranceLibelleCourtIt;
    }

    public void setAssuranceLibelleFr(java.lang.String newAssuranceLibelleFr) {
        assuranceLibelleFr = newAssuranceLibelleFr;
    }

    public void setAssuranceLibelleIt(java.lang.String newAssuranceLibelleIt) {
        assuranceLibelleIt = newAssuranceLibelleIt;
    }

    public void setDecompte13Releve(Boolean decompte13Releve) {
        this.decompte13Releve = decompte13Releve;
    }

    public void setIdAssuranceReference(java.lang.String string) {
        idAssuranceReference = string;
        if (JadeStringUtil.isEmpty(string)) {
            _assuranceReference = null;
        }
    }

    public void setRubriqueId(java.lang.String newRubriqueId) {
        rubriqueId = newRubriqueId;
    }

    public void setSurDocAcompte(java.lang.Boolean boolean1) {
        surDocAcompte = boolean1;
    }

    public void setTauxParCaisse(java.lang.Boolean newTauxParCaisse) {
        tauxParCaisse = newTauxParCaisse;
    }

    public void setTypeAssurance(java.lang.String newTypeAssurance) {
        typeAssurance = newTypeAssurance;
    }

    public void setTypeCalcul(java.lang.String string) {
        typeCalcul = string;
    }
}
