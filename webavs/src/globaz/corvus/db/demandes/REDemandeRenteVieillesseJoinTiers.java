package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class REDemandeRenteVieillesseJoinTiers extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDemandeRente;
    private String idTiers;

    public REDemandeRenteVieillesseJoinTiers() {
        super();

        idDemandeRente = "";
        idTiers = "";
    }

    @Override
    protected String _getFields(BStatement statement) {

        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        String tableDemandeRenteVieillesse = _getCollection()
                + REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE;
        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tableDemande = _getCollection() + PRDemande.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tableDemandeRenteVieillesse);

        sql.append(" INNER JOIN ");
        sql.append(tableDemandeRente);
        sql.append(" ON ");
        sql.append(tableDemandeRenteVieillesse).append(".")
                .append(REDemandeRenteVieillesse.FIELDNAME_ID_DEMANDE_RENTE_VIEILLESSE);
        sql.append("=");
        sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        sql.append(" INNER JOIN ");
        sql.append(tableDemande);
        sql.append(" ON ");
        sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);
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
        return REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        idTiers = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien, c'est une entité composée
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeRente));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("Unable to save this entity (composed with joints)");
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }
}
