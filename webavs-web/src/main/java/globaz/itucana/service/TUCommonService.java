package globaz.itucana.service;

import globaz.globall.db.BTransaction;
import globaz.itucana.adapter.ICommonAdapter;
import globaz.itucana.exception.TUInterfaceException;
import globaz.itucana.model.ITUModelBouclement;

/**
 * Classe communes pour les appels aux services
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public abstract class TUCommonService {
    private ICommonAdapter commonAdapter = null;

    protected TUCommonService(ICommonAdapter _commonAdapter) {
        super();
        commonAdapter = _commonAdapter;
    }

    /**
     * Récupère un adapteur
     * 
     * @return
     */
    protected ICommonAdapter getAdapter() {
        return commonAdapter;
    }

    public abstract void handleBouclement(BTransaction transaction, ITUModelBouclement modelBouclement)
            throws TUInterfaceException;

}
