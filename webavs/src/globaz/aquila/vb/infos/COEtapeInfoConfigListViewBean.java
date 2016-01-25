package globaz.aquila.vb.infos;

import globaz.aquila.db.access.batch.COEtapeInfoConfigManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COEtapeInfoConfigListViewBean extends COEtapeInfoConfigManager implements FWListViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
	 */
    private static final long serialVersionUID = 621963398062559836L;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        COEtapeInfoConfigViewBean configViewBean = new COEtapeInfoConfigViewBean();

        configViewBean.wantLoadEtape(true);

        return configViewBean;
    }
}
