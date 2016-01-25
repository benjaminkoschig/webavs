package globaz.osiris.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.norma.db.fondation.IntTranslatable;
import globaz.norma.db.fondation.PATraductionHelper;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APITauxRubriques;
import globaz.osiris.application.CAApplication;
import java.io.Serializable;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 11:52:28)
 * 
 * @author: Administrator
 */
public class CARubrique extends BEntity implements Serializable, IntTranslatable, APIRubrique {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ALIAS = "ALIAS";

    public static final String FIELD_ANNEECOTISATION = "ANNEECOTISATION";
    public static final String FIELD_ESTVENTILEE = "ESTVENTILEE";
    public static final String FIELD_IDCONTREPARTIE = "IDCONTREPARTIE";
    public static final String FIELD_IDEXTERNE = "IDEXTERNE";
    public static final String FIELD_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String FIELD_IDSECTEUR = "IDSECTEUR";
    public static final String FIELD_IDTRADUCTION = "IDTRADUCTION";
    public static final String FIELD_LIBELLEEXTRAIT = "LIBELLEEXTRAIT";
    public static final String FIELD_NATURERUBRIQUE = "NATURERUBRIQUE";
    public static final String FIELD_NUMCOMPTECG = "NUMCOMPTECG";
    public static final String FIELD_TENIRCOMPTEUR = "TENIRCOMPTEUR";
    public static final String FIELD_USECAISSESPROF = "USECAISSESPROF";
    public static final String TABLE_CARUBRP = "CARUBRP";

    private CACompteCourant _compteCourant = null;
    private CATauxRubriques _tauxRubriques = null;
    private String alias = new String();
    private String anneeCotisation = new String();
    private FWParametersSystemCode csLibelleExtraitCompte = null;
    private FWParametersSystemCodeManager csLibelleExtraitCompteManager = null;
    // code systeme
    private FWParametersSystemCode csNatureRubrique = null;
    private FWParametersSystemCodeManager csNatureRubriques = null;
    private Boolean estVentilee = new Boolean(false);
    private String idContrepartie = new String();
    private String idExterne = new String();
    private String idExterneCompteCourantEcran = new String();

    private String idRubrique = new String();
    private String idSecteur = new String();

    private String idTraduction = new String();
    private String libelleExtraitCompte = new String();

    private boolean loadIdExterneCompteCourantEcran = false;
    private String natureRubrique = new String();
    private String numCompteCG = new String();
    private boolean saisieEcran = false;
    private Boolean tenirCompteur = new Boolean(false);
    // Traduction
    private PATraductionHelper trLibelles = null;
    private Boolean useCaissesProf = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CARubrique
     */
    public CARubrique() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 14:51:53)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Création du compte courant le cas échéant
        if (getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)
                || getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)) {
            CACompteCourant cc = new CACompteCourant();
            cc.setIdExterne(getIdExterne());
            cc.setIdRubrique(getIdRubrique());
            cc.setAlias(getAlias());
            cc.add(transaction);
            if (cc.isNew() || cc.hasErrors()) {
                _addError(transaction, getSession().getLabel("7048"));
            }
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 14:18:31)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _afterRetrieveWithResultSet(globaz.globall.db.BStatement statement) throws java.lang.Exception {

        // Signaler que l'id externe du compte courant doit être chargé
        loadIdExterneCompteCourantEcran = true;
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        setIdRubrique(this._incCounter(transaction, idRubrique));

        // Mise à jour des libellés
        getTraductionHelper().add(transaction);

    }

    /**
     * Après supression
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Vérifier s'il y a des opérations
        if (hasOperations()) {
            _addError(transaction, getSession().getLabel("7040"));
        } else {

            // Suppression de tous les libellés
            getTraductionHelper().delete(transaction);

            // Suppression du compte courant
            if (getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)
                    || getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)) {
                CACompteCourant cc = new CACompteCourant();
                cc.setIdRubrique(getIdRubrique());
                cc.setAlternateKey(APICompteCourant.AK_IDRUBRIQUE);
                cc.retrieve(transaction);
                if (!cc.isNew() || cc.hasErrors()) {
                    cc.delete(transaction);
                    if (cc.hasErrors()) {
                        _addError(transaction, getSession().getLabel("7050"));
                    }
                }
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
        trLibelles = null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().update(transaction);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CARubrique.TABLE_CARUBRP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        anneeCotisation = statement.dbReadNumeric(CARubrique.FIELD_ANNEECOTISATION);
        estVentilee = statement.dbReadBoolean(CARubrique.FIELD_ESTVENTILEE);
        idContrepartie = statement.dbReadNumeric(CARubrique.FIELD_IDCONTREPARTIE);
        idExterne = statement.dbReadString(CARubrique.FIELD_IDEXTERNE);
        idRubrique = statement.dbReadNumeric(CARubrique.FIELD_IDRUBRIQUE);
        idSecteur = statement.dbReadNumeric(CARubrique.FIELD_IDSECTEUR);
        idTraduction = statement.dbReadNumeric(CARubrique.FIELD_IDTRADUCTION);
        natureRubrique = statement.dbReadNumeric(CARubrique.FIELD_NATURERUBRIQUE);
        numCompteCG = statement.dbReadString(CARubrique.FIELD_NUMCOMPTECG);
        tenirCompteur = statement.dbReadBoolean(CARubrique.FIELD_TENIRCOMPTEUR);
        alias = statement.dbReadString(CARubrique.FIELD_ALIAS);
        libelleExtraitCompte = statement.dbReadNumeric(CARubrique.FIELD_LIBELLEEXTRAIT);
        useCaissesProf = statement.dbReadBoolean(CARubrique.FIELD_USECAISSESPROF);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdExterne(), getSession().getLabel("7044"));
        _propertyMandatory(statement.getTransaction(), getIdRubrique(), getSession().getLabel("7045"));
        _propertyMandatory(statement.getTransaction(), getNatureRubrique(), getSession().getLabel("7042"));
        _propertyMandatory(statement.getTransaction(), getIdTraduction(), getSession().getLabel("7041"));

        // Vérifier le code système nature rubrique
        if (getCsNatureRubriques().getCodeSysteme(getNatureRubrique()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("7043"));
        }

        // Synchroniser le compte courant à partir de l'écran
        if (getSaisieEcran()) {
            synchroCompteCourant();
        }

        // Vérifier contrepartie forcée et code ventilation
        if ((getIdContrepartie() != null) && !JadeStringUtil.isIntegerEmpty(getIdContrepartie())) {
            if (getEstVentilee().booleanValue()) {
                _addError(statement.getTransaction(), getSession().getLabel("7046"));
            }
        }

        // Le secteur est obligatoire s'il s'agit d'une comptabilité AVS
        if (CAApplication.getApplicationOsiris().getCAParametres().isComptabiliteAvs()) {
            if ((getIdSecteur() == null) || JadeStringUtil.isIntegerEmpty(getIdSecteur())) {
                _addError(statement.getTransaction(), getSession().getLabel("7047"));
            }
        }
        // Contrôler que la case à cocher "Tenir compteur" soit cochée pour les natures de rubrique "Amortissement" et
        // "Recouvrement"
        if (!getTenirCompteur()
                && (APIRubrique.AMORTISSEMENT.equals(getNatureRubrique()) || APIRubrique.RECOUVREMENT
                        .equals(getNatureRubrique()))) {
            _addError(statement.getTransaction(), getSession().getLabel("7403"));
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2002 11:04:42)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey)
            throws java.lang.Exception {

        // Clé alternée numéro 1 : idExterne
        switch (alternateKey) {
            case AK_IDEXTERNE:
                statement.writeKey(CARubrique.FIELD_IDEXTERNE,
                        this._dbWriteString(statement.getTransaction(), getIdExterne(), ""));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CARubrique.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CARubrique.FIELD_ANNEECOTISATION,
                this._dbWriteNumeric(statement.getTransaction(), getAnneeCotisation(), "anneeCotisation"));
        statement.writeField(CARubrique.FIELD_ESTVENTILEE, this._dbWriteBoolean(statement.getTransaction(),
                getEstVentilee(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estVentilee"));
        statement.writeField(CARubrique.FIELD_IDCONTREPARTIE,
                this._dbWriteNumeric(statement.getTransaction(), getIdContrepartie(), "idContrepartie"));
        statement.writeField(CARubrique.FIELD_IDEXTERNE,
                this._dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
        statement.writeField(CARubrique.FIELD_IDRUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(CARubrique.FIELD_IDSECTEUR,
                this._dbWriteNumeric(statement.getTransaction(), getIdSecteur(), "idSecteur"));
        statement.writeField(CARubrique.FIELD_IDTRADUCTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
        statement.writeField(CARubrique.FIELD_NATURERUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getNatureRubrique(), "natureRubrique"));
        statement.writeField(CARubrique.FIELD_NUMCOMPTECG,
                this._dbWriteString(statement.getTransaction(), getNumCompteCG(), "numCompteCG"));
        statement.writeField(CARubrique.FIELD_TENIRCOMPTEUR, this._dbWriteBoolean(statement.getTransaction(),
                getTenirCompteur(), BConstants.DB_TYPE_BOOLEAN_CHAR, "tenirCompteur"));
        statement.writeField(CARubrique.FIELD_ALIAS,
                this._dbWriteString(statement.getTransaction(), getAlias(), "alias"));
        statement.writeField(CARubrique.FIELD_LIBELLEEXTRAIT,
                this._dbWriteNumeric(statement.getTransaction(), getLibelleExtraitCompte(), "libelleExtraitCompte"));
        statement.writeField(CARubrique.FIELD_USECAISSESPROF, this._dbWriteBoolean(statement.getTransaction(),
                getUseCaissesProf(), BConstants.DB_TYPE_BOOLEAN_CHAR, "useCaissesProf"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:01:41)
     * 
     * @return String
     */
    @Override
    public String getAlias() {
        return alias;
    }

    /**
     * Getter
     */
    @Override
    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 14:00:41)
     * 
     * @return globaz.osiris.db.comptes.CACompteCourant
     */
    @Override
    public APICompteCourant getCompteCourant() {

        // Si le compte courant n'existe pas
        if (JadeStringUtil.isIntegerEmpty(getIdContrepartie())) {
            return null;
        }

        // Si le compte courant est déjà chargé
        if ((_compteCourant == null) || !_compteCourant.getIdCompteCourant().equals(getIdContrepartie())) {
            // Instancier un nouveau compte courant
            _compteCourant = new CACompteCourant();
            _compteCourant.setSession(getSession());

            // Récupérer le compte annexe
            _compteCourant.setIdCompteCourant(getIdContrepartie());
            try {
                _compteCourant.retrieve();
                if (_compteCourant.isNew() || _compteCourant.hasErrors()) {
                    _addError(null, getSession().getLabel("7049"));
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return null;
            }
        }

        return _compteCourant;
    }

    public FWParametersSystemCode getCsLibelleExtraitCompte() {
        if (csLibelleExtraitCompte == null) {
            csLibelleExtraitCompte = new FWParametersSystemCode();
            csLibelleExtraitCompte.setSession(getSession());
            csLibelleExtraitCompte.getCode(getLibelleExtraitCompte());
        }
        return csLibelleExtraitCompte;
    }

    public FWParametersSystemCodeManager getCsLibelleExtraitCompteManager() {
        if (csLibelleExtraitCompteManager == null) {
            csLibelleExtraitCompteManager = new FWParametersSystemCodeManager();
            csLibelleExtraitCompteManager.setSession(getSession());
            csLibelleExtraitCompteManager.getListeCodesSup("OSILBLREXT", getSession().getIdLangue());
        }
        return csLibelleExtraitCompteManager;
    }

    public FWParametersSystemCode getCsNatureRubrique() {
        if (csNatureRubrique == null) {
            // liste pas encore chargee, on la charge
            csNatureRubrique = new FWParametersSystemCode();
            csNatureRubrique.setSession(getSession());
            csNatureRubrique.getCode(getNatureRubrique());
        }
        return csNatureRubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2001 11:19:02)
     * 
     * @return globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsNatureRubriques() {
        // liste déjà chargée ?
        if (csNatureRubriques == null) {
            // liste pas encore chargée, on la charge
            csNatureRubriques = new FWParametersSystemCodeManager();
            csNatureRubriques.setSession(getSession());
            csNatureRubriques.getListeCodesSup("OSINATRUB", getSession().getIdLangue());
        }
        return csNatureRubriques;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.12.2001 15:16:55)
     * 
     * @return String
     */
    @Override
    public String getDescription() {
        // Description dans la langue de l'utilisateur
        return this.getDescription(null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return String
     */
    @Override
    public String getDescription(String codeIsoLangue) {

        String s = "";
        try {
            s = PATraductionHelper.translate(getSession(), getIdTraduction(), codeIsoLangue);
        } catch (Exception e) {
            _addError(null, e.toString());
        }
        // // Extraire le libellé
        // String s = this.getTraductionHelper().getDescription(codeIsoLangue);
        // if (this.getTraductionHelper().getError() != null) {
        // this._addError(null, this.getTraductionHelper().getError().getMessage());
        // }

        return s;
    }

    @Override
    public Boolean getEstVentilee() {
        return estVentilee;
    }

    @Override
    public String getIdContrepartie() {
        return idContrepartie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.12.2001 11:02:39)
     * 
     * @return String
     */
    @Override
    public String getIdentificationSource() {
        return _getTableName();
    }

    @Override
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 13:58:09)
     * 
     * @return String
     */
    @Override
    public String getIdExterneCompteCourantEcran() {

        if (loadIdExterneCompteCourantEcran && (getCompteCourant() != null)) {
            loadIdExterneCompteCourantEcran = false;
            return getCompteCourant().getIdExterne();
        } else {
            return idExterneCompteCourantEcran;
        }
    }

    @Override
    public String getIdRubrique() {
        return idRubrique;
    }

    @Override
    public String getIdSecteur() {
        return idSecteur;
    }

    @Override
    public String getIdTraduction() {
        return idTraduction;
    }

    /**
     * @return
     */
    public String getLibelleExtraitCompte() {
        return libelleExtraitCompte;
    }

    @Override
    public String getNatureRubrique() {
        return natureRubrique;
    }

    @Override
    public String getNumCompteCG() {
        return numCompteCG;
    }

    /**
     * Récupère le numéro de compte pour la mise en compte en comptabilité générale Date de création : (28.10.2002
     * 10:48:53)
     * 
     * @return String le numéro de compte en comptabilité générale
     */
    @Override
    public String getNumeroComptePourCG() {
        // Si le numéro CG est fourni
        if (!JadeStringUtil.isIntegerEmpty(getNumCompteCG())) {
            return getNumCompteCG();
            // Sinon, retourne l'identifiant externe
        } else {
            return getIdExterne();
        }
    }

    /**
     * Cette méthode retourne un String des références de la rubrique en question S'il ne trouve rien ou qu'il y a des
     * erreurs, il retourne vide
     * 
     * @param format
     *            : 1 = description du code système, 2 = numéro et description du code système, 3 = code et description
     *            du code système, 4 = numéro, code et description du code système
     * @param retourLigne
     *            : 1 toutes les informations à la suite séparées par des espaces, 2 = retour à la ligne entre chaque
     *            code
     * @return String contenant les informations
     */
    public String getReferencesRubriqueDescription(int format, int retourLigne) {
        final int FORMAT_DESCRIPTION = 1;
        final int FORMAT_NUM_DESCRIPTION = 2;
        final int FORMAT_CODE_DESCR = 3;
        final int FORMAT_NUM_CODE_DESCR = 4;
        final int LIGNE_EN_LIGNE = 1;
        String description = "";
        CAReferenceRubriqueManager manager = new CAReferenceRubriqueManager();
        manager.setSession(getSession());
        manager.setForIdRubrique(getIdRubrique());

        try {
            manager.find();
            if (manager.size() <= 0) {
                return description;
            }
            for (int i = 0; i < manager.size(); i++) {
                CAReferenceRubrique ref = (CAReferenceRubrique) manager.getEntity(i);
                if (format == FORMAT_DESCRIPTION) {
                    description = description + ref.getCsCodeReference().getCurrentCodeUtilisateur().getLibelle();
                } else if (format == FORMAT_NUM_DESCRIPTION) {
                    description = description + ref.getIdCodeReference() + " "
                            + ref.getCsCodeReference().getCurrentCodeUtilisateur().getLibelle();
                } else if (format == FORMAT_CODE_DESCR) {
                    description = description + ref.getCsCodeReference().getCurrentCodeUtilisateur().getCode();
                } else if (format == FORMAT_NUM_CODE_DESCR) {
                    description = description + ref.getIdCodeReference() + " "
                            + ref.getCsCodeReference().getCurrentCodeUtilisateur().getCode();
                }
                if ((i + 1) != manager.size()) {
                    if (retourLigne == LIGNE_EN_LIGNE) {
                        description = description + "\n";
                    } else {
                        description = description + ", ";
                    }
                }
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return description;
        }
        return description;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 14:09:39)
     * 
     * @return boolean
     */
    public boolean getSaisieEcran() {
        return saisieEcran;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.01.2002 10:36:57)
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    @Override
    public APITauxRubriques getTauxRubriques() {

        return this.getTauxRubriques("99.99.9999", null);
    }

    /**
     * Retourne les taux par défaut à une date donnée
     * 
     * @param date
     * @param idCaisseProf
     * @return CATauxRubriques
     */
    public CATauxRubriques getTauxRubriques(String date, String idCaisseProf) {
        return this.getTauxRubriques(date, idCaisseProf, "");
    }

    /**
     * Retourne les taux par défaut à une date donnée en fonction de l'order by <pr>
     * CATauxRubriquesManager.ORDER_BY_DATE_DESC CATauxRubriquesManager.ORDER_BY_DATE_ASC
     * CATauxRubriquesManager.ORDER_BY_CAISSE_PROF_ASC_DATE_DESC CATauxRubriquesManager.ORDER_BY_IDEXTERNE_ASC_DATE_ASC
     * </pr>
     * 
     * @param date
     * @param idCaisseProf
     * @param orderBy
     * @return CATauxRubriques
     */
    public CATauxRubriques getTauxRubriques(String date, String idCaisseProf, String orderBy) {

        // String derniereDate = _dbWriteDateAMJ(statement.getTransaction(),
        // date);
        CATauxRubriquesManager manager = new CATauxRubriquesManager();
        manager.setForIdRubrique(getIdRubrique());
        if (JadeStringUtil.isBlank(orderBy)) {
            manager.setOrderBy(orderBy);
        }
        manager.setUntilDate(date);
        manager.setSession(getSession());

        try {
            manager.find();
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return null;
        }
        if (manager.size() != 0) {
            _tauxRubriques = null;
            if (!JadeStringUtil.isBlankOrZero(idCaisseProf)) {
                boolean caisseDefautRenseignee = false;
                for (int i = 0; i < manager.size(); i++) {
                    CATauxRubriques rub = (CATauxRubriques) manager.getEntity(i);
                    if (rub.getIdCaisseProf().equals("0") && (caisseDefautRenseignee == false)) {
                        caisseDefautRenseignee = true;
                        _tauxRubriques = rub;
                    }
                    if (rub.getIdCaisseProf().equals(idCaisseProf)) {
                        _tauxRubriques = rub;
                        return _tauxRubriques;
                    }
                }
                return _tauxRubriques;
            } else {
                CATauxRubriques rub = (CATauxRubriques) manager.getFirstEntity();
                if (rub.getIdCaisseProf().equals("0")) {
                    _tauxRubriques = rub;
                    return _tauxRubriques;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    @Override
    public Boolean getTenirCompteur() {
        return tenirCompteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 14:57:23)
     * 
     * @return globaz.norma.db.fondation.PATraductionHelper
     */
    private PATraductionHelper getTraductionHelper() {
        if (trLibelles == null) {
            try {
                trLibelles = new PATraductionHelper(this);
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return trLibelles;
    }

    public PATraductionHelper getTrLibelles() {
        return trLibelles;
    }

    public Boolean getUseCaissesProf() {
        return useCaissesProf;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 15:16:08)
     * 
     * @return boolean
     */
    public boolean hasOperations() {

        // Vérifier s'il y a des opération
        CAEcritureManager mgr = new CAEcritureManager();
        mgr.setSession(getSession());
        mgr.setForIdCompte(getIdRubrique());
        try {
            if (mgr.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            return true;
        }
    }

    public boolean isUseCaissesProf() {
        return getUseCaissesProf().booleanValue();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:01:41)
     * 
     * @param newAlias
     *            String
     */
    @Override
    public void setAlias(String newAlias) {
        alias = newAlias;
    }

    /**
     * Setter
     */
    @Override
    public void setAnneeCotisation(String newAnneeCotisation) {
        anneeCotisation = newAnneeCotisation;
    }

    /**
     * Description dans la langue de l'utilisateur Date de création : (19.12.2001 10:55:21)
     * 
     * @param newDescription
     *            String
     */
    @Override
    public void setDescription(String newDescription) {
        this.setDescription(newDescription, null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            String
     */
    @Override
    public void setDescription(String newDescription, String codeISOLangue) {
        getTraductionHelper().setDescription(newDescription, codeISOLangue);
        if (getTraductionHelper().getError() != null) {
            _addError(null, getTraductionHelper().getError().getMessage());
        }
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            String
     * @param codeISOLangue
     *            String
     */
    @Override
    public void setDescriptionDe(String newDescription) {
        // Mise à jour du libellé
        this.setDescription(newDescription, "DE");
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            String
     * @param codeISOLangue
     *            String
     */
    @Override
    public void setDescriptionFr(String newDescription) {
        // Mise à jour du libellé
        this.setDescription(newDescription, "FR");
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            String
     * @param codeISOLangue
     *            String
     */
    @Override
    public void setDescriptionIt(String newDescription) {
        // Mise à jour du libellé
        this.setDescription(newDescription, "IT");
    }

    @Override
    public void setEstVentilee(Boolean newEstVentilee) {
        estVentilee = newEstVentilee;
    }

    @Override
    public void setIdContrepartie(String newIdContrepartie) {
        idContrepartie = newIdContrepartie;
        _compteCourant = null;
    }

    @Override
    public void setIdExterne(String newIdExterne) {
        idExterne = newIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 13:58:09)
     * 
     * @param newIdExterneCompteCourantEcran
     *            String
     */
    @Override
    public void setIdExterneCompteCourantEcran(String newIdExterneCompteCourantEcran) {
        idExterneCompteCourantEcran = newIdExterneCompteCourantEcran;
    }

    @Override
    public void setIdRubrique(String newIdRubrique) {
        idRubrique = newIdRubrique;
    }

    @Override
    public void setIdSecteur(String newIdSecteur) {
        idSecteur = newIdSecteur;
    }

    @Override
    public void setIdTraduction(String newIdTraduction) {
        idTraduction = newIdTraduction;
    }

    /**
     * @param string
     */
    public void setLibelleExtraitCompte(String string) {
        libelleExtraitCompte = string;
    }

    @Override
    public void setNatureRubrique(String newNatureRubrique) {
        natureRubrique = newNatureRubrique;
        // Le code système n'est plus valable
        csNatureRubrique = null;
    }

    @Override
    public void setNumCompteCG(String newNumCompteCG) {
        numCompteCG = newNumCompteCG;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 14:09:39)
     * 
     * @param newSaisieEcran
     *            boolean
     */
    public void setSaisieEcran(boolean newSaisieEcran) {
        saisieEcran = newSaisieEcran;
    }

    @Override
    public void setTenirCompteur(Boolean newTenirCompteur) {
        tenirCompteur = newTenirCompteur;
    }

    public void setTrLibelles(PATraductionHelper trLibelles) {
        this.trLibelles = trLibelles;
    }

    public void setUseCaissesProf(Boolean useCaissesProf) {
        this.useCaissesProf = useCaissesProf;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 14:02:12)
     */
    private void synchroCompteCourant() {

        // Vérifier les attributs vides
        if (JadeStringUtil.isBlank(getIdExterneCompteCourantEcran())) {
            setIdContrepartie("0");
            return;
        }

        // Instancier un nouveau compte courant
        _compteCourant = new CACompteCourant();
        _compteCourant.setSession(getSession());
        _compteCourant.setAlternateKey(APICompteCourant.AK_IDEXTERNE);
        _compteCourant.setIdExterne(getIdExterneCompteCourantEcran());
        try {
            _compteCourant.retrieve();
            _compteCourant.setAlternateKey(0);
            if (!_compteCourant.isNew() && !_compteCourant.hasErrors()) {
                setIdContrepartie(_compteCourant.getIdCompteCourant());
            } else {
                setIdContrepartie("0");
                _addError(null, getSession().getLabel("7051"));
            }

        } catch (Exception e) {
            _addError(null, e.getMessage());
        }

    }

}
