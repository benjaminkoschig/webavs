package globaz.osiris.db.rentes.check.montantpargenre;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

public class CARentesCheckMontantParGenre extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String idExterne;

    private String idRubrique;
    private String montant;

    @Override
    protected String _getTableName() {
        // Not used
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        montant = statement.dbReadNumeric(CAOperation.FIELD_MONTANT);
        idExterne = statement.dbReadString(CARubrique.FIELD_IDEXTERNE);
        idRubrique = statement.dbReadNumeric(CARubrique.FIELD_IDRUBRIQUE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing yet
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not used
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not used
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getMontant() {
        return montant;
    }

}
