package globaz.lynx.db.societesdebitrice;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class LXSocieteDebitriceListViewBean extends LXSocieteDebitriceManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public LXSocieteDebitriceListViewBean() {
        super();
    }

    /**
     * @see globaz.lynx.db.societesdebitrice.LXSocieteDebitriceManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXSocieteDebitriceViewBean();
    }

}
