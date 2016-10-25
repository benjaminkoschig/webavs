package globaz.common.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class CommonLogWIRR extends BEntity implements Serializable {

    private static final long serialVersionUID = 8755637531995554164L;

    private static final String TABLE_NAME = "LOGWIRR";

    private static final String FIELD_NAME_IDLOG = "IDLOG";
    private static final String FIELD_NAME_VISA = "VISA";
    private static final String FIELD_NAME_NOM = "NOM";
    private static final String FIELD_NAME_SEDEXID = "SEDEXID";
    private static final String FIELD_NAME_MESSAGEID = "MESSAGEID";

    private String idLog = "";
    private String visa = "";
    private String prenomNom = "";
    private String sedexId = "";
    private String messageId = "";

    public String getIdLog() {
        return idLog;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public String getVisa() {
        return visa;
    }

    public void setVisa(String visa) {
        this.visa = visa;
    }

    public String getSedexId() {
        return sedexId;
    }

    public String getPrenomNom() {
        return prenomNom;
    }

    public void setPrenomNom(String prenomNom) {
        this.prenomNom = prenomNom;
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdLog(this._incCounter(transaction, "0"));

    }

    public void setSedexId(String sedexId) {
        this.sedexId = sedexId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    protected String _getTableName() {
        return CommonLogWIRR.TABLE_NAME;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {

        idLog = statement.dbReadNumeric(CommonLogWIRR.FIELD_NAME_IDLOG);
        visa = statement.dbReadString(CommonLogWIRR.FIELD_NAME_VISA);
        prenomNom = statement.dbReadString(CommonLogWIRR.FIELD_NAME_NOM);
        sedexId = statement.dbReadString(CommonLogWIRR.FIELD_NAME_SEDEXID);
        messageId = statement.dbReadString(CommonLogWIRR.FIELD_NAME_MESSAGEID);

    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        statement.writeKey(CommonLogWIRR.FIELD_NAME_IDLOG,
                this._dbWriteNumeric(statement.getTransaction(), getIdLog(), ""));
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {

        statement.writeField(CommonLogWIRR.FIELD_NAME_IDLOG,
                this._dbWriteNumeric(statement.getTransaction(), idLog, "idLog"));
        statement.writeField(CommonLogWIRR.FIELD_NAME_VISA,
                this._dbWriteString(statement.getTransaction(), visa, "visa"));
        statement.writeField(CommonLogWIRR.FIELD_NAME_NOM,
                this._dbWriteString(statement.getTransaction(), prenomNom, "prenomNom"));
        statement.writeField(CommonLogWIRR.FIELD_NAME_SEDEXID,
                this._dbWriteString(statement.getTransaction(), sedexId, "sedexId"));
        statement.writeField(CommonLogWIRR.FIELD_NAME_MESSAGEID,
                this._dbWriteString(statement.getTransaction(), messageId, "messageId"));

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing to do
    }

}
