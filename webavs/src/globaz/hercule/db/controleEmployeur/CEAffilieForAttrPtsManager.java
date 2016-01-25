package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;

/**
 * @author SCO
 * @since 6 sept. 2010
 */
public class CEAffilieForAttrPtsManager extends BManager {

    private static final long serialVersionUID = 8223114364820913771L;
    private String forIdAffiliation;
    private String forIdControle;
    private String likeNumeroAffilie;

    @Override
    protected String _getFields(final BStatement statement) {

        StringBuffer sqlFields = new StringBuffer();

        CEUtils.sqlAddField(sqlFields, "AFF.MAIAFF");
        CEUtils.sqlAddField(sqlFields, "AFF.MALNAF");
        CEUtils.sqlAddField(sqlFields, "AFF.MATTAF");
        CEUtils.sqlAddField(sqlFields, "AFF.MADDEB");
        CEUtils.sqlAddField(sqlFields, "AFF.MADFIN");
        CEUtils.sqlAddField(sqlFields, "AFF.MATCDN");
        CEUtils.sqlAddField(sqlFields, "AFF.MATBRA");
        CEUtils.sqlAddField(sqlFields, "TIERS.HTITIE");
        CEUtils.sqlAddField(sqlFields, "TIERS.HTLDE1");
        CEUtils.sqlAddField(sqlFields, "TIERS.HTLDE2");
        CEUtils.sqlAddField(sqlFields, "CONT.MDICON");
        CEUtils.sqlAddField(sqlFields, "CONT.MDDCDE");
        CEUtils.sqlAddField(sqlFields, "CONT.MDDCFI");
        CEUtils.sqlAddField(sqlFields, "CONT.MDDPRE");
        CEUtils.sqlAddField(sqlFields, "CONT.MDDEFF");
        CEUtils.sqlAddField(sqlFields, "CONT.MDLNAF");
        CEUtils.sqlAddField(sqlFields, "COUP.PCOUID AS CODESUVA");
        CEUtils.sqlAddField(sqlFields, "COUP.PCOLUT AS LIBELLESUVA");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(final BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + "AFAFFIP AFF");
        sqlFrom.append(" INNER JOIN " + _getCollection() + "TITIERP TIERS ON AFF.HTITIE = TIERS.HTITIE");
        sqlFrom.append(" INNER JOIN " + _getCollection() + "CECONTP CONT ON CONT.HTITIE = TIERS.HTITIE");
        sqlFrom.append(" LEFT JOIN " + _getCollection()
                + "FWCOUP as coup on (AFF.MATSUV = coup.pcosid AND coup.PLAIDE='" + getSession().getIdLangue() + "') ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getGroupBy(final BStatement statement) {

        StringBuffer sqlGroupeBy = new StringBuffer();

        CEUtils.sqlAddField(sqlGroupeBy, "AFF.MAIAFF");
        CEUtils.sqlAddField(sqlGroupeBy, "AFF.MALNAF");
        CEUtils.sqlAddField(sqlGroupeBy, "AFF.MATTAF");
        CEUtils.sqlAddField(sqlGroupeBy, "AFF.MADDEB");
        CEUtils.sqlAddField(sqlGroupeBy, "AFF.MADFIN");
        CEUtils.sqlAddField(sqlGroupeBy, "TIERS.HTITIE");
        CEUtils.sqlAddField(sqlGroupeBy, "TIERS.HTLDE1");
        CEUtils.sqlAddField(sqlGroupeBy, "TIERS.HTLDE2");
        CEUtils.sqlAddField(sqlGroupeBy, "CONT.MDDCDE");
        CEUtils.sqlAddField(sqlGroupeBy, "CONT.MDDCFI");
        CEUtils.sqlAddField(sqlGroupeBy, "CONT.MDDPRE");

        return sqlGroupeBy.toString();
    }

    @Override
    protected String _getOrder(final BStatement statement) {
        return "AFF.MALNAF";
    }

    @Override
    protected String _getWhere(final BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        // affiliés employeurs
        CEUtils.sqlAddCondition(sqlWhere, "AFF.MATTAF IN (" + CodeSystem.TYPE_AFFILI_EMPLOY + ", "
                + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ")");

        if (!JadeStringUtil.isBlank(getLikeNumeroAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "AFF.MALNAF LIKE " + this._dbWriteString(statement.getTransaction(), getLikeNumeroAffilie() + "%"));
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdAffiliation())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "AFF.MAIAFF = " + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliation()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdControle())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "CONT.MDICON = " + this._dbWriteNumeric(statement.getTransaction(), getForIdControle()));
        }

        CEUtils.sqlAddCondition(sqlWhere, "CONT.MDBFDR = '1'");
        CEUtils.sqlAddCondition(sqlWhere, "CONT.MDDEFF <> 0");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEAffilieForAttrPts();
    }

    /**
     * Getter de forIdAffiliation
     * 
     * @return the forIdAffiliation
     */
    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    /**
     * Setter de forIdAffiliation
     * 
     * @param forIdAffiliation the forIdAffiliation to set
     */
    public void setForIdAffiliation(final String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    /**
     * Getter de forIdControle
     * 
     * @return the forIdControle
     */
    public String getForIdControle() {
        return forIdControle;
    }

    /**
     * Setter de forIdControle
     * 
     * @param forIdControle the forIdControle to set
     */
    public void setForIdControle(final String forIdControle) {
        this.forIdControle = forIdControle;
    }

    /**
     * Getter de likeNumeroAffilie
     * 
     * @return the likeNumeroAffilie
     */
    public String getLikeNumeroAffilie() {
        return likeNumeroAffilie;
    }

    /**
     * Setter de likeNumeroAffilie
     * 
     * @param likeNumeroAffilie the likeNumeroAffilie to set
     */
    public void setLikeNumeroAffilie(final String likeNumeroAffilie) {
        this.likeNumeroAffilie = likeNumeroAffilie;
    }

}
