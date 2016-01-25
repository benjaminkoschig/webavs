package globaz.naos.db.affiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class AFAffiliationModeFacturation extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Fields
    private java.lang.String affilieNumero = new String();
    private java.lang.String codeFacturation = new String();
    private java.lang.String declarationSalaire = new String();
    private java.lang.String idTiers = new String();
    private java.lang.Boolean releveParitaire = new Boolean(false);
    private java.lang.Boolean traitement = new Boolean(false);
    private java.lang.String typeAffiliation = new String();

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric("HTITIE");
        affilieNumero = statement.dbReadString("MALNAF");
        typeAffiliation = statement.dbReadNumeric("MATTAF");
        declarationSalaire = statement.dbReadNumeric("MATDEC");
        traitement = statement.dbReadBoolean("MABTRA");
        releveParitaire = statement.dbReadBoolean("MABREP");
        codeFacturation = statement.dbReadNumeric("MATCFA");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // On ne fait rien

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // On ne fait rien

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // On ne fait rien

    }

    public java.lang.String getAffilieNumero() {
        return affilieNumero;
    }

    public java.lang.String getCodeFacturation() {
        return codeFacturation;
    }

    public java.lang.String getDeclarationSalaire() {
        return declarationSalaire;
    }

    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public java.lang.Boolean getReleveParitaire() {
        return releveParitaire;
    }

    public java.lang.Boolean getTraitement() {
        return traitement;
    }

    public java.lang.String getTypeAffiliation() {
        return typeAffiliation;
    }

    public java.lang.Boolean isReleveParitaire() {
        return releveParitaire;
    }

    public java.lang.Boolean isTraitement() {
        return traitement;
    }

    public void setAffilieNumero(java.lang.String affilieNumero) {
        this.affilieNumero = affilieNumero;
    }

    public void setCodeFacturation(java.lang.String codeFacturation) {
        this.codeFacturation = codeFacturation;
    }

    public void setDeclarationSalaire(java.lang.String declarationSalaire) {
        this.declarationSalaire = declarationSalaire;
    }

    public void setIdTiers(java.lang.String idTiers) {
        this.idTiers = idTiers;
    }

    public void setReleveParitaire(java.lang.Boolean releveParitaire) {
        this.releveParitaire = releveParitaire;
    }

    public void setTraitement(java.lang.Boolean traitement) {
        this.traitement = traitement;
    }

    public void setTypeAffiliation(java.lang.String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

}
