/**
 * 
 */
package globaz.corvus.db.ventilation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Bean représentant une ventilation dans les prestations accordées
 * 
 * @author est *
 */
public class REVentilation extends BEntity {

    private static final long serialVersionUID = -5702500565129720354L;
    public static final String FIELDNAME_ID_VENTILATION = "ID_VENTILATION";
    public static final String FIELDNAME_ID_PRESTATION_ACCORDEE = "ID_REPRACC";
    public static final String FIELDNAME_MONTANT_VENTILE = "MONTANT_VENTILE";
    public static final String FIELDNAME_CS_TYPE_VENTILATION = "CS_TYPE_VENTILATION";
    public static final String TABLE_NAME_VENTILATION = "RE_VENTILATION";

    private String idVentilation = "";
    private String idPrestationAccordee = "";
    private String montantVentile = "";
    private String csTypeVentilation = "";

    @Override
    protected String _getTableName() {
        return TABLE_NAME_VENTILATION;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idVentilation = statement.dbReadNumeric(FIELDNAME_ID_VENTILATION);
        idPrestationAccordee = statement.dbReadNumeric(FIELDNAME_ID_PRESTATION_ACCORDEE);
        montantVentile = statement.dbReadNumeric(FIELDNAME_MONTANT_VENTILE);
        csTypeVentilation = statement.dbReadNumeric(FIELDNAME_CS_TYPE_VENTILATION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        //
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_VENTILATION, this._dbWriteNumeric(
                statement.getTransaction(), idVentilation, "idVentilation"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_PRESTATION_ACCORDEE,
                _dbWriteNumeric(statement.getTransaction(), idPrestationAccordee, "idPrestationAccordee"));
        statement.writeField(FIELDNAME_MONTANT_VENTILE,
                _dbWriteNumeric(statement.getTransaction(), montantVentile, "montantVentile"));
        statement.writeField(FIELDNAME_CS_TYPE_VENTILATION,
                _dbWriteNumeric(statement.getTransaction(), csTypeVentilation, "csTypeVentilation"));
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getMontantVentile() {
        return montantVentile;
    }

    public String getCsTypeVentilation() {
        return csTypeVentilation;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setMontantVentile(String montantVentile) {
        this.montantVentile = montantVentile;
    }

    public void setCsTypeVentilation(String csTypeVentilation) {
        this.csTypeVentilation = csTypeVentilation;
    }

    public String getIdVentilation() {
        return idVentilation;
    }

    public void setIdVentilation(String idVentilation) {
        this.idVentilation = idVentilation;
    }
}