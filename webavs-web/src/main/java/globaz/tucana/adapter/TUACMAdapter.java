package globaz.tucana.adapter;

import globaz.globall.db.BTransaction;
import globaz.itucana.adapter.ICommonAdapter;
import globaz.itucana.constantes.TUTypesBouclement;
import globaz.itucana.exception.TUProcessCSTucanaInterfaceException;
import globaz.itucana.exception.TUProcessFWTucanaInterfaceException;
import globaz.itucana.exception.TUProcessTucanaInterfaceException;
import globaz.itucana.model.ITUEntete;
import java.util.Iterator;

/**
 * Adapteur pour les ACM
 * 
 * @author fgo date de création : 7 août
 * @version : version 1.0
 * 
 */
public class TUACMAdapter extends TUCommonAdapter implements ICommonAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.adapter.TUCommonAdapter#handleEntete(globaz.globall.db. BTransaction,
     * globaz.itucana.model.ITUEntete)
     */
    @Override
    protected String handleEntete(BTransaction transaction, ITUEntete entete) throws TUProcessTucanaInterfaceException {
        return super.addBouclement(transaction, TUTypesBouclement.BOUCLEMENT_ACM, entete);

    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.tucana.adapter.TUCommonAdapter#handleLines(globaz.globall.db. BTransaction, java.util.Iterator,
     * java.lang.String, java.lang.String)
     */
    @Override
    protected void handleLines(BTransaction transaction, Iterator lignes, String idBouclement, String noPassage)
            throws TUProcessTucanaInterfaceException {
        try {
            super.addLignes(transaction, TUTypesBouclement.BOUCLEMENT_ACM, lignes, idBouclement, noPassage);
        } catch (TUProcessFWTucanaInterfaceException e) {
            throw new TUProcessTucanaInterfaceException(e);
        } catch (TUProcessCSTucanaInterfaceException e) {
            throw new TUProcessTucanaInterfaceException(e);
        }
    }
}
