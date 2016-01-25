package globaz.osiris.db.comptes;

import globaz.osiris.api.APIOperation;

/**
 * Insérez la description du type ici. Date de création : (13.02.2002 10:25:06)
 * 
 * @author: Administrator
 */
public class CAPaiementManager extends CAEcritureManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // Récupérer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // S'il n'y a pas de sélection de type d'écriture, on force
        if ((getForIdTypeOperation().length() == 0) || (getLikeIdTypeOperation().length() == 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEOPERATION LIKE "
                    + this._dbWriteString(statement.getTransaction(), APIOperation.CAPAIEMENT + "%");
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAPaiement();
    }
}
