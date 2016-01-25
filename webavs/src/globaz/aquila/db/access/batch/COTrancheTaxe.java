package globaz.aquila.db.access.batch;

import globaz.globall.db.BTransaction;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 09:29:42)
 * 
 * @author: Administrator
 */
public class COTrancheTaxe extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FNAME_ID_CALCUL_TAXE = "OIICTX";
    public static final String FNAME_ID_TRANCHE_TAXE = "OKITTX";
    public static final String FNAME_MONTANT_VARIABLE = "OKMVAR";
    public static final String FNAME_TAUX_PLAFOND = "OKNPLA";
    public static final String FNAME_VALEUR_PLAFOND = "OKMPLA";

    public static final String TABLE_NAME = "COTXTTP";

    private java.lang.String idCalculTaxe = new String();
    private java.lang.String idTrancheTaxe = new String();
    private java.lang.String montantVariable = new String();
    private java.lang.String tauxPlafond = new String();
    private java.lang.String valeurPlafond = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CATrancheTaxe
     */
    public COTrancheTaxe() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdTrancheTaxe(this._incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return COTrancheTaxe.TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTrancheTaxe = statement.dbReadNumeric(COTrancheTaxe.FNAME_ID_TRANCHE_TAXE);
        idCalculTaxe = statement.dbReadNumeric(COTrancheTaxe.FNAME_ID_CALCUL_TAXE);
        valeurPlafond = statement.dbReadNumeric(COTrancheTaxe.FNAME_VALEUR_PLAFOND, 2);
        tauxPlafond = statement.dbReadNumeric(COTrancheTaxe.FNAME_TAUX_PLAFOND);
        montantVariable = statement.dbReadNumeric(COTrancheTaxe.FNAME_MONTANT_VARIABLE, 2);
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
        statement.writeKey(COTrancheTaxe.FNAME_ID_TRANCHE_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdTrancheTaxe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(COTrancheTaxe.FNAME_ID_TRANCHE_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdTrancheTaxe(), "idTrancheTaxe"));
        statement.writeField(COTrancheTaxe.FNAME_ID_CALCUL_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), "idCalculTaxe"));
        statement.writeField(COTrancheTaxe.FNAME_VALEUR_PLAFOND,
                this._dbWriteNumeric(statement.getTransaction(), getValeurPlafond(), "valeurPlafond"));
        statement.writeField(COTrancheTaxe.FNAME_TAUX_PLAFOND,
                this._dbWriteNumeric(statement.getTransaction(), getTauxPlafond(), "tauxPlafond"));
        statement.writeField(COTrancheTaxe.FNAME_MONTANT_VARIABLE,
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
