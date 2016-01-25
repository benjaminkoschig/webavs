/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.ordresversements;

import globaz.cygnus.db.paiement.RFPrestation;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * 
 * @author fha
 */
public class RFPrestationJointOrdreVersementManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPrestation = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFPrestationJointOrdreVersementManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFPrestationJointOrdreVersement.createFromClause(_getCollection()));

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

        // on prend que le requerant
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }

        if (!JadeStringUtil.isEmpty(forIdPrestation)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestation.FIELDNAME_ID_PRESTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdPrestation));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new RFPrestationJointOrdreVersement();
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public String getFromClause() {
        return fromClause;
    }

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

}
