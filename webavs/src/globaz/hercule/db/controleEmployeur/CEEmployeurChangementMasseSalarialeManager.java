package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * @author MMO
 * @since 27 juillet 07
 */
public class CEEmployeurChangementMasseSalarialeManager extends BManager {

    private static final long serialVersionUID = 4033124734231958013L;
    public static final String AS = " AS ";
    public static final String F_AFF_DDEB = AFAffiliation.FIELDNAME_AFF_DDEBUT;
    public static final String F_AFF_DFIN = AFAffiliation.FIELDNAME_AFF_DFIN;
    public static final String F_AFF_NUMAFFILIE = "MALNAF";
    public static final String F_AFF_TYPE = AFAffiliation.FIELDNAME_AFFILIATION_TYPE;
    public static final String F_CACPTA_NUMAFFILIE = "IDEXTERNEROLE";
    public static final String F_CACPTR_ANCIENCUMULMASSE = "ANCIENCUMULMASSE";

    public static final String F_CACPTR_CUMULMASSE = "CUMULMASSE";
    public static final String F_CACPTR_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String F_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";

    public static final String F_IDTIER = "HTITIE";
    public static final String F_TIER_COMPLEMENTNOM = "HTLDE2";
    public static final String F_TIER_NOM = "HTLDE1";

    public static final String FROM = " FROM ";
    public static final String GROUP_BY = " GROUP BY ";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String ON = " ON ";

    public static final String T_AFF = AFAffiliation.TABLE_NAME;
    public static final String T_ANCIENNEMASSESALARIALE = "ANCIENNEMASSESALARIALE";
    public static final String T_CACPTA = "CACPTAP";
    public static final String T_CACPTR = "CACPTRP";
    public static final String T_MASSESALARIALE = "MASSESALARIALE";
    public static final String T_TIER = "TITIERP";
    public static final String WHERE = " WHERE ";

    private String forAnnee;
    private String fromNumAffilie;
    private String toNumAffilie;

    @Override
    protected String _getFields(final BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        CEUtils.sqlAddField(sqlFields, F_AFF_NUMAFFILIE);
        CEUtils.sqlAddField(sqlFields, _getCollection() + T_TIER + "." + F_IDTIER);
        CEUtils.sqlAddField(sqlFields, F_TIER_NOM);
        CEUtils.sqlAddField(sqlFields, F_TIER_COMPLEMENTNOM);
        CEUtils.sqlAddField(sqlFields, F_AFF_DDEB);
        CEUtils.sqlAddField(sqlFields, F_AFF_DFIN);
        CEUtils.sqlAddField(sqlFields, T_MASSESALARIALE + "." + F_CACPTR_CUMULMASSE + AS + F_CACPTR_CUMULMASSE);
        CEUtils.sqlAddField(sqlFields, T_ANCIENNEMASSESALARIALE + "." + F_CACPTR_CUMULMASSE + AS
                + F_CACPTR_ANCIENCUMULMASSE);
        CEUtils.sqlAddField(sqlFields, "GROUPE.CELGRP");// nom du groupe
        CEUtils.sqlAddField(sqlFields, "COUP.PCOUID AS CODESUVA");
        CEUtils.sqlAddField(sqlFields, "COUP.PCOLUT AS LIBELLESUVA");

        return sqlFields.toString();

    }

    @Override
    protected String _getFrom(final BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + T_AFF);
        sqlFrom.append(INNER_JOIN);
        sqlFrom.append(_getCollection() + T_TIER);
        sqlFrom.append(ON);
        sqlFrom.append(_getCollection() + T_AFF + "." + F_IDTIER + "=" + _getCollection() + T_TIER + "." + F_IDTIER);
        sqlFrom.append(LEFT_JOIN);
        sqlFrom.append(_getCollection() + T_CACPTA);
        sqlFrom.append(ON);
        sqlFrom.append("(" + _getCollection() + T_CACPTA + "." + F_CACPTA_NUMAFFILIE + "=" + _getCollection() + T_AFF
                + "." + F_AFF_NUMAFFILIE + " AND " + _getCollection() + T_CACPTA + ".IDROLE="
                + CEAffiliationService.getRoleForAffilieParitaire(getSession()) + ") ");
        sqlFrom.append(LEFT_JOIN);
        sqlFrom.append(giveTableMasseSalariale(Integer.valueOf(getForAnnee()).intValue() - 1) + AS + T_MASSESALARIALE);
        sqlFrom.append(ON);
        sqlFrom.append(T_MASSESALARIALE + "." + F_IDCOMPTEANNEXE + "=" + _getCollection() + T_CACPTA + "."
                + F_IDCOMPTEANNEXE);
        sqlFrom.append(LEFT_JOIN);
        sqlFrom.append(giveTableMasseSalariale(Integer.valueOf(getForAnnee()).intValue() - 2) + AS
                + T_ANCIENNEMASSESALARIALE);
        sqlFrom.append(ON);
        sqlFrom.append(T_ANCIENNEMASSESALARIALE + "." + F_IDCOMPTEANNEXE + "=" + _getCollection() + T_CACPTA + "."
                + F_IDCOMPTEANNEXE);
        sqlFrom.append(" LEFT JOIN " + _getCollection() + "cememp as membre on (membre.maiaff = " + _getCollection()
                + T_AFF + ".maiaff) ");
        sqlFrom.append(" LEFT JOIN " + _getCollection() + "cegrpp as groupe on (groupe.ceidgr = membre.ceidgr) ");
        sqlFrom.append(" LEFT JOIN " + _getCollection() + "FWCOUP as coup on (" + _getCollection()
                + "AFAFFIP.MATSUV = coup.pcosid AND coup.PLAIDE='" + getSession().getIdLangue() + "') ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(final BStatement statement) {
        return F_AFF_NUMAFFILIE;
    }

    @Override
    protected String _getWhere(final BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        // affiliés employeurs
        CEUtils.sqlAddCondition(sqlWhere, F_AFF_TYPE + " IN (" + CodeSystem.TYPE_AFFILI_EMPLOY + ", "
                + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ")");

        // affiliés qui ont changé de masse salariale
        CEUtils.sqlAddCondition(sqlWhere, T_MASSESALARIALE + "." + F_CACPTR_CUMULMASSE + "<>"
                + T_ANCIENNEMASSESALARIALE + "." + F_CACPTR_CUMULMASSE);

        if (!JadeStringUtil.isBlank(getFromNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    F_AFF_NUMAFFILIE + " >= " + _dbWriteString(statement.getTransaction(), getFromNumAffilie()));
        }

        if (!JadeStringUtil.isBlank(getToNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    F_AFF_NUMAFFILIE + " <= " + _dbWriteString(statement.getTransaction(), getToNumAffilie()));
        }

        if (!JadeStringUtil.isBlank(getForAnnee())) {
            /**
             * affiliations qui courent durant getForAnnee()
             */
            CEUtils.sqlAddCondition(sqlWhere,
                    F_AFF_DDEB + " <= " + _dbWriteDateAMJ(statement.getTransaction(), "31.12." + getForAnnee()));
            CEUtils.sqlAddCondition(sqlWhere,
                    "(" + F_AFF_DFIN + " >= " + _dbWriteDateAMJ(statement.getTransaction(), "01.01." + getForAnnee())
                            + " OR " + F_AFF_DFIN + " = 0 )");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEEmployeurChangementMasseSalariale();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getToNumAffilie() {
        return toNumAffilie;
    }

    private String giveTableMasseSalariale(final int annee) {

        String idRubrique = CEUtils.getIdRubrique(getSession());

        return "(SELECT SUM(" + F_CACPTR_CUMULMASSE + ") " + AS + F_CACPTR_CUMULMASSE + ", " + F_IDCOMPTEANNEXE + FROM
                + _getCollection() + T_CACPTR + " " + WHERE + "ANNEE = " + String.valueOf(annee) + " AND "
                + F_CACPTR_IDRUBRIQUE + " IN( " + idRubrique + ")" + GROUP_BY + F_IDCOMPTEANNEXE + ")";
    }

    public void setForAnnee(final String string) {
        forAnnee = string;
    }

    public void setFromNumAffilie(final String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setToNumAffilie(final String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }

}
