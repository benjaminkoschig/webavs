package globaz.cygnus.db.decisions;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * 
 * @author dma
 */
public class RFDecisionJointTiersSummary extends RFDecision {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_PRESTATION = "FOIPRE";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_MONTANT_PRESTATION = "FOMTOT";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    public static final String TABLE_AVS = "TIPAVSP";

    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";

    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);

        /************* Faire aussi la jointure sur le gestionnaire *******/

        // jointure entre la table des décisions et la table association
        // dossiers-décisions
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssDossierDecision.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFQdPrincipale.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecision.FIELDNAME_ID_QD_PRINICIPALE);

        // jointure entre la table association dossiers-décisions et la table
        // dossiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_DOSSIER);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDossierDecision.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssDossierDecision.FIELDNAME_ID_DOSSIER);

        // jointure entre la table des dossiers et la table des demandes
        // prestation
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_PRDEM);

        // jointure entre la table des demandes et la table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointTiersSummary.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointTiersSummary.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointTiersSummary.FIELDNAME_ID_TIERS_TI);

        // jointure entre la table des numeros AVS et la table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointTiersSummary.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointTiersSummary.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointTiersSummary.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDecisionJointTiersSummary.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDecisionJointTiersSummary.FIELDNAME_ID_TIERS_TI);
        /*
         * fromClauseBuffer.append(leftJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE); fromClauseBuffer.append(on);
         * fromClauseBuffer.append(schema); fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
         * fromClauseBuffer.append(point); fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_DECISION);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFDecision.TABLE_NAME); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFDecision.FIELDNAME_ID_DECISION);
         * 
         * fromClauseBuffer.append(leftJoin); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
         * fromClauseBuffer.append(on); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
         * fromClauseBuffer.append(point);
         * fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
         * fromClauseBuffer.append(egal); fromClauseBuffer.append(schema);
         * fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE); fromClauseBuffer.append(point);
         * fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);
         */
        return fromClauseBuffer.toString();
    }

    private String dateDebut;
    private String dateFin;
    private transient String fromClause = null;
    private String idDossier = "";
    private String idPrestation = "";
    private String idTiers = "";
    private String montant = "";
    private String nss = "";

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFDecisionJointTiersSummary.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        nss = NSUtil.formatAVSUnknown(statement.dbReadString(RFDecisionJointTiersSummary.FIELDNAME_NUM_AVS));
        idTiers = statement.dbReadString(RFDecisionJointTiersSummary.FIELDNAME_ID_TIERS_TI);
        idPrestation = statement.dbReadNumeric(RFDecisionJointTiersSummary.FIELDNAME_ID_PRESTATION);
        idDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_ID_DOSSIER);
        montant = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        dateDebut = statement.dbReadDateAMJ(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
        dateFin = statement.dbReadDateAMJ(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
    }

    public String getDateDebut() {
        return dateDebut;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDateFin() {
        return dateFin;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    public String getNss() {
        return nss;
    }

}
