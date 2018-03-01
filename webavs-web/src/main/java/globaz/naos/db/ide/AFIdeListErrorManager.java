package globaz.naos.db.ide;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

public class AFIdeListErrorManager extends BManager implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFIdeListErrorAnnonce();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder();
        sqlWhere.append(_getCollection() + AFIdeListErrorAnnonce.IDE_ANNONCE_TABLE_NAME + "."
                + AFIdeListErrorAnnonce.IDE_ANNONCE_FIELD_ETAT + "<>" + CodeSystem.ETAT_ANNONCE_IDE_TRAITE);

        return sqlWhere.toString();
    }

}
