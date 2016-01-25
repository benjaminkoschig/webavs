package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author BSC
 */
public class RERenteCalculee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_RENTE_CALCULEE = "YNIRCA";
    public static final String TABLE_NAME_RENTE_CALCULEE = "RERECAL";

    private String idRenteCalculee = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRenteCalculee(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return RERenteCalculee.TABLE_NAME_RENTE_CALCULEE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idRenteCalculee = statement.dbReadNumeric(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE,
                this._dbWriteNumeric(statement.getTransaction(), idRenteCalculee, "idRenteCalculee"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE,
                this._dbWriteNumeric(statement.getTransaction(), idRenteCalculee, "idRenteCalculee"));
    }

    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setIdRenteCalculee(String string) {
        idRenteCalculee = string;
    }
}
