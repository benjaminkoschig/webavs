package ch.globaz.orion.db;

import ch.globaz.common.jadedb.TableDefinition;

public enum EBPucsFileMergedTableDef implements TableDefinition {
    ID("ID", String.class, true),
    ID_PUCS_FILE("ID_PUCS_FILE", String.class),
    ID_MERGED("ID_MERGED", String.class);

    public static final String TABLE = "EBPUCS_MERGED";
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
    public String getColumnName() {
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

    @Override
    public boolean hasPspy() {
        return true;
    }

    @Override
    public boolean hasCspy() {
        return false;
    }
}
