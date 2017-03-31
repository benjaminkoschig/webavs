/*
 * Globaz SA.
 */
package ch.globaz.common.jadedb;

public interface TableDefinition {
    String getTableName();

    String getColumnName();

    Class<?> getType();

    boolean isPrimaryKey();

    boolean hasPspy();

    boolean hasCspy();

}
