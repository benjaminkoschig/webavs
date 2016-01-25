/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFAssQdDossierJointDossierJointTiersManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCsTypeRelation = "";

    private String forIdQd = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFAssQdDossierJointDossierJointTiersManager.
     */
    public RFAssQdDossierJointDossierJointTiersManager() {
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
                    RFAssQdDossierJointDossierJointTiers.createFromClause(_getCollection()));
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

        if (!JadeStringUtil.isBlankOrZero(forCsTypeRelation)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeRelation));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdQd)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssQdDossier.FIELDNAME_ID_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdQd));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFQdJointDossierJointTiersJointDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFAssQdDossierJointDossierJointTiers();
    }

    public String getForCsTypeRelation() {
        return forCsTypeRelation;
    }

    public String getForIdQd() {
        return forIdQd;
    }

    public void setForCsTypeRelation(String forCsTypeRelation) {
        this.forCsTypeRelation = forCsTypeRelation;
    }

    public void setForIdQd(String forIdQd) {
        this.forIdQd = forIdQd;
    }

}