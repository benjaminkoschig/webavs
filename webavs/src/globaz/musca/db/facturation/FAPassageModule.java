package globaz.musca.db.facturation;

import globaz.globall.util.JACalendar;

public class FAPassageModule extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String datePeriode = new String();
    private java.lang.Boolean estGenere = new Boolean(false);
    private java.lang.String idAction = new String();
    private java.lang.String idModuleFacturation = new String();
    private String idPassage = new String();
    private java.lang.String idTypeModule = new String();
    private String status = new String();

    /**
     * Commentaire relatif au constructeur FAModulePassage
     */
    public FAPassageModule() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idModuleFacturation = statement.dbReadNumeric("IDMODFAC");
        estGenere = statement.dbReadBoolean("ESTGENERE");
        idAction = statement.dbReadNumeric("IDACTION");
        idPassage = statement.dbReadNumeric("IDPASSAGE");
        idTypeModule = statement.dbReadString("IDTYPEMODULE");
        datePeriode = statement.dbReadDateAMJ("DATEPERIODE", JACalendar.FORMAT_MMsYYYY);
        status = statement.dbReadNumeric("STATUS");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    public String getDatePeriode() {
        return datePeriode;
    }

    public java.lang.String getIdAction() {
        return idAction;
    }

    public java.lang.String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public String getIdPassage() {
        return idPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2003 15:13:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTypeModule() {
        return idTypeModule;
    }

    public String getStatus() {
        return status;
    }

    public java.lang.Boolean isEstGenere() {
        return estGenere;
    }

    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }

    public void setEstGenere(java.lang.Boolean newEstGenere) {
        estGenere = newEstGenere;
    }

    public void setIdAction(java.lang.String newIdAction) {
        idAction = newIdAction;
    }

    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation) {
        idModuleFacturation = newIdModuleFacturation;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2003 15:13:58)
     * 
     * @param newIdTypeModule
     *            java.lang.String
     */
    public void setIdTypeModule(java.lang.String newIdTypeModule) {
        idTypeModule = newIdTypeModule;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
