package globaz.lynx.db.canevas;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class LXCanevasVentilationListViewBean extends LXCanevasVentilationManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.lynx.db.canevas.LXCanevasVentilationManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXCanevasVentilationViewBean();
    }
}
