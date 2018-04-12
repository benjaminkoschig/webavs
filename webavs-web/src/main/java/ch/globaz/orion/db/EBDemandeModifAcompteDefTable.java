package ch.globaz.orion.db;

import java.math.BigDecimal;
import java.util.Date;
import ch.globaz.common.jadedb.TableDefinition;

public enum EBDemandeModifAcompteDefTable implements TableDefinition {
    ID("ID", Integer.class, true),
    ID_DEMANDE_PORTAIL("IDDEMANDE_PORTAIL", Integer.class),
    ID_AFFILIATION("IDAFFILIATION", Integer.class),
    NUM_AFFILIE("NUMAFFILIE", String.class),
    ANNEE("ANNEE", Integer.class),
    REVENU("REVENU", BigDecimal.class),
    CAPITAL("CAPITAL", BigDecimal.class),
    CS_STATUT("CS_STATUT", Integer.class),
    REMARQUE("REMARQUE", String.class),
    DATE_RECEPTION("DATERECEPTION", Date.class),
    ID_DECISION_REF("IDDECISION_REF", Integer.class),
    REMARQUE_CP("REMARQUE_CP", String.class);

    public static final String TABLE = "EBDEM_MODIF_ACO";
    private String column;
    private Class<?> type;
    private boolean primaryKey = false;

    EBDemandeModifAcompteDefTable(String column, Class<?> type) {
        this.column = column;
        this.type = type;
    }

    EBDemandeModifAcompteDefTable(String column, Class<?> type, boolean primaryKey) {
        this.column = column;
        this.type = type;
        this.primaryKey = primaryKey;
    }

    @Override
    public String getTableName() {
        return TABLE;
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
