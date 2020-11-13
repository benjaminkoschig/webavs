package globaz.apg.db.importation.turnover;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class APTurnover extends BEntity {

    public static final String TABLE_NAME = "APTURNOVERS";
    public static final String FIELDNAME_ID = "ID";
    public static final String FIELDNAME_REFERENCE_DATA = "REFERENCE_DATA";
    public static final String FIELDNAME_YEAR = "YEAR";
    public static final String FIELDNAME_MONTH = "MONTH";
    public static final String FIELDNAME_UNIT = "UNIT";
    public static final String FIELDNAME_MONTANT = "MONTANT";

    private String idTurnover;
    private String referenceData;
    private String year;
    private String month;
    private String amount;
    private String unit;

    public APTurnover() {
        year = "";
        month = "";
        amount = "";
        unit = "CHF";
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdTurnover(_incCounter(transaction, idTurnover, _getTableName()));
    }

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        referenceData = statement.dbReadString(APTurnover.FIELDNAME_REFERENCE_DATA);
        year = statement.dbReadNumeric(APTurnover.FIELDNAME_YEAR);
        month = statement.dbReadDateAMJ(APTurnover.FIELDNAME_MONTH);
        amount = statement.dbReadNumeric(APTurnover.FIELDNAME_MONTANT);
        unit = statement.dbReadString(APTurnover.FIELDNAME_UNIT);
    }

    @Override
    protected void _validate(BStatement bStatement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID,
                this._dbWriteNumeric(statement.getTransaction(), idTurnover, "id"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(APTurnover.FIELDNAME_ID,
                this._dbWriteNumeric(statement.getTransaction(), idTurnover, "id"));
        statement.writeField(APTurnover.FIELDNAME_REFERENCE_DATA,
                this._dbWriteString(statement.getTransaction(), referenceData, "ReferenceData"));
        statement.writeField(APTurnover.FIELDNAME_YEAR,
                this._dbWriteString(statement.getTransaction(), year, "Year"));
        statement.writeField(APTurnover.FIELDNAME_MONTH,
                this._dbWriteString(statement.getTransaction(), month, "Month"));
        statement.writeField(APTurnover.FIELDNAME_MONTANT,
                this._dbWriteString(statement.getTransaction(), amount, "Montant"));
        statement.writeField(APTurnover.FIELDNAME_UNIT,
                this._dbWriteString(statement.getTransaction(), unit, "Unit"));
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReferenceData() {
        return referenceData;
    }

    public void setReferenceData(String referenceData) {
        this.referenceData = referenceData;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setIdTurnover(String id) {
        this.idTurnover = id;
    }

    public String getIdTurnover(){
        return idTurnover;
    }

}
