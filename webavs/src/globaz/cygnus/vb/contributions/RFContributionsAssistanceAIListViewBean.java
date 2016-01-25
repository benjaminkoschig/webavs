package globaz.cygnus.vb.contributions;

import globaz.cygnus.db.contributions.RFContributionsAssistanceAIManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author PBA
 */
public class RFContributionsAssistanceAIListViewBean extends RFContributionsAssistanceAIManager implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFContributionsAssistanceAIListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFContributionsAssistanceAIDetailViewBean();
    }
}
