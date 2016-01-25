/*
 * Créé le 7 janvier 2009
 */
package globaz.cygnus.db.demandes;

import globaz.cygnus.api.demandes.IRFDemande;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author FHA
 */
public class RFCorrectionDemandeManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_DROITS_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forCsEtat = "";
    private String forIdDemande = "";
    private String forIdParentDemande = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeManager.
     */
    public RFCorrectionDemandeManager() {
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
            StringBuffer from = new StringBuffer();

            from.append(_getCollection());
            from.append(RFDemande.TABLE_NAME);

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

        if (!JadeStringUtil.isIntegerEmpty(forIdDemande)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE_PARENT);
            sqlWhere.append(" = ");
            sqlWhere.append(forIdDemande);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsEtat)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_CS_ETAT);
            sqlWhere.append(" = ");
            sqlWhere.append(IRFDemande.PAYE);

            sqlWhere.append(" OR ");

            sqlWhere.append(RFDemande.FIELDNAME_CS_ETAT);
            sqlWhere.append(" = ");
            sqlWhere.append(IRFDemande.ANNULE);
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFDemande)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFCorrectionDemande();
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdParentDemande() {
        return forIdParentDemande;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdParentDemande(String forIdParentDemande) {
        this.forIdParentDemande = forIdParentDemande;
    }

}
