package globaz.osiris.db.print;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.suiviprocedure.CAFaillite;

public class CAFailliteForExcelListManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ALIAS_TABLE_COMPTE_ANNEXE = "C";
    private static final String ALIAS_TABLE_FAILLITE = "F";
    private static final String ALIAS_TABLE_AFFILIATION = "AFF";
    protected static final String AND = " AND ";
    protected static final String AS_FIELD = " AS ";
    protected static final String BETWEEN = " BETWEEN ";
    private static final String CONDITION_DATE_CLOTURE = "00020000";

    protected static final String COUNT = "COUNT(*)";
    protected static final String DECIMAL = " DECIMAL ";

    protected static final String DIFFERENT = "<>";
    protected static final String EGAL = "=";
    protected static final String ESPACE = " ";
    public static final String FIELD_COMMENTAIRE = "COMMENTAIRE";
    protected static final String FROM = " FROM ";
    protected static final String GREATER_DB_OPERAND = " > ";
    protected static final String GROUP_BY = " GROUP BY ";
    protected static final String IN = " IN ";
    protected static final String INNER_JOIN = " INNER JOIN ";

    protected static final String IS_NOT_NULL = " IS NOT NULL ";
    protected static final String IS_NULL = " IS NULL ";
    protected static final String LEFT_JOIN = " LEFT JOIN ";
    protected static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";
    protected static final String LIKE = " LIKE ";
    protected static final String NOT_IN = " NOT IN ";
    protected static final String ON = " ON ";
    protected static final String OR = " OR ";
    protected static final String ORDER_BY = " ORDER BY ";

    protected static final String PLUS = "+";
    protected static final String PLUS_GRAND_EGAL = ">=";
    protected static final String PLUS_PETIT_EGAL = "<=";
    protected static final String POINT = ".";
    protected static final String SELECT = "SELECT ";
    protected static final String SMALLER_DB_OPERAND = " < ";
    protected static final String SUBSTRING = " SUBSTR ";
    private static final String TABLE_COMPTE_ANNEXE = "CACPTAP";
    private static final String TABLE_AFFILIATION = "AFAFFIP";
    private static final String TABLE_NAME = "CAFAILP";
    protected static final String UNION = " UNION ";
    protected static final String UNION_ALL = " UNION ALL ";

    protected static final String WHERE = " WHERE ";
    protected static final String ZERO = "0";

    private String forIdCategorie;
    private String forSelectionRole;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();
        addField(sqlFields, CACompteAnnexe.FIELD_IDEXTERNEROLE); // idexternerole
        addField(sqlFields, CACompteAnnexe.FIELD_IDROLE); // idexternerole
        addField(sqlFields, AFAffiliation.FIELDNAME_AFFILIATION_TYPE); // type affiliation
        addField(sqlFields, CACompteAnnexe.FIELD_DESCRIPTION); // description
        addField(sqlFields, CAFaillite.FIELD_DATE_FAILLITE); // date de la faillite
        addField(sqlFields, CAFaillite.FIELD_DATE_PRODUCTION); // date production
        addField(sqlFields, CAFaillite.FIELD_DATE_PRODUCTION_DEFINITIVE); // date production définitive
        addField(sqlFields, CAFaillite.FIELD_DATE_ANNULATION_PRODUCTION); // date annulation
        addField(sqlFields, CAFaillite.FIELD_DATE_REVOCATION); // date de révocation rétractation
        addField(sqlFields, CAFaillite.FIELD_DATE_SUSPENSION_FAILLITE); // date de suspension
        addField(sqlFields, CAFaillite.FIELD_DATE_ETAT_COLLOCATION); // date etat colloc
        addField(sqlFields, CAFaillite.FIELD_DATE_MODIFICATION_ETAT_COLLOCATION); // modif état colloc
        addField(sqlFields, CAFaillite.FIELD_DATE_CLOTURE_FAILLITE); // date cloture faillite
        addField(sqlFields, CAFaillite.FIELD_MONTANT_PRODUCTION); // montant production
        addField(sqlFields, CAFailliteForExcelListManager.FIELD_COMMENTAIRE); // commentaire
        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(_getTableName() + CAFailliteForExcelListManager.ESPACE
                + CAFailliteForExcelListManager.ALIAS_TABLE_FAILLITE);
        addTableLink(from);
        return from.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer order = new StringBuffer();

        order.append(CACompteAnnexe.FIELD_IDEXTERNEROLE);
        return order.toString();
    }

    protected String _getTableName() {
        return _getCollection() + CAFailliteForExcelListManager.TABLE_NAME;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append("(");
        sqlWhere.append(CAFailliteForExcelListManager.ALIAS_TABLE_FAILLITE + CAFailliteForExcelListManager.POINT
                + CAFaillite.FIELD_DATE_CLOTURE_FAILLITE + " = 0" + CAFailliteForExcelListManager.OR);
        sqlWhere.append(CAFailliteForExcelListManager.ALIAS_TABLE_FAILLITE + CAFailliteForExcelListManager.POINT
                + CAFaillite.FIELD_DATE_CLOTURE_FAILLITE);
        sqlWhere.append(CAFailliteForExcelListManager.PLUS + CAFailliteForExcelListManager.CONDITION_DATE_CLOTURE
                + CAFailliteForExcelListManager.ESPACE);
        sqlWhere.append(CAFailliteForExcelListManager.PLUS_GRAND_EGAL + CAFailliteForExcelListManager.ESPACE);
        sqlWhere.append(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD));
        sqlWhere.append(")");

        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            sqlWhere.append(CAFailliteForExcelListManager.AND);
            if (JadeStringUtil.contains(getForSelectionRole(), ",")) {
                sqlWhere.append(CACompteAnnexe.FIELD_IDROLE);
                sqlWhere.append(CAFailliteForExcelListManager.IN);
                sqlWhere.append("(");
                sqlWhere.append(getForSelectionRole());
                sqlWhere.append(")");
            } else {
                sqlWhere.append(CACompteAnnexe.FIELD_IDROLE);
                sqlWhere.append(CAFailliteForExcelListManager.EGAL);
                sqlWhere.append(getForSelectionRole());
            }
        }

        if (!JadeStringUtil.isBlank(getForIdCategorie())) {
            sqlWhere.append(CAFailliteForExcelListManager.AND);
            if (JadeStringUtil.contains(getForIdCategorie(), ",")) {
                sqlWhere.append(CACompteAnnexe.FIELD_IDCATEGORIE);
                sqlWhere.append(CAFailliteForExcelListManager.IN);
                sqlWhere.append("(");
                sqlWhere.append(getForIdCategorie());
                sqlWhere.append(")");
            } else {
                sqlWhere.append(CACompteAnnexe.FIELD_IDCATEGORIE);
                sqlWhere.append(CAFailliteForExcelListManager.EGAL);
                sqlWhere.append(getForIdCategorie());
            }
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAFailliteForExcelList();
    }

    private String addField(StringBuffer fields, String columnName) {
        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(columnName);

        return fields.toString();
    }

    private void addTableLink(StringBuffer buffer) {
        // Join de la table compte annexe
        buffer.append(CAFailliteForExcelListManager.INNER_JOIN);
        buffer.append(_getCollection() + CAFailliteForExcelListManager.TABLE_COMPTE_ANNEXE
                + CAFailliteForExcelListManager.ESPACE + CAFailliteForExcelListManager.ALIAS_TABLE_COMPTE_ANNEXE);
        buffer.append(CAFailliteForExcelListManager.ON);
        buffer.append(CAFailliteForExcelListManager.ALIAS_TABLE_FAILLITE + CAFailliteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        buffer.append(CAFailliteForExcelListManager.EGAL);
        buffer.append(CAFailliteForExcelListManager.ALIAS_TABLE_COMPTE_ANNEXE + CAFailliteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        // Join de la table affiliation
        buffer.append(CAFailliteForExcelListManager.INNER_JOIN);
        buffer.append(_getCollection() + CAFailliteForExcelListManager.TABLE_AFFILIATION
                + CAFailliteForExcelListManager.ESPACE + CAFailliteForExcelListManager.ALIAS_TABLE_AFFILIATION);
        buffer.append(CAFailliteForExcelListManager.ON);
        buffer.append(CAFailliteForExcelListManager.ALIAS_TABLE_AFFILIATION + CAFailliteForExcelListManager.POINT
                + AFAffiliation.FIELDNAME_TIER_ID);
        buffer.append(CAFailliteForExcelListManager.EGAL);
        buffer.append(CAFailliteForExcelListManager.ALIAS_TABLE_COMPTE_ANNEXE + CAFailliteForExcelListManager.POINT
                + CACompteAnnexe.FIELD_IDTIERS);
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

}
