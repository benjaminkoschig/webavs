package globaz.cygnus.db.paiement;

import globaz.corvus.db.lots.RELot;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author fha
 */
public class RFLotJointPrestationJointOVManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String[] forEtatsLot = null;
    private String forIdDecision = "";
    private String forIdLot = "";
    private String forLotOwner = "";
    private String forOrderBy = "";
    private transient String fromClause = null;
    private String idGestionnaire = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFLotJointPrestationJointOVManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFLotJointPrestationJointOV.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
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

        if (!JadeStringUtil.isEmpty(forIdLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestation.FIELDNAME_ID_LOT);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdLot));
        }

        if ((null != forEtatsLot) && (forEtatsLot.length > 0)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RELot.FIELDNAME_ETAT);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String id : forEtatsLot) {
                inc++;
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), id));
                if (inc < forEtatsLot.length) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(") ");

        }

        if (!JadeStringUtil.isEmpty(forLotOwner)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RELot.FIELDNAME_LOT_OWNER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forLotOwner));
        }

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestation.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isEmpty(idGestionnaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFLot.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), idGestionnaire));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new RFLotJointPrestationJointOV();
    }

    public String[] getForEtatsLot() {
        return forEtatsLot;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForLotOwner() {
        return forLotOwner;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------
    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForEtatsLot(String[] forEtatsLot) {
        this.forEtatsLot = forEtatsLot;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForLotOwner(String forLotOwner) {
        this.forLotOwner = forLotOwner;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

}
