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
 * @since 2 aout. 2010
 */
public class CEEmployeurMasseSalarialeManager extends BManager {

    private static final long serialVersionUID = 8683428671241547043L;
    public static final String F_AFF_DDEB = AFAffiliation.FIELDNAME_AFF_DDEBUT;
    public static final String F_AFF_DFIN = AFAffiliation.FIELDNAME_AFF_DFIN;

    public static final String F_AFF_NUMAFFILIE = "MALNAF";
    public static final String F_AFF_TYPE = AFAffiliation.FIELDNAME_AFFILIATION_TYPE;
    public static final String F_CACPTA_NUMAFFILIE = "IDEXTERNEROLE";
    public static final String F_CACPTR_CUMULMASSE_MOINS_1 = "CUMULMASSEAN1";
    public static final String F_CACPTR_CUMULMASSE_MOINS_2 = "CUMULMASSEAN2";
    public static final String F_CACPTR_CUMULMASSE_MOINS_3 = "CUMULMASSEAN3";
    public static final String F_CACPTR_CUMULMASSE_MOINS_4 = "CUMULMASSEAN4";
    public static final String F_CACPTR_CUMULMASSE_MOINS_5 = "CUMULMASSEAN5";
    public static final String F_CACPTR_CUMULMASSE_AF = "CUMULMASSEAF";

    public static final String F_CACPTR_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String F_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";

    public static final String F_IDTIER = "HTITIE";
    public static final String F_TIER_COMPLEMENTNOM = "HTLDE2";
    public static final String F_TIER_NOM = "HTLDE1";

    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String ON = " ON ";

    public static final String T_AFF = AFAffiliation.TABLE_NAME;
    public static final String T_CACPTA = "CACPTAP";
    public static final String T_CACPTR = "CACPTRP";
    public static final String T_TIER = "TITIERP";

    private String forAnnee;
    private String fromNumAffilie;
    private String toNumAffilie;

    @Override
    protected String _getFields(final BStatement statement) {

        int annee = Integer.valueOf(getForAnnee()).intValue();

        StringBuffer sqlFields = new StringBuffer("");

        CEUtils.sqlAddField(sqlFields, F_AFF_NUMAFFILIE);
        CEUtils.sqlAddField(sqlFields, _getCollection() + T_TIER + "." + F_IDTIER);
        CEUtils.sqlAddField(sqlFields, F_TIER_NOM);
        CEUtils.sqlAddField(sqlFields, F_TIER_COMPLEMENTNOM);
        CEUtils.sqlAddField(sqlFields, F_AFF_DDEB);
        CEUtils.sqlAddField(sqlFields, F_AFF_DFIN);
        CEUtils.sqlAddField(sqlFields, giveColonneMasseSalariale(annee - 1) + " AS " + F_CACPTR_CUMULMASSE_MOINS_1);
        CEUtils.sqlAddField(sqlFields, giveColonneMasseSalariale(annee - 2) + " AS " + F_CACPTR_CUMULMASSE_MOINS_2);
        CEUtils.sqlAddField(sqlFields, giveColonneMasseSalariale(annee - 3) + " AS " + F_CACPTR_CUMULMASSE_MOINS_3);
        CEUtils.sqlAddField(sqlFields, giveColonneMasseSalariale(annee - 4) + " AS " + F_CACPTR_CUMULMASSE_MOINS_4);
        CEUtils.sqlAddField(sqlFields, giveColonneMasseSalariale(annee - 5) + " AS " + F_CACPTR_CUMULMASSE_MOINS_5);
        CEUtils.sqlAddField(sqlFields, giveColonneMasseSalarialeAF(annee - 1) + " AS " + F_CACPTR_CUMULMASSE_AF);
        CEUtils.sqlAddField(sqlFields, giveColonneNbCI(annee - 1) + " AS NBCI1");
        CEUtils.sqlAddField(sqlFields, giveColonneNbCI(annee - 2) + " AS NBCI2");
        CEUtils.sqlAddField(sqlFields, giveColonneNbCI(annee - 3) + " AS NBCI3");
        CEUtils.sqlAddField(sqlFields, giveColonneNbCI(annee - 4) + " AS NBCI4");
        CEUtils.sqlAddField(sqlFields, giveColonneNbCI(annee - 5) + " AS NBCI5");
        CEUtils.sqlAddField(sqlFields, "HBCADM AS NUMCAISSE");
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

        sqlFrom.append(" LEFT JOIN ");
        sqlFrom.append(_getCollection() + "afsuafp suiv ON (SUIV.MAIAFF = " + _getCollection()
                + "afaffip.MAIAFF AND SUIV.mytgen = 830001 AND ( SUIV.mydfin >= "
                + (Integer.valueOf(getForAnnee()).intValue()) + "1231  OR SUIV.mydfin = 0 ) AND ( SUIV.myddeb <= "
                + (Integer.valueOf(getForAnnee()).intValue()) + "1231 OR SUIV.myddeb = 0 ))");
        sqlFrom.append(" LEFT JOIN ");
        sqlFrom.append(_getCollection() + "tiadmip admip ON admip.htitie = suiv.htitie ");

        sqlFrom.append(" LEFT OUTER JOIN ");
        sqlFrom.append(_getCollection() + T_CACPTA);
        sqlFrom.append(ON);
        sqlFrom.append("(" + _getCollection() + T_CACPTA + "." + F_CACPTA_NUMAFFILIE + "=" + _getCollection() + T_AFF
                + "." + F_AFF_NUMAFFILIE + " AND " + _getCollection() + T_CACPTA + ".IDROLE = "
                + CEAffiliationService.getRoleForAffilieParitaire(getSession()) + ")");
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

        if (!JadeStringUtil.isBlank(getFromNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    F_AFF_NUMAFFILIE + " >= " + _dbWriteString(statement.getTransaction(), getFromNumAffilie()));
        }

        if (!JadeStringUtil.isBlank(getToNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    F_AFF_NUMAFFILIE + " <= " + _dbWriteString(statement.getTransaction(), getFromNumAffilie()));
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
        return new CEEmployeurMasseSalariale();
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

    private String giveColonneMasseSalariale(final int annee) {

        String idRubrique = CEUtils.getIdRubrique(getSession());

        return "(SELECT SUM(CUMULMASSE) " + "FROM " + _getCollection() + T_CACPTR + " " + "WHERE ANNEE = "
                + String.valueOf(annee) + " AND " + F_CACPTR_IDRUBRIQUE + " IN( " + idRubrique + ")   AND "
                + _getCollection() + T_CACPTR + "." + F_IDCOMPTEANNEXE + "=" + _getCollection() + T_CACPTA + "."
                + F_IDCOMPTEANNEXE + ")";
    }

    private String giveColonneNbCI(final int annee) {
        return "(SELECT COUNT(*) FROM " + _getCollection() + "CIECRIP " + "INNER JOIN " + _getCollection()
                + "CIINDIP ON " + _getCollection() + "CIINDIP.KAIIND = " + _getCollection() + "CIECRIP.kaiind "
                + "WHERE kbnann = " + annee + " AND " + _getCollection() + "CIECRIP.KBITIE = " + _getCollection()
                + "AFAFFIP.maiaff)";
    }

    private String giveColonneMasseSalarialeAF(final int annee) {

        return "(SELECT SUM(CUMULMASSE) " + "FROM " + _getCollection() + T_CACPTR + " " + "WHERE ANNEE = "
                + String.valueOf(annee) + " AND " + F_CACPTR_IDRUBRIQUE + " IN( SELECT IDRUBRIQUE FROM  "
                + _getCollection() + "CARUBRP RUB JOIN " + _getCollection()
                + "AFASSUP ASS ON ASS.MBIRUB = RUB.IDRUBRIQUE "
                + "WHERE ASS.MBTTYP = 812002 AND ASS.MBTGEN = 801001)  AND " + _getCollection() + T_CACPTR + "."
                + F_IDCOMPTEANNEXE + "=" + _getCollection() + T_CACPTA + "." + F_IDCOMPTEANNEXE + ")";
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
