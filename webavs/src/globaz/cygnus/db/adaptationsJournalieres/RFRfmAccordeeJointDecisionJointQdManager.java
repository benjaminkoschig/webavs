/*
 * Créé le 5 avril 2011
 */
package globaz.cygnus.db.adaptationsJournalieres;

import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFRfmAccordeeJointDecisionJointQdManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forIdQdPrincipale = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossierManager.
     */
    public RFRfmAccordeeJointDecisionJointQdManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFRfmAccordeeJointDecisionJointQd.createFromClause(_getCollection()));
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

        if (!JadeStringUtil.isIntegerEmpty(forIdQdPrincipale)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdQdPrincipale));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFRfmAccordeeJointDecisionJointQd)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFRfmAccordeeJointDecisionJointQd();
    }

    public String getForIdQdPrincipale() {
        return forIdQdPrincipale;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForIdQdPrincipale(String forIdQdPrincipale) {
        this.forIdQdPrincipale = forIdQdPrincipale;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
