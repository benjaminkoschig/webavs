package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;

public class CASoldeSectionUntilDateManager extends CAEcritureManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFields(BStatement statement) {
        return "SUM(" + _getCollection() + "CAOPERP.MONTANT) AS SOLDESSECTION";
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CASoldeSectionUntilDate();
    }

}
