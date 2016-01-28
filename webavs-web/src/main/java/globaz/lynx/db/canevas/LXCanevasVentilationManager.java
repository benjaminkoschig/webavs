package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class LXCanevasVentilationManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCodeDebitCredit;
    private String forIdOperationCanevas;
    private String forIdVentilationCanevas;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXCanevasVentilation.TABLE_LXCANVP;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return _getCollection() + LXCanevasVentilation.TABLE_LXCANVP + "."
                + LXCanevasVentilation.FIELD_IDVENTILATIONCANEVAS;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdVentilationCanevas())
                && JadeStringUtil.isDigit(getForIdVentilationCanevas())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXCanevasVentilation.TABLE_LXCANVP + "."
                    + LXCanevasVentilation.FIELD_IDVENTILATIONCANEVAS + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdVentilationCanevas());

        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperationCanevas())
                && JadeStringUtil.isDigit(getForIdOperationCanevas())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXCanevasVentilation.TABLE_LXCANVP + "."
                    + LXCanevasVentilation.FIELD_IDOPERATIONCANEVAS + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOperationCanevas());

        }

        if (!JadeStringUtil.isIntegerEmpty(getForCodeDebitCredit())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + LXCanevasVentilation.TABLE_LXCANVP + "."
                    + LXCanevasVentilation.FIELD_CODEDEBITCREDIT + " = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForCodeDebitCredit());

        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXCanevasVentilation();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForCodeDebitCredit() {
        return forCodeDebitCredit;
    }

    public String getForIdOperationCanevas() {
        return forIdOperationCanevas;
    }

    public String getForIdVentilationCanevas() {
        return forIdVentilationCanevas;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCodeDebitCredit(String forCsCodeDebitCredit) {
        forCodeDebitCredit = forCsCodeDebitCredit;
    }

    public void setForIdOperationCanevas(String forIdOperationCanevas) {
        this.forIdOperationCanevas = forIdOperationCanevas;
    }

    public void setForIdVentilationCanevas(String forIdVentilationCanevas) {
        this.forIdVentilationCanevas = forIdVentilationCanevas;
    }

}
