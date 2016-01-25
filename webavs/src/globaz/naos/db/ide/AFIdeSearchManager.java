package globaz.naos.db.ide;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import java.io.Serializable;

public class AFIdeSearchManager extends BManager implements Serializable {

    private static final long serialVersionUID = -6424923065533092073L;
    private String forNumeroIDE;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFIdeSearch();
    }

    public String getForNumeroIDE() {
        return forNumeroIDE;
    }

    public void setForNumeroIDE(String forNumeroIDE) {
        this.forNumeroIDE = forNumeroIDE;
    }

}
