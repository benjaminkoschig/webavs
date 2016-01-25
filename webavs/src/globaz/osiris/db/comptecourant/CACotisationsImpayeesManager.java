package globaz.osiris.db.comptecourant;

import globaz.aquila.api.helper.ICOEtapeHelper;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAEvenementContentieux;

/**
 * Modifié le : 14 nov. 07
 */
public class CACotisationsImpayeesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String BETWEEN = " BETWEEN ";
    private static final String FROM = " FROM ";
    private static final String IN = " IN ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String IS_NOT_NULL = " IS NOT NULL ";
    private static final String LIKE = " LIKE ";
    private static final String NOT_BETWEEN = " NOT BETWEEN ";
    private static final String NOT_IN = " NOT IN ";
    private static final String NOT_LIKE = " NOT LIKE ";
    private static final String ON = " ON ";
    private static final String SELECT = "SELECT ";
    private static final String WHERE = " WHERE ";

    private boolean forDateAnterieur = false;
    private String forDateCG = "";
    private boolean forPoursuite = false;
    private boolean forSursis = false;

    private String order = "";

    public CACotisationsImpayeesManager() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String select = "";

        if (!(isForPoursuite() || isForSursis())) {
            select += "cc.idexterne,";
        }
        select += "sum(montant) as " + CACotisationsImpayees.FIELD_TOTAL_MONTANT;

        return select;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        fromClauseBuffer.append(_getCollection() + CACotisationsImpayees.TABLE_CACPTCP + " cc");
        // jointure entre table
        fromClauseBuffer.append(CACotisationsImpayeesManager.INNER_JOIN + _getCollection() + "CAOPERP op"
                + CACotisationsImpayeesManager.ON + "cc.idcomptecourant=op.idcomptecourant");
        fromClauseBuffer.append(CACotisationsImpayeesManager.INNER_JOIN + _getCollection() + "CAJOURP jo"
                + CACotisationsImpayeesManager.ON + "op.idjournal=jo.idjournal");
        // Cas en poursuite de l'ancien ctx.
        if (isForPoursuite() && !CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            /*
             * Requete pour poursuite OSIRIS SELECT sum(montant) as "total montant" FROM WEBAVS.CACPTCP cc INNER JOIN
             * WEBAVS.CAOPERP op ON cc.idcomptecourant=op.idcomptecourant INNER JOIN WEBAVS.CAJOURP jo ON
             * op.idjournal=jo.idjournal INNER JOIN ( SELECT sc.idsection FROM WEBAVS.CASECTP sc INNER JOIN ( SELECT
             * idEveCon FROM WEBAVS.CAEVCTP ec INNER JOIN ( SELECT idParametreEtape FROM WEBAVS.CAPECTP pe INNER JOIN (
             * SELECT idetape FROM WEBAVS.CAETCTP WHERE typeEtape IN (216003, 216004, 216005) ) et ON et.idetape =
             * pe.idetape ) ep ON ep.idParametreEtape=ec.idParametreEtape WHERE ec.dateExecution<>0 ) ecp ON
             * ecp.idEveCon = sc.idLastEtapeCtx WHERE solde>0 ) se ON se.idsection=op.idsection WHERE op.etat=205002 AND
             * jo.datevaleurcg<=20071035 AND cc.idexterne LIKE '%.110_.%' AND cc.idexterne NOT LIKE '%.1106.%'
             */
            fromClauseBuffer.append(CACotisationsImpayeesManager.INNER_JOIN + "(");
            fromClauseBuffer.append(getIdSection(statement.getTransaction()));
            fromClauseBuffer.append(") se" + CACotisationsImpayeesManager.ON + "se.idsection=op.idsection");
        } else { // Autres cas
            fromClauseBuffer.append(CACotisationsImpayeesManager.INNER_JOIN + _getCollection() + "CASECTP se"
                    + CACotisationsImpayeesManager.ON + "se.idsection=op.idsection");
        }

        fromClauseBuffer.append(CACotisationsImpayeesManager.INNER_JOIN + _getCollection() + "CAJOURP jo2"
                + CACotisationsImpayeesManager.ON + "se.idjournal=jo2.idjournal");

        return fromClauseBuffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer("");

        sqlWhere.append("op.etat=" + APIOperation.ETAT_COMPTABILISE);

        // ForDateCG
        if (!JadeStringUtil.isBlank(getForDateCG())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CACotisationsImpayeesManager.AND);
            }
            sqlWhere.append("jo.datevaleurcg" + "<=");
            String d = this._dbWriteDateAMJ(statement.getTransaction(), getForDateCG());
            sqlWhere.append(d);
        }

        // ForDate
        if (!JadeStringUtil.isBlank(getForDateCG())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CACotisationsImpayeesManager.AND);
            }

            sqlWhere.append("jo2.datevaleurcg");
            if (!isForDateAnterieur()) {
                sqlWhere.append(CACotisationsImpayeesManager.BETWEEN);
            } else {
                sqlWhere.append(CACotisationsImpayeesManager.NOT_BETWEEN);
            }
            String d = this._dbWriteDateAMJ(statement.getTransaction(), getForDateCG());
            d = d.substring(0, 6) + "01";
            sqlWhere.append(d);
            sqlWhere.append(CACotisationsImpayeesManager.AND);
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateCG()));
        }

        // Contentieux est suspendu
        if (isForSursis()) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CACotisationsImpayeesManager.AND);
            }
            sqlWhere.append("se." + CASection.FIELD_CONTENTIEUXESTSUS + "='"
                    + this._dbWriteBoolean(statement.getTransaction(), Boolean.TRUE) + "'");
            // Bug 6321
            if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                sqlWhere.append(CACotisationsImpayeesManager.AND + "se.idLastEtatAquila"
                        + CACotisationsImpayeesManager.IN + "(");
                sqlWhere.append(ICOEtapeHelper.ETAPE_POURSUITE_SQL_NOT_IN_FORMAT);
                sqlWhere.append(")");
            }
        }

        /*
         * Requete pour AQUILA : SELECT sum(montant) as "total_montant" FROM WEBAVS.CACPTCP cc INNER JOIN WEBAVS.CAOPERP
         * op ON cc.idcomptecourant=op.idcomptecourant INNER JOIN WEBAVS.CAJOURP jo ON op.idjournal=jo.idjournal INNER
         * JOIN WEBAVS.casectp se ON se.idsection = op.idsection
         * 
         * WHERE op.etat=205002 AND jo.datevaleurcg<=20071035 AND cc.idexterne LIKE '%.110_.%' AND cc.idexterne NOT LIKE
         * '%.1106.%' AND se.idLastEtatAquila IS NOT NULL AND se.idLastEtatAquila NOT IN
         * (0,5200029,5200030,5200035,5200044,5200032,5200033,5200034) AND se.solde > 0
         */
        // Poursuite AQUILA
        if (isForPoursuite() && CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            // Est au contentieux
            sqlWhere.append(CACotisationsImpayeesManager.AND + "se.idLastEtatAquila"
                    + CACotisationsImpayeesManager.IS_NOT_NULL);
            // Etapes avant la requisition de poursuite
            sqlWhere.append(CACotisationsImpayeesManager.AND + "se.idLastEtatAquila"
                    + CACotisationsImpayeesManager.NOT_IN + "(");
            sqlWhere.append(ICOEtapeHelper.ETAPE_POURSUITE_SQL_NOT_IN_FORMAT);
            sqlWhere.append(")");
        }

        sqlWhere.append(CACotisationsImpayeesManager.AND);
        sqlWhere.append("cc.idexterne");
        sqlWhere.append(CACotisationsImpayeesManager.LIKE);
        sqlWhere.append(" '%.110_.%' ");

        sqlWhere.append(CACotisationsImpayeesManager.AND);
        sqlWhere.append("cc.idexterne");
        sqlWhere.append(CACotisationsImpayeesManager.NOT_LIKE);
        sqlWhere.append(" '%.1106.%' ");

        if (!(isForPoursuite() || isForSursis())) {
            sqlWhere.append(getGroupBy());
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        CACotisationsImpayees entity = new CACotisationsImpayees();
        return entity;
    }

    public String getForDateCG() {
        return forDateCG;
    }

    /**
     * @return
     */
    protected String getGroupBy() {
        return " GROUP BY cc.idexterne ";
    }

    /**
     * Retourne les id etapes de type : poursuite, continuer et vente
     * 
     * @return les id etapes de type : poursuite, continuer et vente
     */
    private String getIdEtape() {
        StringBuffer sql = new StringBuffer("");
        sql.append(CACotisationsImpayeesManager.SELECT);
        sql.append("idEtape");
        sql.append(CACotisationsImpayeesManager.FROM);
        sql.append(_getCollection() + "CAETCTP");
        sql.append(CACotisationsImpayeesManager.WHERE);
        sql.append("typeEtape" + CACotisationsImpayeesManager.IN + "(" + APIEtape.ETAPE_POURSUITE_FORMAT + ")");
        // SELECT idetape FROM WEBAVS.CAETCTP WHERE typeEtape IN (216003,
        // 216004, 216005)
        return sql.toString();
    }

    /**
     * @return
     */
    private String getIdEvenementContentieux() {
        StringBuffer sql = new StringBuffer("");
        sql.append(CACotisationsImpayeesManager.SELECT);
        sql.append("ec.idEveCon");
        sql.append(CACotisationsImpayeesManager.FROM);
        sql.append(_getCollection() + CAEvenementContentieux.TABLE_CAEVCTP + " ec");
        sql.append(CACotisationsImpayeesManager.INNER_JOIN + "(");
        sql.append(getIdParametreEtape());
        sql.append(") ep" + CACotisationsImpayeesManager.ON + "ep.idParametreEtape=ec.idParametreEtape");
        sql.append(CACotisationsImpayeesManager.WHERE + "ec.dateExecution<>0");
        // SELECT idEveCon FROM WEBAVS.CAEVCTP ec INNER JOIN (
        // SELECT idParametreEtape FROM WEBAVS.CAPECTP pe INNER JOIN (
        // SELECT idetape FROM WEBAVS.CAETCTP WHERE typeEtape IN (216003,
        // 216004, 216005)
        // ) et ON et.idetape = pe.idetape
        // ) ep ON ep.idParametreEtape=ec.idParametreEtape
        // WHERE ec.dateExecution<>0
        return sql.toString();
    }

    /**
     * @return
     */
    private String getIdParametreEtape() {
        StringBuffer sql = new StringBuffer("");
        sql.append(CACotisationsImpayeesManager.SELECT);
        sql.append("pe.idParametreEtape");
        sql.append(CACotisationsImpayeesManager.FROM);
        sql.append(_getCollection() + "CAPECTP pe");
        sql.append(CACotisationsImpayeesManager.INNER_JOIN + "(");
        sql.append(getIdEtape());
        sql.append(") et" + CACotisationsImpayeesManager.ON + "et.idetape = pe.idetape");
        // SELECT idParametreEtape FROM WEBAVS.CAPECTP pe INNER JOIN (
        // SELECT idetape FROM WEBAVS.CAETCTP WHERE typeEtape IN (216003,
        // 216004, 216005)
        // ) et ON et.idetape = pe.idetape
        return sql.toString();
    }

    /**
     * @return
     */
    private String getIdSection(BTransaction transaction) {
        StringBuffer sql = new StringBuffer("");
        sql.append(CACotisationsImpayeesManager.SELECT);
        sql.append("sc.idsection, sc.idjournal");
        sql.append(CACotisationsImpayeesManager.FROM);
        sql.append(_getCollection() + CASection.TABLE_CASECTP + " sc");
        sql.append(CACotisationsImpayeesManager.INNER_JOIN + "(");
        sql.append(getIdEvenementContentieux());
        sql.append(") ecp" + CACotisationsImpayeesManager.ON + "ecp.idEveCon = sc.idLastEtapeCtx");
        sql.append(CACotisationsImpayeesManager.WHERE + "sc." + CASection.FIELD_CONTENTIEUXESTSUS + "='"
                + this._dbWriteBoolean(transaction, Boolean.FALSE) + "'");
        // SELECT sc.idsection FROM WEBAVS.CASECTP sc INNER JOIN (
        // SELECT idEveCon FROM WEBAVS.CAEVCTP ec INNER JOIN (
        // SELECT idParametreEtape FROM WEBAVS.CAPECTP pe INNER JOIN (
        // SELECT idetape FROM WEBAVS.CAETCTP WHERE typeEtape IN (216003,
        // 216004, 216005)
        // ) et ON et.idetape = pe.idetape
        // ) ep ON ep.idParametreEtape=ec.idParametreEtape
        // WHERE ec.dateExecution<>0
        // ) ecp ON ecp.idEveCon = sc.idLastEtapeCtx
        // WHERE solde>0
        return sql.toString();
    }

    public String getOrderByDefaut() {
        return order;
    }

    /**
     * @return the forDateAnterieur
     */
    public boolean isForDateAnterieur() {
        return forDateAnterieur;
    }

    /**
     * @return the forPoursuite
     */
    public boolean isForPoursuite() {
        return forPoursuite;
    }

    /**
     * @return the forSursis
     */
    public boolean isForSursis() {
        return forSursis;
    }

    public void setForDateAnterieur(boolean forDateAnterieur) {
        this.forDateAnterieur = forDateAnterieur;
    }

    public void setForDateCG(String fordatecg) {
        forDateCG = fordatecg;
    }

    /**
     * @param forPoursuite
     *            the forPoursuite to set
     */
    public void setForPoursuite(boolean forPoursuite) {
        this.forPoursuite = forPoursuite;
    }

    /**
     * @param forSursis
     *            the forSursis to set
     */
    public void setForSursis(boolean forSursis) {
        this.forSursis = forSursis;
    }
}
