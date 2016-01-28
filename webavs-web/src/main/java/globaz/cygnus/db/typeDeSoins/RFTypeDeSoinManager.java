/*
 * Créé le 11 janvier 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFTypeDeSoinManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String forCodeTypeDeSoin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFTypeDeSoinManager
     */
    public RFTypeDeSoinManager() {
        super();
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

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forCodeTypeDeSoin)) {

            sqlWhere.append(RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteString(statement.getTransaction(), forCodeTypeDeSoin));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFTypeDeSoin)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFTypeDeSoin();
    }

    public String getForCodeTypeDeSoin() {
        return forCodeTypeDeSoin;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForCodeTypeDeSoin(String forCodeTypeDeSoin) {
        this.forCodeTypeDeSoin = forCodeTypeDeSoin;
    }

    public void setForIdTypeDeSoin(String forCodeTypeDeSoin) {
        this.forCodeTypeDeSoin = forCodeTypeDeSoin;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
