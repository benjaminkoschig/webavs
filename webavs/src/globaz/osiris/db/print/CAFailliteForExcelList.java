package globaz.osiris.db.print;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CAFailliteForExcelList extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String FIELD_COMMENTAIRE = "COMMENTAIRE";
    public final static String FIELD_DATE_ETAT_COLLOC = "DATEETATCOLLOC";
    public final static String FIELD_DATE_FAILLITE = "DATEFAILLITE";
    public final static String FIELD_DATE_SUSPENSION = "DATESUSPENSION";
    public final static String FIELD_DESCRIPTION = "DESCRIPTION";
    public final static String FIELD_IDEXTERNEROLE = "IDEXTERNEROLE";
    public final static String FIELD_IDROLE = "IDROLE";

    private String commentaire = "";
    private String dateEtatColloc = "";
    private String dateFaillite = "";
    private String dateSuspension = "";
    private String numAdmin = "";
    private String role = "";
    private String societe = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAdmin = statement.dbReadString(CAFailliteForExcelList.FIELD_IDEXTERNEROLE);
        societe = statement.dbReadString(CAFailliteForExcelList.FIELD_DESCRIPTION);
        dateFaillite = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_FAILLITE);
        dateEtatColloc = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_ETAT_COLLOC);
        dateSuspension = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_SUSPENSION);
        commentaire = statement.dbReadString(CAFailliteForExcelList.FIELD_COMMENTAIRE);
        role = statement.dbReadString(CAFailliteForExcelList.FIELD_IDROLE);
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

    public String getCommentaire() {
        return commentaire;
    }

    public String getDateEtatColloc() {
        return dateEtatColloc;
    }

    public String getDateFaillite() {
        return dateFaillite;
    }

    public String getDateSuspension() {
        return dateSuspension;
    }

    public String getNumAdmin() {
        return numAdmin;
    }

    public String getRole() {
        return role;
    }

    public String getSociete() {
        return societe;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setDateEtatColloc(String dateEtatColloc) {
        this.dateEtatColloc = dateEtatColloc;
    }

    public void setDateFaillite(String dateFaillite) {
        this.dateFaillite = dateFaillite;
    }

    public void setDateSuspension(String dateSuspension) {
        this.dateSuspension = dateSuspension;
    }

    public void setNumAdmin(String numAdmin) {
        this.numAdmin = numAdmin;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

}
