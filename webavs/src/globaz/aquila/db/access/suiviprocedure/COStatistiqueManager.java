/**
 *
 */
package globaz.aquila.db.access.suiviprocedure;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.common.COBManager;
import globaz.aquila.db.access.batch.COEtapeInfo;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

// select h.ofiseq, css.pcolut, h.odieta, cse.pcolut, count(*) nombre, sum(oemsol) montant, sum( cast( (CASE WHEN
// onlval is not null THEN onlval else '0' END) as numeric))
// from webavsciam.cohistp h
// inner join webavsciam.COETAPP e on e.ODIETA=h.ODIETA
// left outer join webavsciam.coetinfv v on v.onihis=h.oeihis and omieic=0
// inner join webavsciam.coseqp s on s.ofiseq= h.ofiseq
// inner join webavsciam.fwcoup css on css.pcosid=s.oftseq and css.plaide='F'
// inner join webavsciam.fwcoup cse on cse.pcosid=e.odteta and cse.plaide='F'
// where oebann='2' and e.odteta not in (5200030, 5200029, 5200035)
// and oedexe between 20110101 and 20110831
// group by h.ofiseq, css.pcolut, h.odieta, cse.pcolut
// order by h.ofiseq, h.odieta

/**
 * @author sel
 * 
 */
public class COStatistiqueManager extends COBManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forLangue = "F";
    private String periodeAu = null;
    private String periodeDu = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        // select h.ofiseq, css.pcolut, h.odieta, cse.pcolut, count(*) nombre, sum(oemsol) montant, sum( cast( (CASE
        // WHEN onlval is not null THEN onlval else '0' END) as numeric))
        StringBuffer sqlFields = new StringBuffer();
        sqlFields.append(ICOHistoriqueConstante.TABLE_NAME).append(".")
                .append(ICOHistoriqueConstante.FNAME_ID_SEQUENCE);
        sqlFields.append(", ").append("CSS.PCOLUT").append(" ").append(COStatistique.SEQUENCE_LIBELLE);
        sqlFields.append(", ").append(ICOHistoriqueConstante.TABLE_NAME).append(".")
                .append(ICOHistoriqueConstante.FNAME_ID_ETAPE);
        sqlFields.append(", ").append("CSE.PCOLUT").append(" ").append(COStatistique.ETAPE_LIBELLE);
        sqlFields.append(", COUNT(*) " + COStatistique.NB_PAR_ETAPE);
        sqlFields.append(", SUM(oemsol) " + COStatistique.SUM_SOLDE);
        sqlFields.append(", SUM( CAST( (CASE WHEN onlval IS NOT NULL THEN onlval ELSE '0' END) AS NUMERIC)) "
                + COStatistique.SUM_TAXES);

        return sqlFields.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        // from webavsciam.cohistp h
        // inner join webavsciam.COETAPP e on e.ODIETA=h.ODIETA
        // left outer join webavsciam.coetinfv v on v.onihis=h.oeihis and omieic=0
        // inner join webavsciam.coseqp s on s.ofiseq= h.ofiseq
        // inner join webavsciam.fwcoup css on css.pcosid=s.oftseq and css.plaide='F'
        // inner join webavsciam.fwcoup cse on cse.pcosid=e.odteta and cse.plaide='F'
        StringBuffer sqlFrom = new StringBuffer("");
        sqlFrom.append(_getCollection()).append(ICOHistoriqueConstante.TABLE_NAME).append(" ")
                .append(ICOHistoriqueConstante.TABLE_NAME);

        sqlFrom.append(COBManager.INNER_JOIN);
        sqlFrom.append(_getCollection()).append(ICOEtapeConstante.TABLE_NAME).append(" ")
                .append(ICOEtapeConstante.TABLE_NAME);
        sqlFrom.append(COBManager.ON);
        sqlFrom.append(ICOHistoriqueConstante.TABLE_NAME).append(".").append(ICOHistoriqueConstante.FNAME_ID_ETAPE);
        sqlFrom.append(" = ");
        sqlFrom.append(ICOEtapeConstante.TABLE_NAME).append(".").append(ICOEtapeConstante.FNAME_ID_ETAPE);

        sqlFrom.append(COBManager.LEFT_OUTER_JOIN);
        sqlFrom.append(_getCollection()).append(COEtapeInfo.TABLE_NAME_VALEUR).append(" ")
                .append(COEtapeInfo.TABLE_NAME_VALEUR);
        sqlFrom.append(COBManager.ON);
        sqlFrom.append(COEtapeInfo.TABLE_NAME_VALEUR).append(".").append(COEtapeInfo.FNAME_IDHISTORIQUE);
        sqlFrom.append(" = ");
        sqlFrom.append(ICOHistoriqueConstante.TABLE_NAME).append(".")
                .append(ICOHistoriqueConstante.FNAME_ID_HISTORIQUE);
        sqlFrom.append(COBManager.AND).append(COEtapeInfo.TABLE_NAME_VALEUR).append(".")
                .append(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG).append(" = 0");

        sqlFrom.append(COBManager.INNER_JOIN);
        sqlFrom.append(_getCollection()).append(ICOSequenceConstante.TABLE_NAME).append(" ")
                .append(ICOSequenceConstante.TABLE_NAME);
        sqlFrom.append(COBManager.ON);
        sqlFrom.append(ICOSequenceConstante.TABLE_NAME).append(".").append(ICOSequenceConstante.FNAME_ID_SEQUENCE);
        sqlFrom.append(" = ");
        sqlFrom.append(ICOHistoriqueConstante.TABLE_NAME).append(".").append(ICOHistoriqueConstante.FNAME_ID_SEQUENCE);

        sqlFrom.append(COBManager.INNER_JOIN);
        sqlFrom.append(_getCollection()).append("FWCOUP").append(" ").append("CSS");
        sqlFrom.append(COBManager.ON);
        sqlFrom.append("CSS").append(".").append("PCOSID");
        sqlFrom.append(" = ");
        sqlFrom.append(ICOSequenceConstante.TABLE_NAME).append(".").append(ICOSequenceConstante.FNAME_LIB_SEQUENCE);
        sqlFrom.append(COBManager.AND).append("CSS.PLAIDE").append(" = '").append(getForLangue()).append("'");

        sqlFrom.append(COBManager.INNER_JOIN);
        sqlFrom.append(_getCollection()).append("FWCOUP").append(" ").append("CSE");
        sqlFrom.append(COBManager.ON);
        sqlFrom.append("CSE").append(".").append("PCOSID");
        sqlFrom.append(" = ");
        sqlFrom.append(ICOEtapeConstante.TABLE_NAME).append(".").append(ICOEtapeConstante.FNAME_LIBETAPE);
        sqlFrom.append(COBManager.AND).append("CSE.PLAIDE").append(" = '").append(getForLangue()).append("'");

        return sqlFrom.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        // order by h.ofiseq, h.odieta
        StringBuffer sqlOrder = new StringBuffer();
        sqlOrder.append(ICOHistoriqueConstante.TABLE_NAME).append(".").append(ICOHistoriqueConstante.FNAME_ID_SEQUENCE);
        sqlOrder.append(", ").append(ICOHistoriqueConstante.TABLE_NAME).append(".")
                .append(ICOHistoriqueConstante.FNAME_ID_ETAPE);

        return sqlOrder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // where oebann='2' and e.odteta not in (5200030, 5200029, 5200035)
        // and oedexe between 20110101 and 20110831
        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(ICOHistoriqueConstante.FNAME_EST_ANNULE).append("=")
                .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        sqlWhere.append(COBManager.AND).append(ICOEtapeConstante.TABLE_NAME).append(".")
                .append(ICOEtapeConstante.FNAME_LIBETAPE);
        sqlWhere.append(COBManager.NOT_IN).append("(");
        sqlWhere.append(ICOEtape.CS_AUCUNE);
        sqlWhere.append(", ").append(ICOEtape.CS_CONTENTIEUX_CREE);
        sqlWhere.append(", ").append(ICOEtape.CS_ARD_CREE);
        sqlWhere.append(")");

        if (!JadeStringUtil.isBlank(getPeriodeDu()) && !JadeStringUtil.isBlank(getPeriodeAu())) {
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(ICOHistoriqueConstante.FNAME_DATE_EXECUTION);
            sqlWhere.append(COBManager.BETWEEN);
            sqlWhere.append(JACalendar.format(getPeriodeDu(), JACalendar.FORMAT_YYYYMMDD));
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(JACalendar.format(getPeriodeAu(), JACalendar.FORMAT_YYYYMMDD));
        }

        sqlWhere.append(getGroupBy());
        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COStatistique();
    }

    /**
     * @return the forLangue
     */
    public String getForLangue() {
        return forLangue;
    }

    /*
	 *
	 */
    protected String getGroupBy() {
        // group by h.ofiseq, css.pcolut, h.odieta, cse.pcolut
        StringBuffer sqlGroupBy = new StringBuffer(COBManager.GROUP_BY);
        sqlGroupBy.append(ICOHistoriqueConstante.TABLE_NAME).append(".")
                .append(ICOHistoriqueConstante.FNAME_ID_SEQUENCE);
        sqlGroupBy.append(", ").append("CSS.PCOLUT");
        sqlGroupBy.append(", ").append(ICOHistoriqueConstante.TABLE_NAME).append(".")
                .append(ICOHistoriqueConstante.FNAME_ID_ETAPE);
        sqlGroupBy.append(", ").append("CSE.PCOLUT");

        return sqlGroupBy.toString();
    }

    /**
     * @return the periodeAu
     */
    public String getPeriodeAu() {
        return periodeAu;
    }

    /**
     * @return the periodeDu
     */
    public String getPeriodeDu() {
        return periodeDu;
    }

    /**
     * @param forLangue
     *            the forLangue to set
     */
    public void setForLangue(String forLangue) {
        this.forLangue = forLangue;
    }

    /**
     * @param untilDate
     */
    public void setPeriodeAu(String untilDate) {
        periodeAu = untilDate;
    }

    /**
     * @param fromDate
     */
    public void setPeriodeDu(String fromDate) {
        periodeDu = fromDate;
    }

}
