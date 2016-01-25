/*
 * Créé le 15 nov. 07
 */
package globaz.corvus.db.attestationsFiscales;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr
 * 
 * 
 * 
 */

public class REAttestationFiscaleDecisionManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_TIERS = "HTITIE";
    public static final String FIELDNAME_NSS = "HVNAVS";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    // Format : mm.aaaa
    private String forCodesPrestationsIn = "";
    private String forCsGenreRenteAccodeeNotIn = "";

    private String forDepuisValidation = "";
    private String forEtatDecisions = "";
    private String forIdsRentesAccordeesIn = "";
    private String fornss = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * SELECT ciciweb.repracc.ztlcpr, ciciweb.repracc.ZTDFDR FROM ciciweb.repracc INNER JOIN ciciweb.TIHAVSP ON
     * CICIWEB.REPRACC.ZTITBE = ciciweb.TIHAVSP.HTITIE inner join ciciweb.REPRSDU on ciciweb.REPRACC.ZTIPRA =
     * ciciweb.REPRSDU.YMIRAC inner join ciciweb.REVALDEC on ciciweb.REPRSDU.YMIPRD = ciciweb.REVALDEC.YVIPRD inner join
     * ciciweb.REDECIS on ciciweb.REDECIS.YWIDEC = ciciweb.REVALDEC.YVIDEC WHERE (ciciweb.TIHAVSP.HVNAVS like 'forNss')
     * AND CICIWEB.REPRACC.ZTLCPR IN ( forCodesPrestationsIn ) AND CICIWEB.REDECIS.YWTETA IN (52837003 ) ORDER BY YWDVAL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getSql(BStatement statement) {

        String INNER_JOIN = " INNER JOIN ";
        String LEFT_JOIN = " LEFT OUTER JOIN ";
        String ON = " ON ";
        String OR = " OR ";
        String AND = " AND ";
        String EGAL = " = ";
        String BETWEEN = " BETWEEN ";
        String POINT = ".";
        String AS = " AS ";

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");

        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" ");
        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

        sql.append(INNER_JOIN).append(_getCollection()).append(REAttestationFiscaleDecisionManager.TABLE_AVS_HISTO);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection()).append(REAttestationFiscaleDecisionManager.TABLE_AVS_HISTO)
                .append(POINT).append(REAttestationFiscaleDecisionManager.FIELDNAME_ID_TIERS);

        sql.append(INNER_JOIN).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        sql.append(EGAL).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                .append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(INNER_JOIN).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        sql.append(ON).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                .append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);
        sql.append(EGAL).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION)
                .append(POINT).append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE);

        sql.append(INNER_JOIN).append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS);
        sql.append(ON).append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(POINT)
                .append(REDecisionEntity.FIELDNAME_ID_DECISION);
        sql.append(EGAL).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION)
                .append(POINT).append(REValidationDecisions.FIELDNAME_ID_DECISION);

        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isBlankOrZero(getFornss())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            } else {
                whereClause.append(" WHERE ");
            }
            whereClause.append(_getCollection()).append(REAttestationFiscaleDecisionManager.TABLE_AVS_HISTO)
                    .append(POINT).append(REAttestationFiscaleDecisionManager.FIELDNAME_NSS);
            whereClause.append(EGAL).append("'" + fornss + "'");

        }

        if (!JadeStringUtil.isBlankOrZero(getForCodesPrestationsIn())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            } else {
                whereClause.append(" WHERE ");
            }

            whereClause.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" IN ( ");
            whereClause.append(getForCodesPrestationsIn()).append(") ");
        }

        if (!JadeStringUtil.isEmpty(getForEtatDecisions())) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            } else {
                whereClause.append(" WHERE ");
            }

            whereClause.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(POINT)
                    .append(REDecisionEntity.FIELDNAME_ETAT);
            whereClause.append(" IN (" + forEtatDecisions + ")");

        }

        sql.append(whereClause.toString());

        sql.append(" ORDER BY ");
        sql.append(REDecisionEntity.FIELDNAME_DATE_VALIDATION);

        return sql.toString();

    }

    @Override
    protected java.lang.String _getSqlCount(BStatement statement) {

        String INNER_JOIN = " INNER JOIN ";
        String LEFT_JOIN = " LEFT OUTER JOIN ";
        String ON = " ON ";
        String OR = " OR ";
        String AND = " AND ";
        String EGAL = " = ";
        String BETWEEN = " BETWEEN ";
        String POINT = ".";
        String AS = " AS ";

        StringBuffer sql = new StringBuffer();
        try {
            sql.append("SELECT COUNT(").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(") ");

            sql.append(" FROM ");
            sql.append(_getCollection());
            sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

            sql.append(INNER_JOIN).append(_getCollection()).append(REAttestationFiscaleDecisionManager.TABLE_AVS_HISTO);
            sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sql.append(EGAL).append(_getCollection()).append(REAttestationFiscaleDecisionManager.TABLE_AVS_HISTO)
                    .append(POINT).append(REAttestationFiscaleDecisionManager.FIELDNAME_ID_TIERS);

            sql.append(INNER_JOIN).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
            sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
            sql.append(EGAL).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                    .append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);

            sql.append(INNER_JOIN).append(_getCollection())
                    .append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
            sql.append(ON).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                    .append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);
            sql.append(EGAL).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION)
                    .append(POINT).append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE);

            sql.append(INNER_JOIN).append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS);
            sql.append(ON).append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(POINT)
                    .append(REDecisionEntity.FIELDNAME_ID_DECISION);
            sql.append(EGAL).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION)
                    .append(POINT).append(REValidationDecisions.FIELDNAME_ID_DECISION);

            StringBuffer whereClause = new StringBuffer();

            if (!JadeStringUtil.isBlankOrZero(getFornss())) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                } else {
                    whereClause.append(" WHERE ");
                }
                whereClause.append(_getCollection()).append(REAttestationFiscaleDecisionManager.TABLE_AVS_HISTO)
                        .append(POINT).append(REAttestationFiscaleDecisionManager.FIELDNAME_NSS);
                whereClause.append(EGAL).append("'" + fornss + "'");

            }

            if (!JadeStringUtil.isBlankOrZero(getForCodesPrestationsIn())) {
                if (whereClause.length() > 0) {
                    whereClause.append(" AND ");
                } else {
                    whereClause.append(" WHERE ");
                }

                whereClause.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(POINT).append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" IN ( ");
                whereClause.append(getForCodesPrestationsIn()).append(") ");
            }

            if (!JadeStringUtil.isEmpty(getForEtatDecisions())) {
                if (whereClause.length() != 0) {
                    whereClause.append(" AND ");
                } else {
                    whereClause.append(" WHERE ");
                }

                whereClause.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(POINT)
                        .append(REDecisionEntity.FIELDNAME_ETAT);
                whereClause.append(" IN (" + forEtatDecisions + ")");

            }

            if (!JadeStringUtil.isEmpty(forCsGenreRenteAccodeeNotIn)) {
                if (whereClause.length() != 0) {
                    whereClause.append(" AND ");
                } else {
                    whereClause.append(" WHERE ");
                }

                whereClause.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(POINT).append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
                whereClause.append(" NOT IN (" + forCsGenreRenteAccodeeNotIn + ")");
            }

            if (!JadeStringUtil.isEmpty(forIdsRentesAccordeesIn)) {
                if (whereClause.length() != 0) {
                    whereClause.append(" AND ");
                } else {
                    whereClause.append(" WHERE ");
                }

                whereClause.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
                whereClause.append(" IN (" + forIdsRentesAccordeesIn + ")");

            }

            if (!JadeStringUtil.isEmpty(forDepuisValidation)) {
                if (whereClause.length() != 0) {
                    whereClause.append(" AND ");
                } else {
                    whereClause.append(" WHERE ");
                }

                whereClause.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(POINT)
                        .append(REDecisionEntity.FIELDNAME_DATE_VALIDATION);

                whereClause.append(">="
                        + this._dbWriteNumeric(statement.getTransaction(),
                                PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(forDepuisValidation)));

            }

            sql.append(whereClause.toString());

        } catch (JAException e) {
            getSession().addError(e.getMessage());
        }
        return sql.toString();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAttestationFiscaleDecision();
    }

    public String getForCodesPrestationsIn() {
        return forCodesPrestationsIn;
    }

    public String getForCsGenreRenteAccodeeNotIn() {
        return forCsGenreRenteAccodeeNotIn;
    }

    public String getForDepuisValidation() {
        return forDepuisValidation;
    }

    public String getForEtatDecisions() {
        return forEtatDecisions;
    }

    public String getForIdsRentesAccordeesIn() {
        return forIdsRentesAccordeesIn;
    }

    public String getFornss() {
        return fornss;
    }

    public void setForCodesPrestationsIn(String forCodesPrestationsIn) {
        this.forCodesPrestationsIn = forCodesPrestationsIn;
    }

    public void setForCsGenreRenteAccodeeNotIn(String forCsGenreRenteAccodeeNotIn) {
        this.forCsGenreRenteAccodeeNotIn = forCsGenreRenteAccodeeNotIn;
    }

    public void setForDepuisValidation(String forDepuisValidation) {
        this.forDepuisValidation = forDepuisValidation;
    }

    public void setForEtatDecisions(String forEtatDecisions) {
        this.forEtatDecisions = forEtatDecisions;
    }

    public void setForIdsRentesAccordeesIn(String forIdsRentesAccordeesIn) {
        this.forIdsRentesAccordeesIn = forIdsRentesAccordeesIn;
    }

    public void setFornss(String fornss) {
        this.fornss = fornss;
    }
}
