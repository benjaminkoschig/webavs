package globaz.cygnus.db.recapitulation;

import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulation extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);

        return fromClauseBuffer.toString();
    }

    String idPrestation = "";
    /**
     * Génération de la clause from pour la requête
     * 
     * @param schema
     * 
     * @return la clause from
     */

    String idTiersOv = "";

    private String montantOvs = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // super._readProperties(statement);
        idTiersOv = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_TIERS);
        idPrestation = statement.dbReadNumeric(RFOrdresVersements.FIELDNAME_ID_PRESTATION);
        montantOvs = statement
                .dbReadNumeric(RFDecisionJointRFPrestationJointRFLotsJointRFOrdreVersementRecapitulationManager.ALIAS_SOMME_MONTANT_OV);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdTiersOv() {
        return idTiersOv;
    }

    public String getMontantOvs() {
        return montantOvs;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdTiersOv(String idTiersOv) {
        this.idTiersOv = idTiersOv;
    }

    public void setMontantOvs(String montantOvs) {
        this.montantOvs = montantOvs;
    }

}
