package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * @author MMO
 * @since 12 aout 2010
 */
public class CEAffilieManager extends BManager {

    private static final long serialVersionUID = -7921486665272126029L;

    public static final String F_AFF_DDEB = AFAffiliation.FIELDNAME_AFF_DDEBUT;
    public static final String F_AFF_DFIN = AFAffiliation.FIELDNAME_AFF_DFIN;
    public static final String F_AFF_NUMAFFILIE = "MALNAF";
    public static final String F_AFF_TYPE = AFAffiliation.FIELDNAME_AFFILIATION_TYPE;
    public static final String F_IDAFFILIATION = AFAffiliation.FIELDNAME_AFFILIATION_ID;
    public static final String F_CODE_SUVA = "MATSUV";
    public static final String F_IDTIER = "HTITIE";
    public static final String F_TIER_COMPLEMENTNOM = "HTLDE2";
    public static final String F_TIER_NOM = "HTLDE1";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LIKE = " LIKE ";
    public static final String ON = " ON ";
    public static final String T_AFF = AFAffiliation.TABLE_NAME;
    public static final String T_TIER = "TITIERP";

    // Attributs
    private String forIdAffiliation;
    private String likeNumeroAffilie;
    private String sansDateFinAff;

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        CEUtils.sqlAddField(sqlFields, F_IDAFFILIATION);
        CEUtils.sqlAddField(sqlFields, F_AFF_NUMAFFILIE);
        CEUtils.sqlAddField(sqlFields, F_AFF_TYPE);
        CEUtils.sqlAddField(sqlFields, F_AFF_DDEB);
        CEUtils.sqlAddField(sqlFields, F_AFF_DFIN);
        CEUtils.sqlAddField(sqlFields, _getCollection() + T_TIER + "." + F_IDTIER);
        CEUtils.sqlAddField(sqlFields, F_TIER_NOM);
        CEUtils.sqlAddField(sqlFields, F_TIER_COMPLEMENTNOM);
        CEUtils.sqlAddField(sqlFields, F_CODE_SUVA);

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + T_AFF);
        sqlFrom.append(INNER_JOIN);
        sqlFrom.append(_getCollection() + T_TIER);
        sqlFrom.append(ON);
        sqlFrom.append(_getCollection() + T_AFF + "." + F_IDTIER + "=" + _getCollection() + T_TIER + "." + F_IDTIER);

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return F_AFF_NUMAFFILIE;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        // affiliés employeurs
        CEUtils.sqlAddCondition(sqlWhere, F_AFF_TYPE + " IN (" + CodeSystem.TYPE_AFFILI_EMPLOY + ", "
                + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ")");

        if (!JadeStringUtil.isBlank(getLikeNumeroAffilie())) {
            likeNumeroAffilie = CEUtils.formatNumeroAffilie(getSession(), likeNumeroAffilie);
            CEUtils.sqlAddCondition(sqlWhere,
                    F_AFF_NUMAFFILIE + LIKE + _dbWriteString(statement.getTransaction(), getLikeNumeroAffilie() + "%"));
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdAffiliation())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    F_IDAFFILIATION + " = " + _dbWriteNumeric(statement.getTransaction(), getForIdAffiliation()));
        }

        // Le boolean est stocké sous forme de string car c'est une limitation
        // du widget qui n'accepte que ce format la.
        if (!JadeStringUtil.isBlankOrZero(getSansDateFinAff())) {
            if (Boolean.parseBoolean(getSansDateFinAff())) {
                CEUtils.sqlAddCondition(sqlWhere, F_AFF_DFIN + " = 0");
            }
        }
        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEAffilie();
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    public String getSansDateFinAff() {
        return sansDateFinAff;
    }

    public void setForIdAffiliation(String newForIdAffiliation) {
        forIdAffiliation = newForIdAffiliation;
    }

    public void setLikeNumeroAffilie(String newLikeNumeroAffilie) {
        likeNumeroAffilie = newLikeNumeroAffilie;
    }

    public void setSansDateFinAff(String sansDateFinAff) {
        this.sansDateFinAff = sansDateFinAff;
    }

}
