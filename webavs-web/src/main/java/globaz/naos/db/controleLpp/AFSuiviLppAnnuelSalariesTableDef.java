/*
 * Globaz SA.
 */
package globaz.naos.db.controleLpp;

import java.math.BigDecimal;
import ch.globaz.common.jadedb.TableDefinition;

public enum AFSuiviLppAnnuelSalariesTableDef implements TableDefinition {
    ID("ID", String.class, true),
    NUMERO_AFFILIE("NUMERO_AFFILIE", String.class),
    ID_AFFILIATION("ID_AFFILIATION", Long.class),
    NSS("NSS", String.class),
    NOM_SALARIE("NOM_SALARIE", String.class),
    MOIS_DEBUT("MOIS_DEBUT", Integer.class),
    MOIS_FIN("MOIS_FIN", Integer.class),
    ANNEE("ANNEE", Integer.class),
    SALAIRE("SALAIRE", BigDecimal.class),
    NIVEAU_SECURITE("NIVEAU_SECURITE", Integer.class);

    public static final String TABLE_NAME = "AF_SUIVI_LPP_ANN_SALARIES";
    private String columnName;
    private Class<?> classType;
    private boolean primaryKey;

    AFSuiviLppAnnuelSalariesTableDef(String columnName, Class<?> classType) {
        this.columnName = columnName;
        this.classType = classType;
    }

    AFSuiviLppAnnuelSalariesTableDef(String columnName, Class<?> classType, boolean primaryKey) {
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
