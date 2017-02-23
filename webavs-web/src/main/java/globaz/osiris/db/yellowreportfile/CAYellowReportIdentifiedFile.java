package globaz.osiris.db.yellowreportfile;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.util.Date;

public class CAYellowReportIdentifiedFile extends BEntity {

    private static final long serialVersionUID = 4038910868963388307L;

    public static final String TABLE_NAME = "CAYRIF";

    public static final String FIELD_ID = "YRIFID";
    public static final String FIELD_FILENAME = "YRIFFN";
    public static final String FIELD_DATE_CREATED = "YRIFDC";

    private String idIdentifiedFile;
    private String fileName;
    private Date dateCreated;

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idIdentifiedFile = statement.dbReadNumeric(CAYellowReportIdentifiedFile.FIELD_ID);
        fileName = statement.dbReadString(CAYellowReportIdentifiedFile.FIELD_FILENAME);
        dateCreated = new Date(Long.valueOf(statement.dbReadNumeric(CAYellowReportIdentifiedFile.FIELD_DATE_CREATED)));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (getFileName() == null) {
            _addError(statement.getTransaction(), "No filename defined");
        }

        if (dateCreated == null) {
            _addError(statement.getTransaction(), "No date created defined");
        }
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdIdentifiedFile(this._incCounter(transaction, idIdentifiedFile));
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAYellowReportIdentifiedFile.FIELD_ID,
                this._dbWriteNumeric(statement.getTransaction(), getIdIdentifiedFile(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAYellowReportIdentifiedFile.FIELD_ID,
                this._dbWriteNumeric(statement.getTransaction(), getIdIdentifiedFile(), "id"));

        statement.writeField(CAYellowReportIdentifiedFile.FIELD_FILENAME,
                this._dbWriteString(statement.getTransaction(), getFileName(), "filename"));

        statement.writeField(CAYellowReportIdentifiedFile.FIELD_DATE_CREATED, this._dbWriteNumeric(
                statement.getTransaction(), String.valueOf(getDateCreated().getTime()), "date created"));
    }

    public String getIdIdentifiedFile() {
        return idIdentifiedFile;
    }

    public void setIdIdentifiedFile(String idIdentifiedFile) {
        this.idIdentifiedFile = idIdentifiedFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
