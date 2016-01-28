/*
 * Cr�� le 11 janvier 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFTypeDeSoinJointSousTypeDeSoinManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCodeSousTypeDeSoin = "";

    private String forCodeTypeDeSoin = "";
    private String forCsSousTypeDeSoin = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe LIDossiersJointTiersManager.
     */
    public RFTypeDeSoinJointSousTypeDeSoinManager() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFTypeDeSoinJointSousTypeDeSoin.createFromClause(_getCollection()));
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

        if (!JadeStringUtil.isIntegerEmpty(forCodeTypeDeSoin)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCodeTypeDeSoin));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCodeSousTypeDeSoin)) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forCodeSousTypeDeSoin));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsSousTypeDeSoin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsSousTypeDeSoin));
        }

        return sqlWhere.toString();
    }

    /**
     * D�finition de l'entit� (LIDossiersJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFTypeDeSoinJointSousTypeDeSoin();
    }

    public String getForCodeSousTypeDeSoin() {
        return forCodeSousTypeDeSoin;
    }

    public String getForCodeTypeDeSoin() {
        return forCodeTypeDeSoin;
    }

    public String getForCsSousTypeDeSoin() {
        return forCsSousTypeDeSoin;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForCodeSousTypeDeSoin(String forCodeSousTypeDeSoin) {
        this.forCodeSousTypeDeSoin = forCodeSousTypeDeSoin;
    }

    public void setForCodeTypeDeSoin(String forCodeTypeDeSoin) {
        this.forCodeTypeDeSoin = forCodeTypeDeSoin;
    }

    public void setForCsSousTypeDeSoin(String forCsSousTypeDeSoin) {
        this.forCsSousTypeDeSoin = forCsSousTypeDeSoin;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
