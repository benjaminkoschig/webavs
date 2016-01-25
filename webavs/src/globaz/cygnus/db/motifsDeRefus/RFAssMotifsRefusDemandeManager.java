/*
 * Créé le 26 janvier 2010
 */
package globaz.cygnus.db.motifsDeRefus;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author jje
 */
public class RFAssMotifsRefusDemandeManager extends BManager {

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
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private transient String forIdDemande = "";
    private transient String forIdMotifDeRefus = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFAssMotifsRefusDemandeManager.
     */
    public RFAssMotifsRefusDemandeManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFAssMotifsRefusDemandeManager.createFromClause(_getCollection()));
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

        if (!JadeStringUtil.isIntegerEmpty(forIdDemande)) {

            sqlWhere.append(RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDemande));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdMotifDeRefus)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdMotifDeRefus));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFAssMotifsRefusDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFAssMotifsRefusDemande();
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdMotifDeRefus() {
        return forIdMotifDeRefus;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdMotifDeRefus(String forIdMotifDeRefus) {
        this.forIdMotifDeRefus = forIdMotifDeRefus;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
