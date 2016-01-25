package globaz.pavo.db.bta;

import globaz.framework.bean.FWListViewBeanInterface;

public class CIDossierBtaListViewBean extends CIDossierBtaManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void getDossierBta(CIDossierBtaViewBean viewBean) {
        try {
            setSession(viewBean.getSession());
            this.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
