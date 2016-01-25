/*
 * Créé le 5 avril 2011
 */
package globaz.cygnus.db.adaptationsJournalieres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFAdaptationJournaliereManager extends BManager {

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
        fromClauseBuffer.append(RFAdaptationJournaliere.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String[] forEtatsAdaptation = null;
    private String forIdDecision = "";
    private String forNumeroDecision = "";
    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossierManager.
     */
    public RFAdaptationJournaliereManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(RFAdaptationJournaliereManager.createFromClause(_getCollection()));
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

        if ((null != forEtatsAdaptation) && (forEtatsAdaptation.length > 0)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFAdaptationJournaliere.FIELDNAME_CS_ETAT);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String etat : forEtatsAdaptation) {
                inc++;
                sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), etat));
                if (inc < forEtatsAdaptation.length) {
                    sqlWhere.append(",");
                }
            }
            sqlWhere.append(") ");

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAdaptationJournaliere.FIELDNAME_ID_DECISION_PC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isBlankOrZero(forNumeroDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAdaptationJournaliere.FIELDNAME_NUMERO_DECISION_PC);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forNumeroDecision));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFDossierJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFAdaptationJournaliere();
    }

    public String[] getForEtatsAdaptation() {
        return forEtatsAdaptation;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForEtatsAdaptation(String[] forEtatsAdaptation) {
        this.forEtatsAdaptation = forEtatsAdaptation;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
