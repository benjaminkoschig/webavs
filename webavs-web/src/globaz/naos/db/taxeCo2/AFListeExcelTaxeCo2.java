package globaz.naos.db.taxeCo2;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.listeAgenceCommunale.AFListeAgenceCommunale;

public class AFListeExcelTaxeCo2 extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static java.lang.String TABLE_FIELDS = "MWIDTC, AFAFFIP.HTITIE, AFTACOP.MAIAFF, MALNAF, MADESL, MWMMAS, MWMTAU, MWTFAC, MADDEB, MADFIN, AFAFFIP.MATMOT, MYDDEB, MYDFIN, HBCADM";

    private java.lang.String dateDebutAffiliation = new String();
    private java.lang.String dateDebutCaisse = new String();
    private java.lang.String dateFinAffiliation = new String();
    private java.lang.String dateFinCaisse = new String();
    private java.lang.String description = new String();
    private java.lang.String etat = new String();
    private java.lang.String idAffiliation = new String();
    private java.lang.String idTaxe = new String();
    private java.lang.String idTiers = new String();
    private java.lang.String masse = new String();
    private java.lang.String motifFin = new String();
    private java.lang.String numAffilie = new String();
    private java.lang.String numCaisseAvs = new String();
    private java.lang.String taux = new String();

    public AFListeExcelTaxeCo2() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        return AFListeAgenceCommunale.TABLE_FIELDS;
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        dateDebutAffiliation = statement.dbReadNumeric("MADDEB");
        dateDebutCaisse = statement.dbReadNumeric("MYDDEB");
        dateFinAffiliation = statement.dbReadNumeric("MADFIN");
        dateFinCaisse = statement.dbReadNumeric("MYDFIN");
        description = statement.dbReadString("MADESL");
        etat = statement.dbReadNumeric("MWTFAC");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idTaxe = statement.dbReadNumeric("MWIDTC");
        idTiers = statement.dbReadNumeric("HTITIE");
        masse = statement.dbReadNumeric("MWMMAS");
        motifFin = statement.dbReadNumeric("MATMOT");
        numAffilie = statement.dbReadString("MALNAF");
        numCaisseAvs = statement.dbReadString("HBCADM");
        taux = statement.dbReadNumeric("MWMTAU");
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

    public java.lang.String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public java.lang.String getDateDebutCaisse() {
        return dateDebutCaisse;
    }

    public java.lang.String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public java.lang.String getDateFinCaisse() {
        return dateFinCaisse;
    }

    public java.lang.String getDateFormate(String date) {
        String dateFormate = "";
        if (!JadeStringUtil.isBlankOrZero(date)) {
            dateFormate = date.substring(6, 8) + "." + date.substring(4, 6) + "." + date.substring(0, 4);
        }
        return dateFormate;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public java.lang.String getEtat() {
        return etat;
    }

    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    public java.lang.String getIdTaxe() {
        return idTaxe;
    }

    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public java.lang.String getMasse() {
        return masse;
    }

    public java.lang.String getMotifFin() {
        return motifFin;
    }

    public java.lang.String getNumAffilie() {
        return numAffilie;
    }

    public java.lang.String getNumCaisseAvs() {
        return numCaisseAvs;
    }

    public java.lang.String getTaux() {
        return taux;
    }

    public void setDateDebutAffiliation(java.lang.String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutCaisse(java.lang.String dateDebutCaisse) {
        this.dateDebutCaisse = dateDebutCaisse;
    }

    public void setDateFinAffiliation(java.lang.String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinCaisse(java.lang.String dateFinCaisse) {
        this.dateFinCaisse = dateFinCaisse;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public void setEtat(java.lang.String etat) {
        this.etat = etat;
    }

    public void setIdAffiliation(java.lang.String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdTaxe(java.lang.String idTaxe) {
        this.idTaxe = idTaxe;
    }

    public void setIdTiers(java.lang.String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMasse(java.lang.String masse) {
        this.masse = masse;
    }

    public void setMotifFin(java.lang.String motifFin) {
        this.motifFin = motifFin;
    }

    public void setNumAffilie(java.lang.String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumCaisseAvs(java.lang.String numCaisseAvs) {
        this.numCaisseAvs = numCaisseAvs;
    }

    public void setTaux(java.lang.String taux) {
        this.taux = taux;
    }
}
