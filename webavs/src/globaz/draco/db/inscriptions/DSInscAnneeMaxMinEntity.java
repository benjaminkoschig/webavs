package globaz.draco.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class DSInscAnneeMaxMinEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String valeurMax = "";
    String valeurMin = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return "DSINDP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        valeurMin = statement.dbReadNumeric("VALMIN");
        valeurMax = statement.dbReadNumeric("VALMAX");

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

    public String getValeurMax() {
        return valeurMax;
    }

    public String getValeurMin() {
        return valeurMin;
    }

    public void setValeurMax(String valeurMax) {
        this.valeurMax = valeurMax;
    }

    public void setValeurMin(String valeurMin) {
        this.valeurMin = valeurMin;
    }

}
