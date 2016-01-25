package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CAMaxAnneeCotisationEcritureForSection extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeCotisationMax = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setAnneeCotisationMax(statement.dbReadNumeric("anneeCotisationMax"));

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAnneeCotisationMax() {
        return anneeCotisationMax;
    }

    public void setAnneeCotisationMax(String anneeCotisationMax) {
        this.anneeCotisationMax = anneeCotisationMax;
    }

}
