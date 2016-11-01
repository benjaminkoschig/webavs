package ch.globaz.orion.db;

import ch.globaz.common.jadedb.TableDefinition;

public enum EBPucsFileMergedTableDef implements TableDefinition {
    ID("ID", String.class, true),
    ID_PUCS_FILE("ID_PUCS_FILE", String.class),
    ID_MERGED("ID_PUCS_FILE", String.class);

    public static final String TABLE = "EBPUC_FILE_MERGED";
    private String column;
    private Class<?> type;
    private boolean primaryKey = false;

    EBPucsFileMergedTableDef(String column, Class<?> type) {
        this.column = column;
        this.type = type;
    }

    EBPucsFileMergedTableDef(String column, Class<?> type, boolean primaryKey) {
        this.column = column;
        this.type = type;
        this.primaryKey = primaryKey;
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean isPrimaryKey() {
        return primaryKey;
    }
}
