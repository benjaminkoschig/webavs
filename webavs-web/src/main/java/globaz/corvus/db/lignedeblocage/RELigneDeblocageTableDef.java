/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import java.math.BigDecimal;
import ch.globaz.common.jadedb.TableDefinition;

public enum RELigneDeblocageTableDef implements TableDefinition {
    ID("ID", String.class, true),
    ID_TIERS_CREANCIER("ID_TIERS_CREANCIER", Long.class),
    ID_ROLE_SECTION("ID_ROLE_SECTION", Long.class),
    ID_TIERS_ADRESSE_PAIEMENT("ID_TIERS_ADRESSE_PAIEMENT", Long.class),
    ID_APPLICATION_ADRESSE_PAIEMENT("ID_APPLICATION_ADRESSE_PAIEMENT", Long.class),
    ID_SECTION_COMPENSEE("ID_SECTION_COMPENSEE", Long.class),
    ID_RENTE_ACCORDEE("ID_RENTE_ACCORDEE", Long.class),
    ID_LOT("ID_LOT", Long.class),
    CS_TYPE_DEBLOCAGE("CS_TYPE_DEBLOCAGE", Integer.class),
    CS_ETAT("CS_ETAT", Integer.class),
    MONTANT("MONTANT", BigDecimal.class),
    REFERENCE_PAIEMENT("REFERENCE_PAIEMENT", String.class);

    public static final String TABLE_NAME = "RE_LIGNE_DEBLOCAGE";
    private String columnName;
    private Class<?> classType;
    private boolean primaryKey;

    RELigneDeblocageTableDef(String columnName, Class<?> classType) {
        this.columnName = columnName;
        this.classType = classType;
    }

    RELigneDeblocageTableDef(String columnName, Class<?> classType, boolean primaryKey) {
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
}
