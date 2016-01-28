/*
 * Créé le 5 nov. 07
 */
package globaz.corvus.db.attestationsFiscales;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr
 * 
 * 
 * 
 */
public class REAttestationFiscaleRenteAccordee extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_TIERS = "TITIERP";
    private String codePrestation = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String fractionRente = "";
    private String idInfoCompta = "";
    private String idPrestationAccordee = "";
    private String idRenteAccordee = "";

    private String idTierAdressePmt = "";
    private String idTierRequerant = "";

    private String idTiersBaseCalcul = "";

    private String idTiersBeneficiaire = "";
    private String nomBenef = "";
    private String numeroAvsBenef = "";
    private String prenomBenef = "";
    private String sexe = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        idRenteAccordee = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        idTierAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idTierRequerant = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        numeroAvsBenef = statement.dbReadString(REAttestationFiscaleRenteAccordee.FIELDNAME_NUM_AVS);
        nomBenef = statement.dbReadString(REAttestationFiscaleRenteAccordee.FIELDNAME_NOM);
        prenomBenef = statement.dbReadString(REAttestationFiscaleRenteAccordee.FIELDNAME_PRENOM);
        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        fractionRente = statement.dbReadString(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE);
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        idTiersBaseCalcul = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL);
        idInfoCompta = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        sexe = statement.dbReadString(REAttestationFiscaleRenteAccordee.FIELDNAME_SEXE);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTierAdressePmt() {
        return idTierAdressePmt;
    }

    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getNomBenef() {
        return nomBenef;
    }

    public String getNumeroAvsBenef() {
        return numeroAvsBenef;
    }

    public String getPrenomBenef() {
        return prenomBenef;
    }

    public String getSexe() {
        return sexe;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTierAdressePmt(String idTierAdressePmt) {
        this.idTierAdressePmt = idTierAdressePmt;
    }

    public void setIdTierRequerant(String idTierRequerant) {
        this.idTierRequerant = idTierRequerant;
    }

    public void setIdTiersBaseCalcul(String idTiersBaseCalcule) {
        idTiersBaseCalcul = idTiersBaseCalcule;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setNomBenef(String nomBenef) {
        this.nomBenef = nomBenef;
    }

    public void setNumeroAvsBenef(String numeroAvsBenef) {
        this.numeroAvsBenef = numeroAvsBenef;
    }

    public void setPrenomBenef(String prenomBenef) {
        this.prenomBenef = prenomBenef;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
}
