package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * <p>
 * Entité représentant une jointure, dans la situation familiale (HERA), entre les enfants (table SFENFAN) et les
 * membres de famille (table SFMBRFAM).
 * </p>
 * 
 * @author PBA
 */
public class SFEnfantJoinMembreFamille extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idConjoint;
    private String idEnfant;
    private String idMembreFamille;
    private String idTiers;
    private String nom;
    private String nss;
    private String prenom;

    public SFEnfantJoinMembreFamille() {
        super();

        idConjoint = "";
        idEnfant = "";
        idMembreFamille = "";
        idTiers = "";
        nom = "";
        nss = "";
        prenom = "";
    }

    @Override
    protected String _getFields(BStatement statement) {

        String tableEnfant = _getCollection() + SFEnfant.TABLE_NAME;
        String tableMembreFamille = _getCollection() + SFMembreFamille.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tableEnfant).append(".").append(SFEnfant.FIELD_IDENFANT).append(",");
        sql.append(tableEnfant).append(".").append(SFEnfant.FIELD_IDCONJOINT).append(",");

        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE).append(",");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_IDTIERS).append(",");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_NOM).append(",");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_PRENOM).append(",");

        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        String tableEnfant = _getCollection() + SFEnfant.TABLE_NAME;
        String tableMembreFamille = _getCollection() + SFMembreFamille.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(tableEnfant);

        sql.append(" INNER JOIN ");
        sql.append(tableMembreFamille);
        sql.append(" ON ");
        sql.append(tableEnfant).append(".").append(SFEnfant.FIELD_IDMEMBREFAMILLE);
        sql.append("=");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_IDMEMBREFAMILLE);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(tableTiers);
        sql.append(" ON ");
        sql.append(tableMembreFamille).append(".").append(SFMembreFamille.FIELD_IDTIERS);
        sql.append("=");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(tablePersonneAvs);
        sql.append(" ON ");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return SFEnfant.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idConjoint = statement.dbReadNumeric(SFEnfant.FIELD_IDCONJOINT);
        idEnfant = statement.dbReadNumeric(SFEnfant.FIELD_IDENFANT);
        idMembreFamille = statement.dbReadNumeric(SFMembreFamille.FIELD_IDMEMBREFAMILLE);
        idTiers = statement.dbReadNumeric(SFMembreFamille.FIELD_IDTIERS);

        if (JadeStringUtil.isBlank(idTiers)) {
            nom = statement.dbReadString(SFMembreFamille.FIELD_NOM);
            prenom = statement.dbReadString(SFMembreFamille.FIELD_PRENOM);
        } else {
            nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
            prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
            nss = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien, cette entité n'est pas sauvegardable en BDD
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(SFEnfant.FIELD_IDENFANT,
                this._dbWriteNumeric(statement.getTransaction(), idEnfant, "idEnfant"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException(
                "Entité comprenant des jointures, ne peut pas être sauvée en base de données");
    }

    public String getIdConjoint() {
        return idConjoint;
    }

    public String getIdEnfant() {
        return idEnfant;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setIdConjoint(String idConjoint) {
        this.idConjoint = idConjoint;
    }

    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
