/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * 
 * @author fha
 */
public class RFDecisionJointCopieManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String forIdDecision = "";

    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFDecisionJointCopieManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFDecisionJointCopie.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new RFDecisionJointCopie();
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getFromClause() {
        return fromClause;
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------
    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
