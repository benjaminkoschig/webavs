package ch.globaz.orion.db;

import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class EBPucsFileMergedEntity extends JadeEntity {
    private String id;
    private String idPucFile;
    private String idMerged;

    @Override
    protected void writeProperties() {
        writeStringAsNumeric(EBPucsFileMergedTableDef.ID_PUCS_FILE, idPucFile);
    }

    @Override
    protected void readProperties() {
        id = this.read(EBPucsFileMergedTableDef.ID);
        idPucFile = this.readString(EBPucsFileMergedTableDef.ID_PUCS_FILE);
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

}
