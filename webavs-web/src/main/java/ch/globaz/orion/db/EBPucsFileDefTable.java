package ch.globaz.orion.db;

import java.math.BigDecimal;
import java.util.Date;
import ch.globaz.common.jadedb.TableDefinition;

public enum EBPucsFileDefTable implements TableDefinition {
    ID("ID", String.class, true),
    ID_JOB("ID_PROCESS_INFO", Integer.class),
    ID_FILE_NAME("ID_FILE_NAME", String.class),
    ID_AFFILIATION("ID_AFFILIATION", String.class),
    ANNEE_DECLARATION("ANNEE_DECLARATION", Integer.class),
    STATUS("STATUS", Integer.class),
    DATE_RECEPTION("DATE_RECEPTION", Date.class),
    HANDLING_USER("HANDLING_USER", String.class),
    NOM_AFFILIE("NOM_AFFILIE", String.class),
    NB_SALAIRES("NB_SALAIRES", Integer.class),
    NUMERO_AFFILIE("NUMERO_AFFILIE", String.class),
    PROVENANCE("PROVENANCE", String.class),
    TOTAL_CONTROLE("TOTAL_CONTROLE", BigDecimal.class),
    SIZE_FILE_IN_KO("SIZE_FILE_IN_KO", Double.class),
    IS_AF_SEUL("IS_AF_SEUL", Boolean.class),
    DUPLICATE("DUPLICATE", Boolean.class),
    SAL_INF_LIMIT("SAL_INF_LIMIT", Boolean.class),
    NIVEAU_SECURITE("NIVEAU_SECURITE", Integer.class),
    SEARCH_STRING("SEARCH_STRING", String.class);

    public static final String TABLE = "EBPUCS_FILE";
    private String column;
    private Class<?> type;
    private boolean primaryKey = false;

    EBPucsFileDefTable(String column, Class<?> type) {
        this.column = column;
        this.type = type;
    }

    EBPucsFileDefTable(String column, Class<?> type, boolean primaryKey) {
        this.column = column;
        this.type = type;
        this.primaryKey = primaryKey;
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getColumn() {
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
}
