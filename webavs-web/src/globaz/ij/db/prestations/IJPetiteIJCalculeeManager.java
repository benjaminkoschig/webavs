package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPetiteIJCalculeeManager extends IJIJCalculeeManager {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return IJPetiteIJCalculee.createFromClause(_getCollection());
    }

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
        return new IJPetiteIJCalculee();
    }
}
