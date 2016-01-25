package globaz.corvus.db.demandes;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class REDemandeRenteJointBaseCalcul extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idBaseCalcul = "";
    private String idDemandeRente = "";

    public REDemandeRenteJointBaseCalcul() {
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
    protected String _getTableName() {
        return REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idBaseCalcul = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        idDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // rien, ce n'est pas un objet de la base de données
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // rien, ce n'est pas un objet de la base de données
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public void setIdBaseCalcul(String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }
}
