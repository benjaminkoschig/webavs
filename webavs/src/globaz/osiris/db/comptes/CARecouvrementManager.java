package globaz.osiris.db.comptes;

import globaz.osiris.api.APIOperation;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (13.02.2002 10:28:36)
 * 
 * @author: Administrator
 */
public class CARecouvrementManager extends CAPaiementManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdOrdreRecouvrement = new String();

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // R�cup�rer depuis la superclasse
        String sqlWhere = super._getWhere(statement);

        // traitement du positionnement selon l'ordre de versement
        if (getForIdOrdreRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDODRDE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdOrdreRecouvrement());
        }

        // S'il n'y a pas de s�lection de type d'�criture, on force
        if ((getForIdTypeOperation().length() == 0) && (getLikeIdTypeOperation().length() == 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEOPERATION LIKE "
                    + this._dbWriteString(statement.getTransaction(), APIOperation.CARECOUVREMENT) + "%";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CARecouvrement();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:29:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdOrdreRecouvrement() {
        return forIdOrdreRecouvrement;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:29:36)
     * 
     * @param newForIdOrdreRecouvrement
     *            java.lang.String
     */
    public void setForIdOrdreRecouvrement(java.lang.String newForIdOrdreRecouvrement) {
        forIdOrdreRecouvrement = newForIdOrdreRecouvrement;
    }
}
