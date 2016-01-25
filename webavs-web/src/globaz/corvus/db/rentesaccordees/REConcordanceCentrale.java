/*
 * Créé le 5 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author scr
 * 
 * 
 * 
 */
public class REConcordanceCentrale extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codePrestation = "";
    private String droitApplique = "";
    private String idBaseCalcul = "";
    private String idRenteAccordee = "";
    private String idTiersBeneficiaire = "";
    private String montant = "";
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
        idRenteAccordee = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        idBaseCalcul = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);
        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        droitApplique = statement.dbReadString(REBasesCalcul.FIELDNAME_DROIT_APPLIQUE);

        montant = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        codePrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        nss = statement.dbReadString(REConcordanceCentraleManager.FIELDNAME_NUM_AVS);

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

    public String getDroitApplique() {
        return droitApplique;
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getMontant() {
        return montant;
    }

    public String getNss() {
        return nss;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setDroitApplique(String droitApplique) {
        this.droitApplique = droitApplique;
    }

    public void setIdBaseCalcul(String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }
}
