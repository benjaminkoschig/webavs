package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class CAJournalISODetail extends BEntity implements Serializable {

    private static final long serialVersionUID = 561587486462452835L;

    public static final String TABLE_CAJOISO = "CAJOISO";
    public static final String FIELD_ID = "JOISOID";
    public static final String FIELD_IDJOURNAL = "JOISOIJ";
    public static final String FIELD_MSGID = "JOISOME";
    public static final String FIELD_CREDTTM = "JOISODT";
    public static final String FIELD_NTFCTNID = "JOISONT";
    public static final String FIELD_FILENAME = "JOISOFN";

    private String idJournalISO;
    private String idJournal;
    private String messageId;
    private String createdDateTime;
    private String notificationId;
    private String fileName;

    @Override
    protected String _getTableName() {
        return CAJournalISODetail.TABLE_CAJOISO;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idJournalISO = statement.dbReadNumeric(CAJournalISODetail.FIELD_ID);
        idJournal = statement.dbReadNumeric(CAJournalISODetail.FIELD_IDJOURNAL);
        messageId = statement.dbReadString(CAJournalISODetail.FIELD_MSGID);
        createdDateTime = statement.dbReadString(CAJournalISODetail.FIELD_CREDTTM);
        notificationId = statement.dbReadString(CAJournalISODetail.FIELD_NTFCTNID);
        fileName = statement.dbReadString(CAJournalISODetail.FIELD_FILENAME);
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdJournalISO(this._incCounter(transaction, idJournalISO));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing to do
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAJournalISODetail.FIELD_ID,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalISO(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAJournalISODetail.FIELD_ID,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalISO(), "id journal ISO"));

        statement.writeField(CAJournalISODetail.FIELD_IDJOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "id journal"));

        statement.writeField(CAJournalISODetail.FIELD_MSGID,
                this._dbWriteString(statement.getTransaction(), getMessageId(), "Message ID"));

        statement.writeField(CAJournalISODetail.FIELD_CREDTTM,
                this._dbWriteString(statement.getTransaction(), getCreatedDateTime(), "Created datetime"));

        statement.writeField(CAJournalISODetail.FIELD_NTFCTNID,
                this._dbWriteString(statement.getTransaction(), getNotificationId(), "Notification Id"));

        statement.writeField(CAJournalISODetail.FIELD_FILENAME,
                this._dbWriteString(statement.getTransaction(), getFileName(), "Filename"));

    }

    public String getIdJournalISO() {
        return idJournalISO;
    }

    public void setIdJournalISO(String idJournalISO) {
        this.idJournalISO = idJournalISO;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
