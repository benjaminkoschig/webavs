package ch.globaz.orion.db;

import ch.globaz.common.jadedb.TableDefinition;

public enum EBDemandeModifAcompteMessageDefTable implements TableDefinition {
    ID("ID", String.class, true),
    IDDEMANDE("IDDEMANDE", Integer.class),
    MESSAGE("MESSAGE", String.class);

    public static final String TABLE = "EBDEM_MODIF_ACO_MESSAGE";
    private String column;
    private Class<?> type;
    private boolean primaryKey = false;

    EBDemandeModifAcompteMessageDefTable(String column, Class<?> type) {
        this.column = column;
        this.type = type;
    }

    EBDemandeModifAcompteMessageDefTable(String column, Class<?> type, boolean primaryKey) {
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
        return true;
    }

}
