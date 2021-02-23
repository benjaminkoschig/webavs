package globaz.prestation.interfaces.util.nss.canton;

import ch.globaz.common.domaine.Canton;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class PRCanton  extends BEntity {

    private static final long serialVersionUID = 1L;



    private String libelleCanton = "";
    private String csCanton = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        csCanton = statement.dbReadNumeric("PCOSID");
        libelleCanton = statement.dbReadString("PCOLUT");
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
     * @return the csCanton
     */
    public String getCsCanton() {
        return csCanton;
    }


    /**
     * @param csCanton
     *            the csCanton to set
     */
    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }
    public String getLibelleCanton() {
        return libelleCanton;
    }

    public void setLibelleCanton(String libelleCanton) {
        this.libelleCanton = libelleCanton;
    }

}
