/*
 * Créé le 07 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREFactureARestituer;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author scr
 * 
 */

public class REFacturesARestituerManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    public static final String CLE_NON_TRAITE = "FACTURES_NON_TRAITEES";

    private String forCsEtat = "";
    private String forIdRA = "";

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

        if (!JadeStringUtil.isBlankOrZero(forCsEtat)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (CLE_NON_TRAITE.equals(forCsEtat)) {
                sqlWhere += REFactureARestituer.FIELDNAME_CS_ETAT + "<>"
                        + _dbWriteNumeric(statement.getTransaction(), IREFactureARestituer.CS_FACTURE);
            } else {
                sqlWhere += REFactureARestituer.FIELDNAME_CS_ETAT + "="
                        + _dbWriteNumeric(statement.getTransaction(), forCsEtat);
            }
        }

        if (!JadeStringUtil.isBlankOrZero(forIdRA)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REFactureARestituer.FIELDNAME_ID_RENTE_ACCORDEE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdRA);
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
        return new REFactureARestituer();
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForIdRA() {
        return forIdRA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REFactureARestituer.FIELDNAME_ID_FACTURE_A_RESTITUER;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForIdRA(String forIdRA) {
        this.forIdRA = forIdRA;
    }

}
