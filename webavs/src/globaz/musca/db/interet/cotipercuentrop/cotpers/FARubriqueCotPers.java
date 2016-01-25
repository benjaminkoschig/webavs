package globaz.musca.db.interet.cotipercuentrop.cotpers;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;

public class FARubriqueCotPers extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idRubrique;

    @Override
    protected String _getTableName() {
        return CARubriqueSoumiseInteret.TABLE_CAIMRSP;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdRubrique(statement.dbReadNumeric(CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE));
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

    public String getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

}
