/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;

/**
 * 
 * @author fha
 * @revision JJE -> date échéance
 */
public class RFPrestationAccordeeJointREPrestationAccordee extends RFPrestationAccordee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String FIELDNAME_VISA_GESTIONNAIRE = "FVISA";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

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
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);

        /************* Faire aussi la jointure sur le gestionnaire *******/
        // jointure entre la table des prestations et la table des prestations des rentes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);

        return fromClauseBuffer.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String csGenrePrestationAccordee = "";
    private String dateDebutDroit = "";
    private String dateEcheance = "";
    private String dateFinDroit = "";
    private String dateValidationDecision = "";
    private transient String fromClause = null;
    private String idInfoCompta = "";
    private String idTiers = "";
    private String montantPrestation = "";
    private String referencePaiement = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFPrestationAccordeeJointREPrestationAccordee.createFromClause(_getCollection()));
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

        idTiers = statement.dbReadString(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        referencePaiement = statement.dbReadString(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT);
        csGenrePrestationAccordee = statement.dbReadString(REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        dateEcheance = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE));
        idInfoCompta = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);

    }

    public String getCsGenrePrestationAccordee() {
        return csGenrePrestationAccordee;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

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

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setCsGenrePrestationAccordee(String csGenrePrestationAccordee) {
        this.csGenrePrestationAccordee = csGenrePrestationAccordee;
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

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

}
