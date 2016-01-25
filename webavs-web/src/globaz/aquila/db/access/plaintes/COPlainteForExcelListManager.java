package globaz.aquila.db.access.plaintes;

import globaz.aquila.application.COApplication;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.external.IntRole;
import globaz.osiris.translation.CACodeSystem;
import globaz.webavs.common.CommonProperties;

public class COPlainteForExcelListManager extends COBManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected final static String ALIAS_DESCRIPTION_ADMIN = "ADMINDESC";
    protected final static String ALIAS_ID_COMPTE_ANNEXE_ADMIN = "IDADMIN";
    protected final static String ALIAS_ID_EXTERNEROLE_ADMIN = "ADMIN";
    protected final static String ALIAS_ID_EXTERNEROLE_ALPHA = "A";
    protected final static String ALIAS_ID_EXTERNEROLE_SOCIETE = "SOCIETE";
    protected final static String ALIAS_SOCIETE_DESCRIPTION = "SOCIETEDESC";
    protected final static String ALIAS_TABLE_COPLAIP = "PL";
    protected final static String ALIAS_UNDER_QUERY = "SR";
    protected final static String FIRST_CACPTAP = "ALPHA";
    protected final static String POINT = ".";
    protected final static String SECOND_CACPTAP = "BRAVO";
    protected final static String VIRGULE = ",";

    private String datePlainte = "";
    private String description = "";
    int intervalDroit = 0;
    private String motif = "";
    private String nomAdmin = "";
    private String numAdmin = "";
    private String periodeAu = "";
    private String periodeDu = "";
    private String societe = "";
    private String type = "";

    private String withoutEndDate = "";

    /**
	 *
	 */
    public COPlainteForExcelListManager() {
        super();

        intervalDroit = giveSizeNumAffilie();
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();
        addField(sqlFields, COPlainteForExcelListManager.ALIAS_ID_EXTERNEROLE_ADMIN);
        addField(sqlFields, COPlainteForExcelListManager.ALIAS_DESCRIPTION_ADMIN);
        addField(sqlFields, CACompteAnnexe.FIELD_IDEXTERNEROLE + COBManager.AS_FIELD
                + COPlainteForExcelListManager.ALIAS_ID_EXTERNEROLE_SOCIETE);
        addField(sqlFields, CACompteAnnexe.FIELD_DESCRIPTION + COBManager.AS_FIELD
                + COPlainteForExcelListManager.ALIAS_SOCIETE_DESCRIPTION);
        addField(sqlFields, COPlainteForExcelListManager.ALIAS_TABLE_COPLAIP + COPlainteForExcelListManager.POINT + "*");
        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(_getCollection() + CACompteAnnexe.TABLE_CACPTAP);
        addTableLink(from);
        return from.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer _order = new StringBuffer();

        _order.append(COPlainteForExcelListManager.ALIAS_ID_EXTERNEROLE_SOCIETE);
        _order.append(COPlainteForExcelListManager.VIRGULE);
        _order.append(COPlainteForExcelListManager.ALIAS_ID_EXTERNEROLE_ADMIN);
        return _order.toString();
    }

    protected String _getTableName() {
        return _getCollection() + CACompteAnnexe.TABLE_CACPTAP;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer("");

        sqlWhere.append(CACompteAnnexe.FIELD_IDROLE);
        sqlWhere.append(COBManager.NOT_IN);
        sqlWhere.append("(" + IntRole.ROLE_ADMINISTRATEUR + COPlainteForExcelListManager.VIRGULE + IntRole.ROLE_AFFILIE
                + ")");
        sqlWhere.append(COBManager.AND);
        sqlWhere.append(CACompteAnnexe.FIELD_IDGENRECOMPTE);
        sqlWhere.append(COBManager.DIFFERENT);
        sqlWhere.append(CACodeSystem.COMPTE_AUXILIAIRE);

        if (!JadeStringUtil.isBlank(getPeriodeDu())) {
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COPlainteForExcelList.FIELD_PERIODE_DU);
            sqlWhere.append(COBManager.PLUS_GRAND_EGAL);
            sqlWhere.append(JACalendar.format(getPeriodeDu(), JACalendar.FORMAT_YYYYMMDD));
        }
        if (!JadeStringUtil.isBlank(getPeriodeAu())) {
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COPlainteForExcelList.FIELD_PERIODE_AU);
            sqlWhere.append(COBManager.PLUS_PETIT_EGAL);
            sqlWhere.append(JACalendar.format(getPeriodeAu(), JACalendar.FORMAT_YYYYMMDD));
        }
        if ("on".equals(getWithoutEndDate())) {
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COPlainteForExcelList.FIELD_PERIODE_AU);
            sqlWhere.append(COBManager.IS_NULL);
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COPlainteForExcelList();
    }

    /**
     * @param fields
     * @param columnName
     * @return
     */
    private String addField(StringBuffer fields, String columnName) {
        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(columnName);

        return fields.toString();
    }

    /**
     * ajoute les jointures
     * 
     * @param buffer
     */
    protected void addTableLink(StringBuffer buffer) {
        buffer.append(COBManager.INNER_JOIN);
        addUnderQuery(buffer);
        buffer.append(COBManager.ON);
        buffer.append(COPlainteForExcelListManager.ALIAS_UNDER_QUERY + COPlainteForExcelListManager.POINT
                + COPlainteForExcelListManager.ALIAS_ID_EXTERNEROLE_ALPHA);
        buffer.append(COBManager.EGAL);
        buffer.append(COBManager.SUBSTRING + "(");
        buffer.append(_getTableName() + COPlainteForExcelListManager.POINT + CACompteAnnexe.FIELD_IDEXTERNEROLE);
        buffer.append(COPlainteForExcelListManager.VIRGULE + "1");
        buffer.append(COPlainteForExcelListManager.VIRGULE + intervalDroit + ")");
        buffer.append(COBManager.INNER_JOIN);
        buffer.append(_getCollection() + COPlaintePenale.TABLE_NAME + " "
                + COPlainteForExcelListManager.ALIAS_TABLE_COPLAIP);
        buffer.append(COBManager.ON);
        buffer.append(COPlaintePenale.FIELDNAME_IDCOMPTEAUXILIAIRE + COBManager.EGAL
                + COPlainteForExcelListManager.ALIAS_ID_COMPTE_ANNEXE_ADMIN);
    }

    /**
     * Permet d'ajouter la sous-requete
     * 
     * @param buffer
     */
    protected void addUnderQuery(StringBuffer buffer) {
        buffer.append("(");
        buffer.append(COBManager.SELECT);
        buffer.append(COBManager.SUBSTRING);
        buffer.append("(");
        buffer.append(COPlainteForExcelListManager.SECOND_CACPTAP + COPlainteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_IDEXTERNEROLE);
        buffer.append(COPlainteForExcelListManager.VIRGULE);
        buffer.append("1");
        buffer.append(COPlainteForExcelListManager.VIRGULE);
        buffer.append(intervalDroit);
        buffer.append(")" + COBManager.AS_FIELD + COPlainteForExcelListManager.ALIAS_ID_EXTERNEROLE_ALPHA);
        buffer.append(COPlainteForExcelListManager.VIRGULE);
        buffer.append(COPlainteForExcelListManager.SECOND_CACPTAP + COPlainteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_IDEXTERNEROLE + COBManager.AS_FIELD
                + COPlainteForExcelListManager.ALIAS_ID_EXTERNEROLE_ADMIN);
        buffer.append(COPlainteForExcelListManager.VIRGULE);
        buffer.append(COPlainteForExcelListManager.SECOND_CACPTAP + COPlainteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_DESCRIPTION + COBManager.AS_FIELD
                + COPlainteForExcelListManager.ALIAS_DESCRIPTION_ADMIN);
        buffer.append(COPlainteForExcelListManager.VIRGULE);
        buffer.append(COPlainteForExcelListManager.SECOND_CACPTAP + COPlainteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + COBManager.AS_FIELD
                + COPlainteForExcelListManager.ALIAS_ID_COMPTE_ANNEXE_ADMIN);
        buffer.append(COBManager.FROM);
        buffer.append(_getTableName() + " " + COPlainteForExcelListManager.SECOND_CACPTAP);
        addWhereForUnderQuery(buffer);
    }

    /**
     * ajoute la clause where de la sous-requete
     * 
     * @param buffer
     */
    private void addWhereForUnderQuery(StringBuffer buffer) {
        buffer.append(COBManager.WHERE);
        buffer.append(COPlainteForExcelListManager.SECOND_CACPTAP + COPlainteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_IDROLE);
        buffer.append(COBManager.IN);
        buffer.append("(" + IntRole.ROLE_ADMINISTRATEUR + COPlainteForExcelListManager.VIRGULE + IntRole.ROLE_AFFILIE
                + ")");
        buffer.append(COBManager.AND);
        buffer.append(COPlainteForExcelListManager.SECOND_CACPTAP + COPlainteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_IDGENRECOMPTE + COBManager.EGAL + CACodeSystem.COMPTE_AUXILIAIRE);
        buffer.append(") " + COPlainteForExcelListManager.ALIAS_UNDER_QUERY);
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

    public String getWithoutEndDate() {
        return withoutEndDate;
    }

    /**
     * @return
     */
    private int giveSizeNumAffilie() {
        // Placer cette méthode dans COApplication ?
        int size = 0;

        String format = CAApplication.getApplicationOsiris().getCAParametres().getFormatAdminNumAffilie();
        size = format.split("-")[0].length();

        if (size == 0) {
            size = Integer.parseInt(COApplication.getApplicationAquila().getProperty(
                    CommonProperties.KEY_TAILLE_CHAMPS_AFF));
        }

        return size;
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

    public void setWithoutEndDate(String withoutEndDate) {
        this.withoutEndDate = withoutEndDate;
    }
}
