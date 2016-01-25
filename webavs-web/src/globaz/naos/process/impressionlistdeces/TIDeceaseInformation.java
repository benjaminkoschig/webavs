package globaz.naos.process.impressionlistdeces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;

public class TIDeceaseInformation extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idTiers = new String();
    private String lastExecutionDate = new String();
    private String lastModificationDate = new String();
    private final String tableName = "TIHISTD";

    public TIDeceaseInformation() {
        super();
    }

    public TIDeceaseInformation(BSession session) {
        this();
        setSession(session);
    }

    @Override
    protected String _getTableName() {
        return tableName;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadString("IDTIERS");
        lastExecutionDate = statement.dbReadString("EXECDATE");
        lastModificationDate = statement.dbReadString("MODIFDATE");

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IDTIERS", this._dbWriteString(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("EXECDATE",
                this._dbWriteString(statement.getTransaction(), getLastExecutionDate(), "lastExecutionDate"));
        statement.writeField("MODIFDATE",
                this._dbWriteString(statement.getTransaction(), getLastModificationDate(), "lastModificationDate"));
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getLastExecutionDate() {
        return lastExecutionDate;
    }

    public String getLastModificationDate() {
        return lastModificationDate;
    }

    public void setIdTiers(String newIdTiers) {
        idTiers = newIdTiers;
    }

    public void setLastExecutionDate(String newLastExecutionDate) {
        lastExecutionDate = newLastExecutionDate;
    }

    public void setLastModificationDate(String newLastModificationDate) {
        lastModificationDate = newLastModificationDate;
    }

}
