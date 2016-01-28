package globaz.campus.process.chargementLot;

public class GESerieRecords {
    private GERecord1 record1;
    private GERecord2 record2;
    private GERecord3 record3;

    /**
     * Constructor for Serie50x.
     */
    public GESerieRecords() {
        super();
        record1 = new GERecord1();
        record2 = new GERecord2();
        record3 = new GERecord3();
    }

    public void addRecord(String record) {
        if ((record.substring(0, 6).endsWith("1"))) {
            record1.parse(record);
        } else if ((record.substring(0, 6).endsWith("2"))) {
            record2.parse(record);
        } else if ((record.substring(0, 6).endsWith("3"))) {
            record3.parse(record);
        }
    }

    public GERecord1 getRecord1() {
        return record1;
    }

    public GERecord2 getRecord2() {
        return record2;
    }

    public GERecord3 getRecord3() {
        return record3;
    }

    public void setRecord1(GERecord1 record1) {
        this.record1 = record1;
    }

    public void setRecord2(GERecord2 record2) {
        this.record2 = record2;
    }

    public void setRecord3(GERecord3 record3) {
        this.record3 = record3;
    }

    /**
     * Method toPrettyString.
     */
    public String toPrettyString() {
        return record1.toPrettyString() + "\n" + record2.toPrettyString() + "\n" + record3.toPrettyString();
    }

    @Override
    public String toString() {
        return record1.getRecord() + "\n" + record2.getRecord() + "\n" + record3.getRecord();
    }

}
