package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPEcritureMemeTiers extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idAffilie = new String();
    private String idTiers = new String();
    private String numAffilie = new String();

    @Override
    protected String _getFields(BStatement statement) {
        // TODO Auto-generated method stub
        return "*";
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAffilie = statement.dbReadString("MALNAF");
        idAffilie = statement.dbReadString("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");

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

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

}
