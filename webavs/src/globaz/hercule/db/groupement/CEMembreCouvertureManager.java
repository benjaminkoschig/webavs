package globaz.hercule.db.groupement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.db.couverture.CECouverture;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author SCO
 * @since 04 aout 2011
 */
public class CEMembreCouvertureManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdGroupe = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        CEUtils.sqlAddField(sqlFields, "membre." + CEMembre.FIELD_IDAFFILIATION);
        CEUtils.sqlAddField(sqlFields, CECouverture.FIELD_NUMAFFILIE);
        CEUtils.sqlAddField(sqlFields, CECouverture.FIELD_ANNEE);

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + CEMembre.TABLE_CEMEMP + " as membre ");
        sqlFrom.append(" left join " + _getCollection() + CECouverture.TABLE_CECOUVP + " as couverture on couverture."
                + CECouverture.FIELD_IDAFFILIE + " = membre." + CEMembre.FIELD_IDAFFILIATION);

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isEmpty(getForIdGroupe())) {
            CEUtils.sqlAddCondition(sqlWhere, CEGroupe.FIELD_IDGROUPE + " = " + getForIdGroupe());
        }

        CEUtils.sqlAddCondition(sqlWhere, CECouverture.FIELD_COUVERTUREACTIVE + "='1'");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEMembreCouverture();
    }

    public String getForIdGroupe() {
        return forIdGroupe;
    }

    public void setForIdGroupe(String forIdGroupe) {
        this.forIdGroupe = forIdGroupe;
    }
}
