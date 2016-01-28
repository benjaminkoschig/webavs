package globaz.osiris.db.comptes;

import globaz.osiris.api.APIOperation;

/**
 * Insérez la description du type ici. Date de création : (14.02.2002 13:35:05)
 * 
 * @author: Administrator
 */
public class CAPaiementBVRManager extends CAPaiementManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdOrganeExecution = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        // return _getCollection()+"CAOPERV2";
        return _getCollection() + "CAOPERP LEFT OUTER JOIN " + _getCollection() + "CAOPBVP ON " + _getCollection()
                + "CAOPERP.IDOPERATION=" + _getCollection() + "CAOPBVP.IDOPERATION";
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // Récupérer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // Si sélection sur l'organe d'exécution
        if (getForIdOrganeExecution().length() == 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDORGANEEXECUTION = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOrganeExecution());
        }

        // S'il n'y a pas de sélection de type d'écriture, on force
        if ((getForIdTypeOperation().length() == 0) && (getLikeIdTypeOperation().length() == 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEOPERATION LIKE "
                    + this._dbWriteString(statement.getTransaction(), APIOperation.CAPAIEMENTBVR) + "%";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAPaiementBVR();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 13:37:37)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdOrganeExecution() {
        return forIdOrganeExecution;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 13:37:37)
     * 
     * @param newForIdOrganeExecution
     *            java.lang.String
     */
    public void setForIdOrganeExecution(java.lang.String newForIdOrganeExecution) {
        forIdOrganeExecution = newForIdOrganeExecution;
    }
}
