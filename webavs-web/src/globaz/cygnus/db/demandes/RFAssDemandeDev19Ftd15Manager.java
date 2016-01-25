package globaz.cygnus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * author jje
 */
public class RFAssDemandeDev19Ftd15Manager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande15 = "";
    private String forIdDemande19 = "";

    private transient String fromClause = null;

    public RFAssDemandeDev19Ftd15Manager() {
        super();
        wantCallMethodBeforeFind(false);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFAssDemandeDev19Ftd15.createFromClause(_getCollection()));
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
        // String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(forIdDemande15)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_FTD15);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdDemande15));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDemande19)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssDemandeDev19Ftd15.FIELDNAME_ID_DEMANDE_DEV19);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdDemande19));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFAssDemandeDev19Ftd15();
    }

    public String getForIdDemande15() {
        return forIdDemande15;
    }

    public String getForIdDemande19() {
        return forIdDemande19;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForIdDemande15(String forIdDemande15) {
        this.forIdDemande15 = forIdDemande15;
    }

    public void setForIdDemande19(String forIdDemande19) {
        this.forIdDemande19 = forIdDemande19;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}