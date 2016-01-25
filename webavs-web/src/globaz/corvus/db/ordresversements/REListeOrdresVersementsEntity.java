package globaz.corvus.db.ordresversements;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.prestations.REPrestations;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.math.BigDecimal;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;

/**
 * Entité pour la liste des ordres de versement
 */
public class REListeOrdresVersementsEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean compense;
    private String dateEnvoi;
    private String idDomaineApplicationAdressePaiement;
    private Long idOrdreVersement;
    private Long idPrestation;
    private Long idTiersAdressePaiement;
    private BigDecimal montant;
    private TypeOrdreVersement type;

    public REListeOrdresVersementsEntity() {
        super();

        compense = false;
        dateEnvoi = null;
        idDomaineApplicationAdressePaiement = null;
        idOrdreVersement = null;
        idPrestation = null;
        idTiersAdressePaiement = null;
        montant = null;
        type = null;
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

        String tableLot = _getCollection() + RELot.TABLE_NAME_LOT;
        String tableOrdreVersement = _getCollection() + REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;

        sql.append("DISTINCT ");

        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_IS_COMPENSE).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_DOMAINE_APP).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_PRESTATION).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_TIERS_ADRESSE_PMT)
                .append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_MONTANT).append(",");
        sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_TYPE).append(",");

        sql.append(tableLot).append(".").append(RELot.FIELDNAME_DATE_ENVOI).append(",");

        // pour l'order by du manager
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1_MAJ).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2_MAJ).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_PRESTATION);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;
        String tableLot = _getCollection() + RELot.TABLE_NAME_LOT;
        String tableOrdreVersement = _getCollection() + REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;
        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tableDossierPrestation = _getCollection() + PRDemande.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;

        sql.append(tablePrestation);

        sql.append(" INNER JOIN ").append(tableLot);
        sql.append(" ON ").append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_LOT).append("=")
                .append(tableLot).append(".").append(RELot.FIELDNAME_ID_LOT);

        sql.append(" INNER JOIN ").append(tableOrdreVersement);
        sql.append(" ON ").append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_PRESTATION)
                .append("=").append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_PRESTATION);

        sql.append(" INNER JOIN ").append(tableDemandeRente);
        sql.append(" ON ").append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_DEMANDE_RENTE)
                .append("=").append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        sql.append(" INNER JOIN ").append(tableDossierPrestation);
        sql.append(" ON ").append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION)
                .append("=").append(tableDossierPrestation).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(tableDossierPrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS).append("=")
                .append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        compense = statement.dbReadBoolean(REOrdresVersements.FIELDNAME_IS_COMPENSE);
        dateEnvoi = statement.dbReadNumeric(RELot.FIELDNAME_DATE_ENVOI);
        idDomaineApplicationAdressePaiement = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_DOMAINE_APP);
        idOrdreVersement = Long.parseLong(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT));
        idPrestation = Long.parseLong(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_PRESTATION));
        idTiersAdressePaiement = Long.parseLong(statement
                .dbReadNumeric(REOrdresVersements.FIELDNAME_ID_TIERS_ADRESSE_PMT));
        montant = new BigDecimal(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_MONTANT));
        type = TypeOrdreVersement.parse(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_TYPE));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getIdDomaineApplicationAdressePaiement() {
        return idDomaineApplicationAdressePaiement;
    }

    public Long getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public Long getIdPrestation() {
        return idPrestation;
    }

    public Long getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public TypeOrdreVersement getType() {
        return type;
    }

    public boolean isCompense() {
        return compense;
    }
}
