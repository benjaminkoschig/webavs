package ch.globaz.orion.ws.service.manager;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.math.BigDecimal;

public class AFReleveForSdd extends BEntity {

    private static final long serialVersionUID = 1L;

    private String idRubrique;
    private BigDecimal montantFacture;
    private BigDecimal masseFacture;

    @Override
    protected String _getTableName() {
        // Not implemented
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        montantFacture = statement.dbReadBigDecimal("MONTANTFACTURE");
        masseFacture = statement.dbReadBigDecimal("MASSEFACTURE");
        idRubrique = statement.dbReadNumeric("ID_RUBRIQUE");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not implemented
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public BigDecimal getMontantFacture() {
        return montantFacture;
    }

    public void setMontantFacture(BigDecimal montantFacture) {
        this.montantFacture = montantFacture;
    }

    public BigDecimal getMasseFacture() {
        return masseFacture;
    }

    public void setMasseFacture(BigDecimal masseFacture) {
        this.masseFacture = masseFacture;
    }

}
