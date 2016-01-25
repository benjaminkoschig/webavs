package globaz.osiris.db.comptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;

public class CAOperationOrdreVersementManager extends CAOperationOrdreManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        if (JadeStringUtil.isIntegerEmpty(getForIdTypeOperation())
                && JadeStringUtil.isIntegerEmpty(getLikeIdTypeOperation())) {
            setLikeIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);
        }

        String sqlWhere = super._getWhere(statement);

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAOperationOrdreVersement();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:26:37)
     * 
     * @return boolean
     */
    public boolean isForEstBloque() {
        return (forEstBloque != null) ? forEstBloque.booleanValue() : false;
    }
}
