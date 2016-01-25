package globaz.itucana.adapter;

import globaz.globall.db.BTransaction;
import globaz.itucana.exception.TUProcessTucanaInterfaceException;
import globaz.itucana.model.ITUModelBouclement;

/**
 * Adaptateur commun pour bouclement
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public interface ICommonAdapter {

    /**
     * Génère le bouclement
     * 
     * @param transaction
     * @param modelBouclement
     * @throws TUProcessTucanaInterfaceException
     */
    public void handleBouclement(BTransaction transaction, ITUModelBouclement modelBouclement)
            throws TUProcessTucanaInterfaceException;
}
