package globaz.corvus.db.echeances;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.infos.PRInfoCompl;

public class REDemandeJoinInfoCompl extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeInfoCompl;
    private String idInfoCompl;

    public REDemandeJoinInfoCompl() {
        super();

        idInfoCompl = "";
        csTypeInfoCompl = "";
    }

    @Override
    protected String _getTableName() {
        return PRInfoCompl.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idInfoCompl = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_ID_INFO_COMPL);
        csTypeInfoCompl = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(PRInfoCompl.FIELDNAME_ID_INFO_COMPL, idInfoCompl);
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("Entité composée");
    }

    public String getCsTypeInfoCompl() {
        return csTypeInfoCompl;
    }

    public String getIdInfoCompl() {
        return idInfoCompl;
    }

    public void setCsTypeInfoCompl(String csTypeInfoCompl) {
        this.csTypeInfoCompl = csTypeInfoCompl;
    }

    public void setIdInfoCompl(String idInfoCompl) {
        this.idInfoCompl = idInfoCompl;
    }
}
