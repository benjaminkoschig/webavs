package globaz.pavo.process.ree;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class AFLienReeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFLIENP";
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFLienRee();
    }

}
