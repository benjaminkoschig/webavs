package ch.globaz.common.sql;

import ch.globaz.common.jadedb.TableDefinition;

enum TableDefTest implements TableDefinition {

    COL1();

    @Override
    public String getTableName() {
        return "TABLE";
    }

    @Override
    public String getColumn() {
        return "COL1";
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public boolean isPrimaryKey() {
        return false;
    }

}
