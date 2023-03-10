package globaz.lynx.db.impression;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXUtils;

public class LXImpressionGrandLivre extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_FOURNISSEURESTBLOQUE = "FOURNISSEURESTBLOQUE";
    public static final String FIELD_IDEXTERNEFOUR = "IDEXTERNEFOUR";
    public static final String FIELD_IDEXTERNESECTION = "IDEXTERNESECTION";

    private String complement;
    private String csCodeTVA;
    private String csMotifBlocage;
    private String csTypeOperation;
    private String dateEcheance;
    private String dateOperation;
    private Boolean estBloque = new Boolean(false);
    private Boolean fournisseurEstBloque = new Boolean(false);
    private String idExterneFournisseur;
    private String idExterneSection;
    private String idFournisseur;
    private String idSociete;
    private String libelle;
    private String montant;
    private String nom;
    private String referenceExterne;
    private String tauxEscompte;

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        setMontant(statement.dbReadNumeric(LXOperation.FIELD_MONTANT, 2));
        setLibelle(statement.dbReadString(LXOperation.FIELD_LIBELLE));
        setEstBloque(statement.dbReadBoolean(LXOperation.FIELD_ESTBLOQUE));
        setFournisseurEstBloque(statement.dbReadBoolean(LXImpressionGrandLivre.FIELD_FOURNISSEURESTBLOQUE));
        setTauxEscompte(statement.dbReadNumeric(LXOperation.FIELD_TAUXESCOMPTE, 2));
        setCsMotifBlocage(statement.dbReadNumeric(LXOperation.FIELD_CSMOTIFBLOCAGE));
        setCsCodeTVA(statement.dbReadNumeric(LXOperation.FIELD_CSCODETVA));
        setReferenceExterne(statement.dbReadString(LXOperation.FIELD_REFERENCEEXTERNE));
        setIdSociete(statement.dbReadNumeric(LXSection.FIELD_IDSOCIETE));
        setIdFournisseur(statement.dbReadNumeric(LXSection.FIELD_IDFOURNISSEUR));
        setIdExterneFournisseur(statement.dbReadString(LXImpressionGrandLivre.FIELD_IDEXTERNEFOUR));
        setIdExterneSection(statement.dbReadString(LXImpressionGrandLivre.FIELD_IDEXTERNESECTION));
        setDateEcheance(statement.dbReadDateAMJ(LXOperation.FIELD_DATEECHEANCE));
        setDateOperation(statement.dbReadDateAMJ(LXOperation.FIELD_DATEOPERATION));
        setNom(statement.dbReadString("HTLDE1"));
        setComplement(statement.dbReadString("HTLDE2"));
        setCsTypeOperation(statement.dbReadNumeric(LXOperation.FIELD_CSTYPEOPERATION));
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

    public String getComplement() {
        return complement;
    }

    public String getCsCodeTVA() {
        return csCodeTVA;
    }

    public String getCsMotifBlocage() {
        return csMotifBlocage;
    }

    public String getCsTypeOperation() {
        return csTypeOperation;
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

    /**
     * @return the fournisseurEstBloque
     */
    public Boolean getFournisseurEstBloque() {
        return fournisseurEstBloque;
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
     * Return une concat?nation s?par? d'un espace du nom et du compl?ment
     */
    public String getNomCompletFournisseur() {
        return LXUtils.getNomComplet(getNom(), getComplement());
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public String getTauxEscompte() {
        return tauxEscompte;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public void setCsCodeTVA(String csCodeTVA) {
        this.csCodeTVA = csCodeTVA;
    }

    public void setCsMotifBlocage(String csMotifBlocage) {
        this.csMotifBlocage = csMotifBlocage;
    }

    public void setCsTypeOperation(String csTypeOperation) {
        this.csTypeOperation = csTypeOperation;
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

    /**
     * @param fournisseurEstBloque
     *            the fournisseurEstBloque to set
     */
    public void setFournisseurEstBloque(Boolean fournisseurEstBloque) {
        this.fournisseurEstBloque = fournisseurEstBloque;
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

    public void setTauxEscompte(String tauxEscompte) {
        this.tauxEscompte = tauxEscompte;
    }
}
