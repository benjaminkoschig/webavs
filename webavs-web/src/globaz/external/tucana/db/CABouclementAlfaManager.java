package globaz.external.tucana.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * Manager pour le bouclement ALFA OSIRIS.<br/>
 * <br/>
 * Sql example :<br/>
 * SELECT sum(a.MONTANT) MONTANT, sum(a.MASSE) MASSE, h.PCOUID FROM webavs.CAOPERP a, webavs.CARUBRP b, webavs.CAJOURP
 * c, webavs.CACPTAP d, webavs.AFAFFIP e, webavs.AFCOTIP f, webavs.AFASSUP g, webavs.FWCOUP h, webavs.AFPLAFP gb WHERE
 * a.ETAT = 205002 and a.IDCOMPTE = b.IDRUBRIQUE and b.IDEXTERNE = '5500.4030.0000' and a.IDJOURNAL = c.IDJOURNAL and
 * (c.DATEVALEURCG >= 20070101 and c.DATEVALEURCG <= 20070131) and a.IDCOMPTEANNEXE = d.IDCOMPTEANNEXE and d.IDTIERS =
 * e.HTITIE and d.IDEXTERNEROLE = e.MALNAF and (CAST(SUBSTR(CAST(e.MADDEB as CHAR(8)), 1, 4) as NUMERIC) <=
 * a.ANNEECOTISATION and (CAST(SUBSTR(CAST(e.MADFIN as CHAR(8)), 1, 4) as NUMERIC) >= a.ANNEECOTISATION or e.MADFIN = 0
 * )) and e.MAIAFF = gb.MAIAFF and gb.MUIPLA = f.MUIPLA and (CAST(SUBSTR(CAST(f.MEDDEB as CHAR(8)), 1, 4) as NUMERIC) <=
 * a.ANNEECOTISATION and (CAST(SUBSTR(CAST(f.MEDFIN as CHAR(8)), 1, 4) as NUMERIC) >= a.ANNEECOTISATION or f.MEDFIN =
 * 0)) and f.MBIASS = g.MBIASS and g.MBTTYP = 812002 and g.MBTCAN = h.PCOSID and h.PLAIDE = 'F' group by h.PCOUID order
 * by h.PCOUID
 * 
 * @author dda
 */
public class CABouclementAlfaManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean forAssuranceFFPP = false;
    private String forDatePeriodeBegin;
    private String forDatePeriodeEnd;

    private String forIdExterneRubrique;

    private boolean groupByAnneeCotisation = false;

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        /*
         * SELECT sum(a.MONTANT) MONTANT, sum(a.MASSE) MASSE, h.PCOUID FROM webavs.CAOPERP a, webavs.CARUBRP b,
         * webavs.CAJOURP c, webavs.CACPTAP d, webavs.AFAFFIP e, webavs.AFCOTIP f, webavs.AFASSUP g, webavs.FWCOUP h,
         * webavs.AFPLAFP gb WHERE a.ETAT = 205002 and a.IDCOMPTE = b.IDRUBRIQUE and b.IDEXTERNE = '5500.4030.0000' and
         * a.IDJOURNAL = c.IDJOURNAL and (c.DATEVALEURCG >= 20070101 and c.DATEVALEURCG <= 20070131) and
         * a.IDCOMPTEANNEXE = d.IDCOMPTEANNEXE and d.IDTIERS = e.HTITIE and d.IDEXTERNEROLE = e.MALNAF and
         * (CAST(SUBSTR(CAST(e.MADDEB as CHAR(8)), 1, 4) as NUMERIC) <= a.ANNEECOTISATION and (CAST(SUBSTR(CAST(e.MADFIN
         * as CHAR(8)), 1, 4) as NUMERIC) >= a.ANNEECOTISATION or e.MADFIN = 0 )) and e.MAIAFF = gb.MAIAFF and gb.MUIPLA
         * = f.MUIPLA and (CAST(SUBSTR(CAST(f.MEDDEB as CHAR(8)), 1, 4) as NUMERIC) <= a.ANNEECOTISATION and
         * (CAST(SUBSTR(CAST(f.MEDFIN as CHAR(8)), 1, 4) as NUMERIC) >= a.ANNEECOTISATION or f.MEDFIN = 0)) and f.MBIASS
         * = g.MBIASS and g.MBTTYP = 812002 and g.MBTCAN = h.PCOSID and h.PLAIDE = 'F' group by h.PCOUID order by
         * h.PCOUID
         */

        String select = "SELECT sum(a." + CAOperation.FIELD_MONTANT + ") " + CAOperation.FIELD_MONTANT + ", sum(a."
                + CAOperation.FIELD_MASSE + ") " + CAOperation.FIELD_MASSE + ", h.PCOUID ";

        if (isGroupByAnneeCotisation()) {
            select += ", a." + CAOperation.FIELD_ANNEECOTISATION + " ";
        }

        select += " FROM ";
        select += getFrom();
        select += " WHERE ";
        select += " a." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE + " and ";
        select += getWhereRubrique();
        select += getWhereJournal();
        select += "a." + CAOperation.FIELD_IDCOMPTEANNEXE + " = d." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " and ";
        select += getWhereAffiliation();
        select += getWhereCotisation();
        select += getWhereAssurance();
        select += getWhereCodeSystem();
        select += getGroupBy();
        select += getOrderBy();

        return select;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CABouclementAlfa();
    }

    public String getForDatePeriodeBegin() {
        return forDatePeriodeBegin;
    }

    public String getForDatePeriodeEnd() {
        return forDatePeriodeEnd;
    }

    public String getForIdExterneRubrique() {
        return forIdExterneRubrique;
    }

    private String getFrom() {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " a, " + _getCollection() + CARubrique.TABLE_CARUBRP
                + " b, " + _getCollection() + CAJournal.TABLE_CAJOURP + " c, " + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " d, " + _getCollection() + "AFAFFIP e, " + _getCollection()
                + "AFCOTIP f, " + _getCollection() + "AFASSUP g, " + _getCollection() + "FWCOUP h, " + _getCollection()
                + "AFPLAFP gb";
    }

    private String getGroupBy() {
        String groupBy = "group by h.PCOUID ";

        if (isGroupByAnneeCotisation()) {
            groupBy += ", a." + CAOperation.FIELD_ANNEECOTISATION + " ";
        }

        return groupBy;
    }

    private String getOrderBy() {
        return "order by h.PCOUID";
    }

    private String getWhereAffiliation() {
        return "d." + CACompteAnnexe.FIELD_IDTIERS + " = e.HTITIE and d." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                + " = e.MALNAF and (CAST(SUBSTR(CAST(e.MADDEB as CHAR(8)), 1, 4) as NUMERIC) <= a."
                + CAOperation.FIELD_ANNEECOTISATION
                + " and (CAST(SUBSTR(CAST(e.MADFIN as CHAR(8)), 1, 4) as NUMERIC) >= a."
                + CAOperation.FIELD_ANNEECOTISATION + " or e.MADFIN = 0)) and ";
    }

    private String getWhereAssurance() {
        // String result = "f.MBIASS = g.MBIASS and g.MBTTYP = ";
        String result = "f.MBIASS = g.MBIASS and g.MBTTYP IN ( ";

        if (isForAssuranceFFPP()) {
            result += CodeSystem.TYPE_ASS_FFPP;
            result += ",";
            result += CodeSystem.TYPE_ASS_FFPP_MASSE;
        } else {
            result += CodeSystem.TYPE_ASS_COTISATION_AF;
        }

        result += " ) and g.MBIRUB = b." + CARubrique.FIELD_IDRUBRIQUE + " and ";

        return result;
    }

    private String getWhereCodeSystem() {
        return "g.MBTCAN = h.PCOSID and h.PLAIDE = 'F' ";
    }

    private String getWhereCotisation() {
        String result = "e.MAIAFF = gb.MAIAFF and gb.MUIPLA = f.MUIPLA and (CAST(SUBSTR(CAST(f.MEDDEB as CHAR(8)), 1, 4) as NUMERIC) <= a."
                + CAOperation.FIELD_ANNEECOTISATION
                + " and (CAST(SUBSTR(CAST(f.MEDFIN as CHAR(8)), 1, 4) as NUMERIC) > a."
                + CAOperation.FIELD_ANNEECOTISATION + " or f.MEDFIN = 0 ";
        result += " or ( ";
        result += "CAST(SUBSTR(CAST(f.MEDFIN as CHAR(8)), 1, 4) as NUMERIC) = a." + CAOperation.FIELD_ANNEECOTISATION
                + " and ";
        result += "(CAST(f.MEDFIN as CHAR(8)) = concat(CAST(a." + CAOperation.FIELD_ANNEECOTISATION
                + " as CHAR(4)), '1231') or f.MEDFIN >= " + getForDatePeriodeEnd() + ")))) and ";
        return result;
    }

    private String getWhereJournal() {
        return "a." + CAOperation.FIELD_IDJOURNAL + " = c." + CAJournal.FIELD_IDJOURNAL + " and (c."
                + CAJournal.FIELD_DATEVALEURCG + " >= " + getForDatePeriodeBegin() + " and c."
                + CAJournal.FIELD_DATEVALEURCG + " <= " + getForDatePeriodeEnd() + ") and ";
    }

    private String getWhereRubrique() {
        return "a." + CAOperation.FIELD_IDCOMPTE + " = b." + CARubrique.FIELD_IDRUBRIQUE + " and b."
                + CARubrique.FIELD_IDEXTERNE + " = '" + getForIdExterneRubrique() + "' and ";
    }

    public boolean isForAssuranceFFPP() {
        return forAssuranceFFPP;
    }

    public boolean isGroupByAnneeCotisation() {
        return groupByAnneeCotisation;
    }

    public void setForAssuranceFFPP(boolean forAssuranceFFPP) {
        this.forAssuranceFFPP = forAssuranceFFPP;
    }

    public void setForDatePeriodeBegin(String forDatePeriodeBegin) {
        this.forDatePeriodeBegin = forDatePeriodeBegin;
    }

    public void setForDatePeriodeEnd(String forDatePeriodeEnd) {
        this.forDatePeriodeEnd = forDatePeriodeEnd;
    }

    public void setForIdExterneRubrique(String forIdExterneRubrique) {
        this.forIdExterneRubrique = forIdExterneRubrique;
    }

    public void setGroupByAnneeCotisation(boolean groupByAnneeCotisation) {
        this.groupByAnneeCotisation = groupByAnneeCotisation;
    }
}
