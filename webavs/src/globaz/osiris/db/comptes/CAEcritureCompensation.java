package globaz.osiris.db.comptes;

import globaz.globall.db.BTransaction;
import globaz.osiris.api.APIOperation;

/**
 * @author dda
 */
public class CAEcritureCompensation extends CAEcriture {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CAEcriture.
     */
    public CAEcritureCompensation() {
        super();
        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CAECRITURECOMPENSATION);
    }

    public CAEcritureCompensation(CAOperation parent) {
        super(parent);

        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CAECRITURECOMPENSATION);
    }

    /**
     * @see globaz.osiris.db.comptes.CAEcriture#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);

        setIdTypeOperation(APIOperation.CAECRITURECOMPENSATION);
    }

}
