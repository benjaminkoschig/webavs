/*
 * Créé le 9 janv. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.globall.db.BStatement;

/**
 * @author scr
 * 
 * 
 * 
 */
public class RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions extends RERenteAccordeeJoinInfoComptaJoinPrstDues {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer(
                RERenteAccordeeJoinInfoComptaJoinPrstDues.createFromClause(schema));
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        // jointure entre table des prestations dues et table validation de
        // décision
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE);

        // jointure entre table des validation de décisions et table des
        // décision
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REValidationDecisions.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDecisionEntity.FIELDNAME_ID_DECISION);

        return fromClauseBuffer.toString();
    }

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idDecision = "";
    private String csTypeDecision = "";
    private String dateDecision = "";

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisions.createFromClause(_getCollection());
        }

        return fromClause;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idDecision = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_ID_DECISION);
        csTypeDecision = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_TYPE_DECISION);
        dateDecision = statement.dbReadDateAMJ(REDecisionEntity.FIELDNAME_DATE_DECISION);
    }

    /**
     * @return the fromClause
     */
    @Override
    public String getFromClause() {
        return fromClause;
    }

    /**
     * @return the idDecision
     */
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @param fromClause
     *            the fromClause to set
     */
    @Override
    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }
    public String getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }
}
