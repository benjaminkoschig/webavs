package globaz.osiris.db.comptes;

import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISlave;
import globaz.osiris.api.APIVersementAvance;

/**
 * @author dda
 */
public class CAVersementAvance extends CAVersement implements APIVersementAvance {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAVersementAvance() {
        super();
        setIdTypeOperation(APIOperation.CAVERSEMENTAVANCE);
    }

    public CAVersementAvance(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAVERSEMENTAVANCE);
    }

    /**
	 * 
	 */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);

        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CAVERSEMENTAVANCE);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 17:39:24)
     */
    @Override
    protected APISlave createSlave() {
        CAVersementAvance ecr = new CAVersementAvance();
        ecr.dupliquer(this);
        return ecr;
    }
}
