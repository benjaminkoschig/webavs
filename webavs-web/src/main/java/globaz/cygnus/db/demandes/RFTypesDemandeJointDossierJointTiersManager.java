/*
 * Créé le 7 janvier 2009
 */
package globaz.cygnus.db.demandes;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Set;

/**
 * @author jje
 */
public class RFTypesDemandeJointDossierJointTiersManager extends BManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_TIERS = "TITIERP";

    private String alias_date_demande = "DATE_DEMANDE";
    private Set<String> cssSousTypeDeSoinToIgnore = null;
    private String forCsEtatDemande = "";
    private String forCsStatutDemande = "";
    private String forIdDecision = "";
    private String forIdExecutionProcess = "";
    private String forIdGestionnaire = "";

    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeJointDossierJointTiersManager
     */
    public RFTypesDemandeJointDossierJointTiersManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        fields.append(RFDemande.FIELDNAME_ID_DEMANDE + ",");
        fields.append(RFDemande.FIELDNAME_ID_DEMANDE_PARENT + ",");
        fields.append(RFDemande.FIELDNAME_DATE_RECEPTION + ",");
        fields.append(RFDemande.FIELDNAME_DATE_FACTURE + ",");
        fields.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT + ",");
        fields.append(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT + ",");
        fields.append(RFDemande.FIELDNAME_DATE_IMPUTATION + ",");
        fields.append(RFDemande.FIELDNAME_NUMERO_FACTURE + ",");
        fields.append(RFDemande.FIELDNAME_MONTANT_FACTURE + ",");
        fields.append(RFDemande.FIELDNAME_MONTANT_A_PAYER + ",");
        fields.append(RFDemande.FIELDNAME_MONTANT_MENSUEL + ",");
        fields.append(RFDemande.FIELDNAME_IS_FORCER_PAIEMENT + ",");
        fields.append(RFDemande.FIELDNAME_IS_PP + ",");
        fields.append(RFDemande.FIELDNAME_IS_TEXTE_REDIRECTION + ",");
        fields.append(RFDemande.FIELDNAME_CS_ETAT + ",");
        fields.append(RFDemande.FIELDNAME_CS_SOURCE + ",");
        fields.append(RFDemande.FIELDNAME_CS_STATUT + ",");
        fields.append(RFDemande.FIELDNAME_ID_FOURNISSEUR + ",");
        fields.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE + ",");
        fields.append(RFDemande.FIELDNAME_ID_DOSSIER + ",");
        fields.append(RFDemande.FIELDNAME_ID_ADRESSE_PAIEMENT + ",");
        fields.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN + ",");
        fields.append(RFDemande.FIELDNAME_ID_QD_PRINCIPALE + ",");
        fields.append(RFDemande.FIELDNAME_ID_QD_ASSURE + ",");
        fields.append(RFDemande.FIELDNAME_ID_DECISION + ",");
        fields.append(RFDemande.FIELDNAME_ID_EXECUTION_PROCESS + ",");
        fields.append(RFDemande.FIELDNAME_IS_CONTRAT_TRAVAIL + ",");
        fields.append(RFDemande.FIELDNAME_IS_RETRO + ",");
        fields.append(RFTypesDemandeJointDossierJointTiers.FIELDNAME_NUM_AVS + ",");
        fields.append(RFTypesDemandeJointDossierJointTiers.FIELDNAME_DATENAISSANCE + ",");
        fields.append(RFTypesDemandeJointDossierJointTiers.FIELDNAME_DATEDECES + ",");
        fields.append(RFTypesDemandeJointDossierJointTiers.FIELDNAME_SEXE + ",");
        fields.append(RFTypesDemandeJointDossierJointTiers.FIELDNAME_NOM + ",");
        fields.append(RFTypesDemandeJointDossierJointTiers.FIELDNAME_PRENOM + ",");
        fields.append(RFTypesDemandeJointDossierJointTiers.FIELDNAME_VISA_GESTIONNAIRE + ",");
        fields.append(RFSousTypeDeSoin.FIELDNAME_CODE + ",");
        fields.append(RFTypeDeSoin.FIELDNAME_CODE + ",");
        fields.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN + ",");
        fields.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN + ",");
        fields.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT + ",");
        fields.append(RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT + ",");
        fields.append(RFDemandeFrq17Fra18.FIELDNAME_DATE_DECOMPTE + ",");
        fields.append(RFDemandeMoy5_6_7.FIELDNAME_DATE_DECISION_OAI + ",");
        fields.append(RFDemandeDev19.FIELDNAME_DATE_ENVOI_MDC + ",");
        fields.append(RFDemandeDev19.FIELDNAME_DATE_RECEPTION_PREAVIS + ",");
        fields.append(RFDemandeDev19.FIELDNAME_DATE_ENVOI_ACCEPTATION + ",");
        fields.append(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_VERSE_OAI + ",");
        fields.append(RFDemandeMoy5_6_7.FIELDNAME_MONTANT_FACTURE_44 + ",");
        fields.append(_getCollection());
        fields.append(RFTypesDemandeJointDossierJointTiersManager.TABLE_TIERS);
        fields.append(".");
        fields.append(RFTypesDemandeJointDossierJointTiers.FIELDNAME_ID_TIERS_TI + ",");

        fields.append("CASE ");
        fields.append("WHEN ");
        fields.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        fields.append(" IS NULL OR ");
        fields.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        fields.append(" = 0 ");
        fields.append(" THEN ");
        fields.append(RFDemande.FIELDNAME_DATE_FACTURE);
        fields.append(" ELSE ");
        fields.append(RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT);
        fields.append(" END AS ");
        fields.append(alias_date_demande);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFTypesDemandeJointDossierJointTiers.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return alias_date_demande;
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

        if (!JadeStringUtil.isEmpty(forCsEtatDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDemande.TABLE_NAME + "." + RFDemande.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDemande));
        }

        if (!JadeStringUtil.isEmpty(forCsStatutDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDemande.TABLE_NAME + "." + RFDemande.FIELDNAME_CS_STATUT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsStatutDemande));
        }

        if ((cssSousTypeDeSoinToIgnore != null) && (cssSousTypeDeSoinToIgnore.size() > 0)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
            sqlWhere.append(" NOT IN ( ");

            int j = 0;
            for (String csStr : cssSousTypeDeSoinToIgnore) {
                j++;
                if (cssSousTypeDeSoinToIgnore.size() == j) {
                    sqlWhere.append(csStr);
                } else {
                    sqlWhere.append(csStr + ",");
                }
            }
            sqlWhere.append(" ) ");
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdGestionnaire)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaire));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdExecutionProcess)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_EXECUTION_PROCESS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdExecutionProcess));
        } else {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" (");
            sqlWhere.append(RFDemande.FIELDNAME_ID_EXECUTION_PROCESS);
            sqlWhere.append(" IS NULL ");
            sqlWhere.append(" OR ");
            sqlWhere.append(RFDemande.FIELDNAME_ID_EXECUTION_PROCESS);
            sqlWhere.append(" = 0");
            sqlWhere.append(") ");
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFTypesDemandeJointDossierJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFTypesDemandeJointDossierJointTiers();
    }

    public Set<String> getCssSousTypeDeSoinToIgnore() {
        return cssSousTypeDeSoinToIgnore;
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsStatutDemande() {
        return forCsStatutDemande;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdExecutionProcess() {
        return forIdExecutionProcess;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public void setCssSousTypeDeSoinToIgnore(Set<String> cssSousTypeDeSoinToIgnore) {
        this.cssSousTypeDeSoinToIgnore = cssSousTypeDeSoinToIgnore;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsStatutDemande(String forCsStatutDemande) {
        this.forCsStatutDemande = forCsStatutDemande;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdExecutionProcess(String forIdExecutionProcess) {
        this.forIdExecutionProcess = forIdExecutionProcess;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

}
