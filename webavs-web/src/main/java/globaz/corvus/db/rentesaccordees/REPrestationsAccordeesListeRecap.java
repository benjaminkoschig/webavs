package globaz.corvus.db.rentesaccordees;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * @author HPE
 */
public class REPrestationsAccordeesListeRecap extends BEntity {

    private static final long serialVersionUID = 1L;

    private CodePrestation codePrestation;
    private String idPrestationAccordee;
    private boolean isPrestationBloquee;
    private boolean isRetenues;
    private FWCurrency montantPrestation;

    public REPrestationsAccordeesListeRecap() {
        super();

        codePrestation = CodePrestation.INCONNU;
        idPrestationAccordee = "";
        isPrestationBloquee = false;
        isRetenues = false;
        montantPrestation = new FWCurrency("0.00");
    }

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
    protected String _getFields(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE)
                .append(", ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)
                .append(", ");

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION)
                .append(", ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                .append(", ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        codePrestation = CodePrestation.parse((statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)));
        idPrestationAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        isRetenues = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_RETENUES);
        isPrestationBloquee = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);
        montantPrestation = new FWCurrency(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationAccordee, "idPrestationAccordee"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // rien
    }

    public CodePrestation getCodePrestation() {
        return codePrestation;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public boolean isPrestationBloquee() {
        return isPrestationBloquee;
    }

    public boolean isRetenues() {
        return isRetenues;
    }

    public FWCurrency getMontantPrestation() {
        return montantPrestation;
    }

    public void setCodePrestation(CodePrestation codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setPrestationBloquee(boolean isPrestationBloquee) {
        this.isPrestationBloquee = isPrestationBloquee;
    }

    public void setRetenues(boolean isRetenues) {
        this.isRetenues = isRetenues;
    }

    public void setMontantPrestation(FWCurrency montant) {
        montantPrestation = montant;
    }
}
