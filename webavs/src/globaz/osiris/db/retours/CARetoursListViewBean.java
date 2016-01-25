package globaz.osiris.db.retours;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;

public class CARetoursListViewBean extends CARetoursManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CARetoursListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CARetoursViewBean();
    }

    public String getTotalRetoursLot() {

        if (!JadeStringUtil.isIntegerEmpty(getForIdLot())) {
            CARetoursManager rManager = new CARetoursManager();
            rManager.setSession(getSession());
            rManager.setForIdLot(getForIdLot());
            try {
                return new FWCurrency(rManager.getSum(CARetours.FIELDNAME_MONTANT_RETOUR).toString()).toStringFormat();
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
}
