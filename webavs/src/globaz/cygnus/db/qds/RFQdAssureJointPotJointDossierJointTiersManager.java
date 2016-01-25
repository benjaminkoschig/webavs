/*
 * Cr�� le 20 janvier 2009
 */
package globaz.cygnus.db.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.typeDeSoins.RFPotAssure;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFQdAssureJointPotJointDossierJointTiersManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_DROITS_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forIdPotAssure = "";

    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe RFQdPrincipaleJointDossierManager
     */
    public RFQdAssureJointPotJointDossierJointTiersManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFQdAssureJointPotJointDossierJointTiers.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Red�finition de la m�thode _getWhere du parent afin de g�n�rer le WHERE de la requ�te en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        // String schema = _getCollection();

        sqlWhere.append(RFQd.FIELDNAME_CS_ETAT_QD);
        sqlWhere.append(" <> ");
        sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IRFQd.CS_ETAT_QD_CLOTURE));

        if (!JadeStringUtil.isBlankOrZero(forIdPotAssure)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPotAssure.FIELDNAME_ID_POT_ASSURE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdPotAssure));
        }

        return sqlWhere.toString();
    }

    /**
     * D�finition de l'entit� (RFQdPrincipaleJointDossier)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFQdAssureJointPotJointDossierJointTiers();
    }

    public String getForIdPotAssure() {
        return forIdPotAssure;
    }

    public void setForIdPotAssure(String forIdPotAssure) {
        this.forIdPotAssure = forIdPotAssure;
    }

}
