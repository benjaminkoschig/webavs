package globaz.osiris.db.interet.util.planparsection;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;

/**
 * idPlan touché(es) pour une section.
 * 
 * @author DDA
 * 
 */
public class CAPlanParSection extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPlan;

    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdPlan(statement.dbReadNumeric(CARubriqueSoumiseInteret.FIELD_IDPLACALINT));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Nothing
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Nothing
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

}
