package globaz.corvus.db.lignedeblocageventilation;

import java.math.BigDecimal;
import ch.globaz.common.jadedb.TableDefinition;

public enum RELigneDeblocageVentilationTableDef implements TableDefinition {

    ID("ID", String.class, true),
    ID_LIGNE_DEBLOCAGE("ID_LIGNE_DEBLOCAGE", Long.class),
    ID_SECTION_SOURCE("ID_SECTION_SOURCE", Long.class),
    MONTANT("MONTANT", BigDecimal.class);

    public static final String TABLE_NAME = "RE_LIGNE_DEBLOCAGE_VENTIL";
    private String columnName;
    private Class<?> classType;
    private boolean primaryKey;

    RELigneDeblocageVentilationTableDef(String columnName, Class<?> classType) {
        this.columnName = columnName;
        this.classType = classType;
    }

    RELigneDeblocageVentilationTableDef(String columnName, Class<?> classType, boolean primaryKey) {
        this.columnName = columnName;
        this.classType = classType;
        this.primaryKey = primaryKey;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Class<?> getType() {
        return classType;
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
