package globaz.pavo.process.ree;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;

public class CIEcritureReeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = new String();

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        // Fields sur la table affiliation
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIINDIP.KANAVS");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIINDIP.KALNOM");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIINDIP.KATSEX");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIINDIP.KADNAI");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIINDIP.KAIPAY");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBMMON");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBNMOD");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBNMOF");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBTGEN");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBTEXT");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBTSPE");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIJOURP.KCID");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIJOURP.KCITIN");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBITIE");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBIPAR");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBICHO");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "AFAFFIP.MALNAF");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBLIB");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBIAFF");
        CEUtils.sqlAddField(sqlFields, "CIINDIPPA.KANAVS AS PARTENAIRE_KANAVS");
        CEUtils.sqlAddField(sqlFields, "CIINDIPPA.KALNOM AS PARTENAIRE_KALNOM");
        CEUtils.sqlAddField(sqlFields, _getCollection() + "CIECRIP.KBNANN");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + "CIECRIP");

        sqlFrom.append(" inner join " + _getCollection() + "CIINDIP on " + _getCollection() + "CIECRIP.KAIIND="
                + _getCollection() + "CIINDIP.KAIIND");
        sqlFrom.append(" inner join " + _getCollection() + "CIJOURP on " + _getCollection() + "CIECRIP.KCID="
                + _getCollection() + "CIJOURP.KCID ");
        sqlFrom.append(" left outer join " + _getCollection() + "AFAFFIP on " + _getCollection() + "CIECRIP.KBITIE="
                + _getCollection() + "AFAFFIP.MAIAFF ");
        sqlFrom.append(" left outer join " + _getCollection() + "TITIERP on " + _getCollection() + "AFAFFIP.HTITIE="
                + _getCollection() + "TITIERP.HTITIE ");
        sqlFrom.append(" left outer join " + _getCollection() + "CIINDIP AS CIINDIPPA on " + _getCollection()
                + "CIECRIP.KBIPAR=CIINDIPPA.KAIIND ");
        sqlFrom.append(" left outer join " + _getCollection() + "CIREMAP on " + _getCollection() + "CIECRIP.KIIREM="
                + _getCollection() + "CIREMAP.KIIREM ");
        sqlFrom.append(" left outer join " + _getCollection() + "CIRAOUP on " + _getCollection() + "CIECRIP.KKIRAO="
                + _getCollection() + "CIRAOUP.KKIRAO");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return _getCollection() + "CIINDIP.KANAVS";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        CEUtils.sqlAddCondition(sqlWhere, _getCollection() + "CIECRIP.KBTCPT<>303005");
        CEUtils.sqlAddCondition(sqlWhere,
                "(KBTGEN IN (310001,310003, 310009) OR (KBTGEN = 310007 and KBTSPE IN (312002,312003)))");
        CEUtils.sqlAddCondition(sqlWhere, "KBTCPT IN (303001,303002,303004)");
        CEUtils.sqlAddCondition(sqlWhere, _getCollection() + "CIECRIP.KBNANN=" + getForAnnee());

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIEcritureRee();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }
}
