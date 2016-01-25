/*
 * Créé le 07 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author scr
 * 
 */

public class REEntetesBlocageManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private Boolean forIsMontantBloque = null;

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (forIsMontantBloque != null && forIsMontantBloque.booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REEnteteBlocage.FIELDNAME_MONTANT_BLOQUE + ">"
                    + _dbWriteNumeric(statement.getTransaction(), "0");

        }
        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REEnteteBlocage();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE;
    }
}
