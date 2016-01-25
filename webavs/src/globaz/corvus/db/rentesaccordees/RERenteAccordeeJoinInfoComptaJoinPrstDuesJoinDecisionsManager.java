/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author scr
 * 
 */

public class RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager extends
        RERenteAccordeeJoinInfoComptaJoinPrstDuesManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = "";

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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions.createFromClause(_getCollection());

    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isBlank(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REDecisionEntity.FIELDNAME_ID_DECISION + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDecision);
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
        return new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions();
    }

    /**
     * @return the forIdDecision
     */
    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * @param forIdDecision
     *            the forIdDecision to set
     */
    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

}
