package globaz.osiris.db.print;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.osiris.db.suiviprocedure.CAFaillite;

public class CAFailliteForExcelList extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String FIELD_IDEXTERNEROLE = "IDEXTERNEROLE";
    public final static String FIELD_IDROLE = "IDROLE";
    public final static String FIELD_AFFILIATION_TYPE = AFAffiliation.FIELDNAME_AFFILIATION_TYPE;
    public final static String FIELD_DESCRIPTION = "DESCRIPTION";
    public final static String FIELD_DATE_FAILLITE = "DATEFAILLITE";
    public final static String FIELD_DATE_SUSPENSION = "DATESUSPENSION";
    public final static String FIELD_DATE_PRODUCTION = CAFaillite.FIELD_DATE_PRODUCTION;
    public final static String FIELD_DATE_PRODUCTION_DEFINITIVE = CAFaillite.FIELD_DATE_PRODUCTION_DEFINITIVE;
    public final static String FIELD_DATE_ANNULATION = CAFaillite.FIELD_DATE_ANNULATION_PRODUCTION;
    public final static String FIELD_DATE_REVOCATION = CAFaillite.FIELD_DATE_REVOCATION;
    public final static String FIELD_DATE_ETAT_COLLOC = "DATEETATCOLLOC";
    public final static String FIELD_DATE_MODIF_COLLOC = CAFaillite.FIELD_DATE_MODIFICATION_ETAT_COLLOCATION;
    public final static String FIELD_DATE_CLOTURE = CAFaillite.FIELD_DATE_CLOTURE_FAILLITE;
    public final static String FIELD_MONTANT_PRODUCTION = CAFaillite.FIELD_MONTANT_PRODUCTION;
    public final static String FIELD_COMMENTAIRE = "COMMENTAIRE";

    private String commentaire = "";
    private String dateEtatColloc = "";
    private String dateFaillite = "";
    private String dateSuspension = "";
    private String numAdmin = "";
    private String role = "";
    private String societe = "";
    private String typeAffiliation = "";
    private String dateProduction = "";
    private String dateProductionDefinitive = "";
    private String dateAnnulationProduction = "";
    private String dateRevocation = "";
    private String dateModificationCollocation = "";
    private String dateCloture = "";
    private String montantProduction = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAdmin = statement.dbReadString(CAFailliteForExcelList.FIELD_IDEXTERNEROLE);
        role = statement.dbReadString(CAFailliteForExcelList.FIELD_IDROLE);
        typeAffiliation = statement.dbReadString(CAFailliteForExcelList.FIELD_AFFILIATION_TYPE);
        societe = statement.dbReadString(CAFailliteForExcelList.FIELD_DESCRIPTION);
        dateFaillite = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_FAILLITE);
        dateProduction = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_PRODUCTION);
        dateProductionDefinitive = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_PRODUCTION_DEFINITIVE);
        dateAnnulationProduction = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_ANNULATION);
        dateRevocation = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_REVOCATION);
        dateSuspension = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_SUSPENSION);
        dateEtatColloc = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_ETAT_COLLOC);
        dateModificationCollocation = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_MODIF_COLLOC);
        dateCloture = statement.dbReadDateAMJ(CAFailliteForExcelList.FIELD_DATE_CLOTURE);
        montantProduction = statement.dbReadNumeric(CAFailliteForExcelList.FIELD_MONTANT_PRODUCTION);
        commentaire = statement.dbReadString(CAFailliteForExcelList.FIELD_COMMENTAIRE);
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

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public String getDateProduction() {
        return dateProduction;
    }

    public String getDateProductionDefinitive() {
        return dateProductionDefinitive;
    }

    public String getDateAnnulationProduction() {
        return dateAnnulationProduction;
    }

    public String getDateRevocation() {
        return dateRevocation;
    }

    public String getDateModificationCollocation() {
        return dateModificationCollocation;
    }

    public String getDateCloture() {
        return dateCloture;
    }

    public String getMontantProduction() {
        return montantProduction;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public void setDateProduction(String dateProduction) {
        this.dateProduction = dateProduction;
    }

    public void setDateProductionDefinitive(String dateProductionDefinitive) {
        this.dateProductionDefinitive = dateProductionDefinitive;
    }

    public void setDateAnnulationProduction(String dateAnnulationProduction) {
        this.dateAnnulationProduction = dateAnnulationProduction;
    }

    public void setDateRevocation(String dateRevocation) {
        this.dateRevocation = dateRevocation;
    }

    public void setDateModificationCollocation(String dateModificationCollocation) {
        this.dateModificationCollocation = dateModificationCollocation;
    }

    public void setDateCloture(String dateCloture) {
        this.dateCloture = dateCloture;
    }

    public void setMontantProduction(String montantProduction) {
        this.montantProduction = montantProduction;
    }

}
