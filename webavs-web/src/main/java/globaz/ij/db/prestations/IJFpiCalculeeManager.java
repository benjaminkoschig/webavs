package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 *
 * @author ebko
 */
public class IJFpiCalculeeManager extends IJIJCalculeeManager {

     private static final long serialVersionUID = 1L;

    @Override
    protected String _getFrom(BStatement statement) {
        return IJFpiCalculee.createFromClause(_getCollection());
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJFpiCalculee();
    }
}
