package ch.globaz.common.process.byitem;

import java.util.Date;
import ch.globaz.common.jadedb.TableDefinition;

enum ProcessEntityTableDef implements TableDefinition {
    ID("ID", String.class, true),
    START_DATE("START_DATE", Date.class),
    END_DATE("END_DATE", Date.class),
    ETAT("ETAT", String.class),
    TIME_ITEM("TIME_ITEM", Long.class),
    TIME_BEFORE("TIME_BEFORE", Long.class),
    TIME_AFTER("TIME_AFTER", Long.class),
    NB_ITEM_TOTAL("NB_ITEM_TOTAL", Integer.class),
    NB_ITEM_IN_ERROR("NB_ITEM_IN_ERROR", Integer.class),
    KEY_PROCES("KEY_PROCES", String.class),
    USER("USER", String.class);

    public static final String TABLE = "PROCESS_INFO";
    private String column;
    private Class<?> type;
    private boolean primaryKey = false;

    ProcessEntityTableDef(String column, Class<?> type) {
        this.column = column;
        this.type = type;
    }

    ProcessEntityTableDef(String column, Class<?> type, boolean primaryKey) {
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
