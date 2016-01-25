package globaz.corvus.db.decisions;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * Entité utilisée par le manager {@link RERentesADiminuerManager} qui permet de rechercher les rentes nécessitant une
 * diminution lors de la préparation d'une décision depuis une demande de rente
 */
public class RERentesADiminuer extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append(",");
        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");

        sql.append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(",");
        sql.append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(",");
        sql.append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(",");
        sql.append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(",");
        sql.append(RERentesADiminuerManager.ALIAS_PRESTATION_ACCORDEE_FAMILLE).append(".")
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("this entity can't be persisted");
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("this entity can't be persisted");
    }

}
