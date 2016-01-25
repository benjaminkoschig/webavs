package globaz.lynx.db.ventilation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class LXVentilationManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeDebitCredit;
    private String forIdOperation;
    private String forIdVentilation;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXVentilation.TABLE_LXVENTP;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return _getCollection() + LXVentilation.TABLE_LXVENTP + "." + LXVentilation.FIELD_IDVENTILATION;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdVentilation()) && JadeStringUtil.isDigit(getForIdVentilation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXVentilation.TABLE_LXVENTP + "." + LXVentilation.FIELD_IDVENTILATION
                    + " = " + _dbWriteNumeric(statement.getTransaction(), getForIdVentilation());

        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperation()) && JadeStringUtil.isDigit(getForIdOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXVentilation.TABLE_LXVENTP + "." + LXVentilation.FIELD_IDOPERATION + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdOperation());

        }

        if (!JadeStringUtil.isIntegerEmpty(getForCodeDebitCredit())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXVentilation.TABLE_LXVENTP + "." + LXVentilation.FIELD_CODEDEBITCREDIT
                    + " = " + _dbWriteNumeric(statement.getTransaction(), getForCodeDebitCredit());

        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXVentilation();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCodeDebitCredit() {
        return forCodeDebitCredit;
    }

    public String getForIdOperation() {
        return forIdOperation;
    }

    public String getForIdVentilation() {
        return forIdVentilation;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCodeDebitCredit(String forCsCodeDebitCredit) {
        forCodeDebitCredit = forCsCodeDebitCredit;
    }

    public void setForIdOperation(String forIdOperation) {
        this.forIdOperation = forIdOperation;
    }

    public void setForIdVentilation(String forIdVentilation) {
        this.forIdVentilation = forIdVentilation;
    }

}
