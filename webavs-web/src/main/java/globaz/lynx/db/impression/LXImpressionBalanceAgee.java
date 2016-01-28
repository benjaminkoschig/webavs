package globaz.lynx.db.impression;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXUtils;

public class LXImpressionBalanceAgee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_IDEXTERNEFOUR = "IDEXTERNEFOUR";
    public static final String FIELD_IDEXTERNESECTION = "IDEXTERNESECTION";

    private String complement;
    private String csEtat;
    private String dateEcheance;
    private String dateOperation;
    private Boolean estBloque = new Boolean(false);
    private String idExterneFournisseur;
    private String idExterneSection;
    private String idFournisseur;
    private String idOperation;
    private String idSection;
    private String idSociete;
    private String libelle;
    private String montant;
    private String nom;
    private String referenceExterne;

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        setIdOperation(statement.dbReadNumeric(LXOperation.FIELD_IDOPERATION));
        setIdSection(statement.dbReadNumeric(LXOperation.FIELD_IDSECTION));
        setMontant(statement.dbReadNumeric(LXOperation.FIELD_MONTANT, 2));
        setLibelle(statement.dbReadString(LXOperation.FIELD_LIBELLE));
        setEstBloque(statement.dbReadBoolean(LXOperation.FIELD_ESTBLOQUE));
        setIdSociete(statement.dbReadNumeric(LXSection.FIELD_IDSOCIETE));
        setIdFournisseur(statement.dbReadNumeric(LXSection.FIELD_IDFOURNISSEUR));
        setIdExterneFournisseur(statement.dbReadString(FIELD_IDEXTERNEFOUR));
        setIdExterneSection(statement.dbReadString(FIELD_IDEXTERNESECTION));
        setDateEcheance(statement.dbReadDateAMJ(LXOperation.FIELD_DATEECHEANCE));
        setDateOperation(statement.dbReadDateAMJ(LXOperation.FIELD_DATEOPERATION));
        setNom(statement.dbReadString("HTLDE1"));
        setComplement(statement.dbReadString("HTLDE2"));
        setCsEtat(statement.dbReadNumeric(LXOperation.FIELD_CSETATOPERATION));
        setReferenceExterne(statement.dbReadString(LXOperation.FIELD_REFERENCEEXTERNE));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nothing
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // nothing
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // nothing

    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getComplement() {
        return complement;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public String getIdExterneFournisseur() {
        return idExterneFournisseur;
    }

    public String getIdExterneSection() {
        return idExterneSection;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdOperation() {
        return idOperation;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdSociete() {
        return idSociete;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Return une concaténation séparé d'un espace du nom et du complément
     */
    public String getNomCompletFournisseur() {
        return LXUtils.getNomComplet(getNom(), getComplement());
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public void setIdExterneFournisseur(String idExterneFournisseur) {
        this.idExterneFournisseur = idExterneFournisseur;
    }

    public void setIdExterneSection(String idExterneSection) {
        this.idExterneSection = idExterneSection;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdOperation(String idOperation) {
        this.idOperation = idOperation;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }
}
