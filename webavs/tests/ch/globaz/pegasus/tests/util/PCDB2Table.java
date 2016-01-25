package ch.globaz.pegasus.tests.util;

public class PCDB2Table {
    private Boolean deleteOnTestStartup = true;
    private String tableName = null;

    public PCDB2Table(String tableName, Boolean deleteOnTestStartup) {
        this.deleteOnTestStartup = deleteOnTestStartup;
        this.tableName = tableName;
    }

    public Boolean getDeleteOnTestStartup() {
        return deleteOnTestStartup;
    }

    public String getTableName() {
        return tableName;
    }

    public void setDeleteOnTestStartup(Boolean deleteOnTestStartup) {
        this.deleteOnTestStartup = deleteOnTestStartup;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}