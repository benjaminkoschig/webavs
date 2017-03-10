/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import java.math.BigDecimal;
import ch.globaz.common.jadedb.TableDefinition;

public enum RELigneDeblocageTableDef implements TableDefinition {
    ID_DEBLOCAGE("ID_DEBLOCAGE", String.class, true),
    ID_TIERS_CREANCIER("ID_TIERS_CREANCIER", Integer.class),
    ID_ROLE_DETTE_EN_COMPTA("ID_ROLE_DETTE_EN_COMPTA", Integer.class),
    ID_TIERS_ADRESSE_PAIEMENT("ID_TIERS_ADRESSE_PAIEMENT", Integer.class),
    ID_APPLICATION_ADRESSE_PAIEMENT("ID_APPLICATION_ADRESSE_PAIEMENT", Integer.class),
    ID_SECTION_DETTE_EN_COMPTA("ID_SECTION_DETTE_EN_COMPTA", Integer.class),
    ID_PRESTATION("ID_PRESTATION", Integer.class),
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
    public String getColumn() {
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
