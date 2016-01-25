package globaz.pavo.db.upidaily;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author BJO
 * 
 */
public class CIUpiDaily extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // attributs de la table CIUPIP
    private String idUpiDaily = new String();
    private String nomUpiDaily = new String();

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 l'id du dossier
        if (JadeStringUtil.isBlank(getIdUpiDaily())) {
            setIdUpiDaily(_incCounter(transaction, "0"));
        }
    }

    @Override
    protected String _getTableName() {
        return "CIUPIP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idUpiDaily = statement.dbReadNumeric("KTID");
        nomUpiDaily = statement.dbReadString("KTLNOM");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".KTID",
                _dbWriteNumeric(statement.getTransaction(), getIdUpiDaily(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KTID", _dbWriteNumeric(statement.getTransaction(), getIdUpiDaily(), "idUpiDaily"));
        statement.writeField("KTLNOM", _dbWriteString(statement.getTransaction(), getNomUpiDaily(), "nomUpiDaily"));
    }

    public String getIdUpiDaily() {
        return idUpiDaily;
    }

    public String getNomUpiDaily() {
        return nomUpiDaily;
    }

    public void setIdUpiDaily(String idUpiDaily) {
        this.idUpiDaily = idUpiDaily;
    }

    public void setNomUpiDaily(String nomUpiDaily) {
        this.nomUpiDaily = nomUpiDaily;
    }

}
