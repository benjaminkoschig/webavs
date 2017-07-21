/*
 * Globaz SA.
 */
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

    private static final long serialVersionUID = -7937459372384591359L;
    private int forAnnee;

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
        AFUtil.sqlAddField(sqlFields, "af.MADFIN");
        AFUtil.sqlAddField(sqlFields, "ecr.KBTEXT");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sqlFrom = new StringBuilder();

        sqlFrom.append(_getCollection() + "AFAFFIP af ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "CIECRIP ecr on ecr.KBITIE = af.MAIAFF ");
        sqlFrom.append("LEFT JOIN " + _getCollection() + "CIINDIP ci on ecr.KAIIND = ci.KAIIND ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder();

        // WHERE ecr.KBNANN = 2010
        AFUtil.sqlAddCondition(sqlWhere, "ecr.KBNANN = " + getForAnnee());

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

    public int getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(int forAnnee) {
        this.forAnnee = forAnnee;
    }

}
