package globaz.corvus.db.lignedeblocageventilation;

import globaz.globall.db.BEntity;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;

public class RELigneDeblocageVentilationManager extends JadeManager<RELigneDeblocageVentilation> {
    Set<Long> forIdsLigneDeblocage = new HashSet<Long>();

    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        sqlWhere.and(RELigneDeblocageVentilationTableDef.ID_LIGNE_DEBLOCAGE).in(forIdsLigneDeblocage);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RELigneDeblocageVentilation();
    }

    public void setForIdsLigneDeblocage(Set<Long> forIdsLigneDeblocage) {
        this.forIdsLigneDeblocage = forIdsLigneDeblocage;
    }

}
