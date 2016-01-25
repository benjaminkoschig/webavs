package globaz.itucana.service;

import globaz.globall.db.BTransaction;
import globaz.itucana.adapter.ICommonAdapter;
import globaz.itucana.exception.TUInterfaceException;
import globaz.itucana.model.ITUModelBouclement;

/**
 * Classe de service CG permettant de récupérer les models CG
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public class TUCGService extends TUCommonService {

    /**
     * @param cgAdapter
     */
    protected TUCGService(ICommonAdapter adapter) {
        super(adapter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itucana.service.TUCommonService#handleBouclement(globaz.globall .db.BTransaction,
     * globaz.itucana.model.ICommonModelBouclement)
     */
    @Override
    public void handleBouclement(BTransaction transaction, ITUModelBouclement modelBouclement)
            throws TUInterfaceException {
        getAdapter().handleBouclement(transaction, modelBouclement);
    }

}
