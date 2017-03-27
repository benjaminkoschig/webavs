/*
 * Globaz SA
 */
package globaz.corvus.db.deblocage;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class REDeblocageVersementManager extends BManager {

    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDeblocageVersement();
    }

    @Override
    protected String _getSql(BStatement statement) {

        return super._getSql(statement);
    }

}
