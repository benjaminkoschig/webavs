package globaz.apg.db.droits;

import globaz.apg.interfaces.APDroitAvecParent;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * Jointure entre le droit APG militaire et les tiers
 * 
 * @author PBA
 */
public class APDroitMaterniteJointTiers extends BEntity implements APDroitAvecParent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDroit;
    private String idTiers;
    private String nomTiers;
    private String numeroAvsTiers;
    private String prenomTiers;
    private String uneDateDebutPeriode;
    private String uneDateFinPeriode;
    private String idDroitParent;

    public APDroitMaterniteJointTiers() {
        super();

        idDroit = "";
        idTiers = "";
        nomTiers = "";
        numeroAvsTiers = "";
        prenomTiers = "";
        uneDateDebutPeriode = "";
        uneDateFinPeriode = "";
        idDroitParent = "";
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG).append(",");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT).append(",");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT).append(",");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEFINDROIT).append(",");

        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;
        String tableDroitMaternite = _getCollection() + APDroitMaternite.TABLE_NAME_MAT;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tableDemandePrestation = _getCollection() + PRDemande.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableDroitLAPG);

        sql.append(" INNER JOIN ");
        sql.append(tableDroitMaternite);
        sql.append(" ON ");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        sql.append("=");
        sql.append(tableDroitMaternite).append(".").append(APDroitMaternite.FIELDNAME_IDDROIT_MAT);

        sql.append(" INNER JOIN ");
        sql.append(tableDemandePrestation);
        sql.append(" ON ");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDEMANDE);
        sql.append("=");
        sql.append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

        sql.append(" INNER JOIN ");
        sql.append(tableTiers);
        sql.append(" ON ");
        sql.append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS);
        sql.append("=");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(tablePersonneAvs);
        sql.append(" ON ");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return APDroitMaternite.TABLE_NAME_MAT;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        idDroitParent = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        idTiers = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
        nomTiers = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        numeroAvsTiers = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenomTiers = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        uneDateDebutPeriode = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT));
        uneDateFinPeriode = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(APDroitLAPG.FIELDNAME_DATEFINDROIT));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APDroitLAPG.FIELDNAME_IDDROIT_LAPG,
                this._dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    public String getIdDroit() {
        return idDroit;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getNumeroAvsTiers() {
        return numeroAvsTiers;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    String getUneDateDebutPeriode() {
        return uneDateDebutPeriode;
    }

    String getUneDateFinPeriode() {
        return uneDateFinPeriode;
    }

    @Override
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNumeroAvsTiers(String numeroAvsTiers) {
        this.numeroAvsTiers = numeroAvsTiers;
    }

    void setUneDateDebutPeriode(String uneDateDebutPeriode) {
        this.uneDateDebutPeriode = uneDateDebutPeriode;
    }

    void setUneDateFinPeriode(String uneDateFinPeriode) {
        this.uneDateFinPeriode = uneDateFinPeriode;
    }

    @Override
    public final String getIdDroitParent() {
        return idDroitParent;
    }

    @Override
    public final void setIdDroitParent(String idDroitParent) {
        this.idDroitParent = idDroitParent;
    }

}
