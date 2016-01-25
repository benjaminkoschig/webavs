/*
 * Created on Jul 18, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.db.affiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * @author cuva Created on Jul 18, 2006
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class AFAffiliationUnionCotisationManager extends AFAffiliationUnionTierCountByCantonManager {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient String fromClause = null;

    // private String forEntreYear;

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sbFields = new StringBuffer(50);
        sbFields.append(_getCollection() + AFAffiliation.TABLE_NAME + ".");
        sbFields.append(AFAffiliation.FIELDNAME_AFFILIATION_TYPE);
        sbFields.append(", COUNT(*) AS " + AFAffiliationUnionTierCountByCanton.FIELDNAME_NB_AFFILIE);
        return sbFields.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Surcharge
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = AFAffiliationUnionTierCountByCanton.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getGroupBy(BStatement statement) {
        StringBuffer sqlGroupBy = new StringBuffer(30);
        sqlGroupBy.append(_getCollection() + AFAffiliation.TABLE_NAME + ".");
        sqlGroupBy.append(AFAffiliation.FIELDNAME_AFFILIATION_TYPE);

        return sqlGroupBy.toString();
    }

    // /**
    // * Surcharge.
    // *
    // * @param statement
    // * @return
    // *
    // * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
    // **/
    // protected String _getWhere(BStatement statement) {
    //
    // StringBuffer sbWhere = new StringBuffer(50);
    // if(!JadeStringUtil.isIntegerEmpty(getForEntreYear())){
    // int year = Integer.parseInt(getForEntreYear());
    //
    // sbWhere.append("((" +year + "0101 between " + _getCollection() +
    // AFAffiliation.TABLE_NAME);
    // sbWhere.append("." + AFAffiliation.FIELDNAME_AFF_DDEBUT + " and " +
    // _getCollection() + AFAffiliation.TABLE_NAME);
    // sbWhere.append("." + AFAffiliation.FIELDNAME_AFF_DFIN + ")");
    // sbWhere.append(" or (" + _getCollection() + AFAffiliation.TABLE_NAME +
    // "." + AFAffiliation.FIELDNAME_AFF_DFIN + "=0");
    // sbWhere.append(" and " + _getCollection() + AFAffiliation.TABLE_NAME +
    // "." + AFAffiliation.FIELDNAME_AFF_DFIN + "<=" + year + "0101))");
    // sbWhere.append(" and ");
    // sbWhere.append("((" +year + "0101 between " + _getCollection() +
    // AFCotisation.TABLE_NAME);
    // sbWhere.append("." + AFCotisation.FIELDNAME_DATE_DEB + " and " +
    // _getCollection() + AFCotisation.TABLE_NAME);
    // sbWhere.append("." + AFCotisation.FIELDNAME_DATE_FIN + ")");
    // sbWhere.append(" or (" + _getCollection() + AFCotisation.TABLE_NAME + "."
    // + AFCotisation.FIELDNAME_DATE_FIN + "=0");
    // sbWhere.append(" and " + _getCollection() + AFCotisation.TABLE_NAME + "."
    // + AFCotisation.FIELDNAME_DATE_FIN + ">=" + year + "1231))");
    // }
    // sbWhere.append(" and " + _getCollection() + AFAffiliation.TABLE_NAME +
    // "." + AFAffiliation.FIELDNAME_AFFILIATION_TYPE + "<>" +
    // CodeSystem.AVEC_BENIFICIARE);
    // return sbWhere.toString();
    // }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getOrder(BStatement statement) {
        return AFAffiliation.FIELDNAME_AFFILIATION_TYPE;
    }

    /**
     * Surcharge Renvoie la requête SQL à exécuter. <i>
     * <p>
     * Construit un requête SELECT * FROM ... WHERE ... ORDER BY ... </i>
     * 
     * @return la requête SQL
     */
    @Override
    protected java.lang.String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer("SELECT ");
            String sqlFields = _getFields(statement);
            if (!JadeStringUtil.isEmpty(sqlFields)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(" FROM ");
            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if (!JadeStringUtil.isEmpty(sqlWhere)) {
                sqlBuffer.append(" WHERE ");
                sqlBuffer.append(sqlWhere);
            }
            String sqlGroupeBy = _getGroupBy(statement);
            if (!JadeStringUtil.isEmpty(sqlGroupeBy)) {
                sqlBuffer.append(" GROUP BY ");
                sqlBuffer.append(sqlGroupeBy);
            }
            String sqlOrder = _getOrder(statement);
            if (!JadeStringUtil.isEmpty(sqlOrder)) {
                sqlBuffer.append(" ORDER BY ");
                sqlBuffer.append(sqlOrder);
            }
            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * Surcharge.
     * 
     * @return
     * @throws java.lang.Exception
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     **/
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliationUnionCotisation();
    }

    // /**
    // * @return
    // */
    // public String getForEntreYear() {
    // return forEntreYear;
    // }
    //
    // /**
    // * @param string
    // */
    // public void setForEntreYear(String string) {
    // forEntreYear = string;
    // }

}
