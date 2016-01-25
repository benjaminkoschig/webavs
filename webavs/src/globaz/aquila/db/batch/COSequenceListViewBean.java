package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COSequenceManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COSequenceListViewBean extends COSequenceManager implements FWListViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    private String forIdSequence = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COSequenceViewBean();
    }

    @Override
    public String getForIdSequence() {
        return forIdSequence;
    }

    @Override
    public void setForIdSequence(String forIdSequence) {
        this.forIdSequence = forIdSequence;
    }
}
