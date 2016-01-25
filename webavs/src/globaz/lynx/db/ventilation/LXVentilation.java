package globaz.lynx.db.ventilation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class LXVentilation extends BEntity implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CODEDEBITCREDIT = "CODEDEBITCREDIT";
    public static final String FIELD_COURSMONNAIE = "COURSMONNAIE";
    public static final String FIELD_IDCENTRECHARGE = "IDCENTRECHARGE";
    public static final String FIELD_IDCOMPTE = "IDCOMPTE";
    public static final String FIELD_IDOPERATION = "IDOPERATION";
    // Colonnes de la table
    public static final String FIELD_IDVENTILATION = "IDVENTILATION";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_MONTANT = "MONTANT";
    public static final String FIELD_MONTANTMONNAIE = "MONTANTMONNAIE";

    // Nom de la table
    public static final String TABLE_LXVENTP = "LXVENTP";

    private String codeDebitCredit = "";
    private String coursMonnaie = "0.00000";
    private String idCentreCharge = "";
    private String idCompte = "";
    private String idOperation = "";
    private String idVentilation = "";
    private String libelle = "";
    private String montant = "";
    private String montantMonnaie = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdVentilation(_incCounter(transaction, idVentilation));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_LXVENTP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdVentilation(statement.dbReadNumeric(FIELD_IDVENTILATION));
        setIdOperation(statement.dbReadNumeric(FIELD_IDOPERATION));
        setIdCompte(statement.dbReadNumeric(FIELD_IDCOMPTE));
        setIdCentreCharge(statement.dbReadNumeric(FIELD_IDCENTRECHARGE));
        setMontant(statement.dbReadNumeric(FIELD_MONTANT, 2));
        setLibelle(statement.dbReadString(FIELD_LIBELLE));
        setMontantMonnaie(statement.dbReadNumeric(FIELD_MONTANTMONNAIE));
        setCoursMonnaie(statement.dbReadNumeric(FIELD_COURSMONNAIE));
        setCodeDebitCredit(statement.dbReadNumeric(FIELD_CODEDEBITCREDIT));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        // Controle de l'id operation
        if (JadeStringUtil.isIntegerEmpty(getIdOperation())) {
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
        statement.writeKey(FIELD_IDVENTILATION, _dbWriteNumeric(statement.getTransaction(), getIdVentilation(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDVENTILATION,
                _dbWriteNumeric(statement.getTransaction(), getIdVentilation(), "idVentilation"));
        statement.writeField(FIELD_IDOPERATION,
                _dbWriteNumeric(statement.getTransaction(), getIdOperation(), "idOperation"));
        statement.writeField(FIELD_IDCOMPTE, _dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField(FIELD_IDCENTRECHARGE,
                _dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(), "idCentreCharge"));
        statement.writeField(FIELD_MONTANT, _dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField(FIELD_LIBELLE, _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(FIELD_MONTANTMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getMontantMonnaie(), "montantMonnaie"));
        statement.writeField(FIELD_COURSMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getCoursMonnaie(), "coursMonnaie"));
        statement.writeField(FIELD_CODEDEBITCREDIT,
                _dbWriteNumeric(statement.getTransaction(), getCodeDebitCredit(), "codeDebitCredit"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    // *******************************************************
    // Clonage de l'object
    // *******************************************************
    @Override
    public Object clone() {
        LXVentilation ventilationClone = null;
        try {
            ventilationClone = (LXVentilation) super.clone();
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

    public String getIdOperation() {
        return idOperation;
    }

    public String getIdVentilation() {
        return idVentilation;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getMontantMonnaie() {
        return montantMonnaie;
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

    public void setIdOperation(String idOperation) {
        this.idOperation = idOperation;
    }

    public void setIdVentilation(String idVentilation) {
        this.idVentilation = idVentilation;
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
}
