/*
 * Globaz SA.
 */
package ch.globaz.common.jadedb;

public interface TableDefinition {
    String getTableName();

    String getColumn();

    Class<?> getType();

    boolean isPrimaryKey();
}
