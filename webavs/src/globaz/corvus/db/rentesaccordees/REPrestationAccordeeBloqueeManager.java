/*
 * Créé le 07 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author scr
 * 
 */

public class REPrestationAccordeeBloqueeManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdEnteteBlocage = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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

        if (!JadeStringUtil.isIntegerEmpty(forIdEnteteBlocage)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationAccordeeBloquee.FIELDNAME_ID_ENTETE_BLOCAGE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdEnteteBlocage);

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
        return new REPrestationAccordeeBloquee();
    }

    public String getForIdEnteteBlocage() {
        return forIdEnteteBlocage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REPrestationAccordeeBloquee.FIELDNAME_ID_PA_BLOQUEE;
    }

    public void setForIdEnteteBlocage(String forIdEnteteBlocage) {
        this.forIdEnteteBlocage = forIdEnteteBlocage;
    }
}
