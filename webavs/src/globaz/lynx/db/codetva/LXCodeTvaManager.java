package globaz.lynx.db.codetva;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class LXCodeTvaManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsCodeTVA;
    private String forDateBetween;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXCodeTva.TABLE_LXCTVAP;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForCsCodeTVA()) && JadeStringUtil.isDigit(getForCsCodeTVA())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXCodeTva.FIELD_CSCODETVA).append(" = ")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsCodeTVA()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateBetween())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXCodeTva.FIELD_DATEDEBUT).append(" <= ")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateBetween()));
            sqlWhere.append(" AND (");
            sqlWhere.append(LXCodeTva.FIELD_DATEFIN).append(" >= ")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getForDateBetween()));
            sqlWhere.append(" OR ").append(LXCodeTva.FIELD_DATEFIN).append(" = 0)");
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXCodeTva();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCsCodeTVA() {
        return forCsCodeTVA;
    }

    public String getForDateBetween() {
        return forDateBetween;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsCodeTVA(String forCsCodeTVA) {
        this.forCsCodeTVA = forCsCodeTVA;
    }

    public void setForDateBetween(String forDateBetween) {
        this.forDateBetween = forDateBetween;
    }

}
