package globaz.aquila.db.access.plaintes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * BEntity factice utilisé pour l'impression de la liste des plaintes
 * 
 * @author MKA
 * 
 */
public class COPlainteForExcelList extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_DATE_PLAINTE = "OLDPLA";
    public static final String FIELD_DESCRIPTION_PLAINTE = "OLTDES";
    public static final String FIELD_MOTIF_PLAINTE = "OLTMOT";
    public final static String FIELD_NOM_ADMINISTRATEUR = "ADMINDESC";
    public final static String FIELD_NOM_SOCIETE = "SOCIETEDESC";
    public final static String FIELD_NUMERO_ADMINISTRATEUR = "ADMIN";
    public final static String FIELD_NUMERO_SOCIETE = "SOCIETE";
    public static final String FIELD_PERIODE_AU = "OLDPEA";
    public static final String FIELD_PERIODE_DU = "OLDPED";
    public static final String FIELD_TYPE_PLAINTE = "OLTTYP";

    private String datePlainte = "";
    private String description = "";
    private String motif = "";
    private String nomAdmin = "";
    private String numAdmin = "";
    private String periodeAu = "";
    private String periodeDu = "";
    private String societe = "";
    private String type = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAdmin = statement.dbReadString(COPlainteForExcelList.FIELD_NUMERO_ADMINISTRATEUR);
        nomAdmin = statement.dbReadString(COPlainteForExcelList.FIELD_NOM_ADMINISTRATEUR);
        societe = statement.dbReadString(COPlainteForExcelList.FIELD_NOM_SOCIETE);
        datePlainte = statement.dbReadDateAMJ(COPlainteForExcelList.FIELD_DATE_PLAINTE);
        description = statement.dbReadString(COPlainteForExcelList.FIELD_DESCRIPTION_PLAINTE);
        type = statement.dbReadString(COPlainteForExcelList.FIELD_TYPE_PLAINTE);
        motif = statement.dbReadString(COPlainteForExcelList.FIELD_MOTIF_PLAINTE);
        periodeDu = statement.dbReadDateAMJ(COPlainteForExcelList.FIELD_PERIODE_DU);
        periodeAu = statement.dbReadDateAMJ(COPlainteForExcelList.FIELD_PERIODE_AU);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getDatePlainte() {
        return datePlainte;
    }

    public String getDescription() {
        return description;
    }

    public String getMotif() {
        return motif;
    }

    public String getNomAdmin() {
        return nomAdmin;
    }

    public String getNumAdmin() {
        return numAdmin;
    }

    public String getPeriodeAu() {
        return periodeAu;
    }

    public String getPeriodeDu() {
        return periodeDu;
    }

    public String getSociete() {
        return societe;
    }

    public String getType() {
        return type;
    }

    public void setDatePlainte(String datePlainte) {
        this.datePlainte = datePlainte;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNomAdmin(String nomAdmin) {
        this.nomAdmin = nomAdmin;
    }

    public void setNumAdmin(String numAdmin) {
        this.numAdmin = numAdmin;
    }

    public void setPeriodeAu(String periodeAu) {
        this.periodeAu = periodeAu;
    }

    public void setPeriodeDu(String periodeDu) {
        this.periodeDu = periodeDu;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public void setType(String type) {
        this.type = type;
    }

}
