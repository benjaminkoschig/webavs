package globaz.ij.db.prononces;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPetiteIJ extends IJPrononce implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_CS_SITUATION_ASSURE = "XGTSIA";

    /**
     */
    public static final String FIELDNAME_ID_DERNIER_REVENU_OU_MANQUE_A_GAGNER = "XGIREV";

    /**
     */
    public static final String FIELDNAME_ID_PRONONCE_PETITE_IJ = "XGIPIJ";

    /**
     */
    public static final String FIELDNAME_SOUMIS_COTISATION_AC = "XGBCAC";

    /**
     */
    public static final String FIELDNAME_SOUMIS_COTISATION_AVS_AI_APG = "XGBCAV";

    /**
     */
    public static final String TABLE_NAME_PETITE_IJ = "IJPETITE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_PETITE_IJ);

        // jointure avec la table des prononces
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_PRONONCE);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_PRONONCE_PETITE_IJ);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_PRONONCE);

        return fromClause.toString();
    }

    private String csSituationAssure = "";
    private String idDernierRevenuOuManqueAGagner = "";
    private transient IJRevenu revenu;

    private Boolean soumisCotisationAC = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private Boolean soumisCotisationAVSAIAPG = Boolean.FALSE;

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        setCsTypeIJ(IIJPrononce.CS_PETITE_IJ);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_PETITE_IJ;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idPrononce = statement.dbReadNumeric(FIELDNAME_ID_PRONONCE_PETITE_IJ);
        csSituationAssure = statement.dbReadNumeric(FIELDNAME_CS_SITUATION_ASSURE);
        soumisCotisationAVSAIAPG = statement.dbReadBoolean(FIELDNAME_SOUMIS_COTISATION_AVS_AI_APG);
        soumisCotisationAC = statement.dbReadBoolean(FIELDNAME_SOUMIS_COTISATION_AC);
        idDernierRevenuOuManqueAGagner = statement.dbReadNumeric(FIELDNAME_ID_DERNIER_REVENU_OU_MANQUE_A_GAGNER);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_PRONONCE_PETITE_IJ,
                _dbWriteNumeric(statement.getTransaction(), getIdPrononce()));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_PRONONCE_PETITE_IJ,
                    _dbWriteNumeric(statement.getTransaction(), getIdPrononce(), "idPrononce"));
        }

        statement.writeField(FIELDNAME_CS_SITUATION_ASSURE,
                _dbWriteNumeric(statement.getTransaction(), csSituationAssure, "csSituationAssure"));
        statement.writeField(
                FIELDNAME_ID_DERNIER_REVENU_OU_MANQUE_A_GAGNER,
                _dbWriteNumeric(statement.getTransaction(), idDernierRevenuOuManqueAGagner,
                        "idDernierRevenuOuManqueAGagner"));
        statement.writeField(
                FIELDNAME_SOUMIS_COTISATION_AC,
                _dbWriteBoolean(statement.getTransaction(), soumisCotisationAC, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "soumisCotisationAC"));
        statement.writeField(
                FIELDNAME_SOUMIS_COTISATION_AVS_AI_APG,
                _dbWriteBoolean(statement.getTransaction(), soumisCotisationAVSAIAPG, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "soumisCotisationAVSAIAPG"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJPetiteIJ clone = new IJPetiteIJ();

        duplicatePrononce(clone, action);

        clone.setCsSituationAssure(getCsSituationAssure());
        clone.setSoumisCotisationAC(getSoumisCotisationAC());
        clone.setSoumisCotisationAVSAIAPG(getSoumisCotisationAVSAIAPG());
        clone.setIdDernierRevenuOuManqueAGagner(getIdDernierRevenuOuManqueAGagner());

        return clone;
    }

    /**
     * getter pour l'attribut cs situation assure
     * 
     * @return la valeur courante de l'attribut cs situation assure
     */
    public String getCsSituationAssure() {
        return csSituationAssure;
    }

    /**
     * getter pour l'attribut dernier revenu ou manque AGagner
     * 
     * @return la valeur courante de l'attribut dernier revenu ou manque AGagner
     */
    public String getIdDernierRevenuOuManqueAGagner() {
        return idDernierRevenuOuManqueAGagner;
    }

    /**
     * getter pour l'attribut soumis cotisation AC
     * 
     * @return la valeur courante de l'attribut soumis cotisation AC
     */
    public Boolean getSoumisCotisationAC() {
        return soumisCotisationAC;
    }

    /**
     * getter pour l'attribut soumis cotisation AVSAIAPG
     * 
     * @return la valeur courante de l'attribut soumis cotisation AVSAIAPG
     */
    public Boolean getSoumisCotisationAVSAIAPG() {
        return soumisCotisationAVSAIAPG;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * charge le precedent revenu (avant l'ij) pour cette petite ij.
     * 
     * @return un revenu ou null is id dernier revenu est null.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJRevenu loadRevenu() throws Exception {
        if ((revenu == null) && !JadeStringUtil.isIntegerEmpty(idDernierRevenuOuManqueAGagner)) {
            revenu = new IJRevenu();
            revenu.setIdRevenu(idDernierRevenuOuManqueAGagner);
            revenu.setSession(getSession());
            revenu.retrieve();
        }

        return revenu;
    }

    /**
     * setter pour l'attribut cs situation assure
     * 
     * @param csSituationAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsSituationAssure(String csSituationAssure) {
        this.csSituationAssure = csSituationAssure;
    }

    /**
     * setter pour l'attribut dernier revenu ou manque AGagner
     * 
     * @param dernierRevenuOuManqueAGagner
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDernierRevenuOuManqueAGagner(String dernierRevenuOuManqueAGagner) {
        idDernierRevenuOuManqueAGagner = dernierRevenuOuManqueAGagner;
    }

    /**
     * setter pour l'attribut soumis cotisation AC
     * 
     * @param soumisCotisationAC
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisCotisationAC(Boolean soumisCotisationAC) {
        this.soumisCotisationAC = soumisCotisationAC;
    }

    /**
     * setter pour l'attribut soumis cotisation AVSAIAPG
     * 
     * @param soumisCotisationAVSAIAPG
     *            une nouvelle valeur pour cet attribut
     */
    public void setSoumisCotisationAVSAIAPG(Boolean soumisCotisationAVSAIAPG) {
        this.soumisCotisationAVSAIAPG = soumisCotisationAVSAIAPG;
    }
}
