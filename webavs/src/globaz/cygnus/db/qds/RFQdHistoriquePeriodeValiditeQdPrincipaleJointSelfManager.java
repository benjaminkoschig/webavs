/*
 * Créé le 20 janvier 2009
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.sql.PRWhereStringBuffer;

/**
 * @author jje
 */
public class RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfManager extends BManager {

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
     * Crée une nouvelle instance de la classe RFQdHistoriqueAugmentationJointSelfManager.
     */
    public RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer order = new StringBuffer("n." + RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        order.append(" DESC");
        return order.toString();
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        PRWhereStringBuffer sqlWhere = new PRWhereStringBuffer(_getCollection());

        sqlWhere.newClause().append("n." + RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE).appendEgal()
                .append(getForIdQd());

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFQdHistoriqueAugmentationJointSelf)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelf();
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
