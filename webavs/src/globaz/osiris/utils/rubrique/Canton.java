package globaz.osiris.utils.rubrique;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class Canton extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String canton = "";
    private String csCanton = "";
    private String numeroCanton = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numeroCanton = statement.dbReadNumeric("NUMERO");
        canton = statement.dbReadString("CANTON");
        csCanton = statement.dbReadNumeric("PCOSID");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * @return the canton
     */
    public String getCantonLibelleCourt() {
        return canton;
    }

    /**
     * @return the csCanton
     */
    public String getCsCanton() {
        return csCanton;
    }

    /**
     * @return the numeroCanton
     */
    public String getNumeroCanton() {
        return numeroCanton;
    }

    /**
     * @param canton
     *            the canton to set
     */
    public void setCanton(String canton) {
        this.canton = canton;
    }

    /**
     * @param csCanton
     *            the csCanton to set
     */
    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    /**
     * @param numeroCanton
     *            the numeroCanton to set
     */
    public void setNumeroCanton(String numeroCanton) {
        this.numeroCanton = numeroCanton;
    }

}
