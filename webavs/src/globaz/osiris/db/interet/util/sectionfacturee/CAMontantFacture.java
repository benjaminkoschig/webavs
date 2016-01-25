package globaz.osiris.db.interet.util.sectionfacturee;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;

public class CAMontantFacture extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String montant;

    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Do nothing.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Do nothing.
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Do nothing.
    }

    public String getMontant() {
        return montant;
    }

    public FWCurrency getMontantAsCurrency() {
        return new FWCurrency(getMontant());
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
