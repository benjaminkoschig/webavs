package globaz.corvus.db.attestationsFiscales;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;

/**
 * @author SCR
 */
public class REAttestationFiscaleManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_ID_TIERS = "HTITIE";
    public static final String FIELDNAME_NSS = "HXNAVS";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";

    private boolean controleDateFinDansAnnee = true;
    // Format : mm.aaaa
    private String forAnnee = "";
    private String forCodesPrestationsIn = "";
    private String fornss = "";
    private String forNssA = "";
    private String forNssDe = "";

    /**
     * PAS A JOUR ! selectionne en une seule requete le maximum d'infos necessaires à la génération des attestations
     * fiscales
     * 
     * 
     * SELECT DISTINCT (ZTIPRA), YLIBAC, YIITBC, ZTTGEN, ZTITBE, ZTMPRE, ZTDDDR, ZTDFDR, ZZIEBK, CASE when YWDVAL >
     * ForAnnee.01.01 then 'CODE_4' when ZZIEBK is null then 'CODE_1' when ((ZZIEBK is not null) AND (ZZMBLK = ZZMDBK))
     * then 'CODE_2'
     * 
     * else 'CODE_3' end as CODE_BLOCAGE from cvciweb.REPRACC inner join cvciweb.REREACC on cvciweb.REPRACC.ZTIPRA =
     * cvciweb.REREACC.YLIRAC inner join cvciweb.REBACAL on cvciweb.REREACC.YLIBAC = cvciweb.REBACAL.YIIBCA inner join
     * cvciweb.REPRSDU on cvciweb.REPRACC.ZTIPRA = cvciweb.REPRSDU.YMIRAC inner join cvciweb.REVALDEC on
     * cvciweb.REPRSDU.YMIPRD = cvciweb.REVALDEC.YVIPRD inner join cvciweb.REDECIS on cvciweb.REDECIS.YWIDEC =
     * cvciweb.REVALDEC.YVIDEC left join cvciweb.REENTBLK on cvciweb.REENTBLK.ZZIEBK = cvciweb.REPRACC.ZTIEBK WHERE
     * ZTLCPR in ('10', '13', '20', '23', '50', '70', '72') -- toutes les RA actives au 31.12 de l'année AND ZTDDDR <=
     * 200912 AND ( ZTDFDR = 0 OR ZTDFDR IS NULL OR ZTDFDR >=200912) ORDER BY cvciweb.REPRACC.ZTIPRA;
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

        sql.append("SELECT DISTINCT (");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("), ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA).append(", ");
        sql.append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(", ");
        sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT)
                .append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleManager.TABLE_PERSONNE).append(POINT)
                .append(REAttestationFiscaleManager.FIELDNAME_DATEDECES).append(", ");
        sql.append(_getCollection()).append(REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE).append(POINT)
                .append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE).append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleManager.TABLE_PERSONNE).append(POINT)
                .append(REAttestationFiscaleManager.FIELDNAME_DATEDECES).append(", ");
        sql.append(_getCollection()).append(REAttestationFiscaleManager.TABLE_AVS).append(POINT)
                .append(REAttestationFiscaleManager.FIELDNAME_NSS).append(", ");

        // Le code 4 est utilisé pour les cas qui on une date de validation de décision plus grande que le 01.01 de
        // l'année choisie par l'utilisateur
        sql.append(" CASE WHEN ").append(REDecisionEntity.FIELDNAME_DATE_VALIDATION).append(" > ")
                .append(getForAnnee()).append("0101 ");
        sql.append(" THEN 'CODE_4' ");

        sql.append(" WHEN ").append(_getCollection()).append(REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE);
        sql.append(POINT).append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE).append(" IS NULL ");
        sql.append(" THEN 'CODE_1' ");
        sql.append(" WHEN ((").append(_getCollection()).append(REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE);
        sql.append(POINT).append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE).append(" IS NOT NULL) AND (");
        sql.append(_getCollection()).append(REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE).append(POINT)
                .append(REEnteteBlocage.FIELDNAME_MONTANT_BLOQUE);
        sql.append(" = ");
        sql.append(_getCollection()).append(REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE).append(POINT)
                .append(REEnteteBlocage.FIELDNAME_MONTANT_DEBLOQUE);
        sql.append(")) THEN 'CODE_2' ");

        sql.append(" ELSE 'CODE_3' END AS CODE_BLOCAGE ");

        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

        // inner join cvciweb.REREACC on cvciweb.REPRACC.ZTIPRA = cvciweb.REREACC.YLIRAC
        sql.append(INNER_JOIN).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        sql.append(EGAL).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        // inner join cvciweb.REBACAL on cvciweb.REREACC.YLIBAC = cvciweb.REBACAL.YIIBCA
        sql.append(INNER_JOIN).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        sql.append(ON).append(_getCollection()).append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE).append(POINT)
                .append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);
        sql.append(EGAL).append(_getCollection()).append(REBasesCalcul.TABLE_NAME_BASES_CALCUL).append(POINT)
                .append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);

        // inner join cvciweb.REPRSDU on cvciweb.REPRACC.ZTIPRA = cvciweb.REPRSDU.YMIRAC
        sql.append(INNER_JOIN).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        sql.append(EGAL).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                .append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);

        // inner join cvciweb.REVALDEC on cvciweb.REPRSDU.YMIPRD = cvciweb.REVALDEC.YVIPRD
        sql.append(INNER_JOIN).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        sql.append(ON).append(_getCollection()).append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES).append(POINT)
                .append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);
        sql.append(EGAL).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION)
                .append(POINT).append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE);

        // inner join cvciweb.REDECIS on cvciweb.REDECIS.YWIDEC = cvciweb.REVALDEC.YVIDEC
        sql.append(INNER_JOIN).append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS);
        sql.append(ON).append(_getCollection()).append(REDecisionEntity.TABLE_NAME_DECISIONS).append(POINT)
                .append(REDecisionEntity.FIELDNAME_ID_DECISION);
        sql.append(EGAL).append(_getCollection()).append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION)
                .append(POINT).append(REValidationDecisions.FIELDNAME_ID_DECISION);

        sql.append(INNER_JOIN).append(_getCollection()).append(REAttestationFiscaleManager.TABLE_PERSONNE);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection()).append(REAttestationFiscaleManager.TABLE_PERSONNE).append(POINT)
                .append(REAttestationFiscaleManager.FIELDNAME_ID_TIERS);

        // left join cvciweb.REENTBLK on cvciweb.REENTBLK.ZZIEBK = cvciweb.REPRACC.ZTIEBK
        sql.append(LEFT_JOIN).append(_getCollection()).append(REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE);
        sql.append(ON).append(_getCollection()).append(REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE).append(POINT)
                .append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE);
        sql.append(EGAL).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_ENTETE_BLOCAGE);

        sql.append(INNER_JOIN).append(_getCollection()).append(REAttestationFiscaleManager.TABLE_AVS);
        sql.append(ON).append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT).append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append(EGAL).append(_getCollection()).append(REAttestationFiscaleManager.TABLE_AVS).append(POINT)
                .append(REAttestationFiscaleManager.FIELDNAME_ID_TIERS);

        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isBlankOrZero(getForCodesPrestationsIn())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(" WHERE ").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" IN ( ");
            whereClause.append(getForCodesPrestationsIn()).append(") ");
        }

        if (!JadeStringUtil.isBlankOrZero(getFornss())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(" WHERE ").append(_getCollection()).append(REAttestationFiscaleManager.TABLE_AVS)
                    .append(POINT).append(REAttestationFiscaleManager.FIELDNAME_NSS);
            whereClause.append(EGAL).append("'" + fornss + "'");

        }

        if (!JadeStringUtil.isBlankOrZero(getForAnnee())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append("<=").append(getForAnnee())
                    .append("12");

            whereClause.append(" AND (").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" = 0 OR ");
            whereClause.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL ");

            if (isControleDateFinDansAnnee()) {
                whereClause.append("OR ");
                whereClause.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" >= ")
                        .append(getForAnnee()).append("12) ");
            } else {
                whereClause.append("OR ");
                whereClause.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" >= ")
                        .append(getForAnnee()).append("01)");

            }
            // Cette condition ne devrait plus être utilisée car on prend quand même ces cas on mettant code 4 dans le
            // case ci-dessus.
            // Exclure les décisions prise durant l'année en cours.
            // whereClause.append(" AND (").append(REDecisions.FIELDNAME_DATE_VALIDATION).append(" < ").append(getForAnnee()).append("0101) ");
        }

        if (!JadeStringUtil.isBlank(forNssDe) || !JadeStringUtil.isBlank(forNssA)) {
            whereClause.append(" AND ");
            whereClause.append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" BETWEEN ");
        }

        if (!JadeStringUtil.isBlank(forNssDe) && !JadeStringUtil.isBlank(forNssA)) {
            whereClause.append("'").append(forNssDe).append("'");
            whereClause.append(" AND ");
            whereClause.append("'").append(forNssA).append("'");
        } else if (!JadeStringUtil.isBlank(forNssA)) {
            whereClause.append("'756.0000.0000.00'");
            whereClause.append(" AND ");
            whereClause.append("'").append(forNssA).append("'");
        } else if (!JadeStringUtil.isBlank(forNssDe)) {
            whereClause.append("'").append(forNssDe).append("'");
            whereClause.append(" AND ");
            whereClause.append("'756.9999.999.99'");
        }

        sql.append(whereClause.toString());

        // sql.append(" GROUP BY ");
        // sql.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(", ");
        // sql.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL).append(", ");
        // sql.append(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL).append(", ");
        // sql.append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(", ");
        // sql.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(", ");
        // sql.append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION).append(", ");
        // sql.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(", ");
        // sql.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(", ");
        // sql.append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);

        sql.append(" ORDER BY ");
        sql.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

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
        return new REAttestationFiscale();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCodesPrestationsIn() {
        return forCodesPrestationsIn;
    }

    public String getFornss() {
        return fornss;
    }

    public String getForNssA() {
        return forNssA;
    }

    public String getForNssDe() {
        return forNssDe;
    }

    public boolean isControleDateFinDansAnnee() {
        return controleDateFinDansAnnee;
    }

    public void setControleDateFinDansAnnee(boolean controleDateFinDansAnnee) {
        this.controleDateFinDansAnnee = controleDateFinDansAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCodesPrestationsIn(String forCodesPrestationsIn) {
        this.forCodesPrestationsIn = forCodesPrestationsIn;
    }

    public void setFornss(String fornss) {
        this.fornss = fornss;
    }

    public void setForNssA(String forNssA) {
        this.forNssA = forNssA;
    }

    public void setForNssDe(String forNssDe) {
        this.forNssDe = forNssDe;
    }

}
