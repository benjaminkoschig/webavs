package globaz.corvus.db.lignedeblocage;

import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;

public class RELigneDeblocageManager extends JadeManager<RELigneDeblocage> {

    private static final long serialVersionUID = 1L;
    private Integer forIdRentePrestation;
    private RELigneDeblocageEtat forEtat;
    private RELigneDeblocageType orderByType;

    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        sqlWhere.and(RELigneDeblocageTableDef.ID_RENTE_PRESTATION).equal(forIdRentePrestation);
        sqlWhere.and(RELigneDeblocageTableDef.CS_ETAT).equal(forEtat);
    }

    @Override
    protected RELigneDeblocage _newEntity() throws Exception {
        return new RELigneDeblocage();
    }

}
