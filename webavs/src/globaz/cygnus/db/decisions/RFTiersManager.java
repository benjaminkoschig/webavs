package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.ITIAliasDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class RFTiersManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String AND = " AND ";

    public static final String F_IDTIER = "HTITIE";

    // champs
    public static final String F_NSS = "HXNAVS";
    public static final String F_TIER_COMPLEMENTNOM = "HTLDE2";
    public static final String F_TIER_NAISSANCE = "HPDNAI";
    public static final String F_TIER_NOM = "HTLDE1";
    // sql
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LIKE = " LIKE ";
    public static final String ON = " ON ";
    // tables
    public static final String T_NSS = "TIPAVSP";
    public static final String T_PERSONNE = "TIPERSP";
    public static final String T_TIER = "TITIERP";

    private String forDateNaissance;
    private transient String fromClause = null;
    // Attributs
    private String likeNom;
    private String likeNumeroNSS;

    private String likePrenom;

    public String likeAlias = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {

        String schema = _getCollection();

        StringBuffer fields = new StringBuffer();

        fields.append(" distinct ");

        fields.append(schema + RFTiersManager.T_NSS + "." + RFTiersManager.F_NSS);

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(schema + RFTiersManager.T_TIER + "." + RFTiersManager.F_TIER_NOM);

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(schema + RFTiersManager.T_TIER + "." + RFTiersManager.F_TIER_COMPLEMENTNOM);

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append("a." + RFTiersManager.F_TIER_NAISSANCE);

        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(schema + RFTiersManager.T_TIER + "." + RFTiersManager.F_IDTIER);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFTiers.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + ITITiersDefTable.TABLE_NAME + "." + ITITiersDefTable.DESIGNATION_1_MAJ);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + ITITiersDefTable.TABLE_NAME + "." + ITITiersDefTable.DESIGNATION_2_MAJ);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likeNumeroNSS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + RFTiers.T_PERSONNE_AVS + "." + RFTiers.AVS_NSS);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeNumeroNSS) + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("a." + RFTiers.TIER_DATE_DE_NAISSANCE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JadeStringUtil.isEmpty(likeAlias)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("upper(" + schema + ITIAliasDefTable.TABLE_NAME + "." + ITIAliasDefTable.LIBELLE_ALIAS
                    + ")");
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeAlias) + "%"));
        }

        return sqlWhere.toString();
    }

    // Méthodes
    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFTiers();
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroNSS() {
        return likeNumeroNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getLikeAlias() {
        return likeAlias;
    }

    public void setLikeAlias(String likeAlias) {
        this.likeAlias = likeAlias;
    }

    public void setLikeNumeroNSS(String likeNumeroNSS) {
        this.likeNumeroNSS = likeNumeroNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

}
