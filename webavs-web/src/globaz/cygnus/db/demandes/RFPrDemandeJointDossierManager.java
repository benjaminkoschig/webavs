/*
 * Créé le 12 février 2010
 */
package globaz.cygnus.db.demandes;

import globaz.cygnus.db.dossiers.RFDossier;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author jje
 */
public class RFPrDemandeJointDossierManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CLE_DROITS_TOUS = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forEtatDemande = "";
    private String forEtatDossier = "";
    private String forIdDossier = "";
    private String forIdTiers = "";
    private String forTypeDemande = "";
    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFPrDemandeJointDossierManager.
     */
    public RFPrDemandeJointDossierManager() {
        super();
        wantCallMethodBeforeFind(false);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFPrDemandeJointDossier.createFromClause(_getCollection()));
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
        String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDossier)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDossier.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDossier));
        }

        if (!JadeStringUtil.isBlank(forTypeDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema);
            sqlWhere.append(PRDemande.TABLE_NAME);
            sqlWhere.append(".");
            sqlWhere.append(PRDemande.FIELDNAME_TYPE_DEMANDE);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forTypeDemande));
        }

        if (!JadeStringUtil.isBlank(forEtatDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema);
            sqlWhere.append(PRDemande.TABLE_NAME);
            sqlWhere.append(".");
            sqlWhere.append(PRDemande.FIELDNAME_ETAT);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forEtatDemande));
        }

        if (!JadeStringUtil.isBlank(forEtatDossier)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema);
            sqlWhere.append(RFDossier.TABLE_NAME);
            sqlWhere.append(".");
            sqlWhere.append(RFDossier.FIELDNAME_CS_ETAT_DOSSIER);
            sqlWhere.append("=");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forEtatDossier));
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFPrDemandeJointDossier)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFPrDemandeJointDossier();
    }

    public String getForEtatDemande() {
        return forEtatDemande;
    }

    public String getForEtatDossier() {
        return forEtatDossier;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForTypeDemande() {
        return forTypeDemande;
    }

    public void setForEtatDemande(String forEtatDemande) {
        this.forEtatDemande = forEtatDemande;
    }

    public void setForEtatDossier(String forEtatDossier) {
        this.forEtatDossier = forEtatDossier;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForTypeDemande(String forTypeDemande) {
        this.forTypeDemande = forTypeDemande;
    }

}
