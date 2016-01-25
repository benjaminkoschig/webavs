/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BStatement;

/**
 * 
 * @author fha
 * @revision JJE -> date échéance
 */
public class RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation extends RFPrestationAccordee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_TABLE_RE_ACCORDEE_FILS = "RE_ACCORDEE_FILS";
    public static final String ALIAS_TABLE_RE_ACCORDEE_PARENT = "RE_ACCORDEE_PARENT";
    public static final String ALIAS_TABLE_RF_ACCORDEE_FILS = "RF_ACCORDEE_FILS";
    public static final String ALIAS_TABLE_RF_ACCORDEE_PARENT = "RF_ACCORDEE_PARENT";

    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRESTATION_ID_TIERS_BENEFICIAIRE = "ZTITBE";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema, boolean isDiminutionAndAdaptation) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";
        String as = " as ";

        if (isDiminutionAndAdaptation) {
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
            fromClauseBuffer.append(as);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);

            // jointure (FILS) entre la table des RFPrestations et la table des REPrestations
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
            fromClauseBuffer.append(as);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            fromClauseBuffer.append(on);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_PARENT_ADAPTATION);
            fromClauseBuffer.append(egal);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);

            // jointure (PARENT) entre la table des RFPrestations et la table des REPrestations
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
            fromClauseBuffer.append(as);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_PARENT);
            fromClauseBuffer.append(on);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_PARENT);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
            fromClauseBuffer.append(egal);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);

            // jointure entre la table des REPrestations et la table RFPrestation
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
            fromClauseBuffer.append(as);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_FILS);
            fromClauseBuffer.append(on);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_FILS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
            fromClauseBuffer.append(egal);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);

            // jointure entre la table des TITiers et la table des REPrestations
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_TIERS);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_TIERS);
            fromClauseBuffer.append(point);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_ID_TIERS_TI);
            fromClauseBuffer.append(egal);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_PARENT);
            fromClauseBuffer.append(point);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_PRESTATION_ID_TIERS_BENEFICIAIRE);

            // jointure entre la table des TITiers et la table des TIPavsp (avs)
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_AVS);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_AVS);
            fromClauseBuffer.append(point);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_ID_TIERS_TI);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_TIERS);
            fromClauseBuffer.append(point);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_ID_TIERS_TI);

        } else {

            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
            fromClauseBuffer.append(as);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);

            // jointure (FILS) entre la table des RFPrestations et la table des REPrestations
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
            fromClauseBuffer.append(as);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            fromClauseBuffer.append(on);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);
            fromClauseBuffer.append(egal);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_PARENT_ADAPTATION);

            // jointure (PARENT) entre la table des RFPrestations et la table des REPrestations
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
            fromClauseBuffer.append(as);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_PARENT);
            fromClauseBuffer.append(on);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_PARENT);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
            fromClauseBuffer.append(egal);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_PARENT);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);

            // jointure entre la table des REPrestations et la table RFPrestation
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
            fromClauseBuffer.append(as);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_FILS);
            fromClauseBuffer.append(on);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_FILS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
            fromClauseBuffer.append(egal);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RF_ACCORDEE_FILS);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);

            // jointure entre la table des TITiers et la table des REPrestations
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_TIERS);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_TIERS);
            fromClauseBuffer.append(point);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_ID_TIERS_TI);
            fromClauseBuffer.append(egal);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.ALIAS_TABLE_RE_ACCORDEE_FILS);
            fromClauseBuffer.append(point);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_PRESTATION_ID_TIERS_BENEFICIAIRE);

            // jointure entre la table des TITiers et la table des TIPavsp (avs)
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_AVS);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_AVS);
            fromClauseBuffer.append(point);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_ID_TIERS_TI);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.TABLE_TIERS);
            fromClauseBuffer.append(point);
            fromClauseBuffer
                    .append(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_ID_TIERS_TI);

        }

        return fromClauseBuffer.toString();
    }

    private String cs_source_adaptation = "";
    private String csGenrePrestationAccordee = "";
    private String csTypePrestationFils = "";
    private String csTypePrestationParent = "";
    private String dateDebutDroit = "";
    private String dateEcheance = "";
    private String dateFinDroit = "";
    private String dateValidationDecision = "";
    private transient String fromClause = null;
    private String idInfoCompta = "";
    private String idTiers = "";
    private boolean isDiminutionAndAdaptation = false;
    private String montantFils = "";
    private String montantParent = "";
    private String montantPrestation = "";
    private String nomTiers = "";
    private String nssTiers = "";
    private String prenomTiers = "";
    private String referencePaiement = "";

    public RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation(boolean isDiminutionAndAdaptation) {
        super();
        this.isDiminutionAndAdaptation = isDiminutionAndAdaptation;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.createFromClause(
                            _getCollection(), isDiminutionAndAdaptation));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        montantFils = statement
                .dbReadNumeric(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_MONTANT_FILS);
        montantParent = statement
                .dbReadNumeric(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_MONTANT_PARENT);
        idTiers = statement
                .dbReadString(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE);
        cs_source_adaptation = statement
                .dbReadNumeric(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_SOURCE);
        nssTiers = statement
                .dbReadString(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_NUM_AVS);
        nomTiers = statement
                .dbReadString(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_NOM);
        prenomTiers = statement
                .dbReadString(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptation.FIELDNAME_PRENOM);

        csTypePrestationParent = statement
                .dbReadString(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_TYPE_PRESTATION_PARENT);

        csTypePrestationFils = statement
                .dbReadString(RFPrestationAccordeeJointREPrestationAccordeeJointTiersAdaptationManager.ALIAS_CHAMP_TYPE_PRESTATION_FILS);

    }

    public String getCs_source_adaptation() {
        return cs_source_adaptation;
    }

    public String getCsGenrePrestationAccordee() {
        return csGenrePrestationAccordee;
    }

    public String getCsTypePrestationFils() {
        return csTypePrestationFils;
    }

    public String getCsTypePrestationParent() {
        return csTypePrestationParent;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateValidationDecision() {
        return dateValidationDecision;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantFils() {
        return montantFils;
    }

    public String getMontantParent() {
        return montantParent;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getNssTiers() {
        return nssTiers;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setCs_source_adaptation(String cs_source_adaptation) {
        this.cs_source_adaptation = cs_source_adaptation;
    }

    public void setCsGenrePrestationAccordee(String csGenrePrestationAccordee) {
        this.csGenrePrestationAccordee = csGenrePrestationAccordee;
    }

    public void setCsTypePrestationFils(String csTypePrestationFils) {
        this.csTypePrestationFils = csTypePrestationFils;
    }

    public void setCsTypePrestationParent(String csTypePrestationParent) {
        this.csTypePrestationParent = csTypePrestationParent;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateValidationDecision(String dateValidationDecision) {
        this.dateValidationDecision = dateValidationDecision;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantFils(String montantFils) {
        this.montantFils = montantFils;
    }

    public void setMontantParent(String montantParent) {
        this.montantParent = montantParent;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNssTiers(String nssTiers) {
        this.nssTiers = nssTiers;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

}
