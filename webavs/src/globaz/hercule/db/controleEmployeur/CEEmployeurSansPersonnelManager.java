package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.db.groupement.CEGroupe;
import globaz.hercule.db.groupement.CEMembre;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * @author MMO
 * @since 30 juillet 2010
 */
public class CEEmployeurSansPersonnelManager extends BManager {

    private static final long serialVersionUID = -694593490108010615L;
    public static final String AS = " AS ";

    public static final String F_AFF_DDEB = AFAffiliation.FIELDNAME_AFF_DDEBUT;
    public static final String F_AFF_DFIN = AFAffiliation.FIELDNAME_AFF_DFIN;
    public static final String F_AFF_NUMAFFILIE = "MALNAF";
    public static final String F_AFF_TYPE = AFAffiliation.FIELDNAME_AFFILIATION_TYPE;
    public static final String F_IDAFFILIATION = AFAffiliation.FIELDNAME_AFFILIATION_ID;
    public static final String F_IDGROUPE = CEGroupe.FIELD_IDGROUPE;
    public static final String F_IDPARTICULARITE = "MFTPAR";

    public static final String F_IDTIER = "HTITIE";

    public static final String F_NOMGROUPE = CEGroupe.FIELD_LIBELLE;
    public static final String F_PARTICULARITE_DDEB = "MFDDEB";
    public static final String F_TIER_COMPLEMENTNOM = "HTLDE2";
    public static final String F_TIER_NOM = "HTLDE1";
    public static final String FROM = " FROM ";

    public static final String GROUP_BY = " GROUP BY ";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String ON = " ON ";

    public static final String PARTICULARITE_SANS_PERSONNEL = CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL;

    public static final String SELECT = " SELECT ";

    public static final String T_AFF = AFAffiliation.TABLE_NAME;
    public static final String T_AFFPART = "AFPARTP";
    public static final String T_GROUPE = CEGroupe.TABLE_CEGRPP;
    public static final String T_MEMBRE = CEMembre.TABLE_CEMEMP;
    public static final String T_TIER = "TITIERP";
    public static final String WHERE = " WHERE ";

    private String forAnnee = "";

    @Override
    protected String _getFields(final BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        CEUtils.sqlAddField(sqlFields, _getCollection() + T_AFF + "." + F_IDAFFILIATION);
        CEUtils.sqlAddField(sqlFields, F_AFF_NUMAFFILIE);
        CEUtils.sqlAddField(sqlFields, _getCollection() + T_TIER + "." + F_IDTIER);
        CEUtils.sqlAddField(sqlFields, F_TIER_NOM);
        CEUtils.sqlAddField(sqlFields, F_TIER_COMPLEMENTNOM);
        CEUtils.sqlAddField(sqlFields, F_AFF_DDEB);
        CEUtils.sqlAddField(sqlFields, F_AFF_DFIN);
        CEUtils.sqlAddField(sqlFields, F_PARTICULARITE_DDEB);
        CEUtils.sqlAddField(sqlFields, F_NOMGROUPE);
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
        sqlFrom.append(INNER_JOIN);
        sqlFrom.append(_getCollection() + T_AFFPART);
        sqlFrom.append(ON);
        sqlFrom.append(_getCollection() + T_AFFPART + "." + F_IDAFFILIATION + "=" + _getCollection() + T_AFF + "."
                + F_IDAFFILIATION);
        sqlFrom.append(LEFT_JOIN);
        sqlFrom.append(_getCollection() + T_MEMBRE);
        sqlFrom.append(ON);
        sqlFrom.append(_getCollection() + T_MEMBRE + "." + F_IDAFFILIATION + "=" + _getCollection() + T_AFF + "."
                + F_IDAFFILIATION);
        sqlFrom.append(LEFT_JOIN);
        sqlFrom.append(_getCollection() + T_GROUPE);
        sqlFrom.append(ON);
        sqlFrom.append(_getCollection() + T_GROUPE + "." + F_IDGROUPE + "=" + _getCollection() + T_MEMBRE + "."
                + F_IDGROUPE);
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

        if (!JadeStringUtil.isBlank(getForAnnee())) {
            /**
             * affiliations ouvertes en getForAnnee() une affiliation radiée durant 2010 n'est pas considérée comme
             * ouverte en 2010
             */
            CEUtils.sqlAddCondition(sqlWhere,
                    F_AFF_DDEB + " <= " + _dbWriteDateAMJ(statement.getTransaction(), "31.12." + getForAnnee()));
            CEUtils.sqlAddCondition(sqlWhere,
                    "(" + F_AFF_DFIN + " > " + _dbWriteDateAMJ(statement.getTransaction(), "31.12." + getForAnnee())
                            + " OR " + F_AFF_DFIN + " = 0 )");

            /**
             * affiliés avec une particularité sans personnel dont l'année de la date de début correspond à
             * getForAnnee()
             */
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    F_IDPARTICULARITE + " = "
                            + _dbWriteNumeric(statement.getTransaction(), PARTICULARITE_SANS_PERSONNEL));
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    F_PARTICULARITE_DDEB + " <= "
                            + _dbWriteDateAMJ(statement.getTransaction(), "31.12." + getForAnnee()));
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    F_PARTICULARITE_DDEB + " >= "
                            + _dbWriteDateAMJ(statement.getTransaction(), "01.01." + getForAnnee()));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEEmployeurSansPersonnel();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(final String string) {
        forAnnee = string;
    }

}
