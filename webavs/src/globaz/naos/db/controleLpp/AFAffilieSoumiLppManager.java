package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.util.AFUtil;
import globaz.pavo.db.compte.CIEcriture;

/**
 * 
 * @author sco
 * @since 08 sept. 2011
 */
public class AFAffilieSoumiLppManager extends BManager {

    // SELECT
    // af.MAIAFF,af.MALNAF,ecr.KBMMON,ci.KANAVS,ci.KALNOM,ci.KATSEX,ci.KADNAI,ecr.KBNMOD,
    // ecr.KBNMOF,ecr.KBTGEN,ecr.KAIIND,suiv.MYDFIN,af.MADFIN from CCJUWEB.AFAFFIP af
    // LEFT OUTER JOIN (
    // SELECT * FROM CCJUWEB.AFSUAFP
    // WHERE MYTGEN = 830003 AND MYDFIN > 0
    // ) suiv on suiv.MAIAFF = af.MAIAFF
    // LEFT OUTER JOIN (
    // SELECT * FROM CCJUWEB.AFSUAFP
    // WHERE MYTGEN = 830003 AND MYDFIN = 0
    // ) suiv2 on suiv2.MAIAFF = af.MAIAFF
    // INNER JOIN CCJUWEB.CIECRIP ecr on ecr.KBITIE = af.MAIAFF
    // LEFT JOIN CCJUWEB.CIINDIP ci on ecr.KAIIND = ci.KAIIND
    // WHERE ecr.KBNANN = 2010
    // AND (suiv.MYISUA IS NULL or (suiv.MYTGEN = 830003 and suiv.MYDFIN < af.MADFIN and suiv.MYDFIN > 0) or
    // (suiv.MYTGEN = 830003 and suiv.MYDFIN > 0 and af.MADFIN = 0))
    // AND suiv2.MYISUA is null
    // AND af.MATTAF in (804002, 804012, 804005)
    // AND (KBTGEN IN (310001 ,310006) OR (KBTGEN = 310007 and KBTSPE = (312003)))
    // AND KBTCPT IN (303001,303002,303004);

    private static final long serialVersionUID = -7937459372384591359L;
    private String forAnnee;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sqlFields = new StringBuilder("");

        AFUtil.sqlAddField(sqlFields, "af.HTITIE");
        AFUtil.sqlAddField(sqlFields, "af.MAIAFF");
        AFUtil.sqlAddField(sqlFields, "af.MALNAF");
        AFUtil.sqlAddField(sqlFields, "af.MATSEC");
        AFUtil.sqlAddField(sqlFields, "ecr.KBMMON");
        AFUtil.sqlAddField(sqlFields, "ci.KANAVS");
        AFUtil.sqlAddField(sqlFields, "ci.KALNOM");
        AFUtil.sqlAddField(sqlFields, "ci.KATSEX");
        AFUtil.sqlAddField(sqlFields, "ci.KADNAI");
        AFUtil.sqlAddField(sqlFields, "ci.KATSEC");
        AFUtil.sqlAddField(sqlFields, "ecr.KBNMOD");
        AFUtil.sqlAddField(sqlFields, "ecr.KBNMOF");
        AFUtil.sqlAddField(sqlFields, "ecr.KBTGEN");
        AFUtil.sqlAddField(sqlFields, "ecr.KAIIND");
        AFUtil.sqlAddField(sqlFields, "suiv.MYDFIN");
        AFUtil.sqlAddField(sqlFields, "af.MADFIN");
        AFUtil.sqlAddField(sqlFields, "ecr.KBTEXT");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + "AFAFFIP af ");

        sqlFrom.append("LEFT OUTER JOIN ( SELECT * FROM " + _getCollection()
                + "AFSUAFP	WHERE MYTGEN = 830003 AND (MYDFIN > " + getForAnnee() + "0101 OR (MYDDEB < " + getForAnnee()
                + "1231 AND MYDFIN = 0))) suiv on suiv.MAIAFF = af.MAIAFF ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "CIECRIP ecr on ecr.KBITIE = af.MAIAFF ");
        sqlFrom.append("LEFT JOIN " + _getCollection() + "CIINDIP ci on ecr.KAIIND = ci.KAIIND ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder();

        // WHERE ecr.KBNANN = 2010
        AFUtil.sqlAddCondition(sqlWhere, "ecr.KBNANN = " + getForAnnee());

        // AND (suiv.MYISUA IS NULL or (suiv.MYTGEN = 830003 and suiv.MYDFIN < af.MADFIN and suiv.MYDFIN > 0) or
        // (suiv.MYTGEN = 830003 and suiv.MYDFIN > 0 and af.MADFIN = 0))
        AFUtil.sqlAddCondition(sqlWhere, "suiv.MYISUA IS NULL ");

        // AND af.MATTAF in (804002, 804012, 804005)
        AFUtil.sqlAddCondition(sqlWhere, "af.MATTAF in (804002, 804012, 804005)");

        // AND (KBTGEN IN (310001 ,310006) OR (KBTGEN = 310007 and KBTSPE = (312003)))
        AFUtil.sqlAddCondition(sqlWhere, "(KBTGEN IN (" + CIEcriture.CS_CIGENRE_1 + " ," + CIEcriture.CS_CIGENRE_6
                + ") OR (KBTGEN = " + CIEcriture.CS_CIGENRE_7 + " and KBTSPE = (312003)))");

        // AND KBTCPT IN (303001,303002,303004)
        AFUtil.sqlAddCondition(sqlWhere, "KBTCPT IN (303001,303002,303004)");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffilieSoumiLpp();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

}
