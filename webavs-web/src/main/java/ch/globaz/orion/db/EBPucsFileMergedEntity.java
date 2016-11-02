package ch.globaz.orion.db;

import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class EBPucsFileMergedEntity extends JadeEntity {
    private static final long serialVersionUID = -2130929314591964282L;

    private String id;
    private String idPucFile;
    private String idMerged;

    @Override
    protected void writeProperties() {
        writeStringAsNumeric(EBPucsFileMergedTableDef.ID_PUCS_FILE, idPucFile);
        writeStringAsNumeric(EBPucsFileMergedTableDef.ID_MERGED, idMerged);
    }

    @Override
    protected void readProperties() {
        id = this.read(EBPucsFileMergedTableDef.ID);
        idPucFile = this.readString(EBPucsFileMergedTableDef.ID_PUCS_FILE);
        idMerged = this.readString(EBPucsFileMergedTableDef.ID_MERGED);
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return EBPucsFileMergedTableDef.class;
    }

    @Override
    public String getIdEntity() {
        return id;
    }

    @Override
    public void setIdEntity(String id) {
        this.id = id;
    }

    public String getIdPucFile() {
        return idPucFile;
    }

    public void setIdPucFile(String idPucFile) {
        this.idPucFile = idPucFile;
    }

    public String getIdMerged() {
        return idMerged;
    }

    public void setIdMerged(String idMerged) {
        this.idMerged = idMerged;
    }
}
