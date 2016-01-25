/*
 * Créé le 5 janvier 2011
 */
package globaz.cygnus.db.decisions;

import globaz.cygnus.db.demandes.RFDemande;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * 
 * @author jje
 */
public class RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefusManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient String forCsEtatDecision = "";
    private transient String forIdExecutionProcessAvasadDecision = "";
    private transient String forIdGestionnaire = "";
    private transient boolean forIsNotDecisionAvasad = false;
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefusManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();

        sqlOrder.append(RFDecision.FIELDNAME_ID_DECISION);
        sqlOrder.append(",");
        sqlOrder.append(RFDemande.FIELDNAME_ID_DEMANDE);

        return sqlOrder.toString();
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

        if (!JadeStringUtil.isEmpty(forIdGestionnaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaire));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDecision.TABLE_NAME + "." + RFDecision.FIELDNAME_ETAT_DECISION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDecision));
        }

        if (forIsNotDecisionAvasad) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" (");
            sqlWhere.append(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS);
            sqlWhere.append(" IS NULL ");
            sqlWhere.append(" OR ");
            sqlWhere.append(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS);
            sqlWhere.append(" = 0");
            sqlWhere.append(") ");
        } else {
            if (!JadeStringUtil.isBlankOrZero(forIdExecutionProcessAvasadDecision)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(RFDecision.FIELDNAME_ID_EXECUTION_PROCESS);
                sqlWhere.append(" = ");
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdExecutionProcessAvasadDecision));
            }
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDecisionJointDemandeJointAssMotifRefusJointMotifDeRefus();
    }

    public String getForCsEtatDecision() {
        return forCsEtatDecision;
    }

    public String getForIdExecutionProcessAvasadDecision() {
        return forIdExecutionProcessAvasadDecision;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
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

    public boolean isForIsNotDecisionAvasad() {
        return forIsNotDecisionAvasad;
    }

    public void setForCsEtatDecision(String forCsEtatDecision) {
        this.forCsEtatDecision = forCsEtatDecision;
    }

    public void setForIdExecutionProcessAvasadDecision(String forIdExecutionProcessAvasadDecision) {
        this.forIdExecutionProcessAvasadDecision = forIdExecutionProcessAvasadDecision;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setForIsNotDecisionAvasad(boolean forIsNotDecisionAvasad) {
        this.forIsNotDecisionAvasad = forIsNotDecisionAvasad;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
