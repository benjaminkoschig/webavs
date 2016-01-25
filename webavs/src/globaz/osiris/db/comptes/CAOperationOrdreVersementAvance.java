package globaz.osiris.db.comptes;

import globaz.globall.db.BTransaction;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreVersementAvance;

/**
 * @author dda
 */
public class CAOperationOrdreVersementAvance extends CAOperationOrdreVersement implements
        APIOperationOrdreVersementAvance {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAOperationOrdreVersementAvance() {
        super();
        setIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE);
    }

    public CAOperationOrdreVersementAvance(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperationOrdreVersement#_beforeAdd(globaz. globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);

        setIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE);
    }

}
