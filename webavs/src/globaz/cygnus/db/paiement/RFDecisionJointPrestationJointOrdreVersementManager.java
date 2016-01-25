package globaz.cygnus.db.paiement;

import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.api.ordresversements.IRFOrdresVersements;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author mbo
 */
public class RFDecisionJointPrestationJointOrdreVersementManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean forDettesDecisions = Boolean.FALSE;
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFDecisionJointPrestationJointOrdreVersementManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append(RFDecision.FIELDNAME_ID_DECISION).append(",");
        fields.append(RFOrdresVersements.FIELDNAME_MONTANT);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFDecisionJointPrestationJointOrdreVersement.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();

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
        // String schema = this._getCollection();

        // clause pour les décisions avec avances
        if (forDettesDecisions) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            // décisions en état non-validées
            sqlWhere.append(RFDecision.FIELDNAME_ETAT_DECISION);
            sqlWhere.append("=");
            sqlWhere.append(IRFDecisions.NON_VALIDE);

            sqlWhere.append(" AND ");

            // ordres de versement de type dette
            sqlWhere.append(RFOrdresVersements.FIELDNAME_TYPE_VERSEMENT);
            sqlWhere.append("=");
            sqlWhere.append(IRFOrdresVersements.CS_TYPE_DETTE);

            sqlWhere.append(" AND ");

            // ordres de versements compensé
            sqlWhere.append(RFOrdresVersements.FIELDNAME_IS_COMPENSE);
            sqlWhere.append("=");
            sqlWhere.append("'1'");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new RFDecisionJointPrestationJointOrdreVersement();
    }

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isForDettesDecisions() {
        return forDettesDecisions;
    }

    public void setForDettesDecisions(boolean forDettesDecisions) {
        this.forDettesDecisions = forDettesDecisions;
    }
}
