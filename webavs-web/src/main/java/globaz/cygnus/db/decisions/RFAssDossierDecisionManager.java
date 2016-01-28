package globaz.cygnus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author fha
 */
public class RFAssDossierDecisionManager extends PRAbstractManager {
    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = "";

    /**
     * Crée une nouvelle instance de la classe LIDossiersJointTiersManager.
     */
    public RFAssDossierDecisionManager() {
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
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFAssDossierDecision.TABLE_NAME + "." + RFAssDossierDecision.FIELDNAME_ID_DECISION
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
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
        return new RFAssDossierDecision();
    }

    public String getForIdDecision() {
        return forIdDecision;
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
        return RFDecision.FIELDNAME_ID_DECISION;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

}
