/*
 * Cr�� le 20 janvier 2009
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.sql.PRWhereStringBuffer;

/**
 * @author jje
 */
public class RFQdHistoriqueSoldeChargeJointSelfManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // private transient String fromClause = null;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdQd = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe RFQdHistoriqueSoldeChargeJointSelfManager.
     */
    public RFQdHistoriqueSoldeChargeJointSelfManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Red�finition de la m�thode _getOrder du parent afin de changer le tri de la requ�te
     * 
     * @param statement
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer order = new StringBuffer("n." + RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE);
        order.append(" DESC");
        return order.toString();
    }

    /**
     * Red�finition de la m�thode _getWhere du parent afin de g�n�rer le WHERE de la requ�te en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        PRWhereStringBuffer sqlWhere = new PRWhereStringBuffer(_getCollection());

        sqlWhere.newClause().append("n." + RFQdSoldeCharge.FIELDNAME_ID_QD).appendEgal().append(getForIdQd());

        return sqlWhere.toString();
    }

    /**
     * D�finition de l'entit� (RFQdHistoriqueSoldeChargeJointSelf)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdHistoriqueSoldeChargeJointSelf();
    }

    /**
     * @return the forIdQd
     */
    public String getForIdQd() {
        return forIdQd;
    }

    /**
     * @param forIdQd
     *            the forIdQd to set
     */
    public void setForIdQd(String forIdQd) {
        this.forIdQd = forIdQd;
    }

}
