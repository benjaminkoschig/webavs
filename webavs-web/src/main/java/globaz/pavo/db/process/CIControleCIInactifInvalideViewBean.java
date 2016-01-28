package globaz.pavo.db.process;

import globaz.pavo.vb.CIAbstractPersistentViewBean;
import ch.globaz.common.business.exceptions.CommonTechnicalException;

/**
 * @author mmo
 */
public class CIControleCIInactifInvalideViewBean extends CIAbstractPersistentViewBean {

    @Override
    public void add() throws Exception {
        throw new CommonTechnicalException("Not implemented");

    }

    @Override
    public void delete() throws Exception {
        throw new CommonTechnicalException("Not implemented");
    }

    @Override
    public void retrieve() throws Exception {
        // Rien à faire
    }

    @Override
    public void update() throws Exception {
        throw new CommonTechnicalException("Not implemented");

    }

}
