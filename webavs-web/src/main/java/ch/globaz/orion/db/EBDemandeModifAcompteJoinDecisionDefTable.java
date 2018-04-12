package ch.globaz.orion.db;

import java.math.BigDecimal;
import ch.globaz.common.jadedb.TableDefinition;

public enum EBDemandeModifAcompteJoinDecisionDefTable implements TableDefinition {
    ID("ID", Integer.class, true),
    ID_DEMANDE_PORTAIL("IDDEMANDE_PORTAIL", Integer.class),
    ID_AFFILIATION("IDAFFILIATION", Integer.class),
    NUM_AFFILIE("NUMAFFILIE", String.class),
    ANNEE("ANNEE", Integer.class),
    REVENU("REVENU", BigDecimal.class),
    CAPITAL("CAPITAL", BigDecimal.class),
    CS_STATUT("CS_STATUT", Integer.class),
    REMARQUE("REMARQUE", String.class),
    DATE_RECEPTION("DATERECEPTION", Integer.class),
    ID_DECISION_REF("IDDECISION_REF", Integer.class),
    TYPE_DECISION("TYPE_DECISION", Integer.class),
    REVENU_DETERMINANT("IHMDCA", BigDecimal.class),
    REMARQUE_CP("REMARQUE_CP", String.class);

    private String column;
    private Class<?> type;
    private boolean primaryKey = false;

    EBDemandeModifAcompteJoinDecisionDefTable(String column, Class<?> type) {
        this.column = column;
        this.type = type;
    }

    EBDemandeModifAcompteJoinDecisionDefTable(String column, Class<?> type, boolean primaryKey) {
        this.column = column;
        this.type = type;
        this.primaryKey = primaryKey;
    }

    @Override
    public String getTableName() {
        return null;
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
