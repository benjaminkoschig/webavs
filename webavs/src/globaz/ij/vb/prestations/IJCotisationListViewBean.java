package globaz.ij.vb.prestations;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.ij.db.prestations.IJCotisationManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJCotisationListViewBean extends IJCotisationManager implements FWListViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJCotisationViewBean();
    }
}
