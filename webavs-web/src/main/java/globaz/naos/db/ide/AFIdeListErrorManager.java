package globaz.naos.db.ide;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import java.io.Serializable;

public class AFIdeListErrorManager extends BManager implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFIdeListErrorAnnonce();
    }

}
