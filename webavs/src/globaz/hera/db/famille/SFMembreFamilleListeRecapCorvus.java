package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class SFMembreFamilleListeRecapCorvus extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idMembreFamille = "";
    private String idTiers = "";

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
        return SFMembreFamille.FIELD_IDMEMBREFAMILLE + ", " + SFMembreFamille.FIELD_IDTIERS;
    }

    @Override
    protected String _getTableName() {
        return SFMembreFamille.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMembreFamille = statement.dbReadNumeric(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        idTiers = statement.dbReadNumeric(SFMembreFamille.FIELD_IDTIERS);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(SFMembreFamille.FIELD_IDMEMBREFAMILLE,
                _dbWriteNumeric(statement.getTransaction(), idMembreFamille, "idMembreFamille"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
