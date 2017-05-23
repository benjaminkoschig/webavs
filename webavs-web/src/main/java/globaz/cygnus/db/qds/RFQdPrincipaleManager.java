/*
 * Créé le 7 janvier 2009
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Set;

/**
 * @author jje
 */
public class RFQdPrincipaleManager extends BManager {

    private static final long serialVersionUID = 1L;
    public static final String CLE_DROITS_TOUS = "";

    private String forIdGesModSolExcAugQdPreDec = "";
    private Boolean forIdGesModSolExcAugQdPreDecNotNull = Boolean.FALSE;
    private Set<String> forIdsQd = null;
    private String forNotIdGesModSolExcPreDec = "";

    private transient String fromClause = null;

    public RFQdPrincipaleManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuilder from = new StringBuilder();

            from.append(_getCollection());
            from.append(RFQdPrincipale.TABLE_NAME);

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sqlWhere = new StringBuilder();

        if (!JadeStringUtil.isEmpty(forNotIdGesModSolExcPreDec)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" ( ");
            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" <> ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forNotIdGesModSolExcPreDec));
            sqlWhere.append(" AND ");
            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" IS NOT NULL ");
            sqlWhere.append(" AND ");
            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" <> ");
            sqlWhere.append(" '' ");
            sqlWhere.append(" ) ");
        }

        if (null != forIdsQd) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
            sqlWhere.append(" IN (");
            int j = 0;
            for (String id : forIdsQd) {
                if (!JadeStringUtil.isEmpty(id)) {
                    j++;
                    if (forIdsQd.size() != j) {
                        sqlWhere.append(id + ",");
                    } else {
                        sqlWhere.append(id + ") ");
                    }
                }
            }
        }

        if (!JadeStringUtil.isEmpty(forIdGesModSolExcAugQdPreDec)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGesModSolExcAugQdPreDec));

        }

        if (forIdGesModSolExcAugQdPreDecNotNull) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" ( ");
            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" IS NOT NULL ");
            sqlWhere.append(" AND ");
            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
            sqlWhere.append(" NOT LIKE '' ) ");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdPrincipale();
    }

    public String getForIdGesModSolExcAugQdPreDec() {
        return forIdGesModSolExcAugQdPreDec;
    }

    public Boolean getForIdGesModSolExcAugQdPreDecNotNull() {
        return forIdGesModSolExcAugQdPreDecNotNull;
    }

    public Set<String> getForIdsQd() {
        return forIdsQd;
    }

    public String getForNotIdGesModSolExcPreDec() {
        return forNotIdGesModSolExcPreDec;
    }

    public void setForIdGesModSolExcAugQdPreDec(String forIdGesModSolExcAugQdPreDec) {
        this.forIdGesModSolExcAugQdPreDec = forIdGesModSolExcAugQdPreDec;
    }

    public void setForIdGesModSolExcAugQdPreDecNotNull(Boolean forIdGesModSolExcAugQdPreDecNotNull) {
        this.forIdGesModSolExcAugQdPreDecNotNull = forIdGesModSolExcAugQdPreDecNotNull;
    }

    public void setForIdsQd(Set<String> forIdsQd) {
        this.forIdsQd = forIdsQd;
    }

    public void setForNotIdGesModSolExcPreDec(String forNotIdGesModSolExcPreDec) {
        this.forNotIdGesModSolExcPreDec = forNotIdGesModSolExcPreDec;
    }

}
