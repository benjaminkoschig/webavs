package globaz.naos.db.listeAgenceCommunale;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class AFListeAgenceCommunale extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static java.lang.String TABLE_FIELDS = "AFAFFIP.MALNAF, AFAFFIP.MAIAFF, TITIERP.HTITIE, TITIERP.HTLDE1, TITIERP.HTLDE2, AFAFFIP.MADDEB, AFAFFIP.MADFIN, AFAFFIP.MATTAF, AFAFFIP.MABTRA, TITIER2.HTITIE AS IDTIERSADM, TITIER2.HTLDE1 AS NOMADM, TITIER2.HTLDE2 AS PRENOMADM, TITIER2.HTTLAN AS LANGUEADM, TIADMIP.HBCADM AS CODEADM, TICTIEP.HGDDRE, TICTIEP.HGDFRE";

    private java.lang.String codeAdministration = new String();
    private java.lang.String dateDebutAffiliation = new String();
    private java.lang.String dateFinAffiliation = new String();
    private java.lang.String genreAffiliation = new String();
    private java.lang.String idAffiliation = new String();

    private java.lang.String idTiers = new String();

    private java.lang.String idTiersAdministration = new String();
    private java.lang.String langueAdministration = new String();
    private java.lang.String nom = new String();

    private java.lang.String nomAdministration1 = new String();

    private java.lang.String nomAdministration2 = new String();
    private java.lang.String numAffilie = new String();
    private java.lang.String prenom = new String();
    private Boolean provisoire = new Boolean(false);

    public AFListeAgenceCommunale() {
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
        numAffilie = statement.dbReadString("MALNAF");
        dateDebutAffiliation = statement.dbReadNumeric("MADDEB");
        dateFinAffiliation = statement.dbReadNumeric("MADFIN");
        genreAffiliation = statement.dbReadNumeric("MATTAF");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idTiers = statement.dbReadNumeric("HTITIE");
        idTiersAdministration = statement.dbReadNumeric("IDTIERSADM");
        nomAdministration1 = statement.dbReadString("NOMADM");
        nomAdministration2 = statement.dbReadString("PRENOMADM");
        langueAdministration = statement.dbReadNumeric("LANGUEADM");
        codeAdministration = statement.dbReadString("CODEADM");
        nom = statement.dbReadString("HTLDE1");
        prenom = statement.dbReadString("HTLDE2");
        provisoire = statement.dbReadBoolean("MABTRA");
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

    public java.lang.String getCodeAdministration() {
        return codeAdministration;
    }

    public java.lang.String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public java.lang.String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public java.lang.String getDateFormate(String date) {
        String dateFormate = "";
        if (!JadeStringUtil.isBlankOrZero(date)) {
            dateFormate = date.substring(6, 8) + "." + date.substring(4, 6) + "." + date.substring(0, 4);
        }
        return dateFormate;
    }

    public java.lang.String getGenreAffiliation() {
        return genreAffiliation;
    }

    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    public java.lang.String getIdTiers() {
        return idTiers;
    }

    public java.lang.String getIdTiersAdministration() {
        return idTiersAdministration;
    }

    public java.lang.String getLangueAdministration() {
        return langueAdministration;
    }

    public java.lang.String getNom() {
        return nom;
    }

    public java.lang.String getNomAdministration1() {
        return nomAdministration1;
    }

    public java.lang.String getNomAdministration2() {
        return nomAdministration2;
    }

    public java.lang.String getNumAffilie() {
        return numAffilie;
    }

    public java.lang.String getPrenom() {
        return prenom;
    }

    public Boolean getProvisoire() {
        return provisoire;
    }

    public void setCodeAdministration(java.lang.String codeAdministration) {
        this.codeAdministration = codeAdministration;
    }

    public void setDateDebutAffiliation(java.lang.String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateFinAffiliation(java.lang.String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setGenreAffiliation(java.lang.String genreAffiliation) {
        this.genreAffiliation = genreAffiliation;
    }

    public void setIdAffiliation(java.lang.String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdTiers(java.lang.String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdministration(java.lang.String idTiersAdministration) {
        this.idTiersAdministration = idTiersAdministration;
    }

    public void setLangueAdministration(java.lang.String langueAdministration) {
        this.langueAdministration = langueAdministration;
    }

    public void setNom(java.lang.String nom) {
        this.nom = nom;
    }

    public void setNomAdministration1(java.lang.String nomAdministration1) {
        this.nomAdministration1 = nomAdministration1;
    }

    public void setNomAdministration2(java.lang.String nomAdministration2) {
        this.nomAdministration2 = nomAdministration2;
    }

    public void setNumAffilie(java.lang.String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setPrenom(java.lang.String prenom) {
        this.prenom = prenom;
    }

    public void setProvisoire(Boolean provisoire) {
        this.provisoire = provisoire;
    }

}
