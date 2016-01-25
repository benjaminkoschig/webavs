/*
 * Créé le 5 nov. 07
 */
package globaz.corvus.db.attestationsFiscales;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author scr
 * 
 * 
 * 
 */
public class REAttestationFiscale extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CODE_BLOCAGE_1 = "CODE_1";
    public static final String CODE_BLOCAGE_2 = "CODE_2";
    public static final String CODE_BLOCAGE_3 = "CODE_3";
    public static final String CODE_BLOCAGE_4 = "CODE_4";

    private String codeBlocage = "";
    private String codePrestation = "";
    private String dateDebutDroit = "";
    private String dateDeces = "";
    private String dateFinDroit = "";
    private String idBaseCalcul = "";
    private String idEnteteBlocage = "";
    private String idInfoCompta = "";
    private String idRenteAccordee = "";
    private String idTiersBaseCalcul = "";
    private String idTiersBeneficiaire = "";
    private Boolean isRetenu;
    private String montantPrestation = "";
    private String nss = "";

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

        idRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        idInfoCompta = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        idBaseCalcul = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        idTiersBaseCalcul = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL);
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        dateDebutDroit = statement.dbReadString(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
        dateFinDroit = statement.dbReadString(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
        idEnteteBlocage = statement.dbReadNumeric(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE);
        isRetenu = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_RETENUES);
        codeBlocage = statement.dbReadString("CODE_BLOCAGE");
        dateDeces = statement.dbReadDateAMJ(REAttestationFiscaleManager.FIELDNAME_DATEDECES);
        nss = statement.dbReadString(REAttestationFiscaleManager.FIELDNAME_NSS);
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

    public String getCodeBlocage() {
        return codeBlocage;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getIdEnteteBlocage() {
        return idEnteteBlocage;
    }

    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getNss() {
        return nss;
    }

    public Boolean isRetenu() {
        return isRetenu;
    }

    public void setCodeBlocage(String codeBlocage) {
        this.codeBlocage = codeBlocage;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setIdBaseCalcul(String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setIdEnteteBlocage(String idEnteteBlocage) {
        this.idEnteteBlocage = idEnteteBlocage;
    }

    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiersBaseCalcul(String idtiersBaseCalcul) {
        idTiersBaseCalcul = idtiersBaseCalcul;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setRetenu(Boolean isRetenu) {
        this.isRetenu = isRetenu;
    }
}
