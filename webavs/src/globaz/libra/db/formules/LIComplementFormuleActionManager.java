package globaz.libra.db.formules;

import globaz.envoi.db.parametreEnvoi.access.ENComplementFormuleManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;

/**
 * @author hpe
 * 
 */
public class LIComplementFormuleActionManager extends ENComplementFormuleManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_beforeFind(BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);
        setForCsTypeFormule(LIComplementFormuleAction.CS_FAMILLE);
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LIComplementFormuleAction();
    }

}