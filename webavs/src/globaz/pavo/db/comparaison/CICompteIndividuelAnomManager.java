package globaz.pavo.db.comparaison;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;

public class CICompteIndividuelAnomManager extends BManager {

    private static final long serialVersionUID = -8114962616061937435L;

    public CICompteIndividuelAnomManager() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CICompteIndividuelAnom();
    }
}
