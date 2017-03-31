package ch.globaz.common.sql;

import ch.globaz.common.jadedb.TableDefinition;

public enum TableDefTestWithSpys implements TableDefinition {

    COL1("COL1"),
    COL2("COL2");

    private String columnName;

    private TableDefTestWithSpys(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getTableName() {
        return "TABLE";
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public boolean isPrimaryKey() {
        return false;
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