package globaz.osiris.db.bulletinneutre;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class CABulletinNeutreTaxeSommation extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_IDTAXESOMMATION = "IDTAXESOMMATION";
    public static final String FIELD_MASSESALARIALEFROM = "MASSESALARIALEFROM";
    public static final String FIELD_MASSESALARIALETO = "MASSESALARIALETO";
    public static final String FIELD_MONTANT = "MONTANT";

    public static final String TABLE_CABNTXP = "CABNTXP";

    private String idTaxeSommation;
    private String masseSalarialeFrom;
    private String masseSalarialeTo;
    private String montant;

    /**
     * @see BEntity#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdTaxeSommation(this._incCounter(transaction, idTaxeSommation));
    }

    @Override
    protected String _getTableName() {
        return CABulletinNeutreTaxeSommation.TABLE_CABNTXP;
    }

    /**
     * @see BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdTaxeSommation(statement.dbReadNumeric(CABulletinNeutreTaxeSommation.FIELD_IDTAXESOMMATION));
        setMasseSalarialeFrom(statement.dbReadNumeric(CABulletinNeutreTaxeSommation.FIELD_MASSESALARIALEFROM, 2));
        setMasseSalarialeTo(statement.dbReadNumeric(CABulletinNeutreTaxeSommation.FIELD_MASSESALARIALETO, 2));
        setMontant(statement.dbReadNumeric(CABulletinNeutreTaxeSommation.FIELD_MONTANT, 2));
    }

    /**
     * @see BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing yet
    }

    /**
     * @see BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CABulletinNeutreTaxeSommation.FIELD_IDTAXESOMMATION,
                this._dbWriteNumeric(statement.getTransaction(), getIdTaxeSommation(), "idTaxeSommation"));
    }

    /**
     * @see BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CABulletinNeutreTaxeSommation.FIELD_IDTAXESOMMATION,
                this._dbWriteNumeric(statement.getTransaction(), getIdTaxeSommation(), "idTaxeSommation"));
        statement.writeField(CABulletinNeutreTaxeSommation.FIELD_MASSESALARIALEFROM,
                this._dbWriteNumeric(statement.getTransaction(), getMasseSalarialeFrom(), "masseSalarialeFrom"));
        statement.writeField(CABulletinNeutreTaxeSommation.FIELD_MASSESALARIALETO,
                this._dbWriteNumeric(statement.getTransaction(), getMasseSalarialeTo(), "masseSalarialeTo"));
        statement.writeField(CABulletinNeutreTaxeSommation.FIELD_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));

    }

    public String getIdTaxeSommation() {
        return idTaxeSommation;
    }

    public String getMasseSalarialeFrom() {
        return masseSalarialeFrom;
    }

    public String getMasseSalarialeTo() {
        return masseSalarialeTo;
    }

    public String getMontant() {
        return montant;
    }

    public void setIdTaxeSommation(String idTaxeSommation) {
        this.idTaxeSommation = idTaxeSommation;
    }

    public void setMasseSalarialeFrom(String masseSalarialeFrom) {
        this.masseSalarialeFrom = masseSalarialeFrom;
    }

    public void setMasseSalarialeTo(String masseSalarialeTo) {
        this.masseSalarialeTo = masseSalarialeTo;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}
