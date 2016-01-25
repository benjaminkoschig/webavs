package globaz.ij.vb.basesindemnisation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisationManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFormulaireIndemnisationListViewBean extends IJFormulaireIndemnisationManager implements
        FWViewBeanInterface {

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
        return new IJFormulaireIndemnisationViewBean();
    }
}
