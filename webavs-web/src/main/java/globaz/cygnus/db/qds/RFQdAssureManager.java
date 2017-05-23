package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class RFQdAssureManager extends BManager {

    private static final long serialVersionUID = 1L;
    public static final String CLE_DROITS_TOUS = "";

    private String forIdGesModSolExcAugQdPreDec = "";
    private Boolean forIdGesModSolExcAugQdPreDecNotNull = Boolean.FALSE;

    private transient String fromClause = null;

    public RFQdAssureManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuilder from = new StringBuilder();

            from.append(_getCollection());
            from.append(RFQdAssure.TABLE_NAME);

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sqlWhere = new StringBuilder();

        if (!JadeStringUtil.isEmpty(forIdGesModSolExcAugQdPreDec)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQdAssure.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGesModSolExcAugQdPreDec));

        }

        if (forIdGesModSolExcAugQdPreDecNotNull) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" ( ");
            sqlWhere.append(RFQdAssure.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" IS NOT NULL ");
            sqlWhere.append(" AND ");
            sqlWhere.append(RFQdAssure.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" NOT LIKE '' ) ");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdAssure();
    }

    public String getForIdGesModSolExcAugQdPreDec() {
        return forIdGesModSolExcAugQdPreDec;
    }

    public Boolean getForIdGesModSolExcAugQdPreDecNotNull() {
        return forIdGesModSolExcAugQdPreDecNotNull;
    }

    public void setForIdGesModSolExcAugQdPreDec(String forIdGesModSolExcAugQdPreDec) {
        this.forIdGesModSolExcAugQdPreDec = forIdGesModSolExcAugQdPreDec;
    }

    public void setForIdGesModSolExcAugQdPreDecNotNull(Boolean forIdGesModSolExcAugQdPreDecNotNull) {
        this.forIdGesModSolExcAugQdPreDecNotNull = forIdGesModSolExcAugQdPreDecNotNull;
    }

}