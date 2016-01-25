package globaz.tucana.db.list;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class TUAffiliationProblemManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDatePeriodeBegin;
    private String forDatePeriodeEnd;
    private String forGenreAssu;
    private String forIdExterneRubrique;

    private boolean searchOsirisOnly = false;

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        String select = "SELECT a." + "MONTANT" + ", d." + "IDEXTERNEROLE" + ", d." + "IDROLE" + ", d."
                + "IDCOMPTEANNEXE" + " ";
        select += " FROM ";
        select += getFrom();
        select += " WHERE ";
        select += " a." + "ETAT" + " = " + "205002" + " and ";
        select += getWhereRubrique();
        select += getWhereJournal();
        select += "a." + "IDCOMPTEANNEXE" + " = d." + "IDCOMPTEANNEXE" + " ";

        if (!isSearchOsirisOnly()) {
            select += " and ";
            select += getWhereAffiliation();
            select += getWhereCotisation();
            select += getWhereAssurance();
            select += getWhereCodeSystem();
        }

        select += getOrderBy();

        return select;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new TUAffiliationProblem();
    }

    public String getForDatePeriodeBegin() {
        return forDatePeriodeBegin;
    }

    public String getForDatePeriodeEnd() {
        return forDatePeriodeEnd;
    }

    public String getForGenreAssu() {
        return forGenreAssu;
    }

    public String getForIdExterneRubrique() {
        return forIdExterneRubrique;
    }

    private String getFrom() {
        String from = _getCollection() + "CAOPERP" + " a, " + _getCollection() + "CARUBRP" + " b, " + _getCollection()
                + "CAJOURP" + " c, " + _getCollection() + "CACPTAP" + " d ";

        if (!isSearchOsirisOnly()) {
            from += ", " + _getCollection() + "AFAFFIP e, " + _getCollection() + "AFCOTIP f, " + _getCollection()
                    + "AFASSUP g, " + _getCollection() + "FWCOUP h, " + _getCollection() + "AFPLAFP gb";
        }

        return from;
    }

    private String getOrderBy() {
        return "order by d." + "IDEXTERNEROLE" + ", d." + "IDROLE";
    }

    private String getWhereAffiliation() {
        return "d." + "IDTIERS" + " = e.HTITIE and d." + "IDEXTERNEROLE"
                + " = e.MALNAF and (CAST(SUBSTR(CAST(e.MADDEB as CHAR(8)), 1, 4) as NUMERIC) <= a." + "ANNEECOTISATION"
                + " and (CAST(SUBSTR(CAST(e.MADFIN as CHAR(8)), 1, 4) as NUMERIC) >= a." + "ANNEECOTISATION"
                + " or e.MADFIN = 0)) and ";
    }

    private String getWhereAssurance() {
        return "f.MBIASS = g.MBIASS and g.MBTTYP = " + getForGenreAssu() + " and g.MBIRUB = b." + "IDRUBRIQUE"
                + " and ";
    }

    private String getWhereCodeSystem() {
        return "g.MBTCAN = h.PCOSID and h.PLAIDE = 'F' ";
    }

    private String getWhereCotisation() {
        String result = "e.MAIAFF = gb.MAIAFF and gb.MUIPLA = f.MUIPLA and (CAST(SUBSTR(CAST(f.MEDDEB as CHAR(8)), 1, 4) as NUMERIC) <= a."
                + "ANNEECOTISATION"
                + " and (CAST(SUBSTR(CAST(f.MEDFIN as CHAR(8)), 1, 4) as NUMERIC) > a."
                + "ANNEECOTISATION" + " or f.MEDFIN = 0 ";
        result += " or ( ";
        result += "CAST(SUBSTR(CAST(f.MEDFIN as CHAR(8)), 1, 4) as NUMERIC) = a." + "ANNEECOTISATION" + " and ";
        result += "(CAST(f.MEDFIN as CHAR(8)) = concat(CAST(a." + "ANNEECOTISATION"
                + " as CHAR(4)), '1231') or f.MEDFIN >= " + getForDatePeriodeEnd() + ")))) and ";
        return result;
    }

    private String getWhereJournal() {
        return "a." + "IDJOURNAL" + " = c." + "IDJOURNAL" + " and (c." + "DATEVALEURCG" + " >= "
                + getForDatePeriodeBegin() + " and c." + "DATEVALEURCG" + " <= " + getForDatePeriodeEnd() + ") and ";
    }

    private String getWhereRubrique() {
        return "a." + "IDCOMPTE" + " = b." + "IDRUBRIQUE" + " and b." + "IDEXTERNE" + " = '"
                + getForIdExterneRubrique() + "' and ";
    }

    public boolean isSearchOsirisOnly() {
        return searchOsirisOnly;
    }

    public void setForDatePeriodeBegin(String forDatePeriodeBegin) {
        this.forDatePeriodeBegin = forDatePeriodeBegin;
    }

    public void setForDatePeriodeEnd(String forDatePeriodeEnd) {
        this.forDatePeriodeEnd = forDatePeriodeEnd;
    }

    public void setForGenreAssu(String forGenreAssu) {
        this.forGenreAssu = forGenreAssu;
    }

    public void setForIdExterneRubrique(String forIdExterneRubrique) {
        this.forIdExterneRubrique = forIdExterneRubrique;
    }

    public void setSearchOsirisOnly(boolean searchOsirisOnly) {
        this.searchOsirisOnly = searchOsirisOnly;
    }

}
