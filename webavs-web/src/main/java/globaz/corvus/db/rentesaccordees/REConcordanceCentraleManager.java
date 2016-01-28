/*
 * Créé le 15 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr
 * 
 *         Manager pour l'envoi des annonces test (46 + 43) pour concordance avec la centrale
 */

public class REConcordanceCentraleManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IDTIERS = "HTITIE";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";

    /** DOCUMENT ME! */
    public static final String TABLE_AVS = "TIPAVSP";

    // Format : mm.aaaa
    private String forDate = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * selectionne en une seule requete toutes les infos necessaires au versement des rentes
     * 
     * <p>
     * redefini car la requete est un peu compliquee.
     * </p>
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

        /*
         * select YLIRAC, YLIBAC, ZTITBE, ZTLCPR, ZTMPRE, YILDAP, HXNAVS FROM CICIWEB.REREACC
         * 
         * INNER JOIN CICIWEB.REPRACC ON CICIWEB.REPRACC.ZTIPRA=CICIWEB.REREACC.YLIRAC INNER JOIN CICIWEB.TIPAVSP ON
         * CICIWEB.REPRACC.ZTITBE=CICIWEB.TIPAVSP.HTITIE INNER JOIN CICIWEB.REBACAL ON
         * CICIWEB.REBACAL.YIIBCA=CICIWEB.REREACC.YLIBAC INNER JOIN CICIWEB.REPRSDU ON
         * CICIWEB.REPRACC.ZTIPRA=CICIWEB.REPRSDU.YMIRAC INNER JOIN CICIWEB.REVALDEC ON
         * CICIWEB.REPRSDU.YMIPRD=CICIWEB.REVALDEC.YVIPRD INNER JOIN CICIWEB.REDECIS ON
         * CICIWEB.REVALDEC.YVIDEC=CICIWEB.REDECIS.YWIDEC WHERE ZTDDDR<=201004 AND (ZTDFDR = 0 OR ZTDFDR >= 201004 OR
         * ZTDFDR IS NULL ) AND YWDDEC <= 20100401 group by YLIRAC, YLIBAC, ZTITBE, ZTLCPR, ZTMPRE, YILDAP, HXNAVS order
         * by YLIRAC
         */

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(", ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_DROIT_APPLIQUE).append(", ");
        sql.append(_getCollection()).append(TABLE_AVS).append(POINT).append(FIELDNAME_NUM_AVS);

        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);

        sql.append(INNER_JOIN).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        sql.append(EGAL).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(INNER_JOIN).append(_getCollection()).append(TABLE_AVS);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection()).append(TABLE_AVS).append(POINT).append(FIELDNAME_IDTIERS);

        sql.append(INNER_JOIN).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(ON).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        sql.append(EGAL).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        sql.append(INNER_JOIN).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        sql.append(ON).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                .append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append(EGAL).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(INNER_JOIN).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        sql.append(ON).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION)
                .append(POINT).append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE);
        sql.append(EGAL).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                .append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);

        sql.append(INNER_JOIN).append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS);
        sql.append(ON).append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(POINT)
                .append(REDecisionEntity.FIELDNAME_ID_DECISION);
        sql.append(EGAL).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION)
                .append(POINT).append(REValidationDecisions.FIELDNAME_ID_DECISION);

        sql.append(" WHERE ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" <= ")
                .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDate()));

        sql.append(AND);

        sql.append(" ( ").append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" >= ")
                .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDate())).append(OR).append(_getCollection())
                .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(EGAL).append(" 0 ").append(OR)
                .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL )");

        sql.append(AND);

        sql.append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(POINT)
                .append(REDecisionEntity.FIELDNAME_DATE_DECISION).append(" <= ")
                .append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDate()) + "01");

        sql.append(" GROUP BY ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(", ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_DROIT_APPLIQUE).append(", ");
        sql.append(_getCollection()).append(TABLE_AVS).append(POINT).append(FIELDNAME_NUM_AVS);

        sql.append(" ORDER BY ");
        sql.append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        return sql.toString();
    }

    @Override
    protected java.lang.String _getSqlCount(BStatement statement) {
        _addError(statement.getTransaction(), "Unsuported function. Use RECountManager.getCount() method instead");
        return null;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REConcordanceCentrale();
    }

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

}
