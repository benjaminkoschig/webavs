/*
 * Créé le 11 janvier 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author jje
 */
public class RFSousTypeDeSoinManager extends PRAbstractManager {

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
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String forCodeSousTypeDeSoin = "";
    private String forIdTypeSoin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFSousTypeDeSoinManager
     */
    public RFSousTypeDeSoinManager() {
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

        if (!JadeStringUtil.isIntegerEmpty(forCodeSousTypeDeSoin)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteString(statement.getTransaction(), forCodeSousTypeDeSoin));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTypeSoin)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdTypeSoin));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFSousTypeDeSoin)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFSousTypeDeSoin();
    }

    public String getForCodeSousTypeDeSoin() {
        return forCodeSousTypeDeSoin;
    }

    public String getForIdTypeSoin() {
        return forIdTypeSoin;
    }

    public String getFromClause() {
        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN;
    }

    public void setForCodeSousTypeDeSoin(String forCodeSousTypeDeSoin) {
        this.forCodeSousTypeDeSoin = forCodeSousTypeDeSoin;
    }

    public void setForIdTypeSoin(String forIdTypeSoin) {
        this.forIdTypeSoin = forIdTypeSoin;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}