package globaz.osiris.db.contentieux;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 09:29:42)
 * 
 * @author: Administrator
 */
public class CATrancheTaxe extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idCalculTaxe = new String();
    private java.lang.String idTrancheTaxe = new String();
    private java.lang.String montantVariable = new String();
    private java.lang.String tauxPlafond = new String();
    private java.lang.String valeurPlafond = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CATrancheTaxe
     */
    public CATrancheTaxe() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CATXTTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTrancheTaxe = statement.dbReadNumeric("IDTRANCHETAXE");
        idCalculTaxe = statement.dbReadNumeric("IDCALCULTAXE");
        valeurPlafond = statement.dbReadNumeric("VALEURPLAFOND", 2);
        tauxPlafond = statement.dbReadNumeric("TAUXPLAFOND");
        montantVariable = statement.dbReadNumeric("MONTANTVARIABLE", 2);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDTRANCHETAXE", this._dbWriteNumeric(statement.getTransaction(), getIdTrancheTaxe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDTRANCHETAXE",
                this._dbWriteNumeric(statement.getTransaction(), getIdTrancheTaxe(), "idTrancheTaxe"));
        statement.writeField("IDCALCULTAXE",
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), "idCalculTaxe"));
        statement.writeField("VALEURPLAFOND",
                this._dbWriteNumeric(statement.getTransaction(), getValeurPlafond(), "valeurPlafond"));
        statement.writeField("TAUXPLAFOND",
                this._dbWriteNumeric(statement.getTransaction(), getTauxPlafond(), "tauxPlafond"));
        statement.writeField("MONTANTVARIABLE",
                this._dbWriteNumeric(statement.getTransaction(), getMontantVariable(), "montantVariable"));
    }

    public java.lang.String getIdCalculTaxe() {
        return idCalculTaxe;
    }

    /**
     * Getter
     */
    public java.lang.String getIdTrancheTaxe() {
        return idTrancheTaxe;
    }

    public java.lang.String getMontantVariable() {
        return montantVariable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2002 08:13:14)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTauxPlafond() {
        return tauxPlafond;
    }

    public java.lang.String getValeurPlafond() {
        return valeurPlafond;
    }

    public void setIdCalculTaxe(java.lang.String newIdCalculTaxe) {
        idCalculTaxe = newIdCalculTaxe;
    }

    /**
     * Setter
     */
    public void setIdTrancheTaxe(java.lang.String newIdTrancheTaxe) {
        idTrancheTaxe = newIdTrancheTaxe;
    }

    public void setMontantVariable(java.lang.String newMontantVariable) {
        montantVariable = newMontantVariable;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2002 08:13:14)
     * 
     * @param newTauxPlafond
     *            java.lang.String
     */
    public void setTauxPlafond(java.lang.String newTauxPlafond) {
        tauxPlafond = newTauxPlafond;
    }

    public void setValeurPlafond(java.lang.String newValeurPlafond) {
        valeurPlafond = newValeurPlafond;
    }
}
