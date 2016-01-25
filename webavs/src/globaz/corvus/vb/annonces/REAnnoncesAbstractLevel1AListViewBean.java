package globaz.corvus.vb.annonces;

import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1AManager;
import globaz.globall.db.BEntity;

public class REAnnoncesAbstractLevel1AListViewBean extends REAnnoncesAbstractLevel1AManager {

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
        return new REAnnoncesAbstractLevel1AViewBean();
    }

    @Override
    public String getOrderBy() {
        return super.getOrderBy() + REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A;
    }

    @Override
    public String getOrderByDefaut() {
        return REAnnoncesAbstractLevel1A.FIELDNAME_ID_ANNONCE_ABS_LEV_1A + " DESC";
    }

}
