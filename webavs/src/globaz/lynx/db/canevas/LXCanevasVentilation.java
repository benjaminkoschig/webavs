package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class LXCanevasVentilation extends BEntity implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CODEDEBITCREDIT = "CODEDEBITCREDIT";
    public static final String FIELD_COURSMONNAIE = "COURSMONNAIE";
    public static final String FIELD_IDCENTRECHARGE = "IDCENTRECHARGE";
    public static final String FIELD_IDCOMPTE = "IDCOMPTE";
    public static final String FIELD_IDOPERATIONCANEVAS = "IDOPERATIONCANEVAS";
    // Colonnes de la table
    public static final String FIELD_IDVENTILATIONCANEVAS = "IDVENTILATIONCANEVAS";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_MONTANT = "MONTANT";
    public static final String FIELD_MONTANTMONNAIE = "MONTANTMONNAIE";
    public static final String FIELD_POURCENTAGE = "POURCENTAGE";

    // Nom de la table
    public static final String TABLE_LXCANVP = "LXCANVP";

    private String codeDebitCredit = "";
    private String coursMonnaie = "0.00000";
    private String idCentreCharge = "";
    private String idCompte = "";
    private String idOperationCanevas = "";
    private String idVentilationCanevas = "";
    private String libelle = "";
    private String montant = "";
    private String montantMonnaie = "";
    private String pourcentage = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdVentilationCanevas(this._incCounter(transaction, idVentilationCanevas));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return LXCanevasVentilation.TABLE_LXCANVP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdVentilationCanevas(statement.dbReadNumeric(LXCanevasVentilation.FIELD_IDVENTILATIONCANEVAS));
        setIdOperationCanevas(statement.dbReadNumeric(LXCanevasVentilation.FIELD_IDOPERATIONCANEVAS));
        setIdCompte(statement.dbReadNumeric(LXCanevasVentilation.FIELD_IDCOMPTE));
        setIdCentreCharge(statement.dbReadNumeric(LXCanevasVentilation.FIELD_IDCENTRECHARGE));
        setMontant(statement.dbReadNumeric(LXCanevasVentilation.FIELD_MONTANT, 2));
        setLibelle(statement.dbReadString(LXCanevasVentilation.FIELD_LIBELLE));
        setMontantMonnaie(statement.dbReadNumeric(LXCanevasVentilation.FIELD_MONTANTMONNAIE));
        setCoursMonnaie(statement.dbReadNumeric(LXCanevasVentilation.FIELD_COURSMONNAIE));
        setCodeDebitCredit(statement.dbReadNumeric(LXCanevasVentilation.FIELD_CODEDEBITCREDIT));
        setPourcentage(statement.dbReadNumeric(LXCanevasVentilation.FIELD_POURCENTAGE));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        // Controle de l'id operation
        if (JadeStringUtil.isIntegerEmpty(getIdOperationCanevas())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_OPERATION"));
        }
        // Controle de l'id compte
        if (JadeStringUtil.isIntegerEmpty(getIdCompte())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_IDENTIFIANT_COMPTE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(LXCanevasVentilation.FIELD_IDVENTILATIONCANEVAS,
                this._dbWriteNumeric(statement.getTransaction(), getIdVentilationCanevas(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(LXCanevasVentilation.FIELD_IDVENTILATIONCANEVAS,
                this._dbWriteNumeric(statement.getTransaction(), getIdVentilationCanevas(), "idVentilationCanevas"));
        statement.writeField(LXCanevasVentilation.FIELD_IDOPERATIONCANEVAS,
                this._dbWriteNumeric(statement.getTransaction(), getIdOperationCanevas(), "idOperationCanevas"));
        statement.writeField(LXCanevasVentilation.FIELD_IDCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField(LXCanevasVentilation.FIELD_IDCENTRECHARGE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(), "idCentreCharge"));
        statement.writeField(LXCanevasVentilation.FIELD_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField(LXCanevasVentilation.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(LXCanevasVentilation.FIELD_MONTANTMONNAIE,
                this._dbWriteNumeric(statement.getTransaction(), getMontantMonnaie(), "montantMonnaie"));
        statement.writeField(LXCanevasVentilation.FIELD_POURCENTAGE,
                this._dbWriteNumeric(statement.getTransaction(), getPourcentage(), "pourcentage"));
        statement.writeField(LXCanevasVentilation.FIELD_COURSMONNAIE,
                this._dbWriteNumeric(statement.getTransaction(), getCoursMonnaie(), "coursMonnaie"));
        statement.writeField(LXCanevasVentilation.FIELD_CODEDEBITCREDIT,
                this._dbWriteNumeric(statement.getTransaction(), getCodeDebitCredit(), "codeDebitCredit"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    // *******************************************************
    // Clonage de l'object
    // *******************************************************
    @Override
    public Object clone() {
        LXCanevasVentilation ventilationClone = null;
        try {
            ventilationClone = (LXCanevasVentilation) super.clone();
        } catch (CloneNotSupportedException cnse) {
            _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("CLONE_ERROR"));
        }
        return ventilationClone;
    }

    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public String getCoursMonnaie() {
        return coursMonnaie;
    }

    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    public String getIdCompte() {
        return idCompte;
    }

    public String getIdOperationCanevas() {
        return idOperationCanevas;
    }

    public String getIdVentilationCanevas() {
        return idVentilationCanevas;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantMonnaie() {
        return montantMonnaie;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getPourcentage() {
        return pourcentage;
    }

    public void setCodeDebitCredit(String codeDebitCredit) {
        this.codeDebitCredit = codeDebitCredit;
    }

    public void setCoursMonnaie(String coursMonnaie) {
        this.coursMonnaie = coursMonnaie;
    }

    public void setIdCentreCharge(String idCentreCharge) {
        this.idCentreCharge = idCentreCharge;
    }

    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    public void setIdOperationCanevas(String idOperationCanevas) {
        this.idOperationCanevas = idOperationCanevas;
    }

    public void setIdVentilationCanevas(String idVentilationCanevas) {
        this.idVentilationCanevas = idVentilationCanevas;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantMonnaie(String montantMonnaie) {
        this.montantMonnaie = montantMonnaie;
    }

    public void setPourcentage(String pourcentage) {
        this.pourcentage = pourcentage;
    }
}
