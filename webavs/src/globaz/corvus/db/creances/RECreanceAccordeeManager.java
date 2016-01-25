/*
 * Créé le 18 juil. 07
 */

package globaz.corvus.db.creances;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author BSC
 * 
 */
public class RECreanceAccordeeManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // criteres sur le creancier
    private String forCsTypeCreancier = "";
    private String forCsTypeCreancierNotEqual = "";
    private String forIdCreancier = "";

    private String forIdOrdreVersement = "";
    private String forIdRenteAccordee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        StringBuffer fromClauseBuffer = new StringBuffer(super._getFrom(statement));

        if (!JadeStringUtil.isIntegerEmpty(forCsTypeCreancier)
                || !JadeStringUtil.isIntegerEmpty(forCsTypeCreancierNotEqual)) {

            // jointure entre table des creances accordees et table des
            // creanciers
            fromClauseBuffer.append(innerJoin);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(RECreancier.TABLE_NAME_CREANCIER);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(RECreanceAccordee.TABLE_NAME_CREANCES_ACCORDEES);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RECreanceAccordee.FIELDNAME_ID_CREANCIER);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(RECreancier.TABLE_NAME_CREANCIER);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RECreancier.FIELDNAME_ID_CREANCIER);
        }

        return fromClauseBuffer.toString();
    }

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

        if (!JadeStringUtil.isIntegerEmpty(forIdCreancier)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreanceAccordee.FIELDNAME_ID_CREANCIER + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdCreancier);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRenteAccordee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreanceAccordee.FIELDNAME_ID_RENTE_ACCORDEE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdRenteAccordee);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdOrdreVersement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreanceAccordee.FIELDNAME_ID_ORDRE_VERSEMENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdOrdreVersement);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsTypeCreancier)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreancier.FIELDNAME_CS_TYPE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsTypeCreancier);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsTypeCreancierNotEqual)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += RECreancier.FIELDNAME_CS_TYPE + "<>"
                    + _dbWriteNumeric(statement.getTransaction(), forCsTypeCreancierNotEqual);
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
        return new RECreanceAccordee();
    }

    /**
     * @return
     */
    public String getForCsTypeCreancier() {
        return forCsTypeCreancier;
    }

    /**
     * @return
     */
    public String getForCsTypeCreancierNotEqual() {
        return forCsTypeCreancierNotEqual;
    }

    /**
     * @return
     */
    public String getForIdCreancier() {
        return forIdCreancier;
    }

    /**
     * @return
     */
    public String getForIdOrdreVersement() {
        return forIdOrdreVersement;
    }

    /**
     * @return
     */
    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return RECreanceAccordee.FIELDNAME_ID_CREANCE_ACCORDEE;
    }

    /**
     * @param string
     */
    public void setForCsTypeCreancier(String string) {
        forCsTypeCreancier = string;
    }

    /**
     * @param string
     */
    public void setForCsTypeCreancierNotEqual(String string) {
        forCsTypeCreancierNotEqual = string;
    }

    /**
     * @param string
     */
    public void setForIdCreancier(String string) {
        forIdCreancier = string;
    }

    /**
     * @param string
     */
    public void setForIdOrdreVersement(String string) {
        forIdOrdreVersement = string;
    }

    /**
     * @param string
     */
    public void setForIdRenteAccordee(String string) {
        forIdRenteAccordee = string;
    }

}
