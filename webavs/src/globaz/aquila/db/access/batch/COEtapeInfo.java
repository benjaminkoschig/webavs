package globaz.aquila.db.access.batch;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COEtapeInfo extends COEtapeInfoConfig {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String COMPLEMENT1_FRAIS_VARIABLE = "frais variable";
    public static final String COMPLEMENT1_INTERETS = "interets";
    public static final String COMPLEMENT1_TAXE = "taxe";
    public static final String FNAME_COMPLEMENT1 = "ONLCO1";
    public static final String FNAME_COMPLEMENT2 = "ONLCO2";
    public static final String FNAME_COMPLEMENT3 = "ONLCO3";
    public static final String FNAME_COMPLEMENT4 = "ONLCO4";
    public static final String FNAME_IDETAPEINFO = "ONIEIV";

    public static final String FNAME_IDHISTORIQUE = "ONIHIS";
    public static final String FNAME_VALEUR = "ONLVAL";
    private static final long serialVersionUID = -8700376846308030784L;

    public static final String TABLE_NAME_VALEUR = "COETINFV";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * @param collection
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static final String createFieldsClause(String collection) {
        StringBuffer fields = new StringBuffer();

        fields.append(COEtapeInfoConfig.FNAME_IDETAPE);
        fields.append(",");
        fields.append(COEtapeInfo.FNAME_IDETAPEINFO);
        fields.append(",");
        fields.append(collection);
        fields.append(COEtapeInfo.TABLE_NAME_VALEUR);
        fields.append(".");
        fields.append(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG);
        fields.append(",");
        fields.append(COEtapeInfo.FNAME_IDHISTORIQUE);
        fields.append(",");
        fields.append(COEtapeInfoConfig.FNAME_CS_LIBELLE);
        fields.append(",");
        fields.append(COEtapeInfoConfig.FNAME_CS_TYPE);
        fields.append(",");
        fields.append(COEtapeInfoConfig.FNAME_DESCRIPTION);
        fields.append(",");
        fields.append(COEtapeInfoConfig.FNAME_ORDRE);
        fields.append(",");
        fields.append(COEtapeInfo.FNAME_VALEUR);
        fields.append(",");
        fields.append(COEtapeInfo.FNAME_COMPLEMENT1);
        fields.append(",");
        fields.append(COEtapeInfo.FNAME_COMPLEMENT2);
        fields.append(",");
        fields.append(COEtapeInfo.FNAME_COMPLEMENT3);
        fields.append(",");
        fields.append(COEtapeInfo.FNAME_COMPLEMENT4);
        fields.append(",");
        fields.append(COEtapeInfoConfig.FNAME_AUTOMATIQUE);
        fields.append(",");
        fields.append(COEtapeInfoConfig.FNAME_REMPLACEDATEEXEC);
        fields.append(",");
        fields.append(COEtapeInfoConfig.FNAME_REQUIS);
        fields.append(",");
        fields.append(collection);
        fields.append(COEtapeInfo.TABLE_NAME_VALEUR);
        fields.append(".");
        fields.append(BSpy.FIELDNAME);

        return fields.toString();
    }

    /**
     * @param collection
     *            DOCUMENT ME!
     * @param leftJoin
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String collection, boolean leftJoin) {
        StringBuffer from = new StringBuffer();

        from.append(collection);
        from.append(COEtapeInfo.TABLE_NAME_VALEUR);

        // jointure avec la table des configs
        if (leftJoin) {
            from.append(" LEFT JOIN ");
        } else {
            from.append(" INNER JOIN ");
        }

        from.append(collection);
        from.append(COEtapeInfoConfig.TABLE_NAME_CONFIG);
        from.append(" ON ");
        from.append(collection);
        from.append(COEtapeInfo.TABLE_NAME_VALEUR);
        from.append(".");
        from.append(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG);
        from.append("=");
        from.append(collection);
        from.append(COEtapeInfoConfig.TABLE_NAME_CONFIG);
        from.append(".");
        from.append(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG);

        return from.toString();
    }

    private String complement1 = "";
    private String complement2 = "";
    private String complement3 = "";
    private String complement4 = "";
    private String idEtapeInfo = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idHistorique = "";

    private String valeur = "";

    /**
     * @return false
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idEtapeInfo = this._incCounter(transaction, "0");
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFields(BStatement statement) {
        return COEtapeInfo.createFieldsClause(_getCollection());
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return COEtapeInfo.createFromClause(_getCollection(), false);
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return COEtapeInfo.TABLE_NAME_VALEUR;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idEtapeInfo = statement.dbReadNumeric(COEtapeInfo.FNAME_IDETAPEINFO);
        idHistorique = statement.dbReadNumeric(COEtapeInfo.FNAME_IDHISTORIQUE);
        valeur = statement.dbReadString(COEtapeInfo.FNAME_VALEUR);
        complement1 = statement.dbReadString(COEtapeInfo.FNAME_COMPLEMENT1);
        complement2 = statement.dbReadString(COEtapeInfo.FNAME_COMPLEMENT2);
        complement3 = statement.dbReadString(COEtapeInfo.FNAME_COMPLEMENT3);
        complement4 = statement.dbReadString(COEtapeInfo.FNAME_COMPLEMENT4);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(COEtapeInfo.FNAME_IDETAPEINFO,
                this._dbWriteNumeric(statement.getTransaction(), idEtapeInfo, "idEtapeInfo"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG,
                    this._dbWriteNumeric(statement.getTransaction(), getIdEtapeInfoConfig(), "idEtapeInfoConfig"));
        }

        statement.writeField(COEtapeInfo.FNAME_IDETAPEINFO,
                this._dbWriteNumeric(statement.getTransaction(), idEtapeInfo, "idEtapeInfo"));
        statement.writeField(COEtapeInfo.FNAME_IDHISTORIQUE,
                this._dbWriteNumeric(statement.getTransaction(), idHistorique, "idHistorique"));
        statement.writeField(COEtapeInfo.FNAME_VALEUR,
                this._dbWriteString(statement.getTransaction(), valeur, "valeur"));
        statement.writeField(COEtapeInfo.FNAME_COMPLEMENT1,
                this._dbWriteString(statement.getTransaction(), complement1, "complement1"));
        statement.writeField(COEtapeInfo.FNAME_COMPLEMENT2,
                this._dbWriteString(statement.getTransaction(), complement2, "complement2"));
        statement.writeField(COEtapeInfo.FNAME_COMPLEMENT3,
                this._dbWriteString(statement.getTransaction(), complement3, "complement3"));
        statement.writeField(COEtapeInfo.FNAME_COMPLEMENT4,
                this._dbWriteString(statement.getTransaction(), complement4, "complement4"));
    }

    /**
     * getter pour l'attribut complement1.
     * 
     * @return la valeur courante de l'attribut complement1
     */
    public String getComplement1() {
        return complement1;
    }

    /**
     * getter pour l'attribut complement2.
     * 
     * @return la valeur courante de l'attribut complement2
     */
    public String getComplement2() {
        return complement2;
    }

    /**
     * getter pour l'attribut complement3.
     * 
     * @return la valeur courante de l'attribut complement3
     */
    public String getComplement3() {
        return complement3;
    }

    /**
     * getter pour l'attribut complement4.
     * 
     * @return la valeur courante de l'attribut complement4
     */
    public String getComplement4() {
        return complement4;
    }

    /**
     * getter pour l'attribut id etape info.
     * 
     * @return la valeur courante de l'attribut id etape info
     */
    public String getIdEtapeInfo() {
        return idEtapeInfo;
    }

    /**
     * getter pour l'attribut id historique.
     * 
     * @return la valeur courante de l'attribut id historique
     */
    public String getIdHistorique() {
        return idHistorique;
    }

    /**
     * getter pour l'attribut valeur.
     * 
     * @return la valeur courante de l'attribut valeur
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * setter pour l'attribut complement1.
     * 
     * @param complement1
     *            une nouvelle valeur pour cet attribut
     */
    public void setComplement1(String complement1) {
        this.complement1 = complement1;
    }

    /**
     * setter pour l'attribut complement2.
     * 
     * @param complement2
     *            une nouvelle valeur pour cet attribut
     */
    public void setComplement2(String complement2) {
        this.complement2 = complement2;
    }

    /**
     * setter pour l'attribut complement3.
     * 
     * @param complement3
     *            une nouvelle valeur pour cet attribut
     */
    public void setComplement3(String complement3) {
        this.complement3 = complement3;
    }

    /**
     * setter pour l'attribut complement4.
     * 
     * @param complement4
     *            une nouvelle valeur pour cet attribut
     */
    public void setComplement4(String complement4) {
        this.complement4 = complement4;
    }

    /**
     * setter pour l'attribut id etape info.
     * 
     * @param idEtapeInfo
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEtapeInfo(String idEtapeInfo) {
        this.idEtapeInfo = idEtapeInfo;
    }

    /**
     * setter pour l'attribut id historique.
     * 
     * @param idHistorique
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdHistorique(String idHistorique) {
        this.idHistorique = idHistorique;
    }

    /**
     * setter pour l'attribut valeur.
     * 
     * @param valeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}
