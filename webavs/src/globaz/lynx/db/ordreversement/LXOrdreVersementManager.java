package globaz.lynx.db.ordreversement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXOrdreVersementManager extends LXOperationManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<String> forCsTypeOperationIn;

    /**
     * Renvoie la clause de tri
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer tmp = new StringBuffer(super._getOrder(statement));

        if (tmp.length() != 0) {
            tmp.append(" , ");
        }
        tmp.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_NUMTRANSACTION).append(" asc ");

        return tmp.toString();
    }

    /**
     * @see globaz.lynx.db.facture.LXOperationManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer(super._getWhere(statement));

        if (getForCsTypeOperationIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSTYPEOPERATION, getForCsTypeOperationIn()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.lynx.db.paiement.LXOperationManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXOrdreVersement();
    }

    public ArrayList<String> getForCsTypeOperationIn() {
        return forCsTypeOperationIn;
    }

    public void setForCsTypeOperationIn(ArrayList<String> forCsTypeOperationIn) {
        this.forCsTypeOperationIn = forCsTypeOperationIn;
    }
}
