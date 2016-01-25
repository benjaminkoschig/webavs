package globaz.osiris.db.bulletinneutre;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CABulletinNeutreTaxeSommationManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forMasseBetween;

    /**
     * @see BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CABulletinNeutreTaxeSommation.TABLE_CABNTXP;
    }

    /**
     * @see BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String where = super._getWhere(statement);

        if (!JadeStringUtil.isBlank(getForMasseBetween())) {
            if (!JadeStringUtil.isBlank(where)) {
                where += " AND ";
            }

            where += getForMasseBetween() + " >= " + CABulletinNeutreTaxeSommation.FIELD_MASSESALARIALEFROM;
            where += " and (";
            where += getForMasseBetween() + " < " + CABulletinNeutreTaxeSommation.FIELD_MASSESALARIALETO;
            where += " or ";
            where += CABulletinNeutreTaxeSommation.FIELD_MASSESALARIALETO + " = 0";
            where += ")";
        }

        return where;
    }

    /**
     * @see BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CABulletinNeutreTaxeSommation();
    }

    public String getForMasseBetween() {
        return forMasseBetween;
    }

    public void setForMasseBetween(String forMasseBetween) {
        this.forMasseBetween = forMasseBetween;
    }

}
