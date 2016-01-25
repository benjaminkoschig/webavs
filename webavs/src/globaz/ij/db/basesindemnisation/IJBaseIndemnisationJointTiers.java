package globaz.ij.db.basesindemnisation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.db.demandes.PRDemande;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class IJBaseIndemnisationJointTiers extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idBaseAdministration;
    private String idTiers;

    // public IJBaseIndemnisationJointTiers() {
    // System.out.println("New IJBaseIndemnisationJointTiers");
    // }

    // @Override
    // protected String _getFields(BStatement statement) {
    // String tableBaseIndemnisation = this._getCollection() + IJBaseIndemnisation.TABLE_NAME;
    // String tableTiers = this._getCollection() + ITITiersDefTable.TABLE_NAME;
    //
    // StringBuilder sql = new StringBuilder();
    // //
    // // sql.append(tableBaseIndemnisation).append(".").append(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION)
    // // .append(",");
    // // sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
    //
    // return sql.toString();
    // }

    @Override
    protected String _getFrom(BStatement statement) {
        String tableBaseIndemnisation = _getCollection() + IJBaseIndemnisation.TABLE_NAME;
        String tablePrononce = _getCollection() + IJPrononce.TABLE_NAME_PRONONCE;
        String tableDemande = _getCollection() + PRDemande.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();
        sql.append(tablePrononce);

        sql.append(" OUTER JOIN ");
        sql.append(tableBaseIndemnisation);
        sql.append(" ON ");
        sql.append(tableBaseIndemnisation).append(".").append(IJBaseIndemnisation.FIELDNAME_IDPRONONCE);
        sql.append("=");
        sql.append(tablePrononce).append(".").append(IJPrononce.FIELDNAME_ID_PRONONCE);

        sql.append(" INNER JOIN ");
        sql.append(tableDemande);
        sql.append(" ON ");
        sql.append(tablePrononce).append(".").append(IJPrononce.FIELDNAME_ID_DEMANDE);
        sql.append("=");
        sql.append(tableDemande).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

        sql.append(" INNER JOIN ");
        sql.append(tableTiers);
        sql.append(" ON ");
        sql.append(tableDemande).append(".").append(PRDemande.FIELDNAME_IDTIERS);
        sql.append("=");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return IJBaseIndemnisation.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idBaseAdministration = statement.dbReadNumeric(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION);
        idTiers = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
    }

    // @Override
    // protected String _getTableName() {
    // return IJBaseIndemnisation.TABLE_NAME;
    // }
    //
    // @Override
    // protected void _readProperties(BStatement statement) throws Exception {
    // this.idTiers = statement.dbReadString(IPRTiers.FIELD_TI_IDTIERS);
    //
    // }
    //
    // @Override
    // protected void _validate(BStatement statement) throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // protected void _writePrimaryKey(BStatement statement) throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // protected void _writeProperties(BStatement statement) throws Exception {
    // // TODO Auto-generated method stub
    //
    // }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien, c'est une entité composée
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IJBaseIndemnisation.FIELDNAME_IDBASEINDEMNISATION,
                this._dbWriteNumeric(statement.getTransaction(), idBaseAdministration));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("Unable to save this entity (composed with joints)");
    }

    public final String getIdBaseAdministration() {
        return idBaseAdministration;
    }

    public final String getIdTiers() {
        return idTiers;

    }

    public final void setIdBaseAdministration(String idBaseAdministration) {
        this.idBaseAdministration = idBaseAdministration;
    }

    public final void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
        System.out.println("IJBaseIndemnisationJointTiers.setIdTiers() : idTiers =  " + this.idTiers);
    }

}
