package globaz.osiris.db.yellowreportfile;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.util.Date;

public class CAYellowReportFile extends BEntity {

    private static final long serialVersionUID = 4038910868963388307L;

    public static final String TABLE_NAME = "CAYRFI";

    public static final String FIELD_ID = "YRFIID";
    public static final String FIELD_FILENAME = "YRFIFN";
    public static final String FIELD_TYPEOF = "YRFITY";
    public static final String FIELD_STATE = "YRFIST";
    public static final String FIELD_REMARK = "YRFIRE";
    public static final String FIELD_IS_EXISTING_FILE = "YRFIIE";
    public static final String FIELD_DATE_CREATED = "YRFIDC";
    public static final String FIELD_IDBLOB_CONTENT = "YRFIIB";

    private String idISOFile;
    private String fileName;
    private CAYellowReportFileType type;
    private CAYellowReportFileState state;
    private String remark;
    private Date dateCreated;
    private String idBlobContent;

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idISOFile = statement.dbReadNumeric(CAYellowReportFile.FIELD_ID);
        fileName = statement.dbReadString(CAYellowReportFile.FIELD_FILENAME);
        type = CAYellowReportFileType.getEnumFromName(statement.dbReadString(CAYellowReportFile.FIELD_TYPEOF));
        state = CAYellowReportFileState.getEnumFromName(statement.dbReadString(CAYellowReportFile.FIELD_STATE));
        remark = statement.dbReadString(CAYellowReportFile.FIELD_REMARK);
        dateCreated = new Date(Long.valueOf(statement.dbReadNumeric(CAYellowReportFile.FIELD_DATE_CREATED)));
        idBlobContent = statement.dbReadString(CAYellowReportFile.FIELD_IDBLOB_CONTENT);
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdISOFile(this._incCounter(transaction, idISOFile));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (getFileName() == null) {
            _addError(statement.getTransaction(), "No filename defined");
        }

        if (getType() == null) {
            _addError(statement.getTransaction(), "No type defined");
        }

        if (getState() == null) {
            _addError(statement.getTransaction(), "No state defined");
        }

        if (dateCreated == null) {
            _addError(statement.getTransaction(), "No date created defined");
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAYellowReportFile.FIELD_ID,
                this._dbWriteNumeric(statement.getTransaction(), getIdISOFile(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAYellowReportFile.FIELD_ID,
                this._dbWriteNumeric(statement.getTransaction(), getIdISOFile(), "id"));

        statement.writeField(CAYellowReportFile.FIELD_FILENAME,
                this._dbWriteString(statement.getTransaction(), getFileName(), "filename"));

        statement.writeField(CAYellowReportFile.FIELD_TYPEOF,
                this._dbWriteString(statement.getTransaction(), getType().name(), "type of iso file"));

        statement.writeField(CAYellowReportFile.FIELD_STATE,
                this._dbWriteString(statement.getTransaction(), getState().name(), "state of iso file"));

        statement.writeField(CAYellowReportFile.FIELD_REMARK,
                this._dbWriteString(statement.getTransaction(), getRemark(), "remark of state"));

        statement.writeField(CAYellowReportFile.FIELD_DATE_CREATED, this._dbWriteNumeric(statement.getTransaction(),
                String.valueOf(getDateCreated().getTime()), "date created"));

        statement.writeField(CAYellowReportFile.FIELD_IDBLOB_CONTENT,
                this._dbWriteString(statement.getTransaction(), getIdBlobContent(), "id reference of blob"));
    }

    public String getIdISOFile() {
        return idISOFile;
    }

    public void setIdISOFile(String idISOFile) {
        this.idISOFile = idISOFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public CAYellowReportFileType getType() {
        return type;
    }

    public void setType(CAYellowReportFileType type) {
        this.type = type;
    }

    public CAYellowReportFileState getState() {
        return state;
    }

    public void setState(CAYellowReportFileState state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getIdBlobContent() {
        return idBlobContent;
    }

    public void setIdBlobContent(String idBlobContent) {
        this.idBlobContent = idBlobContent;
    }
}
