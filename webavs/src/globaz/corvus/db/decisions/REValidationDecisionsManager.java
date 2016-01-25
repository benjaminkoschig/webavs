package globaz.corvus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class REValidationDecisionsManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = "";
    private String forIdPrestationDue = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement);
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

        if (!JadeStringUtil.isIntegerEmpty(getForIdDecision())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REValidationDecisions.FIELDNAME_ID_DECISION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdDecision()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdPrestationDue())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdPrestationDue()));
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
        return new REValidationDecisions();
    }

    /**
     * @return the forIdDecision
     */
    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * @return the forIdPrestationDue
     */
    public String getForIdPrestationDue() {
        return forIdPrestationDue;
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
        return REValidationDecisions.FIELDNAME_ID_VALIDATION_DECISION;
    }

    /**
     * @param forIdDecision
     *            the forIdDecision to set
     */
    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    /**
     * @param forIdPrestationDue
     *            the forIdPrestationDue to set
     */
    public void setForIdPrestationDue(String forIdPrestationDue) {
        this.forIdPrestationDue = forIdPrestationDue;
    }

}
