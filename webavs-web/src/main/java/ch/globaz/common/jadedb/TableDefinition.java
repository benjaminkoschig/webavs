package ch.globaz.common.jadedb;

public interface TableDefinition {
    public String getTableName();

    public String getColumn();

    public Class<?> getType();

    public boolean isPrimaryKey();
}
