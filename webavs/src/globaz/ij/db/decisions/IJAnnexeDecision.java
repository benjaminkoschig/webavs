package globaz.ij.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author SCR
 * 
 */
public class IJAnnexeDecision extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_DESCRIPTION = "XMLDES";

    public static final String FIELDNAME_ID_ANNEXE = "XMIANN";
    public static final String FIELDNAME_ID_DECISION = "XMIDEC";
    public static final String TABLE_NAME = "IJANNEXE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String description = "";
    private String idAnnexe = "";
    private String idDecision = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdAnnexe(_incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        description = statement.dbReadString(FIELDNAME_DESCRIPTION);
        idAnnexe = statement.dbReadNumeric(FIELDNAME_ID_ANNEXE);
        idDecision = statement.dbReadNumeric(FIELDNAME_ID_DECISION);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        ;
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_ANNEXE, _dbWriteNumeric(statement.getTransaction(), idAnnexe, "idAnnexe"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_DECISION,
                _dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement.writeField(FIELDNAME_ID_ANNEXE, _dbWriteNumeric(statement.getTransaction(), idAnnexe, "idAnnexe"));
        statement.writeField(FIELDNAME_DESCRIPTION,
                _dbWriteString(statement.getTransaction(), description, "description"));
    }

    public String getDescription() {
        return description;
    }

    public String getIdAnnexe() {
        return idAnnexe;
    }

    public String getIdDecision() {
        return idDecision;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdAnnexe(String idAnnexe) {
        this.idAnnexe = idAnnexe;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }
}
