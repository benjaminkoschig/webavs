package globaz.corvus.db.attestationsFiscales;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class REAttestationFiscaleRentAccordOrdreVerseManager extends BManager {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME_PRESTATIONS_DUES = "REPRSDU";
    public static final String TABLE_NAME_VALIDATION_DECISION = "REVALDEC";
    public static final String TABLE_NAME_PRESTATION = "REPREST";
    public static final String TABLE_NAME_ORDRE_VERSEMENT = "REORVER";

    public static final String FIELDNAME_CS_TYPE_PRESTATIONS_DUE = "YMTTYP";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE_PRESTATIONS_DUE = "YMIRAC";
    public static final String FIELDNAME_ORDRE_VERSEMENT_MONTANT_DETTE = "YVMMAC";
    public static final String FIELDNAME_TYPE_ORDRE_VERSEMENT = "YVTTYP";

    public static final String FIELDNAME_ID_PRESTATION_DUE_PRESTATION_DUE = "YMIPRD";
    public static final String FIELDNAME_ID_PRESTATION_DUE_VALIDATION_DECISION = "YVIPRD";

    public static final String FIELDNAME_ID_DECISION_PRESTATION = "YUIDEC";
    public static final String FIELDNAME_ID_DECISION_VALIDATION_DECISION = "YVIDEC";

    public static final String FIELDNAME_ID_PRESTATION_PRESTATION = "YUIPRE";
    public static final String FIELDNAME_ID_PRESTATION_ORDRE_VERSEMENT = "YVIPRE";

    private String forIdRenteAccordee = "";
    private String forCsType = "";

    @Override
    protected String _getSql(BStatement statement) {
        String INNER_JOIN = " INNER JOIN ";
        String LEFT_JOIN = " LEFT OUTER JOIN ";
        String ON = " ON ";
        String OR = " OR ";
        String AND = " AND ";
        String EGAL = " = ";
        String POINT = ".";
        String AS = " AS ";

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATIONS_DUES)
                .append(POINT)
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_RENTE_ACCORDEE_PRESTATIONS_DUE)
                .append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATIONS_DUES)
                .append(POINT).append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_CS_TYPE_PRESTATIONS_DUE)
                .append(", ");
        ;
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_ORDRE_VERSEMENT)
                .append(POINT)
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ORDRE_VERSEMENT_MONTANT_DETTE)
                .append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_ORDRE_VERSEMENT)
                .append(POINT).append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_TYPE_ORDRE_VERSEMENT);
        sql.append(" FROM ");
        sql.append(_getCollection())
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATIONS_DUES);

        sql.append(INNER_JOIN).append(_getCollection())
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_VALIDATION_DECISION).append(ON);
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATIONS_DUES)
                .append(POINT)
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_PRESTATION_DUE_PRESTATION_DUE);
        sql.append(EGAL);
        sql.append(_getCollection())
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_VALIDATION_DECISION).append(POINT)
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_PRESTATION_DUE_VALIDATION_DECISION);

        sql.append(INNER_JOIN).append(_getCollection())
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATION).append(ON);
        sql.append(_getCollection())
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_VALIDATION_DECISION).append(POINT)
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_DECISION_VALIDATION_DECISION);
        sql.append(EGAL);
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATION)
                .append(POINT).append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_DECISION_PRESTATION);

        sql.append(INNER_JOIN).append(_getCollection())
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_ORDRE_VERSEMENT).append(ON);
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATION)
                .append(POINT)
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_PRESTATION_PRESTATION);
        sql.append(EGAL);
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_ORDRE_VERSEMENT)
                .append(POINT)
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_PRESTATION_ORDRE_VERSEMENT);

        sql.append(" WHERE ");
        StringBuffer whereClause = new StringBuffer();
        if (!JadeStringUtil.isBlankOrZero(getForIdRenteAccordee())) {
            if (whereClause.length() > 0) {
                whereClause.append(AND);
            }

            whereClause.append(_getCollection())
                    .append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                    .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_RENTE_ACCORDEE_PRESTATIONS_DUE)
                    .append(" IN ( ");
            whereClause.append(getForIdRenteAccordee()).append(") ");
        }
        if (!JadeStringUtil.isBlankOrZero(getForCsType())) {
            if (whereClause.length() > 0) {
                whereClause.append(AND);
            }

            whereClause.append(_getCollection())
                    .append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                    .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_CS_TYPE_PRESTATIONS_DUE)
                    .append(" IN ( ");
            whereClause.append(getForCsType()).append(") ");
        }
        sql.append(whereClause.toString());
        sql.append(" ORDER BY ");
        sql.append(_getCollection()).append(REAttestationFiscaleRentAccordOrdreVerseManager.TABLE_NAME_PRESTATIONS_DUES)
                .append(POINT)
                .append(REAttestationFiscaleRentAccordOrdreVerseManager.FIELDNAME_ID_RENTE_ACCORDEE_PRESTATIONS_DUE);

        return sql.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAttestationFiscaleRentAccordOrdreVerse();
    }

    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public void setForIdRenteAccordee(String forIdRenteAccordee) {
        this.forIdRenteAccordee = forIdRenteAccordee;
    }

    public String getForCsType() {
        return forCsType;
    }

    public void setForCsType(String forCsType) {
        this.forCsType = forCsType;
    }

}
