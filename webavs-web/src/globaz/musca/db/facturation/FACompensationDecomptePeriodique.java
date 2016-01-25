package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author MMO
 * @since 26 juin 2012
 */
public class FACompensationDecomptePeriodique extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean compteAnnexeASurveiller = new Boolean(false);
    private String idExterneFactureCompensation = "";
    private String idFacture = "";
    private String idSection = "";
    private String idTiers = "";
    private String idTypeFactureCompensation = "";
    private String montantFacture = "";

    private String montantSection = "";
    private String numeroAffilie = "";
    private String role = "";

    /**
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idTiers = statement.dbReadNumeric("IDTIERS");
        numeroAffilie = statement.dbReadString("IDEXTERNEROLE");
        role = statement.dbReadNumeric("IDROLE");
        compteAnnexeASurveiller = statement.dbReadBoolean("ASURVEILLER");
        idFacture = statement.dbReadNumeric("IDENTETEFACTURE");
        idSection = statement.dbReadNumeric("IDSECTION");
        montantFacture = statement.dbReadNumeric("TOTALFACTURE", 2);
        montantSection = statement.dbReadNumeric("SOLDE", 2);
        idExterneFactureCompensation = statement.dbReadString("IDEXTERNE");
        idTypeFactureCompensation = statement.dbReadNumeric("IDTYPESECTION");

    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public Boolean getCompteAnnexeASurveiller() {
        return compteAnnexeASurveiller;
    }

    public String getIdExterneFactureCompensation() {
        return idExterneFactureCompensation;
    }

    public String getIdFacture() {
        return idFacture;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTypeFactureCompensation() {
        return idTypeFactureCompensation;
    }

    public String getMontantFacture() {
        return montantFacture;
    }

    public String getMontantSection() {
        return montantSection;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getRole() {
        return role;
    }

    public void setCompteAnnexeASurveiller(Boolean compteAnnexeASurveiller) {
        this.compteAnnexeASurveiller = compteAnnexeASurveiller;
    }

    public void setIdExterneFactureCompensation(String idExterneFactureCompensation) {
        this.idExterneFactureCompensation = idExterneFactureCompensation;
    }

    public void setIdFacture(String idFacture) {
        this.idFacture = idFacture;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTypeFactureCompensation(String idTypeFactureCompensation) {
        this.idTypeFactureCompensation = idTypeFactureCompensation;
    }

    public void setMontantFacture(String montantFacture) {
        this.montantFacture = montantFacture;
    }

    public void setMontantSection(String montantSection) {
        this.montantSection = montantSection;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
