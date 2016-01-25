package globaz.corvus.vb.documents;

import globaz.babel.db.copies.CTCopiesManager;
import globaz.globall.db.BEntity;

public class RECopiesListViewBean extends CTCopiesManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RECopiesViewBean();
    }

}
