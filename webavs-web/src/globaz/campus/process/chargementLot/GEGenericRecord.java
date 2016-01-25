package globaz.campus.process.chargementLot;

public class GEGenericRecord {
    private final GERecordField codeApplication;
    private final GERecordField codeEnregistrement;
    private GERecordField fields[];
    private String record = "";

    /**
     * Constructor for GenericRecord.
     */
    protected GEGenericRecord(String _codeEnregistrement) {
        super();
        codeApplication = new GERecordField(0, 2);
        codeApplication.setFieldValue("95");
        codeApplication.setFieldName("Code Application");
        codeApplication.setIndex(0);
        codeEnregistrement = new GERecordField(2, 5);
        codeEnregistrement.setFieldName("Code Enregistrement");
        codeEnregistrement.setFieldValue(_codeEnregistrement);
        codeEnregistrement.setIndex(1);
    }

    public String generateRecord() {
        // TODO 150 au lieu de 130
        String s = "";
        for (int i = 0; i < fields.length; i++) {
            s += fields[i].getFieldValue();
        }
        while (s.length() < 150) {
            s += " ";
        }
        return s;
    }

    /**
     * Returns the codeApplication.
     * 
     * @return RecordField
     */
    public final GERecordField getCodeApplication() {
        return codeApplication;
    }

    /**
     * Returns the codeEnregistrement.
     * 
     * @return RecordField
     */
    public final GERecordField getCodeEnregistrement() {
        return codeEnregistrement;
    }

    /**
     * Returns the fields.
     * 
     * @return RecordField[]
     */
    protected GERecordField[] getFields() {
        return fields;
    }

    /**
     * Returns the record.
     * 
     * @return String
     */
    public String getRecord() {
        return record;
    }

    /**
     * Method instanceField.
     * 
     * @param one
     *            based begin index
     * @param one
     *            based end index
     * @param one
     *            based field index (one based)
     * @param name
     * @return RecordField
     */
    protected GERecordField instanceField(int begin, int end, int index, String name) {
        GERecordField f = new GERecordField(begin - 1, end);
        f.setIndex(index - 1);
        f.setFieldName(name);
        return f;
    }

    public void parse(String s) {
        // TODO 150 au lieu de 130
        while (s.length() < 150) {
            s += " ";
        }
        setRecord(s);
        for (int i = 0; i < fields.length; i++) {
            fields[i].setFieldValue(s.substring(fields[i].getBeginIndex(), fields[i].getEndIndex()));
        }
    }

    /**
     * Sets the fields.
     * 
     * @param fields
     *            The fields to set
     */
    protected void setFields(GERecordField[] fields) {
        this.fields = fields;
    }

    /**
     * Sets the record.
     * 
     * @param record
     *            The record to set
     */
    public void setRecord(String record) {
        this.record = record;
    }

    public final String toPrettyString() {
        String s = "";
        for (int i = 0; i < fields.length; i++) {
            s += fields[i].getFieldName() + "\t = '" + fields[i].format() + "'\n";
        }
        return s;
    }

    @Override
    public final String toString() {
        String s = "";
        for (int i = 0; i < fields.length; i++) {
            s += fields[i].getFieldName() + "\t = '" + fields[i].getFieldValue() + "'\n";
        }
        return s;
    }

}
