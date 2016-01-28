package globaz.apg.vb.annonces;

import globaz.apg.db.annonces.APBreakRule;
import globaz.apg.db.annonces.APBreakRuleManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author dde
 */
public class APBreakRuleListViewBean extends APBreakRuleManager implements FWListViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APBreakRule();
    }

}
