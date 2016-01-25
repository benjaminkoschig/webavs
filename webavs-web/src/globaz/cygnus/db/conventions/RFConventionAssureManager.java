/*
 * Créé le 13 avril 2010
 */
package globaz.cygnus.db.conventions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * author fha
 */
public class RFConventionAssureManager extends PRAbstractManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFConventionAssure.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String forIdConvention = "";

    private String forOrderBy = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFConventionAssure.
     */
    public RFConventionAssureManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(createFromClause(_getCollection()));
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

        // !! date SQL aaaammjj !!
        if (!JadeStringUtil.isIntegerEmpty(forIdConvention)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFConventionAssure.FIELDNAME_ID_CONVENTION);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdConvention));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFConventionAssure)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFConventionAssure();
    }

    public String getForIdConvention() {
        return forIdConvention;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    @Override
    public String getOrderByDefaut() {
        return null;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
