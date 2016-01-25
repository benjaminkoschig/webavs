package globaz.osiris.db.comptes;

import globaz.osiris.api.APIOperation;

/**
 * Insérez la description du type ici. Date de création : (13.02.2002 10:28:36)
 * 
 * @author: Administrator
 */
public class CAVersementManager extends CAPaiementManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdOrdreVersement = new String();

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // Récupérer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement selon l'ordre de versement
        if (getForIdOrdreVersement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDODRDE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdOrdreVersement());
        }

        // S'il n'y a pas de sélection de )type( d'écriture, on force
        if ((getForIdTypeOperation().length() == 0) && (getLikeIdTypeOperation().length() == 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEOPERATION LIKE "
                    + this._dbWriteString(statement.getTransaction(), APIOperation.CAVERSEMENT) + "%";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAVersement();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:29:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdOrdreVersement() {
        return forIdOrdreVersement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:29:36)
     * 
     * @param newForIdOrdreVersement
     *            java.lang.String
     */
    public void setForIdOrdreVersement(java.lang.String newForIdOrdreVersement) {
        forIdOrdreVersement = newForIdOrdreVersement;
    }
}
