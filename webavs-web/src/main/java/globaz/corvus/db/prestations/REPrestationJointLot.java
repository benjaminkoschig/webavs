package globaz.corvus.db.prestations;

import globaz.corvus.db.lots.RELot;
import globaz.globall.db.BStatement;

/**
 * Jointure entre la table de la prestations découlant d'une décision (REPREST) et les lots (RELOTS)
 * 
 * @author PBA
 */
public class REPrestationJointLot extends REPrestations {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatLot;
    private String csTypeLot;
    private String dateCreationLot;
    private String dateEnvoiLot;
    private String descriptionLot;
    private String gestionnaireLot;
    private String idJournalComptaLot;

    public REPrestationJointLot() {
        super();

        csEtatLot = "";
        csTypeLot = "";
        dateCreationLot = "";
        dateEnvoiLot = "";
        descriptionLot = "";
        gestionnaireLot = "";
        idJournalComptaLot = "";
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;
        String tableLot = _getCollection() + RELot.TABLE_NAME_LOT;

        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ETAT).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_DECISION).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_DEMANDE_RENTE).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_LOT).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_MOIS_ANNEE).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_MONTANT_PRESTATION).append(",");
        sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_TYPE).append(",");

        sql.append(tableLot).append(".").append(RELot.FIELDNAME_DATE_CREATION).append(",");
        sql.append(tableLot).append(".").append(RELot.FIELDNAME_DATE_ENVOI).append(",");
        sql.append(tableLot).append(".").append(RELot.FIELDNAME_DESCRIPTION).append(",");
        sql.append(tableLot).append(".").append(RELot.FIELDNAME_ETAT).append(",");
        sql.append(tableLot).append(".").append(RELot.FIELDNAME_ID_JOURNAL_CA).append(",");
        sql.append(tableLot).append(".").append(RELot.FIELDNAME_ID_LOT).append(",");
        sql.append(tableLot).append(".").append(RELot.FIELDNAME_LOT_OWNER).append(",");
        sql.append(tableLot).append(".").append(RELot.FIELDNAME_TYPE_LOT);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;
        String tableLot = _getCollection() + RELot.TABLE_NAME_LOT;

        sql.append(tablePrestation);

        sql.append(" INNER JOIN ").append(tableLot);
        sql.append(" ON ").append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_LOT).append("=")
                .append(tableLot).append(".").append(RELot.FIELDNAME_ID_LOT);

        return sql.toString();
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        csEtatLot = statement.dbReadNumeric(RELot.FIELDNAME_ETAT);
        csTypeLot = statement.dbReadNumeric(RELot.FIELDNAME_TYPE_LOT);
        dateCreationLot = statement.dbReadDateAMJ(RELot.FIELDNAME_DATE_CREATION);
        dateEnvoiLot = statement.dbReadDateAMJ(RELot.FIELDNAME_DATE_ENVOI);
        descriptionLot = statement.dbReadString(RELot.FIELDNAME_DESCRIPTION);
        gestionnaireLot = statement.dbReadString(RELot.FIELDNAME_LOT_OWNER);
        idJournalComptaLot = statement.dbReadNumeric(RELot.FIELDNAME_ID_JOURNAL_CA);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // rien, on ne peut pas écrire cette entité en BDD car composée de deux entités
    }

    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getCsTypeLot() {
        return csTypeLot;
    }

    public String getDateCreationLot() {
        return dateCreationLot;
    }

    public String getDateEnvoiLot() {
        return dateEnvoiLot;
    }

    public String getDescriptionLot() {
        return descriptionLot;
    }

    public String getGestionnaireLot() {
        return gestionnaireLot;
    }

    public String getIdJournalComptaLot() {
        return idJournalComptaLot;
    }

    public void setCsEtatLot(String csEtatLot) {
        this.csEtatLot = csEtatLot;
    }

    public void setCsTypeLot(String csTypeLot) {
        this.csTypeLot = csTypeLot;
    }

    public void setDateCreationLot(String dateCreationLot) {
        this.dateCreationLot = dateCreationLot;
    }

    public void setDateEnvoiLot(String dateEnvoiLot) {
        this.dateEnvoiLot = dateEnvoiLot;
    }

    public void setDescriptionLot(String descriptionLot) {
        this.descriptionLot = descriptionLot;
    }

    public void setGestionnaireLot(String gestionnaireLot) {
        this.gestionnaireLot = gestionnaireLot;
    }

    public void setIdJournalComptaLot(String idJournalComptaLot) {
        this.idJournalComptaLot = idJournalComptaLot;
    }
}
