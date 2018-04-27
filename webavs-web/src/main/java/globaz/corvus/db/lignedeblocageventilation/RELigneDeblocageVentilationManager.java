package globaz.corvus.db.lignedeblocageventilation;

import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;

public class RELigneDeblocageVentilationManager extends JadeManager<RELigneDeblocageVentilation> {
    Set<Long> forIdsLigneDeblocage = new HashSet<Long>();
    String forIdSection;

    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        if (!forIdsLigneDeblocage.isEmpty()) {
            sqlWhere.and(RELigneDeblocageVentilationTableDef.ID_LIGNE_DEBLOCAGE).in(forIdsLigneDeblocage);
        }
        if (!JadeStringUtil.isBlankOrZero(forIdSection)) {
            sqlWhere.and(RELigneDeblocageVentilationTableDef.ID_SECTION_SOURCE).equal(forIdSection);
        }
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RELigneDeblocageVentilation();
    }

    public void setForIdsLigneDeblocage(Set<Long> forIdsLigneDeblocage) {
        this.forIdsLigneDeblocage = forIdsLigneDeblocage;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

}
