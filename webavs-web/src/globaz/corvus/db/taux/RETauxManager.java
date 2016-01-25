package globaz.corvus.db.taux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class RETauxManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsType = "";
    private String forDate = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RETaux.TABLE_NAME_TAUX);

        return fromClauseBuffer.toString();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForCsType())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RETaux.FIELDNAME_TYPE_TAUX);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForCsType()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDate())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RETaux.FIELDNAME_DATE_DEBUT);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), getForDate()));
            whereClause.append(" AND ");

            whereClause.append(" ( ");
            whereClause.append(RETaux.FIELDNAME_DATE_FIN);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), getForDate()));
            whereClause.append(" OR ");
            whereClause.append(RETaux.FIELDNAME_DATE_FIN);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(" )");
        }

        return whereClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RETaux();
    }

    /**
     * @return
     */
    public String getForCsType() {
        return forCsType;
    }

    /**
     * @return
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return RETaux.FIELDNAME_DATE_DEBUT;
    }

    /**
     * @param string
     */
    public void setForCsType(String string) {
        forCsType = string;
    }

    /**
     * @param string
     */
    public void setForDate(String string) {
        forDate = string;
    }

}
