package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.osiris.api.APIOperation;

public class CAOperationBulletinNeutreManager extends CAOperationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // R�cup�rer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // S'il n'y a pas de s�lection de type d'�criture, on force
        if ((getForIdTypeOperation().length() == 0) || (getLikeIdTypeOperation().length() == 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEOPERATION LIKE "
                    + this._dbWriteString(statement.getTransaction(), APIOperation.CAOPERATIONBULLETINNEUTRE + "%");
        }

        return sqlWhere;
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperationManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAOperationBulletinNeutre();
    }
}
