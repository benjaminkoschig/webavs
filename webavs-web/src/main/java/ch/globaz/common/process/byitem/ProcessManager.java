package ch.globaz.common.process.byitem;

import globaz.globall.db.BEntity;
import java.util.List;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;

class ProcessManager extends JadeManager<ProcessEntity> {

    private List<String> forInKeyProcess;
    private String forEtat;

    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        sqlWhere.and(ProcessEntityTableDef.KEY_PROCES).inForString(forInKeyProcess);
        sqlWhere.and(ProcessEntityTableDef.ETAT).equal(forEtat);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new ProcessEntity();
    }

    public List<String> getForInKeyProcess() {
        return forInKeyProcess;
    }

    public void setForInKeyProcess(List<String> forInKeyProcess) {
        this.forInKeyProcess = forInKeyProcess;
    }

    public void setForEtat(ProcessState forEtat) {
        if (forEtat != null) {
            this.forEtat = forEtat.toString();
        }
    }

}
