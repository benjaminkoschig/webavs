package globaz.osiris.db.utils;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class CARemarqueManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CARemarque.TABLE_CAREMAP;
    }

    /**
     * @see BManager#_getOrder(BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * @see BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        return "";
    }

    /**
     * @see BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CARemarque();
    }
}
