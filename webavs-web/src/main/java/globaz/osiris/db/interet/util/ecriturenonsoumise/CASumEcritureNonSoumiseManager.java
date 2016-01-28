package globaz.osiris.db.interet.util.ecriturenonsoumise;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;

public class CASumEcritureNonSoumiseManager extends CAEcritureNonSoumiseManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "sum(a." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASumEcritureNonSoumise();
    }

    @Override
    protected StringBuffer getGroupBySql() {
        return new StringBuffer();
    }

    @Override
    protected String getOrderSql() {
        return new String();
    }
}
