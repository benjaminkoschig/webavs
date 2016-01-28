package globaz.apg.db.droits;

import globaz.apg.interfaces.APDroitAvecParent;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.ArrayList;
import java.util.List;

/**
 * Jointure entre le droit APG militaire et les tiers
 * 
 * @author PBA
 */
public class APDroitAPGJointTiers extends BEntity implements APDroitAvecParent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csGenreService;
    private String idDroit;
    private String idTiers;
    private String nbrJourSoldes;
    private String nomTiers;
    private String numeroAvsTiers;
    private String numeroReference;
    private List<JadePeriodWrapper> periodes;
    private String prenomTiers;
    private String uneDateDebutPeriode;
    private String uneDateFinPeriode;
    private String idDroitParent;

    public APDroitAPGJointTiers() {
        super();

        csGenreService = "";
        idDroit = "";
        idTiers = "";
        nbrJourSoldes = "";
        nomTiers = "";
        numeroAvsTiers = "";
        numeroReference = "";
        periodes = new ArrayList<JadePeriodWrapper>();
        prenomTiers = "";
        uneDateDebutPeriode = "";
        uneDateFinPeriode = "";
        idDroitParent = "";
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;
        String tableDroitAPG = _getCollection() + APDroitAPG.TABLE_NAME_DROIT_APG;
        String tablePeriode = _getCollection() + APPeriodeAPG.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG).append(",");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT).append(",");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_GENRESERVICE).append(",");

        sql.append(tableDroitAPG).append(".").append(APDroitAPG.FIELDNAME_NBRJOURSSOLDES).append(",");
        sql.append(tableDroitAPG).append(".").append(APDroitAPG.FIELDNAME_NOCOMPTE).append(",");

        sql.append(tablePeriode).append(".").append(APPeriodeAPG.FIELDNAME_DATEDEBUT).append(",");
        sql.append(tablePeriode).append(".").append(APPeriodeAPG.FIELDNAME_DATEFIN).append(",");

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
        String tableDroitAPG = _getCollection() + APDroitAPG.TABLE_NAME_DROIT_APG;
        String tablePeriode = _getCollection() + APPeriodeAPG.TABLE_NAME;
        String tableDemandePrestation = _getCollection() + PRDemande.TABLE_NAME;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableDroitLAPG);

        sql.append(" INNER JOIN ");
        sql.append(tableDroitAPG);
        sql.append(" ON ");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        sql.append("=");
        sql.append(tableDroitAPG).append(".").append(APDroitAPG.FIELDNAME_IDDROIT_APG);

        sql.append(" INNER JOIN ");
        sql.append(tablePeriode);
        sql.append(" ON ");
        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        sql.append("=");
        sql.append(tablePeriode).append(".").append(APPeriodeAPG.FIELDNAME_IDDROIT);

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
        return APDroitAPG.TABLE_NAME_DROIT_APG;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        csGenreService = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_GENRESERVICE);
        idDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        idDroitParent = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        idTiers = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
        nbrJourSoldes = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NBRJOURSSOLDES);
        nomTiers = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        numeroAvsTiers = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        numeroReference = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCOMPTE);
        prenomTiers = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        uneDateDebutPeriode = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(APPeriodeAPG.FIELDNAME_DATEDEBUT));
        uneDateFinPeriode = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(APPeriodeAPG.FIELDNAME_DATEFIN));
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

    public String getCsGenreService() {
        return csGenreService;
    }

    @Override
    public String getIdDroit() {
        return idDroit;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNbrJourSoldes() {
        return nbrJourSoldes;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getNumeroAvsTiers() {
        return numeroAvsTiers;
    }

    public String getNumeroReference() {
        return numeroReference;
    }

    public List<JadePeriodWrapper> getPeriodes() {
        return periodes;
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

    public void setCsGenreService(String csGenreService) {
        this.csGenreService = csGenreService;
    }

    @Override
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNbrJourSoldes(String nbrJourSoldes) {
        this.nbrJourSoldes = nbrJourSoldes;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNumeroAvsTiers(String numeroAvsTiers) {
        this.numeroAvsTiers = numeroAvsTiers;
    }

    public void setNumeroReference(String numeroReference) {
        this.numeroReference = numeroReference;
    }

    public void setPeriodes(List<JadePeriodWrapper> periodes) {
        this.periodes = periodes;
    }

    void setUneDateDebutPeriode(String uneDateDebutPeriode) {
        this.uneDateDebutPeriode = uneDateDebutPeriode;
    }

    void setUneDateFinPeriode(String uneDateFinPeriode) {
        this.uneDateFinPeriode = uneDateFinPeriode;
    }

    @Override
    public String getIdDroitParent() {
        return idDroitParent;
    }

    @Override
    public void setIdDroitParent(String idDroitParent) {
        this.idDroitParent = idDroitParent;
    }
}
