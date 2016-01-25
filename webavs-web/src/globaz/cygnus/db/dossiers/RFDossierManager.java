/*
 * Cr�� le 11 d�cembre 2009
 */
package globaz.cygnus.db.dossiers;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFDossierManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * G�n�ration de la clause from pour la requ�te
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String forIdPrDem = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe RFDossierManager.
     */
    public RFDossierManager() {
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
     * Red�finition de la m�thode _getWhere du parent afin de g�n�rer le WHERE de la requ�te en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdPrDem)) {

            sqlWhere.append(RFDossier.FIELDNAME_ID_PRDEM);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdPrDem));
        }

        return sqlWhere.toString();
    }

    /**
     * D�finition de l'entit� (RFDossierJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDossierJointDecisionJointTiers();
    }

    public String getForIdPrDem() {
        return forIdPrDem;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForIdPrDem(String forIdPrDem) {
        this.forIdPrDem = forIdPrDem;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
