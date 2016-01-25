package globaz.osiris.db.interet.tardif.montantsoumis;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;

/**
 * idCompteCourant touché pour une section.
 * 
 * @author DDA
 * 
 */
public class CASumMontantSoumisParPlan extends BEntity {

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

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
