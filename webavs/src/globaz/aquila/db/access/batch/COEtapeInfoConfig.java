package globaz.aquila.db.access.batch;

import globaz.aquila.process.COInsertQueryBuilder;
import globaz.aquila.process.ICOExportableSQL;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * <p>
 * Représente la configuration d'une information à saisir par l'utilisateur lorsqu'il veut atteindre une étape.
 * </p>
 * <p>
 * Cette classe configure le type de l'information, c'est-à-dire la façon dont elle doit être présentée dans l'écran
 * ainsi que son libellé.
 * </p>
 * <p>
 * Il y a deux types de configuration.
 * </p>
 * <p>
 * La première, pour laquelle le champ {@link #getCsLibelle()} est renseigné et le champ {@link #getDescription()} est
 * vide, représente une information utilisée par l'application d'une manière ou d'une autre. Dans ce cas, le cs est
 * utilisé tant pour l'affichage du libellé dans l'écran que pour recherche l'information dans la base.
 * </p>
 * <p>
 * La deuxième, pour laquelle le champ {@link #getDescription()} est renseigné et le champ {@link #getCsLibelle()} vaut
 * {@link #CS_PERSONNALISE}, représente une information personnalisée de l'utilisateur. Dans ce cas, l'information n'a
 * pas de signification pour l'application, seul l'utilisateur a entière liberté pour les créer et les utiliser comme
 * bon lui semble.
 * </p>
 * <p>
 * La première catégorie d'informations est pré-configurée lors du déploiement de l'application, la seconde peut être
 * effectuée par l'utilisateur.
 * </p>
 * <p>
 * Pour la première catégorie, des champs booleens permettent de séparer encore plus finement les types de
 * configurations:
 * </p>
 * <dl>
 * <dt>{@link #getAutomatique()}</dt>
 * <dd>Si vrai, ce champ indique que l'information devra être automatiquement demandée pour chaque étape pour laquelle
 * elle est configurée. Si faux, l'information ne sera pas demandée, un traitement spécial devra être effectué. Ce champ
 * est utilisé principalement pour distinguer les frais et intérêts variables dont la saisie et le traitement par la
 * suite sont fondamentalement différents des informations demandées autrement.</dd>
 * <dt>{@link #getRemplaceDateExecution()}</dt>
 * <dd>Si vrai, le champ 'date d'exécution' qui est normalement demandé dans tous les écrans ne sera pas affiché, au
 * contraire, une autre champ avec le libellé indiqué par cette configuration sera affiché.</dd>
 * </dl>
 * 
 * @author vre
 */
public class COEtapeInfoConfig extends BEntity implements ICOExportableSQL {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    public static final String CS_BOOLEAN = "5110001";

    /** DOCUMENT ME! */
    public static final String CS_CHAINE = "5110002";

    /** DOCUMENT ME! */
    public static final String CS_CODES = "5110005";

    /** DOCUMENT ME! */
    public static final String CS_DATE = "5110003";

    /** DOCUMENT ME! */
    public static final String CS_DATE_ETABLISSEMENT_ADB = "5010011";

    /** DOCUMENT ME! */
    public static final String CS_DATE_EXECUTION_SAISIE = "5010002";

    /** DOCUMENT ME! */
    public static final String CS_DATE_NOTIFICATION_CDP = "5010001";

    /** DOCUMENT ME! */
    public static final String CS_DATE_OCTROI = "5010003";

    /** DOCUMENT ME! */
    public static final String CS_DATE_RECEPTION_CDP = "5010013";

    /** DOCUMENT ME! */
    public static final String CS_DATE_RECEPTION_PV_SAISIE = "5010014";

    /** DOCUMENT ME! */
    public static final String CS_DATE_RECEPTION_RDV = "5010008";

    public static final String CS_DATE_VERSEMENT = "5010017";

    /** DOCUMENT ME! */
    public static final String CS_DELAI_VENTE = "5010005";

    /** DOCUMENT ME! */
    public static final String CS_FRAIS_VARIABLES = "5010006";

    /** DOCUMENT ME! */
    public static final String CS_GROUPE_TYPE_CONFIG = "COETAINFO";

    /** DOCUMENT ME! */
    public static final String CS_GROUPE_TYPE_INFO = "COTYPETINF";

    /** DOCUMENT ME! */
    public static final String CS_GROUPE_TYPE_SAISIE = "COTYPSAISI";
    /** DOCUMENT ME! */
    public static final String CS_INTERETS = "5010007";

    /** DOCUMENT ME! */
    public static final String CS_MONTANT_IMPUTER = "5010012";

    /** DOCUMENT ME! */
    public static final String CS_NO_SERIE_RDV = "5010015";

    /** DOCUMENT ME! */
    public static final String CS_NOMBRE = "5110004";

    /** DOCUMENT ME! */
    public static final String CS_NUMERO_ADB = "5010004";

    /** DOCUMENT ME! */
    public static final String CS_PERSONNALISE = "5010009";

    /** DOCUMENT ME! */
    public static final String CS_PV_DE_DISTRACTION = "5010010";

    /** DOCUMENT ME! */
    public static final String CS_TYPE_SAISIE = "5010016";

    /** DOCUMENT ME! */
    public static final String CS_TYPE_SAISIE_BIENS_IMMOBILIERS = "5130003";

    /** DOCUMENT ME! */
    public static final String CS_TYPE_SAISIE_BIENS_MOBILIERS = "5130002";

    /** DOCUMENT ME! */
    public static final String CS_TYPE_SAISIE_SALAIRE = "5130001";

    /** DOCUMENT ME! */
    public static final String FNAME_AUTOMATIQUE = "OMBAUT";

    /** DOCUMENT ME! */
    public static final String FNAME_CS_LIBELLE = "OMTLIB";

    /** DOCUMENT ME! */
    public static final String FNAME_CS_TYPE = "OMTTYP";

    /** DOCUMENT ME! */
    public static final String FNAME_DESCRIPTION = "OMLDES";

    /** DOCUMENT ME! */
    public static final String FNAME_IDETAPE = "OMIETA";

    /** DOCUMENT ME! */
    public static final String FNAME_IDETAPEINFOCONFIG = "OMIEIC";

    /** DOCUMENT ME! */
    public static final String FNAME_ORDRE = "OMNORD";

    /** DOCUMENT ME! */
    public static final String FNAME_PARAM = "OMLPRM";

    /** DOCUMENT ME! */
    public static final String FNAME_REMPLACEDATEEXEC = "OMBRDE";

    /** DOCUMENT ME! */
    public static final String FNAME_REQUIS = "OMBREQ";

    private static final long serialVersionUID = -1784399157726158906L;

    /** DOCUMENT ME! */
    public static final String TABLE_NAME_CONFIG = "COETINFC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean automatique = Boolean.FALSE;
    private String csLibelle = "";
    private String csType = "";
    private String description = "";
    private COEtape etape;
    private String idEtape = "";
    private String idEtapeInfoConfig = "";
    private boolean loadEtape = false;
    private String ordre = "";
    private String param = "";

    private Boolean remplaceDateExecution = Boolean.FALSE;
    private Boolean requis = Boolean.FALSE;
    private COSequence sequence;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (loadEtape) {
            // etape
            etape = new COEtape();
            etape.setIdEtape(idEtape);
            etape.setSession(getSession());
            etape.retrieve();

            // sequence
            sequence = new COSequence();
            sequence.setIdSequence(etape.getIdSequence());
            sequence.setSession(getSession());
            sequence.retrieve();
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idEtapeInfoConfig = this._incCounter(transaction, "0");
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // effacer les informations pour cette configuration
        COEtapeInfoManager infoManager = new COEtapeInfoManager();

        infoManager.setForIdEtapeInfoConfig(idEtapeInfoConfig);
        infoManager.setSession(getSession());
        infoManager.find();

        for (int idInfo = 0; idInfo < infoManager.size(); ++idInfo) {
            ((COEtapeInfo) infoManager.get(idInfo)).delete(transaction);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return COEtapeInfoConfig.TABLE_NAME_CONFIG;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idEtape = statement.dbReadNumeric(COEtapeInfoConfig.FNAME_IDETAPE);
        idEtapeInfoConfig = statement.dbReadNumeric(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG);
        csLibelle = statement.dbReadNumeric(COEtapeInfoConfig.FNAME_CS_LIBELLE);
        description = statement.dbReadString(COEtapeInfoConfig.FNAME_DESCRIPTION);
        ordre = statement.dbReadNumeric(COEtapeInfoConfig.FNAME_ORDRE);
        automatique = statement.dbReadBoolean(COEtapeInfoConfig.FNAME_AUTOMATIQUE);
        csType = statement.dbReadNumeric(COEtapeInfoConfig.FNAME_CS_TYPE);
        param = statement.dbReadString(COEtapeInfoConfig.FNAME_PARAM);
        remplaceDateExecution = statement.dbReadBoolean(COEtapeInfoConfig.FNAME_REMPLACEDATEEXEC);
        requis = statement.dbReadBoolean(COEtapeInfoConfig.FNAME_REQUIS);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), ordre, getSession().getLabel("AQUILA_ORDRE_REQUIS"));

        if (COEtapeInfoConfig.CS_PERSONNALISE.equals(csLibelle)) {
            _propertyMandatory(statement.getTransaction(), description,
                    getSession().getLabel("AQUILA_DESCRIPTION_REQUISE"));
        }

        _propertyMandatory(statement.getTransaction(), idEtape, getSession().getLabel("AQUILA_ETAPE_NON_TROUVEE"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG,
                this._dbWriteNumeric(statement.getTransaction(), idEtapeInfoConfig, "idEtapeInfoConfig"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(COEtapeInfoConfig.FNAME_IDETAPE,
                this._dbWriteNumeric(statement.getTransaction(), idEtape, "idEtape"));
        statement.writeField(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG,
                this._dbWriteNumeric(statement.getTransaction(), idEtapeInfoConfig, "idEtapeInfoConfig"));
        statement.writeField(COEtapeInfoConfig.FNAME_CS_LIBELLE,
                this._dbWriteNumeric(statement.getTransaction(), csLibelle, "csLibelle"));
        statement.writeField(COEtapeInfoConfig.FNAME_DESCRIPTION,
                this._dbWriteString(statement.getTransaction(), description, "description"));
        statement.writeField(COEtapeInfoConfig.FNAME_ORDRE,
                this._dbWriteNumeric(statement.getTransaction(), ordre, "ordre"));
        statement.writeField(COEtapeInfoConfig.FNAME_AUTOMATIQUE, this._dbWriteBoolean(statement.getTransaction(),
                automatique, BConstants.DB_TYPE_BOOLEAN_CHAR, "automatique"));
        statement.writeField(COEtapeInfoConfig.FNAME_CS_TYPE,
                this._dbWriteNumeric(statement.getTransaction(), csType, "csType"));
        statement.writeField(COEtapeInfoConfig.FNAME_PARAM,
                this._dbWriteString(statement.getTransaction(), param, "param"));
        statement.writeField(COEtapeInfoConfig.FNAME_REMPLACEDATEEXEC, this._dbWriteBoolean(statement.getTransaction(),
                remplaceDateExecution, BConstants.DB_TYPE_BOOLEAN_CHAR, "remplaceDateExecution"));
        statement.writeField(COEtapeInfoConfig.FNAME_REQUIS,
                this._dbWriteBoolean(statement.getTransaction(), requis, BConstants.DB_TYPE_BOOLEAN_CHAR, "requis"));
    }

    /**
     * @return un nom de champ identifiant cette infos de manière unique
     */
    public String createNomChamp() {
        return "etape_info_" + csLibelle;
    }

    /**
     * @see ICOExportableSQL#export(COInsertQueryBuilder, BTransaction)
     */
    @Override
    public void export(COInsertQueryBuilder query, BTransaction transaction) {
        query.addColumn(COEtapeInfoConfig.FNAME_IDETAPE, this._dbWriteNumeric(transaction, idEtape, "idEtape"));
        query.addColumn(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG,
                this._dbWriteNumeric(transaction, idEtapeInfoConfig, "idEtapeInfoConfig"));
        query.addColumn(COEtapeInfoConfig.FNAME_CS_LIBELLE, this._dbWriteNumeric(transaction, csLibelle, "csLibelle"));
        query.addColumn(COEtapeInfoConfig.FNAME_DESCRIPTION,
                this._dbWriteString(transaction, description, "description"));
        query.addColumn(COEtapeInfoConfig.FNAME_ORDRE, this._dbWriteNumeric(transaction, ordre, "ordre"));
        query.addColumn(COEtapeInfoConfig.FNAME_AUTOMATIQUE,
                this._dbWriteBoolean(transaction, automatique, BConstants.DB_TYPE_BOOLEAN_CHAR, "automatique"));
        query.addColumn(COEtapeInfoConfig.FNAME_CS_TYPE, this._dbWriteNumeric(transaction, csType, "csType"));
        query.addColumn(COEtapeInfoConfig.FNAME_PARAM, this._dbWriteString(transaction, param, "param"));
        query.addColumn(COEtapeInfoConfig.FNAME_REMPLACEDATEEXEC, this._dbWriteBoolean(transaction,
                remplaceDateExecution, BConstants.DB_TYPE_BOOLEAN_CHAR, "remplaceDateExecution"));
        query.addColumn(COEtapeInfoConfig.FNAME_REQUIS,
                this._dbWriteBoolean(transaction, requis, BConstants.DB_TYPE_BOOLEAN_CHAR, "requis"));
    }

    /**
     * si vrai, l'info sera affichée automatiquement dans l'écran de transition et celui de détail de l'historique.
     * 
     * @return la valeur courante de l'attribut automatique
     */
    public Boolean getAutomatique() {
        return automatique;
    }

    /**
     * getter pour l'attribut cs libelle.
     * 
     * @return la valeur courante de l'attribut cs libelle
     */
    public String getCsLibelle() {
        return csLibelle;
    }

    /**
     * getter pour l'attribut csType.
     * 
     * @return la valeur courante de l'attribut csType
     */
    public String getCsType() {
        return csType;
    }

    /**
     * getter pour l'attribut description.
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescription() {
        return description;
    }

    /**
     * si loadEtape est vrai, retourne l'étape pour cette configuration après retrieve.
     * 
     * @return la valeur courante de l'attribut etape
     */
    public COEtape getEtape() {
        return etape;
    }

    /**
     * getter pour l'attribut id etape.
     * 
     * @return la valeur courante de l'attribut id etape
     */
    public String getIdEtape() {
        return idEtape;
    }

    /**
     * getter pour l'attribut id etape info config.
     * 
     * @return la valeur courante de l'attribut id etape info config
     */
    public String getIdEtapeInfoConfig() {
        return idEtapeInfoConfig;
    }

    /**
     * getter pour l'attribut libelle.
     * 
     * @return la valeur courante de l'attribut libelle
     */
    public String getLibelle() {
        if (COEtapeInfoConfig.CS_PERSONNALISE.equals(getCsLibelle())) {
            return description;
        } else {
            if (getSession() != null) {
                return getSession().getCodeLibelle(csLibelle);
            } else {
                return "";
            }
        }
    }

    /**
     * getter pour l'attribut libelle type.
     * 
     * @return la valeur courante de l'attribut libelle type
     */
    public String getLibelleType() {
        if (getSession() != null) {
            return getSession().getCodeLibelle(csType);
        } else {
            return "";
        }
    }

    /**
     * getter pour l'attribut ordre.
     * 
     * @return la valeur courante de l'attribut ordre
     */
    public String getOrdre() {
        return ordre;
    }

    /**
     * @return
     */
    public String getParam() {
        return param;
    }

    /**
     * si vrai, la date d'exécution du contentieux sera remplacée par la valeur de cette info.
     * 
     * @return la valeur courante de l'attribut remplace date execution
     */
    public Boolean getRemplaceDateExecution() {
        return remplaceDateExecution;
    }

    /**
     * getter pour l'attribut requis.
     * 
     * @return la valeur courante de l'attribut requis
     */
    public Boolean getRequis() {
        return requis;
    }

    /**
     * getter pour l'attribut sequence.
     * 
     * @return la valeur courante de l'attribut sequence
     */
    public COSequence getSequence() {
        return sequence;
    }

    /**
     * @see ICOExportableSQL#getTableName()
     */
    @Override
    public String getTableName() {
        return _getTableName();
    }

    /**
     * getter pour l'attribut load etape.
     * 
     * @return la valeur courante de l'attribut load etape
     */
    public boolean isLoadEtape() {
        return loadEtape;
    }

    /**
     * setter pour l'attribut automatique.
     * 
     * @param automatique
     *            une nouvelle valeur pour cet attribut
     */
    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
    }

    /**
     * setter pour l'attribut cs libelle.
     * 
     * @param csLibelle
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsLibelle(String csLibelle) {
        this.csLibelle = csLibelle;
    }

    /**
     * setter pour l'attribut csType.
     * 
     * @param type
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsType(String type) {
        csType = type;
    }

    /**
     * setter pour l'attribut description.
     * 
     * @param description
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * setter pour l'attribut id etape.
     * 
     * @param idEtape
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEtape(String idEtape) {
        this.idEtape = idEtape;
    }

    /**
     * setter pour l'attribut id etape info config.
     * 
     * @param idEtapeInfoConfig
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEtapeInfoConfig(String idEtapeInfoConfig) {
        this.idEtapeInfoConfig = idEtapeInfoConfig;
    }

    /**
     * setter pour l'attribut ordre.
     * 
     * @param ordre
     *            une nouvelle valeur pour cet attribut
     */
    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    /**
     * @param string
     */
    public void setParam(String string) {
        param = string;
    }

    /**
     * setter pour l'attribut remplace date execution.
     * 
     * @param remplaceDateExecution
     *            une nouvelle valeur pour cet attribut
     */
    public void setRemplaceDateExecution(Boolean remplaceDateExecution) {
        this.remplaceDateExecution = remplaceDateExecution;
    }

    /**
     * setter pour l'attribut requis.
     * 
     * @param requis
     *            une nouvelle valeur pour cet attribut
     */
    public void setRequis(Boolean requis) {
        this.requis = requis;
    }

    /**
     * @param loadEtape
     *            vrai pour charger l'étape en {@link #_afterRetrieve(BTransaction)}
     */
    public void wantLoadEtape(boolean loadEtape) {
        this.loadEtape = loadEtape;
    }
}
