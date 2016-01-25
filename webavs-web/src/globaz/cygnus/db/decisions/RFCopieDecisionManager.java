package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author fha
 */
public class RFCopieDecisionManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFCopieDecision.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String forIdDecision = "";
    private String forIdTiers = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    /**
     * Crée une nouvelle instance de la classe LIDossiersJointTiersManager.
     */
    public RFCopieDecisionManager() {
        super();
        wantCallMethodBeforeFind(false);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isEmpty(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFCopieDecision.FIELDNAME_ID_TIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFCopieDecision.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        return sqlWhere.toString();
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
        return new RFCopieDecision();
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getFromClause() {
        return fromClause;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return RFCopieDecision.FIELDNAME_ID_COPIE;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
