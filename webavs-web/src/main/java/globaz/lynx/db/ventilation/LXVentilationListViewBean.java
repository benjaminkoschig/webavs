package globaz.lynx.db.ventilation;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class LXVentilationListViewBean extends LXVentilationManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.lynx.db.ventilation.LXVentilationManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXVentilationViewBean();
    }

}
