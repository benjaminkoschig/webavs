package globaz.osiris.db.interet.util.ecriturenonsoumise;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.osiris.db.comptes.CAOperation;

public class CAEcritureNonSoumise extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String date;
    private String montant;

    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
        setDate(statement.dbReadDateAMJ(CAOperation.FIELD_DATE));
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

    public String getDate() {
        return date;
    }

    public JADate getJADate() throws Exception {
        return new JADate(getDate());
    }

    public String getMontant() {
        return montant;
    }

    public FWCurrency getMontantToCurrency() {
        return new FWCurrency(getMontant());
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
